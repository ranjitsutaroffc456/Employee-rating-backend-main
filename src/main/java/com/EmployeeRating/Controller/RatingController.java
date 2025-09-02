package com.EmployeeRating.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.EmployeeRating.Dto.RatingDto;
import com.EmployeeRating.Dto.TeamLeadDailyRatingDto;
import com.EmployeeRating.Entity.Rating;
import com.EmployeeRating.Service.RatingService;

@RestController
@RequestMapping("/rating")
public class RatingController {

	@Autowired
	RatingService ratingService;

	// Old endpoints - commented out as per requirement
	/*
	@PostMapping("/save/{empid}")
	public ResponseEntity<?> save(@RequestBody RatingDto dto,
			@PathVariable(name = "empid", required = true) String empid) {
		return ratingService.save(dto, empid);
	}

	@GetMapping("/getRating/{id}")
	public Rating getRating(@PathVariable(name = "id", required = true) Long id) {
		return ratingService.getRating(id);
	}

	@PostMapping(value = "/pmupdate/{employeeId}", consumes = "application/json")
	public ResponseEntity<?> update(@RequestBody RatingDto dto,@PathVariable String employeeId) {
		return ratingService.update(dto,employeeId);
	}
	@PostMapping("/pmupdateall")
	public ResponseEntity<?> updateAll(@RequestBody List<RatingDto> dtoList) {
		return ratingService.update(dtoList);
//	    return ratingService.saveAll(dtoList);
	}
	@PostMapping("/bulkSave")
	public ResponseEntity<?> bulkSave(@RequestBody List<RatingDto> dtos) {
		return ratingService.bulkSaveRatings(dtos);
	}
	*/
	
	// New team lead daily rating endpoints
	@PostMapping("/teamlead/daily/{teamLeadEmail}")
	public ResponseEntity<?> saveTeamLeadDailyRatings(
			@RequestBody TeamLeadDailyRatingDto dto,
			@PathVariable String teamLeadEmail) {
		return ratingService.saveTeamLeadDailyRatings(dto, teamLeadEmail);
	}
	
	@GetMapping("/teamlead/{teamLeadEmail}")
	public ResponseEntity<?> getEmployeeRatingsByTeamLead(@PathVariable String teamLeadEmail) {
		return ratingService.getEmployeeRatingsByTeamLead(teamLeadEmail);
	}
	
	@GetMapping("/date/{ratingDate}")
	public ResponseEntity<?> getEmployeeRatingsByDate(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ratingDate) {
		return ratingService.getEmployeeRatingsByDate(ratingDate);
	}
	
}