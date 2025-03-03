package com.cloud.emr.Examination.Examination.repository;

import com.cloud.emr.Examination.Equipment.entity.EquipmentEntity;
import com.cloud.emr.Examination.Examination.entity.ExaminationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExaminationRepository extends JpaRepository<ExaminationEntity, Long> {

    Optional<ExaminationEntity> findByExaminationIdOptional(Long examinationId);

    ExaminationEntity findByExaminationId(Long examinationId);

    ExaminationEntity findByExaminationName(String examinationName);

    List<ExaminationEntity> findAllByEquipmentEntity(EquipmentEntity equipment);
}
