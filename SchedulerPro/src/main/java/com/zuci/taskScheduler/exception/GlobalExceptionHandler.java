package com.zuci.taskScheduler.exception;

import com.zuci.taskScheduler.model.ApiError;
import com.zuci.taskScheduler.model.SignUp;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNameAlreadyExistException.class)
    public String userNameException(){ return "UserName already taken";}
    @ExceptionHandler(MobileNumberAlreadyExist.class)
    public String mobileNumberException(){ return "Mobile number already exist";}
    @ExceptionHandler(EmailIdAlreadyExistException.class)
    public String emailIdException(){ return "Email already exist";}
    @ExceptionHandler(TaskIdNotFoundException.class)
    public ResponseEntity<String> taskIdException(){
        return ResponseEntity.ok("Task Id Not Found");
    }
    @ExceptionHandler
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        ApiError apiError = new ApiError();
        apiError.setMessage(errors.toString());
        apiError.setDate(new Date());
        apiError.setPath(request.getServletPath());
        return ResponseEntity.badRequest().body(apiError);
    }
}
