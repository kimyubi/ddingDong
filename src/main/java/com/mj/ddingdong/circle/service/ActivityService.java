package com.mj.ddingdong.circle.service;

import com.mj.ddingdong.circle.domain.Activity;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.form.ActivityForm;
import com.mj.ddingdong.circle.repository.ActivityRepository;
import com.mj.ddingdong.circle.repository.CircleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final CircleRepository circleRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public void plusViewCount(Circle circle, Optional<Activity> activity) {
        activity.orElseThrow().setViewCount(activity.get().getViewCount() + 1);
        activityRepository.save(activity.get());

        circle.getActivities().add(activity.get());
        circleRepository.save(circle);
    }

    public void modifyActivity(Activity activity, ActivityForm activityForm) {
        modelMapper.map(activityForm, activity);
    }

    public void deleteActivity(Activity activity) {
        activityRepository.delete(activity);
    }
}
