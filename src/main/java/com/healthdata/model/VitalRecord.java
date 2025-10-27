package com.healthdata.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class VitalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateMesure;
    private double valeur;
    private String typeMesure; // e.g., Temperature, HeartRate

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public VitalRecord() {}

    public VitalRecord(LocalDateTime dateMesure, double valeur, String typeMesure, Patient patient) {
        this.dateMesure = dateMesure;
        this.valeur = valeur;
        this.typeMesure = typeMesure;
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getTypeMesure() {
        return typeMesure;
    }

    public void setTypeMesure(String typeMesure) {
        this.typeMesure = typeMesure;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public LocalDateTime getDateMesure() {
        return dateMesure;
    }

    public void setDateMesure(LocalDateTime dateMesure) {
        this.dateMesure = dateMesure;
    }
}
