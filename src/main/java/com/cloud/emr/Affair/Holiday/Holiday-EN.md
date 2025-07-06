# Holiday Documentation

> **Scope**: Detailed documentation and usage guide on `HolidayDateType`, `checkDateString()`, exception scenarios, and integration with public methods in `HolidayService`.

## 1. Purpose

This document unpacks the full implementation and impact of Issue #104 in the Cloud EMR backend, focusing on regex-based holiday string parsing and downstream implications.

---

## 2. Overview of Holiday Date Formats

The `HolidayDateType` enum defines **6 specific date patterns** recognized by the EMR backend:

| Format  | Example              | Regex Pattern                                | Use Case                             |
|---------|----------------------|----------------------------------------------|--------------------------------------|
| DAY     | 20250501             | `^[2-9]\d{7}$`                               | Extract holiday on a specific date   |
| WEEK    | 202505W1             | `^[2-9]\d{5}W[1-5]$`                         | Extract weekly holidays              |
| MONTH   | 202505               | `^[2-9]\d{5}$`                               | Extract monthly holidays             |
| QUARTER | 2025Q1               | `^[2-9]\d{3}Q[1-4]$`                         | Extract quarterly holidays           |
| YEAR    | 2025                 | `^[2-9]\d{3}$`                               | Extract yearly holidays              |
| RANGE   | 20250501, 20250510   | `^[2-9]\d{7}$` for both, and `date1 < date2` | Extract holidays within date range   |

> **Note**: Normally Enum ordering matters — more specific patterns like `RANGE` should be checked before broader patterns like `DAY`, but it didn't matter for Holiday in this project

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

## 4. Public Holiday Date Validation and Query Method Collection

### HolidayDateType

#### `checkDateString(String date)`
- **Behavior**
  - Returns the matching `HolidayDateType` enum constant.
  - Throws `IllegalArgumentException` if:
    - The input is null or empty.
    - The input doesn't match any known format.

- **Exception Scenarios**

| Input          | Exception Triggered                | Message                                         |
|----------------|------------------------------------|-------------------------------------------------|
| `null`         | Yes                                | `Date string cannot be null or empty.`          |
| `""`           | Yes                                | `Date string cannot be null or empty.`          |
| `2025-05-01`   | Yes                                | `Unknown date format: 2025-05-01`               |
| `2025W6`       | Yes                                | `Unknown date format: 2025W6`          |
| `2025Q5`       | Yes                                | `Unknown date format: 2025Q5`                   |
| `20250230`     | No (regex passes, semantics wrong) | *(Parses as DAY, left to caller for semantics)* |

#### `checkRangeDate(String date1, String date2)`
- **Behavior**
  - Returns `true` if both inputs match the `DAY` format and `date1 < date2`.
  - Returns `false` if format matches but `date1 >= date2`.
  - Throws `IllegalArgumentException` if:
    - Either input is null or empty.

- **Result Scenarios**

| date1       | date2       | Result     | Notes                                              |
|-------------|-------------|------------|----------------------------------------------------|
| `null`      | `20250510`  | Exception  | Input is null                                      |
| `""`        | `20250510`  | Exception  | Input is empty                                     |
| `20250510`  | `""`        | Exception  | Input is empty                                     |
| `20250510`  | `20250501`  | false      | Format valid, but date1 > date2                    |
| `2025-05-01`| `20250510`  | false      | Format invalid (dash used)                         |
| `20250501`  | `20250510`  | true       | Valid DAY format and date1 < date2                |

### HolidayService

#### `readHolidayByDay(String yearMonthDay)`
- **Validates**: `DAY` format (e.g., 20250501)
- **Returns**: Single `HolidayResponse` by exact date
- **Throws**:
  - `IllegalArgumentException` if format is incorrect
  - `NoSuchElementException` if no holiday found for date

#### `readHolidayByWeek(String yearMonthWeek)`
- **Validates**: `WEEK` format (e.g., 202505W1)
- **Returns**: List of holidays within the week's start and end date range

#### `readHolidayByMonth(String yearMonth)`
- **Validates**: `MONTH` format (e.g., 202505)
- **Returns**: List of holidays within the month

#### `readHolidayByQuarter(String yearQuarter)`
- **Validates**: `QUARTER` format (e.g., 2025Q1)
- **Returns**: List of holidays from the first to last month in the quarter

#### `readHolidayByYear(String yearStr)`
- **Validates**: `YEAR` format (e.g., 2025)
- **Returns**: List of holidays in that calendar year

#### `readHolidayByRange(String startStr, String endStr)`
- **Validates**: `RANGE` format via `checkRangeDate`
- **Returns**: List of holidays between the two dates (inclusive)

---