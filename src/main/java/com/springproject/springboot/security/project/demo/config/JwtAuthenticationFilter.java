package com.springproject.springboot.security.project.demo.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private  final UserDetailsService userDetailsService;
  private final JwtService jwtService;
    @Override
    protected void doFilterInternal(
       @NonNull HttpServletRequest request, 
       @NonNull HttpServletResponse response, 
       @NonNull FilterChain filterChain)//design pattern
    throws ServletException, IOException {

  final String authHeader=request.getHeader("Authorization");
  final String jwt;
  final String userEmail;
  if(authHeader == null || !authHeader.startsWith("Bearer ")){
    filterChain.doFilter(request, response);
    return;//Want to exit
  }
    jwt=authHeader.substring(7) ; //excute jwttoken from authHeader

    //To Do check userdetails User exist or not in db *before to check we need jwtservice
    
    userEmail= jwtService.extractUserName(jwt);//To do extract UserEmail from jwt token
  if(userEmail !=null  && SecurityContextHolder.getContext().getAuthentication() == null){
    UserDetails userDetails=this.userDetailsService.loadUserByUsername(userEmail);
    if(jwtService.isTokenValid(jwt,userDetails)){
        UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(
            userDetails, 
           null,
           userDetails.getAuthorities());


            authToken.setDetails(

            new WebAuthenticationDetailsSource().buildDetails(request));

   SecurityContextHolder.getContext().setAuthentication(authToken);
       }
       filterChain.doFilter(request, response);
        }
   }

  }
    