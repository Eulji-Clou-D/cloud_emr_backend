package com.cloud.emr.Examination.ExaminationResult.repository;

import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationResultRepository extends JpaRepository<ExaminationJournalEntity, Long> {
    List<ExaminationJournalEntity> findExaminationJournalEntitiesByPatientEntity(PatientEntity patientEntity);
}
