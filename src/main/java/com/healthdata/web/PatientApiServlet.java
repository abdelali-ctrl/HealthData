package com.healthdata.web;

import com.google.gson.Gson;
import com.healthdata.dao.PatientDao;
import com.healthdata.model.Patient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.GsonBuilder;
import com.healthdata.web.util.LocalDateTimeAdapter;
import java.time.LocalDateTime;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A true RESTful servlet for Patient resources.
 * This servlet speaks JSON, not HTML/JSP.
 */
@WebServlet("/api/patients/*")
public class PatientApiServlet extends HttpServlet {

    private final PatientDao patientDao = new PatientDao();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    /**
     * Handles GET requests:
     * - GET /api/patients -> Returns all patients
     * - GET /api/patients/1 -> Returns patient with ID 1
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        Long patientId = parsePatientId(pathInfo);

        if (patientId == null) {
            // No ID: /api/patients
            List<Patient> patients = patientDao.findAll();
            sendJsonResponse(resp, patients);
        } else {
            // With ID: /api/patients/1
            Patient patient = patientDao.findById(patientId);
            if (patient != null) {
                sendJsonResponse(resp, patient);
            } else {
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Patient not found");
            }
        }
    }

    /**
     * Handles POST requests:
     * - POST /api/patients -> Creates a new patient from JSON body
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Read the JSON body from the request
            String jsonBody = req.getReader().lines().collect(Collectors.joining());
            Patient newPatient = gson.fromJson(jsonBody, Patient.class);

            // Save new patient
            patientDao.save(newPatient); // 'newPatient' object will be updated with the new ID

            resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            sendJsonResponse(resp, newPatient);
        } else {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "POST not allowed with ID");
        }
    }

    /**
     * Handles PUT requests:
     * - PUT /api/patients/1 -> Updates patient with ID 1 from JSON body
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long patientId = parsePatientId(req.getPathInfo());

        if (patientId == null) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing patient ID for update");
            return;
        }

        Patient existingPatient = patientDao.findById(patientId);
        if (existingPatient == null) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Patient not found");
            return;
        }

        // Read JSON body and update existing patient
        String jsonBody = req.getReader().lines().collect(Collectors.joining());
        Patient updatedData = gson.fromJson(jsonBody, Patient.class);

        // Update fields
        existingPatient.setNom(updatedData.getNom());
        existingPatient.setAge(updatedData.getAge());
        existingPatient.setSexe(updatedData.getSexe());
        // Do not set ID!

        patientDao.update(existingPatient);
        sendJsonResponse(resp, existingPatient);
    }

    /**
     * Handles DELETE requests:
     * - DELETE /api/patients/1 -> Deletes patient with ID 1
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long patientId = parsePatientId(req.getPathInfo());

        if (patientId == null) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing patient ID for delete");
            return;
        }

        Patient existingPatient = patientDao.findById(patientId);
        if (existingPatient == null) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Patient not found");
            return;
        }

        patientDao.deleteById(patientId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
    }

    // --- Helper Methods ---

    /**
     * Tries to parse a patient ID from the /path/info/
     */
    private Long parsePatientId(String pathInfo) {
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                return Long.parseLong(pathInfo.substring(1)); // Remove leading "/"
            } catch (NumberFormatException e) {
                // Not a number, ignore
            }
        }
        return null; // No ID
    }

    /**
     * Sends an object as JSON response
     */
    private void sendJsonResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(gson.toJson(data));
        out.flush();
    }

    /**
     * Sends a JSON error response
     */
    private void sendError(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print("{\"error\":\"" + message + "\"}");
        out.flush();
    }
}