package com.mj.ddingdong.notification;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.main.CurrentAccount;
import com.mj.ddingdong.notification.domain.Notification;
import com.mj.ddingdong.notification.repository.NotificationRepository;
import com.mj.ddingdong.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @GetMapping("/notification")
    public String getNotifications(@CurrentAccount Account account, Model model) {
        List<Notification> notifications = notificationRepository.findByAccountAndCheckedOrderByCreatedTimeDesc(account, false);
        long numberOfChecked = notificationRepository.countByAccountAndChecked(account, true);
        ClassifyNotifications(model, notifications, numberOfChecked, notifications.size());
        model.addAttribute("isNew", true);
        model.addAttribute(account);
        notificationService.changeCheckedToTrue(notifications);
        return "notification/list";
    }

    @GetMapping("/notification/old")
    public String getOldNotifications(@CurrentAccount Account account, Model model) {
        List<Notification> notifications = notificationRepository.findByAccountAndCheckedOrderByCreatedTimeDesc(account, true);
        long numberOfNotChecked = notificationRepository.countByAccountAndChecked(account, false);
        ClassifyNotifications(model, notifications, notifications.size(), numberOfNotChecked);
        model.addAttribute("isNew", false);
        model.addAttribute(account);
        return "notification/list";
    }

    @PostMapping("/notification")
    public String deleteNotifications(@CurrentAccount Account account) {
        notificationRepository.deleteByAccountAndChecked(account, true);
        return "redirect:/notification";
    }


    private void ClassifyNotifications(Model model, List<Notification> notifications, long numberOfChecked, long numberOfNotChecked) {
        List<Notification> circleCreatedNotifications = new ArrayList<>();
        List<Notification> recruitStartedNotification = new ArrayList<>();

        for (Notification notification : notifications) {
            switch (notification.getNotificationType()) {
                case CIRCLE_CREATED: circleCreatedNotifications.add(notification); break;
                case RECRUIT_STARTED: recruitStartedNotification.add(notification); break;
            }
        }

        model.addAttribute("numberOfNotChecked", numberOfNotChecked);
        model.addAttribute("numberOfChecked", numberOfChecked);
        model.addAttribute("notifications", notifications);
        model.addAttribute("circleCreatedNotifications", circleCreatedNotifications);
        model.addAttribute("recruitStartedNotification", recruitStartedNotification);
    }

}
