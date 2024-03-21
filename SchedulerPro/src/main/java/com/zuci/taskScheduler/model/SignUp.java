package com.zuci.taskScheduler.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SignUp {
    @Id
    private String username;
    @Column(unique = true)
    @NotBlank(message = "Please enter your email")
    @Email(message = "Enter a valid email")
    private String emailId;
    @NotEmpty(message = "Please enter your Password")
    private String password;
    @Column(unique = true)
    private long mobileNumber;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Role> roles;
}
