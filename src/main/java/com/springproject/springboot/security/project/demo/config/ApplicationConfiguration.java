package com.springproject.springboot.security.project.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springproject.springboot.security.project.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor

public class ApplicationConfiguration {


    private final UserRepository repository;
    
    @Bean
    public UserDetailsService userDetailsService(){
       return username-> repository.findByEmail(username)
       .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
       
    }

   
   
  //to handle authentication
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();//retrieve user details by querying and check credentials
        authProvider.setUserDetailsService(userDetailsService()); //userdetails needed for authentication
        authProvider.setPasswordEncoder(passwordEncoder()); //password encode and verifying
        return authProvider;
    }
 
//for handling authentication request->valid user credential and return authentication object if valid
//handle login request and verify Jwt token
//AuthenticationConfiguration to access AuthenticationManager
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
    return config.getAuthenticationManager();
  }

    @Bean
    public PasswordEncoder passwordEncoder() {
     return  new BCryptPasswordEncoder();

    }

}



