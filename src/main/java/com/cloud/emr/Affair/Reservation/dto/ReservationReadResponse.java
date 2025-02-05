package com.cloud.emr.Affair.Reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 단일 예약 조회
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationReadResponse {

    private Long reservationId; // 예약 ID
    private Long patientNo; // 환자 번호
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationDate; // 예약 날짜 시간 (변경 없이 최초 예약된 날짜)
    private String reservationYn; // 예약 여부


}

