/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database.jpa;

import database.entities.AccountDetails;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import database.entities.DoctorDetails;
import database.entities.PatientDetails;
import database.jpa.exceptions.IllegalOrphanException;
import database.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * @author Sanjay Kumar Jain
 */
public class AccountDetailsJpaController implements Serializable {

    public AccountDetailsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AccountDetails accountDetails) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DoctorDetails doctorDetails = accountDetails.getDoctorDetails();
            if (doctorDetails != null) {
                doctorDetails = em.getReference(doctorDetails.getClass(), doctorDetails.getDoctorId());
                accountDetails.setDoctorDetails(doctorDetails);
            }
            PatientDetails patientDetails = accountDetails.getPatientDetails();
            if (patientDetails != null) {
                patientDetails = em.getReference(patientDetails.getClass(), patientDetails.getPatientId());
                accountDetails.setPatientDetails(patientDetails);
            }
            em.persist(accountDetails);
            if (doctorDetails != null) {
                AccountDetails oldAccountDetailsOfDoctorDetails = doctorDetails.getAccountDetails();
                if (oldAccountDetailsOfDoctorDetails != null) {
                    oldAccountDetailsOfDoctorDetails.setDoctorDetails(null);
                    oldAccountDetailsOfDoctorDetails = em.merge(oldAccountDetailsOfDoctorDetails);
                }
                doctorDetails.setAccountDetails(accountDetails);
                doctorDetails = em.merge(doctorDetails);
            }
            if (patientDetails != null) {
                AccountDetails oldAccountDetailsOfPatientDetails = patientDetails.getAccountDetails();
                if (oldAccountDetailsOfPatientDetails != null) {
                    oldAccountDetailsOfPatientDetails.setPatientDetails(null);
                    oldAccountDetailsOfPatientDetails = em.merge(oldAccountDetailsOfPatientDetails);
                }
                patientDetails.setAccountDetails(accountDetails);
                patientDetails = em.merge(patientDetails);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AccountDetails accountDetails) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccountDetails persistentAccountDetails = em.find(AccountDetails.class, accountDetails.getId());
            DoctorDetails doctorDetailsOld = persistentAccountDetails.getDoctorDetails();
            DoctorDetails doctorDetailsNew = accountDetails.getDoctorDetails();
            PatientDetails patientDetailsOld = persistentAccountDetails.getPatientDetails();
            PatientDetails patientDetailsNew = accountDetails.getPatientDetails();
            List<String> illegalOrphanMessages = null;
            if (doctorDetailsOld != null && !doctorDetailsOld.equals(doctorDetailsNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain DoctorDetails " + doctorDetailsOld + " since its accountDetails field is not nullable.");
            }
            if (patientDetailsOld != null && !patientDetailsOld.equals(patientDetailsNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain PatientDetails " + patientDetailsOld + " since its accountDetails field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (doctorDetailsNew != null) {
                doctorDetailsNew = em.getReference(doctorDetailsNew.getClass(), doctorDetailsNew.getDoctorId());
                accountDetails.setDoctorDetails(doctorDetailsNew);
            }
            if (patientDetailsNew != null) {
                patientDetailsNew = em.getReference(patientDetailsNew.getClass(), patientDetailsNew.getPatientId());
                accountDetails.setPatientDetails(patientDetailsNew);
            }
            accountDetails = em.merge(accountDetails);
            if (doctorDetailsNew != null && !doctorDetailsNew.equals(doctorDetailsOld)) {
                AccountDetails oldAccountDetailsOfDoctorDetails = doctorDetailsNew.getAccountDetails();
                if (oldAccountDetailsOfDoctorDetails != null) {
                    oldAccountDetailsOfDoctorDetails.setDoctorDetails(null);
                    oldAccountDetailsOfDoctorDetails = em.merge(oldAccountDetailsOfDoctorDetails);
                }
                doctorDetailsNew.setAccountDetails(accountDetails);
                doctorDetailsNew = em.merge(doctorDetailsNew);
            }
            if (patientDetailsNew != null && !patientDetailsNew.equals(patientDetailsOld)) {
                AccountDetails oldAccountDetailsOfPatientDetails = patientDetailsNew.getAccountDetails();
                if (oldAccountDetailsOfPatientDetails != null) {
                    oldAccountDetailsOfPatientDetails.setPatientDetails(null);
                    oldAccountDetailsOfPatientDetails = em.merge(oldAccountDetailsOfPatientDetails);
                }
                patientDetailsNew.setAccountDetails(accountDetails);
                patientDetailsNew = em.merge(patientDetailsNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = accountDetails.getId();
                if (findAccountDetails(id) == null) {
                    throw new NonexistentEntityException("The accountDetails with id " + id + " no longer exists.");
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
            AccountDetails accountDetails;
            try {
                accountDetails = em.getReference(AccountDetails.class, id);
                accountDetails.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The accountDetails with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            DoctorDetails doctorDetailsOrphanCheck = accountDetails.getDoctorDetails();
            if (doctorDetailsOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AccountDetails (" + accountDetails + ") cannot be destroyed since the DoctorDetails " + doctorDetailsOrphanCheck + " in its doctorDetails field has a non-nullable accountDetails field.");
            }
            PatientDetails patientDetailsOrphanCheck = accountDetails.getPatientDetails();
            if (patientDetailsOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AccountDetails (" + accountDetails + ") cannot be destroyed since the PatientDetails " + patientDetailsOrphanCheck + " in its patientDetails field has a non-nullable accountDetails field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(accountDetails);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AccountDetails> findAccountDetailsEntities() {
        return findAccountDetailsEntities(true, -1, -1);
    }

    public List<AccountDetails> findAccountDetailsEntities(int maxResults, int firstResult) {
        return findAccountDetailsEntities(false, maxResults, firstResult);
    }

    private List<AccountDetails> findAccountDetailsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AccountDetails.class));
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

    public AccountDetails findAccountDetails(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AccountDetails.class, id);
        } finally {
            em.close();
        }
    }
    
    public AccountDetails findAccountDetails(String username) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<AccountDetails> query = em.createNamedQuery("AccountDetails.findByUsername", AccountDetails.class);
            return query.setParameter("username", username).getSingleResult();
        }
        catch(Exception e) {
        	return new AccountDetails();
        }
        finally {
            em.close();
        }
    }

    public int getAccountDetailsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AccountDetails> rt = cq.from(AccountDetails.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
