package com.springproject.springboot.security.project.demo.controller;

import java.util.Date;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springproject.springboot.security.project.demo.model.NewPasswordForm;
import com.springproject.springboot.security.project.demo.model.ProfileEdit;
import com.springproject.springboot.security.project.demo.model.UserInformationUpdate;
import com.springproject.springboot.security.project.demo.repository.UserRepository;
import com.springproject.springboot.security.project.demo.service.EmailSender;
import com.springproject.springboot.security.project.demo.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;





@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class UserController {

    private final UserService userService;

    public static String getRandomString(){
        Random r=new Random();
        int number =r.nextInt(99999);
        return  String.format("%06d", number);
    }

    String randomCode=getRandomString();

    private final UserRepository userRepository;
    private final EmailSender emailSender;

    @PutMapping("resendCodeForFPassword")
    public ResponseEntity<String> resendCodeForFP(@RequestParam String email) {

        var user=userRepository.findByEmail(email)
        .orElseThrow(()-> new RuntimeException("User not found with this email" + email));
        emailSender.sendEmail(email, "<html>" +
        "<body>" +
        "Your ResetPassword code is ."
        + "<br/> "  + "<u>" + randomCode +"</u>" +
        "<br/> Regards,<br/>" +
        "RESEND Registration team" +
        "</body>" +
        "</html>");

        user.setEmailVerifiedAt(new Date());
        user.setRandomCode(randomCode);
        userRepository.save(user);
        
        return new ResponseEntity<>("ResendCode Successfully for FP",HttpStatus.OK);
    }
    
 

    @PutMapping("resetPassword")
    public ResponseEntity<AuthenticationResponse> restPassword(@Valid @RequestBody NewPasswordForm request) {
      
        return ResponseEntity.ok(userService.reset(request));

        
    }

    @PutMapping("editProfile")
    public ResponseEntity<String> editProfilePath(@Valid @ModelAttribute ProfileEdit request ) {
    
        
        return ResponseEntity.ok(userService.editProfile(request));
    }

    @PutMapping("editUserInformation")
    public String editUserInformation(@RequestBody UserInformationUpdate user) {
       
        userService.editUserInformation(user);
        return "Update Successfully User Information";
    }

}
