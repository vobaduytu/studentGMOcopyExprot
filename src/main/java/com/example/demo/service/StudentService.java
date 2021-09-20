package com.example.demo.service;

import com.example.demo.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> showAll();

    List<Student> showAllStudent(Long id);

    Optional<Student> findById(Long id);

    Student save(Student student);

    void delete(Long id);

    void exportToExcel(List<Student> students);


}
