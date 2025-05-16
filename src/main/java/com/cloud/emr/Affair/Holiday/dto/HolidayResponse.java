package com.cloud.emr.Affair.Holiday.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HolidayResponse {

    private Long id;
    private LocalDate holidayDate;
    private Boolean holidayNational;
    private String holidayReason;

}

