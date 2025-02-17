package com.cloud.emr.Affair.MedicalFee.repository;


import com.cloud.emr.Affair.MedicalFee.entity.MedicalTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalTypeRepository extends JpaRepository<MedicalTypeEntity, Long> {
    MedicalTypeEntity findByMedicalTypeName(String medicalTypeName);
}
