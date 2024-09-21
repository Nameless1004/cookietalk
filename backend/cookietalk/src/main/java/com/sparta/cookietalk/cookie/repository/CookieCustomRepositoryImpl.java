package com.sparta.cookietalk.cookie.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.cookietalk.category.dto.QCategoryInfo;
import com.sparta.cookietalk.category.entity.QCategory;
import com.sparta.cookietalk.category.entity.QCookieCategory;
import com.sparta.cookietalk.cookie.dto.CookieResponse;
import com.sparta.cookietalk.cookie.dto.CookieResponse.Detail;
import com.sparta.cookietalk.cookie.dto.CookieResponse.List;
import com.sparta.cookietalk.cookie.entity.QCookie;
import com.sparta.cookietalk.upload.QUploadFile;
import com.sparta.cookietalk.user.dto.QCreatorInfo;
import com.sparta.cookietalk.user.entity.QUser;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<List> getCookieListByChannelId(Long channelId, Pageable pageRequest) {
        return null;
    }
}
