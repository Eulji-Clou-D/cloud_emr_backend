package com.cloud.emr.Affair.Reservation.repository;

import com.cloud.emr.Affair.CheckIn.entity.CheckInEntity;
import com.cloud.emr.Affair.Reservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {


    List<ReservationEntity> findByReservationYn(String Y);

    // 특정 환자(patientNo)와 예약 날짜(reservationDate)에 대한 예약이 존재하는지 확인
    boolean existsByPatientEntity_PatientNoAndReservationDate(Long patientNo, LocalDateTime reservationDate);

}

/*
  저장: save()
  조회: findById(), findAll(), findAllById(), count(), existsById()
  삭제: deleteById(), delete(), deleteAll(), deleteAllById()
*/