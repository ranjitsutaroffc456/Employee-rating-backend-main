package com.EmployeeRating.ServiceImplementation;

import com.EmployeeRating.Dto.LoginRequest;
import com.EmployeeRating.Dto.LoginResponse;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Exception.InvalidCredentialsException;
import com.EmployeeRating.Exception.UserNotFoundException;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Service.AuthService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Login attempt for employeeId: {}", request.getEmployeeId());

        Employee employee = employeeRepo.findByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> {
                    log.error("User not found for employeeId: {}", request.getEmployeeId());
                    return new UserNotFoundException("User not found");
                });

        log.info("User found: {}", employee.getEmployeeId());

        if (!employee.getPassword().equals(request.getPassword())) {
            log.error("Invalid password for employeeId: {}", request.getEmployeeId());
            throw new InvalidCredentialsException("Invalid credentials");
        }

        log.info("Emp.................{}", employee);

        // Redirect only for Developer and Team Lead
        String redirectUrl = null; // default no redirect
        String email = null;
        switch (employee.getEmployeeRole().toLowerCase()) {
            case "developer":
                redirectUrl = "/Nutan-dashboard";
                break;
            case "team lead":
            case "teamlead":
                redirectUrl = "/Anwesha-dashboard";
                email = employee.getEmployeeEmail();
                break;
            // Any other role → no redirect
            default:
                redirectUrl = null;
                break;
        }
        return new LoginResponse(
                "Login successful",
                employee.getEmployeeRole(),
                employee.getEmployeeId(),
                employee.getEmployeeName(),
                redirectUrl,
                email

        );
    }
}