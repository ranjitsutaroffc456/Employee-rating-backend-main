package com.EmployeeRating.Dto;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalTime;


@Data
public class EmployeeTaskRequestDto {

        private LocalDate date;
        private String empName;
        private String projectName;
        private String taskName;
        private String description;
        private String status;
        private LocalTime hours;
        private LocalTime extraHours;
        private String prLink;
        private String teamLead;
    }

