package com.cloud.emr.Affair.Holiday.entity;

import com.cloud.emr.Main.User.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "HolidayEntity")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HolidayEntity {

    // 휴일 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id", nullable = false, unique = true)
    private Long id;

    // 휴일 날짜
    @Column(name = "holiday_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime holidayDate;

    // 휴일 사유
    @Column(name = "holiday_reason", length = 255)
    private String holidayReason;

}
