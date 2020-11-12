package com.mj.ddingdong.circle.service;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Activity;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.event.CircleCreatedEvent;
import com.mj.ddingdong.circle.form.CircleForm;
import com.mj.ddingdong.circle.repository.ActivityRepository;
import com.mj.ddingdong.circle.repository.CircleRepository;
import com.mj.ddingdong.recruit.domain.Enrollment;
import com.mj.ddingdong.recruit.domain.Recruit;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CircleService {

    private final CircleRepository circleRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final ActivityRepository activityRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Circle saveCircle(Account account, CircleForm circleForm) {
        validateAccountManager(account);
        Circle circle = modelMapper.map(circleForm, Circle.class);
        circle.getManagers().add(account);
        circle.setCreatedTime(LocalDateTime.now());
        // 여기서 알림 보내기.
        circle = circleRepository.save(circle);
        applicationEventPublisher.publishEvent(new CircleCreatedEvent(circle));

        return circle;
    }

    public Circle validatePath(String path) {
        Circle circle = circleRepository.findByPath(path);
        if(circle == null){
            throw new IllegalArgumentException(path +"에 해당하는 동아리가 존재하지 않습니다.");
        }
        return circle;
    }

    public void validateAccountMemberOrManager(Account account, Circle circle) {
        if(!((account.isRecognizedManager()&&account.isSignUpAsManager())|| circle.isMember(account))){
            throw new AccessDeniedException("해당 기능을 이용하실 수 없습니다.");
        }
    }

    public void validateAccountManager(Account account) {
        if(!(account.isRecognizedManager()&&account.isRecognizedManager())){
            throw new AccessDeniedException("해당 기능을 이용하실 수 없습니다.");
        }
    }

    public boolean circleManagedByManager(Account account, Circle circle) {
        for(Account manager : circle.getManagers()){
            if(manager.getId().equals(account.getId())){
                return true;
            }
        }
        throw new AccessDeniedException("해당 기능을 이용하실 수 없습니다.");
    }

    public void saveActivity(Circle circle, Activity activity) {
        activity.setWritedTime(LocalDateTime.now());
        activity.setCircle(circle);
        activityRepository.save(activity);

        circle.getActivities().add(activity);
        circleRepository.save(circle);
    }


    public boolean isManagerToCircle(Account account, Circle circle) {
        for(Account manager : circle.getManagers()){
            if(manager.getId().equals(account.getId())){
                return true;
            }
        }
        return false;
    }

    public void passEnrollment(Circle circle, Enrollment enrollment) {
        if(!circle.isMember(enrollment.getAccount())) {
            circle.getMembers().add(enrollment.getAccount());
            circleRepository.save(circle);
        }

    }

    public boolean isManagerToCircleOrMine(Enrollment enrollment, Account account, Circle circle) {
        if(enrollment.getAccount().equals(account)|| isManagerToCircle(account,circle)){
            return true;
        }
        else{
            throw new AccessDeniedException("해당 기능을 이용하실 수 없습니다.");
        }
    }

    public void modifyCircle(Circle circle, CircleForm circleForm) {
        modelMapper.map(circleForm,circle);

    }

    public void updateBanner(Circle circle, String image) {
        circle.setBannerImage(image);
    }

    public void enableStudyBanner(Circle circle) {
        circle.setUseBanner(true);
    }

    public void disableStudyBanner(Circle circle) {
        circle.setUseBanner(false);
    }


    public void deleteCircle(Circle circle) {
        circleRepository.delete(circle);
    }
}
