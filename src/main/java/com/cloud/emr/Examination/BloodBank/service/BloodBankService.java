package com.cloud.emr.Examination.BloodBank.service;

import com.cloud.emr.Examination.BloodBank.dto.BloodBankRegisterRequest;
import com.cloud.emr.Examination.BloodBank.dto.BloodBankResponse;
import com.cloud.emr.Examination.BloodBank.dto.BloodBankUpdateRequest;
import com.cloud.emr.Examination.BloodBank.repository.BloodBankRepository;
import com.cloud.emr.Examination.Equipment.entity.EquipmentEntity;
import com.cloud.emr.Examination.Equipment.repository.EquipmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BloodBankService {
    
    private final BloodBankRepository bloodBankRepository;

    private final EquipmentRepository equipmentRepository;

    public BloodBankService(EquipmentRepository equipmentRepository, BloodBankRepository bloodBankRepository) {
        this.bloodBankRepository = bloodBankRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public ExaminationEntity registerExamination(BloodBankRegisterRequest bloodBankRegisterRequest) {

        EquipmentEntity foundEquipment = null;

        if (bloodBankRegisterRequest.getEquipmentId() != null) {
            foundEquipment = equipmentRepository.findByEquipmentId(bloodBankRegisterRequest.getEquipmentId());
        }

        ExaminationEntity examinationEntity = ExaminationEntity.builder()
                .equipmentEntity(foundEquipment)
                .examinationName(bloodBankRegisterRequest.getExaminationName())
                .examinationType(bloodBankRegisterRequest.getExaminationType())
                .examinationConstraints(bloodBankRegisterRequest.getExaminationConstraints())
                .examinationLocation(bloodBankRegisterRequest.getExaminationLocation())
                .examinationPrice(bloodBankRegisterRequest.getExaminationPrice())
                .build();

        return bloodBankRepository.save(examinationEntity);
    }

    public BloodBankResponse readExamination(Long examinationId) {

        Optional<ExaminationEntity> examinationEntityOptional = bloodBankRepository.findByExaminationIdOptional(examinationId);

        if (examinationEntityOptional.isPresent()) {
            ExaminationEntity examinationEntity = examinationEntityOptional.get();

            return new BloodBankResponse(
                    examinationEntity.getExaminationId(),
                    examinationEntity.getEquipmentEntity().getEquipmentId(),
                    examinationEntity.getEquipmentEntity().getEquipmentName(),
                    examinationEntity.getExaminationName(),
                    examinationEntity.getExaminationType(),
                    examinationEntity.getExaminationConstraints(),
                    examinationEntity.getExaminationLocation(),
                    examinationEntity.getExaminationPrice()
            );
        }
        return null;
    }

    public List<BloodBankResponse> readExaminationByEuipmentId(Long equipmentID) {

        EquipmentEntity foundEquipment = equipmentRepository.findByEquipmentIdOptional(equipmentID)
                .orElseThrow(() -> new RuntimeException("해당 장비 정보가 없습니다."));

        List<ExaminationEntity> examinationEntities = bloodBankRepository.findAllByEquipmentEntity(foundEquipment);

        return examinationEntities.stream()
                .map(examinationEntity -> new BloodBankResponse(
                        examinationEntity.getExaminationId(),
                        examinationEntity.getEquipmentEntity().getEquipmentId(),
                        examinationEntity.getEquipmentEntity().getEquipmentName(),
                        examinationEntity.getExaminationName(),
                        examinationEntity.getExaminationType(),
                        examinationEntity.getExaminationConstraints(),
                        examinationEntity.getExaminationLocation(),
                        examinationEntity.getExaminationPrice()
                ))
                .collect(Collectors.toList());
    }

    public List<BloodBankResponse> readAllExamination() {

        List<ExaminationEntity> examinationEntities = bloodBankRepository.findAll();

        return examinationEntities.stream()
                .map(examinationEntity -> new BloodBankResponse(
                        examinationEntity.getExaminationId(),
                        examinationEntity.getEquipmentEntity().getEquipmentId(),
                        examinationEntity.getEquipmentEntity().getEquipmentName(),
                        examinationEntity.getExaminationName(),
                        examinationEntity.getExaminationType(),
                        examinationEntity.getExaminationConstraints(),
                        examinationEntity.getExaminationLocation(),
                        examinationEntity.getExaminationPrice()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public ExaminationEntity updateExamination(Long examinationId, BloodBankUpdateRequest bloodBankUpdateRequest) {

        Optional<ExaminationEntity> examinationEntityOptional = bloodBankRepository.findByExaminationIdOptional(examinationId);

        if (examinationEntityOptional.isPresent()) {
            EquipmentEntity foundEquipment = null;

            Long equipmentId = examinationEntityOptional.get().getEquipmentEntity().getEquipmentId();

            if (equipmentId != null) {
                foundEquipment = equipmentRepository.findByEquipmentIdOptional(equipmentId)
                        .orElseThrow(() -> new RuntimeException("기존 해당 장비 정보가 없습니다."));
            }

            if (bloodBankUpdateRequest.getEquipmentId() != null) {
                foundEquipment = equipmentRepository.findByEquipmentIdOptional(bloodBankUpdateRequest.getEquipmentId())
                        .orElseThrow(() -> new RuntimeException("갱신하려는 해당 장비 정보가 없습니다."));
            }

            ExaminationEntity updatedEntity = ExaminationEntity.builder()
                    .examinationId(examinationId)
                    .equipmentEntity(foundEquipment)
                    .examinationName(bloodBankUpdateRequest.getExaminationName() != null ? bloodBankUpdateRequest.getExaminationName() : examinationEntityOptional.get().getExaminationName())
                    .examinationType(bloodBankUpdateRequest.getExaminationType() != null ? bloodBankUpdateRequest.getExaminationType() : examinationEntityOptional.get().getExaminationType())
                    .examinationConstraints(bloodBankUpdateRequest.getExaminationConstraints() != null ? bloodBankUpdateRequest.getExaminationConstraints() : examinationEntityOptional.get().getExaminationConstraints())
                    .examinationLocation(bloodBankUpdateRequest.getExaminationLocation() != null ? bloodBankUpdateRequest.getExaminationLocation() : examinationEntityOptional.get().getExaminationLocation())
                    .examinationPrice(bloodBankUpdateRequest.getExaminationPrice() != null ? bloodBankUpdateRequest.getExaminationPrice() : examinationEntityOptional.get().getExaminationPrice())
                    .build();

            return bloodBankRepository.save(updatedEntity);
        }
        return null;
    }

    @Transactional
    public BloodBankResponse deleteExamination(Long examinationID) {

        ExaminationEntity examinationEntity = bloodBankRepository.findByExaminationIdOptional(examinationID)
                          .orElseThrow(() -> new RuntimeException("해당 검사 정보가 없습니다."));

        BloodBankResponse deletedExamination = new BloodBankResponse(
                examinationEntity.getExaminationId(),
                examinationEntity.getEquipmentEntity().getEquipmentId(),
                examinationEntity.getEquipmentEntity().getEquipmentName(),
                examinationEntity.getExaminationName(),
                examinationEntity.getExaminationType(),
                examinationEntity.getExaminationConstraints(),
                examinationEntity.getExaminationLocation(),
                examinationEntity.getExaminationPrice()
        );

        bloodBankRepository.delete(examinationEntity);

        return deletedExamination;
    }
}
