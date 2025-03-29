package com.cloud.emr.Examination.ExaminationSchedule.service;

import com.cloud.emr.Examination.Equipment.entity.EquipmentEntity;
import com.cloud.emr.Examination.Equipment.repository.EquipmentRepository;
import com.cloud.emr.Examination.ExaminationSchedule.dto.ExaminationScheduleRegisterRequest;
import com.cloud.emr.Examination.ExaminationSchedule.dto.ExaminationScheduleResponse;
import com.cloud.emr.Examination.ExaminationSchedule.dto.ExaminationScheduleUpdateRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExaminationScheduleService {
    
    private final ExaminationRepository examinationRepository;

    private final EquipmentRepository equipmentRepository;

    public ExaminationScheduleService(EquipmentRepository equipmentRepository, ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public ExaminationEntity registerExamination(ExaminationScheduleRegisterRequest examinationScheduleRegisterRequest) {

        EquipmentEntity foundEquipment = null;

        if (examinationScheduleRegisterRequest.getEquipmentId() != null) {
            foundEquipment = equipmentRepository.findByEquipmentId(examinationScheduleRegisterRequest.getEquipmentId());
        }

        ExaminationEntity examinationEntity = ExaminationEntity.builder()
                .equipmentEntity(foundEquipment)
                .examinationName(examinationScheduleRegisterRequest.getExaminationName())
                .examinationType(examinationScheduleRegisterRequest.getExaminationType())
                .examinationConstraints(examinationScheduleRegisterRequest.getExaminationConstraints())
                .examinationLocation(examinationScheduleRegisterRequest.getExaminationLocation())
                .examinationPrice(examinationScheduleRegisterRequest.getExaminationPrice())
                .build();

        return examinationRepository.save(examinationEntity);
    }

    public ExaminationScheduleResponse readExamination(Long examinationId) {

        Optional<ExaminationEntity> examinationEntityOptional = examinationRepository.findByExaminationIdOptional(examinationId);

        if (examinationEntityOptional.isPresent()) {
            ExaminationEntity examinationEntity = examinationEntityOptional.get();

            return new ExaminationScheduleResponse(
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

    public List<ExaminationScheduleResponse> readExaminationByEuipmentId(Long equipmentID) {

        EquipmentEntity foundEquipment = equipmentRepository.findByEquipmentIdOptional(equipmentID)
                .orElseThrow(() -> new RuntimeException("해당 장비 정보가 없습니다."));

        List<ExaminationEntity> examinationEntities = examinationRepository.findAllByEquipmentEntity(foundEquipment);

        return examinationEntities.stream()
                .map(examinationEntity -> new ExaminationScheduleResponse(
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

    public List<ExaminationScheduleResponse> readAllExamination() {

        List<ExaminationEntity> examinationEntities = examinationRepository.findAll();

        return examinationEntities.stream()
                .map(examinationEntity -> new ExaminationScheduleResponse(
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
    public ExaminationEntity updateExamination(Long examinationId, ExaminationScheduleUpdateRequest examinationScheduleUpdateRequest) {

        Optional<ExaminationEntity> examinationEntityOptional = examinationRepository.findByExaminationIdOptional(examinationId);

        if (examinationEntityOptional.isPresent()) {
            EquipmentEntity foundEquipment = null;

            Long equipmentId = examinationEntityOptional.get().getEquipmentEntity().getEquipmentId();

            if (equipmentId != null) {
                foundEquipment = equipmentRepository.findByEquipmentIdOptional(equipmentId)
                        .orElseThrow(() -> new RuntimeException("기존 해당 장비 정보가 없습니다."));
            }

            if (examinationScheduleUpdateRequest.getEquipmentId() != null) {
                foundEquipment = equipmentRepository.findByEquipmentIdOptional(examinationScheduleUpdateRequest.getEquipmentId())
                        .orElseThrow(() -> new RuntimeException("갱신하려는 해당 장비 정보가 없습니다."));
            }

            ExaminationEntity updatedEntity = ExaminationEntity.builder()
                    .examinationId(examinationId)
                    .equipmentEntity(foundEquipment)
                    .examinationName(examinationScheduleUpdateRequest.getExaminationName() != null ? examinationScheduleUpdateRequest.getExaminationName() : examinationEntityOptional.get().getExaminationName())
                    .examinationType(examinationScheduleUpdateRequest.getExaminationType() != null ? examinationScheduleUpdateRequest.getExaminationType() : examinationEntityOptional.get().getExaminationType())
                    .examinationConstraints(examinationScheduleUpdateRequest.getExaminationConstraints() != null ? examinationScheduleUpdateRequest.getExaminationConstraints() : examinationEntityOptional.get().getExaminationConstraints())
                    .examinationLocation(examinationScheduleUpdateRequest.getExaminationLocation() != null ? examinationScheduleUpdateRequest.getExaminationLocation() : examinationEntityOptional.get().getExaminationLocation())
                    .examinationPrice(examinationScheduleUpdateRequest.getExaminationPrice() != null ? examinationScheduleUpdateRequest.getExaminationPrice() : examinationEntityOptional.get().getExaminationPrice())
                    .build();

            return examinationRepository.save(updatedEntity);
        }
        return null;
    }

    @Transactional
    public ExaminationScheduleResponse deleteExamination(Long examinationID) {

        ExaminationEntity examinationEntity = examinationRepository.findByExaminationIdOptional(examinationID)
                          .orElseThrow(() -> new RuntimeException("해당 검사 정보가 없습니다."));

        ExaminationScheduleResponse deletedExamination = new ExaminationScheduleResponse(
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
