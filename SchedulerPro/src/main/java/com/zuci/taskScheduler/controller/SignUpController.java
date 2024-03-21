package com.zuci.taskScheduler.controller;

import com.zuci.taskScheduler.model.Login;
import com.zuci.taskScheduler.model.SignUp;
import com.zuci.taskScheduler.service.CustomUserDetailsService;
import com.zuci.taskScheduler.service.JwtUtilityService;
import com.zuci.taskScheduler.service.SignUpService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
@Slf4j
@RestController
@CrossOrigin
public class SignUpController {
    @Autowired
    private SignUpService signUpService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtilityService jwtUtilityService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/signup")
    public String createUser(@Valid @RequestBody SignUp signUp){
        SignUp sign=signUpService.createUser(signUp);
        if(sign==null)
           return "Check the input";
        else
            return "SignUp Success";
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody Login login){
        //log.info("mhysugfecdv");
        UserDetails userDetails =customUserDetailsService.loadUserByUsername(login.getUsername());
        if(userDetails==null) {
            return "Invalid username. ";
        }
        String hashedPassword = userDetails.getPassword();

        if (!passwordEncoder.matches(login.getPassword(), hashedPassword)) {
            return "Invalid password. ";
        }
        return jwtUtilityService.generateToken(login.getUsername());
    }
}
