package com.mj.ddingdong.circle.repository;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.tag.domain.FieldTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface CircleRepositoryExtension {

    Page<Circle> findByKeyword(String keyword, Pageable pageable);

    List<Circle> findFirst9OrderByCreatedTimeDesc();

    List<Circle> findFirst12OrderByCreatedTimeDesc();

    List<Circle> findByManager(Account account);

    List<Circle> findByMember(Account account);

}
