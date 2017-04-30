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
import database.entities.BillDetails;
import database.entities.AccountDetails;
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
public class PatientDetailsJpaController implements Serializable {

    public PatientDetailsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PatientDetails patientDetails) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        AccountDetails accountDetailsOrphanCheck = patientDetails.getAccountDetails();
        if (accountDetailsOrphanCheck != null) {
            PatientDetails oldPatientDetailsOfAccountDetails = accountDetailsOrphanCheck.getPatientDetails();
            if (oldPatientDetailsOfAccountDetails != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The AccountDetails " + accountDetailsOrphanCheck + " already has an item of type PatientDetails whose accountDetails column cannot be null. Please make another selection for the accountDetails field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BillDetails billDetails = patientDetails.getBillDetails();
            if (billDetails != null) {
                billDetails = em.getReference(billDetails.getClass(), billDetails.getBillNo());
                patientDetails.setBillDetails(billDetails);
            }
            AccountDetails accountDetails = patientDetails.getAccountDetails();
            if (accountDetails != null) {
                accountDetails = em.getReference(accountDetails.getClass(), accountDetails.getId());
                patientDetails.setAccountDetails(accountDetails);
            }
            em.persist(patientDetails);
            if (billDetails != null) {
                PatientDetails oldPatientIdOfBillDetails = billDetails.getPatientId();
                if (oldPatientIdOfBillDetails != null) {
                    oldPatientIdOfBillDetails.setBillDetails(null);
                    oldPatientIdOfBillDetails = em.merge(oldPatientIdOfBillDetails);
                }
                billDetails.setPatientId(patientDetails);
                billDetails = em.merge(billDetails);
            }
            if (accountDetails != null) {
                accountDetails.setPatientDetails(patientDetails);
                accountDetails = em.merge(accountDetails);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPatientDetails(patientDetails.getPatientId()) != null) {
                throw new PreexistingEntityException("PatientDetails " + patientDetails + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PatientDetails patientDetails) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PatientDetails persistentPatientDetails = em.find(PatientDetails.class, patientDetails.getPatientId());
            BillDetails billDetailsOld = persistentPatientDetails.getBillDetails();
            BillDetails billDetailsNew = patientDetails.getBillDetails();
            AccountDetails accountDetailsOld = persistentPatientDetails.getAccountDetails();
            AccountDetails accountDetailsNew = patientDetails.getAccountDetails();
            List<String> illegalOrphanMessages = null;
            if (billDetailsOld != null && !billDetailsOld.equals(billDetailsNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain BillDetails " + billDetailsOld + " since its patientId field is not nullable.");
            }
            if (accountDetailsNew != null && !accountDetailsNew.equals(accountDetailsOld)) {
                PatientDetails oldPatientDetailsOfAccountDetails = accountDetailsNew.getPatientDetails();
                if (oldPatientDetailsOfAccountDetails != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The AccountDetails " + accountDetailsNew + " already has an item of type PatientDetails whose accountDetails column cannot be null. Please make another selection for the accountDetails field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (billDetailsNew != null) {
                billDetailsNew = em.getReference(billDetailsNew.getClass(), billDetailsNew.getBillNo());
                patientDetails.setBillDetails(billDetailsNew);
            }
            if (accountDetailsNew != null) {
                accountDetailsNew = em.getReference(accountDetailsNew.getClass(), accountDetailsNew.getId());
                patientDetails.setAccountDetails(accountDetailsNew);
            }
            patientDetails = em.merge(patientDetails);
            if (billDetailsNew != null && !billDetailsNew.equals(billDetailsOld)) {
                PatientDetails oldPatientIdOfBillDetails = billDetailsNew.getPatientId();
                if (oldPatientIdOfBillDetails != null) {
                    oldPatientIdOfBillDetails.setBillDetails(null);
                    oldPatientIdOfBillDetails = em.merge(oldPatientIdOfBillDetails);
                }
                billDetailsNew.setPatientId(patientDetails);
                billDetailsNew = em.merge(billDetailsNew);
            }
            if (accountDetailsOld != null && !accountDetailsOld.equals(accountDetailsNew)) {
                accountDetailsOld.setPatientDetails(null);
                accountDetailsOld = em.merge(accountDetailsOld);
            }
            if (accountDetailsNew != null && !accountDetailsNew.equals(accountDetailsOld)) {
                accountDetailsNew.setPatientDetails(patientDetails);
                accountDetailsNew = em.merge(accountDetailsNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = patientDetails.getPatientId();
                if (findPatientDetails(id) == null) {
                    throw new NonexistentEntityException("The patientDetails with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PatientDetails patientDetails;
            try {
                patientDetails = em.getReference(PatientDetails.class, id);
                patientDetails.getPatientId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The patientDetails with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            BillDetails billDetailsOrphanCheck = patientDetails.getBillDetails();
            if (billDetailsOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PatientDetails (" + patientDetails + ") cannot be destroyed since the BillDetails " + billDetailsOrphanCheck + " in its billDetails field has a non-nullable patientId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            AccountDetails accountDetails = patientDetails.getAccountDetails();
            if (accountDetails != null) {
                accountDetails.setPatientDetails(null);
                accountDetails = em.merge(accountDetails);
            }
            em.remove(patientDetails);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PatientDetails> findPatientDetailsEntities() {
        return findPatientDetailsEntities(true, -1, -1);
    }

    public List<PatientDetails> findPatientDetailsEntities(int maxResults, int firstResult) {
        return findPatientDetailsEntities(false, maxResults, firstResult);
    }

    private List<PatientDetails> findPatientDetailsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PatientDetails.class));
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

    public PatientDetails findPatientDetails(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PatientDetails.class, id);
        } finally {
            em.close();
        }
    }

    public int getPatientDetailsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PatientDetails> rt = cq.from(PatientDetails.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
