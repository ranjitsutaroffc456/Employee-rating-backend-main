package com.EmployeeRating.ServiceImplementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EmployeeRating.Dto.EmployeeTasksSummaryDto;
import com.EmployeeRating.Dto.TaskDto;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.EmployeeTask;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Repository.EmployeeTaskRepository;
import com.EmployeeRating.Service.TaskService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImple implements TaskService {

    private final EmployeeTaskRepository employeeTaskRepository;
    private final EmployeeRepo employeeRepo;

    @Override
    @Transactional(readOnly = true)
    public EmployeeTasksSummaryDto getTasksSummaryByEmployeeId(String employeeId) {
        Optional<Employee> employeeOpt = employeeRepo.findByEmployeeId(employeeId);
        String employeeName = employeeOpt.map(Employee::getEmployeeName).orElse(null);

        List<EmployeeTask> tasks = employeeTaskRepository.findByEmployeIdOrderByCreatedAtAsc(employeeId);

        List<TaskDto> entries = tasks.stream()
                .map(t -> new TaskDto(t.getTaskName(), t.getStatus(),
                        t.getWorkDate() != null ? t.getWorkDate().toString() : 
                        (t.getDate() != null ? t.getDate().toString() : null)))
                .collect(Collectors.toList());

        java.util.Map<String, Double> daily = tasks.stream()
                .collect(Collectors.groupingBy(
                        t -> {
                            LocalDate workDate = t.getWorkDate() != null ? t.getWorkDate() : t.getDate();
                            return workDate != null ? workDate.toString() : "unknown";
                        },
                        Collectors.summingDouble(t -> t.getHoursSpent() != null ? t.getHoursSpent() : 0.0)));

        double total = daily.values().stream().mapToDouble(Double::doubleValue).sum();

        return new EmployeeTasksSummaryDto(employeeId, employeeName, entries, daily, total);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeTasksSummaryDto> getTasksSummariesByTeamLeadEmail(String teamLeadEmail) {
        return employeeRepo.findByTeamLeadEmail(teamLeadEmail).stream()
                .map(emp -> getTasksSummaryByEmployeeId(emp.getEmployeeId()))
                .collect(Collectors.toList());
    }
}


