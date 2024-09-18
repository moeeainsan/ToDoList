package com.springproject.springboot.security.project.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name="tokens")
public class Token {

    @Id
    @GeneratedValue
    
    private Integer id;
    private String token;
    private boolean  isLoggedOut;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User  user;



}
