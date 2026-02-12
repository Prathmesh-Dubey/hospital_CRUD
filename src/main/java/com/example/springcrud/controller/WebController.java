package com.example.springcrud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // ================= LANDING PAGE =================
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ================= ADMIN PANEL ROUTES =================
    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("activeTab", "dashboard");
        return "admin/dashboard";
    }

    @GetMapping("/admin/patients")
    public String adminPatients(Model model) {
        model.addAttribute("pageTitle", "Patient Management");
        model.addAttribute("activeTab", "patients");
        return "admin/patients";
    }

    @GetMapping("/admin/doctors")
    public String adminDoctors(Model model) {
        model.addAttribute("pageTitle", "Doctor Management");
        model.addAttribute("activeTab", "doctors");
        return "admin/doctors";
    }

    @GetMapping("/admin/clinics")
    public String adminClinics(Model model) {
        model.addAttribute("pageTitle", "Clinic Management");
        model.addAttribute("activeTab", "clinics");
        return "admin/clinics";
    }

    @GetMapping("/admin/medicines")
    public String adminMedicines(Model model) {
        model.addAttribute("pageTitle", "Medicines Management");
        model.addAttribute("activeTab", "medicines");
        return "admin/medicines";
    }

    @GetMapping("/admin/tests")
    public String adminTests(Model model) {
        model.addAttribute("pageTitle", "Tests Management");
        model.addAttribute("activeTab", "tests");
        return "admin/tests";
    }

    @GetMapping("/admin/profile")
    public String adminProfile(Model model) {
        model.addAttribute("pageTitle", "Admin Profile");
        model.addAttribute("activeTab", "profile");
        return "admin/profile";
    }
}
