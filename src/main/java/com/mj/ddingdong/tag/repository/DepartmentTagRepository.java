package com.mj.ddingdong.tag.repository;

import com.mj.ddingdong.tag.domain.DepartmentTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public interface DepartmentTagRepository extends JpaRepository<DepartmentTag,Long> {
}
