package com.mj.ddingdong.notification.service;

import com.mj.ddingdong.notification.domain.Notification;
import com.mj.ddingdong.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void changeCheckedToTrue(List<Notification> notifications) {
        notifications.forEach(notification -> notification.setChecked(true));
        notificationRepository.saveAll(notifications);
    }
}
