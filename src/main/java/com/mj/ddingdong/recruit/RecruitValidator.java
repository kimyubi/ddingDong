package com.mj.ddingdong.recruit;

import com.mj.ddingdong.recruit.domain.Recruit;
import com.mj.ddingdong.recruit.form.RecruitForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RecruitValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(RecruitForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RecruitForm recruitForm = (RecruitForm) o;


        if (isNotValidPublishRecruitTime(recruitForm)) {
            errors.rejectValue("publishRecruitTime", "wrong.publishRecruitTime", "공고 게시 일시는 현재 시각 이후로 설정해주세요.");
        }

        if (isNotValidStartRecruitTime(recruitForm)) {
            errors.rejectValue("startRecruitTime", "wrong.startRecruitTime", "모집 시작 일시를 정확히 입력하세요.");
        }

        if (isNotValidEndDateTime(recruitForm)) {
            errors.rejectValue("endRecruitTime", "wrong.endRecruitTime", "모집 종료 일시를 정확히 입력하세요.");
        }
    }

    private boolean isNotValidPublishRecruitTime(RecruitForm recruitForm) {
        return recruitForm.getPublishRecruitTime().isBefore(LocalDateTime.now());
    }

    private boolean isNotValidStartRecruitTime(RecruitForm recruitForm) {
        return recruitForm.getStartRecruitTime().isBefore(recruitForm.getPublishRecruitTime());
    }

    private boolean isNotValidEndDateTime(RecruitForm recruitForm) {
        LocalDateTime endRecruitTime = recruitForm.getEndRecruitTime();
        return endRecruitTime.isBefore(recruitForm.getStartRecruitTime()) || endRecruitTime.isBefore(recruitForm.getPublishRecruitTime());
    }
}
