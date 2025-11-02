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


    /**
     * Retourne la liste limitée des dernières mesures (ex: 100 dernières)
     * avec les patients associés pour éviter le N+1.
     */
    public List<VitalRecord> findRecent(int limit) {
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
