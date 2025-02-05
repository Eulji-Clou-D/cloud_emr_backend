package com.cloud.emr.Affair.Patient.service;

import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Affair.Patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private static PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public static List<PatientEntity> searchPatientByName(String patientName) {
        if(patientName == null || patientName.isEmpty()) {
            throw new IllegalArgumentException("환자 이름은 필수입니다.");
        }

        return patientRepository.findByPatientName(patientName);
    }

    public PatientEntity findPatientByNo(Long patientNo) {
        return patientRepository.findByPatientNo(patientNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 환자를 찾을 수 없습니다." + patientNo));
    }

    public String findPatientRrnByPatientNo(Long patientNo) {
        return patientRepository.findByPatientNoAndPatientRrnIsNotNull(patientNo)
                .map(PatientEntity::getPatientRrn) // PatientEntity에서 rrn 값만 추출
                .orElseThrow(() -> new IllegalArgumentException("해당 환자의 주민번호를 찾을 수 없습니다. patientNo: " + patientNo));
    }

}
