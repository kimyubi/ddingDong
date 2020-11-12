package com.mj.ddingdong.recruit.repository;

import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.recruit.domain.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface RecruitRepository extends JpaRepository<Recruit,Long> {

    List<Recruit> findByCircleOrderByStartRecruitTime(Circle circle);

    Recruit findRecruitById(Long id);
}
