package com.cloud.emr.Affair.DoctorTreatment.service;


import com.cloud.emr.Affair.DoctorTreatment.dto.DoctorTreatmentRequest;
import com.cloud.emr.Affair.DoctorTreatment.dto.DoctorTreatmentResponse;
import com.cloud.emr.Affair.DoctorTreatment.entity.DoctorTreatmentEntity;
import com.cloud.emr.Affair.DoctorTreatment.repository.DoctorTreatmentRepository;
import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Affair.Patient.service.PatientService;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import com.cloud.emr.Main.User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorTreatmentService {

    @Autowired
    private DoctorTreatmentRepository doctorTreatmentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientService patientService;
    @Autowired
    private DoctorTreatmentService doctorTreatmentService;

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

    public DoctorTreatmentResponse createTreatment(DoctorTreatmentRequest doctorTreatmentRequest) {

        //의사는 겹쳐도 상관 없음. reqStartTime, reqEndTime은 겹치면 안 됨.
        Long reqUserId = doctorTreatmentRequest.getUserId();

        LocalDateTime reqStartTime = doctorTreatmentRequest.getDoctorTreatmentStart();
        LocalDateTime reqEndTime = doctorTreatmentRequest.getDoctorTreatmentEnd();

        //의사 정보 가져오기
        UserEntity targetUser = userService.findUserById(doctorTreatmentRequest.getUserId());
        if (targetUser == null) {
            throw new RuntimeException("해당하는 유저번호가 없습니다.");
        }

        //환자 정보 가져오기
        PatientEntity targetPatient = patientService.findPatientByNo(doctorTreatmentRequest.getPatientNo());
        if (targetPatient == null) {
            throw new RuntimeException("해당하는 환자 번호가 없습니다.");
        }

        //일정 겹치는지 확인
        if (doctorTreatmentService.isOverlap(reqUserId, reqStartTime, reqEndTime)) {
            throw new RuntimeException("일정이 겹칩니다.");
        }



        DoctorTreatmentEntity doctorTreatmentEntity = DoctorTreatmentEntity.builder()
                .doctorTreatmentId(doctorTreatmentRequest.getDoctorTreatmentId())
                .userEntity(targetUser)
                .patientEntity(targetPatient)
                .doctorTreatmentStart(doctorTreatmentRequest.getDoctorTreatmentStart())
                .doctorTreatmentEnd(doctorTreatmentRequest.getDoctorTreatmentEnd())
                .build();

        doctorTreatmentEntity = doctorTreatmentRepository.save(doctorTreatmentEntity);

        return new DoctorTreatmentResponse(
                doctorTreatmentEntity.getDoctorTreatmentId(),
                doctorTreatmentEntity.getUserEntity().getId(),
                doctorTreatmentEntity.getPatientEntity().getPatientNo(),
                doctorTreatmentEntity.getDoctorTreatmentStart(),
                doctorTreatmentEntity.getDoctorTreatmentEnd()
        );
    }

    public DoctorTreatmentEntity findById(Long doctorTreatmentId) {
        return doctorTreatmentRepository.findById(doctorTreatmentId).orElse(null);
    }

    public DoctorTreatmentResponse updateDoctorTreatment(Long userId, Long doctorTreatmentId, DoctorTreatmentRequest doctorTreatmentRequest) {


        DoctorTreatmentEntity targetSchedule = doctorTreatmentService.findById(doctorTreatmentId);

        if (targetSchedule == null) {
            throw new RuntimeException("존재하지 않는 스케쥴 번호");
        }

        UserEntity ownerCheck = targetSchedule.getUserEntity();

        if (ownerCheck.getId().equals(userId)) {

            DoctorTreatmentEntity existing = doctorTreatmentRepository.findById(doctorTreatmentId).orElseThrow(
                    () -> new IllegalArgumentException("예상치 못한 에러")
            );

            //수정하려는 일정이 기존 일정과 겹칠때
            if (isUpdateOverlap(
                    doctorTreatmentId,
                    existing.getUserEntity().getId(),
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
                    saved.getUserEntity().getId(),
                    saved.getPatientEntity().getPatientNo(),
                    saved.getDoctorTreatmentStart(),
                    saved.getDoctorTreatmentEnd()
            );
        } else {
        throw new RuntimeException("스케쥴 등록정보 일치하지 않음");
    }

    }

    public DoctorTreatmentResponse deleteById(Long doctorTreatmentId) {

        DoctorTreatmentEntity doctorTreatment = doctorTreatmentService.findById(doctorTreatmentId);
        if (doctorTreatment == null) {
            throw new RuntimeException("존재하지 않는 진료 스케쥴");
        }


        DoctorTreatmentEntity doctorTreatmentEntity = doctorTreatmentRepository.findById(doctorTreatmentId).orElseThrow(() -> new IllegalArgumentException("예상치 못한 에러"));

        doctorTreatmentRepository.delete(doctorTreatmentEntity);

        return new DoctorTreatmentResponse(
                doctorTreatmentEntity.getDoctorTreatmentId(),
                doctorTreatmentEntity.getUserEntity().getId(),
                doctorTreatmentEntity.getPatientEntity().getPatientNo(),
                doctorTreatmentEntity.getDoctorTreatmentStart(),
                doctorTreatmentEntity.getDoctorTreatmentEnd()
        );
    }

    public List<DoctorTreatmentResponse> getAllDoctorTreatmentByUserId(Long userId) {


        if(userService.findUserById(userId) == null) {
            throw new RuntimeException("존재하지 않는 유저");
        }


        UserEntity targetUser = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저가 존재하지 않음")
        );

        List<DoctorTreatmentEntity> doctorTreatmentEntities = doctorTreatmentRepository.findByUserEntity(targetUser);

        return doctorTreatmentEntities.stream().map(doctorTreatmentEntity -> {
            DoctorTreatmentResponse doctorTreatmentResponse = new DoctorTreatmentResponse(
                    doctorTreatmentEntity.getDoctorTreatmentId(),
                    doctorTreatmentEntity.getPatientEntity().getPatientNo(),
                    doctorTreatmentEntity.getUserEntity().getId(),
                    doctorTreatmentEntity.getDoctorTreatmentStart(),
                    doctorTreatmentEntity.getDoctorTreatmentEnd()
            );

            return doctorTreatmentResponse;
        }).collect(Collectors.toList());
    }
}
