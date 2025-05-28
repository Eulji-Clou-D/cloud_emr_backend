package com.cloud.emr.Affair.CheckIn.entity;

import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "CheckIn")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckInEntity {

    // 접수 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checkIn_id", nullable = false)
    private Long checkInId;  // checkIn_id 컬럼과 매핑된 필드

    // 비식별관계 FK
    // 다대일 (한 환자는 여러 접수가 가능)
    // 환자번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_no", referencedColumnName = "patient_no", nullable = false)
    private PatientEntity patientEntity;  // 환자 정보와 연관

    // 비식별관계 FK
    // 다대일 (한 유저는 여러 접수가 가능)
    // 사용자 IDa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userEntity;

    // 접수 일자 시간 (접수된 시간)
    @CreationTimestamp
    @Column(name = "checkIn_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInDate; // 접수 일자 시간 (자동으로 접수된 시간)

    // 방문 목적
    @Column(name = "checkIn_purpose", nullable = false, length = 100)
    private String checkInPurpose;

}
