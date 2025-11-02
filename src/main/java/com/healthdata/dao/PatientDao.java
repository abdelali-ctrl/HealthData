package com.healthdata.dao;

import com.healthdata.model.Patient;
import jakarta.persistence.EntityManager;
import java.util.List;

public class PatientDao {
    public void save(Patient p) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        em.close();
    }

    public List<Patient> findAll() {
        EntityManager em = JpaUtil.getEntityManager();
        List<Patient> list = em.createQuery("SELECT p FROM Patient p", Patient.class).getResultList();
        em.close();
        return list;
    }

    public Patient findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        Patient p = em.find(Patient.class, id);
        em.close();
        return p;
    }
    public void deleteById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();

        // Find the patient first
        Patient p = em.find(Patient.class, id);
        if (p != null) {
            em.remove(p);
        }

        em.getTransaction().commit();
        em.close();
    }
    public void update(Patient p) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(p);
        em.getTransaction().commit();
        em.close();
    }

    public List<Patient> searchByName(String searchTerm) {
        EntityManager em = JpaUtil.getEntityManager();


        String jpql = "SELECT p FROM Patient p WHERE LOWER(p.nom) LIKE LOWER(:term)";

        List<Patient> list = em.createQuery(jpql, Patient.class)
                .setParameter("term", "%" + searchTerm + "%")
                .getResultList();
        em.close();
        return list;
    }

}
