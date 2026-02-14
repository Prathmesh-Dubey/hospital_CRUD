package com.example.springcrud.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.springcrud.model.DoctorLogin;

public interface DoctorLoginRepository extends MongoRepository<DoctorLogin, String> {

    Optional<DoctorLogin> findByPhone(String phone);

    Optional<DoctorLogin> findByPhoneAndPassword(String phone, String password);
}
