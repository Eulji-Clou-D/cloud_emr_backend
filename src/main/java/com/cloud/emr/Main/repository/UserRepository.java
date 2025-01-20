package com.cloud.emr.Main.repository;

import com.cloud.emr.Main.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUserLoginId(String userLoginId);

    boolean existsByUserLoginId(String userLoginId);
}
