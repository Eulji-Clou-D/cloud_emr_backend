package com.cloud.emr.Affair.Recess.dto;

import com.cloud.emr.Main.User.status.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecessRequest {

    private Long userId; // 사용자 ID
    private RoleType Role; // DOCTOR 또는 STAFF
    private LocalDateTime recessStart;
    private LocalDateTime recessEnd;
    private String recessReason;

}
