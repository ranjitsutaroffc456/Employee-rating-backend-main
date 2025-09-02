package com.EmployeeRating.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "employee_tasks", indexes = {
        @Index(name = "idx_employee_tasks_employee_id", columnList = "employeId")
})
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    
    private String employeId;
    private String employeeName;
    private LocalDate date = LocalDate.now();
    private String projectName;

    private String taskName;

    @Column(length = 1000)
    private String description;

    private String status;

    private LocalTime hours;
    private LocalTime extraHours;
    private String fileName;
    
    // Consolidated fields from Task entity
    @Column(name = "hours_spent", nullable = false)
    private Double hoursSpent = 0.0; // decimal hours for compatibility with Task entity
    
    @Column(name = "work_date", nullable = false)
    private LocalDate workDate = LocalDate.now(); // consolidated with date field
    
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(length = 1000)
    private String prLink;

    private String teamLead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;
}
