package com.sparta.cookietalk.cookie.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.cookietalk.category.entity.QCategory;
import com.sparta.cookietalk.category.entity.QCookieCategory;
import com.sparta.cookietalk.channel.entity.QChannel;
import com.sparta.cookietalk.common.dto.Response;
import com.sparta.cookietalk.common.enums.ProcessStatus;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieSearch;
import com.sparta.cookietalk.cookie.entity.QCookie;
import com.sparta.cookietalk.cookie.entity.QUserRecentCookie;
import com.sparta.cookietalk.upload.QUploadFile;
import com.sparta.cookietalk.user.entity.QUser;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class CookieCustomRepositoryImpl implements CookieCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public CookieResponse.Detail getCookieDetails(Long id){
        QCookie cookie = QCookie.cookie;
        QChannel channel = QChannel.channel;
        QUser user = QUser.user;
        QUploadFile video = new QUploadFile("videoFile");
        QUploadFile thumbnail = new QUploadFile("thumbnailFile");
        QUploadFile attachment = new QUploadFile("attachmentFile");
        QCategory category = QCategory.category;
        QCookieCategory cookieCategory = QCookieCategory.cookieCategory;

        CookieResponse.Detail c = queryFactory
            .select(Projections.constructor(CookieResponse.Detail.class,
                channel.id,
                user.id,
                user.nickname,
                cookie.id,
                cookie.proccessStatus,
                cookie.title,
                cookie.description,
                video.id,
                thumbnail.id,
                attachment.id,
                cookie.createdAt
                ))
            .distinct()
            .from(cookie)
            .innerJoin(cookie.channel, channel)
            .innerJoin(channel.user, user)
            .innerJoin(cookie.videoFile, video)
            .innerJoin(cookie.thumbnailFile, thumbnail)
            .leftJoin(cookie.attachmentFile, attachment)
            .where(cookie.id.eq(id))
            .fetchOne();
        return c;
    }

    @Override
    public List<CookieResponse.RecentList> getRecentCookies(List<Long> list) {
        QCookie cookie = QCookie.cookie;
        QChannel channel = QChannel.channel;
        QUser user = QUser.user;
        QUploadFile thumbnail = new QUploadFile("thumbnailFile");
        QUserRecentCookie userRecentCookie = QUserRecentCookie.userRecentCookie;

        return queryFactory
            .select(Projections.constructor(CookieResponse.RecentList.class,
                user.id,
                user.nickname,
                cookie.id,
                cookie.title,
                thumbnail.s3Url,
                userRecentCookie.viewAt))
            .from(userRecentCookie)
            .innerJoin(userRecentCookie.cookie, cookie)
            .innerJoin(cookie.channel, channel)
            .innerJoin(channel.user, user)
            .leftJoin(cookie.thumbnailFile, thumbnail)
            .where(cookie.id.in(list))
            .orderBy(userRecentCookie.viewAt.desc())
            .fetch();
    }

    @Override
    public Response.Page<CookieResponse.List> findCookieListByChannelId(Long channelId, Pageable pageable, boolean isMine) {
        QUser user = QUser.user;
        QCookie cookie = QCookie.cookie;
        QChannel channel = QChannel.channel;
        QUploadFile thumbnail = new QUploadFile("thumbnailFile");

        BooleanBuilder ex = new BooleanBuilder();

        // 내 채널이 아니면 ProcessStatus가 성공인 쿠키만 보여줘야 된다.
        if(!isMine) {
            ex.and(cookie.proccessStatus.eq(ProcessStatus.SUCCESS));
        }

        List<CookieResponse.List> fetch = queryFactory.
            select(Projections.constructor(CookieResponse.List.class,
                user.id,
                user.nickname,
                cookie.id,
                cookie.title,
                thumbnail.s3Url,
                cookie.proccessStatus,
                cookie.createdAt))
            .distinct()
            .from(channel)
            .join(channel.cookies, cookie)
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


        return Response.Page.of(fetch, count,pageable);
    }

    @Override
    public Response.Page<CookieResponse.List> searchCookieListByKeyword(Pageable pageable, CookieSearch search) {
        QUser user = QUser.user;
        QCookie cookie = QCookie.cookie;
        QChannel channel = QChannel.channel;
        QUploadFile thumbnail = new QUploadFile("thumbnailFile");

        List<CookieResponse.List> fetch = queryFactory.
            select(Projections.constructor(CookieResponse.List.class,
                user.id,
                user.nickname,
                cookie.id,
                cookie.title,
                thumbnail.s3Url,
                cookie.proccessStatus,
                cookie.createdAt))
            .distinct()
            .from(channel)
            .join(channel.cookies, cookie)
            .join(channel.user, user)
            .join(cookie.thumbnailFile, thumbnail)
            .where(cookie.proccessStatus.eq(ProcessStatus.SUCCESS).and(byKeyword(search.getKeyword())))
            .orderBy(cookie.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long count = queryFactory.select(Wildcard.count)
            .distinct()
            .from(cookie)
            .where(cookie.proccessStatus.eq(ProcessStatus.SUCCESS).and(byKeyword(search.getKeyword())))
            .fetchOne();

        return Response.Page.of(fetch, count,pageable);
    }

    @Override
    public Response.Slice<CookieResponse.List> getSliceByCategoryId(int size, LocalDateTime cursor,
        CookieSearch search) {
        QUser user = QUser.user;
        QCookie cookie = QCookie.cookie;
        QChannel channel = QChannel.channel;
        QUploadFile thumbnail = new QUploadFile("thumbnailFile");
        QCookieCategory cookieCategory = QCookieCategory.cookieCategory;
        QCategory category = QCategory.category;

        BooleanBuilder cursorBooleanBuilder = new BooleanBuilder();
        if(cursor != null) {
            cursorBooleanBuilder.and(cookie.createdAt.lt(cursor));
        }

        List<CookieResponse.List> fetch = queryFactory.
            select(Projections.constructor(CookieResponse.List.class,
                user.id,
                user.nickname,
                cookie.id,
                cookie.title,
                thumbnail.s3Url,
                cookie.proccessStatus,
                cookie.createdAt))
            .distinct()
            .from(cookie)
            .join(cookie.cookieCategories, cookieCategory)
            .join(cookieCategory.category, category)
            .join(cookie.thumbnailFile, thumbnail)
            .join(cookie.channel, channel)
            .join(channel.user, user)
            .where(cursorBooleanBuilder.and(cookie.proccessStatus.eq(ProcessStatus.SUCCESS).and(byCategory(search.getCategoryId()))))
            .orderBy(cookie.createdAt.desc())
            .limit(size + 1)
            .fetch();

        boolean hasNextPage = false;
        if(fetch.size() > size) {
            hasNextPage = true;
            fetch.remove(size);
        }

        return new Response.Slice<>(fetch, hasNextPage, fetch.size(), fetch.isEmpty() ? null : fetch.get(fetch.size() - 1).createdAt());
    }


    BooleanExpression byKeyword(String keyword) {
        if(!StringUtils.hasText(keyword)) {
            return null;
        }

        return QCookie.cookie.title.containsIgnoreCase(keyword);
    }

    BooleanExpression byCategory(Long categoryId) {
        if(categoryId == null) {
            return null;
        }
        return QCategory.category.id.eq(categoryId);
    }
}
