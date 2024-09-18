package com.springproject.springboot.security.project.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springproject.springboot.security.project.demo.entity.Note;



@Repository

public interface NoteRepository extends JpaRepository<Note, Integer> {

     List<Note> findByUserId(Integer id);

@Query("SELECT n FROM Note n INNER JOIN User u ON n.user.id = u.id WHERE n.user.id = :userId ORDER BY n.created_at DESC")
List<Note> findAllNoteByUserIdOrderedByDateDesc(@Param("userId") Integer userId);
   
}
