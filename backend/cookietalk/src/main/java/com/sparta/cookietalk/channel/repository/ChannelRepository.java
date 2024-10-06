package com.sparta.cookietalk.channel.repository;

import com.sparta.cookietalk.channel.entity.Channel;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    // 여기 profileImage 문제 같음
    @Query("SELECT c FROM Channel c JOIN FETCH c.user WHERE c.user.id = :id")
    Optional<Channel> findChannelWithUserByUserId(@Param("id")Long userId);

    @Query("SELECT c FROM Channel c JOIN FETCH c.user WHERE c.id = :id")
    Optional<Channel> findChannelWithUserById(@Param("id") Long channelId);
}
