package com.springproject.springboot.security.project.demo.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springproject.springboot.security.project.demo.entity.User;



public interface UserRepository extends JpaRepository<User, Integer>{
    
   Optional<User>findByEmail(String email);

   // User findById(Integer id);



}
