package com.example.demo.service.impl;

import com.example.demo.model.Student;

import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;
import com.example.demo.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class StudentImpl implements StudentService {


    @Autowired
    private StudentRepository studentRepository;



    @Override
    public List<Student> showAll() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> showAllStudent(Long id) {
        return studentRepository.findAllByStudentClassId(id);
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }


    @Override
    public void exportToExcel(List<Student> students) {
        File outputFile = new File("report_" + new Date().getTime() + ".xlsx");


        // create output file
        try {
            boolean result = outputFile.createNewFile();
            if (!result) {
                System.out.println("Create new file failed");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // export data to output file
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            ExcelUtils.tryExport(outputStream, students);
            System.out.println("Exported file success");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exported file failed");
        }
    }


}
