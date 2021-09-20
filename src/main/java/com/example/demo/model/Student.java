package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long student_id;

    private String name;
    private String dob;
    private String gender;

    private String phoneNumber;

    private String note;

    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private StudentClass studentClass;

    public Student(String newName, String newDob, String newGender, String newPhone) {
        this.name = newName;
        this.dob = newDob;
        this.gender = newGender;
        this.phoneNumber = newPhone;
    }

    public Student(long student_id, String name, String dob, String gender, String phoneNumber, StudentClass studentClass) {
        this.student_id = student_id;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.studentClass = studentClass;
    }

    public Student(long id, String newName, String newDob, String newGender, String newPhone) {
        this.student_id = id;
        this.name = newName;
        this.dob = newDob;
        this.gender = newGender;
        this.phoneNumber = newPhone;
    }

    public Student() {

    }

    public long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(long student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public StudentClass getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(StudentClass studentClass) {
        this.studentClass = studentClass;
    }


}
