/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package travelm;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import travelm.exceptions.NonexistentEntityException;

/**
 *
 * @author jospl
 */
public class GastosJpaController implements Serializable {

    public GastosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Gastos gastos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viajes viajeId = gastos.getViajeId();
            if (viajeId != null) {
                viajeId = em.getReference(viajeId.getClass(), viajeId.getId());
                gastos.setViajeId(viajeId);
            }
            em.persist(gastos);
            if (viajeId != null) {
                viajeId.getGastosCollection().add(gastos);
                viajeId = em.merge(viajeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Gastos gastos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Gastos persistentGastos = em.find(Gastos.class, gastos.getId());
            Viajes viajeIdOld = persistentGastos.getViajeId();
            Viajes viajeIdNew = gastos.getViajeId();
            if (viajeIdNew != null) {
                viajeIdNew = em.getReference(viajeIdNew.getClass(), viajeIdNew.getId());
                gastos.setViajeId(viajeIdNew);
            }
            gastos = em.merge(gastos);
            if (viajeIdOld != null && !viajeIdOld.equals(viajeIdNew)) {
                viajeIdOld.getGastosCollection().remove(gastos);
                viajeIdOld = em.merge(viajeIdOld);
            }
            if (viajeIdNew != null && !viajeIdNew.equals(viajeIdOld)) {
                viajeIdNew.getGastosCollection().add(gastos);
                viajeIdNew = em.merge(viajeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = gastos.getId();
                if (findGastos(id) == null) {
                    throw new NonexistentEntityException("The gastos with id " + id + " no longer exists.");
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
            Gastos gastos;
            try {
                gastos = em.getReference(Gastos.class, id);
                gastos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gastos with id " + id + " no longer exists.", enfe);
            }
            Viajes viajeId = gastos.getViajeId();
            if (viajeId != null) {
                viajeId.getGastosCollection().remove(gastos);
                viajeId = em.merge(viajeId);
            }
            em.remove(gastos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Gastos> findGastosEntities() {
        return findGastosEntities(true, -1, -1);
    }

    public List<Gastos> findGastosEntities(int maxResults, int firstResult) {
        return findGastosEntities(false, maxResults, firstResult);
    }

    private List<Gastos> findGastosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Gastos.class));
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

    public Gastos findGastos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Gastos.class, id);
        } finally {
            em.close();
        }
    }

    public int getGastosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Gastos> rt = cq.from(Gastos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
