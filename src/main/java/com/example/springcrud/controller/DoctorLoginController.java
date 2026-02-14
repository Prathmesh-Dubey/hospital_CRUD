package com.example.springcrud.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.springcrud.model.DoctorLogin;
import com.example.springcrud.repository.DoctorLoginRepository;

@Controller
@RequestMapping("/doctor")
public class DoctorLoginController {

    @Autowired
    private DoctorLoginRepository doctorLoginRepository;

    // Show Register Page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("doctorLogin", new DoctorLogin());
        return "doctor-register";
    }

    // Register Doctor
    @PostMapping("/register")
    public String registerDoctor(@ModelAttribute DoctorLogin doctorLogin, Model model) {

        Optional<DoctorLogin> existing =
                doctorLoginRepository.findByPhone(doctorLogin.getPhone());

        if (existing.isPresent()) {
            model.addAttribute("error", "Phone number already registered!");
            return "doctor-register";
        }

        doctorLoginRepository.save(doctorLogin);
        model.addAttribute("success", "Account created successfully!");
        return "doctor-login";
    }

    // Show Login Page
    @GetMapping("/login")
    public String showLoginPage() {
        return "doctor-login";
    }

    // Login Doctor
    @PostMapping("/login")
    public String loginDoctor(@RequestParam String phone,
                              @RequestParam String password,
                              Model model) {

        Optional<DoctorLogin> doctor =
                doctorLoginRepository.findByPhoneAndPassword(phone, password);

        if (doctor.isPresent()) {
            model.addAttribute("doctor", doctor.get());
            return "doctor-dashboard";
        } else {
            model.addAttribute("error", "Invalid phone or password!");
            return "doctor-login";
        }
    }
}
