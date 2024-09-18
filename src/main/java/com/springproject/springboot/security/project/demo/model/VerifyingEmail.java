package com.springproject.springboot.security.project.demo.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class VerifyingEmail {


    @Valid

    @Email
    @NotBlank(message="email is required")
    private String email;

    @NotBlank(message="Code is required")
    private String randomCode;

}
