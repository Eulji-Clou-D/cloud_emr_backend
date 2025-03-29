package com.cloud.emr.Examination.ExaminationJournal.service;

import com.cloud.emr.Examination.Equipment.entity.EquipmentEntity;
import com.cloud.emr.Examination.Equipment.repository.EquipmentRepository;
import com.cloud.emr.Examination.ExaminationJournal.dto.ExaminationJournalRegisterRequest;
import com.cloud.emr.Examination.ExaminationJournal.dto.ExaminationJournalResponse;
import com.cloud.emr.Examination.ExaminationJournal.dto.ExaminationJournalUpdateRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExaminationJournalService {
    
    private final ExaminationRepository examinationRepository;

    private final EquipmentRepository equipmentRepository;

    public ExaminationJournalService(EquipmentRepository equipmentRepository, ExaminationRepository examinationRepository) {
        this.examinationRepository = examinationRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public ExaminationEntity registerExamination(ExaminationJournalRegisterRequest examinationJournalRegisterRequest) {

        EquipmentEntity foundEquipment = null;

        if (examinationJournalRegisterRequest.getEquipmentId() != null) {
            foundEquipment = equipmentRepository.findByEquipmentId(examinationJournalRegisterRequest.getEquipmentId());
        }

        ExaminationEntity examinationEntity = ExaminationEntity.builder()
                .equipmentEntity(foundEquipment)
                .examinationName(examinationJournalRegisterRequest.getExaminationName())
                .examinationType(examinationJournalRegisterRequest.getExaminationType())
                .examinationConstraints(examinationJournalRegisterRequest.getExaminationConstraints())
                .examinationLocation(examinationJournalRegisterRequest.getExaminationLocation())
                .examinationPrice(examinationJournalRegisterRequest.getExaminationPrice())
                .build();

        return examinationRepository.save(examinationEntity);
    }

    public ExaminationJournalResponse readExamination(Long examinationId) {

        Optional<ExaminationEntity> examinationEntityOptional = examinationRepository.findByExaminationIdOptional(examinationId);

        if (examinationEntityOptional.isPresent()) {
            ExaminationEntity examinationEntity = examinationEntityOptional.get();

            return new ExaminationJournalResponse(
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

    public List<ExaminationJournalResponse> readExaminationByEuipmentId(Long equipmentID) {

        EquipmentEntity foundEquipment = equipmentRepository.findByEquipmentIdOptional(equipmentID)
                .orElseThrow(() -> new RuntimeException("해당 장비 정보가 없습니다."));

        List<ExaminationEntity> examinationEntities = examinationRepository.findAllByEquipmentEntity(foundEquipment);

        return examinationEntities.stream()
                .map(examinationEntity -> new ExaminationJournalResponse(
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

    public List<ExaminationJournalResponse> readAllExamination() {

        List<ExaminationEntity> examinationEntities = examinationRepository.findAll();

        return examinationEntities.stream()
                .map(examinationEntity -> new ExaminationJournalResponse(
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
    public ExaminationEntity updateExamination(Long examinationId, ExaminationJournalUpdateRequest examinationJournalUpdateRequest) {

        Optional<ExaminationEntity> examinationEntityOptional = examinationRepository.findByExaminationIdOptional(examinationId);

        if (examinationEntityOptional.isPresent()) {
            EquipmentEntity foundEquipment = null;

            Long equipmentId = examinationEntityOptional.get().getEquipmentEntity().getEquipmentId();

            if (equipmentId != null) {
                foundEquipment = equipmentRepository.findByEquipmentIdOptional(equipmentId)
                        .orElseThrow(() -> new RuntimeException("기존 해당 장비 정보가 없습니다."));
            }

            if (examinationJournalUpdateRequest.getEquipmentId() != null) {
                foundEquipment = equipmentRepository.findByEquipmentIdOptional(examinationJournalUpdateRequest.getEquipmentId())
                        .orElseThrow(() -> new RuntimeException("갱신하려는 해당 장비 정보가 없습니다."));
            }

            ExaminationEntity updatedEntity = ExaminationEntity.builder()
                    .examinationId(examinationId)
                    .equipmentEntity(foundEquipment)
                    .examinationName(examinationJournalUpdateRequest.getExaminationName() != null ? examinationJournalUpdateRequest.getExaminationName() : examinationEntityOptional.get().getExaminationName())
                    .examinationType(examinationJournalUpdateRequest.getExaminationType() != null ? examinationJournalUpdateRequest.getExaminationType() : examinationEntityOptional.get().getExaminationType())
                    .examinationConstraints(examinationJournalUpdateRequest.getExaminationConstraints() != null ? examinationJournalUpdateRequest.getExaminationConstraints() : examinationEntityOptional.get().getExaminationConstraints())
                    .examinationLocation(examinationJournalUpdateRequest.getExaminationLocation() != null ? examinationJournalUpdateRequest.getExaminationLocation() : examinationEntityOptional.get().getExaminationLocation())
                    .examinationPrice(examinationJournalUpdateRequest.getExaminationPrice() != null ? examinationJournalUpdateRequest.getExaminationPrice() : examinationEntityOptional.get().getExaminationPrice())
                    .build();

            return examinationRepository.save(updatedEntity);
        }
        return null;
    }

    @Transactional
    public ExaminationJournalResponse deleteExamination(Long examinationID) {

        ExaminationEntity examinationEntity = examinationRepository.findByExaminationIdOptional(examinationID)
                          .orElseThrow(() -> new RuntimeException("해당 검사 정보가 없습니다."));

        ExaminationJournalResponse deletedExamination = new ExaminationJournalResponse(
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
