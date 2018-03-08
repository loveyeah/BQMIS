package power.ejb.administration;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJCarwhList.
 * 
 * @see power.ejb.administration.AdJCarwhList
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJCarwhListFacade implements AdJCarwhListFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved AdJCarwhList entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJCarwhList entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJCarwhList entity) {
		LogUtil.log("saving AdJCarwhList instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent AdJCarwhList entity.
	 * 
	 * @param entity
	 *            AdJCarwhList entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJCarwhList entity) {
		LogUtil.log("deleting AdJCarwhList instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJCarwhList.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AdJCarwhList entity and return it or a copy of
	 * it to the sender. A copy of the AdJCarwhList entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJCarwhList entity to update
	 * @return AdJCarwhList the persisted AdJCarwhList entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJCarwhList update(AdJCarwhList entity) {
		LogUtil.log("updating AdJCarwhList instance", Level.INFO, null);
		try {
			AdJCarwhList result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AdJCarwhList findById(Long id) {
		LogUtil.log("finding AdJCarwhList instance with id: " + id, Level.INFO,
				null);
		try {
			AdJCarwhList instance = entityManager.find(AdJCarwhList.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJCarwhList entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJCarwhList property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJCarwhList> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJCarwhList> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJCarwhList instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJCarwhList model where model."
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
	 * Find all AdJCarwhList entities.
	 * 
	 * @return List<AdJCarwhList> all AdJCarwhList entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJCarwhList> findAll() {
		LogUtil.log("finding all AdJCarwhList instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJCarwhList model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}