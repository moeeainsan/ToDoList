package com.springproject.springboot.security.project.demo.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springproject.springboot.security.project.demo.entity.Note;
import com.springproject.springboot.security.project.demo.entity.User;
import com.springproject.springboot.security.project.demo.repository.NoteRepository;
import com.springproject.springboot.security.project.demo.repository.UserRepository;
import com.springproject.springboot.security.project.demo.service.NoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/")

@RequiredArgsConstructor

public class NoteController {

    @Autowired
    private final NoteService noteService;

    @Autowired
    private final UserRepository userRepository;

   private final NoteRepository noteRepository;
    @PostMapping("create/{userId}")
    public ResponseEntity<?> createNote(@Valid @RequestBody Note note, @PathVariable Integer userId) {

    
        // Fetch user and handle potential absence
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Note not found with id: " + userId));

        

        // Create and save the note using the service
       Note createdNote = noteService.createNote(note, user);

        return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
    }


    // @DeleteMapping("delete/{id}")
    // public ResponseEntity<String> deleteNote(@PathVariable Integer id) {
    //   noteRepository.deleteById(id);
    //   return new ResponseEntity<>("Deleted",HttpStatus.OK);
    // }
 


    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Integer id) {
        if (!noteRepository.existsById(id)) {
            return new ResponseEntity<>("NoteID not found", HttpStatus.NOT_FOUND);
        }
        
        noteRepository.deleteById(id);
        return new ResponseEntity<>("Note with ID " + id + " has been deleted successfully.", HttpStatus.OK);
    }
    
    @PostMapping("update/{id}")

    public ResponseEntity<String> updatNote(@PathVariable Integer id,@RequestBody Note newNoteData) {
      Optional<Note> noteData = noteRepository.findById(id);

      if(noteData.isPresent()){
      Note updateNoteData =noteData.get();
      updateNoteData.setTitle(newNoteData.getTitle());
      updateNoteData.setDescription(newNoteData.getDescription());
      
      updateNoteData.setUpdated_at(new Date());
      updateNoteData.setPriority(newNoteData.getPriority());

      Note noteObj=noteRepository.save(updateNoteData);
      return new ResponseEntity<>("Successfully Update ",HttpStatus.OK);
      }

      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("allNote/{userId}")
    public ResponseEntity<List<Note>> getAllNotes(@PathVariable Integer userId) {
        try {
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            List<Note> noteList = noteRepository.findByUserId(userId); // Ensure this method exists
            if (noteList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        
            return new ResponseEntity<>(noteList, HttpStatus.OK);
        } catch (Exception ex) {
            
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{userId}/notesDescInDate")
    public ResponseEntity<List<Note>> retrieveAllNotesInDateDescOrder(@PathVariable Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        List<Note> notes = noteService.findNoteByUserInDateDescOrder(userId);
        return new ResponseEntity<>(notes,HttpStatus.OK);
    }
}

