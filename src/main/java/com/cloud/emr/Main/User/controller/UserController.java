package com.cloud.emr.Main.User.controller;

import com.cloud.emr.Main.User.dto.UserLoginRequest;
import com.cloud.emr.Main.User.dto.UserRegisterRequest;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    // 다양한 데이터 타입을 반환 받기 위한 ResponseEntity<Object> 사용
    public ResponseEntity<Object> registerUser(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        try {

            UserEntity userEntity = userService.registerUser(userRegisterRequest);

            // user_id를 포함해 성공 응답 반환
            return ResponseEntity.ok(Map.of(
                    "message", "회원가입 성공",
                    "user_id", userEntity.getUserId()  // user_id 반환
            ));
        } catch (Exception e) {
            //
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "회원가입 실패",
                    "error", e.getMessage()
            ));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginRequest userLoginRequest) {

        return null;

    }



}

/*
 1. 회원가입

 http://localhost:8081/api/users/register

 {
  "userDepartmentName": "Neurology",
  "userName": "Jane Smith",
  "userGender": "Female",
  "userLoginId": "jane.smith456",
  "userPassword1": "SecurePass!456",
  "userPassword2": "SecurePass!456",
  "userAddress": "456 Oak Ave, Springfield",
  "userEmail": "jane.smith@example.com",
  "userTel": "010-9876-5432",
  "userBirth": "1985-07-20",
  "userHireDate": "2020-06-15"
}
 */

