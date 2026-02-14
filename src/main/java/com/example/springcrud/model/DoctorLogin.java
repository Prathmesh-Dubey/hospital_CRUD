package com.example.springcrud.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "doctorLogin")
public class DoctorLogin {

    @Id
    private String id;

    @Indexed(unique = true)
    private String phone;

    private String password;
    private String name;
    private String specialization;
    private int experience;
    private String qualification;
    private String email;

    public DoctorLogin() {
    }

    public DoctorLogin(String phone, String password, String name,
                       String specialization, int experience,
                       String qualification, String email) {
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.specialization = specialization;
        this.experience = experience;
        this.qualification = qualification;
        this.email = email;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
