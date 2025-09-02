package com.EmployeeRating.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.EmployeeRating.Entity.Rating;

public interface RatingRepo extends JpaRepository<Rating, Long>{
    
    // Find ratings by employee ID and date
    Optional<Rating> findByEmployeeIdAndRatingDate(String employeeId, LocalDate ratingDate);
    
    // Find all ratings for an employee
    List<Rating> findByEmployeeIdOrderByRatingDateDesc(String employeeId);
    
    // Find all ratings for a specific date
    List<Rating> findByRatingDate(LocalDate ratingDate);
    
    // Find all ratings given by a team lead
    List<Rating> findByRatedByOrderByRatingDateDesc(String ratedBy);
    
    // Find ratings for employees under a team lead for a specific date
    @Query("SELECT r FROM Rating r WHERE r.employeeId IN " +
           "(SELECT e.employeeId FROM Employee e WHERE e.teamLeadEmail = :teamLeadEmail) " +
           "AND r.ratingDate = :ratingDate")
    List<Rating> findByTeamLeadEmailAndRatingDate(@Param("teamLeadEmail") String teamLeadEmail, 
                                                  @Param("ratingDate") LocalDate ratingDate);
}
