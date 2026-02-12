package com.example.springcrud.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.springcrud.model.Admin;
import com.example.springcrud.repository.AdminRepository;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        Admin savedAdmin = adminRepository.save(admin);
        return new ResponseEntity<>(savedAdmin, HttpStatus.CREATED);
    }

    // ================= READ WITH FILTERS =================
    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdAfter,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdBefore,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime lastLoginAfter,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime lastLoginBefore
    ) {

        List<Admin> admins = adminRepository.findAll();

        List<Admin> filteredAdmins = admins.stream()

                // username (partial, case-insensitive)
                .filter(a -> username == null ||
                        (a.getUsername() != null &&
                         a.getUsername().toLowerCase()
                           .contains(username.toLowerCase())))

                // email (exact)
                .filter(a -> email == null ||
                        (a.getEmail() != null &&
                         a.getEmail().equalsIgnoreCase(email)))

                // role
                .filter(a -> role == null ||
                        (a.getRole() != null &&
                         a.getRole().equalsIgnoreCase(role)))

                // status
                .filter(a -> status == null ||
                        (a.getStatus() != null &&
                         a.getStatus().equalsIgnoreCase(status)))

                // createdAt >= createdAfter
                .filter(a -> createdAfter == null ||
                        (a.getCreatedAt() != null &&
                         a.getCreatedAt().isAfter(createdAfter)))

                // createdAt <= createdBefore
                .filter(a -> createdBefore == null ||
                        (a.getCreatedAt() != null &&
                         a.getCreatedAt().isBefore(createdBefore)))

                // lastLoginAt >= lastLoginAfter
                .filter(a -> lastLoginAfter == null ||
                        (a.getLastLoginAt() != null &&
                         a.getLastLoginAt().isAfter(lastLoginAfter)))

                // lastLoginAt <= lastLoginBefore
                .filter(a -> lastLoginBefore == null ||
                        (a.getLastLoginAt() != null &&
                         a.getLastLoginAt().isBefore(lastLoginBefore)))

                .collect(Collectors.toList());

        return new ResponseEntity<>(filteredAdmins, HttpStatus.OK);
    }

    // ================= READ BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable String id) {
        Optional<Admin> admin = adminRepository.findById(id);
        return admin.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(
            @PathVariable String id,
            @RequestBody Admin admin
    ) {

        Optional<Admin> adminOptional = adminRepository.findById(id);

        if (adminOptional.isPresent()) {
            Admin existing = adminOptional.get();

            existing.setUsername(admin.getUsername());
            existing.setEmail(admin.getEmail());
            existing.setRole(admin.getRole());
            existing.setStatus(admin.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());

            Admin updatedAdmin = adminRepository.save(existing);
            return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAdmin(@PathVariable String id) {
        try {
            adminRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
