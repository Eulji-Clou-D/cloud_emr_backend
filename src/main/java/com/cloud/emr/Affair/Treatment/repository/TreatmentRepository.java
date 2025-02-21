package com.cloud.emr.Affair.Treatment.repository;


import com.cloud.emr.Affair.Treatment.entity.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository extends JpaRepository<TreatmentEntity, Long> {


}
