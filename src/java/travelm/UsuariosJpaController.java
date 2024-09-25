/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package travelm;

import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import travelm.exceptions.NonexistentEntityException;

/**
 *
 * @author jospl
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) {
        if (usuarios.getViajesCollection() == null) {
            usuarios.setViajesCollection(new ArrayList<Viajes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Viajes> attachedViajesCollection = new ArrayList<Viajes>();
            for (Viajes viajesCollectionViajesToAttach : usuarios.getViajesCollection()) {
                viajesCollectionViajesToAttach = em.getReference(viajesCollectionViajesToAttach.getClass(), viajesCollectionViajesToAttach.getId());
                attachedViajesCollection.add(viajesCollectionViajesToAttach);
            }
            usuarios.setViajesCollection(attachedViajesCollection);
            em.persist(usuarios);
            for (Viajes viajesCollectionViajes : usuarios.getViajesCollection()) {
                Usuarios oldUsuarioIdOfViajesCollectionViajes = viajesCollectionViajes.getUsuarioId();
                viajesCollectionViajes.setUsuarioId(usuarios);
                viajesCollectionViajes = em.merge(viajesCollectionViajes);
                if (oldUsuarioIdOfViajesCollectionViajes != null) {
                    oldUsuarioIdOfViajesCollectionViajes.getViajesCollection().remove(viajesCollectionViajes);
                    oldUsuarioIdOfViajesCollectionViajes = em.merge(oldUsuarioIdOfViajesCollectionViajes);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getId());
            Collection<Viajes> viajesCollectionOld = persistentUsuarios.getViajesCollection();
            Collection<Viajes> viajesCollectionNew = usuarios.getViajesCollection();
            Collection<Viajes> attachedViajesCollectionNew = new ArrayList<Viajes>();
            for (Viajes viajesCollectionNewViajesToAttach : viajesCollectionNew) {
                viajesCollectionNewViajesToAttach = em.getReference(viajesCollectionNewViajesToAttach.getClass(), viajesCollectionNewViajesToAttach.getId());
                attachedViajesCollectionNew.add(viajesCollectionNewViajesToAttach);
            }
            viajesCollectionNew = attachedViajesCollectionNew;
            usuarios.setViajesCollection(viajesCollectionNew);
            usuarios = em.merge(usuarios);
            for (Viajes viajesCollectionOldViajes : viajesCollectionOld) {
                if (!viajesCollectionNew.contains(viajesCollectionOldViajes)) {
                    viajesCollectionOldViajes.setUsuarioId(null);
                    viajesCollectionOldViajes = em.merge(viajesCollectionOldViajes);
                }
            }
            for (Viajes viajesCollectionNewViajes : viajesCollectionNew) {
                if (!viajesCollectionOld.contains(viajesCollectionNewViajes)) {
                    Usuarios oldUsuarioIdOfViajesCollectionNewViajes = viajesCollectionNewViajes.getUsuarioId();
                    viajesCollectionNewViajes.setUsuarioId(usuarios);
                    viajesCollectionNewViajes = em.merge(viajesCollectionNewViajes);
                    if (oldUsuarioIdOfViajesCollectionNewViajes != null && !oldUsuarioIdOfViajesCollectionNewViajes.equals(usuarios)) {
                        oldUsuarioIdOfViajesCollectionNewViajes.getViajesCollection().remove(viajesCollectionNewViajes);
                        oldUsuarioIdOfViajesCollectionNewViajes = em.merge(oldUsuarioIdOfViajesCollectionNewViajes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getId();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            Collection<Viajes> viajesCollection = usuarios.getViajesCollection();
            for (Viajes viajesCollectionViajes : viajesCollection) {
                viajesCollectionViajes.setUsuarioId(null);
                viajesCollectionViajes = em.merge(viajesCollectionViajes);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
