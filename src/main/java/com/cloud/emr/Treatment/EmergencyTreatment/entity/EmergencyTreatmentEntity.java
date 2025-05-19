package com.cloud.emr.Treatment.EmergencyTreatment.entity;


import com.cloud.emr.Affair.CheckIn.entity.CheckInEntity;
import com.cloud.emr.Treatment.Treatment.entity.TreatmentEntity;
import com.cloud.emr.Treatment.Treatment.status.TreatmentUseType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
로직 설명
접수 -> 진료예약(생성) ->  외래 실제 진료시 볼경우 진료여부 Y - order가 생성


 */
@Entity(name = "Emergency_Treatments")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyTreatmentEntity {



    @Id
    private Long id;

    @MapsId
    @OneToOne(targetEntity = TreatmentEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id")
    private TreatmentEntity treatmentId;

    //접수 정보
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkIn_id", referencedColumnName = "checkIn_id", nullable = false)
    private CheckInEntity checkInEntity;


    @Column(name = "treatment_status")
    @Enumerated(EnumType.STRING)
    private TreatmentUseType treatmentStatus;


}
