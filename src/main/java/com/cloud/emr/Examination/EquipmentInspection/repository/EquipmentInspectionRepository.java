package com.cloud.emr.Examination.EquipmentInspection.repository;

import com.cloud.emr.Examination.Equipment.entity.EquipmentEntity;
import com.cloud.emr.Examination.EquipmentInspection.entity.EquipmentInspectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentInspectionRepository extends JpaRepository<EquipmentInspectionEntity, Long> {
    EquipmentInspectionEntity findByEquipmentInspectionId(Long equipmentInspectionId);

    Optional<EquipmentEntity> findByEquipmentIdOptional(Long equipmentId);

    List<EquipmentEntity> findAllByEquipmentName(String equipmentName);
}
