package com.cloud.emr.Affair.Holiday.repository;

import com.cloud.emr.Affair.Holiday.entity.HolidayEntity;
import com.cloud.emr.Main.User.type.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HolidayRepository extends JpaRepository<HolidayEntity, Long> {

}

