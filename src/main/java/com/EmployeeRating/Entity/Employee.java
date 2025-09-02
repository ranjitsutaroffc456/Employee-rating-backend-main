package com.EmployeeRating.Entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String employeeId;
	
	@Column(nullable = false)
	private String employeeName;
	
	@Column(nullable = false)
	private String employeeEmail;
	
	@Column(nullable = false)
	private String projectManagerName;
	
	@Column(nullable = false)	
	private String projectManagerEmail;
	
	private String projectName;
	
	@Column(nullable = true)
	private Boolean pmSubmitted;
		
	@JsonFormat(pattern = "yyyy-MM-dd") // 👈 defines how to parse it
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
	
	private String teamLead;
	
	private String teamLeadEmail;
	
	@Column(nullable = true)
	private Boolean isTLSubmitted;
	
	@Column(nullable = true)
	private Boolean isHrSend;

	private String pmoName;
	
	private String pmoEmail;
	
	private String designation;
	
	private String department;
	
	private String employmentType;
	
	@Column(nullable = true)
	private Boolean isPmoSubmitted;
	
	private LocalDate joiningDate;
	
	private LocalDate leaveDate;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="rating_id")
	@JsonIgnore
	private Rating rating;

	@Column(nullable = true)
	private Boolean noticePeriod;
	
	@Column(nullable = true)
	private Boolean probationaPeriod;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    List<EmployeeTask> employeeTasks;
    
    // Authentication fields (consolidated from User and UserRegistration)
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String employeeRole;
    
    @Transient
    private String confirmPassword; // Only for validation, not stored
}
