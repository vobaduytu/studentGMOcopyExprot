package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.service.StudentClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/display")
public class DisplayController {

@Autowired
private StudentClassService studentClassService;

    @GetMapping(value = "")
    public String todoList(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("listProgramme", studentClassService.showAll());
        return "display";
    }
}
