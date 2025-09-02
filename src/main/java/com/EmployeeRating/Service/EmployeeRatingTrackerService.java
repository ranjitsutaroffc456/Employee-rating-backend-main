package com.EmployeeRating.Service;

import org.springframework.http.ResponseEntity;

import com.EmployeeRating.Dto.EmployeeRatingTrackerDto;

public interface EmployeeRatingTrackerService {

	ResponseEntity<?> save(EmployeeRatingTrackerDto dto);

	ResponseEntity<?> tlSubmit(String employeeId);

	ResponseEntity<?> pmSubmit(String employeeId);
	
	ResponseEntity<?> pmoSubmit(String employeeId);

}
