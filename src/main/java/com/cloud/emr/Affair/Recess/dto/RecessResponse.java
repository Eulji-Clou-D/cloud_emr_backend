package com.cloud.emr.Affair.Recess.dto;

import com.cloud.emr.Affair.Recess.entity.RecessEntity;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.type.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RecessResponse {

    private Long id;           // 휴진 ID 추가
    private Long userId;
    private RoleType role;
    private LocalDateTime recessStart;
    private LocalDateTime recessEnd;
    private String recessReason;
    private LocalDateTime recessCreate;
    private LocalDateTime recessUpdate;

    public static RecessResponse from(RecessEntity entity) {
        UserEntity user = entity.getUserEntity();

        return new RecessResponse(
                entity.getId(),
                user.getId(),
                user.getRole(),
                entity.getRecessStart(),
                entity.getRecessEnd(),
                entity.getRecessReason(),
                entity.getRecessCreate(),
                entity.getRecessUpdate()
        );
    }
}