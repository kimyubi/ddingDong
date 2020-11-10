package com.mj.ddingdong.circle.service;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Activity;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.form.CircleForm;
import com.mj.ddingdong.circle.repository.ActivityRepository;
import com.mj.ddingdong.circle.repository.CircleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CircleService {

    private final CircleRepository circleRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final ActivityRepository activityRepository;

    public Circle saveCircle(Account account, CircleForm circleForm) {
        validateAccountManager(account);
        Circle circle = modelMapper.map(circleForm, Circle.class);
        circle.getManagers().add(account);

        return circleRepository.save(circle);
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
}
