package com.EmployeeRating.Controller;


import com.EmployeeRating.Dto.LoginRequest;
import com.EmployeeRating.Dto.LoginResponse;
import com.EmployeeRating.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
