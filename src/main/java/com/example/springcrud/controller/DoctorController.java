package com.example.springcrud.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.springcrud.model.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springcrud.model.Doctor;
import com.example.springcrud.model.LoginRequest;
import com.example.springcrud.repository.DoctorRepository;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

        @Autowired
        private BCryptPasswordEncoder passwordEncoder;

        @Autowired
        private DoctorRepository doctorRepository;

        // ================= CREATE =================
        @PostMapping
        public ResponseEntity<?> createDoctor(@RequestBody Doctor doctor) {

                doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));

                Doctor savedDoctor = doctorRepository.save(doctor);

                savedDoctor.setPassword(null); // never return password

                return new ResponseEntity<>(savedDoctor, HttpStatus.CREATED);
        }

        // ================= READ WITH FILTERS =================
        @GetMapping
        public ResponseEntity<List<Doctor>> getAllDoctors(
                        @RequestParam(required = false) String name,
                        @RequestParam(required = false) String specialization,
                        @RequestParam(required = false) Integer minExperience,
                        @RequestParam(required = false) Integer maxExperience,
                        @RequestParam(required = false) String qualification,
                        @RequestParam(required = false) String gender,
                        @RequestParam(required = false) String hospitalName,
                        @RequestParam(required = false) Double minFee,
                        @RequestParam(required = false) Double maxFee,
                        @RequestParam(required = false) Double minRating,
                        @RequestParam(required = false) String availability,
                        @RequestParam(required = false) String address) {

                List<Doctor> doctors = doctorRepository.findAll();

                List<Doctor> filteredDoctors = doctors.stream()

                                .filter(d -> name == null ||
                                                (d.getName() != null &&
                                                                d.getName().toLowerCase().contains(name.toLowerCase())))

                                .filter(d -> specialization == null ||
                                                (d.getSpecialization() != null &&
                                                                d.getSpecialization().toLowerCase().contains(
                                                                                specialization.toLowerCase())))

                                .filter(d -> minExperience == null ||
                                                (d.getExperience() != null &&
                                                                d.getExperience() >= minExperience))

                                .filter(d -> maxExperience == null ||
                                                (d.getExperience() != null &&
                                                                d.getExperience() <= maxExperience))

                                .filter(d -> qualification == null ||
                                                (d.getQualification() != null &&
                                                                d.getQualification().stream()
                                                                                .anyMatch(q -> q.equalsIgnoreCase(
                                                                                                qualification))))

                                .filter(d -> gender == null ||
                                                (d.getGender() != null &&
                                                                d.getGender().equalsIgnoreCase(gender)))

                                .filter(d -> hospitalName == null ||
                                                (d.getHospitalName() != null &&
                                                                d.getHospitalName().toLowerCase()
                                                                                .contains(hospitalName.toLowerCase())))

                                .filter(d -> minFee == null ||
                                                (d.getConsultationFee() != null &&
                                                                d.getConsultationFee() >= minFee))

                                .filter(d -> maxFee == null ||
                                                (d.getConsultationFee() != null &&
                                                                d.getConsultationFee() <= maxFee))

                                .filter(d -> minRating == null ||
                                                (d.getRating() != null &&
                                                                d.getRating() >= minRating))

                                .filter(d -> availability == null ||
                                                (d.getAvailability() != null &&
                                                                d.getAvailability().equalsIgnoreCase(availability)))

                                .filter(d -> address == null ||
                                                (d.getAddress() != null &&
                                                                d.getAddress().toLowerCase()
                                                                                .contains(address.toLowerCase())))

                                .collect(Collectors.toList());

                return new ResponseEntity<>(filteredDoctors, HttpStatus.OK);
        }

        // ================= READ BY ID =================
        @GetMapping("/{doctorId}")
        public ResponseEntity<Doctor> getDoctorById(@PathVariable String doctorId) {
                Optional<Doctor> doctor = doctorRepository.findById(doctorId);
                return doctor.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        // ================= auth nby phone and password =================
        @PostMapping("/login")
        public ResponseEntity<?> authDoctor(@RequestBody LoginRequest request) {

                Optional<Doctor> doctor = doctorRepository.findByPhone(request.getPhone());

                if (doctor.isPresent() &&
                                passwordEncoder.matches(request.getPassword(), doctor.get().getPassword())) {

                        Doctor loggedInDoctor = doctor.get();

                        LoginResponse response = new LoginResponse(
                                        "Login Successful",
                                        loggedInDoctor.getPhone(), // use ID, not phone
                                        loggedInDoctor.getName(),
                                        "DOCTOR");

                        return ResponseEntity.ok(response);

                }

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Invalid phone or password");
        }

        // ================= UPDATE =================
        @PutMapping("/{doctorId}")
        public ResponseEntity<Doctor> updateDoctor(
                        @PathVariable String doctorId,
                        @RequestBody Doctor doctor) {

                return doctorRepository.findById(doctorId)
                                .map(existing -> {

                                        existing.setName(doctor.getName());
                                        existing.setSpecialization(doctor.getSpecialization());
                                        existing.setExperience(doctor.getExperience());
                                        existing.setQualification(doctor.getQualification());
                                        existing.setGender(doctor.getGender());
                                        existing.setPhone(doctor.getPhone());
                                        existing.setEmail(doctor.getEmail());
                                        existing.setConsultationFee(doctor.getConsultationFee());
                                        existing.setAvailability(doctor.getAvailability());
                                        existing.setHospitalName(doctor.getHospitalName());
                                        existing.setRating(doctor.getRating());
                                        existing.setAddress(doctor.getAddress());

                                        Doctor updated = doctorRepository.save(existing);
                                        return new ResponseEntity<>(updated, HttpStatus.OK);
                                })
                                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        // ================= DELETE =================
        @DeleteMapping("/{doctorId}")
        public ResponseEntity<HttpStatus> deleteDoctor(@PathVariable String doctorId) {
                try {
                        doctorRepository.deleteById(doctorId);
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } catch (Exception e) {
                        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
        }
}
