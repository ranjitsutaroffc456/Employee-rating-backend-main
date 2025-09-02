package com.EmployeeRating.Controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.EmployeeRating.Dto.EmployeeRegiResponse;
import com.EmployeeRating.Dto.EmployeeRegistrationReqDto;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Service.EmployeeRegistrationService;
import com.EmployeeRating.ServiceImplementation.EmployeeRegistrationServiceImpl;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EmployeeRegistrationController {
	
	@Autowired
	private EmployeeRegistrationService service;
	
	@PostMapping("/register")
	public ResponseEntity<EmployeeRegiResponse> register(@Valid @RequestBody EmployeeRegistrationReqDto dto) {
		Employee saved = service.user(dto);
		EmployeeRegiResponse resp = new EmployeeRegiResponse(
                saved.getId(),
                saved.getEmployeeId(),
                saved.getEmployeeName(),
                saved.getEmployeeRole(),
                "Registration successful"
        );
		return ResponseEntity.ok(resp); 
		}
	@GetMapping("/employees/export")
	public ResponseEntity<String> exportEmployees() {
	    String filePath = "exports/employees.xlsx";  //Save inside exports folder
	    ((EmployeeRegistrationServiceImpl) service).exportEmployeesToExcel(filePath);
	    return ResponseEntity.ok("Employees exported successfully to: " + filePath);
	}
	}

