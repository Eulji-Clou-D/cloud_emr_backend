package com.cloud.emr.Affair.Holiday.repository;

import com.cloud.emr.Affair.Holiday.entity.HolidayEntity;
import com.cloud.emr.Affair.Recess.entity.RecessEntity;
import com.cloud.emr.Main.User.type.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface HolidayRepository extends JpaRepository<HolidayEntity, Long> {

    HolidayEntity findByHolidayDate(LocalDate holidayDate);

    List<HolidayEntity> findAllByHolidayDateBetween(LocalDateTime start, LocalDateTime end);

}

