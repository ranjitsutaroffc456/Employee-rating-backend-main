package com.EmployeeRating.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String message;
    private String employeeRole;
    private String employeeId;  // returning employeeId (userId from DB)
    private String employeeName;
    private String redirectUrl;
    private String email;
}
