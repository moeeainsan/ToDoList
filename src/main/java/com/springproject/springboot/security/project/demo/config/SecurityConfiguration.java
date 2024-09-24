package com.springproject.springboot.security.project.demo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.springproject.springboot.security.project.demo.exception.CustomLogoutHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthenticationFilter jwtAuthFilter;
  private final CustomLogoutHandler logoutHandler;

    @Value("${cloud.aws.region.static}")    
    private String region;
    
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    
       
       http 
        .csrf(csrf -> csrf.disable())
       .authorizeHttpRequests(requests->requests
        .requestMatchers("api/register","api/authenticate","api/verify","api/resend","api/resendCodeForFPassword","api/resetPassword").permitAll()
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


    @Bean
    public AmazonS3 s3Amazon() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
              //  .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

}
