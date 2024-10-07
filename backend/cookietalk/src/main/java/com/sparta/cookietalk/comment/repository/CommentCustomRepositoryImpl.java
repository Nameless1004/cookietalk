package com.sparta.cookietalk.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.cookietalk.comment.dto.CommentResponse;
import com.sparta.cookietalk.comment.entity.QComment;
import com.sparta.cookietalk.common.dto.Response;
import com.sparta.cookietalk.common.dto.Response.Page;
import com.sparta.cookietalk.cookie.entity.QCookie;
import com.sparta.cookietalk.user.entity.QUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommentResponse.List> getCommentsByCookieId(Pageable pageable, Long cookieId) {
        QCookie cookie = QCookie.cookie;
        QComment comment = QComment.comment;
        QUser user = QUser.user;
        List<CommentResponse.List> fetch = queryFactory.select(Projections.constructor(CommentResponse.List.class,
            user.id,
            user.nickname,
            comment.id,
            comment.contents,
            comment.createdAt))
            .distinct()
            .from(comment)
            .leftJoin(comment.cookie, cookie).on(cookie.id.eq(cookieId))
            .leftJoin(comment.authUser, user)
            .orderBy(comment.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory.select(Wildcard.count)
            .distinct()
            .from(comment)
            .join(comment.cookie, cookie).on(cookie.id.eq(cookieId))
            .leftJoin(comment.authUser, user)
            .fetchOne();

        return Response.Page.of(fetch, count, pageable);
    }
}
