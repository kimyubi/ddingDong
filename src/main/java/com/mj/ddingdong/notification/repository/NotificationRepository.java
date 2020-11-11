package com.mj.ddingdong.notification.repository;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Long countByAccountAndChecked(Account account, boolean b);
}
