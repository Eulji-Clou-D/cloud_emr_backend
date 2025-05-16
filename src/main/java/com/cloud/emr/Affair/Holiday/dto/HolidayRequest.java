package com.cloud.emr.Affair.Holiday.dto;

import com.cloud.emr.Main.User.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HolidayRequest {

    private LocalDate holidayDate;
    private Boolean holidayNational;
    private String holidayReason;

}

