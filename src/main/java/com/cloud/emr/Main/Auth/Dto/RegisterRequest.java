package com.cloud.emr.Main.Auth.Dto;

import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.type.Gender;
import com.cloud.emr.Main.User.type.RoleType;
import jakarta.validation.constraints.*;
import lombok.Getter;


import java.time.LocalDate;

@Getter
public class RegisterRequest {

    /*
    @Enumerated(EnumType.STRING)
    @NotEmpty(message = "병원 코드는 필수항목입니다.")
    private HospitalCode userHospitalCode;
     */

    @NotEmpty(message = "부서명은 필수항목입니다.")
    private String departmentName;

    @NotEmpty(message = "이름은 필수항목입니다.")
    private String name;

    @NotNull(message = "성별은 필수항목입니다.")
    private Gender gender;

    @Size(max = 25, min = 3)
    @NotEmpty(message = "아이디는 필수항목입니다.")
    private String loginId;

    @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,16}$", message = "비밀번호는 영문 대소문자, 숫자 또는 특수문자 중 2가지 이상 조합, 8자 이상 16자 이하로 설정해야 합니다.")
    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password;

    @NotEmpty(message = "주소는 필수항목입니다.")
    private String address;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email(message = "유효한 이메일을 입력해주세요.")
    private String email;

    @NotEmpty(message = "휴대폰 번호는 필수항목입니다.")
    @Pattern(regexp = "^01[0-9]-[0-9]{4}-[0-9]{4}$", message = "핸드폰 번호의 양식을 확인해주세요. 예: 010-1234-5678")
    private String telNum;

    @NotNull(message = "생년월일은 필수항목입니다.")
    private LocalDate birth;

    @NotNull(message = "입사일은 필수항목입니다.")
    private LocalDate hireDate;

    // userRegisterDate는 엔티티에서 자동으로 생성

    /**
     * 회원가입을 회원가입을 위한 DTo에서 반환
     * @author : 전재윤
     * @exception IllegalArgumentException : 비밀번호와 비밀번호 확인이 일치하지 않을 경우 예외 반환
     * @return : UserEntity
     */
    public UserEntity toUserEntity() {

        return UserEntity.builder()
                // .hospitalCode(this.userHospitalCode)
                .deptName(this.departmentName)
                .role(RoleType.WAIT) //우선 WAIT로 진행
                .loginId(this.loginId)
                .password(this.password)
                .name(this.name)
                .gender(this.gender)
                .address(this.address)
                .email(this.email)
                .telNum(this.telNum)
                .birth(this.birth.atStartOfDay()) // LocalDate로부터 LocalDateTime으로 변환
                .hireDate(this.hireDate.atStartOfDay()) // LocalDate로부터 LocalDateTime으로 변환
                .build();
    }

}
