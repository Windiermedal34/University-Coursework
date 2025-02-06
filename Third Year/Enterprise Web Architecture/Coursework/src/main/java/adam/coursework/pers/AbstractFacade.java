/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package adam.coursework.pers;

import java.util.List;
import javax.persistence.EntityManager;

/**
 * This facade class serves as the basis for the other facade classes to operate
 * @author adamt
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    /**
     * This function will create a new record in a database table
     * @param entity the record being created
     */
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /**
     * This function edits an existing record in a database table
     * @param entity the updated version of the recorded being edited
     * @return returns the edited record
     */
    public T edit(T entity) {
        return getEntityManager().merge(entity);
    }

    /**
     * This function removes a specific record from a database table
     * @param entity this is the record being removed from the database table
     */
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    /**
     * This function will find and retrieve a specific record from a database table
     * @param id this is the primary key of the record being retrieved
     * @return returns a specific record from a database table
     */
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * This function will retrieve every record from a database table
     * @return returns a list of every record from the table
     */
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
}
