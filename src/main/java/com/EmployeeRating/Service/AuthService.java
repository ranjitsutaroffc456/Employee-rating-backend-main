package com.EmployeeRating.Service;

import com.EmployeeRating.Dto.LoginRequest;
import com.EmployeeRating.Dto.LoginResponse;


public interface AuthService {

  
    public LoginResponse login(LoginRequest request);
        
}
