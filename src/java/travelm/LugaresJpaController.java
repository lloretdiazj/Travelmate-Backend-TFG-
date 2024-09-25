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
public class LugaresJpaController implements Serializable {

    public LugaresJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Lugares lugares) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viajes viajeId = lugares.getViajeId();
            if (viajeId != null) {
                viajeId = em.getReference(viajeId.getClass(), viajeId.getId());
                lugares.setViajeId(viajeId);
            }
            em.persist(lugares);
            if (viajeId != null) {
                viajeId.getLugaresCollection().add(lugares);
                viajeId = em.merge(viajeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Lugares lugares) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Lugares persistentLugares = em.find(Lugares.class, lugares.getId());
            Viajes viajeIdOld = persistentLugares.getViajeId();
            Viajes viajeIdNew = lugares.getViajeId();
            if (viajeIdNew != null) {
                viajeIdNew = em.getReference(viajeIdNew.getClass(), viajeIdNew.getId());
                lugares.setViajeId(viajeIdNew);
            }
            lugares = em.merge(lugares);
            if (viajeIdOld != null && !viajeIdOld.equals(viajeIdNew)) {
                viajeIdOld.getLugaresCollection().remove(lugares);
                viajeIdOld = em.merge(viajeIdOld);
            }
            if (viajeIdNew != null && !viajeIdNew.equals(viajeIdOld)) {
                viajeIdNew.getLugaresCollection().add(lugares);
                viajeIdNew = em.merge(viajeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = lugares.getId();
                if (findLugares(id) == null) {
                    throw new NonexistentEntityException("The lugares with id " + id + " no longer exists.");
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
            Lugares lugares;
            try {
                lugares = em.getReference(Lugares.class, id);
                lugares.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lugares with id " + id + " no longer exists.", enfe);
            }
            Viajes viajeId = lugares.getViajeId();
            if (viajeId != null) {
                viajeId.getLugaresCollection().remove(lugares);
                viajeId = em.merge(viajeId);
            }
            em.remove(lugares);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Lugares> findLugaresEntities() {
        return findLugaresEntities(true, -1, -1);
    }

    public List<Lugares> findLugaresEntities(int maxResults, int firstResult) {
        return findLugaresEntities(false, maxResults, firstResult);
    }

    private List<Lugares> findLugaresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Lugares.class));
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

    public Lugares findLugares(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Lugares.class, id);
        } finally {
            em.close();
        }
    }

    public int getLugaresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Lugares> rt = cq.from(Lugares.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
