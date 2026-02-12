package com.example.springcrud.controller;

import com.example.springcrud.model.Clinic;
import com.example.springcrud.repository.ClinicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clinics")
public class ClinicController {

    @Autowired
    private ClinicRepository clinicRepository;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<Clinic> createClinic(@RequestBody Clinic clinic) {

        if (clinic.getAudit() != null) {
            clinic.getAudit().setCreatedAt(LocalDateTime.now());
            clinic.getAudit().setUpdatedAt(LocalDateTime.now());
        }

        return ResponseEntity.ok(clinicRepository.save(clinic));
    }

    // ================= GET WITH FILTERS =================
    @GetMapping
    public ResponseEntity<List<Clinic>> getClinics(
            @RequestParam(required = false) String clinicType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String service,
            @RequestParam(required = false) Boolean appointmentRequired
    ) {

        List<Clinic> clinics = clinicRepository.findAll();

        if (clinicType != null) {
            clinics = clinics.stream()
                    .filter(c -> clinicType.equalsIgnoreCase(c.getClinicType()))
                    .collect(Collectors.toList());
        }

        if (status != null) {
            clinics = clinics.stream()
                    .filter(c -> status.equalsIgnoreCase(c.getStatus()))
                    .collect(Collectors.toList());
        }

        if (city != null) {
            clinics = clinics.stream()
                    .filter(c -> c.getAddress() != null &&
                            city.equalsIgnoreCase(c.getAddress().getCity()))
                    .collect(Collectors.toList());
        }

        if (department != null) {
            clinics = clinics.stream()
                    .filter(c -> c.getDepartments() != null &&
                            c.getDepartments().contains(department))
                    .collect(Collectors.toList());
        }

        if (service != null) {
            clinics = clinics.stream()
                    .filter(c -> c.getServices() != null &&
                            c.getServices().contains(service))
                    .collect(Collectors.toList());
        }

        if (appointmentRequired != null) {
            clinics = clinics.stream()
                    .filter(c -> appointmentRequired.equals(c.getAppointmentRequired()))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(clinics);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<Clinic> getClinicById(@PathVariable String id) {

        Optional<Clinic> clinic = clinicRepository.findById(id);
        return clinic.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<Clinic> updateClinic(
            @PathVariable String id,
            @RequestBody Clinic updatedClinic) {

        return clinicRepository.findById(id)
                .map(existing -> {

                    updatedClinic.setClinicId(existing.getClinicId());

                    if (updatedClinic.getAudit() != null) {
                        updatedClinic.getAudit().setUpdatedAt(LocalDateTime.now());
                    }

                    return ResponseEntity.ok(clinicRepository.save(updatedClinic));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinic(@PathVariable String id) {

        if (!clinicRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        clinicRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
