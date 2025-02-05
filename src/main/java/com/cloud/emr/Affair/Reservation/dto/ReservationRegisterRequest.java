package com.cloud.emr.Affair.Reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
// 환자 예약
public class ReservationRegisterRequest {

    private Long patientNo; // 환자 번호
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationDate; // 예약 날짜 시간

}

