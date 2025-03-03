package com.cloud.emr.Examination.Equipment.service;

import com.cloud.emr.Examination.Equipment.dto.EquipmentRegisterRequest;
import com.cloud.emr.Examination.Equipment.dto.EquipmentResponse;
import com.cloud.emr.Examination.Equipment.dto.EquipmentUpdateRequest;
import com.cloud.emr.Examination.Equipment.entity.EquipmentEntity;
import com.cloud.emr.Examination.Equipment.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public EquipmentEntity registerEquipment(EquipmentRegisterRequest equipmentRegisterRequest) {

        EquipmentEntity equipmentEntity = EquipmentEntity.builder()
                .equipmentName(equipmentRegisterRequest.getEquipmentName())
                .equipmentProductNumber(equipmentRegisterRequest.getEquipmentProductNumber())
                .equipmentManufacturer(equipmentRegisterRequest.getEquipmentManufacturer())
                .equipmentLocation(equipmentRegisterRequest.getEquipmentLocation())
                .equipmentState(equipmentRegisterRequest.getEquipmentState())
                .equipmentSchedule(equipmentRegisterRequest.getEquipmentSchedule())
                .build();

        return equipmentRepository.save(equipmentEntity);
    }

    public EquipmentResponse readEquipment(Long equipmentId) {

        EquipmentEntity equipmentEntity = equipmentRepository.findByEquipmentIdOptional(equipmentId)
                .orElseThrow(() -> new RuntimeException("해당 장비 정보가 없습니다."));

        return new EquipmentResponse(
                equipmentEntity.getEquipmentId(),
                equipmentEntity.getEquipmentName(),
                equipmentEntity.getEquipmentProductNumber(),
                equipmentEntity.getEquipmentManufacturer(),
                equipmentEntity.getEquipmentLocation(),
                equipmentEntity.getEquipmentState(),
                equipmentEntity.getEquipmentSchedule()
        );
    }

    public List<EquipmentResponse> readEquipmentByEquipmentName(String equipmentName) {

        List<EquipmentEntity> equipmentEntities = equipmentRepository.findAllByEquipmentName(equipmentName);

        return equipmentEntities.stream()
                .map(equipmentEntity -> new EquipmentResponse(
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

    public List<EquipmentResponse> readAllEquipment() {

        List<EquipmentEntity> equipmentEntities = equipmentRepository.findAll();

        return equipmentEntities.stream()
                .map(equipmentEntity -> new EquipmentResponse(
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
    public EquipmentEntity updateEquipment(Long equipmentId, EquipmentUpdateRequest equipmentUpdateRequest) {

        EquipmentEntity equipmentEntity = equipmentRepository.findByEquipmentIdOptional(equipmentId)
                .orElseThrow(() -> new RuntimeException("기존 해당 장비 정보가 없습니다."));

        if(!equipmentUpdateRequest.getEquipmentName().isBlank()
            || !equipmentUpdateRequest.getEquipmentProductNumber().isBlank()
            || !equipmentUpdateRequest.getEquipmentManufacturer().isBlank()){
            if(!equipmentUpdateRequest.getEquipmentName().isBlank()
                && !equipmentUpdateRequest.getEquipmentProductNumber().isBlank()
                && !equipmentUpdateRequest.getEquipmentManufacturer().isBlank()){
                throw new RuntimeException("장비의 이름, 제품번호, 제조사를 모두 채우세요.");
            }
        }

        EquipmentEntity updatedEntity = EquipmentEntity.builder()
                .equipmentId(equipmentId)
                .equipmentName(equipmentUpdateRequest.getEquipmentName() != null ? equipmentUpdateRequest.getEquipmentName() : equipmentEntity.getEquipmentName())
                .equipmentProductNumber(equipmentUpdateRequest.getEquipmentProductNumber() != null ? equipmentUpdateRequest.getEquipmentProductNumber() : equipmentEntity.getEquipmentProductNumber())
                .equipmentManufacturer(equipmentUpdateRequest.getEquipmentManufacturer() != null ? equipmentUpdateRequest.getEquipmentManufacturer() : equipmentEntity.getEquipmentManufacturer())
                .equipmentLocation(equipmentUpdateRequest.getEquipmentLocation() != null ? equipmentUpdateRequest.getEquipmentLocation() : equipmentEntity.getEquipmentLocation())
                .equipmentState(equipmentUpdateRequest.getEquipmentState() != null ? equipmentUpdateRequest.getEquipmentState() : equipmentEntity.getEquipmentState())
                .equipmentSchedule(equipmentUpdateRequest.getEquipmentSchedule() != null ? equipmentUpdateRequest.getEquipmentSchedule() : equipmentEntity.getEquipmentSchedule())
                .build();

        return equipmentRepository.save(updatedEntity);
    }

    @Transactional
    public EquipmentResponse deleteEquipment(Long equipmentId) {

        EquipmentEntity equipmentEntity = equipmentRepository.findByEquipmentIdOptional(equipmentId)
                .orElseThrow(() -> new RuntimeException("해당 장비 정보가 없습니다."));

        EquipmentResponse deletedEquipment = new EquipmentResponse(
                equipmentEntity.getEquipmentId(),
                equipmentEntity.getEquipmentName(),
                equipmentEntity.getEquipmentProductNumber(),
                equipmentEntity.getEquipmentManufacturer(),
                equipmentEntity.getEquipmentLocation(),
                equipmentEntity.getEquipmentState(),
                equipmentEntity.getEquipmentSchedule()
        );

        equipmentRepository.delete(equipmentEntity);

        return deletedEquipment;
    }
}
