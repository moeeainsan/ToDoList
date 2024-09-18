package com.springproject.springboot.security.project.demo.error;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
   
    private Map<String, String> errors;
}
