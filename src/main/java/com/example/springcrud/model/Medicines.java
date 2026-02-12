package com.example.springcrud.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "medicines")
public class Medicines {

    @Id
    private String id;   // maps to _id in MongoDB

    private String medId;
    private String name;
    private String companyName;

    private String recordStatus;
    private Boolean doctorChangeAllowed;

    private String dosage;
    private String route;
    private String frequency;
    private Integer duration; // in days

    private LocalDateTime expiryDate;
    private Double price;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private String specialInstructions;

    // ---------- CONSTRUCTORS ----------

    public Medicines() {
    }

    // ---------- GETTERS & SETTERS ----------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedId() {
        return medId;
    }

    public void setMedId(String medId) {
        this.medId = medId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Boolean getDoctorChangeAllowed() {
        return doctorChangeAllowed;
    }

    public void setDoctorChangeAllowed(Boolean doctorChangeAllowed) {
        this.doctorChangeAllowed = doctorChangeAllowed;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
}
