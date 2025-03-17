package com.cloud.emr.Affair.DocotorRecess.entity;

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

@Entity(name = "DoctorRecessEntity")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRecessEntity {

    // 의사 휴진 id
    @Id
    @Column (name = "doctorRecess_id", nullable = false, unique = true)
    private Long DoctorRecessId;

    // 사용자 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity userEntity;

    // 휴진 시작 날짜 시간 (30분 단위)
    @Column(name = "doctorRecess_startdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime DoctorRecessStart;

    // 휴진 종료 날짜 시간 (30분 단위)
    @Column(name = "doctorRecess_enddate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime DoctorRecessEnd;

    // 휴진 사유
    @Column(name = "doctorRecess_reason", length = 255)
    private String DoctorRecessReason;

    // 휴진 생성일 (타임 스탬프)
    @CreationTimestamp
    @Column(name = "doctorRecess_createtime", updatable = false)
    private LocalDateTime doctorRecessCreatetime;

}
