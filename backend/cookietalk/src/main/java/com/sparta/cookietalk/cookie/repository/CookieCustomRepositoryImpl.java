package com.sparta.cookietalk.cookie.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import lombok.RequiredArgsConstructor;
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

//    @Override
//    public Page<List> getCookieListByChannelId(Long channelId, Pageable pageRequest) {
//        return null;
//    }
}
