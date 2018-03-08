package power.ejb.administration;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJMeetMx.
 * 
 * @see power.ejb.administration.AdJMeetMx
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJMeetMxFacade implements AdJMeetMxFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved AdJMeetMx entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJMeetMx entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJMeetMx entity) {
		LogUtil.log("saving AdJMeetMx instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent AdJMeetMx entity.
	 * 
	 * @param entity
	 *            AdJMeetMx entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJMeetMx entity) {
		LogUtil.log("deleting AdJMeetMx instance", Level.INFO, null);
		try {
			entity = entityManager
					.getReference(AdJMeetMx.class, entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AdJMeetMx entity and return it or a copy of it
	 * to the sender. A copy of the AdJMeetMx entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJMeetMx entity to update
	 * @return AdJMeetMx the persisted AdJMeetMx entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJMeetMx update(AdJMeetMx entity) {
		LogUtil.log("updating AdJMeetMx instance", Level.INFO, null);
		try {
			AdJMeetMx result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public AdJMeetMx findById(Long id) {
		LogUtil.log("finding AdJMeetMx instance with id: " + id, Level.INFO,
				null);
		try {
			AdJMeetMx instance = entityManager.find(AdJMeetMx.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJMeetMx entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJMeetMx property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJMeetMx> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJMeetMx> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJMeetMx instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJMeetMx model where model."
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
	 * Find all AdJMeetMx entities.
	 * 
	 * @return List<AdJMeetMx> all AdJMeetMx entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJMeetMx> findAll() {
		LogUtil.log("finding all AdJMeetMx instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJMeetMx model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}