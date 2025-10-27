package com.healthdata.web;


import com.healthdata.dao.JpaUtil;
import com.healthdata.model.Patient;
import com.healthdata.model.VitalRecord;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@WebServlet("/generate")
public class GenerateDataServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int n = Integer.parseInt(req.getParameter("n"));
        String nom = req.getParameter("nomPatient");
        int age = Integer.parseInt(req.getParameter("age"));
        String sexe = req.getParameter("sexe");

        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        Patient p = new Patient(nom, age, sexe);
        em.persist(p);

        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            String type = rnd.nextBoolean() ? "HeartRate" : "Temperature";
            double val = type.equals("HeartRate") ? 60 + rnd.nextDouble() * 40 : 35 + rnd.nextDouble() * 3;
            VitalRecord v = new VitalRecord(LocalDateTime.now().minusMinutes(rnd.nextInt(5000)), val, type, p);
            em.persist(v);
            if (i % 50 == 0) { em.flush(); em.clear(); }
        }

        em.getTransaction().commit();
        em.close();
        resp.sendRedirect("vitals");
    }
}
