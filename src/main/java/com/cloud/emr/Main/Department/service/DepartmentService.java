package com.cloud.emr.Main.Department.service;

import com.cloud.emr.Main.Department.dto.DepartmentResponse;
import com.cloud.emr.Main.Department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(dept -> new DepartmentResponse(
                        dept.getId(),
                        dept.getCode(),
                        dept.getName(),
                        dept.getEngName(),
                        dept.getType()
                ))
                .collect(Collectors.toList());
    }
}
