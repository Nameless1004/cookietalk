package com.sparta.cookietalk.cookie.repository;

import com.sparta.cookietalk.cookie.dto.CookieSearch;
import com.sparta.cookietalk.cookie.entity.Cookie;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CookieRepository extends JpaRepository<Cookie, Long>, CookieCustomRepository {

    @Query("SELECT c "
        + "FROM Cookie c "
        + "LEFT JOIN FETCH c.videoFile "
        + "LEFT JOIN FETCH c.thumbnailFile "
        + "LEFT JOIN FETCH c.attachmentFile "
        + "WHERE c.thumbnailFile.id = :uploadFileId OR c.videoFile.id = :uploadFileId OR c.attachmentFile.id = :uploadFileId")
    Cookie findByUploadFileId(@Param("uploadFileId")Long uploadFileId);

    @Query("SELECT c FROM Cookie c JOIN FETCH c.channel JOIN FETCH c.channel.user LEFT JOIN FETCH c.videoFile LEFT JOIN FETCH c.thumbnailFile LEFT JOIN FETCH c.attachmentFile WHERE c.id=:id")
    Optional<Cookie> findWithChannelAndUserAllUploadFileById(@Param("id") Long cookieId);
}
