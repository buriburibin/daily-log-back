package com.daily.log.userAuth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    UserAuth findFirstByUserIdOrderByRegDateDesc(String userId);
}