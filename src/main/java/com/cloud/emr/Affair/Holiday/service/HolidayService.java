package com.cloud.emr.Affair.Holiday.service;

import com.cloud.emr.Affair.Holiday.dto.HolidayRequest;
import com.cloud.emr.Affair.Holiday.dto.HolidayResponse;
import com.cloud.emr.Affair.Holiday.entity.HolidayEntity;
import com.cloud.emr.Affair.Holiday.repository.HolidayRepository;
import com.cloud.emr.Main.User.entity.UserEntity;
import com.cloud.emr.Main.User.repository.UserRepository;
import com.cloud.emr.Main.User.type.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    /** 1. 휴일 등록 **/
    @Transactional(rollbackFor = Exception.class)
    public void registerHoliday(HolidayRequest req) {
        try {
            HolidayEntity e = HolidayEntity.builder()
                    .holidayDate(req.getHolidayDate())
                    .holidayNational(req.getHolidayNational())
                    .holidayReason(req.getHolidayReason())
                    .build();

            holidayRepository.save(e);
        } catch (Exception e) {
            System.out.printf("휴일 등록 중 오류 발생: %s\n", e);
            throw new RuntimeException("휴일 등록 중 오류 발생", e);
        }

    }

    /** 2. 휴일 수정 **/
    @Transactional(rollbackFor = Exception.class)
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
        return new HolidayResponse(updated);
    }

    /** 3. 휴일 삭제 **/
    @Transactional(rollbackFor = Exception.class)
    public void deleteHoliday(Long id) {
        HolidayEntity e = holidayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("휴일 정보를 찾을 수 없습니다."));
        holidayRepository.delete(e);
    }

    /** 4. 휴일 목록 조회, 일 주 월 분기 년 기간 구분 **/
    @Transactional(readOnly = true)
    public HolidayResponse readHolidayByDay(String yearMonthDay) {
        LocalDate date = LocalDate.parse(yearMonthDay, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return new HolidayResponse(holidayRepository.findByHolidayDate(date));
    }

    @Transactional(readOnly = true)
    public List<HolidayResponse> readHolidayByWeek(String yearMonthWeek) {
        // yearWeek format: yyyyMMWd (e.g., 202507W1)
        List<LocalDate> weekDaySpan = convertWeekToDaySpan(yearMonthWeek);
        LocalDateTime startDate = weekDaySpan.get(0).atStartOfDay();
        LocalDateTime endDate = weekDaySpan.get(1).atTime(23, 59, 59);
        return findBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponse> readHolidayByMonth(String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyyMM"));
        LocalDateTime startDate = ym.atDay(1).atStartOfDay();
        LocalDateTime endDate = ym.atEndOfMonth().atTime(23, 59, 59);
        return findBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponse> readHolidayByQuarter(String yearQuarter) {
        // yearQuarter format: YYYYQn (e.g., 2025Q1)
        String[] parts = yearQuarter.split("Q");
        int year = Integer.parseInt(parts[0]);
        int quarter = Integer.parseInt(parts[1]);
        Month startMonth = Month.of((quarter - 1) * 3 + 1);
        YearMonth startYm = YearMonth.of(year, startMonth);
        YearMonth endYm = startYm.plusMonths(2);
        LocalDateTime startDate = startYm.atDay(1).atStartOfDay();
        LocalDateTime endDate = endYm.atEndOfMonth().atTime(23, 59, 59);
        return findBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponse> readHolidayByYear(String yearStr) {
        int year = Integer.parseInt(yearStr);
        LocalDateTime startDate = LocalDate.of(year, 1, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(year, 12, 31).atTime(23, 59, 59);
        return findBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponse> readHolidayByRange(String startStr, String endStr) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime startDate = LocalDate.parse(startStr, fmt).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(endStr, fmt).atTime(23, 59, 59);
        return findBetween(startDate, endDate);
    }

    private List<HolidayResponse> findBetween(LocalDateTime start, LocalDateTime end) {
        List<HolidayEntity> list = holidayRepository.findAllByHolidayDateBetween(start, end);
        return mapHolidayEntitiesToHolidayResponse(list);
    }

    // ─────────────────────────────────────────────────────────
    // Maybe move this to core common?
    private List<HolidayResponse> mapHolidayEntitiesToHolidayResponse(List<HolidayEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(HolidayResponse::new)
                .collect(Collectors.toList());
    }
    public static List<LocalDate> convertWeekToDaySpan(String yearWeek) {
        int weekNum = Integer.parseInt(yearWeek.substring(yearWeek.length() - 1));
        if (weekNum > 5) {
            throw new IllegalArgumentException("주차 값은 5 이하이어야 합니다.");
        }

        List<LocalDate> input = splitWeekToSevenDays(yearWeek);

        int totalTrueDays = getTrueDaysInMonthWeek(input);
        LocalDate weekEnd = input.get(totalTrueDays-1);
        int daysToSubtract = weekNum == 1 ? totalTrueDays - 1: 6;
        LocalDate weekStart = weekEnd.minusDays(daysToSubtract);

        if (weekNum == 5) {
            weekEnd = weekStart;
            for (int i = 0; i < 7; i++) {
                if (weekEnd.getMonth().getValue() != weekStart.plusDays(i).getMonth().getValue()) break;
                weekEnd = weekStart.plusDays(i);
            }
        }

        System.out.println(weekStart + " ~ " + weekEnd);
        return List.of(weekStart, weekEnd);
    }

    public static List<LocalDate> splitWeekToSevenDays(String yearWeek) {
        String week = yearWeek.substring(7);
        int day = Integer.parseInt(week.equals("1") ? "07" : String.valueOf(Integer.parseInt(week) * 7));
        int month = Integer.parseInt(yearWeek.substring(4, 6));
        int year = Integer.parseInt(yearWeek.substring(0, 4));
        LocalDate end = LocalDate.of(year, month, day);
        List<LocalDate> result = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            result.add(end.minusDays(i));
        }
        return result;
    }

    public static int getTrueDaysInMonthWeek(List<LocalDate> input){
        // Get ISO week and week-based year
        LocalDate weekEnd = input.get(0);
        int count = 0;

        for (LocalDate date : input) {
            if (getWeekStartOfDate(weekEnd) != getWeekStartOfDate(date)) break;
            weekEnd = date;
            count++;
        }

        return count;
    }

    public static LocalDate getWeekStartOfDate(LocalDate date){
        int week = date.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        int weekYear = date.get(WeekFields.ISO.weekBasedYear());

        // Get first day of the week (Monday)
        LocalDate weekStart = LocalDate
                .now()
                .with(WeekFields.ISO.weekBasedYear(), weekYear)
                .with(WeekFields.ISO.weekOfWeekBasedYear(), week)
                .with(ChronoField.DAY_OF_WEEK, 1);  // Monday

        return weekStart;
    }


}