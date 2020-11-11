package com.mj.ddingdong.circle.event;
import com.mj.ddingdong.circle.domain.Circle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Slf4j
public class CircleEventListener {

    @EventListener
    public void circleCreatedEventHandler(CircleCreatedEvent circleCreatedEvent){
        Circle circle = circleCreatedEvent.getCircle();
        log.info(circle.getTitle()+" is created");

    }
}
