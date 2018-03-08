package power.ejb.hr;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity HrJContractchange.
 * 
 * @see power.ejb.hr.HrJContractchange
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJContractchangeFacade implements HrJContractchangeFacadeRemote {
	// property constants
	public static final String WORKCONTRACTID = "workcontractid";
	public static final String OLD_CONTRACT_CODE = "oldContractCode";
	public static final String NEW_CONTRACT_CODE = "newContractCode";
	public static final String OLD_DEP_CODE = "oldDepCode";
	public static final String NEW_DEP_CODE = "newDepCode";
	public static final String OLD_STATION_CODE = "oldStationCode";
	public static final String NEW_STATION_CODE = "newStationCode";
	public static final String CHANGE_REASON = "changeReason";
	public static final String MEMO = "memo";
	public static final String INSERTBY = "insertby";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String EMP_ID2 = "empId2";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved HrJContractchange entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJContractchange entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJContractchange entity) {
		LogUtil.log("saving HrJContractchange instance", Level.INFO, null);
		try {
			// 采番处理
			entity.setContractchangeid(bll.getMaxId("HR_J_CONTRACTCHANGE", "CONTRACTCHANGEID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrJContractchange entity.
	 * 
	 * @param entity
	 *            HrJContractchange entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJContractchange entity) {
		LogUtil.log("deleting HrJContractchange instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJContractchange.class, entity
					.getContractchangeid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrJContractchange entity and return it or a
	 * copy of it to the sender. A copy of the HrJContractchange entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJContractchange entity to update
	 * @return HrJContractchange the persisted HrJContractchange entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJContractchange update(HrJContractchange entity) {
		LogUtil.log("updating HrJContractchange instance", Level.INFO, null);
		try {
			HrJContractchange result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJContractchange findById(Long id) {
		LogUtil.log("finding HrJContractchange instance with id: " + id,
				Level.INFO, null);
		try {
			HrJContractchange instance = entityManager.find(
					HrJContractchange.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJContractchange entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJContractchange property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJContractchange> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJContractchange> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJContractchange instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJContractchange model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrJContractchange> findByWorkcontractid(Object workcontractid) {
		return findByProperty(WORKCONTRACTID, workcontractid);
	}

	public List<HrJContractchange> findByOldContractCode(Object oldContractCode) {
		return findByProperty(OLD_CONTRACT_CODE, oldContractCode);
	}

	public List<HrJContractchange> findByNewContractCode(Object newContractCode) {
		return findByProperty(NEW_CONTRACT_CODE, newContractCode);
	}

	public List<HrJContractchange> findByOldDepCode(Object oldDepCode) {
		return findByProperty(OLD_DEP_CODE, oldDepCode);
	}

	public List<HrJContractchange> findByNewDepCode(Object newDepCode) {
		return findByProperty(NEW_DEP_CODE, newDepCode);
	}

	public List<HrJContractchange> findByOldStationCode(Object oldStationCode) {
		return findByProperty(OLD_STATION_CODE, oldStationCode);
	}

	public List<HrJContractchange> findByNewStationCode(Object newStationCode) {
		return findByProperty(NEW_STATION_CODE, newStationCode);
	}

	public List<HrJContractchange> findByChangeReason(Object changeReason) {
		return findByProperty(CHANGE_REASON, changeReason);
	}

	public List<HrJContractchange> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<HrJContractchange> findByInsertby(Object insertby) {
		return findByProperty(INSERTBY, insertby);
	}

	public List<HrJContractchange> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<HrJContractchange> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<HrJContractchange> findByEmpId2(Object empId2) {
		return findByProperty(EMP_ID2, empId2);
	}

	public List<HrJContractchange> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all HrJContractchange entities.
	 * 
	 * @return List<HrJContractchange> all HrJContractchange entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJContractchange> findAll() {
		LogUtil
				.log("finding all HrJContractchange instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from HrJContractchange model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}