package com.cloud.emr.Treatment.Treatment.entity;


import com.cloud.emr.Affair.CheckIn.entity.CheckInEntity;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Treatment.Treatment.status.TreatmentType;
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
접수 -> 진료예약(생성) -> 실제 진료시 볼경우 진료여부 Y - order가 생성


 */
@Entity(name = "Treatments")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id", nullable = false)
    private Long treatmentId;

    @Column(name = "treatment_status")
    @Enumerated(EnumType.STRING)
    private TreatmentUseType treatmentStatus;

    @Column(name = "treatment_type")
    @Enumerated(EnumType.STRING)
    private TreatmentType TreatmentType;

    //진료생성일
    @CreationTimestamp
    @Column(name = "treatment_date", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date treatmentDate;

    //진료과
    @Column(name = "treatment_dept")
    private String treatmentDept;

    //진료의
    @JoinColumn(name = "treatment_doc")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity treatmentDoc;



}
