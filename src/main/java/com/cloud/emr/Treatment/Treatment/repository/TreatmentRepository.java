package com.cloud.emr.Treatment.Treatment.repository;


import com.cloud.emr.Treatment.Treatment.entity.TreatmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository extends JpaRepository<TreatmentEntity, Long> {


}
