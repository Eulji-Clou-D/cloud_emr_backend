package com.cloud.emr.Affair.MedicalFee.repository;

import com.cloud.emr.Affair.MedicalFee.entity.MedicalFeeEntity;
import com.cloud.emr.Treatment.Treatment.entity.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalFeeRepository extends JpaRepository <MedicalFeeEntity, Long> {
    List<MedicalFeeEntity> findMedicalFeeEntitiesByTreatmentEntity(TreatmentEntity treatmentEntity);
}
