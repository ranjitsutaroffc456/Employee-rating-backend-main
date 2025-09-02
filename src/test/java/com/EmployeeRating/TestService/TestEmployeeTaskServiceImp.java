package com.EmployeeRating.TestService;


import com.EmployeeRating.Entity.Employee;
import com.EmployeeRating.Repository.EmployeeRepo;
import com.EmployeeRating.Repository.EmployeeTaskRepository;
import com.EmployeeRating.Service.EmployeeTaskService;
import com.EmployeeRating.ServiceImplementation.EmployeeTaskServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestEmployeeTaskServiceImp {

    @Mock
    private EmployeeTaskRepository employeeTaskRepository;
    @InjectMocks
    private EmployeeTaskServiceImp employeeTaskServiceImp;
    @Mock
    private EmployeeRepo employeeRepo;

    private Employee employee;


    @Test
    public void testAddTask() {}



}
