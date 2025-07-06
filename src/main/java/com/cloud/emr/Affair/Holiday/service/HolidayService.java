package com.cloud.emr.Affair.Holiday.service;

import com.cloud.emr.Affair.Holiday.dto.HolidayRequest;
import com.cloud.emr.Affair.Holiday.dto.HolidayResponse;
import com.cloud.emr.Affair.Holiday.entity.HolidayEntity;
import com.cloud.emr.Affair.Holiday.repository.HolidayRepository;
import com.cloud.emr.Affair.Holiday.type.HolidayDateType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
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
        if (!HolidayDateType.DAY.getTypeName().equals(HolidayDateType.checkDateString(req.getHolidayDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))))) {
            throw new IllegalArgumentException("Expected [Day Date] format but received a different format for date string: " + req.getHolidayDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        }

        boolean exists = holidayRepository.existsByHolidayDate(req.getHolidayDate());
        if (exists) {
            throw new IllegalStateException("해당 날짜는 이미 휴일로 등록되어 있습니다.");
        }

        HolidayEntity e = HolidayEntity.builder()
                .holidayDate(req.getHolidayDate())
                .holidayNational(req.getHolidayNational())
                .holidayReason(req.getHolidayReason())
                .build();

        holidayRepository.save(e);
    }

    /** 2. 휴일 수정 **/
    @Transactional(rollbackFor = Exception.class)
    public HolidayResponse updateHoliday(Long id, HolidayRequest req) {
        if (!HolidayDateType.DAY.getTypeName().equals(HolidayDateType.checkDateString(req.getHolidayDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))))) {
            throw new IllegalArgumentException("Expected [Day Date] format but received a different format for date string: " + req.getHolidayDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        }

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
        if (!HolidayDateType.DAY.getTypeName().equals(HolidayDateType.checkDateString(yearMonthDay))) {
            throw new IllegalArgumentException("Expected [Day Date] format but received a different format for date string: " + yearMonthDay);
        }

        LocalDate date = LocalDate.parse(yearMonthDay, DateTimeFormatter.ofPattern("yyyyMMdd"));
        HolidayEntity entity = holidayRepository.findByHolidayDate(date);
        if (entity == null) {
            throw new NoSuchElementException("해당 날짜는 휴일이 아닙니다.");
        }
        return new HolidayResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponse> readHolidayByWeek(String yearMonthWeek) {
        // yearMonthWeek format: yyyyMMWd (e.g., 202507W1)
        if (!HolidayDateType.WEEK.getTypeName().equals(HolidayDateType.checkDateString(yearMonthWeek))) {
            throw new IllegalArgumentException("Expected [Week Date] format but received a different format for date string: " + yearMonthWeek);
        }

        List<LocalDate> weekDaySpan = convertWeekToDaySpan(yearMonthWeek);
        LocalDate startDate = weekDaySpan.get(0);
        LocalDate endDate = weekDaySpan.get(1);
        return findBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponse> readHolidayByMonth(String yearMonth) {
        if (!HolidayDateType.MONTH.getTypeName().equals(HolidayDateType.checkDateString(yearMonth))) {
            throw new IllegalArgumentException("Expected [Month Date] format but received a different format for date string: " + yearMonth);
        }

        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyyMM"));
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();
        return findBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponse> readHolidayByQuarter(String yearQuarter) {
        // yearQuarter format: YYYYQn (e.g., 2025Q1)
        if (!HolidayDateType.QUARTER.getTypeName().equals(HolidayDateType.checkDateString(yearQuarter))) {
            throw new IllegalArgumentException("Expected [Quarter Date] format but received a different format for date string: " + yearQuarter);
        }

        String[] parts = yearQuarter.split("Q");
        int year = Integer.parseInt(parts[0]);
        int quarter = Integer.parseInt(parts[1]);
        Month startMonth = Month.of((quarter - 1) * 3 + 1);
        YearMonth startYm = YearMonth.of(year, startMonth);
        YearMonth endYm = startYm.plusMonths(2);
        LocalDate startDate = startYm.atDay(1);
        LocalDate endDate = endYm.atEndOfMonth();
        return findBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponse> readHolidayByYear(String yearStr) {
        if (!HolidayDateType.YEAR.getTypeName().equals(HolidayDateType.checkDateString(yearStr))) {
            throw new IllegalArgumentException("Expected [Year Date] format but received a different format for date string: " + yearStr);
        }

        int year = Integer.parseInt(yearStr);
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return findBetween(startDate, endDate);
    }

    @Transactional(readOnly = true)
    public List<HolidayResponse> readHolidayByRange(String startStr, String endStr) {
        if (!HolidayDateType.checkRangeDate(startStr, endStr)) {
            throw new IllegalArgumentException("Expected [Range Date] formats but received a different formats for date strings: " + startStr + ", " + endStr);
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.parse(startStr, fmt);
        LocalDate endDate = LocalDate.parse(endStr, fmt);
        return findBetween(startDate, endDate);
    }

    // ─────────────────────────────────────────────────────────
    private List<HolidayResponse> findBetween(LocalDate start, LocalDate end) {
        List<HolidayEntity> entities = holidayRepository.findAllByHolidayDateBetween(start, end);
        if (entities.isEmpty()) {
            throw new NoSuchElementException("해당 날짜들에는 휴일이 없습니다.");
        }
        return mapHolidayEntitiesToHolidayResponse(entities);
    }

    // Maybe move this to core common?
    private List<HolidayResponse> mapHolidayEntitiesToHolidayResponse(List<HolidayEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(HolidayResponse::new)
                .collect(Collectors.toList());
    }
    public static List<LocalDate> convertWeekToDaySpan(String yearMonthWeek) {
        int weekNum = Integer.parseInt(yearMonthWeek.substring((yearMonthWeek.length()-1)));
        if (weekNum > 5) {
            throw new IllegalArgumentException("주차 값은 5 이하이어야 합니다.");
        }

        List<LocalDate> input = splitWeekToSevenDays(yearMonthWeek);
        int totalTrueDays = getTrueDaysInMonthWeek(input);
        LocalDate weekEnd = input.get(totalTrueDays-1);
        int daysToSubtract = weekNum == 1 ? totalTrueDays - 1: 6;
        LocalDate weekStart = weekEnd.minusDays(daysToSubtract);

        if (weekNum == 5) {
            weekEnd = weekStart;
            for (int i = 0; i < 7; i++) {
                if (!weekEnd.getMonth().equals(weekStart.plusDays(i).getMonth())) break;
                weekEnd = weekStart.plusDays(i);
            }
        }

        System.out.println(weekStart + " ~ " + weekEnd);
        return List.of(weekStart, weekEnd);
    }

    public static List<LocalDate> splitWeekToSevenDays(String yearMonthWeek) {
        int year = Integer.parseInt(yearMonthWeek.substring(0, 4));
        int month = Integer.parseInt(yearMonthWeek.substring(4, 6));
        int week = Integer.parseInt(yearMonthWeek.substring(7));

        // 해당 연-월의 1일을 기준으로 시작
        LocalDate firstOfMonth = LocalDate.of(year, month, 1);

        // ISO 주 기준으로 해당 월의 첫 번째 주가 시작되는 날부터 week-1주를 더함
        LocalDate startOfWeek = firstOfMonth
                .with(WeekFields.ISO.dayOfWeek(), 1) // 그 주의 월요일
                .plusWeeks(week - 1);

        List<LocalDate> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            result.add(startOfWeek.plusDays(i));
        }

        return result;
    }

    public static int getTrueDaysInMonthWeek(List<LocalDate> input){
        // Get ISO week and week-based year
        LocalDate weekEnd = input.get(0);
        int count = 0;

        for (LocalDate date : input) {
            if (!getWeekStartOfDate(weekEnd).equals(getWeekStartOfDate(date))) break;
            weekEnd = date;
            count++;
        }

        return count;
    }

    public static LocalDate getWeekStartOfDate(LocalDate date){
        WeekFields wf = WeekFields.ISO;
        return date.with(wf.dayOfWeek(), 1);
    }


}