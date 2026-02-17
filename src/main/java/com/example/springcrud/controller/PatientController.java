package com.example.springcrud.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.springcrud.model.LoginRequest;
import com.example.springcrud.model.LoginResponse;
import com.example.springcrud.model.Patient;
import com.example.springcrud.repository.PatientRepository;
import com.example.springcrud.service.SequenceGeneratorService;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // CREATE
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {

        patient.setId(null);

        long seq = sequenceGeneratorService.generateSequence("patient_sequence");
        patient.setPatientId("PAT-" + (1000 + seq));

        // 🔐 HASH PASSWORD BEFORE SAVING
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));

        Patient savedPatient = patientRepository.save(patient);

        savedPatient.setPassword(null); // never return password

        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<Patient> createPatient(
            @PathVariable String doctorId,
            @RequestBody Patient patient) {

        patient.setId(null);

        long seq = sequenceGeneratorService.generateSequence("patient_sequence");
        patient.setPatientId("PAT-" + (1000 + seq));

        // Assign doctorId automatically
        patient.setDoctorId(doctorId);

        // Hash password
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));

        Patient savedPatient = patientRepository.save(patient);

        savedPatient.setPassword(null);

        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Patient>> getPatientsByDoctor(@PathVariable String doctorId) {
        List<Patient> patients = patientRepository.findByDoctorId(doctorId);
        return ResponseEntity.ok(patients);
    }

    // READ - Get all patients (with filters)
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients(
            @RequestParam(required = false) String patientId,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String emailAddress) {

        List<Patient> patients = patientRepository.findAll();

        List<Patient> filteredPatients = patients.stream()
                .filter(p -> patientId == null ||
                        (p.getPatientId() != null &&
                                patientId.equalsIgnoreCase(p.getPatientId())))
                .filter(p -> fullName == null ||
                        (p.getFullName() != null &&
                                p.getFullName().toLowerCase().contains(fullName.toLowerCase())))
                .filter(p -> gender == null ||
                        (p.getGender() != null &&
                                gender.equalsIgnoreCase(p.getGender())))
                .filter(p -> {
                    if (bloodGroup == null)
                        return true;
                    if (p.getBloodGroup() == null)
                        return false;

                    String decodedBloodGroup = bloodGroup.replace(" ", "+");
                    return decodedBloodGroup.equalsIgnoreCase(p.getBloodGroup());
                })

                .filter(p -> phone == null ||
                        (p.getPhone() != null &&
                                p.getPhone().contains(phone)))
                .filter(p -> emailAddress == null ||
                        (p.getEmailAddress() != null &&
                                p.getEmailAddress().equalsIgnoreCase(emailAddress)))
                .collect(Collectors.toList());

        return new ResponseEntity<>(filteredPatients, HttpStatus.OK);
    }

    // READ - Get patient by Mongo _id
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // UPDATE (DetailsController style)
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable String id,
            @RequestBody Patient patient) {

        Optional<Patient> patientOptional = patientRepository.findById(id);

        if (patientOptional.isPresent()) {
            Patient patientToUpdate = patientOptional.get();

            // DO NOT update patientId
            patientToUpdate.setFullName(patient.getFullName());
            patientToUpdate.setDateOfBirth(patient.getDateOfBirth());
            patientToUpdate.setGender(patient.getGender());
            patientToUpdate.setPhone(patient.getPhone());
            patientToUpdate.setEmailAddress(patient.getEmailAddress());
            patientToUpdate.setResidentialAddress(patient.getResidentialAddress());
            patientToUpdate.setEmergencyContact(patient.getEmergencyContact());
            patientToUpdate.setBloodGroup(patient.getBloodGroup());
            patientToUpdate.setAllergies(patient.getAllergies());
            patientToUpdate.setChronicDiseases(patient.getChronicDiseases());
            patientToUpdate.setCurrentMedications(patient.getCurrentMedications());
            patientToUpdate.setHeight(patient.getHeight());
            patientToUpdate.setWeight(patient.getWeight());

            Patient updatedPatient = patientRepository.save(patientToUpdate);
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginPatient(@RequestBody LoginRequest request) {

        Optional<Patient> patient = patientRepository.findByPhone(request.getPhone());

        if (patient.isPresent() &&
                passwordEncoder.matches(request.getPassword(), patient.get().getPassword())) {

            Patient loggedInPatient = patient.get();

            LoginResponse response = new LoginResponse(
                    "Login Successful",
                    loggedInPatient.getId(),
                    loggedInPatient.getFullName(),
                    "PATIENT");

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials");
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePatient(@PathVariable String id) {
        try {
            patientRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
