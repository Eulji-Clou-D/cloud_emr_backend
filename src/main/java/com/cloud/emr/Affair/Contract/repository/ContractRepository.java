package com.cloud.emr.Affair.Contract.repository;

import com.cloud.emr.Affair.Contract.entity.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {
}
