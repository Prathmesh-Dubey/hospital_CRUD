package com.example.springcrud.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.springcrud.model.Doctor;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {
}