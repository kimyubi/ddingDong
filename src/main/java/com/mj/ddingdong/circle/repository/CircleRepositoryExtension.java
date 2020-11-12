package com.mj.ddingdong.circle.repository;

import com.mj.ddingdong.circle.domain.Circle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface CircleRepositoryExtension {

    Page<Circle> findByKeyword(String keyword, Pageable pageable);
    List<Circle> findFirst9OrderByCreatedTimeDesc();
}
