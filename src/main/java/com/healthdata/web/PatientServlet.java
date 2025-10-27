package com.healthdata.web;

import com.healthdata.dao.PatientDao;
import com.healthdata.model.Patient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/patients")
public class PatientServlet extends HttpServlet {
    private PatientDao dao = new PatientDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String deleteId = req.getParameter("deleteId");
        if (deleteId != null) {
            Long patientId = Long.parseLong(deleteId);
            dao.deleteById(patientId);   // use the new deleteById method
            resp.sendRedirect("patients");
            return;
        }

        req.setAttribute("patients", dao.findAll());
        req.getRequestDispatcher("/pages/patients.jsp").forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nom = req.getParameter("nom");
        int age = Integer.parseInt(req.getParameter("age"));
        String sexe = req.getParameter("sexe");
        dao.save(new Patient(nom, age, sexe));
        resp.sendRedirect("patients");
    }
}

