package com.springproject.springboot.security.project.demo.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProfileEdit {

    @Valid

    @Email
    private String email;

    private MultipartFile profilePath;

}
