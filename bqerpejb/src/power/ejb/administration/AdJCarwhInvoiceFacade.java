package power.ejb.administration;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJCarwhInvoice.
 * 
 * @see power.ejb.administration.AdJCarwhInvoice
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJCarwhInvoiceFacade implements AdJCarwhInvoiceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved AdJCarwhInvoice entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJCarwhInvoice entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJCarwhInvoice entity) {
		LogUtil.log("saving AdJCarwhInvoice instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent AdJCarwhInvoice entity.
	 * 
	 * @param entity
	 *            AdJCarwhInvoice entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJCarwhInvoice entity) {
		LogUtil.log("deleting AdJCarwhInvoice instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJCarwhInvoice.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AdJCarwhInvoice entity and return it or a copy
	 * of it to the sender. A copy of the AdJCarwhInvoice entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            AdJCarwhInvoice entity to update
	 * @return AdJCarwhInvoice the persisted AdJCarwhInvoice entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJCarwhInvoice update(AdJCarwhInvoice entity) {
		LogUtil.log("updating AdJCarwhInvoice instance", Level.INFO, null);
		try {
			AdJCarwhInvoice result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AdJCarwhInvoice findById(Long id) {
		LogUtil.log("finding AdJCarwhInvoice instance with id: " + id,
				Level.INFO, null);
		try {
			AdJCarwhInvoice instance = entityManager.find(
					AdJCarwhInvoice.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJCarwhInvoice entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJCarwhInvoice property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJCarwhInvoice> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJCarwhInvoice> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJCarwhInvoice instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJCarwhInvoice model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJCarwhInvoice entities.
	 * 
	 * @return List<AdJCarwhInvoice> all AdJCarwhInvoice entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJCarwhInvoice> findAll() {
		LogUtil.log("finding all AdJCarwhInvoice instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJCarwhInvoice model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}