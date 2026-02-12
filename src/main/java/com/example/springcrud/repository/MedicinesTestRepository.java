package com.example.springcrud.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.springcrud.model.MedicinesTest;

@Repository
public interface MedicinesTestRepository extends MongoRepository<MedicinesTest, String> {
}