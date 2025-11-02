package com.healthdata.web;

import com.healthdata.dao.PatientDao;
import com.healthdata.dao.VitalRecordDao;
import com.healthdata.model.Patient;
import com.healthdata.model.VitalRecord;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/vitals")
public class VitalRecordServlet extends HttpServlet {
    private final PatientDao patientDao = new PatientDao();
    private final VitalRecordDao vitalDao = new VitalRecordDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String patientIdParam = req.getParameter("patientId");
        String mode = req.getParameter("mode");
        List<VitalRecord> vitals = null;
        List<?> stats = null;

        if ("anomalies".equalsIgnoreCase(mode)) {
            vitals = vitalDao.findAllAnomalies();
            req.setAttribute("mode", "anomalies");

        } else if ("stats".equalsIgnoreCase(mode)) {

            if (patientIdParam != null && !patientIdParam.isEmpty()) {
                long patientId = Long.parseLong(patientIdParam);
                stats = vitalDao.getStatsByPatientId(patientId);
                req.setAttribute("selectedPatientId", patientId);
            } else {
                stats = vitalDao.getStatsByType();
            }
            req.setAttribute("mode", "stats");

        } else {
            if (patientIdParam != null && !patientIdParam.isEmpty()) {
                long patientId = Long.parseLong(patientIdParam);
                vitals = vitalDao.findByPatientId(patientId);
                stats = vitalDao.getStatsByPatientId(patientId);
                req.setAttribute("selectedPatientId", patientId);
            } else {
                vitals = vitalDao.findRecent(100);
                stats = vitalDao.getStatsByType();
            }
        }

        // Common data for JSP
        req.setAttribute("patients", patientDao.findAll());
        req.setAttribute("vitals", vitals);
        req.setAttribute("stats", stats);

        req.getRequestDispatcher("/pages/vitals.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Long patientId = Long.parseLong(req.getParameter("patientId"));
        String type = req.getParameter("typeMesure");
        double valeur = Double.parseDouble(req.getParameter("valeur"));

        Patient p = patientDao.findById(patientId);
        if (p != null) {
            VitalRecord v = new VitalRecord(LocalDateTime.now(), valeur, type, p);
            vitalDao.save(v);
        }

        resp.sendRedirect("vitals");
    }
}