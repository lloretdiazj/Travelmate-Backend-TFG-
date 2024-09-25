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
import travelm.exceptions.IllegalOrphanException;
import travelm.exceptions.NonexistentEntityException;

/**
 *
 * @author jospl
 */
public class ViajesJpaController implements Serializable {

    public ViajesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Viajes viajes) {
        if (viajes.getLugaresCollection() == null) {
            viajes.setLugaresCollection(new ArrayList<Lugares>());
        }
        if (viajes.getGastosCollection() == null) {
            viajes.setGastosCollection(new ArrayList<Gastos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarioId = viajes.getUsuarioId();
            if (usuarioId != null) {
                usuarioId = em.getReference(usuarioId.getClass(), usuarioId.getId());
                viajes.setUsuarioId(usuarioId);
            }
            Collection<Lugares> attachedLugaresCollection = new ArrayList<Lugares>();
            for (Lugares lugaresCollectionLugaresToAttach : viajes.getLugaresCollection()) {
                lugaresCollectionLugaresToAttach = em.getReference(lugaresCollectionLugaresToAttach.getClass(), lugaresCollectionLugaresToAttach.getId());
                attachedLugaresCollection.add(lugaresCollectionLugaresToAttach);
            }
            viajes.setLugaresCollection(attachedLugaresCollection);
            Collection<Gastos> attachedGastosCollection = new ArrayList<Gastos>();
            for (Gastos gastosCollectionGastosToAttach : viajes.getGastosCollection()) {
                gastosCollectionGastosToAttach = em.getReference(gastosCollectionGastosToAttach.getClass(), gastosCollectionGastosToAttach.getId());
                attachedGastosCollection.add(gastosCollectionGastosToAttach);
            }
            viajes.setGastosCollection(attachedGastosCollection);
            em.persist(viajes);
            if (usuarioId != null) {
                usuarioId.getViajesCollection().add(viajes);
                usuarioId = em.merge(usuarioId);
            }
            for (Lugares lugaresCollectionLugares : viajes.getLugaresCollection()) {
                Viajes oldViajeIdOfLugaresCollectionLugares = lugaresCollectionLugares.getViajeId();
                lugaresCollectionLugares.setViajeId(viajes);
                lugaresCollectionLugares = em.merge(lugaresCollectionLugares);
                if (oldViajeIdOfLugaresCollectionLugares != null) {
                    oldViajeIdOfLugaresCollectionLugares.getLugaresCollection().remove(lugaresCollectionLugares);
                    oldViajeIdOfLugaresCollectionLugares = em.merge(oldViajeIdOfLugaresCollectionLugares);
                }
            }
            for (Gastos gastosCollectionGastos : viajes.getGastosCollection()) {
                Viajes oldViajeIdOfGastosCollectionGastos = gastosCollectionGastos.getViajeId();
                gastosCollectionGastos.setViajeId(viajes);
                gastosCollectionGastos = em.merge(gastosCollectionGastos);
                if (oldViajeIdOfGastosCollectionGastos != null) {
                    oldViajeIdOfGastosCollectionGastos.getGastosCollection().remove(gastosCollectionGastos);
                    oldViajeIdOfGastosCollectionGastos = em.merge(oldViajeIdOfGastosCollectionGastos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Viajes viajes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Viajes persistentViajes = em.find(Viajes.class, viajes.getId());
            Usuarios usuarioIdOld = persistentViajes.getUsuarioId();
            Usuarios usuarioIdNew = viajes.getUsuarioId();
            Collection<Lugares> lugaresCollectionOld = persistentViajes.getLugaresCollection();
            Collection<Lugares> lugaresCollectionNew = viajes.getLugaresCollection();
            Collection<Gastos> gastosCollectionOld = persistentViajes.getGastosCollection();
            Collection<Gastos> gastosCollectionNew = viajes.getGastosCollection();
            List<String> illegalOrphanMessages = null;
            for (Gastos gastosCollectionOldGastos : gastosCollectionOld) {
                if (!gastosCollectionNew.contains(gastosCollectionOldGastos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Gastos " + gastosCollectionOldGastos + " since its viajeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuarioIdNew != null) {
                usuarioIdNew = em.getReference(usuarioIdNew.getClass(), usuarioIdNew.getId());
                viajes.setUsuarioId(usuarioIdNew);
            }
            Collection<Lugares> attachedLugaresCollectionNew = new ArrayList<Lugares>();
            for (Lugares lugaresCollectionNewLugaresToAttach : lugaresCollectionNew) {
                lugaresCollectionNewLugaresToAttach = em.getReference(lugaresCollectionNewLugaresToAttach.getClass(), lugaresCollectionNewLugaresToAttach.getId());
                attachedLugaresCollectionNew.add(lugaresCollectionNewLugaresToAttach);
            }
            lugaresCollectionNew = attachedLugaresCollectionNew;
            viajes.setLugaresCollection(lugaresCollectionNew);
            Collection<Gastos> attachedGastosCollectionNew = new ArrayList<Gastos>();
            for (Gastos gastosCollectionNewGastosToAttach : gastosCollectionNew) {
                gastosCollectionNewGastosToAttach = em.getReference(gastosCollectionNewGastosToAttach.getClass(), gastosCollectionNewGastosToAttach.getId());
                attachedGastosCollectionNew.add(gastosCollectionNewGastosToAttach);
            }
            gastosCollectionNew = attachedGastosCollectionNew;
            viajes.setGastosCollection(gastosCollectionNew);
            viajes = em.merge(viajes);
            if (usuarioIdOld != null && !usuarioIdOld.equals(usuarioIdNew)) {
                usuarioIdOld.getViajesCollection().remove(viajes);
                usuarioIdOld = em.merge(usuarioIdOld);
            }
            if (usuarioIdNew != null && !usuarioIdNew.equals(usuarioIdOld)) {
                usuarioIdNew.getViajesCollection().add(viajes);
                usuarioIdNew = em.merge(usuarioIdNew);
            }
            for (Lugares lugaresCollectionOldLugares : lugaresCollectionOld) {
                if (!lugaresCollectionNew.contains(lugaresCollectionOldLugares)) {
                    lugaresCollectionOldLugares.setViajeId(null);
                    lugaresCollectionOldLugares = em.merge(lugaresCollectionOldLugares);
                }
            }
            for (Lugares lugaresCollectionNewLugares : lugaresCollectionNew) {
                if (!lugaresCollectionOld.contains(lugaresCollectionNewLugares)) {
                    Viajes oldViajeIdOfLugaresCollectionNewLugares = lugaresCollectionNewLugares.getViajeId();
                    lugaresCollectionNewLugares.setViajeId(viajes);
                    lugaresCollectionNewLugares = em.merge(lugaresCollectionNewLugares);
                    if (oldViajeIdOfLugaresCollectionNewLugares != null && !oldViajeIdOfLugaresCollectionNewLugares.equals(viajes)) {
                        oldViajeIdOfLugaresCollectionNewLugares.getLugaresCollection().remove(lugaresCollectionNewLugares);
                        oldViajeIdOfLugaresCollectionNewLugares = em.merge(oldViajeIdOfLugaresCollectionNewLugares);
                    }
                }
            }
            for (Gastos gastosCollectionNewGastos : gastosCollectionNew) {
                if (!gastosCollectionOld.contains(gastosCollectionNewGastos)) {
                    Viajes oldViajeIdOfGastosCollectionNewGastos = gastosCollectionNewGastos.getViajeId();
                    gastosCollectionNewGastos.setViajeId(viajes);
                    gastosCollectionNewGastos = em.merge(gastosCollectionNewGastos);
                    if (oldViajeIdOfGastosCollectionNewGastos != null && !oldViajeIdOfGastosCollectionNewGastos.equals(viajes)) {
                        oldViajeIdOfGastosCollectionNewGastos.getGastosCollection().remove(gastosCollectionNewGastos);
                        oldViajeIdOfGastosCollectionNewGastos = em.merge(oldViajeIdOfGastosCollectionNewGastos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = viajes.getId();
                if (findViajes(id) == null) {
                    throw new NonexistentEntityException("The viajes with id " + id + " no longer exists.");
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
            Viajes viajes;
            try {
                viajes = em.getReference(Viajes.class, id);
                viajes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The viajes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Gastos> gastosCollectionOrphanCheck = viajes.getGastosCollection();
            for (Gastos gastosCollectionOrphanCheckGastos : gastosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Viajes (" + viajes + ") cannot be destroyed since the Gastos " + gastosCollectionOrphanCheckGastos + " in its gastosCollection field has a non-nullable viajeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuarios usuarioId = viajes.getUsuarioId();
            if (usuarioId != null) {
                usuarioId.getViajesCollection().remove(viajes);
                usuarioId = em.merge(usuarioId);
            }
            Collection<Lugares> lugaresCollection = viajes.getLugaresCollection();
            for (Lugares lugaresCollectionLugares : lugaresCollection) {
                lugaresCollectionLugares.setViajeId(null);
                lugaresCollectionLugares = em.merge(lugaresCollectionLugares);
            }
            em.remove(viajes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Viajes> findViajesEntities() {
        return findViajesEntities(true, -1, -1);
    }

    public List<Viajes> findViajesEntities(int maxResults, int firstResult) {
        return findViajesEntities(false, maxResults, firstResult);
    }

    private List<Viajes> findViajesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Viajes.class));
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

    public Viajes findViajes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Viajes.class, id);
        } finally {
            em.close();
        }
    }

    public int getViajesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Viajes> rt = cq.from(Viajes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
