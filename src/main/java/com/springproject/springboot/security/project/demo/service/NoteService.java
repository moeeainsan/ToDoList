package com.springproject.springboot.security.project.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.springboot.security.project.demo.entity.Note;
import com.springproject.springboot.security.project.demo.entity.Priority;
import com.springproject.springboot.security.project.demo.entity.User;
import com.springproject.springboot.security.project.demo.repository.NoteRepository;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class NoteService {


 @Autowired
  private final NoteRepository noteRepository;


    public Note createNote(Note note, User user) {
            Note newNote = Note.builder()
            .title(note.getTitle())
            .description(note.getDescription())
            .priority(Priority.MEDIUM)
            .user(user)
            .build();
        return noteRepository.save(newNote);
    }


    public List<Note> findNoteByUserInDateDescOrder(Integer userId) {
        return noteRepository.findAllNoteByUserIdOrderedByDateDesc(userId);
    }


    public List<Note> findNoteByPriorityASC(Integer userId) {
        return noteRepository.findNoteByPriorityASC(userId);
        
    }


    public List<Note> searchingKeyword(String keyword, Integer userId) {
        return noteRepository.findNoteBykeyword(keyword,userId);
        
    }
}
    
    
    



