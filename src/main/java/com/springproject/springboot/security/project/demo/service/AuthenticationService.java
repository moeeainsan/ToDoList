package com.springproject.springboot.security.project.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springproject.springboot.security.project.demo.config.JwtService;
import com.springproject.springboot.security.project.demo.controller.AuthenticationRequest;
import com.springproject.springboot.security.project.demo.controller.AuthenticationResponse;
import com.springproject.springboot.security.project.demo.entity.Token;
import com.springproject.springboot.security.project.demo.entity.User;
import com.springproject.springboot.security.project.demo.exception.ValidationException;
import com.springproject.springboot.security.project.demo.repository.TokenRepsitory;
import com.springproject.springboot.security.project.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService service;
  private final AuthenticationManager authenticationManager;
  private final EmailSender emailSender;

  private final TokenRepsitory tokenRepsitory;

  public static String getRandomNumberString() {
    Random rnd = new Random();
    int number = rnd.nextInt(999999);
    return String.format("%06d", number);
  }

  String randomCode = getRandomNumberString();

  public AuthenticationResponse register(User request) {
    // try{

    Map<String, String> errors = new HashMap<>();

    Optional<User> isUserExist = repository.findByEmail(request.getEmail());

    if (!isUserExist.isEmpty()) {
      errors.put("email", "User already exist!");
      throw new ValidationException(errors);
    }

    User user = User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .randomCode(randomCode)
        .build();

    repository.save(user);

    String jwtToken = service.generateToken(user);

    // Save Token

    Token token = Token.builder()
        .token(jwtToken)
        .isLoggedOut(false)
        .user(user)

        .build();

    tokenRepsitory.save(token);

    emailSender.sendEmail(request.getEmail(), "<html>" +
        "<body>" +
        "<h2>Dear " + request.getFirstName() + " " + request.getLastName() + ",</h2>"
        + "<br/> We're excited to have you get started. " +
        "Please click on below link to confirm your account."
        + "<br/> " + randomCode + "" +
        "<br/> Regards,<br/>" +
        "MFA Registration team" +
        "</body>" +
        "</html>");

    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();

  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {

    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()));
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = service.generateToken(user);

    List<Token> validTokenListByUser= tokenRepsitory.findActiveTokensByUserId(user.getId());
    if(!validTokenListByUser.isEmpty()){
      validTokenListByUser.forEach(t->{
        t.setLoggedOut(true);
      });
    }

    Token token = Token.builder()
        .token(jwtToken)
        .isLoggedOut(false)
        .user(user)

        .build();

    tokenRepsitory.save(token);

    return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
  }

}
