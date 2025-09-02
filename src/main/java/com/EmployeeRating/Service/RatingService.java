package com.EmployeeRating.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.EmployeeRating.Dto.RatingDto;
import com.EmployeeRating.Dto.TeamLeadDailyRatingDto;
import com.EmployeeRating.Entity.Rating;

public interface RatingService {

	// Old methods - commented out as per requirement
	/*
	ResponseEntity<?> save(RatingDto dto,String empid);

	Rating getRating(Long id);

	ResponseEntity<?> update(RatingDto dto, String employeeId);

	ResponseEntity<?> update(List<RatingDto> dtoList);

    ResponseEntity<?> getRatingsByEmployeeIds(List<String> employeeIds);
    ResponseEntity<?> bulkSaveRatings(List<RatingDto> dtos);
    */
    
    // New team lead daily rating methods
    ResponseEntity<?> saveTeamLeadDailyRatings(TeamLeadDailyRatingDto dto, String teamLeadEmail);
    
    ResponseEntity<?> getEmployeeRatingsByTeamLead(String teamLeadEmail);
    
    ResponseEntity<?> getEmployeeRatingsByDate(LocalDate ratingDate);
}
