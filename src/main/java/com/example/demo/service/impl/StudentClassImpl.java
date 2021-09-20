package com.example.demo.service.impl;

import com.example.demo.model.StudentClass;
import com.example.demo.repository.StudentClassRepository;
import com.example.demo.service.StudentClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentClassImpl implements StudentClassService {
    @Autowired
    private StudentClassRepository studentClassRepository;


    @Override
    public List<StudentClass> showAll() {
        return studentClassRepository.findAll();
    }
}
