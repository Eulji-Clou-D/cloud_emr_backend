package com.cloud.emr.Examination.EquipmentJournal.entity;

import com.cloud.emr.Main.User.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name = "Equipment_Journal")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentJournalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_journal_id", nullable = false)
    private Long equipmentJournalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipment_id", nullable = false)
    private com.cloud.emr.Examination.EquipmentJournal.entity.EquipmentEntity EquipmentEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity UserEntity;

    @Column(name = "equipment_inspection_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date equipmentInspectionDate;

    @Column(name = "equipment_inspection_result")
    private String equipmentInspectionResult;

    @Column(name = "equipment_inspection_records")
    private String equipmentInspectionRecords;

    @Column(name = "equipment_inspection_notes")
    private String equipmentInspectionNotes;

    // 이 아래는 그냥 다른 테이블에서 가져와서 화면에 띄울까?
//    @Column(name = "equipment_location")
//    private String equipmentLocation;

    //@Column(name = "equipment_name")
    //private String equipmentName;
}
