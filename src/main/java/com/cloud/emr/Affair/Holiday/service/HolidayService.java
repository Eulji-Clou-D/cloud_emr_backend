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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
                .holidayNational(req.getHolidayNational())
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
                .holidayNational(req.getHolidayNational() != null ? req.getHolidayNational() : e.getHolidayNational())
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

    /** 4. 휴일 목록 조회, 일 주 월 분기 년 구분 **/
    @Transactional(readOnly = true)
    public List<HolidayResponse> listByPeriod(String period, String number) {
        if(!validatePeriod(period, number)) return Collections.emptyList();

        List<HolidayEntity> holidayDateList = getHolidayDateByNumber(period, number);
        if (holidayDateList.isEmpty()) return Collections.emptyList();
        return holidayDateList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────────────────────────
    private Boolean validatePeriod(String period, String number) {
        return ("day".equals(period) && number.length() == 8) || ("year".equals(period) && number.length() == 4);
    }

    private List<HolidayEntity> getHolidayDateByNumber(String period, String number) {
        if("day".equals(period)) {
            int year = Integer.parseInt(number.substring(0, 4));
            int month = Integer.parseInt(number.substring(4, 6));
            int day = Integer.parseInt(number.substring(6, 8));

            return List.of(holidayRepository.findByHolidayDate(LocalDate.of(year, month, day)));
        }
        else if("year".equals(period)){
            int year = Integer.parseInt(number);
            LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
            LocalDateTime end = LocalDateTime.of(year, 12, 31, 23, 59, 59, 999_999_999);
            return holidayRepository.findAllByHolidayDateBetween(start, end);
        }
        return Collections.emptyList();
    }

    private HolidayResponse toDto(HolidayEntity e) {
        return new HolidayResponse(
                e.getId(),
                e.getHolidayDate(),
                e.getHolidayNational(),
                e.getHolidayReason()
        );
    }

}