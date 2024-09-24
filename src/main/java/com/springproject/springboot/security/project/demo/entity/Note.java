package com.springproject.springboot.security.project.demo.entity;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="notes")
public class Note {

   
   @Valid

    @Id
    @GeneratedValue

    private Integer id;

    @NotEmpty(message="Title can not be blank")
    private String title;

    @NotEmpty(message="Description can not be blank")
    private String description;

    @Enumerated
    private Priority priority;


    @CreationTimestamp
    private Date created_at;

    @CreationTimestamp
    private Date updated_at;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
