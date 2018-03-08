package power.ejb.administration;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJCarwh.
 * 
 * @see power.ejb.administration.AdJCarwh
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJCarwhFacade implements AdJCarwhFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved AdJCarwh entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJCarwh entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJCarwh entity) {
		LogUtil.log("saving AdJCarwh instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent AdJCarwh entity.
	 * 
	 * @param entity
	 *            AdJCarwh entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJCarwh entity) {
		LogUtil.log("deleting AdJCarwh instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJCarwh.class, entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AdJCarwh entity and return it or a copy of it
	 * to the sender. A copy of the AdJCarwh entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJCarwh entity to update
	 * @return AdJCarwh the persisted AdJCarwh entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJCarwh update(AdJCarwh entity) {
		LogUtil.log("updating AdJCarwh instance", Level.INFO, null);
		try {
			AdJCarwh result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AdJCarwh findById(Long id) {
		LogUtil.log("finding AdJCarwh instance with id: " + id, Level.INFO,
				null);
		try {
			AdJCarwh instance = entityManager.find(AdJCarwh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJCarwh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJCarwh property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJCarwh> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJCarwh> findByProperty(String propertyName, final Object value) {
		LogUtil.log("finding AdJCarwh instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJCarwh model where model."
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
	 * Find all AdJCarwh entities.
	 * 
	 * @return List<AdJCarwh> all AdJCarwh entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJCarwh> findAll() {
		LogUtil.log("finding all AdJCarwh instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJCarwh model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}