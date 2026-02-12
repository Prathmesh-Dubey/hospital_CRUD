package com.example.springcrud.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.springcrud.model.Details;

@Repository
public interface DetailsRepository extends MongoRepository<Details, String> {
}