package com.sparta.cookietalk.user.repository;

import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.channel WHERE u.id = :id")
    Optional<User> findWithChannelById(@Param("id") Long id);

    @Query("SELECT u FROM User u JOIN FETCH u.channel WHERE u.username = :username")
    Optional<User> findWithChannelByUsername(@Param("username")String username);

    @Query("SELECT u FROM User u JOIN FETCH u.channel WHERE u.email = :email")
    Optional<User> findWithChannelByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u JOIN FETCH u.channel WHERE u.nickname = :nickname")
    Optional<User> findWithChannelByNickname(@Param("nickname") String nickname);

    default User findByIdOrElseThrow(Long userId){
        return findWithChannelById(userId).orElseThrow(() -> new InvalidRequestException("존재하지 않는 사용자입니다."));
    }

    Optional<User> findByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
