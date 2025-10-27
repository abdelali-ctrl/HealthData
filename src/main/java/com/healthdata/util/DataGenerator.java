package com.healthdata.util;

import com.healthdata.dao.JpaUtil;
import com.healthdata.model.Patient;
import com.healthdata.model.VitalRecord;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Generates a large dataset of patients and their vital records
 * to test Hibernate batch insertion and query performance.
 */
public class DataGenerator {

    public static void main(String[] args) {
        EntityManager em = JpaUtil.getEntityManager();
        em.getTransaction().begin();

        long t0 = System.currentTimeMillis();

        Random rnd = new Random();

        // Generate 10 patients, each with 1000 vitals (10 * 1000 = 10,000 records)
        for (int p = 1; p <= 10; p++) {
            Patient patient = new Patient("Patient_" + p, 20 + rnd.nextInt(50), (p % 2 == 0) ? "H" : "F");
            em.persist(patient);

            for (int i = 0; i < 1000; i++) {
                String type = (i % 2 == 0) ? "HeartRate" : "Temperature";
                double value = type.equals("HeartRate")
                        ? 60 + rnd.nextDouble() * 40   // heart rate between 60–100
                        : 35 + rnd.nextDouble() * 3;   // temperature between 35–38 °C

                VitalRecord record = new VitalRecord(
                        LocalDateTime.now().minusMinutes(rnd.nextInt(5000)),
                        value,
                        type,
                        patient
                );
                em.persist(record);

                // Flush and clear every 50 insertions to avoid memory overload
                if (i % 50 == 0) {
                    em.flush();
                    em.clear();
                }
            }
        }

        em.getTransaction().commit();
        em.close();

        long t1 = System.currentTimeMillis();
        System.out.println("✅ 10,000 vital records inserted in (ms): " + (t1 - t0));
    }
}
