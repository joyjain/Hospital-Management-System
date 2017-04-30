/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.jpa;

import database.entities.RoomDetails;
import database.jpa.exceptions.NonexistentEntityException;
import database.jpa.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Sanjay Kumar Jain
 */
public class RoomDetailsJpaController implements Serializable {

    public RoomDetailsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RoomDetails roomDetails) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(roomDetails);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRoomDetails(roomDetails.getRoomNo()) != null) {
                throw new PreexistingEntityException("RoomDetails " + roomDetails + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RoomDetails roomDetails) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            roomDetails = em.merge(roomDetails);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = roomDetails.getRoomNo();
                if (findRoomDetails(id) == null) {
                    throw new NonexistentEntityException("The roomDetails with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RoomDetails roomDetails;
            try {
                roomDetails = em.getReference(RoomDetails.class, id);
                roomDetails.getRoomNo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The roomDetails with id " + id + " no longer exists.", enfe);
            }
            em.remove(roomDetails);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RoomDetails> findRoomDetailsEntities() {
        return findRoomDetailsEntities(true, -1, -1);
    }

    public List<RoomDetails> findRoomDetailsEntities(int maxResults, int firstResult) {
        return findRoomDetailsEntities(false, maxResults, firstResult);
    }

    private List<RoomDetails> findRoomDetailsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RoomDetails.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public RoomDetails findRoomDetails(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RoomDetails.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoomDetailsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RoomDetails> rt = cq.from(RoomDetails.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
