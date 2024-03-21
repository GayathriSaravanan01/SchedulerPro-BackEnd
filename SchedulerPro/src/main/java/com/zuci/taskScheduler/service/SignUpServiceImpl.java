package com.zuci.taskScheduler.service;

import com.zuci.taskScheduler.exception.EmailIdAlreadyExistException;
import com.zuci.taskScheduler.exception.MobileNumberAlreadyExist;
import com.zuci.taskScheduler.exception.UserNameAlreadyExistException;
import com.zuci.taskScheduler.model.Role;
import com.zuci.taskScheduler.model.SignUp;
import com.zuci.taskScheduler.repository.SignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SignUpServiceImpl implements SignUpService {
    @Autowired
    private SignUpRepository signUpRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public SignUp createUser(SignUp signUp) {

        SignUp sign=signUpRepository.findUserByUsername(signUp.getUsername());
        if(sign==null) {
            SignUp sign1 = signUpRepository.findUserByEmailId(signUp.getEmailId());
            if (sign1 == null) {
                SignUp sign2 = signUpRepository.findUserByMobileNumber(signUp.getMobileNumber());
                if(sign2==null){
                signUp.setPassword(passwordEncoder.encode(signUp.getPassword()));
                return signUpRepository.save(signUp);}
                else {
                    throw new MobileNumberAlreadyExist();
                }
            }
            else{
                throw  new EmailIdAlreadyExistException();
            }
        }
        else{
            throw new UserNameAlreadyExistException();
          }
    }
}
