package com.EmployeeRating.Service;

import com.EmployeeRating.Dto.EmployeeTaskRequestDto;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.EmployeeTask;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeTaskService {
    void saveTasks(Employee employee,List<EmployeeTaskRequestDto> dtoList, List<MultipartFile> files) throws IOException;
    List<EmployeeTask> getListOfTask(String employeeId);
    List<EmployeeTask> getTasksByDate(LocalDate date);

}