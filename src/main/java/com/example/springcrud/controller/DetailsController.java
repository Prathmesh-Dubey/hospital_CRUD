package com.example.springcrud.controller;

import com.example.springcrud.model.Details;
import com.example.springcrud.repository.DetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/details")
public class DetailsController {

    @Autowired
    private DetailsRepository detailsRepository;

    // ================= CREATE =================
    @PostMapping
    public ResponseEntity<Details> createDetails(@RequestBody Details details) {

        if (details.getAudit() != null) {
            details.getAudit().setCreatedAt(LocalDateTime.now());
            details.getAudit().setLastUpdatedAt(LocalDateTime.now());
        }

        return ResponseEntity.ok(detailsRepository.save(details));
    }

    // ================= GET WITH FILTERS =================
    @GetMapping
    public ResponseEntity<List<Details>> getDetails(
            @RequestParam(required = false) String recordStatus,
            @RequestParam(required = false) Boolean doctorChangeAllowed,
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String currentDoctorId,
            @RequestParam(required = false) String icdCode,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String treatmentStatus,
            @RequestParam(required = false) Boolean followUpRequired,
            @RequestParam(required = false) LocalDate treatmentStartFrom,
            @RequestParam(required = false) LocalDate treatmentStartTo
    ) {

        List<Details> records = detailsRepository.findAll();

        if (recordStatus != null) {
            records = records.stream()
                    .filter(d -> recordStatus.equalsIgnoreCase(d.getRecordStatus()))
                    .collect(Collectors.toList());
        }

        if (doctorChangeAllowed != null) {
            records = records.stream()
                    .filter(d -> doctorChangeAllowed.equals(d.getDoctorChangeAllowed()))
                    .collect(Collectors.toList());
        }

        if (patientId != null) {
            records = records.stream()
                    .filter(d -> d.getPatient() != null &&
                            patientId.equals(d.getPatient().getPatientId()))
                    .collect(Collectors.toList());
        }

        if (currentDoctorId != null) {
            records = records.stream()
                    .filter(d -> d.getCurrentDoctor() != null &&
                            currentDoctorId.equals(d.getCurrentDoctor().getDoctorId()))
                    .collect(Collectors.toList());
        }

        if (icdCode != null) {
            records = records.stream()
                    .filter(d -> d.getDiagnosis() != null &&
                            icdCode.equalsIgnoreCase(d.getDiagnosis().getIcdCode()))
                    .collect(Collectors.toList());
        }

        if (severity != null) {
            records = records.stream()
                    .filter(d -> d.getDiagnosis() != null &&
                            severity.equalsIgnoreCase(d.getDiagnosis().getSeverity()))
                    .collect(Collectors.toList());
        }

        if (treatmentStatus != null) {
            records = records.stream()
                    .filter(d -> d.getTreatmentTimeline() != null &&
                            treatmentStatus.equalsIgnoreCase(
                                    d.getTreatmentTimeline().getTreatmentStatus()))
                    .collect(Collectors.toList());
        }

        if (followUpRequired != null) {
            records = records.stream()
                    .filter(d -> d.getFollowUp() != null &&
                            followUpRequired.equals(d.getFollowUp().getFollowUpRequired()))
                    .collect(Collectors.toList());
        }

        if (treatmentStartFrom != null) {
            records = records.stream()
                    .filter(d -> d.getTreatmentTimeline() != null &&
                            d.getTreatmentTimeline().getTreatmentStartDate() != null &&
                            !d.getTreatmentTimeline().getTreatmentStartDate()
                                    .isBefore(treatmentStartFrom))
                    .collect(Collectors.toList());
        }

        if (treatmentStartTo != null) {
            records = records.stream()
                    .filter(d -> d.getTreatmentTimeline() != null &&
                            d.getTreatmentTimeline().getTreatmentStartDate() != null &&
                            !d.getTreatmentTimeline().getTreatmentStartDate()
                                    .isAfter(treatmentStartTo))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(records);
    }

    // ================= GET BY ID =================
    @GetMapping("/{id}")
    public ResponseEntity<Details> getDetailsById(@PathVariable String id) {

        Optional<Details> details = detailsRepository.findById(id);
        return details.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= UPDATE =================
    @PutMapping("/{id}")
    public ResponseEntity<Details> updateDetails(
            @PathVariable String id,
            @RequestBody Details updatedDetails) {

        return detailsRepository.findById(id)
                .map(existing -> {

                    updatedDetails.setPrescriptionId(existing.getPrescriptionId());

                    if (updatedDetails.getAudit() != null) {
                        updatedDetails.getAudit()
                                .setLastUpdatedAt(LocalDateTime.now());
                    }

                    return ResponseEntity.ok(detailsRepository.save(updatedDetails));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= DELETE =================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetails(@PathVariable String id) {

        if (!detailsRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        detailsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
