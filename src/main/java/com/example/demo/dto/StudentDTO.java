package com.example.demo.dto;

import com.example.demo.model.Student;

import java.util.Map;

public class StudentDTO {
    private Student student;
    private Boolean checked;

    public StudentDTO() {
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
