package com.cloud.emr.Affair.Disability.repository;

import com.cloud.emr.Affair.Disability.entity.DisabilityEntity;
import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisabilityRepository extends JpaRepository <DisabilityEntity,Long> {

    Optional<DisabilityEntity> findByPatientEntity_PatientNo(Long patientNo);

    DisabilityEntity findByPatientEntity(PatientEntity patient);
}
