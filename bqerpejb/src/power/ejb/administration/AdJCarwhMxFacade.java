package power.ejb.administration;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJCarwhMx.
 * 
 * @see power.ejb.administration.AdJCarwhMx
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJCarwhMxFacade implements AdJCarwhMxFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved AdJCarwhMx entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJCarwhMx entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJCarwhMx entity) {
		LogUtil.log("saving AdJCarwhMx instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent AdJCarwhMx entity.
	 * 
	 * @param entity
	 *            AdJCarwhMx entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJCarwhMx entity) {
		LogUtil.log("deleting AdJCarwhMx instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJCarwhMx.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AdJCarwhMx entity and return it or a copy of
	 * it to the sender. A copy of the AdJCarwhMx entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJCarwhMx entity to update
	 * @return AdJCarwhMx the persisted AdJCarwhMx entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJCarwhMx update(AdJCarwhMx entity) {
		LogUtil.log("updating AdJCarwhMx instance", Level.INFO, null);
		try {
			AdJCarwhMx result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AdJCarwhMx findById(Long id) {
		LogUtil.log("finding AdJCarwhMx instance with id: " + id, Level.INFO,
				null);
		try {
			AdJCarwhMx instance = entityManager.find(AdJCarwhMx.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJCarwhMx entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJCarwhMx property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJCarwhMx> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJCarwhMx> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJCarwhMx instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJCarwhMx model where model."
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
	 * Find all AdJCarwhMx entities.
	 * 
	 * @return List<AdJCarwhMx> all AdJCarwhMx entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJCarwhMx> findAll() {
		LogUtil.log("finding all AdJCarwhMx instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJCarwhMx model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}