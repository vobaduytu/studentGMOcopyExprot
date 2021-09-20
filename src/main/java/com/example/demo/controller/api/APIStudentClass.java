package com.example.demo.controller.api;

import com.example.demo.model.Student;
import com.example.demo.model.StudentClass;
import com.example.demo.service.StudentClassService;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/class")
public class APIStudentClass {
    @Autowired
    private StudentClassService studentClassService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<StudentClass> studentClasses = studentClassService.showAll();
        return new ResponseEntity<>(studentClasses, HttpStatus.OK);
    }
}
