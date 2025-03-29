package com.cloud.emr.Examination.ExaminationJournal.repository;

import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Examination.ExaminationJournal.entity.ExaminationJournalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationJournalRepository extends JpaRepository<ExaminationJournalEntity, Long> {
    List<ExaminationJournalEntity> findExaminationJournalEntitiesByPatientEntity(PatientEntity patientEntity);
}
