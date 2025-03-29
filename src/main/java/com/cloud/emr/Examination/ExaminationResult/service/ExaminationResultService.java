package com.cloud.emr.Examination.ExaminationResult.service;

import com.cloud.emr.Examination.Equipment.entity.EquipmentEntity;
import com.cloud.emr.Examination.Equipment.repository.EquipmentRepository;
import com.cloud.emr.Examination.ExaminationResult.dto.ExaminationResultRegisterRequest;
import com.cloud.emr.Examination.ExaminationResult.dto.ExaminationResultResponse;
import com.cloud.emr.Examination.ExaminationResult.dto.ExaminationResultUpdateRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExaminationResultService {
    
    private final ExaminationRepository examinationRepository;

    private final EquipmentRepository equipmentRepository;

    public ExaminationResultService(EquipmentRepository equipmentRepository, ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public ExaminationEntity registerExamination(ExaminationResultRegisterRequest examinationResultRegisterRequest) {

        EquipmentEntity foundEquipment = null;

        if (examinationResultRegisterRequest.getEquipmentId() != null) {
            foundEquipment = equipmentRepository.findByEquipmentId(examinationResultRegisterRequest.getEquipmentId());
        }

        ExaminationEntity examinationEntity = ExaminationEntity.builder()
                .equipmentEntity(foundEquipment)
                .examinationName(examinationResultRegisterRequest.getExaminationName())
                .examinationType(examinationResultRegisterRequest.getExaminationType())
                .examinationConstraints(examinationResultRegisterRequest.getExaminationConstraints())
                .examinationLocation(examinationResultRegisterRequest.getExaminationLocation())
                .examinationPrice(examinationResultRegisterRequest.getExaminationPrice())
                .build();

        return examinationRepository.save(examinationEntity);
    }

    public ExaminationResultResponse readExamination(Long examinationId) {

        Optional<ExaminationEntity> examinationEntityOptional = examinationRepository.findByExaminationIdOptional(examinationId);

        if (examinationEntityOptional.isPresent()) {
            ExaminationEntity examinationEntity = examinationEntityOptional.get();

            return new ExaminationResultResponse(
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

    public List<ExaminationResultResponse> readExaminationByEuipmentId(Long equipmentID) {

        EquipmentEntity foundEquipment = equipmentRepository.findByEquipmentIdOptional(equipmentID)
                .orElseThrow(() -> new RuntimeException("해당 장비 정보가 없습니다."));

        List<ExaminationEntity> examinationEntities = examinationRepository.findAllByEquipmentEntity(foundEquipment);

        return examinationEntities.stream()
                .map(examinationEntity -> new ExaminationResultResponse(
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

    public List<ExaminationResultResponse> readAllExamination() {

        List<ExaminationEntity> examinationEntities = examinationRepository.findAll();

        return examinationEntities.stream()
                .map(examinationEntity -> new ExaminationResultResponse(
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
    public ExaminationEntity updateExamination(Long examinationId, ExaminationResultUpdateRequest examinationResultUpdateRequest) {

        Optional<ExaminationEntity> examinationEntityOptional = examinationRepository.findByExaminationIdOptional(examinationId);

        if (examinationEntityOptional.isPresent()) {
            EquipmentEntity foundEquipment = null;

            Long equipmentId = examinationEntityOptional.get().getEquipmentEntity().getEquipmentId();

            if (equipmentId != null) {
                foundEquipment = equipmentRepository.findByEquipmentIdOptional(equipmentId)
                        .orElseThrow(() -> new RuntimeException("기존 해당 장비 정보가 없습니다."));
            }

            if (examinationResultUpdateRequest.getEquipmentId() != null) {
                foundEquipment = equipmentRepository.findByEquipmentIdOptional(examinationResultUpdateRequest.getEquipmentId())
                        .orElseThrow(() -> new RuntimeException("갱신하려는 해당 장비 정보가 없습니다."));
            }

            ExaminationEntity updatedEntity = ExaminationEntity.builder()
                    .examinationId(examinationId)
                    .equipmentEntity(foundEquipment)
                    .examinationName(examinationResultUpdateRequest.getExaminationName() != null ? examinationResultUpdateRequest.getExaminationName() : examinationEntityOptional.get().getExaminationName())
                    .examinationType(examinationResultUpdateRequest.getExaminationType() != null ? examinationResultUpdateRequest.getExaminationType() : examinationEntityOptional.get().getExaminationType())
                    .examinationConstraints(examinationResultUpdateRequest.getExaminationConstraints() != null ? examinationResultUpdateRequest.getExaminationConstraints() : examinationEntityOptional.get().getExaminationConstraints())
                    .examinationLocation(examinationResultUpdateRequest.getExaminationLocation() != null ? examinationResultUpdateRequest.getExaminationLocation() : examinationEntityOptional.get().getExaminationLocation())
                    .examinationPrice(examinationResultUpdateRequest.getExaminationPrice() != null ? examinationResultUpdateRequest.getExaminationPrice() : examinationEntityOptional.get().getExaminationPrice())
                    .build();

            return examinationRepository.save(updatedEntity);
        }
        return null;
    }

    @Transactional
    public ExaminationResultResponse deleteExamination(Long examinationID) {

        ExaminationEntity examinationEntity = examinationRepository.findByExaminationIdOptional(examinationID)
                          .orElseThrow(() -> new RuntimeException("해당 검사 정보가 없습니다."));

        ExaminationResultResponse deletedExamination = new ExaminationResultResponse(
                examinationEntity.getExaminationId(),
                examinationEntity.getEquipmentEntity().getEquipmentId(),
                examinationEntity.getEquipmentEntity().getEquipmentName(),
                examinationEntity.getExaminationName(),
                examinationEntity.getExaminationType(),
                examinationEntity.getExaminationConstraints(),
                examinationEntity.getExaminationLocation(),
                examinationEntity.getExaminationPrice()
        );

        examinationRepository.delete(examinationEntity);

        return deletedExamination;
    }
}
