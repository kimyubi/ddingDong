package com.mj.ddingdong.account.repository;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.account.domain.QAccount;
import com.mj.ddingdong.circle.domain.Activity;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class AccountRepositoryExtensionImpl extends QuerydslRepositorySupport implements AccountRepositoryExtension {
    public AccountRepositoryExtensionImpl() {
        super(Account.class);
    }

    @Override
    public List<Account> findAllByField(String field) {
        QAccount account = QAccount.account;
        JPQLQuery<Account> query = from(account).where(account.FieldTags.any().title.eq(field));
        return query.fetch();
    }
}
