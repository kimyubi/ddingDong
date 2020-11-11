package com.mj.ddingdong.circle.event;

import com.mj.ddingdong.circle.domain.Circle;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class CircleCreatedEvent {

    private Circle circle;

    public CircleCreatedEvent(Circle circle) {
        this.circle = circle;
    }
}
