package com.EmployeeRating.Controller;


import com.EmployeeRating.Dto.EmployeeTaskRequestDto;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.EmployeeTask;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Service.EmployeeTaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class EmployeeTaskController {
    private EmployeeTaskService taskService;
    private ObjectMapper objectMapper;
    private EmployeeRepo employeeRepo;

    @PostMapping("/submit/{employeeId}")
    public ResponseEntity<?> submitTasks(
            @PathVariable String employeeId,
            @RequestPart("tasks") String tasksJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        try {
            // Check employee exists by employeeId (String)
            Optional<Employee> employeeOpt = employeeRepo.findByEmployeeId(employeeId);
            if (employeeOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Employee with employeeId " + employeeId + " not found");
            }
            Employee employee = employeeOpt.get();

            // Parse JSON into DTO list
            List<EmployeeTaskRequestDto> taskRequestList = objectMapper.readValue(
                    tasksJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, EmployeeTaskRequestDto.class)
            );

            // Save tasks
            taskService.saveTasks(employee, taskRequestList, files);

            return ResponseEntity.ok("Tasks submitted successfully");

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid JSON format for tasks");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<?> getListOfTask(@PathVariable String employeeId) {
        List<EmployeeTask> tasks = taskService.getListOfTask(employeeId);

        if (tasks.isEmpty()) {
            return ResponseEntity.ok(
                    Map.of(
                            "message", "Employee exists but has not submitted any tasks yet.",
                            "employeeId", employeeId,
                            "tasks", tasks
                    )
            );
        }

        return ResponseEntity.ok(tasks);
    }


    // Get tasks by date
    @GetMapping("/by-date")
    public ResponseEntity<List<EmployeeTask>> getTasksByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<EmployeeTask> tasks = taskService.getTasksByDate(date);
        return ResponseEntity.ok(tasks);
    }
}
