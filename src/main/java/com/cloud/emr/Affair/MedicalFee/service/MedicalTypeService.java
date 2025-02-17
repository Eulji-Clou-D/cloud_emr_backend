package com.cloud.emr.Affair.MedicalFee.service;

import com.cloud.emr.Affair.MedicalFee.dto.MedicalTypeRequest;
import com.cloud.emr.Affair.MedicalFee.dto.MedicalTypeResponse;
import com.cloud.emr.Affair.MedicalFee.entity.MedicalTypeEntity;
import com.cloud.emr.Affair.MedicalFee.repository.MedicalTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MedicalTypeService {

    @Autowired
    private MedicalTypeRepository medicalTypeRepository;




    public MedicalTypeEntity findMedicalTypeById(Long medicalTypeId) {
        return medicalTypeRepository.findById(medicalTypeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유형 찾을 수 없음" + medicalTypeId));
    }

    public MedicalTypeEntity findMedicalTypeByName(String medicalTypeName) {
        return medicalTypeRepository.findByMedicalTypeName(medicalTypeName);
    }

    public MedicalTypeResponse createMedicalType(MedicalTypeRequest request) {
        MedicalTypeEntity medicalTypeEntity = MedicalTypeEntity.builder()
                .medicalTypeName(request.getMedicalTypeName())
                .medicalTypeFee(request.getMedicalTypeFee())
                .build();

        medicalTypeEntity = medicalTypeRepository.save(medicalTypeEntity);

        return new MedicalTypeResponse(
                medicalTypeEntity.getMedicalTypeId(),
                medicalTypeEntity.getMedicalTypeName(),
                medicalTypeEntity.getMedicalTypeFee()
        );
    }

    public MedicalTypeResponse updateMedicalType(MedicalTypeRequest request, Long medicalTypeId) {
        MedicalTypeEntity medicalTypeEntity = medicalTypeRepository.findById(medicalTypeId)
                .orElseThrow(() -> new RuntimeException("진료 항목을 찾을 수 없습니다."));

        MedicalTypeEntity updatedTypeEntity = MedicalTypeEntity.builder()
                .medicalTypeId(medicalTypeEntity.getMedicalTypeId())
                .medicalTypeName(request.getMedicalTypeName())
                .medicalTypeFee(request.getMedicalTypeFee())
                .build();


        medicalTypeRepository.save(updatedTypeEntity);

        return new MedicalTypeResponse(
                updatedTypeEntity.getMedicalTypeId(),
                updatedTypeEntity.getMedicalTypeName(),
                updatedTypeEntity.getMedicalTypeFee()
        );

    }

    public MedicalTypeResponse deleteById(Long medicalTypeId) {
        MedicalTypeEntity targetEntity = medicalTypeRepository.findById(medicalTypeId)
                .orElseThrow(() -> new RuntimeException("찾을 수 없는 진료 항목입니다."));

        medicalTypeRepository.delete(targetEntity);

        return new MedicalTypeResponse(
                targetEntity.getMedicalTypeId(),
                targetEntity.getMedicalTypeName(),
                targetEntity.getMedicalTypeFee()
        );
    }
}
