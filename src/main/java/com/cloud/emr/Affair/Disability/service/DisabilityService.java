package com.cloud.emr.Affair.Disability.service;

import com.cloud.emr.Affair.Disability.dto.DisabilityRegisterRequest;
import com.cloud.emr.Affair.Disability.dto.DisabilityResponse;
import com.cloud.emr.Affair.Disability.dto.DisabilityUpdateRequest;
import com.cloud.emr.Affair.Disability.entity.DisabilityEntity;
import com.cloud.emr.Affair.Disability.repository.DisabilityRepository;
import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Affair.Patient.repository.PatientRepository;
import com.cloud.emr.Affair.Patient.service.PatientService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisabilityService {

    @Autowired
    private DisabilityRepository disabilityRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    // 1. 장애인 정보 등록 (환자 1명 = 장애인 정보 오직 1개)
    public DisabilityEntity registerDisability(DisabilityRegisterRequest disabilityRegisterRequest) {

        PatientEntity patient = patientService.findPatientByNo(disabilityRegisterRequest.getPatientNo());
        if (patient == null) {
            throw new IllegalStateException("환자를 찾을 수 없습니다.");
        }

        DisabilityEntity existingDisability = disabilityRepository.findByPatientEntity(patient);
        if (existingDisability != null) {
            throw new IllegalStateException("이미 해당 환자에 대한 장애인 정보가 등록되어 있습니다.");
        }

        DisabilityEntity disabilityEntity = DisabilityEntity.builder()
                .patientEntity(patient)
                .disabilityGrade(disabilityRegisterRequest.getDisabilityGrade())
                .disabilityType(disabilityRegisterRequest.getDisabilityType())
                .disabilityDeviceYN(disabilityRegisterRequest.getDisabilityDeviceYN())
                .disabilityDeviceType(disabilityRegisterRequest.getDisabilityDeviceType())
                .build();

        return disabilityRepository.save(disabilityEntity);
    }



    // 2. 특정 환자의 장애인 정보 조회
    public DisabilityResponse readDisabilityByPatientNo(Long patientNo) {

        Optional<DisabilityEntity> disabilityEntityOptional = disabilityRepository.findByPatientEntity_PatientNo(patientNo);

        if (disabilityEntityOptional.isPresent()) {
            DisabilityEntity disabilityEntity = disabilityEntityOptional.get();

            return new DisabilityResponse(
                    disabilityEntity.getDisabilityId(),
                    disabilityEntity.getPatientEntity().getPatientNo(),
                    disabilityEntity.getPatientEntity().getPatientName(),
                    disabilityEntity.getDisabilityGrade(),
                    disabilityEntity.getDisabilityType(),
                    disabilityEntity.getDisabilityDeviceYN(),
                    disabilityEntity.getDisabilityDeviceType()
            );
        }
        return null;
    }



    // 3. 모든 장애인 정보 조회
    public List<DisabilityResponse> readAllDisabilities() {

        List<DisabilityEntity> disabilityEntities = disabilityRepository.findAll();

        return disabilityEntities.stream()
                .map(disabilityEntity -> new DisabilityResponse(
                        disabilityEntity.getDisabilityId(),
                        disabilityEntity.getPatientEntity().getPatientNo(),
                        disabilityEntity.getPatientEntity().getPatientName(),
                        disabilityEntity.getDisabilityGrade(),
                        disabilityEntity.getDisabilityType(),
                        disabilityEntity.getDisabilityDeviceYN(),
                        disabilityEntity.getDisabilityDeviceType()
                ))
                .collect(Collectors.toList());
    }



    // 4. 장애인 정보 수정 (값만 바꾸면 됨)
    @Transactional
    public DisabilityEntity updateDisability(Long patientNo, DisabilityUpdateRequest disabilityUpdateRequest) {

        PatientEntity patientEntity = patientRepository.findByPatientNo(patientNo)
                .orElseThrow(() -> new RuntimeException("해당 환자 정보가 없습니다."));

        DisabilityEntity disabilityEntity = disabilityRepository.findByPatientEntity(patientEntity);

        if (disabilityEntity == null) {
            throw new RuntimeException("해당 장애인 정보가 없습니다.");
        }

        DisabilityEntity updatedEntity = DisabilityEntity.builder()
                .disabilityId(disabilityEntity.getDisabilityId())
                .patientEntity(patientEntity)
                .disabilityGrade(disabilityUpdateRequest.getDisabilityGrade())
                .disabilityType(disabilityUpdateRequest.getDisabilityType())
                .disabilityDeviceYN(disabilityUpdateRequest.getDisabilityDeviceYN())
                .disabilityDeviceType(disabilityUpdateRequest.getDisabilityDeviceType())
                .build();

        return disabilityRepository.save(updatedEntity);
    }



    // 5. 장애인 정보 삭제
    @Transactional
    public DisabilityResponse deleteDisability(Long patientNo) {

        PatientEntity patientEntity = patientRepository.findByPatientNo(patientNo)
                .orElseThrow(() -> new RuntimeException("해당 환자 정보가 없습니다."));

        DisabilityEntity disabilityEntity = disabilityRepository.findByPatientEntity(patientEntity);

        if (disabilityEntity == null) {
            throw new RuntimeException("해당 장애인 정보가 없습니다.");
        }

        DisabilityResponse deletedDisability = new DisabilityResponse(
                disabilityEntity.getDisabilityId(),
                disabilityEntity.getPatientEntity().getPatientNo(),
                disabilityEntity.getPatientEntity().getPatientName(),
                disabilityEntity.getDisabilityGrade(),
                disabilityEntity.getDisabilityType(),
                disabilityEntity.getDisabilityDeviceYN(),
                disabilityEntity.getDisabilityDeviceType()
        );

        disabilityRepository.delete(disabilityEntity);

        return deletedDisability;
    }

}
