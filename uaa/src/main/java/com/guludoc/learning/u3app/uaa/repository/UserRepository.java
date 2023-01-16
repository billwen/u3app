package com.guludoc.learning.u3app.uaa.repository;

import com.guludoc.learning.u3app.uaa.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    /**
     * 根据email查找用户
     * @param email 用户的email地址
     * @return Optional
     */
    Optional<User> findUserByEmail(String email);

    long countByUsername(String username);

    long countByEmail(String email);

    long countByMobile(String mobile);
}
