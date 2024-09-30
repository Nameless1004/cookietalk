package com.sparta.cookietalk.user.repository;

import com.sparta.cookietalk.common.exceptions.InvalidRequestException;
import com.sparta.cookietalk.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    default User findByIdOrElseThrow(Long userId){
        return findById(userId).orElseThrow(() -> new InvalidRequestException("존재하지 않는 사용자입니다."));
    }
}
