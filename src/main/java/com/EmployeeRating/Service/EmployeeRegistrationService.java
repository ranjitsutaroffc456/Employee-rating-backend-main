package com.EmployeeRating.Service;

import com.EmployeeRating.Dto.EmployeeRegistrationReqDto;

import com.EmployeeRating.Entity.Employee;

public interface EmployeeRegistrationService { 
	Employee user(EmployeeRegistrationReqDto dto); 
	}