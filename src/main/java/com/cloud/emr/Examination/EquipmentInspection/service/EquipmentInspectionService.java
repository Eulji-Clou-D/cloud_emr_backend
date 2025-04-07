package com.cloud.emr.Examination.EquipmentInspection.service;

import com.cloud.emr.Examination.Equipment.entity.EquipmentEntity;
import com.cloud.emr.Examination.EquipmentInspection.dto.EquipmentInspectionRegisterRequest;
import com.cloud.emr.Examination.EquipmentInspection.dto.EquipmentInspectionResponse;
import com.cloud.emr.Examination.EquipmentInspection.dto.EquipmentInspectionUpdateRequest;
import com.cloud.emr.Examination.EquipmentInspection.repository.EquipmentInspectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentInspectionService {

    private final EquipmentInspectionRepository equipmentInspectionRepository;

    public EquipmentInspectionService(EquipmentInspectionRepository equipmentInspectionRepository) {
        this.equipmentInspectionRepository = equipmentInspectionRepository;
    }

    public EquipmentEntity registerEquipment(EquipmentInspectionRegisterRequest equipmentInspectionRegisterRequest) {

        EquipmentEntity equipmentEntity = EquipmentEntity.builder()
                .equipmentName(equipmentInspectionRegisterRequest.getEquipmentName())
                .equipmentProductNumber(equipmentInspectionRegisterRequest.getEquipmentProductNumber())
                .equipmentManufacturer(equipmentInspectionRegisterRequest.getEquipmentManufacturer())
                .equipmentLocation(equipmentInspectionRegisterRequest.getEquipmentLocation())
                .equipmentState(equipmentInspectionRegisterRequest.getEquipmentState())
                .equipmentSchedule(equipmentInspectionRegisterRequest.getEquipmentSchedule())
                .build();

        return equipmentInspectionRepository.save(equipmentEntity);
    }

    public EquipmentInspectionResponse readEquipment(Long equipmentId) {

        EquipmentEntity equipmentEntity = equipmentInspectionRepository.findByEquipmentIdOptional(equipmentId)
                .orElseThrow(() -> new RuntimeException("해당 장비 정보가 없습니다."));

        return new EquipmentInspectionResponse(
                equipmentEntity.getEquipmentId(),
                equipmentEntity.getEquipmentName(),
                equipmentEntity.getEquipmentProductNumber(),
                equipmentEntity.getEquipmentManufacturer(),
                equipmentEntity.getEquipmentLocation(),
                equipmentEntity.getEquipmentState(),
                equipmentEntity.getEquipmentSchedule()
        );
    }

    public List<EquipmentInspectionResponse> readEquipmentByEquipmentName(String equipmentName) {

        List<EquipmentEntity> equipmentEntities = equipmentInspectionRepository.findAllByEquipmentName(equipmentName);

        return equipmentEntities.stream()
                .map(equipmentEntity -> new EquipmentInspectionResponse(
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

    public List<EquipmentInspectionResponse> readAllEquipment() {

        List<EquipmentEntity> equipmentEntities = equipmentInspectionRepository.findAll();

        return equipmentEntities.stream()
                .map(equipmentEntity -> new EquipmentInspectionResponse(
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
    public EquipmentEntity updateEquipment(Long equipmentId, EquipmentInspectionUpdateRequest equipmentInspectionUpdateRequest) {

        EquipmentEntity equipmentEntity = equipmentInspectionRepository.findByEquipmentIdOptional(equipmentId)
                .orElseThrow(() -> new RuntimeException("기존 해당 장비 정보가 없습니다."));

        if(!equipmentInspectionUpdateRequest.getEquipmentName().isBlank()
            || !equipmentInspectionUpdateRequest.getEquipmentProductNumber().isBlank()
            || !equipmentInspectionUpdateRequest.getEquipmentManufacturer().isBlank()){
            if(!equipmentInspectionUpdateRequest.getEquipmentName().isBlank()
                && !equipmentInspectionUpdateRequest.getEquipmentProductNumber().isBlank()
                && !equipmentInspectionUpdateRequest.getEquipmentManufacturer().isBlank()){
                throw new RuntimeException("장비의 이름, 제품번호, 제조사를 모두 채우세요.");
            }
        }

        EquipmentEntity updatedEntity = EquipmentEntity.builder()
                .equipmentId(equipmentId)
                .equipmentName(equipmentInspectionUpdateRequest.getEquipmentName() != null ? equipmentInspectionUpdateRequest.getEquipmentName() : equipmentEntity.getEquipmentName())
                .equipmentProductNumber(equipmentInspectionUpdateRequest.getEquipmentProductNumber() != null ? equipmentInspectionUpdateRequest.getEquipmentProductNumber() : equipmentEntity.getEquipmentProductNumber())
                .equipmentManufacturer(equipmentInspectionUpdateRequest.getEquipmentManufacturer() != null ? equipmentInspectionUpdateRequest.getEquipmentManufacturer() : equipmentEntity.getEquipmentManufacturer())
                .equipmentLocation(equipmentInspectionUpdateRequest.getEquipmentLocation() != null ? equipmentInspectionUpdateRequest.getEquipmentLocation() : equipmentEntity.getEquipmentLocation())
                .equipmentState(equipmentInspectionUpdateRequest.getEquipmentState() != null ? equipmentInspectionUpdateRequest.getEquipmentState() : equipmentEntity.getEquipmentState())
                .equipmentSchedule(equipmentInspectionUpdateRequest.getEquipmentSchedule() != null ? equipmentInspectionUpdateRequest.getEquipmentSchedule() : equipmentEntity.getEquipmentSchedule())
                .build();

        return equipmentInspectionRepository.save(updatedEntity);
    }

    @Transactional
    public EquipmentInspectionResponse deleteEquipment(Long equipmentId) {

        EquipmentEntity equipmentEntity = equipmentInspectionRepository.findByEquipmentIdOptional(equipmentId)
                .orElseThrow(() -> new RuntimeException("해당 장비 정보가 없습니다."));

        EquipmentInspectionResponse deletedEquipment = new EquipmentInspectionResponse(
                equipmentEntity.getEquipmentId(),
                equipmentEntity.getEquipmentName(),
                equipmentEntity.getEquipmentProductNumber(),
                equipmentEntity.getEquipmentManufacturer(),
                equipmentEntity.getEquipmentLocation(),
                equipmentEntity.getEquipmentState(),
                equipmentEntity.getEquipmentSchedule()
        );

        equipmentInspectionRepository.delete(equipmentEntity);

        return deletedEquipment;
    }
}
