package power.ejb.hr.ca;

import java.util.Date;
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
 * Facade for entity HrCOvertime.
 * 
 * @see power.ejb.hr.ca.HrCOvertime
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCOvertimeFacade implements HrCOvertimeFacadeRemote {
	// property constants
	public static final String OVERTIME_TYPE_CODE = "overtimeTypeCode";
	public static final String OVERTIME_TYPE = "overtimeType";
	public static final String IF_OVERTIME_FEE = "ifOvertimeFee";
	public static final String OVERTIME_MARK = "overtimeMark";
	public static final String LAST_MODIFIY_BY = "lastModifiyBy";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved HrCOvertime entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCOvertime entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCOvertime entity) {
		LogUtil.log("EJB:加班类别维护新增开始", Level.INFO, null);
		try {
			// 获得流水号
			Long id = bll.getMaxId("HR_C_OVERTIME", "OVERTIME_TYPE_ID");
			// 设定流水号
			entity.setOvertimeTypeId(id);
			// 设定修改时间
			entity.setLastModifiyDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("EJB:加班类别维护新增结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:加班类别维护新增失败", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCOvertime entity.
	 * 
	 * @param entity
	 *            HrCOvertime entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCOvertime entity) {
		LogUtil.log("deleting HrCOvertime instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCOvertime.class, entity
					.getOvertimeTypeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCOvertime entity and return it or a copy of
	 * it to the sender. A copy of the HrCOvertime entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrCOvertime entity to update
	 * @return HrCOvertime the persisted HrCOvertime entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCOvertime update(HrCOvertime entity) {
		LogUtil.log("EJB:加班类别维护更新开始", Level.INFO, null);
		try {
			// 设定修改时间
			entity.setLastModifiyDate(new Date());
			HrCOvertime result = entityManager.merge(entity);
			LogUtil.log("EJB:加班类别维护更新成功", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:加班类别维护更新失败", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCOvertime findById(Long id) {
		LogUtil.log("finding HrCOvertime instance with id: " + id, Level.INFO,
				null);
		try {
			HrCOvertime instance = entityManager.find(HrCOvertime.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCOvertime entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCOvertime property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCOvertime> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCOvertime> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCOvertime instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCOvertime model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrCOvertime> findByOvertimeTypeCode(Object overtimeTypeCode) {
		return findByProperty(OVERTIME_TYPE_CODE, overtimeTypeCode);
	}

	public List<HrCOvertime> findByOvertimeType(Object overtimeType) {
		return findByProperty(OVERTIME_TYPE, overtimeType);
	}

	public List<HrCOvertime> findByIfOvertimeFee(Object ifOvertimeFee) {
		return findByProperty(IF_OVERTIME_FEE, ifOvertimeFee);
	}

	public List<HrCOvertime> findByOvertimeMark(Object overtimeMark) {
		return findByProperty(OVERTIME_MARK, overtimeMark);
	}

	public List<HrCOvertime> findByLastModifiyBy(Object lastModifiyBy) {
		return findByProperty(LAST_MODIFIY_BY, lastModifiyBy);
	}

	public List<HrCOvertime> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<HrCOvertime> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all HrCOvertime entities.
	 * 
	 * @return List<HrCOvertime> all HrCOvertime entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCOvertime> findAll() {
		LogUtil.log("finding all HrCOvertime instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCOvertime model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}