package com.example.springcrud.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.springcrud.model.Clinic;

@Repository
public interface ClinicRepository extends MongoRepository<Clinic, String> {
}