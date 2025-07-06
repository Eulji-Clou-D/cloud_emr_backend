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
    RANGE("Range", "^[2-9]\\d{7}$");                // 사용자 정의 기간 (User-defined range)

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
            return null;
        }

        // Iterate through the enum constants in their defined order
        for (HolidayDateType type : HolidayDateType.values()) {
            Matcher matcher = type.getPattern().matcher(date);
            if (matcher.matches()) {
                return type.getTypeName();
            }
        }
        return null; // No matching format found
    }
}