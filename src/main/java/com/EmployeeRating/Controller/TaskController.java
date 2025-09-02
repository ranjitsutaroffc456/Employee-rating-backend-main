package com.EmployeeRating.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.EmployeeRating.Dto.EmployeeTasksSummaryDto;
import com.EmployeeRating.Service.RatingService;
import com.EmployeeRating.Service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    
    @Autowired
    private RatingService ratingService;

    @GetMapping("/employees/{employeeId}/tasks-summary")
    public ResponseEntity<EmployeeTasksSummaryDto> getTasksSummary(@PathVariable("employeeId") String employeeId) {
        return ResponseEntity.ok(taskService.getTasksSummaryByEmployeeId(employeeId));
    }

    @GetMapping("/employees/tasks/{teamLeadEmail}")
    public ResponseEntity<java.util.List<EmployeeTasksSummaryDto>> getTasksByTeamLead(
            @PathVariable("teamLeadEmail") String teamLeadEmail) {
        return ResponseEntity.ok(taskService.getTasksSummariesByTeamLeadEmail(teamLeadEmail));
    }
    
    // Combined endpoint for team lead dashboard - tasks + ratings
    @GetMapping("/teamlead/dashboard/{teamLeadEmail}")
    public ResponseEntity<Map<String, Object>> getTeamLeadDashboard(@PathVariable("teamLeadEmail") String teamLeadEmail) {
        Map<String, Object> dashboard = new HashMap<>();
        
        // Get tasks for team members
        dashboard.put("tasks", taskService.getTasksSummariesByTeamLeadEmail(teamLeadEmail));
        
        // Get ratings given by this team lead
        dashboard.put("ratings", ratingService.getEmployeeRatingsByTeamLead(teamLeadEmail));
        
        return ResponseEntity.ok(dashboard);
    }
}


