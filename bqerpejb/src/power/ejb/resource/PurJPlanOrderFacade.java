package power.ejb.resource;

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
 * 采购订单与需求计划关联.
 * 
 * @see power.ejb.logistics.PurJPlanOrder
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PurJPlanOrderFacade implements PurJPlanOrderFacadeRemote {
	// property constants
	public static final String REQUIREMENT_DETAIL_ID = "requirementDetailId";
	public static final String PUR_NO = "purNo";
	public static final String PUR_ORDER_DETAILS_ID = "purOrderDetailsId";
	public static final String MR_QTY = "mrQty";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @return entity 增加后记录
     */
	public PurJPlanOrder save(PurJPlanOrder entity) {
		LogUtil.log("saving PurJPlanOrder instance", Level.INFO, null);
		try {
			// 流水号自动采番
			entity.setPlanOrderId(bll.getMaxId("PUR_J_PLAN_ORDER", "PLAN_ORDER_ID"));
			// 设定修改时间
            entity.setLastModifiedDate(new Date());
            // 设定是否使用
            entity.setIsUse("Y");
			
            entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 增加一条记录(多次增加时用)
	 *
	 * @param entity 要增加的记录
	 * @param argId 上次增加记录的流水号
	 * @return Long 增加后记录的流水号
	 */
	public Long save(PurJPlanOrder entity, Long argId) {
		LogUtil.log("saving PurJPlanOrder instance", Level.INFO, null);
		try {
			Long lngId = argId;
			if (lngId == null || lngId.longValue() < 1L) {
				// 流水号自动采番
				lngId = bll.getMaxId("PUR_J_PLAN_ORDER", "PLAN_ORDER_ID");
			} else {
				lngId = Long.valueOf(argId.longValue() + 1L);
			}
			
			// 流水号
			entity.setPlanOrderId(lngId);
			// 设定修改时间
            entity.setLastModifiedDate(new Date());
            // 设定是否使用
            entity.setIsUse("Y");
			
            entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			
			return lngId;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 删除一条记录
	 *
	 * @param entity 采购订单与需求计划关联对象
	 * @throws RuntimeException when the operation fails
	 */
	public void delete(PurJPlanOrder entity) {
		LogUtil.log("deleting PurJPlanOrder instance", Level.INFO, null);
		try {
			// 是否使用设为N
			entity.setIsUse("N");
			// 设定修改时间
			entity.setLastModifiedDate(new Date());
			
			// 更新
			update(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

    /**
     * 修改记录
     *
     * @param entity 要修改的记录
     * @return entity 修改后记录
     */
	public PurJPlanOrder update(PurJPlanOrder entity) {
		LogUtil.log("updating PurJPlanOrder instance", Level.INFO, null);
		try {
			// 设定修改时间
			entity.setLastModifiedDate(new Date());
			PurJPlanOrder result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 通过采购订单与需求计划关联流水号得到采购订单与需求计划关联
	 * 
	 * @param id 采购订单与需求计划关联流水号
     * @return PurJPlanOrder 采购订单与需求计划关联
	 */
	public PurJPlanOrder findById(Long id) {
		LogUtil.log("finding PurJPlanOrder instance with id: " + id,
				Level.INFO, null);
		try {
			PurJPlanOrder instance = entityManager
					.find(PurJPlanOrder.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PurJPlanOrder entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PurJPlanOrder property to query
	 * @param value
	 *            the property value to match
	 * @return List<PurJPlanOrder> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PurJPlanOrder> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PurJPlanOrder instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PurJPlanOrder model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PurJPlanOrder> findByRequirementDetailId(
			Object requirementDetailId) {
		return findByProperty(REQUIREMENT_DETAIL_ID, requirementDetailId);
	}

	public List<PurJPlanOrder> findByPurNo(Object purNo) {
		return findByProperty(PUR_NO, purNo);
	}

	public List<PurJPlanOrder> findByPurOrderDetailsId(Object purOrderDetailsId) {
		return findByProperty(PUR_ORDER_DETAILS_ID, purOrderDetailsId);
	}

	/**
	 * 查找所有的采购订单与需求计划关联
	 * 
	 * @return List<PurJPlanOrder> 采购订单与需求计划关联
	 */
	@SuppressWarnings("unchecked")
	public List<PurJPlanOrder> findAll() {
		LogUtil.log("finding all PurJPlanOrder instances", Level.INFO, null);
		try {
			final String queryString = "select model from PurJPlanOrder model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}