package power.ejb.manage.stat;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity BpCReportType.
 * 
 * @see power.ejb.manage.stat.BpCReportType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCReportTypeFacade implements BpCReportTypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved BpCReportType entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCReportType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCReportType entity) {
		LogUtil.log("saving BpCReportType instance", Level.INFO, null);
		try {
			entity.setId(dll.getMaxId("BP_C_REPORT_TYPE", "ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpCReportType entity.
	 * 
	 * @param entity
	 *            BpCReportType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCReportType entity) {
		LogUtil.log("deleting BpCReportType instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpCReportType.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpCReportType entity and return it or a copy
	 * of it to the sender. A copy of the BpCReportType entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCReportType entity to update
	 * @return BpCReportType the persisted BpCReportType entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCReportType update(BpCReportType entity) {
		LogUtil.log("updating BpCReportType instance", Level.INFO, null);
		try {
			BpCReportType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCReportType findById(Long id) {
		LogUtil.log("finding BpCReportType instance with id: " + id,
				Level.INFO, null);
		try {
			BpCReportType instance = entityManager
					.find(BpCReportType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpCReportType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCReportType property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCReportType> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpCReportType> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpCReportType instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpCReportType model where model."
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
	 * Find all BpCReportType entities.
	 * 
	 * @return List<BpCReportType> all BpCReportType entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpCReportType> findAll() {
		LogUtil.log("finding all BpCReportType instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpCReportType model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}