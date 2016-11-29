package com.gpxparser.repository;

import com.gpxparser.jpa.GpxTracksHistoryEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * example details can be found there
 * http://blog.netgloo.com/2014/10/06/spring-boot-data-access-with-jpa-hibernate-and-mysql/
 */
@Repository
@Transactional
public class GpxTracksDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Save the gpxTrack into the database.
     */
    public void create(GpxTracksHistoryEntity gpxTrack) {
        entityManager.persist(gpxTrack);
        return;
    }

    /**
     * Delete the gpxTracl from the database.
     */
    public void delete(GpxTracksHistoryEntity gpxTrack) {
        if (entityManager.contains(gpxTrack))
            entityManager.remove(gpxTrack);
        else
            entityManager.remove(entityManager.merge(gpxTrack));
        return;
    }


    public GpxTracksHistoryEntity getByFileName(String fileName) {
        return (GpxTracksHistoryEntity) entityManager.createQuery(
                "from GpxTracksHistoryEntity where fileName = :fileName")
                .setParameter("fileName", fileName)
                .getSingleResult();
    }
    /**
     * Return all the users stored in the database.
     */
    @SuppressWarnings("unchecked")
    public List getAll() {
        return entityManager.createQuery("from GpxTracksHistoryEntity").getResultList();
    }
}
