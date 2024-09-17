package com.sparta.cookietalk.upload;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.cookietalk.common.enums.UploadStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UploadFileCutomRepositoryImpl implements UploadFileCutomRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public UploadStatus findStatusById(long id) {
        QUploadFile u = QUploadFile.uploadFile;

        UploadStatus uploadStatus = queryFactory.select(u.status)
            .from(u)
            .where(u.id.eq(id))
            .fetchFirst();

        return uploadStatus;
    }
}
