package com.zuci.taskScheduler.repository;

import com.zuci.taskScheduler.model.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignUpRepository extends JpaRepository<SignUp,String> {
     public SignUp findUserByUsername(String username);

     public SignUp findUserByEmailId(String emailId);

     public SignUp findUserByMobileNumber(long mobileNumber);
}
