package com.healthdata.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.healthdata.dao.PatientDao;
import com.healthdata.dao.VitalRecordDao;
import com.healthdata.model.Patient;
import com.healthdata.model.VitalRecord;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class ContinuousDataGenerator implements ServletContextListener {

    private ScheduledExecutorService scheduler;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final Random rnd = new Random();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // This runs when the application starts
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // Define the task to run repeatedly
        Runnable dataTask = () -> {
            try {
                // Task 1: Add a new patient from the external API
                addNewPatientFromApi();

                // Task 2: Add a new vital to an existing patient
                addVitalToExistingPatient();

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // Schedule the task to start after 5 seconds, and then run every 15 seconds
        System.out.println("--- Continuous Data Generator Started ---");
        scheduler.scheduleAtFixedRate(dataTask, 5, 15, TimeUnit.SECONDS);
    }

    private void addNewPatientFromApi() {
        try {
            HttpRequest apiRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://randomuser.me/api/?inc=name,gender,dob"))
                    .GET()
                    .build();
            HttpResponse<String> apiResponse = httpClient.send(apiRequest, HttpResponse.BodyHandlers.ofString());

            JsonObject user = gson.fromJson(apiResponse.body(), JsonObject.class)
                    .getAsJsonArray("results").get(0).getAsJsonObject();

            String nom = user.getAsJsonObject("name").get("first").getAsString() + " " +
                    user.getAsJsonObject("name").get("last").getAsString();
            int age = user.getAsJsonObject("dob").get("age").getAsInt();
            String sexe = "male".equals(user.get("gender").getAsString()) ? "H" : "F";

            Patient p = new Patient(nom, age, sexe);

            // Use a fresh DAO for this thread
            PatientDao patientDao = new PatientDao();
            patientDao.save(p);
            System.out.println("BACKGROUND: Added new patient: " + p.getNom());

        } catch (Exception e) {
            System.err.println("BACKGROUND: Failed to add new patient from API: " + e.getMessage());
        }
    }

    private void addVitalToExistingPatient() {
        try {
            PatientDao patientDao = new PatientDao();
            VitalRecordDao vitalDao = new VitalRecordDao();

            List<Patient> allPatients = patientDao.findAll();
            if (allPatients.isEmpty()) {
                return; // No patients to update yet
            }

            // Pick a random patient from the list
            Patient patient = allPatients.get(rnd.nextInt(allPatients.size()));

            // Simulate a new heart rate reading
            String type = "HeartRate";
            double val = 60 + rnd.nextDouble() * 40; // 60-100

            VitalRecord v = new VitalRecord(LocalDateTime.now(), val, type, patient);
            vitalDao.save(v);
            System.out.println("BACKGROUND: Added new vital for: " + patient.getNom());

        } catch (Exception e) {
            System.err.println("BACKGROUND: Failed to add vital: " + e.getMessage());
        }
    }


    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // This runs when the application stops
        if (scheduler != null) {
            scheduler.shutdown();
        }
        System.out.println("--- Continuous Data Generator Stopped ---");
    }
}