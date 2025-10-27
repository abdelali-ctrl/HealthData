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

}
