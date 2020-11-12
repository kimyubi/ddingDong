package com.mj.ddingdong.circle.repository;

import com.mj.ddingdong.circle.domain.Circle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface CircleRepository extends JpaRepository<Circle,Long>, CircleRepositoryExtension {
    boolean existsByPath(String path);

    boolean existsByTitle(String title);

    Circle findByPath(String path);

    Circle findCircleById(Long id);

    Page<Circle> findByKeyword(String keyword, Pageable pageable);

    List<Circle> findFirst9OrderByCreatedTimeDesc();

}
