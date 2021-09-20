package com.example.demo.controller.api;

import com.example.demo.dto.StudentDTO;
import com.example.demo.model.Student;

import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;

import com.example.demo.utils.ExportFile;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.*;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping(value = "/api/student")
public class APIStudentController {

    List<Student> students = new ArrayList<Student>();
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    //show all student
    @GetMapping
    public ResponseEntity<?> findById(@RequestParam("class") long id) {
        List<Student> students = studentService.showAllStudent(id);
        if (!students.isEmpty()) {
            return new ResponseEntity<>(students, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/showAll")
    public ResponseEntity<?> showAll(){
        List<Student> studentsList = studentService.showAll();
        if (!studentsList.isEmpty()) {
            return new ResponseEntity<>(studentsList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //add student
    @PostMapping
    public ResponseEntity<?> create(@RequestBody List<StudentDTO> studentList, RedirectAttributes attributes) {
        try {
            for (StudentDTO studentDTO : studentList) {
                String phone = studentDTO.getStudent().getPhoneNumber();
                String name = studentDTO.getStudent().getName();
                String dob = studentDTO.getStudent().getDob();
                List<Student> students = studentRepository.findByPhoneNumber(phone);
                List<Student> studentsName = studentRepository.findByName(name);
                List<Student> studentsDob = studentRepository.findByDob(dob);

                if ((studentsName == null || studentsName.isEmpty()) &&(studentsDob == null || studentsDob.isEmpty()) && (students == null || students.isEmpty()) && (!studentDTO.getStudent().getName().equals("") && studentDTO.getStudent().getPhoneNumber().matches("\\d{10}"))) {
                    studentService.save(studentDTO.getStudent());
                    attributes.addFlashAttribute("mess", "create successfully..!!!");
                }else{
                    attributes.addFlashAttribute("error", "phone number already exists");
                }
            }
            return new ResponseEntity<>(studentList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    // Dowload file
    @GetMapping("/download/studentList.xlsx")
    public void downloadCsv(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=studentList.xlsx");

            ByteArrayInputStream stream = ExportFile.contactListToExcelFile(createTestData());

            IOUtils.copy(stream, response.getOutputStream());
        }catch (Exception e){
            return;
        }

    }


    // create file excel
    @PostMapping("/export")
    public ResponseEntity<?> exportStudent(@RequestBody List<Student> studentList) {
        try {
            studentService.exportToExcel(studentList);
            students = studentList;
            return new ResponseEntity<>(studentList,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private List<Student> createTestData() {
        return students;
    }





}



