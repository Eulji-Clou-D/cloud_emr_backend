package com.cloud.emr.Examination.EquipmentJournal.service;

import com.cloud.emr.Examination.EquipmentJournal.dto.EquipmentJournalRegisterRequest;
import com.cloud.emr.Examination.EquipmentJournal.dto.EquipmentJournalResponse;
import com.cloud.emr.Examination.EquipmentJournal.dto.EquipmentJournalUpdateRequest;
import com.cloud.emr.Examination.EquipmentJournal.repository.EquipmentJournalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentJournalService {

    private final EquipmentJournalRepository equipmentJournalRepository;

    public EquipmentJournalService(EquipmentJournalRepository equipmentJournalRepository) {
        this.equipmentJournalRepository = equipmentJournalRepository;
    }

    public EquipmentEntity registerEquipment(EquipmentJournalRegisterRequest equipmentJournalRegisterRequest) {

        EquipmentEntity equipmentEntity = EquipmentEntity.builder()
                .equipmentName(equipmentJournalRegisterRequest.getEquipmentName())
                .equipmentProductNumber(equipmentJournalRegisterRequest.getEquipmentProductNumber())
                .equipmentManufacturer(equipmentJournalRegisterRequest.getEquipmentManufacturer())
                .equipmentLocation(equipmentJournalRegisterRequest.getEquipmentLocation())
                .equipmentState(equipmentJournalRegisterRequest.getEquipmentState())
                .equipmentSchedule(equipmentJournalRegisterRequest.getEquipmentSchedule())
                .build();

        return equipmentJournalRepository.save(equipmentEntity);
    }

    public EquipmentJournalResponse readEquipment(Long equipmentId) {

        EquipmentEntity equipmentEntity = equipmentJournalRepository.findByEquipmentIdOptional(equipmentId)
                .orElseThrow(() -> new RuntimeException("해당 장비 정보가 없습니다."));

        return new EquipmentJournalResponse(
                equipmentEntity.getEquipmentId(),
                equipmentEntity.getEquipmentName(),
                equipmentEntity.getEquipmentProductNumber(),
                equipmentEntity.getEquipmentManufacturer(),
                equipmentEntity.getEquipmentLocation(),
                equipmentEntity.getEquipmentState(),
                equipmentEntity.getEquipmentSchedule()
        );
    }

    public List<EquipmentJournalResponse> readEquipmentByEquipmentName(String equipmentName) {

        List<EquipmentEntity> equipmentEntities = equipmentJournalRepository.findAllByEquipmentName(equipmentName);

        return equipmentEntities.stream()
                .map(equipmentEntity -> new EquipmentJournalResponse(
                        equipmentEntity.getEquipmentId(),
                        equipmentEntity.getEquipmentName(),
                        equipmentEntity.getEquipmentProductNumber(),
                        equipmentEntity.getEquipmentManufacturer(),
                        equipmentEntity.getEquipmentLocation(),
                        equipmentEntity.getEquipmentState(),
                        equipmentEntity.getEquipmentSchedule()
                ))
                .collect(Collectors.toList());
    }

    public List<EquipmentJournalResponse> readAllEquipment() {

        List<EquipmentEntity> equipmentEntities = equipmentJournalRepository.findAll();

        return equipmentEntities.stream()
                .map(equipmentEntity -> new EquipmentJournalResponse(
                        equipmentEntity.getEquipmentId(),
                        equipmentEntity.getEquipmentName(),
                        equipmentEntity.getEquipmentProductNumber(),
                        equipmentEntity.getEquipmentManufacturer(),
                        equipmentEntity.getEquipmentLocation(),
                        equipmentEntity.getEquipmentState(),
                        equipmentEntity.getEquipmentSchedule()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public EquipmentEntity updateEquipment(Long equipmentId, EquipmentJournalUpdateRequest equipmentJournalUpdateRequest) {

        EquipmentEntity equipmentEntity = equipmentJournalRepository.findByEquipmentIdOptional(equipmentId)
                .orElseThrow(() -> new RuntimeException("기존 해당 장비 정보가 없습니다."));

        if(!equipmentJournalUpdateRequest.getEquipmentName().isBlank()
            || !equipmentJournalUpdateRequest.getEquipmentProductNumber().isBlank()
            || !equipmentJournalUpdateRequest.getEquipmentManufacturer().isBlank()){
            if(!equipmentJournalUpdateRequest.getEquipmentName().isBlank()
                && !equipmentJournalUpdateRequest.getEquipmentProductNumber().isBlank()
                && !equipmentJournalUpdateRequest.getEquipmentManufacturer().isBlank()){
                throw new RuntimeException("장비의 이름, 제품번호, 제조사를 모두 채우세요.");
            }
        }

        EquipmentEntity updatedEntity = EquipmentEntity.builder()
                .equipmentId(equipmentId)
                .equipmentName(equipmentJournalUpdateRequest.getEquipmentName() != null ? equipmentJournalUpdateRequest.getEquipmentName() : equipmentEntity.getEquipmentName())
                .equipmentProductNumber(equipmentJournalUpdateRequest.getEquipmentProductNumber() != null ? equipmentJournalUpdateRequest.getEquipmentProductNumber() : equipmentEntity.getEquipmentProductNumber())
                .equipmentManufacturer(equipmentJournalUpdateRequest.getEquipmentManufacturer() != null ? equipmentJournalUpdateRequest.getEquipmentManufacturer() : equipmentEntity.getEquipmentManufacturer())
                .equipmentLocation(equipmentJournalUpdateRequest.getEquipmentLocation() != null ? equipmentJournalUpdateRequest.getEquipmentLocation() : equipmentEntity.getEquipmentLocation())
                .equipmentState(equipmentJournalUpdateRequest.getEquipmentState() != null ? equipmentJournalUpdateRequest.getEquipmentState() : equipmentEntity.getEquipmentState())
                .equipmentSchedule(equipmentJournalUpdateRequest.getEquipmentSchedule() != null ? equipmentJournalUpdateRequest.getEquipmentSchedule() : equipmentEntity.getEquipmentSchedule())
                .build();

        return equipmentJournalRepository.save(updatedEntity);
    }

    @Transactional
    public EquipmentJournalResponse deleteEquipment(Long equipmentId) {

        EquipmentEntity equipmentEntity = equipmentJournalRepository.findByEquipmentIdOptional(equipmentId)
                .orElseThrow(() -> new RuntimeException("해당 장비 정보가 없습니다."));

        EquipmentJournalResponse deletedEquipment = new EquipmentJournalResponse(
                equipmentEntity.getEquipmentId(),
                equipmentEntity.getEquipmentName(),
                equipmentEntity.getEquipmentProductNumber(),
                equipmentEntity.getEquipmentManufacturer(),
                equipmentEntity.getEquipmentLocation(),
                equipmentEntity.getEquipmentState(),
                equipmentEntity.getEquipmentSchedule()
        );

        equipmentJournalRepository.delete(equipmentEntity);

        return deletedEquipment;
    }
}
