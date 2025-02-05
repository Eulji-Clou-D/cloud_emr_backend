package com.cloud.emr.Affair.Patient.repository;


import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, String> {
    Optional<PatientEntity> findTopByOrderByPatientNoDesc();

    List<PatientEntity> findByPatientName(String patientName);

    Optional<PatientEntity> findByPatientNo(Long patientNo);

    Optional<PatientEntity> findByPatientNoAndPatientRrnIsNotNull(Long patientNo);

}
