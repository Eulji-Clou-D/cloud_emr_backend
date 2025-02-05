package com.cloud.emr.Affair.Reservation.entity;

import com.cloud.emr.Affair.CheckIn.entity.CheckInEntity;
import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "Reservation")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservation_id", nullable = false, columnDefinition = "INT")
    private Long reservationId; // 예약 ID

    // 메모: 나중에 접수가 완료될 시 예약이 제거되도록

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_no", referencedColumnName = "patient_no", nullable = false)
    private PatientEntity patientEntity;


    // 예약 날짜 시간 (사용자 입력)
    @Column(name = "reservation_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationDate;

    @Column(name = "reservation_YN", length = 1, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String reservationYn;

    // 예약이 변경될 날짜와 시간 (사용자 입력)
    @Column(name = "reservation_change_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime reservationChangeDate;

    @Column(name = "reservation_change_cause", length = 100)
    private String reservationChangeCause; // 예약 변경 사유

}

// length = 20이 varchar(20)이라는 뜻
