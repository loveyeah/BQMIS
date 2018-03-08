package power.ejb.manage.project;

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
 * Facade for entity PrjCStatus.
 * 
 * @see power.ejb.manage.project.PrjCStatus
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PrjCStatusFacade implements PrjCStatusFacadeRemote {
	// property constants
	public static final String PRJ_STATUS_NAME = "prjStatusName";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PrjCStatus entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PrjCStatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PrjCStatus entity) {
		LogUtil.log("saving PrjCStatus instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PrjCStatus entity.
	 * 
	 * @param entity
	 *            PrjCStatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PrjCStatus entity) {
		LogUtil.log("deleting PrjCStatus instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PrjCStatus.class, entity
					.getPrjStatusId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PrjCStatus entity and return it or a copy of
	 * it to the sender. A copy of the PrjCStatus entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PrjCStatus entity to update
	 * @return PrjCStatus the persisted PrjCStatus entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PrjCStatus update(PrjCStatus entity) {
		LogUtil.log("updating PrjCStatus instance", Level.INFO, null);
		try {
			PrjCStatus result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PrjCStatus findById(Long id) {
		LogUtil.log("finding PrjCStatus instance with id: " + id, Level.INFO,
				null);
		try {
			PrjCStatus instance = entityManager.find(PrjCStatus.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PrjCStatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PrjCStatus property to query
	 * @param value
	 *            the property value to match
	 * @return List<PrjCStatus> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PrjCStatus> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PrjCStatus instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PrjCStatus model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PrjCStatus> findByPrjStatusName(Object prjStatusName) {
		return findByProperty(PRJ_STATUS_NAME, prjStatusName);
	}

	/**
	 * Find all PrjCStatus entities.
	 * 
	 * @return List<PrjCStatus> all PrjCStatus entities
	 */
	@SuppressWarnings("unchecked")
	public List<PrjCStatus> findAll(String prjStatusType) {
		LogUtil.log("finding all PrjCStatus instances", Level.INFO, null);
		try {
			String queryString = "select t.* from prj_c_status t ";
			
			if (prjStatusType != null && !prjStatusType.equals("")) {
				String strwhere = "where	 t.prj_status_type in ('"
						+ prjStatusType + "')";
				queryString += strwhere;
			}
			List<PrjCStatus> list = bll.queryByNativeSQL(queryString,
					PrjCStatus.class);
			if (list != null) {
				return list;
			} else {
				return null;
			}
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}