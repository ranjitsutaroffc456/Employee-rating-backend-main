package com.EmployeeRating.Repository;

import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Entity.EmployeeTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeTaskRepository extends JpaRepository<EmployeeTask, String> {
    @Query("SELECT t FROM EmployeeTask t WHERE t.employee.employeeId = :employeeId")
    List<EmployeeTask> findByEmployeeId(@Param("employeeId") String employeeId);
    List<EmployeeTask> findByDate(LocalDate date);  // derived query

    List<EmployeeTask> findByEmployee(Employee employee);

    List<EmployeeTask> findByEmployeIdOrderByCreatedAtAsc(String employeId);
}
