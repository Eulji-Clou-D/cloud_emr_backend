package com.cloud.emr.Affair.Reservation.service;

import com.cloud.emr.Affair.Patient.entity.PatientEntity;
import com.cloud.emr.Affair.Patient.repository.PatientRepository;
import com.cloud.emr.Affair.Reservation.dto.ReservationRegisterRequest;
import com.cloud.emr.Affair.Reservation.dto.ReservationReadResponse;
import com.cloud.emr.Affair.Reservation.dto.ReservationUpdateRequest;
import com.cloud.emr.Affair.Reservation.entity.ReservationEntity;
import com.cloud.emr.Affair.Reservation.repository.ReservationRepository;
import com.cloud.emr.Main.User.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PatientRepository patientRepository;


    // 1. 예약 등록
    @Transactional  // 트랜잭션을 적용하여 작업이 하나의 단위로 처리
    public ReservationReadResponse createReservation(ReservationRegisterRequest request, UserEntity userEntity, PatientEntity patientEntity) {

        // 중복 예약 체크
        boolean isDuplicate = reservationRepository.existsByPatientEntity_PatientNoAndReservationDate(request.getPatientNo(), request.getReservationDate());
        if (isDuplicate) {
            throw new IllegalStateException("이미 예약된 시간입니다.");
        }

        // 예약 엔티티 생성 (예약 여부 'Y'로 바로 설정)
        ReservationEntity reservationEntity = ReservationEntity.builder()
                .patientEntity(patientEntity)
                .reservationDate(request.getReservationDate())
                .reservationYn("Y") // 예약 완료 상태
                .build();

        // 예약 저장
        reservationEntity = reservationRepository.save(reservationEntity);

        // 예약 응답 생성
        return new ReservationReadResponse(
                reservationEntity.getReservationId(),
                reservationEntity.getPatientEntity().getPatientNo(),
                reservationEntity.getReservationDate(),
                reservationEntity.getReservationYn()
        );
    }


    // 2. 예약 목록 조회 메서드
    public List<ReservationReadResponse> getAllReservations() {
        // 모든 예약 조회 (예약 상태가 'Y'인 예약만 조회)
        List<ReservationEntity> reservationEntities = reservationRepository.findByReservationYn("Y");

        // ReservationEntity를 ReservationReadResponse로 변환
        return reservationEntities.stream().map(reservationEntity -> {
            // 예약 목록에 필요한 정보만 담아서 응답 생성
            ReservationReadResponse response = new ReservationReadResponse(
                    reservationEntity.getReservationId(),
                    reservationEntity.getPatientEntity().getPatientNo(),
                    reservationEntity.getReservationDate(),
                    reservationEntity.getReservationYn() // 예약 날짜 시간
            );
            return response;
        }).collect(Collectors.toList());
    }


    // 3. 예약 변경
    public ReservationReadResponse updateReservation(Long reservationId, ReservationUpdateRequest updateRequest) {
        // 예약 조회
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        // 빌더로 업데이트된 예약 엔티티 생성
        ReservationEntity updatedReservationEntity = ReservationEntity.builder()
                .reservationId(reservationEntity.getReservationId())
                .patientEntity(reservationEntity.getPatientEntity())
                .reservationDate(
                        updateRequest.getNewReservationDate() != null ? updateRequest.getNewReservationDate() : reservationEntity.getReservationDate() // 예약 날짜 변경만 할 수도 있음
                )
                .reservationYn(
                        updateRequest.getReservationYn() != null ? updateRequest.getReservationYn() : reservationEntity.getReservationYn() // 예약 취소만 할 수도 있음
                )
                .reservationChangeDate(LocalDateTime.now()) // 예약이 변경될 날짜와 시간 (사용자 입력)
                .reservationChangeCause(updateRequest.getReservationChangeCause() != null ? updateRequest.getReservationChangeCause() : "예약 변경 사유")  // 변경 사유 (동적으로 설정)
                .build();

        // 업데이트된 예약 엔티티 저장
        reservationRepository.save(updatedReservationEntity);

        // 반환할 DTO 생성
        return new ReservationReadResponse(
                updatedReservationEntity.getReservationId(),
                updatedReservationEntity.getPatientEntity().getPatientNo(),
                updatedReservationEntity.getReservationDate(),
                updatedReservationEntity.getReservationYn()
        );
    }

}
