package com.EmployeeRating.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EmployeeRating.Entity.Employee;
@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Long>{
	Optional<Employee> findByEmployeeId(String empId);

	List<Employee> findByProjectManagerEmail(String projectManagerEmail);

	List<Employee> findByPmoEmail(String managerOfficer);
	
	List<Employee> findByTeamLeadEmail(String teamLeadEmail);
	
	List<Employee> findByNoticePeriodFalseAndProbationaPeriodFalse();
	
	List<Employee> findByTeamLeadEmailAndNoticePeriodFalseAndProbationaPeriodFalse(String teamLeadEmail);
	
	// Authentication methods (consolidated from UserRepository)
	Optional<Employee> findByEmployeeIdAndPassword(String employeeId, String password);
	
	boolean existsByEmployeeId(String employeeId);
}
