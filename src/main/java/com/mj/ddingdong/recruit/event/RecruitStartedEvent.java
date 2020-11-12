package com.mj.ddingdong.recruit.event;

import com.mj.ddingdong.recruit.domain.Recruit;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class RecruitStartedEvent{

    private Recruit recruit;

    public RecruitStartedEvent(Recruit recruit) {
        this.recruit = recruit;
    }
}
