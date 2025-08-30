package com.cloud.emr.Affair.DoctorTreatment.repository;

import com.cloud.emr.Affair.DoctorTreatment.entity.DoctorTreatmentEntity;
import com.cloud.emr.Main.User.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorTreatmentRepository extends JpaRepository<DoctorTreatmentEntity, Long> {
    List<DoctorTreatmentEntity> findAllByUserEntity(Optional<UserEntity> userEntity);

    List<DoctorTreatmentEntity> findByUserEntity(UserEntity targetUser);
}
