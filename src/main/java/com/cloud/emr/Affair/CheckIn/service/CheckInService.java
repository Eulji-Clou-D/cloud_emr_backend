package com.cloud.emr.Affair.CheckIn.service;

import com.cloud.emr.Affair.CheckIn.dto.CheckInListResponse;
import com.cloud.emr.Affair.CheckIn.dto.CheckInRequest;
import com.cloud.emr.Affair.CheckIn.entity.CheckInEntity;
import com.cloud.emr.Affair.CheckIn.repository.CheckInRepository;
import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Main.User.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CheckInService {

    @Autowired
    private CheckInRepository checkInRepository;

    // 1. 접수 등록 메서드
    public CheckInEntity createCheckIn(CheckInRequest checkInRequest, UserEntity userEntity, PatientEntity patientEntity) {
        // CheckInRequest를 CheckInEntity로 변환
        CheckInEntity checkInEntity = checkInRequest.toCheckInEntity(userEntity, patientEntity);
        // 접수 정보 저장
        return checkInRepository.save(checkInEntity);
    }
    
    // 2. 접수 목록 조회 메서드
    public List<CheckInListResponse> getCheckInList() {
        List<CheckInEntity> checkInEntities = checkInRepository.findAll();

        // CheckInEntity를 CheckInListResponse로 변환
        return checkInEntities.stream().map(checkInEntity -> {
            // 각 CheckInEntity를 CheckInListResponse로 변환
            CheckInListResponse response = new CheckInListResponse(
                    checkInEntity.getCheckInId(),    // 접수 ID
                    checkInEntity.getPatientEntity().getPatientNo(),    // 환자 번호
                    checkInEntity.getCheckInPurpose(), // 접수 목적
                    checkInEntity.getUserEntity().getUserName() // 접수한 유저 이름
            );
            return response;
        }).collect(Collectors.toList());
    }

    // 3. 접수 취소 메서드
    public void cancelCheckIn(Long checkInId) {
        CheckInEntity checkInEntity = checkInRepository.findById(checkInId)
                .orElseThrow(() -> new RuntimeException("접수를 찾을 수 없습니다."));

        // 취소 처리
        checkInRepository.delete(checkInEntity);
    }

}

