package com.cloud.emr.Affair.DocotorRecess.repository;

import com.cloud.emr.Affair.Contract.entity.ContractEntity;
import com.cloud.emr.Affair.DocotorRecess.entity.DoctorRecessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRecessRepository extends JpaRepository<DoctorRecessEntity, Long> {



}
