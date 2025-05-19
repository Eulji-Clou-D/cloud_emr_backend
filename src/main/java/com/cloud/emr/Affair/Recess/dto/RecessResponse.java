package com.cloud.emr.Affair.Recess.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecessResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String role;
    private LocalDateTime recessStart;
    private LocalDateTime recessEnd;
    private String recessReason;
    private LocalDateTime createdAt;

}
