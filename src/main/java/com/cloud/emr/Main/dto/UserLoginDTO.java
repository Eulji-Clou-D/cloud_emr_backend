package com.cloud.emr.Main.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLoginDTO {

    @NotBlank(message = "아이디를 입력해주세요.")
    String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    String userPassword;

}
