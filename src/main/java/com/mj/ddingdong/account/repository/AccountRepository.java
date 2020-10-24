package com.mj.ddingdong.account.repository;

import com.mj.ddingdong.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account,Long> {
    boolean existsBySignUpId(String signUpId);
}
