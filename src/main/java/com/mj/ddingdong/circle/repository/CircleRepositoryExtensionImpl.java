package com.mj.ddingdong.circle.repository;

import com.mj.ddingdong.account.domain.Account;
import com.mj.ddingdong.circle.domain.Circle;
import com.mj.ddingdong.circle.domain.QCircle;
import com.mj.ddingdong.tag.domain.FieldTag;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CircleRepositoryExtensionImpl extends QuerydslRepositorySupport implements CircleRepositoryExtension{

    public CircleRepositoryExtensionImpl() {
        super(Circle.class);
    }

    @Override
    public Page<Circle> findByKeyword(String keyword, Pageable pageable) {
        QCircle circle = QCircle.circle;
        JPQLQuery<Circle> query = from(circle).where(circle.title.contains(keyword).or(circle.field.contains(keyword)));

        JPQLQuery<Circle> pageableQuery = getQuerydsl().applyPagination(pageable, query);
        QueryResults<Circle> circleQueryResults = pageableQuery.fetchResults();

        return new PageImpl<>(circleQueryResults.getResults(),pageable, circleQueryResults.getTotal());
    }

    @Override
    public List<Circle> findFirst9OrderByCreatedTimeDesc() {
        QCircle circle = QCircle.circle;
        JPQLQuery<Circle> query = from(circle).orderBy(circle.createdTime.desc()).limit(9);

        return query.fetch();
    }

    @Override
    public List<Circle> findFirst12OrderByCreatedTimeDesc() {
        QCircle circle = QCircle.circle;
        JPQLQuery<Circle> query = from(circle).orderBy(circle.createdTime.desc()).limit(12);

        return query.fetch();
    }


    @Override
    public List<Circle> findByManager(Account account) {
        QCircle circle = QCircle.circle;
        JPQLQuery<Circle> query = from(circle).where(circle.managers.contains(account));
        return query.fetch();
    }

    @Override
    public List<Circle> findByMember(Account account) {
        QCircle circle = QCircle.circle;

        JPQLQuery<Circle> query = from(circle).where(circle.members.contains(account));
        return query.fetch();
    }
}
