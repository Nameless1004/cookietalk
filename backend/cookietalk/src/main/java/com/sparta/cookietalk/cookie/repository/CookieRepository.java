package com.sparta.cookietalk.cookie.repository;

import com.sparta.cookietalk.cookie.entity.Cookie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CookieRepository extends JpaRepository<Cookie, Long>, CookieCustomRepository {

    @Query("SELECT e "
        + "FROM Cookie e "
        + "LEFT JOIN FETCH e.videoFile "
        + "LEFT JOIN FETCH e.thumbnailFile "
        + "LEFT JOIN FETCH e.attachmentFile "
        + "WHERE e.thumbnailFile.id = :uploadFileId OR e.videoFile.id = :uploadFileId OR e.attachmentFile.id = :uploadFileId")
    Cookie findByUploadFileId(@Param("uploadFileId")Long uploadFileId);
}
