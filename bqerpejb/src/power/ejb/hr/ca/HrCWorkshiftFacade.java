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
 * Facade for entity HrCWorkshift.
 * 
 * @see power.ejb.hr.ca.HrCWorkshift
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCWorkshiftFacade implements HrCWorkshiftFacadeRemote {
	// property constants
	public static final String WORK_SHIFT_CODE = "workShiftCode";
	public static final String WORK_SHIFT = "workShift";
	public static final String WORK_SHIT_FEE = "workShitFee";
	public static final String WORK_SHIFT_MARK = "workShiftMark";
	public static final String LAST_MODIFIY_BY = "lastModifiyBy";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved HrCWorkshift entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCWorkshift entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCWorkshift entity) {
		LogUtil.log("EJB:运行班类别维护新增开始", Level.INFO, null);
		try {
			// 获得流水号
			Long id = bll.getMaxId("HR_C_WORKSHIFT", "WORK_SHIFT_ID");
			// 设定流水号
			entity.setWorkShiftId(id);
			// 设定修改时间
			entity.setLastModifiyDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("EJB:运行班类别维护新增结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:运行班类别维护新增失败", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCWorkshift entity.
	 * 
	 * @param entity
	 *            HrCWorkshift entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCWorkshift entity) {
		LogUtil.log("deleting HrCWorkshift instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCWorkshift.class, entity
					.getWorkShiftId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCWorkshift entity and return it or a copy of
	 * it to the sender. A copy of the HrCWorkshift entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrCWorkshift entity to update
	 * @return HrCWorkshift the persisted HrCWorkshift entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCWorkshift update(HrCWorkshift entity) {
		LogUtil.log("EJB:运行班类别维护更新开始", Level.INFO, null);
		try {
			// 设定修改时间
			entity.setLastModifiyDate(new Date());
			HrCWorkshift result = entityManager.merge(entity);
			LogUtil.log("EJB:运行班类别维护更新结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:运行班类别维护更新失败", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCWorkshift findById(Long id) {
		LogUtil.log("finding HrCWorkshift instance with id: " + id, Level.INFO,
				null);
		try {
			HrCWorkshift instance = entityManager.find(HrCWorkshift.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCWorkshift entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCWorkshift property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCWorkshift> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCWorkshift> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCWorkshift instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCWorkshift model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrCWorkshift> findByWorkShiftCode(Object workShiftCode) {
		return findByProperty(WORK_SHIFT_CODE, workShiftCode);
	}

	public List<HrCWorkshift> findByWorkShift(Object workShift) {
		return findByProperty(WORK_SHIFT, workShift);
	}

	public List<HrCWorkshift> findByWorkShitFee(Object workShitFee) {
		return findByProperty(WORK_SHIT_FEE, workShitFee);
	}

	public List<HrCWorkshift> findByWorkShiftMark(Object workShiftMark) {
		return findByProperty(WORK_SHIFT_MARK, workShiftMark);
	}

	public List<HrCWorkshift> findByLastModifiyBy(Object lastModifiyBy) {
		return findByProperty(LAST_MODIFIY_BY, lastModifiyBy);
	}

	public List<HrCWorkshift> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<HrCWorkshift> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all HrCWorkshift entities.
	 * 
	 * @return List<HrCWorkshift> all HrCWorkshift entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCWorkshift> findAll() {
		LogUtil.log("finding all HrCWorkshift instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCWorkshift model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}