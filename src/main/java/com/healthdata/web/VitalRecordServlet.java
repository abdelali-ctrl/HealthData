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

@WebServlet("/vitals")
public class VitalRecordServlet extends HttpServlet {
    private final PatientDao patientDao = new PatientDao();
    private final VitalRecordDao vitalDao = new VitalRecordDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Afficher les 100 dernières mesures
        req.setAttribute("vitals", vitalDao.findRecent(100));

        // Afficher les stats
        req.setAttribute("stats", vitalDao.getStatsByType());

        // Liste des patients pour le formulaire
        req.setAttribute("patients", patientDao.findAll());

        // Envoi vers la JSP
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
