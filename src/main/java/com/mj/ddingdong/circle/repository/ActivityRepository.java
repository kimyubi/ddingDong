package com.mj.ddingdong.circle.repository;

import com.mj.ddingdong.circle.domain.Activity;
import com.mj.ddingdong.circle.domain.Circle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Repository
public interface ActivityRepository extends JpaRepository<Activity,Long>, ActivityRepositoryExtension {

    Page<Activity> findByCircle(Circle circle, Pageable pageable);

    List<Activity> findByCircle(Circle circle);
}
