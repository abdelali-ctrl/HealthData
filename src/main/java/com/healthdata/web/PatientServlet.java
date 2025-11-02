package com.healthdata.web;

import com.healthdata.dao.PatientDao;
import com.healthdata.model.Patient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {
    private PatientDao dao = new PatientDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String deleteId = req.getParameter("deleteId");
        String editId = req.getParameter("editId");
        String searchTerm = req.getParameter("search");

        if (deleteId != null) {
            Long patientId = Long.parseLong(deleteId);
            dao.deleteById(patientId);
            resp.sendRedirect("patients");
            return;
        }
        if (editId != null) {
            Long patientId = Long.parseLong(editId);
            Patient patientToEdit = dao.findById(patientId);
            req.setAttribute("patientToEdit", patientToEdit);
        }
        List<Patient> patientsList;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            // If there's a search term, use the search method
            patientsList = dao.searchByName(searchTerm);
        } else {
            // Otherwise, get all patients
            patientsList = dao.findAll();
        }
        req.setAttribute("patients", patientsList);
        req.getRequestDispatcher("/pages/patients.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String patientIdParam = req.getParameter("patientId");

        String nom = req.getParameter("nom");
        int age = Integer.parseInt(req.getParameter("age"));
        String sexe = req.getParameter("sexe");

        if (patientIdParam != null && !patientIdParam.isEmpty()) {
            Long patientId = Long.parseLong(patientIdParam);
            Patient p = dao.findById(patientId);
            if (p != null) {
                p.setNom(nom);
                p.setAge(age);
                p.setSexe(sexe);
                dao.update(p);
            }
        } else {
            dao.save(new Patient(nom, age, sexe));
        }

        resp.sendRedirect("patients");
    }
}

