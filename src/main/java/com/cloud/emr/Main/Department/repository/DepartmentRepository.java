package com.cloud.emr.Main.Department.repository;

import com.cloud.emr.Main.Department.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity,Long> {
}
