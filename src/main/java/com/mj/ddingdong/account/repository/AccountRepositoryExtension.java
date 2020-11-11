package com.mj.ddingdong.account.repository;

import com.mj.ddingdong.account.domain.Account;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface AccountRepositoryExtension {

    List<Account> findAllByField(String field);
}
