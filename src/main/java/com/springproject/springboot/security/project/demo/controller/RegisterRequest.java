/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.springproject.springboot.security.project.demo.controller;


import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// @Table(name="users", uniqueConstraints={@UniqueConstraint(columnNames="email")})
@Table(name="users")



 public  class RegisterRequest {

    @Valid
    
    @NotBlank(message="firstName is required")
    private String firstName;

   
    @NotBlank(message="lastName is required")
    private String lastName;

    
    @Email
    @Column(unique=true)
    @NotBlank(message="email is required")
    private String email;

   
    @NotBlank(message="password is required")
    @Size(min= 6 , max =10 , message="Password must be betweeen 6 and 10")
    private String password;

    // private String profile_path;
    // private String created_at;
    // private String updated_at;

}
