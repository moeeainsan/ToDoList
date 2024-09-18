package com.springproject.springboot.security.project.demo.exception;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.springproject.springboot.security.project.demo.entity.Token;
import com.springproject.springboot.security.project.demo.repository.TokenRepsitory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {


    private final TokenRepsitory tokenRepsitory;

    @Override
    public void logout(HttpServletRequest request, 
                        HttpServletResponse response, 
                        Authentication authentication) {
       
  final String authHeader=request.getHeader("Authorization");
  final String jwt;
  if(authHeader == null || !authHeader.startsWith("Bearer ")){
    
    return;//Want to exit
  }
    jwt=authHeader.substring(7) ; //excute jwttoken from authHeader

   //get stored token from DB
   Token storedToken = tokenRepsitory.findByToken(jwt).orElse(null);

    if(jwt != null){
        storedToken.setLoggedOut(true);
        tokenRepsitory.save(storedToken);
    }
    }

}
