package com.example.springcrud.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.springcrud.model.MedicinesTest;
import com.example.springcrud.repository.MedicinesTestRepository;

@RestController
@RequestMapping("/api/medicines-tests")
public class MedicinesTestController {

    @Autowired
    private MedicinesTestRepository medicinesTestRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<MedicinesTest> createMedicinesTest(
            @RequestBody MedicinesTest medicinesTest) {

        MedicinesTest savedTest = medicinesTestRepository.save(medicinesTest);
        return new ResponseEntity<>(savedTest, HttpStatus.CREATED);
    }

    // READ - Get all tests (with filters)
    @GetMapping
    public ResponseEntity<List<MedicinesTest>> getAllMedicinesTests(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String doctorId,
            @RequestParam(required = false) String testName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String resultStatus,
            @RequestParam(required = false) Double maxPrice
    ) {

        List<MedicinesTest> tests = medicinesTestRepository.findAll();

        List<MedicinesTest> filteredTests = tests.stream()
                .filter(t -> patientId == null ||
                        (t.getPatientId() != null &&
                         patientId.equalsIgnoreCase(t.getPatientId())))
                .filter(t -> doctorId == null ||
                        (t.getDoctorId() != null &&
                         doctorId.equalsIgnoreCase(t.getDoctorId())))
                .filter(t -> testName == null ||
                        (t.getTestName() != null &&
                         t.getTestName().toLowerCase().contains(testName.toLowerCase())))
                .filter(t -> category == null ||
                        (t.getCategory() != null &&
                         category.equalsIgnoreCase(t.getCategory())))
                .filter(t -> resultStatus == null ||
                        (t.getResultStatus() != null &&
                         resultStatus.equalsIgnoreCase(t.getResultStatus())))
                .filter(t -> maxPrice == null ||
                        (t.getPrice() != null && t.getPrice() <= maxPrice))
                .collect(Collectors.toList());

        return new ResponseEntity<>(filteredTests, HttpStatus.OK);
    }

    // READ - Get test by Mongo _id
    @GetMapping("/{id}")
    public ResponseEntity<MedicinesTest> getMedicinesTestById(@PathVariable String id) {
        Optional<MedicinesTest> test = medicinesTestRepository.findById(id);
        return test.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // UPDATE (DetailsController style)
    @PutMapping("/{id}")
    public ResponseEntity<MedicinesTest> updateMedicinesTest(
            @PathVariable String id,
            @RequestBody MedicinesTest medicinesTest
    ) {

        Optional<MedicinesTest> testOptional =
                medicinesTestRepository.findById(id);

        if (testOptional.isPresent()) {
            MedicinesTest testToUpdate = testOptional.get();

            testToUpdate.setPatientId(medicinesTest.getPatientId());
            testToUpdate.setDoctorId(medicinesTest.getDoctorId());
            testToUpdate.setTestName(medicinesTest.getTestName());
            testToUpdate.setCategory(medicinesTest.getCategory());
            testToUpdate.setPrice(medicinesTest.getPrice());
            testToUpdate.setDescription(medicinesTest.getDescription());
            testToUpdate.setResultStatus(medicinesTest.getResultStatus());
            testToUpdate.setHistory(medicinesTest.getHistory());

            MedicinesTest updatedTest =
                    medicinesTestRepository.save(testToUpdate);

            return new ResponseEntity<>(updatedTest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteMedicinesTest(@PathVariable String id) {
        try {
            medicinesTestRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
