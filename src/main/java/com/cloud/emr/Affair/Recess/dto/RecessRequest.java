package com.cloud.emr.Affair.Recess.dto;

import com.cloud.emr.Main.User.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecessRequest {

    private LocalDateTime recessStart;
    private LocalDateTime recessEnd;
    private String recessReason;

}
