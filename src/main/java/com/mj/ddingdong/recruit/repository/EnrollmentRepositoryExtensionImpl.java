package com.mj.ddingdong.recruit.repository;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Activity;
import com.mj.ddingdong.recruit.domain.Enrollment;
import com.mj.ddingdong.recruit.domain.QEnrollment;
import com.mj.ddingdong.recruit.domain.Recruit;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class EnrollmentRepositoryExtensionImpl extends QuerydslRepositorySupport implements EnrollmentRepositoryExtension  {

    public EnrollmentRepositoryExtensionImpl() {
        super(Enrollment.class);
    }

    @Override
    public long isAlreadyEnrolled(Account account, Recruit recruit) {
        QEnrollment enrollment = QEnrollment.enrollment;
        JPQLQuery<Enrollment> query = from(enrollment).where(enrollment.account.id.eq(account.getId())
                .and(enrollment.recruit.id.eq(recruit.getId())));

        return query.fetchCount();
    }

    @Override
    public List<Enrollment> findbyRecruit(Recruit recruit) {
        QEnrollment enrollment = QEnrollment.enrollment;
        JPQLQuery<Enrollment> query = from(enrollment).where(enrollment.recruit.id.eq(recruit.getId()));

        return query.fetch();
    }
}
