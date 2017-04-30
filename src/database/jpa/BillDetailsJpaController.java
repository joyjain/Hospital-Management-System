/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.jpa;

import database.entities.BillDetails;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import database.entities.PatientDetails;
import database.jpa.exceptions.IllegalOrphanException;
import database.jpa.exceptions.NonexistentEntityException;
import database.jpa.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Sanjay Kumar Jain
 */
public class BillDetailsJpaController implements Serializable {

    public BillDetailsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BillDetails billDetails) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        PatientDetails patientIdOrphanCheck = billDetails.getPatientId();
        if (patientIdOrphanCheck != null) {
            BillDetails oldBillDetailsOfPatientId = patientIdOrphanCheck.getBillDetails();
            if (oldBillDetailsOfPatientId != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The PatientDetails " + patientIdOrphanCheck + " already has an item of type BillDetails whose patientId column cannot be null. Please make another selection for the patientId field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PatientDetails patientId = billDetails.getPatientId();
            if (patientId != null) {
                patientId = em.getReference(patientId.getClass(), patientId.getPatientId());
                billDetails.setPatientId(patientId);
            }
            em.persist(billDetails);
            if (patientId != null) {
                patientId.setBillDetails(billDetails);
                patientId = em.merge(patientId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBillDetails(billDetails.getBillNo()) != null) {
                throw new PreexistingEntityException("BillDetails " + billDetails + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BillDetails billDetails) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BillDetails persistentBillDetails = em.find(BillDetails.class, billDetails.getBillNo());
            PatientDetails patientIdOld = persistentBillDetails.getPatientId();
            PatientDetails patientIdNew = billDetails.getPatientId();
            List<String> illegalOrphanMessages = null;
            if (patientIdNew != null && !patientIdNew.equals(patientIdOld)) {
                BillDetails oldBillDetailsOfPatientId = patientIdNew.getBillDetails();
                if (oldBillDetailsOfPatientId != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The PatientDetails " + patientIdNew + " already has an item of type BillDetails whose patientId column cannot be null. Please make another selection for the patientId field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (patientIdNew != null) {
                patientIdNew = em.getReference(patientIdNew.getClass(), patientIdNew.getPatientId());
                billDetails.setPatientId(patientIdNew);
            }
            billDetails = em.merge(billDetails);
            if (patientIdOld != null && !patientIdOld.equals(patientIdNew)) {
                patientIdOld.setBillDetails(null);
                patientIdOld = em.merge(patientIdOld);
            }
            if (patientIdNew != null && !patientIdNew.equals(patientIdOld)) {
                patientIdNew.setBillDetails(billDetails);
                patientIdNew = em.merge(patientIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = billDetails.getBillNo();
                if (findBillDetails(id) == null) {
                    throw new NonexistentEntityException("The billDetails with id " + id + " no longer exists.");
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
            BillDetails billDetails;
            try {
                billDetails = em.getReference(BillDetails.class, id);
                billDetails.getBillNo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The billDetails with id " + id + " no longer exists.", enfe);
            }
            PatientDetails patientId = billDetails.getPatientId();
            if (patientId != null) {
                patientId.setBillDetails(null);
                patientId = em.merge(patientId);
            }
            em.remove(billDetails);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BillDetails> findBillDetailsEntities() {
        return findBillDetailsEntities(true, -1, -1);
    }

    public List<BillDetails> findBillDetailsEntities(int maxResults, int firstResult) {
        return findBillDetailsEntities(false, maxResults, firstResult);
    }

    private List<BillDetails> findBillDetailsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BillDetails.class));
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

    public BillDetails findBillDetails(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BillDetails.class, id);
        } finally {
            em.close();
        }
    }

    public int getBillDetailsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BillDetails> rt = cq.from(BillDetails.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
