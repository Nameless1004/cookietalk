package com.sparta.cookietalk.user.repository;

import com.sparta.cookietalk.auth.dto.AuthResponse.CheckNickname;
import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u JOIN FETCH u.channel WHERE u.username = :username")
    Optional<User> findUserWithChannelByUsername(@Param("username")String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    default User findByIdOrElseThrow(Long userId){
        return findById(userId).orElseThrow(() -> new InvalidRequestException("존재하지 않는 사용자입니다."));
    }

    Optional<User> findByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
