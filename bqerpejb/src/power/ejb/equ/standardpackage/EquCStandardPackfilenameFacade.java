package power.ejb.equ.standardpackage;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquCStandardPackfilename.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardPackfilename
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardPackfilenameFacade implements
		EquCStandardPackfilenameFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved EquCStandardPackfilename
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardPackfilename entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquCStandardPackfilename entity) {
		LogUtil.log("saving EquCStandardPackfilename instance", Level.INFO,
				null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent EquCStandardPackfilename entity.
	 * 
	 * @param entity
	 *            EquCStandardPackfilename entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCStandardPackfilename entity) {
		LogUtil.log("deleting EquCStandardPackfilename instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(EquCStandardPackfilename.class,
					entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved EquCStandardPackfilename entity and return it
	 * or a copy of it to the sender. A copy of the EquCStandardPackfilename
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardPackfilename entity to update
	 * @return EquCStandardPackfilename the persisted EquCStandardPackfilename
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardPackfilename update(EquCStandardPackfilename entity) {
		LogUtil.log("updating EquCStandardPackfilename instance", Level.INFO,
				null);
		try {
			EquCStandardPackfilename result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCStandardPackfilename findById(Long id) {
		LogUtil.log("finding EquCStandardPackfilename instance with id: " + id,
				Level.INFO, null);
		try {
			EquCStandardPackfilename instance = entityManager.find(
					EquCStandardPackfilename.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquCStandardPackfilename entities with a specific property
	 * value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardPackfilename property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardPackfilename> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardPackfilename> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding EquCStandardPackfilename instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquCStandardPackfilename model where model."
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
	 * Find all EquCStandardPackfilename entities.
	 * 
	 * @return List<EquCStandardPackfilename> all EquCStandardPackfilename
	 *         entities
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardPackfilename> findAll() {
		LogUtil.log("finding all EquCStandardPackfilename instances",
				Level.INFO, null);
		try {
			final String queryString = "select model from EquCStandardPackfilename model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}