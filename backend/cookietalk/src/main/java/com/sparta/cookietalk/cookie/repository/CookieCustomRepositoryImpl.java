package com.sparta.cookietalk.cookie.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.cookietalk.category.entity.QCookieCategory;
import com.sparta.cookietalk.channel.entity.QChannel;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import com.sparta.cookietalk.cookie.entity.QCookie;
import com.sparta.cookietalk.upload.QUploadFile;
import com.sparta.cookietalk.user.entity.QUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

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
    public Page<Detail> findAllCookiesByChannelId(Long channelId, Pageable pageable) {
        QUser user = QUser.user;
        QCookie cookie = QCookie.cookie;
        QChannel channel = QChannel.channel;
        QUploadFile video = new QUploadFile("videoFile");
        QUploadFile thumbnail = new QUploadFile("thumbnailFile");
        QUploadFile attachment = new QUploadFile("attachmentFile");
        QCookieCategory cookieCategory = QCookieCategory.cookieCategory;

        System.out.println("pageable = " + pageable.getOffset());
        System.out.println("pageable = " + pageable.getPageSize());
        System.out.println("pageable = " + pageable.getPageNumber());
        List<Detail> fetch = queryFactory.
            select(Projections.constructor(Detail.class,
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
                cookie.createdAt))
            .distinct()
            .from(cookie)
            .join(cookie.channel, channel).on(channel.id.eq(channelId))
            .join(channel.user, user)
            .join(cookie.videoFile, video)
            .join(cookie.thumbnailFile, thumbnail)
            .leftJoin(cookie.attachmentFile, attachment)
            .orderBy(cookie.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        System.out.println(fetch.size());
        Long count = queryFactory.select(Wildcard.count)
            .from(cookie)
            .innerJoin(cookie.channel, channel).on(channel.id.eq(channelId))
            .fetchOne();


        return new PageImpl<>(fetch, pageable, count == null ? 0 : count);
    }


//    @Override
//    public Page<List> getCookieListByChannelId(Long channelId, Pageable pageRequest) {
//        return null;
//    }
}
