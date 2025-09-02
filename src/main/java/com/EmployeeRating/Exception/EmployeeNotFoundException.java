package com.EmployeeRating.Exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String employeeId) {
        super("Employee with ID " + employeeId + " not found");
    }
}