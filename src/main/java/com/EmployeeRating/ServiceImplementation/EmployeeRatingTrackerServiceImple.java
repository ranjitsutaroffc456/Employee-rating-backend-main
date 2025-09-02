package com.EmployeeRating.ServiceImplementation;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Dto.EmployeeRatingTrackerDto;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.Rating;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Repository.RatingRepo;
import com.EmployeeRating.Service.EmployeeRatingTrackerService;

@Service
public class EmployeeRatingTrackerServiceImple implements EmployeeRatingTrackerService{
	
	@Autowired
	EmployeeRepo employeeRepo;
	
	@Autowired
	RatingRepo ratingRepo;
	
	@Autowired
	ModelMapper mapper;
	
	@Override
	public ResponseEntity<?> save(EmployeeRatingTrackerDto dto) {
		Rating rating = mapper.map(dto, Rating.class);
		Rating savedRating = ratingRepo.save(rating);
		EmployeeRatingTrackerDto savedDto = mapper.map(savedRating, EmployeeRatingTrackerDto.class);
		return new ResponseEntity<EmployeeRatingTrackerDto>(savedDto,HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> tlSubmit(String employeeId) {
		Employee savedEmployee = employeeRepo.findByEmployeeId(employeeId).get();
		if(savedEmployee.getRating() != null) {
			savedEmployee.getRating().setTlSubmitDate(LocalDate.now());
			ratingRepo.save(savedEmployee.getRating());
		}
		return new ResponseEntity<>("TL Submitted Successfully",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> pmSubmit(String employeeId) {
		Employee savedEmployee = employeeRepo.findByEmployeeId(employeeId).get();
		if(savedEmployee.getRating() != null) {
			savedEmployee.getRating().setPmSubmitDate(LocalDate.now());
			ratingRepo.save(savedEmployee.getRating());
		}
		return new ResponseEntity<>("PM Submitted Successfully",HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> pmoSubmit(String employeeId) {
		Employee savedEmployee = employeeRepo.findByEmployeeId(employeeId).get();
		if(savedEmployee.getRating() != null) {
			savedEmployee.getRating().setPmoSubmitDate(LocalDate.now());
			ratingRepo.save(savedEmployee.getRating());
		}
		return new ResponseEntity<>("PMO Submitted Successfully",HttpStatus.OK);
	}
}
