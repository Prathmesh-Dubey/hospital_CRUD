package com.example.springcrud.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.springcrud.model.Medicines;

@Repository
public interface MedicinesRepository extends MongoRepository<Medicines, String> {
}