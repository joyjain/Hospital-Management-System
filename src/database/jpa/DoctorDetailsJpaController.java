/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import database.entities.AccountDetails;
import database.entities.DoctorDetails;
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
public class DoctorDetailsJpaController implements Serializable {

    public DoctorDetailsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DoctorDetails doctorDetails) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        AccountDetails accountDetailsOrphanCheck = doctorDetails.getAccountDetails();
        if (accountDetailsOrphanCheck != null) {
            DoctorDetails oldDoctorDetailsOfAccountDetails = accountDetailsOrphanCheck.getDoctorDetails();
            if (oldDoctorDetailsOfAccountDetails != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The AccountDetails " + accountDetailsOrphanCheck + " already has an item of type DoctorDetails whose accountDetails column cannot be null. Please make another selection for the accountDetails field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccountDetails accountDetails = doctorDetails.getAccountDetails();
            if (accountDetails != null) {
                accountDetails = em.getReference(accountDetails.getClass(), accountDetails.getId());
                doctorDetails.setAccountDetails(accountDetails);
            }
            em.persist(doctorDetails);
            if (accountDetails != null) {
                accountDetails.setDoctorDetails(doctorDetails);
                accountDetails = em.merge(accountDetails);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDoctorDetails(doctorDetails.getDoctorId()) != null) {
                throw new PreexistingEntityException("DoctorDetails " + doctorDetails + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DoctorDetails doctorDetails) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DoctorDetails persistentDoctorDetails = em.find(DoctorDetails.class, doctorDetails.getDoctorId());
            AccountDetails accountDetailsOld = persistentDoctorDetails.getAccountDetails();
            AccountDetails accountDetailsNew = doctorDetails.getAccountDetails();
            List<String> illegalOrphanMessages = null;
            if (accountDetailsNew != null && !accountDetailsNew.equals(accountDetailsOld)) {
                DoctorDetails oldDoctorDetailsOfAccountDetails = accountDetailsNew.getDoctorDetails();
                if (oldDoctorDetailsOfAccountDetails != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The AccountDetails " + accountDetailsNew + " already has an item of type DoctorDetails whose accountDetails column cannot be null. Please make another selection for the accountDetails field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (accountDetailsNew != null) {
                accountDetailsNew = em.getReference(accountDetailsNew.getClass(), accountDetailsNew.getId());
                doctorDetails.setAccountDetails(accountDetailsNew);
            }
            doctorDetails = em.merge(doctorDetails);
            if (accountDetailsOld != null && !accountDetailsOld.equals(accountDetailsNew)) {
                accountDetailsOld.setDoctorDetails(null);
                accountDetailsOld = em.merge(accountDetailsOld);
            }
            if (accountDetailsNew != null && !accountDetailsNew.equals(accountDetailsOld)) {
                accountDetailsNew.setDoctorDetails(doctorDetails);
                accountDetailsNew = em.merge(accountDetailsNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = doctorDetails.getDoctorId();
                if (findDoctorDetails(id) == null) {
                    throw new NonexistentEntityException("The doctorDetails with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DoctorDetails doctorDetails;
            try {
                doctorDetails = em.getReference(DoctorDetails.class, id);
                doctorDetails.getDoctorId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The doctorDetails with id " + id + " no longer exists.", enfe);
            }
            AccountDetails accountDetails = doctorDetails.getAccountDetails();
            if (accountDetails != null) {
                accountDetails.setDoctorDetails(null);
                accountDetails = em.merge(accountDetails);
            }
            em.remove(doctorDetails);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DoctorDetails> findDoctorDetailsEntities() {
        return findDoctorDetailsEntities(true, -1, -1);
    }

    public List<DoctorDetails> findDoctorDetailsEntities(int maxResults, int firstResult) {
        return findDoctorDetailsEntities(false, maxResults, firstResult);
    }

    private List<DoctorDetails> findDoctorDetailsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DoctorDetails.class));
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

    public DoctorDetails findDoctorDetails(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DoctorDetails.class, id);
        } finally {
            em.close();
        }
    }

    public int getDoctorDetailsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DoctorDetails> rt = cq.from(DoctorDetails.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
