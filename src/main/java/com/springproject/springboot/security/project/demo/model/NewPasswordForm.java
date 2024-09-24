package com.springproject.springboot.security.project.demo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewPasswordForm {


    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    @NotBlank(message = "Email cannot be null")
    private String email;

    
    @NotBlank(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be more than 8 characters")
    private String newPassword;


    @NotBlank(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be more than 8 characters")
    private String confirmPassword;


    @NotBlank(message = "Code cannot be null")
    private String randomCode;


}
