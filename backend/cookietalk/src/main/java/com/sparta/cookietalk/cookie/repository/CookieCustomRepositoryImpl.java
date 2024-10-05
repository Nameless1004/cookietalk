package com.sparta.cookietalk.cookie.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.cookietalk.category.entity.QCategory;
import com.sparta.cookietalk.category.entity.QCookieCategory;
import com.sparta.cookietalk.channel.entity.QChannel;
import com.sparta.cookietalk.common.enums.ProcessStatus;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import com.sparta.cookietalk.cookie.dto.KeywordSearch;
import com.sparta.cookietalk.cookie.entity.QCookie;
import com.sparta.cookietalk.upload.QUploadFile;
import com.sparta.cookietalk.user.entity.QUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.ast.tree.predicate.BooleanExpressionPredicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class CookieCustomRepositoryImpl implements CookieCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public CookieResponse.Detail getCookieDetails(Long id){
//        QCookie cookie = QCookie.cookie;
////        QChannel channel
//        QUser user = QUser.user;
//        QUploadFile video = new QUploadFile("videoFile");
//        QUploadFile thumbnail = new QUploadFile("thumbnailFile");
//        QUploadFile attachment = new QUploadFile("attachmentFile");
//        QCategory category = QCategory.category;
//        QCookieCategory cookieCategory = QCookieCategory.cookieCategory;
//
//        CookieResponse.Detail c = queryFactory
//            .select(Projections.constructor(CookieResponse.Detail.class,
//                cookie.id,
//                cookie.title,
//                cookie.description,
//                video.id,
//                thumbnail.id,
//                attachment.id
//                ))
//            .from(cookie)
//            .innerJoin(cookieCategory).on(cookieCategory.cookie.eq(cookie))
//            .innerJoin(category).on(cookieCategory.category.eq(category))
//            .innerJoin(cookie.videoFile, video)
//            .innerJoin(cookie.thumbnailFile, thumbnail)
//            .leftJoin(cookie.attachmentFile, attachment)
//            .innerJoin(cookie.creator, user)
//            .where(cookie.id.eq(id))
//            .fetchOne();
//        return c;
        return null;
    }

    @Override
    public Page<CookieResponse.List> findCookieListByChannelId(Long channelId, Pageable pageable, boolean isMine) {
        QUser user = QUser.user;
        QCookie cookie = QCookie.cookie;
        QChannel channel = QChannel.channel;
        QUploadFile thumbnail = new QUploadFile("thumbnailFile");
        QCookieCategory cookieCategory = QCookieCategory.cookieCategory;
        QCategory category = QCategory.category;

        BooleanBuilder ex = new BooleanBuilder();

        // 내 채널이 아니면 ProcessStatus가 성공인 쿠키만 보여줘야 된다.
        if(!isMine) {
            ex.and(cookie.proccessStatus.eq(ProcessStatus.SUCCESS));
        }

        List<CookieResponse.List> fetch = queryFactory.
            select(Projections.constructor(CookieResponse.List.class,
                user.id,
                user.nickname,
                channel.id,
                category.id,
                category.name,
                cookie.id,
                cookie.title,
                cookie.description,
                thumbnail.s3Url,
                cookie.proccessStatus,
                cookie.createdAt))
            .distinct()
            .from(channel)
            .join(channel.cookies, cookie)
            .join(cookie.cookieCategories, cookieCategory)
            .join(cookieCategory.category, category)
            .join(channel.user, user)
            .join(cookie.thumbnailFile, thumbnail)
            .where(channel.id.eq(channelId).and(ex))
            .orderBy(cookie.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory.select(Wildcard.count)
            .distinct()
            .from(cookie)
            .innerJoin(cookie.channel, channel).on(channel.id.eq(channelId))
            .fetchOne();


        return new PageImpl<>(fetch, pageable, count == null ? 0 : count);
    }

    @Override
    public Page<CookieResponse.List> searchCookieList(Pageable pageable, KeywordSearch search) {
        QUser user = QUser.user;
        QCookie cookie = QCookie.cookie;
        QChannel channel = QChannel.channel;
        QUploadFile thumbnail = new QUploadFile("thumbnailFile");
        QCookieCategory cookieCategory = QCookieCategory.cookieCategory;
        QCategory category = QCategory.category;

        List<CookieResponse.List> fetch = queryFactory.
            select(Projections.constructor(CookieResponse.List.class,
                user.id,
                user.nickname,
                channel.id,
                category.id,
                category.name,
                cookie.id,
                cookie.title,
                cookie.description,
                thumbnail.s3Url,
                cookie.proccessStatus,
                cookie.createdAt))
            .distinct()
            .from(channel)
            .join(channel.cookies, cookie)
            .join(cookie.cookieCategories, cookieCategory)
            .join(cookieCategory.category, category)
            .join(channel.user, user)
            .join(cookie.thumbnailFile, thumbnail)
            .where(byKeyword(search.getKeyword()).and(cookie.proccessStatus.eq(ProcessStatus.SUCCESS)))
            .orderBy(cookie.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory.select(Wildcard.count)
            .distinct()
            .from(cookie)
            .where(byKeyword(search.getKeyword()).and(cookie.proccessStatus.eq(ProcessStatus.SUCCESS)))
            .fetchOne();


        return new PageImpl<>(fetch, pageable, count == null ? 0 : count);
    }

    BooleanExpression byKeyword(String keyword) {
        if(!StringUtils.hasText(keyword)) {
            return null;
        }

        return QCookie.cookie.title.containsIgnoreCase(keyword);
    }
}
