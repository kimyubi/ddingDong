package com.mj.ddingdong.recruit.repository;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.recruit.domain.Enrollment;
import com.mj.ddingdong.recruit.domain.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long>, EnrollmentRepositoryExtension {

    long isAlreadyEnrolled(Account account, Recruit recruit);

    List<Enrollment> findByRecruitId(Long id);
}
