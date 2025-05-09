package com.cloud.emr.Main.Department.service;

import com.cloud.emr.Main.Department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private DepartmentRepository departmentRepository;

}
