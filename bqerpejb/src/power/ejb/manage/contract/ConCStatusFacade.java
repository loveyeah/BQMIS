package power.ejb.manage.contract;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity ConCStatus.
 * 
 * @see power.ejb.manage.contract.ConCStatus
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConCStatusFacade implements ConCStatusFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	/**
	 * Perform an initial save of a previously unsaved ConCStatus entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            ConCStatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(ConCStatus entity) {
		try {
			entity.setId(bll.getMaxId("CON_C_STATUS", "ID"));
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent ConCStatus entity.
	 * 
	 * @param entity
	 *            ConCStatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(ConCStatus entity) {
		LogUtil.log("deleting ConCStatus instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(ConCStatus.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved ConCStatus entity and return it or a copy of
	 * it to the sender. A copy of the ConCStatus entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            ConCStatus entity to update
	 * @return ConCStatus the persisted ConCStatus entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public ConCStatus update(ConCStatus entity) {
		LogUtil.log("updating ConCStatus instance", Level.INFO, null);
		try {
			ConCStatus result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConCStatus findById(Long id) {
		LogUtil.log("finding ConCStatus instance with id: " + id, Level.INFO,
				null);
		try {
			ConCStatus instance = entityManager.find(ConCStatus.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all ConCStatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ConCStatus property to query
	 * @param value
	 *            the property value to match
	 * @return List<ConCStatus> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<ConCStatus> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding ConCStatus instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from ConCStatus model where model."
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
	 * Find all ConCStatus entities.
	 * 
	 * @return List<ConCStatus> all ConCStatus entities
	 */
	@SuppressWarnings("unchecked")
	public List<ConCStatus> findAll() {
		LogUtil.log("finding all ConCStatus instances", Level.INFO, null);
		try {
			final String queryString = "select model from ConCStatus model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}