package com.cloud.emr.Affair.Holiday.dto;

import com.cloud.emr.Affair.Holiday.entity.HolidayEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HolidayResponse {

    private Long id;
    private LocalDate holidayDate;
    private Boolean holidayNational;
    private String holidayReason;

    public HolidayResponse(HolidayEntity e) {
        this.id = e.getId();
        this.holidayDate = e.getHolidayDate();
        this.holidayNational = e.getHolidayNational();
        this.holidayReason = e.getHolidayReason();
    }

}

