package com.zuci.taskScheduler.service;


import com.zuci.taskScheduler.model.SignUp;
import com.zuci.taskScheduler.repository.SignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private SignUpRepository signUpRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SignUp sign=signUpRepository.findUserByUsername(username);

//        System.out.println(sign);
        if (sign==null){
            return null;
        }
        List<GrantedAuthority> list=sign.getRoles().stream().map(e->new SimpleGrantedAuthority(e.getRoleName())).collect(Collectors.toList());
        return new User(sign.getUsername(),sign.getPassword(),list);
    }
}
