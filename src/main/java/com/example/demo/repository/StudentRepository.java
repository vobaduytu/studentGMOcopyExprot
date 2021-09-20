package com.example.demo.repository;

import com.example.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByStudentClassId(Long id);


    List<Student>findByPhoneNumber(String phone);
    List<Student>findByName(String name);
    List<Student>findByDob(String dob);
}
