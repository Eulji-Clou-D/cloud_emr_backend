package com.cloud.emr.Affair.Holiday.type;

import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public enum HolidayDateType {
    // More specific patterns first
    DAY("Day", "^[2-9]\\d{7}$"),                    // 일 (Day)
    WEEK("Week", "^[2-9]\\d{5}W\\d{1}$"),           // 주 (Week)
    MONTH("Month", "^[2-9]\\d{5}$"),                // 월 (Month)
    QUARTER("Quarter", "^[2-9]\\d{3}Q[1-4]$"),      // 분기 (Quarter)
    YEAR("Year", "^[2-9]\\d{3}$"),                  // 년 (Year)
    RANGE("Range", "");                // 사용자 정의 기간 (User-defined range)

    private final String typeName;
    private final String regex;
    private final Pattern pattern; // Pre-compile the pattern for efficiency

    HolidayDateType(String typeName, String regex) {
        this.typeName = typeName;
        this.regex = regex;
        this.pattern = Pattern.compile(regex); // Compile pattern once at enum initialization
    }

    public static String checkDateString(String date) {
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be null or empty.");
        }

        // Iterate through the enum constants in their defined order
        for (HolidayDateType type : HolidayDateType.values()) {
            Matcher matcher = type.getPattern().matcher(date);
            if (matcher.matches()) {
                return type.getTypeName();
            }
        }
        throw new IllegalArgumentException("Unknown date format: " + date);
    }

    public static Boolean checkRangeDate(String date1, String date2) {
        if (date1 == null || date1.isEmpty() || date2 == null || date2.isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be null or empty.");
        }

        // Iterate through the enum constants in their defined order
        Matcher matcher1 = HolidayDateType.DAY.getPattern().matcher(date1);
        Matcher matcher2 = HolidayDateType.DAY.getPattern().matcher(date2);
        if (matcher1.matches() && matcher2.matches() && Integer.parseInt(date1) < Integer.parseInt(date2)) {
            return true;
        }
        return false;
    }
}