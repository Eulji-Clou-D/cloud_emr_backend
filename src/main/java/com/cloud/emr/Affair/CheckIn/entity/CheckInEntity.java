package com.cloud.emr.Affair.CheckIn.entity;

import com.cloud.emr.Main.User.entity.UserEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    private Long checkInId;

    // patientNo는 아직 PatientEntity와 연관되지 않아서, 컬럼으로만 존재
    // 나중에 성철이가 만들면 연관관계 추가하겠습니다.
    // 환자번호
    @Size(min = 8, max = 8, message = "환자번호는 반드시 8자리여야 합니다.")
    @Column(name = "patient_no", nullable = false, length = 8)
    private Long patientNo;

    // 다대일 (한 유저는 여러 접수가 가능)
    // 사용자 ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", columnDefinition = "BIGINT",  nullable = false)
    private UserEntity userEntity;

    // 접수 일자 시간 (접수된 시간)
    @CreationTimestamp
    @Column(name = "checkIn_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime checkInDate; // 접수 일자 시간 (자동으로 접수된 시간)

    // 방문 목적
    @Column(name = "checkIn_purpose", nullable = false, length = 100)
    private String checkInPurpose;

}
