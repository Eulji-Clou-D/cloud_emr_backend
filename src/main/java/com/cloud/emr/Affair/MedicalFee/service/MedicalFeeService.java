package com.cloud.emr.Affair.MedicalFee.service;

import com.cloud.emr.Affair.MedicalFee.dto.MedicalFeeRequest;
import com.cloud.emr.Affair.MedicalFee.dto.MedicalFeeResponse;
import com.cloud.emr.Affair.MedicalFee.entity.MedicalFeeEntity;
import com.cloud.emr.Affair.MedicalFee.entity.MedicalTypeEntity;
import com.cloud.emr.Affair.MedicalFee.repository.MedicalFeeRepository;
import com.cloud.emr.Treatment.Treatment.entity.TreatmentEntity;
import com.cloud.emr.Treatment.Treatment.repository.TreatmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalFeeService {
    private final MedicalFeeRepository medicalFeeRepository;
    private final TreatmentRepository treatmentRepository;

    public MedicalFeeService(MedicalFeeRepository medicalFeeRepository, TreatmentRepository treatmentRepository) {
        this.medicalFeeRepository = medicalFeeRepository;
        this.treatmentRepository = treatmentRepository;
    }

    @Transactional
    public MedicalFeeResponse createMedicalFee(MedicalFeeRequest medicalFeeRequest, TreatmentEntity treatmentEntity, MedicalTypeEntity medicalTypeEntity) {
        MedicalFeeEntity medicalFeeEntity = MedicalFeeEntity.builder()
                .medicalTypeEntity(medicalTypeEntity)
                .treatmentEntity(treatmentEntity)
                .build();

        medicalFeeEntity = medicalFeeRepository.save(medicalFeeEntity);

        return new MedicalFeeResponse(
                medicalFeeEntity.getMedicalFeeId(),
                medicalFeeEntity.getMedicalTypeEntity().getMedicalTypeId(),
                medicalFeeEntity.getTreatmentEntity().getTreatmentId()
        );
    }
/*
    // medicalfee 생성시 자동으로 총 금액이 업데이트 되도록
    // 추후 MedicalType Update시 수정된 총 금액이 반영되도록 업데이트가 필요함
    @Transactional
    public TreatmentResponse updateTotalFeeForTreatment(Long treatmentId) {
        TreatmentEntity treatment = treatmentRepository.findById(treatmentId).orElseThrow(
                () -> new IllegalArgumentException("Invalid treatment Id: " + treatmentId)
        );


        List<MedicalFeeEntity> medicalFees = medicalFeeRepository.findMedicalFeeEntitiesByTreatmentEntity(treatment);

        Long totalFee = medicalFees.stream()
                .mapToLong(medicalFee -> medicalFee.getMedicalTypeEntity().getMedicalTypeFee())
                .sum();

        // 기존 객체의 필드를 유지하면서 새로운 값만 변경
        TreatmentEntity updatedTreatment = TreatmentEntity.builder()
                .treatmentId(treatment.getTreatmentId())
                .checkInEntity(treatment.getCheckInEntity())
                .treatmentDate(treatment.getTreatmentDate())
                .treatmentStatus(treatment.getTreatmentStatus())
                .treatmentNextDate(treatment.getTreatmentNextDate())
                .treatmentComment(treatment.getTreatmentComment())
                .treatmentDept(treatment.getTreatmentDept())
                .treatmentDoc(treatment.getTreatmentDoc())
                .treatmentTotalFee(totalFee) // 변경할 필드만 수정
                .build();

        treatmentRepository.save(updatedTreatment);

        TreatmentResponse treatmentResponse = new TreatmentResponse(
                updatedTreatment.getTreatmentId(),
                updatedTreatment.getCheckInEntity().getCheckInId(),
                updatedTreatment.getTreatmentDate(),
                updatedTreatment.getTreatmentStatus(),
                updatedTreatment.getTreatmentNextDate(),
                updatedTreatment.getTreatmentComment(),
                updatedTreatment.getTreatmentDept(),
                updatedTreatment.getTreatmentDoc(),
                updatedTreatment.getTreatmentTotalFee()
        );


        return null;



    }

 */
}
