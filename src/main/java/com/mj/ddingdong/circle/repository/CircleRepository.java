package com.mj.ddingdong.circle.repository;

import com.mj.ddingdong.circle.domain.Circle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CircleRepository extends JpaRepository<Circle,Long> {
    boolean existsByPath(String path);

    boolean existsByTitle(String title);

    Circle findByPath(String path);
}
