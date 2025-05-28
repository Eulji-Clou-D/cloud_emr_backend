package com.cloud.emr.Affair.Recess.repository;

import com.cloud.emr.Affair.Recess.entity.RecessEntity;
import com.cloud.emr.Main.User.type.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecessRepository extends JpaRepository<RecessEntity, Long> {

    List<RecessEntity> findByUserEntity_Role(RoleType roleType);

}
