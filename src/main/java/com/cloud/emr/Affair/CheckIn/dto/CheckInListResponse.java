package com.cloud.emr.Affair.CheckIn.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CheckInListResponse {

    @NotNull(message = "접수 ID는 필수 값입니다.")
    @Column(nullable = false, columnDefinition = "INT")
    private Long checkInId;    // 접수 ID

    @NotBlank(message = "환자번호는 필수 값입니다.")
    @Size(min = 8, max = 8, message = "환자번호는 반드시 8자리여야 합니다.")
    @Column(nullable = false, length = 8)
    private Long patientNo;  // 환자 번호

    @NotNull(message = "접수 목적은 필수 값입니다.")
    @Size(max = 100, message = "접수 목적은 최대 500자까지 가능합니다.")
    @Column(nullable = false, length = 100)
    private String checkInPurpose;  // 접수 목적

    @NotNull(message = "접수한 유저 이름은 필수 값입니다.")
    @Column(nullable = false)
    private String userName;   // 접수한 유저 이름

}
