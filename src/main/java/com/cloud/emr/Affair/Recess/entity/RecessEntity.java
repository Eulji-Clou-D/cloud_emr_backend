package com.cloud.emr.Affair.Recess.entity;

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
public class RecessEntity {

    // 의사 휴진 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id", nullable = false, unique = true)
    private Long id;

    // 사용자 id (부서를 통해 의료진, 직원으로 분기)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity userEntity;

    // 휴진 시작 날짜 시간 (30분 단위)
    @Column(name = "recess_start")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime recessStart;

    // 휴진 종료 날짜 시간 (30분 단위)
    @Column(name = "recess_end")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime recessEnd;

    // 휴진 사유
    @Column(name = "recess_reason", length = 255)
    private String recessReason;

    // 휴진 생성일 (타임 스탬프)
    @CreationTimestamp
    @Column(name = "recess_create", updatable = false)
    private LocalDateTime recessCreate;

}
