package com.cloud.emr.Affair.DoctorTreatment.service;


import com.cloud.emr.Affair.DoctorTreatment.dto.DoctorTreatmentRequest;
import com.cloud.emr.Affair.DoctorTreatment.dto.DoctorTreatmentResponse;
import com.cloud.emr.Affair.DoctorTreatment.entity.DoctorTreatmentEntity;
import com.cloud.emr.Affair.DoctorTreatment.repository.DoctorTreatmentRepository;
import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorTreatmentService {

    @Autowired
    private DoctorTreatmentRepository doctorTreatmentRepository;
    @Autowired
    private UserRepository userRepository;


    public boolean isOverlap(Long reqUserId, LocalDateTime reqStartTime, LocalDateTime reqEndTime) {
        Optional<UserEntity> reqUser = userRepository.findById(reqUserId);
        List<DoctorTreatmentEntity> checks = doctorTreatmentRepository.findAllByUserEntity(reqUser);

        if (checks.isEmpty()) {
            return false;
        }

        for(DoctorTreatmentEntity check : checks){
            LocalDateTime existStart = check.getDoctorTreatmentStart();

            LocalDateTime existEnd = check.getDoctorTreatmentEnd();

            if (!((reqEndTime.isBefore(existStart) || reqStartTime.isAfter(existEnd)) && reqStartTime.isBefore(reqEndTime))) {
                return true; //겹침
            }
        }

        return false;
    }

    public DoctorTreatmentResponse createTreatment(DoctorTreatmentRequest doctorTreatmentRequest, UserEntity userEntity, PatientEntity patientEntity) {
        DoctorTreatmentEntity doctorTreatmentEntity = DoctorTreatmentEntity.builder()
                .doctorTreatmentId(doctorTreatmentRequest.getDoctorTreatmentId())
                .userEntity(userEntity)
                .patientEntity(patientEntity)
                .doctorTreatmentStart(doctorTreatmentRequest.getDoctorTreatmentStart())
                .doctorTreatmentEnd(doctorTreatmentRequest.getDoctorTreatmentEnd())
                .build();

        doctorTreatmentEntity = doctorTreatmentRepository.save(doctorTreatmentEntity);

        return new DoctorTreatmentResponse(
                doctorTreatmentEntity.getDoctorTreatmentId(),
                doctorTreatmentEntity.getUserEntity().getUserId(),
                doctorTreatmentEntity.getPatientEntity().getPatientNo(),
                doctorTreatmentEntity.getDoctorTreatmentStart(),
                doctorTreatmentEntity.getDoctorTreatmentEnd()
        );
    }
}
