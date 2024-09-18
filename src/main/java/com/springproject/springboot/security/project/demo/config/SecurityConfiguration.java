package com.springproject.springboot.security.project.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springproject.springboot.security.project.demo.exception.CustomLogoutHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthenticationFilter jwtAuthFilter;
  private final CustomLogoutHandler logoutHandler;

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    //   String[] linkArray = {
    //     "api/register",
    //     "api/authenticate",
    //     "api/verify",
    //     "api/resend",
    //     "api/create/{id}",
    //     "api/delete/{id}",
    //     "api/update/{id}",
    //     "api/allNote/{id}",
    //     "api/{id}/notesDescInDate"
    // };
      
       
       http 
        .csrf(csrf -> csrf.disable())
       .authorizeHttpRequests(requests->requests
        .requestMatchers("api/register","api/authenticate","api/verify","api/resend","api/create/{id}","api/delete/{id}","api/update/{id}","api/allNote/{id}","api/{id}/notesDescInDate").permitAll()
        .anyRequest().authenticated())
        .sessionManagement(management-> management
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

        .logout(l->l.logoutUrl("/logout")
               .addLogoutHandler(logoutHandler)
               .logoutSuccessHandler((request,response,authentication)->SecurityContextHolder.clearContext())
               
        
        );

        return http.build();

        
    }

}
