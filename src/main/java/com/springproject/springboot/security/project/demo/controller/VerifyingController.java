package com.springproject.springboot.security.project.demo.controller;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springproject.springboot.security.project.demo.entity.User;
import com.springproject.springboot.security.project.demo.model.VerifyingEmail;
import com.springproject.springboot.security.project.demo.repository.UserRepository;
import com.springproject.springboot.security.project.demo.service.EmailSender;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor




public class VerifyingController {

  private final EmailSender emailSender;
  @Autowired
    private  UserRepository userRepository;

    public static String getRandomNumberString() {
      Random rnd = new Random();
      int number = rnd.nextInt(999999);
      return String.format("%06d", number);
  }
  
  String randomCode = getRandomNumberString();

  @GetMapping("/verify")

    public ResponseEntity<String> verify
    ( @Valid  @RequestBody VerifyingEmail request) {


        
        User user=userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new IllegalStateException("User not found"));


        if(user.getRandomCode().equalsIgnoreCase(request.getRandomCode()) ){
            user.setEmailVerifiedAt(new Date()); 
            userRepository.save(user);
            return ResponseEntity.ok(
                "Email verified successfully." + 
                "\n{\n\tFirstName: " + user.getFirstName() + 
                ",\n\tLastName: " + user.getLastName() + 
                ",\n\tEmail: " + user.getEmail() +
                ",\n\tVerified At: " + user.getEmailVerifiedAt() +
                "\n}");
            }else{
                return ResponseEntity.ok("Verification is not successfully " + request.getRandomCode() + request.getEmail());
            }
    
    
}

@GetMapping("/resend")
public String resend(@RequestBody User request) {

  User user=userRepository.findByEmail(request.getEmail())
  .orElseThrow(() -> new IllegalStateException("User not found"));

            emailSender.sendEmail(request.getEmail(), "<html>" +
            "<body>" +
            "Your Verification code is ."
            + "<br/> "  + "<u>" + randomCode +"</u>" +
            "<br/> Regards,<br/>" +
            "RESEND Registration team" +
            "</body>" +
            "</html>");

          user.setEmailVerifiedAt(new Date()); 
          user.setRandomCode(randomCode);
          userRepository.save(user);
          return (
          "Email verified successfully." + 
          "\n{\n\tFirstName: " + user.getFirstName() + 
          ",\n\tLastName: " + user.getLastName() + 
          ",\n\tEmail: " + user.getEmail() +
          ",\n\tVerified At: " + user.getEmailVerifiedAt() +
          "\n}");

}



}




    