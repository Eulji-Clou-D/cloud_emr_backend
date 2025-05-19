package com.cloud.emr.Affair.DoctorTreatment.service;


import com.cloud.emr.Affair.DoctorTreatment.dto.DoctorTreatmentRequest;
import com.cloud.emr.Affair.DoctorTreatment.dto.DoctorTreatmentResponse;
import com.cloud.emr.Affair.DoctorTreatment.entity.DoctorTreatmentEntity;
import com.cloud.emr.Affair.DoctorTreatment.repository.DoctorTreatmentRepository;
import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Affair.Patient.service.PatientService;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DoctorTreatmentService {

    @Autowired
    private DoctorTreatmentRepository doctorTreatmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientService patientService;

    // 생성 시 겹칠때
    public boolean isOverlap(Long reqUserId, LocalDateTime reqStartTime, LocalDateTime reqEndTime) {
        Optional<UserEntity> reqUser = userRepository.findById(reqUserId);
        List<DoctorTreatmentEntity> checks = doctorTreatmentRepository.findAllByUserEntity(reqUser);

        if (checks.isEmpty()) {
            return false;
        }

        for(DoctorTreatmentEntity check : checks){
            LocalDateTime existStart = check.getDoctorTreatmentStart();

            LocalDateTime existEnd = check.getDoctorTreatmentEnd();

            if (!((reqEndTime.isBefore(existStart) || reqStartTime.isAfter(existEnd)) && reqStartTime.isBefore(reqEndTime))) {
                return true; //겹침
            }
        }

        return false;
    }
    // 수정 간 겹칠때  (기존 같은 ID 예외처리)
    public boolean isUpdateOverlap(Long doctorTreatmentId, Long reqUserId, LocalDateTime reqStartTime, LocalDateTime reqEndTime) {
        Optional<UserEntity> reqUser = userRepository.findById(reqUserId);
        List<DoctorTreatmentEntity> checks = doctorTreatmentRepository.findAllByUserEntity(reqUser);

        if (checks.isEmpty()) {
            return false;
        }

        for(DoctorTreatmentEntity check : checks){

            if(check.getDoctorTreatmentId().equals(doctorTreatmentId)){
                continue;
            }

            LocalDateTime existStart = check.getDoctorTreatmentStart();

            LocalDateTime existEnd = check.getDoctorTreatmentEnd();

            if (!((reqEndTime.isBefore(existStart) || reqStartTime.isAfter(existEnd)) && reqStartTime.isBefore(reqEndTime))) {
                return true; //겹침
            }
        }

        return false;
    }

    public DoctorTreatmentResponse createTreatment(DoctorTreatmentRequest doctorTreatmentRequest, UserEntity userEntity, PatientEntity patientEntity) {
        DoctorTreatmentEntity doctorTreatmentEntity = DoctorTreatmentEntity.builder()
                .doctorTreatmentId(doctorTreatmentRequest.getDoctorTreatmentId())
                .userEntity(userEntity)
                .patientEntity(patientEntity)
                .doctorTreatmentStart(doctorTreatmentRequest.getDoctorTreatmentStart())
                .doctorTreatmentEnd(doctorTreatmentRequest.getDoctorTreatmentEnd())
                .build();

        doctorTreatmentEntity = doctorTreatmentRepository.save(doctorTreatmentEntity);

        return new DoctorTreatmentResponse(
                doctorTreatmentEntity.getDoctorTreatmentId(),
                doctorTreatmentEntity.getUserEntity().getUserId(),
                doctorTreatmentEntity.getPatientEntity().getPatientNo(),
                doctorTreatmentEntity.getDoctorTreatmentStart(),
                doctorTreatmentEntity.getDoctorTreatmentEnd()
        );
    }

    public DoctorTreatmentEntity findById(Long doctorTreatmentId) {
        return doctorTreatmentRepository.findById(doctorTreatmentId).orElse(null);
    }

    public DoctorTreatmentResponse updateDoctorTreatment(Long doctorTreatmentId, DoctorTreatmentRequest doctorTreatmentRequest) {
        DoctorTreatmentEntity existing = doctorTreatmentRepository.findById(doctorTreatmentId).orElseThrow(
                () -> new IllegalArgumentException("예상치 못한 에러")
        );

        //수정하려는 일정이 기존 일정과 겹칠때
        if (isUpdateOverlap(
                doctorTreatmentId,
                existing.getUserEntity().getUserId(),
                doctorTreatmentRequest.getDoctorTreatmentStart(),
                doctorTreatmentRequest.getDoctorTreatmentEnd()
               )
           ) {
            return null;
        }

        PatientEntity patient = patientService.findPatientByNo(doctorTreatmentRequest.getPatientNo());

        if (patient == null) {
            return null;
        }

        DoctorTreatmentEntity updateDoctorTreatment = DoctorTreatmentEntity.builder()
                .doctorTreatmentId(existing.getDoctorTreatmentId())
                .userEntity(existing.getUserEntity())
                .patientEntity(patient)
                .doctorTreatmentStart(doctorTreatmentRequest.getDoctorTreatmentStart())
                .doctorTreatmentEnd(doctorTreatmentRequest.getDoctorTreatmentEnd())
                .build();

        DoctorTreatmentEntity saved = doctorTreatmentRepository.save(updateDoctorTreatment);

        return new DoctorTreatmentResponse(
                saved.getDoctorTreatmentId(),
                saved.getUserEntity().getUserId(),
                saved.getPatientEntity().getPatientNo(),
                saved.getDoctorTreatmentStart(),
                saved.getDoctorTreatmentEnd()
        );

    }

    public DoctorTreatmentResponse deleteById(Long doctorTreatmentId) {
        DoctorTreatmentEntity doctorTreatmentEntity = doctorTreatmentRepository.findById(doctorTreatmentId).orElseThrow(() -> new IllegalArgumentException("예상치 못한 에러"));

        doctorTreatmentRepository.delete(doctorTreatmentEntity);

        return new DoctorTreatmentResponse(
                doctorTreatmentEntity.getDoctorTreatmentId(),
                doctorTreatmentEntity.getUserEntity().getUserId(),
                doctorTreatmentEntity.getPatientEntity().getPatientNo(),
                doctorTreatmentEntity.getDoctorTreatmentStart(),
                doctorTreatmentEntity.getDoctorTreatmentEnd()
        );
    }
}
