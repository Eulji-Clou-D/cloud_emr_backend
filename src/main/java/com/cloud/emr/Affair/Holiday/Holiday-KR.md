# Holiday Documentation

> **범위**: `HolidayDateType`, `checkDateString()`, 예외 시나리오, HolidayService의 공개 메서드와의 연동에 대한 자세한 문서화 및 사용 가이드.

## 1. 목적
이 문서는 Clou-D EMR 백엔드의 Issue #104의 전체 구현과 영향을 설명하며, 정규표현식을 기반으로 한 날짜 문자열 파싱과 그 이후의 영향을 중심으로 설명합니다.

---
## 2. Holiday 날짜 형식 개요
`HolidayDateType` 열거형은 백엔드에서 인식하는 **6가지 특정 날짜 형식**을 정의합니다:

| 형식      | 예시                 | 정규식 패턴                             | 사용 사례             |
|---------|--------------------|------------------------------------|-------------------|
| DAY     | 20250501           | `^[2-9]\d{7}$`                     | 특정 날짜<br>휴일 추출    |
| WEEK    | 202505W1           | `^[2-9]\d{5}W[1-5]$`               | 주간<br>휴일 추출       |
| MONTH   | 202505             | `^[2-9]\d{5}$`                     | 월간<br>휴일 추출       |
| QUARTER | 2025Q1             | `^[2-9]\d{3}Q[1-4]$`               | 분기별<br>휴일 추출      |
| YEAR    | 2025               | `^[2-9]\d{3}$`                     | 연간<br>휴일 추출       |
| RANGE   | 20250501, 20250510 | 둘다 `^[2-9]\d{7}$` && date1 < date2 | 특정 날짜 범위<br>휴일 추출 |

> 통상 enum 상수의 순서가 중요합니다: 보다 구체적인 정규식이 일반적인 정규식보다 먼저 나와야 ('Range'가 'Day'보다 먼저) 정확한 매칭이 가능하지만, 이 프로젝트의 휴일에는 적용할 필요가 없었습니다.

---

## 3. Holiday API Documentation

```text
[HolidayRequest]
{
  LocalDate holidayDate;
  Boolean holidayNational;
  String holidayReason;
}

[HolidayResponse]
{
  Long id;
  LocalDate holidayDate;
  Boolean holidayNational;
  String holidayReason;
}
```

### ① Register a Holiday

- **URL:** `POST /api/holiday`
- **Role Required:** ADMIN
- **Request Body:** (`application/json`)
```json
{
  "holidayDate": "20250501",
  "holidayNational": true,
  "holidayReason": "Labor Day"
}
```
- **Response:**
```json
{
  "success": true,
  "code": 200,
  "message": "휴일 등록 성공",
  "data": null
}
```

---

### ② Update a Holiday

- **URL:** `PUT /api/holiday/{id}`
- **Role Required:** ADMIN
- **Path Variable:** `id` (Long)
- **Request Body:**
```json
{
  "holidayDate": "20250501",
  "holidayNational": false,
  "holidayReason": "Updated reason"
}
```
- **Response:**
```json
{
  "success": true,
  "code": 200,
  "message": "휴일 수정 성공",
  "data": { ...HolidayResponse object... }
}
```

---

### ③ Delete a Holiday

- **URL:** `DELETE /api/holiday/{id}`
- **Role Required:** ADMIN
- **Path Variable:** `id` (Long)
- **Response:**
```json
{
  "success": true,
  "code": 200,
  "message": "휴일 삭제 성공",
  "data": null
}
```

---

### ④ Get Holiday by Day

- **URL:** `GET /api/holiday/day/{date}`
- **Roles Allowed:** ADMIN, DOCTOR, STAFF
- **Path Variable:** `date` (format: yyyyMMdd, , e.g., 20250501)
- **Response:**
```json
{
  "success": true,
  "code": 200,
  "message": "1개의 휴일 조회 성공",
  "data": { ...HolidayResponse object... }
}
```

---

### ⑤ Get Holidays by Week

- **URL:** `GET /api/holiday/week/{date}`
- **Roles Allowed:** ADMIN, DOCTOR, STAFF
- **Path Variable:** `date` (format: yyyyMMWn, e.g., 202505W1)
- **Response:**
```json
{
  "success": true,
  "code": 200,
  "message": "X개의 휴일 조회 성공",
  "data": [ ...HolidayResponse list... ]
}
```

---

### ⑥ Get Holidays by Month

- **URL:** `GET /api/holiday/month/{date}`
- **Roles Allowed:** ADMIN, DOCTOR, STAFF
- **Path Variable:** `date` (format: yyyyMM, e.g., 202505)
- **Response:**
```json
{
  "success": true,
  "code": 200,
  "message": "X개의 휴일 조회 성공",
  "data": [ ...HolidayResponse list... ]
}
```

---

### ⑦ Get Holidays by Quarter

- **URL:** `GET /api/holiday/quarter/{date}`
- **Roles Allowed:** ADMIN, DOCTOR, STAFF
- **Path Variable:** `date` (format: yyyyQn, e.g., 2025Q1)
- **Response:**
```json
{
  "success": true,
  "code": 200,
  "message": "X개의 휴일 조회 성공",
  "data": [ ...HolidayResponse list... ]
}
```

---

### ⑧ Get Holidays by Year

- **URL:** `GET /api/holiday/year/{date}`
- **Roles Allowed:** ADMIN, DOCTOR, STAFF
- **Path Variable:** `date` (format: yyyy, e.g., 2025)
- **Response:**
```json
{
  "success": true,
  "code": 200,
  "message": "X개의 휴일 조회 성공",
  "data": [ ...HolidayResponse list... ]
}
```

---

### ⑨ Get Holidays by Date Range

- **URL:** `GET /api/holiday/range`
- **Roles Allowed:** ADMIN, DOCTOR, STAFF
- **Query Parameters:**
  - `startDate` (yyyyMMdd, e.g., 20250501)
  - `endDate` (yyyyMMdd, e.g., 20250510)
- **Response:**
```json
{
  "success": true,
  "code": 200,
  "message": "startDate - endDate : X개의 휴일 조회 성공",
  "data": [ ...HolidayResponse list... ]
}
```

---

> For errors, all endpoints return 400/500 status with:
```json
{
  "success": false,
  "code": 400,
  "message": "Error message",
  "data": null
}
```

---

## 4. 공용 휴일 날짜 판별·조회 함수 모음

### HolidayDateType

#### `checkDateString(String date)`
- **동작**
  - 해당 문자열과 일치하는 `HolidayDateType` enum 상수를 반환합니다.
  - 다음의 경우 `IllegalArgumentException` 예외를 발생시킵니다:
    - 입력이 null 또는 빈 문자열일 경우
    - 어떤 형식에도 일치하지 않을 경우

- **예외 상황**

| 입력값          | 예외 발생 여부                     | 메시지                                    |
|--------------|------------------------------------|----------------------------------------|
| `null`       | 예                                 | `Date string cannot be null or empty.` |
| `""`         | 예                                 | `Date string cannot be null or empty.` |
| `2025-05-01` | 예                                 | `Unknown date format: 2025-05-01`      |
| `2025W6`     | 예                                 | `Unknown date format: 2025W6`          |
| `2025Q5`     | 예                                 | `Unknown date format: 2025Q5`          |
| `20250230`   | 아니오 (정규식 통과, 의미적 오류) | *(DAY로 인식되며, 날짜 유효성은 호출자가 판단함)*        |

#### `checkRangeDate(String date1, String date2)`
- **동작**
  - 두 입력값이 모두 `DAY` 형식에 맞고, `date1 < date2`이면 `true`를 반환합니다.
  - 형식은 맞지만 `date1 >= date2`이면 `false`를 반환합니다.
  - 다음의 경우 `IllegalArgumentException` 예외를 발생시킵니다:
    - 입력값 중 하나라도 null 또는 빈 문자열인 경우

- **결과 시나리오**

| date1        | date2        | 결과      | 설명                                                  |
|--------------|--------------|-----------|-------------------------------------------------------|
| `null`       | `20250510`   | 예외      | date1이 null                                          |
| `""`         | `20250510`   | 예외      | date1이 빈 문자열                                     |
| `20250510`   | `""`         | 예외      | date2가 빈 문자열                                     |
| `20250510`   | `20250501`   | false     | 형식은 맞지만 date1 > date2                           |
| `2025-05-01` | `20250510`   | false     | 형식이 잘못됨 (하이픈 포함)                           |
| `20250501`   | `20250510`   | true      | DAY 형식, 날짜 순서 올바름                            |

### HolidayService

#### `readHolidayByDay(String yearMonthDay)`
- **검증 형식**: `DAY`  (예: 20250501)
- **반환값**: 해당 날짜의 단일 `HolidayResponse`
- **예외**:
  - 형식이 잘못되면 `IllegalArgumentException`
  - 휴일이 존재하지 않으면 `NoSuchElementException`

#### `readHolidayByWeek(String yearMonthWeek)`
- **검증 형식**: `WEEK` (예: 202505W1)
- **반환값**: 주간 날짜 범위 내 휴일 리스트

#### `readHolidayByMonth(String yearMonth)`
- **검증 형식**: `MONTH` (예: 202505)
- **반환값**: 해당 월의 휴일 리스트

#### `readHolidayByQuarter(String yearQuarter)`
- **검증 형식**: `QUARTER` (예: 2025Q1)
- **반환값**: 해당 분기의 첫 달부터 마지막 달까지의 휴일 리스트

#### `readHolidayByYear(String yearStr)`
- **검증 형식**: `YEAR` (예: 2025)
- **반환값**: 해당 연도의 모든 휴일 리스트

#### `readHolidayByRange(String startStr, String endStr)`
- **검증 형식**: `RANGE` (`checkRangeDate`로 검증)
- **반환값**: 시작일과 종료일 사이(포함)의 휴일 리스트

---
