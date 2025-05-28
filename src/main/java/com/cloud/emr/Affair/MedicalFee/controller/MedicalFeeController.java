package com.cloud.emr.Affair.MedicalFee.controller;


import com.cloud.emr.Affair.MedicalFee.dto.MedicalFeeRequest;
import com.cloud.emr.Affair.MedicalFee.dto.MedicalFeeResponse;
import com.cloud.emr.Affair.MedicalFee.entity.MedicalTypeEntity;
import com.cloud.emr.Affair.MedicalFee.repository.MedicalTypeRepository;
import com.cloud.emr.Affair.MedicalFee.service.MedicalFeeService;
import com.cloud.emr.Affair.MedicalFee.service.MedicalTypeService;
import com.cloud.emr.Treatment.Treatment.entity.TreatmentEntity;
import com.cloud.emr.Treatment.Treatment.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/medicalfee")
public class MedicalFeeController {

    @Autowired
    private TreatmentService treatmentService;

    @Autowired
    private MedicalFeeService medicalFeeService;
    @Autowired
    private MedicalTypeRepository medicalTypeRepository;
    @Autowired
    private MedicalTypeService medicalTypeService;

/*
    //진료ID에 따른 진료 유형 등록
    @PostMapping("/register")
    public ResponseEntity<Object> registerMedicalFee(@RequestBody MedicalFeeRequest medicalFeeRequest, @RequestParam Long treatmentId) {
        try {
            // 해당 진료가 있나 ID로 조회
            TreatmentEntity treatmentEntity = treatmentService.findTreatmentById(treatmentId);
            if(treatmentEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of(
                                "message", "해당 진료 ID를 찾을 수 없습니다."
                        )
                );
            }

            // reqbody로 받은 유형ID가 있나 조회
            MedicalTypeEntity medicaltypeEntity = medicalTypeService.findMedicalTypeById(medicalFeeRequest.getMedicalTypeId());
            if(medicaltypeEntity == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        Map.of(
                                "message", "해당 진료 유형 ID를 찾을 수 없습니다."
                        )
                );
            }

            //등록 처리
            MedicalFeeResponse medicalFeeResponse = medicalFeeService.createMedicalFee(medicalFeeRequest, treatmentEntity, medicaltypeEntity);


            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "message", "등록 성공",
                            "data", medicalFeeResponse

                    )
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of(
                            "message", "등록 실패",
                            "error", e.getMessage()
                    )
            );
        }
    }
*/
}
