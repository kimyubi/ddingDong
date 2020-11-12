package com.mj.ddingdong.notification;
import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.account.repository.AccountRepository;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.event.CircleCreatedEvent;
import com.mj.ddingdong.circle.repository.CircleRepository;
import com.mj.ddingdong.notification.domain.Notification;
import com.mj.ddingdong.notification.domain.NotificationType;
import com.mj.ddingdong.notification.repository.NotificationRepository;
import com.mj.ddingdong.recruit.domain.Recruit;
import com.mj.ddingdong.recruit.event.RecruitStartedEvent;
import com.mj.ddingdong.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
@Transactional
@Async
@Slf4j
public class EventListenerToNotification {

    private final CircleRepository circleRepository;
    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository;
    private final RecruitRepository recruitRepository;

    @EventListener
    public void circleCreatedEventHandler(CircleCreatedEvent circleCreatedEvent){
        Circle circle = circleRepository.findCircleById(circleCreatedEvent.getCircle().getId());
        Iterable<Account> accounts = accountRepository.findAllByField(circle.getField());
        accounts.forEach(account -> {
            if(account.isCirclesCreateNotification()){
                Notification notification = new Notification();
                notification.setTitle(circle.getTitle());
                notification.setAccount(account);
                notification.setLink("/circle/"+circle.getPath());
                notification.setChecked(false);
                notification.setCreatedTime(LocalDateTime.now());
                notification.setMessage(circle.getShortIntroduce());
                notification.setNotificationType(NotificationType.CIRCLE_CREATED);
                notificationRepository.save(notification);
            }
        });
    }

    @EventListener
    public void recruitStartedEventHandler(RecruitStartedEvent recruitStartedEvent){
        Circle circle = circleRepository.findCircleById(recruitStartedEvent.getRecruit().getCircle().getId());
        Iterable<Account> accounts = accountRepository.findAllByField(circle.getField());
        Recruit recruit = recruitRepository.findRecruitById(recruitStartedEvent.getRecruit().getId());
        accounts.forEach(account -> {
            if(account.isCirclesRestartNotification()){
                Notification notification = new Notification();
                notification.setTitle(recruit.getTitle());
                notification.setAccount(account);
                notification.setLink("/circle/"+circle.getPath()+"/recruit/detail?id="+recruit.getId());
                notification.setChecked(false);
                notification.setCreatedTime(LocalDateTime.now());
                notification.setMessage(recruit.getShortIntroduce());
                notification.setNotificationType(NotificationType.RECRUIT_STARTED);
                notificationRepository.save(notification);
            }
        });

    }

}
