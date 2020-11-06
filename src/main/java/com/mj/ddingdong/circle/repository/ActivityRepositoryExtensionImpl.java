package com.mj.ddingdong.circle.repository;

import com.mj.ddingdong.circle.domain.Activity;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.domain.QActivity;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ActivityRepositoryExtensionImpl extends QuerydslRepositorySupport implements ActivityRepositoryExtension {

    public ActivityRepositoryExtensionImpl() {
        super(Activity.class);
    }

    @Override
    public Page<Activity> findByCircle(Circle circle, Pageable pageable) {
        QActivity activity = QActivity.activity;
        JPQLQuery<Activity> query = from(activity).where(activity.circle.id.eq(circle.getId()))
                .orderBy(activity.writedTime.desc());

        JPQLQuery<Activity> pageableQuery = getQuerydsl().applyPagination(pageable,query);
        QueryResults<Activity> results = pageableQuery.fetchResults();

        return new PageImpl<>(results.getResults(),pageable, results.getTotal());
    }
}
