package com.healthdata.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.healthdata.dao.PatientDao;
import com.healthdata.dao.VitalRecordDao;
import com.healthdata.model.Patient;
import com.healthdata.model.VitalRecord;
import com.healthdata.web.util.LocalDateTimeAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RESTful servlet for VitalRecord resources.
 * Handles JSON requests for vitals, stats, and anomalies.
 */
@WebServlet("/api/vitals/*")
public class VitalRecordApiServlet extends HttpServlet {

    private final VitalRecordDao vitalDao = new VitalRecordDao();
    private final PatientDao patientDao = new PatientDao();
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    /**
     * A helper class (DTO) for parsing incoming JSON for new Vitals.
     * This is needed because the JSON will contain a 'patientId',
     * but the VitalRecord model needs a full 'Patient' object.
     */
    private static class VitalRecordApiDto {
        Double valeur;
        String typeMesure;
        Long patientId;
        String dateMesure; // Optional: as ISO string (e.g., "2023-10-27T10:00:00")
    }

    /**
     * Handles GET requests:
     * - GET /api/vitals -> Returns recent vitals
     * - GET /api/vitals?patientId=X -> Returns vitals for a specific patient
     * - GET /api/vitals/1 -> Returns vital with ID 1
     * - GET /api/vitals/stats -> Returns global stats
     * - GET /api/vitals/stats?patientId=X -> Returns stats for a specific patient
     * - GET /api/vitals/anomalies -> Returns all anomalies
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String patientIdParam = req.getParameter("patientId");
        if (patientIdParam == null) {
            patientIdParam = req.getParameter("PatientId");
        }
        try {
            // Case 1: /api/vitals/stats
            if ("/stats".equals(pathInfo)) {
                List<Object[]> stats;
                if (patientIdParam != null) {
                    stats = vitalDao.getStatsByPatientId(Long.parseLong(patientIdParam));
                } else {
                    stats = vitalDao.getStatsByType();
                }
                sendJsonResponse(resp, stats);
                return;
            }

            // Case 2: /api/vitals/anomalies
            if ("/anomalies".equals(pathInfo)) {
                List<VitalRecord> anomalies = vitalDao.findAllAnomalies();
                sendJsonResponse(resp, anomalies);
                return;
            }

            // Case 3: /api/vitals/1 (Get by specific ID)
            Long vitalId = parseId(pathInfo);
            if (vitalId != null) {
                VitalRecord vital = vitalDao.findById(vitalId);
                if (vital != null) {
                    sendJsonResponse(resp, vital);
                } else {
                    sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Vital record not found");
                }
                return;
            }

            // Case 4: /api/vitals?patientId=X (Filter by patient)
            if (patientIdParam != null) {
                List<VitalRecord> vitals = vitalDao.findByPatientId(Long.parseLong(patientIdParam));
                sendJsonResponse(resp, vitals);
                return;
            }

            // Case 5: /api/vitals (Default: recent vitals)
            if (pathInfo == null || "/".equals(pathInfo)) {
                List<VitalRecord> vitals = vitalDao.findRecent(100);
                sendJsonResponse(resp, vitals);
                return;
            }

            // Fallback for any other path
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid API path");

        } catch (NumberFormatException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid ID or parameter format");
        }
    }

    /**
     * Handles POST requests:
     * - POST /api/vitals -> Creates a new vital record from JSON body
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Read the JSON body from the request
            String jsonBody = req.getReader().lines().collect(Collectors.joining());
            VitalRecordApiDto dto = gson.fromJson(jsonBody, VitalRecordApiDto.class);

            // Validate required fields
            if (dto.patientId == null || dto.valeur == null || dto.typeMesure == null) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing required fields: patientId, valeur, typeMesure");
                return;
            }

            // Find the associated Patient
            Patient patient = patientDao.findById(dto.patientId);
            if (patient == null) {
                sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Patient not found with ID: " + dto.patientId);
                return;
            }

            // Determine timestamp
            LocalDateTime timestamp = (dto.dateMesure != null) ? LocalDateTime.parse(dto.dateMesure) : LocalDateTime.now();

            // Create and save the new vital
            VitalRecord newVital = new VitalRecord(timestamp, dto.valeur, dto.typeMesure, patient);
            vitalDao.save(newVital); // 'newVital' object will be updated with the new ID

            resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            sendJsonResponse(resp, newVital);
        } else {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "POST not allowed with ID");
        }
    }

    /**
     * Handles PUT requests:
     * - PUT /api/vitals/1 -> Updates vital with ID 1 from JSON body
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long vitalId = parseId(req.getPathInfo());

        if (vitalId == null) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing vital record ID for update");
            return;
        }

        VitalRecord existingVital = vitalDao.findById(vitalId);
        if (existingVital == null) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Vital record not found");
            return;
        }

        // Read JSON body and update existing vital
        String jsonBody = req.getReader().lines().collect(Collectors.joining());
        VitalRecordApiDto updatedData = gson.fromJson(jsonBody, VitalRecordApiDto.class);

        // Update fields that are provided
        if (updatedData.valeur != null) {
            existingVital.setValeur(updatedData.valeur);
        }
        if (updatedData.typeMesure != null) {
            existingVital.setTypeMesure(updatedData.typeMesure);
        }
        if (updatedData.dateMesure != null) {
            existingVital.setDateMesure(LocalDateTime.parse(updatedData.dateMesure));
        }
        // Note: Changing the patientId is generally not supported in this simple model

        vitalDao.update(existingVital);
        sendJsonResponse(resp, existingVital);
    }

    /**
     * Handles DELETE requests:
     * - DELETE /api/vitals/1 -> Deletes vital with ID 1
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long vitalId = parseId(req.getPathInfo());

        if (vitalId == null) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Missing vital record ID for delete");
            return;
        }

        VitalRecord existingVital = vitalDao.findById(vitalId);
        if (existingVital == null) {
            sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Vital record not found");
            return;
        }

        vitalDao.deleteById(vitalId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 No Content
    }

    // --- Helper Methods ---

    private Long parseId(String pathInfo) {
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                return Long.parseLong(pathInfo.substring(1)); // Remove leading "/"
            } catch (NumberFormatException e) {
                // Not a number, ignore
            }
        }
        return null; // No ID
    }

    private void sendJsonResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print(gson.toJson(data));
        out.flush();
    }

    private void sendError(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.print("{\"error\":\"" + message + "\"}");
        out.flush();
    }
}