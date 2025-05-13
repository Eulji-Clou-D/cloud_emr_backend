package com.cloud.emr.Affair.Holiday.service;

import com.cloud.emr.Affair.Holiday.dto.HolidayRequest;
import com.cloud.emr.Affair.Holiday.dto.HolidayResponse;
import com.cloud.emr.Affair.Holiday.entity.HolidayEntity;
import com.cloud.emr.Affair.Holiday.repository.HolidayRepository;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import com.cloud.emr.Main.User.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    /** 1. 휴일 등록 **/
    @Transactional
    public HolidayResponse registerHoliday(HolidayRequest req) {
        HolidayEntity e = HolidayEntity.builder()
                .holidayDate(req.getHolidayDate())
                .holidayReason(req.getHolidayReason())
                .build();

        holidayRepository.save(e);
        return toDto(e);
    }

    /** 2. 휴일 수정 **/
    @Transactional
    public HolidayResponse updateHoliday(Long id, HolidayRequest req) {
        HolidayEntity e = holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("휴일 정보를 찾을 수 없습니다."));

        // 엔티티 새로 빌드 후 저장
        HolidayEntity updated = HolidayEntity.builder()
                .id(e.getId())
                .holidayDate(req.getHolidayDate() != null ? req.getHolidayDate() : e.getHolidayDate())
                .holidayReason(req.getHolidayReason() != null ? req.getHolidayReason() : e.getHolidayReason())
                .build();

        holidayRepository.save(updated);
        return toDto(updated);
    }

    /** 3. 휴일 삭제 **/
    @Transactional
    public void deleteHoliday(Long id) {
        HolidayEntity e = holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("휴일 정보를 찾을 수 없습니다."));
        holidayRepository.delete(e);
    }

    /** 4. 휴일 목록 조회 **/
    // 일 별
    // 주 별
    // 월 별
    // 분기 별
    // 년 별
//    @Transactional(readOnly = true)
//    public List<HolidayResponse> listByRole(RoleType role) {
//        return
//    }

    // ─────────────────────────────────────────────────────────
    private HolidayResponse toDto(HolidayEntity e) {
        return new HolidayResponse(
                e.getId(),
                e.getHolidayDate(),
                e.getHolidayReason()
        );
    }
}