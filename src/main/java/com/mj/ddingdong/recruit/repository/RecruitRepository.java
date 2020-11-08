package com.mj.ddingdong.recruit.repository;

import com.mj.ddingdong.recruit.domain.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface RecruitRepository extends JpaRepository<Recruit,Long> {


}
