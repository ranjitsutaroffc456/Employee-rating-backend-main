package com.EmployeeRating.ServiceImplementation;

import com.EmployeeRating.Dto.EmployeeTaskRequestDto;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.EmployeeTask;
import com.EmployeeRating.Exception.EmployeeNotFoundException;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Repository.EmployeeTaskRepository;
import com.EmployeeRating.Service.EmployeeTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeTaskServiceImp implements EmployeeTaskService {


    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private EmployeeTaskRepository taskRepository;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public void saveTasks(Employee employee, List<EmployeeTaskRequestDto> dtoList, List<MultipartFile> files) throws IOException {
        // Ensure uploads folder exists
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Employee employee1 = employeeRepo.findByEmployeeId(employee.getEmployeeId()).get();


        for (int i = 0; i < dtoList.size(); i++) {
            EmployeeTaskRequestDto dto = dtoList.get(i);
            EmployeeTask task = new EmployeeTask();
            task.setEmployeeName(employee1.getEmployeeName());
            task.setEmployeId(employee.getEmployeeId());
            task.setEmployee(employee1);
            task.setDate(dto.getDate());
            task.setProjectName(dto.getProjectName());
            task.setTaskName(dto.getTaskName());
            task.setDescription(dto.getDescription());
            task.setStatus(dto.getStatus());
            task.setHours(dto.getHours());
            task.setExtraHours(dto.getExtraHours());
            task.setTeamLead(dto.getTeamLead());
            task.setPrLink(dto.getPrLink());


            // Attach file if available (match by index)
            if (files != null && i < files.size()) {
                MultipartFile file = files.get(i);
                if (!file.isEmpty()) {
                    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path path = Paths.get(uploadDir, fileName);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    task.setFileName(fileName);
                }
            }

            taskRepository.save(task);
        }
    }

    @Override
    public List<EmployeeTask> getListOfTask(String employeeId) {
        Employee employee = employeeRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        return taskRepository.findByEmployee(employee);
    }

    @Override
    public List<EmployeeTask> getTasksByDate(LocalDate date) {
        return List.of();
    }
}

