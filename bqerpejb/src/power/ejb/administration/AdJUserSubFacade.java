package power.ejb.administration;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJUserSub.
 * 
 * @see power.ejb.administration.AdJUserSub
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJUserSubFacade implements AdJUserSubFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved AdJUserSub entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJUserSub entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJUserSub entity) {
		LogUtil.log("saving AdJUserSub instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent AdJUserSub entity.
	 * 
	 * @param entity
	 *            AdJUserSub entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJUserSub entity) {
		LogUtil.log("deleting AdJUserSub instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJUserSub.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AdJUserSub entity and return it or a copy of
	 * it to the sender. A copy of the AdJUserSub entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJUserSub entity to update
	 * @return AdJUserSub the persisted AdJUserSub entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJUserSub update(AdJUserSub entity) {
		LogUtil.log("updating AdJUserSub instance", Level.INFO, null);
		try {
			AdJUserSub result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AdJUserSub findById(Long id) {
		LogUtil.log("finding AdJUserSub instance with id: " + id, Level.INFO,
				null);
		try {
			AdJUserSub instance = entityManager.find(AdJUserSub.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJUserSub entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJUserSub property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJUserSub> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJUserSub> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJUserSub instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJUserSub model where model."
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
	 * Find all AdJUserSub entities.
	 * 
	 * @return List<AdJUserSub> all AdJUserSub entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJUserSub> findAll() {
		LogUtil.log("finding all AdJUserSub instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJUserSub model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}