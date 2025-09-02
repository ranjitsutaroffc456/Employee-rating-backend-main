package com.EmployeeRating.ServiceImplementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.EmployeeRating.Dto.TeamLeadDailyRatingDto;
import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.Rating;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Repository.RatingRepo;
import com.EmployeeRating.Service.RatingService;

@Service
public class RatingServiceImple implements RatingService {

	@Autowired
	RatingRepo ratingRepo;
	
	@Autowired
	EmployeeRepo employeeRepo;

	// Old methods - commented out as per requirement
	/*
	@Override
	public ResponseEntity<?> save(RatingDto dto, String empid) {
		// Old implementation commented out
	}

	@Override
	public Rating getRating(Long id) {
		// Old implementation commented out
	}

	@Override
	public ResponseEntity<?> update(RatingDto dto, String employeeId) {
		// Old implementation commented out
	}

	@Override
	public ResponseEntity<?> update(List<RatingDto> dtoList) {
		// Old implementation commented out
	}

    @Override
    public ResponseEntity<?> getRatingsByEmployeeIds(List<String> employeeIds) {
        // Old implementation commented out
    }

    @Override
    public ResponseEntity<?> bulkSaveRatings(List<RatingDto> dtos) {
        // Old implementation commented out
	}
	*/
    
    // New team lead daily rating methods
    @Override
    public ResponseEntity<?> saveTeamLeadDailyRatings(TeamLeadDailyRatingDto dto, String teamLeadEmail) {
        List<String> saved = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        
        // Validate that all employees belong to this team lead
        List<String> employeeIds = dto.getEmployeeRatings().stream()
                .map(TeamLeadDailyRatingDto.EmployeeDailyRating::getEmployeeId)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        List<Employee> employees = employeeRepo.findByTeamLeadEmail(teamLeadEmail);
        List<String> validEmployeeIds = employees.stream()
                .map(Employee::getEmployeeId)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        for (TeamLeadDailyRatingDto.EmployeeDailyRating employeeRating : dto.getEmployeeRatings()) {
            String employeeId = employeeRating.getEmployeeId();
            Integer rating = employeeRating.getRating();
            String employeeRemark = employeeRating.getEmployeeRemark();
            
            // Validate employee belongs to team lead
            if (!validEmployeeIds.contains(employeeId)) {
                errors.add("Employee " + employeeId + " does not belong to team lead " + teamLeadEmail);
                continue;
            }
            
            // Validate rating value (1-5)
            if (rating == null || rating < 1 || rating > 5) {
                errors.add("Invalid rating for employee " + employeeId + ": " + rating + " (must be 1-5)");
                continue;
            }
            
            // Check if rating already exists for this employee and date
            Optional<Rating> existingRating = ratingRepo.findByEmployeeIdAndRatingDate(employeeId, dto.getRatingDate());
            if (existingRating.isPresent()) {
                // Update existing rating
                Rating ratingEntity = existingRating.get();
                ratingEntity.setDailyRating(rating);
                ratingEntity.setRatedBy(teamLeadEmail);
                ratingEntity.setEmployeeRemark(employeeRemark);
                ratingRepo.save(ratingEntity);
                saved.add("Updated rating for " + employeeId);
            } else {
                // Create new rating
                Rating ratingEntity = new Rating();
                ratingEntity.setEmployeeId(employeeId);
                ratingEntity.setDailyRating(rating);
                ratingEntity.setRatingDate(dto.getRatingDate());
                ratingEntity.setRatedBy(teamLeadEmail);
                ratingEntity.setEmployeeRemark(employeeRemark);
                ratingRepo.save(ratingEntity);
                saved.add("Created rating for " + employeeId);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("saved", saved);
        result.put("errors", errors);
        result.put("ratingDate", dto.getRatingDate());
        result.put("teamLeadEmail", teamLeadEmail);
        
        if (errors.isEmpty()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }
    
    @Override
    public ResponseEntity<?> getEmployeeRatingsByTeamLead(String teamLeadEmail) {
        List<Rating> ratings = ratingRepo.findByRatedByOrderByRatingDateDesc(teamLeadEmail);
        
        // Group ratings by date
        Map<LocalDate, List<Rating>> ratingsByDate = new HashMap<>();
        for (Rating rating : ratings) {
            ratingsByDate.computeIfAbsent(rating.getRatingDate(), k -> new ArrayList<>()).add(rating);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("teamLeadEmail", teamLeadEmail);
        result.put("ratingsByDate", ratingsByDate);
        result.put("totalRatings", ratings.size());
        
        return ResponseEntity.ok(result);
    }
    
    @Override
    public ResponseEntity<?> getEmployeeRatingsByDate(LocalDate ratingDate) {
        List<Rating> ratings = ratingRepo.findByRatingDate(ratingDate);
        
        // Group ratings by team lead
        Map<String, List<Rating>> ratingsByTeamLead = new HashMap<>();
        for (Rating rating : ratings) {
            ratingsByTeamLead.computeIfAbsent(rating.getRatedBy(), k -> new ArrayList<>()).add(rating);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("ratingDate", ratingDate);
        result.put("ratingsByTeamLead", ratingsByTeamLead);
        result.put("totalRatings", ratings.size());
        
        return ResponseEntity.ok(result);
    }
}

