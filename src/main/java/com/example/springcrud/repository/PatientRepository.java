package com.example.springcrud.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.springcrud.model.Patient;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {
}