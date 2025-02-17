package com.cloud.emr.Affair.Treatment.entity;


import com.cloud.emr.Affair.CheckIn.entity.CheckInEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity(name = "Treatment")
@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id", nullable = false, columnDefinition = "INT")
    private Long treatmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkIn_id", referencedColumnName = "checkIn_id", nullable = false)
    private CheckInEntity checkInEntity;

    @CreationTimestamp
    @Column(name = "treatment_date", columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date treatmentDate;

    @Column(name = "treatment_status")
    private String treatmentStatus;

    @Column(name = "treatment_nextdate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date treatmentNextDate;

    @Column(name = "treatment_comment")
    private String treatmentComment;

    //진료과
    @Column(name = "treatment_dept")
    private String treatmentDept;

    //진료의
    @Column(name = "treatment_doc")
    private String treatmentDoc;

    //총 진료비
    @Column(name = "treatment_total_fee")
    private Long treatmentTotalFee;


}
