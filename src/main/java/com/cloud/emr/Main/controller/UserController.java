package com.cloud.emr.Main.controller;

import com.cloud.emr.Main.dto.UserLoginDTO;
import com.cloud.emr.Main.dto.UserRegisterDTO;
import com.cloud.emr.Main.entity.UserEntity;
import com.cloud.emr.Main.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    // 다양한 데이터 타입을 반환 받기 위한 ResponseEntity<Object> 사용
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        try {
            // 사용자 등록 처리
            userService.registerUser(userRegisterDTO);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDTO userLoginDTO) {


        return null;

    }



}

