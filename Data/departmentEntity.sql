INSERT INTO department(department_code, department_name, department_eng_name, department_type) VALUES
-- 진료부서
('OS', '외과', 'Orthopedic Surgery', 'CLINICAL'),
('IM', '내과', 'Internal Medicine', 'CLINICAL'),
('RAD', '영상의학과', 'Radiology', 'CLINICAL'),

-- 지원부서
('LAB', '진단검사의학과', 'Laboratory Medicine', 'SUPPORT'),
('AN', '마취통증의학과', 'Anesthesiology', 'SUPPORT'),

-- 행정부서
('ADM', '행정팀', 'Administration', 'ADMINISTRATIVE'),
('REG', '원무팀', 'Registration Office', 'ADMINISTRATIVE'),

-- IT 부서
('ITM', '의료정보팀', 'Medical IT', 'IT'),

-- 병동
('031', '031병동', 'Ward 031', 'WARD'),
('050', '050병동', 'Ward 050', 'WARD');