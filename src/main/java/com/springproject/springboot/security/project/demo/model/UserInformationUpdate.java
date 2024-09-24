package com.springproject.springboot.security.project.demo.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInformationUpdate {


    @Valid

    @Email
    private String email;

    @NotBlank(message="first Name cannot be null")
    private String firstName;

    @NotBlank(message="first Name cannot be null")
    private String lastName;

}
