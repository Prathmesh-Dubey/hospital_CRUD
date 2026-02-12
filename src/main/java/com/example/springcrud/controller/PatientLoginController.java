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

import com.example.springcrud.model.PatientLogin;
import com.example.springcrud.repository.PatientLoginRepository;

@RestController
@RequestMapping("/api/patient-logins")
public class PatientLoginController {

    @Autowired
    private PatientLoginRepository patientLoginRepository;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<PatientLogin> createPatientLogin(
            @RequestBody PatientLogin patientLogin) {

        patientLogin.setCreatedAt(LocalDateTime.now());
        patientLogin.setUpdatedAt(LocalDateTime.now());
        patientLogin.setStatus("ACTIVE");

        PatientLogin saved =
                patientLoginRepository.save(patientLogin);

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ================= READ WITH FILTERS =================
    @GetMapping
    public ResponseEntity<List<PatientLogin>> getAllPatientLogins(

            @RequestParam(required = false) String username,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
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

        List<PatientLogin> patients =
                patientLoginRepository.findAll();

        List<PatientLogin> filteredPatients = patients.stream()

                // username (partial)
                .filter(p -> username == null ||
                        (p.getUsername() != null &&
                         p.getUsername().toLowerCase()
                          .contains(username.toLowerCase())))

                // full name (partial)
                .filter(p -> fullName == null ||
                        (p.getFullName() != null &&
                         p.getFullName().toLowerCase()
                          .contains(fullName.toLowerCase())))

                // email (exact)
                .filter(p -> email == null ||
                        (p.getEmail() != null &&
                         p.getEmail().equalsIgnoreCase(email)))

                // phone (partial)
                .filter(p -> phoneNumber == null ||
                        (p.getPhoneNumber() != null &&
                         p.getPhoneNumber().contains(phoneNumber)))

                // status
                .filter(p -> status == null ||
                        (p.getStatus() != null &&
                         p.getStatus().equalsIgnoreCase(status)))

                // createdAt >= createdAfter
                .filter(p -> createdAfter == null ||
                        (p.getCreatedAt() != null &&
                         p.getCreatedAt().isAfter(createdAfter)))

                // createdAt <= createdBefore
                .filter(p -> createdBefore == null ||
                        (p.getCreatedAt() != null &&
                         p.getCreatedAt().isBefore(createdBefore)))

                // lastLoginAt >= lastLoginAfter
                .filter(p -> lastLoginAfter == null ||
                        (p.getLastLoginAt() != null &&
                         p.getLastLoginAt().isAfter(lastLoginAfter)))

                // lastLoginAt <= lastLoginBefore
                .filter(p -> lastLoginBefore == null ||
                        (p.getLastLoginAt() != null &&
                         p.getLastLoginAt().isBefore(lastLoginBefore)))

                .collect(Collectors.toList());

        return new ResponseEntity<>(filteredPatients, HttpStatus.OK);
    }

    // ================= READ BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<PatientLogin> getPatientLoginById(
            @PathVariable String id) {

        Optional<PatientLogin> patient =
                patientLoginRepository.findById(id);

        return patient.map(value ->
                new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() ->
                new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<PatientLogin> updatePatientLogin(
            @PathVariable String id,
            @RequestBody PatientLogin patientLogin
    ) {

        Optional<PatientLogin> optional =
                patientLoginRepository.findById(id);

        if (optional.isPresent()) {
            PatientLogin existing = optional.get();

            existing.setUsername(patientLogin.getUsername());
            existing.setFullName(patientLogin.getFullName());
            existing.setEmail(patientLogin.getEmail());
            existing.setPhoneNumber(patientLogin.getPhoneNumber());
            existing.setStatus(patientLogin.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());

            PatientLogin updated =
                    patientLoginRepository.save(existing);

            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePatientLogin(
            @PathVariable String id) {

        try {
            patientLoginRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
