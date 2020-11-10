package com.mj.ddingdong.recruit.service;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.recruit.domain.Enrollment;
import com.mj.ddingdong.recruit.domain.Recruit;
import com.mj.ddingdong.recruit.form.EnrollmentForm;
import com.mj.ddingdong.recruit.form.RecruitForm;
import com.mj.ddingdong.recruit.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Service
@Transactional()
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public void enrollmentToRecruit(Account account, Circle circle, Recruit recruit, @Valid EnrollmentForm enrollmentForm) {
        Enrollment enrollment= Enrollment.builder()
                .account(account)
                .recruit(recruit)
                .applicationForm(enrollmentForm.getApplicationForm())
                .enrolledTime(LocalDateTime.now())
                .build();

        enrollmentRepository.save(enrollment);
    }
}
