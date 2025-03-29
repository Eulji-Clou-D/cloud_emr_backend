package com.cloud.emr.Examination.ExaminationSchedule.repository;

import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationScheduleRepository extends JpaRepository<ExaminationJournalEntity, Long> {
    List<ExaminationJournalEntity> findExaminationJournalEntitiesByPatientEntity(PatientEntity patientEntity);
}
