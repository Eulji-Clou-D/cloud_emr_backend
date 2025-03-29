package com.cloud.emr.Examination.EquipmentJournal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentJournalRepository extends JpaRepository<EquipmentEntity, Long> {
    EquipmentEntity findByEquipmentId(Long equipmentId);

    Optional<EquipmentEntity> findByEquipmentIdOptional(Long equipmentId);

    List<EquipmentEntity> findAllByEquipmentName(String equipmentName);
}
