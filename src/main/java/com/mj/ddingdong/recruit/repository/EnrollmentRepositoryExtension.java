package com.mj.ddingdong.recruit.repository;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.recruit.domain.Recruit;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface EnrollmentRepositoryExtension {

    long isAlreadyEnrolled(Account account, Recruit recruit);

}
