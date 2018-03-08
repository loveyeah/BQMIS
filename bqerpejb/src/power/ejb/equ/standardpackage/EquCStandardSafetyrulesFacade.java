package power.ejb.equ.standardpackage;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquCStandardSafetyrules.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardSafetyrules
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardSafetyrulesFacade implements
		EquCStandardSafetyrulesFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved EquCStandardSafetyrules
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardSafetyrules entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquCStandardSafetyrules entity) {
		LogUtil
				.log("saving EquCStandardSafetyrules instance", Level.INFO,
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
	 * Delete a persistent EquCStandardSafetyrules entity.
	 * 
	 * @param entity
	 *            EquCStandardSafetyrules entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCStandardSafetyrules entity) {
		LogUtil.log("deleting EquCStandardSafetyrules instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(EquCStandardSafetyrules.class,
					entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved EquCStandardSafetyrules entity and return it
	 * or a copy of it to the sender. A copy of the EquCStandardSafetyrules
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardSafetyrules entity to update
	 * @return EquCStandardSafetyrules the persisted EquCStandardSafetyrules
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardSafetyrules update(EquCStandardSafetyrules entity) {
		LogUtil.log("updating EquCStandardSafetyrules instance", Level.INFO,
				null);
		try {
			EquCStandardSafetyrules result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCStandardSafetyrules findById(Long id) {
		LogUtil.log("finding EquCStandardSafetyrules instance with id: " + id,
				Level.INFO, null);
		try {
			EquCStandardSafetyrules instance = entityManager.find(
					EquCStandardSafetyrules.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquCStandardSafetyrules entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardSafetyrules property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardSafetyrules> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardSafetyrules> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding EquCStandardSafetyrules instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquCStandardSafetyrules model where model."
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
	 * Find all EquCStandardSafetyrules entities.
	 * 
	 * @return List<EquCStandardSafetyrules> all EquCStandardSafetyrules
	 *         entities
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardSafetyrules> findAll() {
		LogUtil.log("finding all EquCStandardSafetyrules instances",
				Level.INFO, null);
		try {
			final String queryString = "select model from EquCStandardSafetyrules model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}