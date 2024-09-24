package com.springproject.springboot.security.project.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springproject.springboot.security.project.demo.config.JwtService;
import com.springproject.springboot.security.project.demo.controller.AuthenticationResponse;
import com.springproject.springboot.security.project.demo.entity.Token;
import com.springproject.springboot.security.project.demo.entity.User;
import com.springproject.springboot.security.project.demo.model.NewPasswordForm;
import com.springproject.springboot.security.project.demo.model.ProfileEdit;
import com.springproject.springboot.security.project.demo.model.UserInformationUpdate;
import com.springproject.springboot.security.project.demo.repository.TokenRepsitory;
import com.springproject.springboot.security.project.demo.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepsitory tokenRepsitory;
    private final UploadProfileService uploadProfileService;

    public AuthenticationResponse reset(NewPasswordForm request) {

        User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalStateException("User not found"));

    if(!user.getRandomCode().equals(request.getRandomCode())){
        throw new IllegalArgumentException("Invalid random code");
    }
    if(!request.getNewPassword().equals(request.getConfirmPassword())){
        throw new IllegalArgumentException("Password doesn't match");
    }
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
    String jwtToken = jwtService.generateToken(user);

    saveUserToken(jwtToken, user);

    emailSender.sendEmail(request.getEmail(), "<html>" +
                    "<body>" +
                    "<h2>Dear "+ user.getFirstName() + " " + user.getLastName() + ",</h2>"
                    + "<br/> Your password reset successfully. " +
                    "</body>" +
                    "</html>");

    return AuthenticationResponse
            .builder()
            .token(jwtToken)
            .build();

    
}

public void saveUserToken(String jwtToken, User user){
    Token token = Token.builder()
        .token(jwtToken)
        .isLoggedOut(false)
        .user(user)
        .build();
    
        tokenRepsitory.save(token);
}

  public String editProfile(@Valid ProfileEdit request) {

    User user = userRepository.findByEmail(request.getEmail())
    .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));
            
        String fileName =  uploadProfileService.uploadImage(request.getProfilePath(),"user");
        user.setProfilePath(fileName);
         userRepository.save(user);
         return "Profile Update Successfully";
        }

    public String editUserInformation(UserInformationUpdate request) {

       User user= userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));

       user.setFirstName(request.getFirstName());
       user.setLastName(request.getLastName());
       userRepository.save(user);
      return "Successfully User Information Update ";

    }


    }





