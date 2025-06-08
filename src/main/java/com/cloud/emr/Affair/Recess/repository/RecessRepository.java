package com.cloud.emr.Affair.Recess.repository;

import com.cloud.emr.Affair.Recess.entity.RecessEntity;
import com.cloud.emr.Main.User.type.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RecessRepository extends JpaRepository<RecessEntity, Long> {

    // 같은 역할, 종료시간이 시작시간 이후, 시작시간이 종료시간 이전인 휴진 조회 (=시간 겹침)
    List<RecessEntity> findByUserEntity_RoleAndRecessEndAfterAndRecessStartBefore(
            RoleType role, LocalDateTime start, LocalDateTime end);

    List<RecessEntity> findByUserEntity_Role(RoleType role);
}
