package com.mj.ddingdong.recruit.service;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.recruit.domain.Enrollment;
import com.mj.ddingdong.recruit.domain.Recruit;
import com.mj.ddingdong.recruit.form.RecruitForm;
import com.mj.ddingdong.recruit.repository.EnrollmentRepository;
import com.mj.ddingdong.recruit.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitService {

    private final RecruitRepository recruitRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final EnrollmentRepository enrollmentRepository;

    public Recruit createRecruit(RecruitForm recruitForm, Circle circle, Account account) {
        Recruit recruit = modelMapper.map(recruitForm, Recruit.class);
        recruit.getManagers().add(account);
        recruit.setCreateRecruitTime(LocalDateTime.now());
        recruit.setCircle(circle);

        recruitRepository.save(recruit);
        return recruit;
    }

    public void deleteRecruit(Circle circle, Recruit recruit) {
        circle.getRecruits().remove(recruit);
        List<Enrollment> enrollments= enrollmentRepository.findByRecruitId(recruit.getId());
        enrollmentRepository.deleteAll(enrollments);
        recruitRepository.delete(recruit);
    }

    public void modifyRecruit(Recruit recruit, RecruitForm recruitForm) {
        modelMapper.map(recruitForm,recruit);
    }
}
