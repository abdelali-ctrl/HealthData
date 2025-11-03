package com.healthdata.dao;

import com.healthdata.model.VitalRecord;
import jakarta.persistence.EntityManager;
import java.util.List;

public class VitalRecordDao {

    /**
     * Sauvegarde une nouvelle mesure dans la base.
     */
    public void save(VitalRecord v) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(v);
        em.getTransaction().commit();
        em.close();
    }

    // --- ADD THIS NEW METHOD ---
    public VitalRecord findById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        VitalRecord v = null;
        try {
            v = em.createQuery(
                            "SELECT v FROM VitalRecord v " +
                                    "JOIN FETCH v.patient " +
                                    "WHERE v.id = :vid",
                            VitalRecord.class)
                    .setParameter("vid", id)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            // No record found, return null
        } finally {
            em.close();
        }
        return v;
    }

    // --- ADD THIS NEW METHOD ---
    public void update(VitalRecord v) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        em.merge(v);
        em.getTransaction().commit();
        em.close();
    }

    // --- ADD THIS NEW METHOD ---
    public void deleteById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();
        VitalRecord v = em.find(VitalRecord.class, id);
        if (v != null) {
            em.remove(v);
        }
        em.getTransaction().commit();
        em.close();
    }


    /**
     * Retourne la liste limitée des dernières mesures (ex: 100 dernières)
     * avec les patients associés pour éviter le N+1.
     */
    public List<VitalRecord> findRecent(int limit) {
        // ... (existing method, no change)
        EntityManager em = JpaUtil.getEntityManager();
        List<VitalRecord> list = em.createQuery(
                        "SELECT v FROM VitalRecord v " +
                                "JOIN FETCH v.patient " +
                                "ORDER BY v.dateMesure DESC",
                        VitalRecord.class)
                .setMaxResults(limit)
                .getResultList();
        em.close();
        return list;
    }

    /**
     * Retourne les statistiques moyennes / min / max par type de mesure.
     */
    public List<Object[]> getStatsByType() {
        // ... (existing method, no change)
        EntityManager em = JpaUtil.getEntityManager();
        List<Object[]> results = em.createQuery(
                        "SELECT v.typeMesure, AVG(v.valeur), MIN(v.valeur), MAX(v.valeur) " +
                                "FROM VitalRecord v GROUP BY v.typeMesure",
                        Object[].class)
                .getResultList();
        em.close();
        return results;
    }

    /**
     * Retourne toutes les mesures pour un patient spécifique.
     */
    public List<VitalRecord> findByPatientId(Long patientId) {
        // ... (existing method, no change)
        EntityManager em = JpaUtil.getEntityManager();
        List<VitalRecord> list = em.createQuery(
                        "SELECT v FROM VitalRecord v " +
                                "JOIN FETCH v.patient p " +
                                "WHERE p.id = :pid " +
                                "ORDER BY v.dateMesure DESC",
                        VitalRecord.class)
                .setParameter("pid", patientId)
                .setMaxResults(50)
                .getResultList();
        em.close();
        return list;
    }

    /**
     * Retourne les statistiques moyennes / min / max par type de mesure pour un patient spécifique.
     */
    public List<Object[]> getStatsByPatientId(Long patientId) {
        // ... (existing method, no change)
        EntityManager em = JpaUtil.getEntityManager();
        List<Object[]> results = em.createQuery(
                        "SELECT v.typeMesure, AVG(v.valeur), MIN(v.valeur), MAX(v.valeur) " +
                                "FROM VitalRecord v " +
                                "WHERE v.patient.id = :pid " +
                                "GROUP BY v.typeMesure",
                        Object[].class)
                .setParameter("pid", patientId)
                .getResultList();
        em.close();
        return results;
    }

    public List<VitalRecord> findAllAnomalies() {
        // ... (existing method, no change)
        EntityManager em = JpaUtil.getEntityManager();

        // This query combines all anomaly rules using 'OR'.
        // You can easily add or edit rules here.
        String hql = "SELECT v FROM VitalRecord v " +
                "JOIN FETCH v.patient " +
                "WHERE " +
                "(v.typeMesure = 'Temperature' AND (v.valeur < 35.0 OR v.valeur > 39.0)) OR " +
                "(v.typeMesure = 'HeartRate' AND (v.valeur < 60.0 OR v.valeur > 100.0)) " +
                "ORDER BY v.dateMesure DESC";

        List<VitalRecord> list = em.createQuery(hql, VitalRecord.class).getResultList();
        em.close();
        return list;
    }
}