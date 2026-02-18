package com.example.springcrud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class docWebController {

    // ================= LANDING PAGE =================
    @GetMapping("/doctor")
    public String Login() {
        return "Login";
    }

    @GetMapping("/doctor/home")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Doctor Home");
        model.addAttribute("activeTab", "home");
        return "doctor/docProfile";
    }

    @GetMapping("/doctor/doctorRegister")
    public String adminDoctorRegister(Model model) {
        model.addAttribute("pageTitle", "Doctor Register");
        model.addAttribute("activeTab", "doctorRegister");
        return "admin/doctor-register";
    }

    @GetMapping("/doctor/dashboard")
    public String adminDoctorDashboard(Model model) {
        model.addAttribute("pageTitle", "Doctor Dashboard");
        model.addAttribute("activeTab", "doctorDashboard");
        return "doctor/docDashboard";
    }

    @GetMapping("/doctor/login")
    public String doctorLoginPage() {
        return "doctor/docLogin";
    }

    @GetMapping("/doctor/patients")
    public String doctorPatientsPage() {
        return "doctor/docPatients";
    }

    @GetMapping("/doctor/addPatient")
    public String doctorAddPatientPage() {
        return "doctor/docAddPatient";
    }

    @GetMapping("/doctor/clinics")
    public String doctorClinicsPage() {
        return "doctor/docClinic";
    }
    @GetMapping("/doctor/addClinic")
    public String doctorAddClinicPage() {
        return "doctor/docAddClinic";
    }

    @GetMapping("/doctor/tests")
    public String doctorTestsPage() {
        return "doctor/docTest";
    }

    @GetMapping("/doctor/addTest")
    public String doctorAddTestPage() {
        return "doctor/docAddTest";
    }
}
