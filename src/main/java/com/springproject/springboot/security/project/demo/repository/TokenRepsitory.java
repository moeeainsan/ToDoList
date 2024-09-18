package com.springproject.springboot.security.project.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springproject.springboot.security.project.demo.entity.Token;

public interface TokenRepsitory extends JpaRepository<Token, Integer> {
    

    @Query("SELECT t FROM Token t WHERE t.user.id = :userId AND t.isLoggedOut = false")
    List<Token> findActiveTokensByUserId(@Param("userId") Integer userId);

    Optional <Token> findByToken(String token);

 
}
