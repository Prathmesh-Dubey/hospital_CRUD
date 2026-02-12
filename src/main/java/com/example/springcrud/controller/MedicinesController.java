package com.example.springcrud.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.springcrud.model.Medicines;
import com.example.springcrud.repository.MedicinesRepository;

@RestController
@RequestMapping("/api/medicines")
public class MedicinesController {

    @Autowired
    private MedicinesRepository medicinesRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<Medicines> createMedicine(@RequestBody Medicines medicine) {
        Medicines savedMedicine = medicinesRepository.save(medicine);
        return new ResponseEntity<>(savedMedicine, HttpStatus.CREATED);
    }

    // READ - Get all medicines (with optional filters)
    @GetMapping
    public ResponseEntity<List<Medicines>> getAllMedicines(
            @RequestParam(required = false) String medId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String recordStatus,
            @RequestParam(required = false) Boolean doctorChangeAllowed,
            @RequestParam(required = false) Double maxPrice
    ) {

        List<Medicines> medicines = medicinesRepository.findAll();

        List<Medicines> filteredMedicines = medicines.stream()
                .filter(m -> medId == null ||
                        (m.getMedId() != null && medId.equalsIgnoreCase(m.getMedId())))
                .filter(m -> name == null ||
                        (m.getName() != null && m.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(m -> companyName == null ||
                        (m.getCompanyName() != null &&
                         companyName.equalsIgnoreCase(m.getCompanyName())))
                .filter(m -> recordStatus == null ||
                        (m.getRecordStatus() != null &&
                         recordStatus.equalsIgnoreCase(m.getRecordStatus())))
                .filter(m -> doctorChangeAllowed == null ||
                        (m.getDoctorChangeAllowed() != null &&
                         m.getDoctorChangeAllowed().equals(doctorChangeAllowed)))
                .filter(m -> maxPrice == null ||
                        (m.getPrice() != null && m.getPrice() <= maxPrice))
                .collect(Collectors.toList());

        return new ResponseEntity<>(filteredMedicines, HttpStatus.OK);
    }

    // READ - Get medicine by Mongo _id
    @GetMapping("/{id}")
    public ResponseEntity<Medicines> getMedicineById(@PathVariable String id) {
        Optional<Medicines> medicine = medicinesRepository.findById(id);
        return medicine.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // UPDATE (DetailsController style)
    @PutMapping("/{id}")
    public ResponseEntity<Medicines> updateMedicine(
            @PathVariable String id,
            @RequestBody Medicines medicine
    ) {

        Optional<Medicines> medicineOptional = medicinesRepository.findById(id);

        if (medicineOptional.isPresent()) {
            Medicines medicineToUpdate = medicineOptional.get();

            medicineToUpdate.setMedId(medicine.getMedId());
            medicineToUpdate.setName(medicine.getName());
            medicineToUpdate.setCompanyName(medicine.getCompanyName());
            medicineToUpdate.setRecordStatus(medicine.getRecordStatus());
            medicineToUpdate.setDoctorChangeAllowed(medicine.getDoctorChangeAllowed());
            medicineToUpdate.setDosage(medicine.getDosage());
            medicineToUpdate.setRoute(medicine.getRoute());
            medicineToUpdate.setFrequency(medicine.getFrequency());
            medicineToUpdate.setDuration(medicine.getDuration());
            medicineToUpdate.setExpiryDate(medicine.getExpiryDate());
            medicineToUpdate.setPrice(medicine.getPrice());
            medicineToUpdate.setStartDate(medicine.getStartDate());
            medicineToUpdate.setEndDate(medicine.getEndDate());
            medicineToUpdate.setSpecialInstructions(medicine.getSpecialInstructions());

            Medicines updatedMedicine = medicinesRepository.save(medicineToUpdate);
            return new ResponseEntity<>(updatedMedicine, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMedicine(@PathVariable String id) {
        try {
            medicinesRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
