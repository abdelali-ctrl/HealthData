package com.healthdata.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private int age;
    private String sexe;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private transient List<VitalRecord> vitals;

    public Patient() {}

    public Patient(String nom, int age, String sexe) {
        this.nom = nom;
        this.age = age;
        this.sexe = sexe;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public int getAge() {
        return age;
    }

    public String getSexe() {
        return sexe;
    }

    public List<VitalRecord> getVitals() {
        return vitals;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setVitals(List<VitalRecord> vitals) {
        this.vitals = vitals;
    }

}
