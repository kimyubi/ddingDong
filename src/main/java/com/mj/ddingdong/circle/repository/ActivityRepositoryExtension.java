package com.mj.ddingdong.circle.repository;

import com.mj.ddingdong.circle.domain.Activity;
import com.mj.ddingdong.circle.domain.Circle;
import com.querydsl.core.QueryResults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ActivityRepositoryExtension {

    Page<Activity> findByCircle(Circle circle, Pageable pageable);

    List<Activity> findByCircle(Circle circle);



}
