package com.cloud.emr.Treatment.OutTreatment.entity;


import com.cloud.emr.Affair.CheckIn.entity.CheckInEntity;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Treatment.Treatment.entity.TreatmentEntity;
import com.cloud.emr.Treatment.Treatment.status.TreatmentUseType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/*
로직 설명
접수 -> 진료예약(생성) ->  외래 실제 진료시 볼경우 진료여부 Y - order가 생성


 */
@Entity(name = "Out_Treatments")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OutTreatmentEntity {


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


    //이전진료
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pre_treatment_id")
    private OutTreatmentEntity preTreatment;

    @Column(name = "treatment_comment")
    private String treatmentComment;


}
