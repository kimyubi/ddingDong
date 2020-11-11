package com.mj.ddingdong.account.repository;

import com.mj.ddingdong.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account,Long>,AccountRepositoryExtension {
    boolean existsBySignUpId(String signUpId);

    boolean existsByNickname(String nickname);

    Account findBySignUpId(String signUpId);

    Account findByNickname(String nickname);

    boolean existsByEmail(String email);

    Account findByEmail(String email);

    boolean existsByToken(String token);

    Account findByToken(String token);

    List<Account> findAllByField(String field);
}
