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
 * Facade for entity InvJLocation.
 * 
 * @see power.ejb.resource.InvJLocation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvJLocationFacade implements InvJLocationFacadeRemote {
    // property constants
	public static final String MATERIAL_ID = "materialId";
	public static final String WHS_NO = "whsNo";
	public static final String LOCATION_NO = "locationNo";
	public static final String MATERIAL_NO = "materialNo";
	public static final String MONTH_COST = "monthCost";
	public static final String YEAR_AMOUNT = "yearAmount";
	public static final String YEAR_COST = "yearCost";
	public static final String OPEN_BALANCE = "openBalance";
	public static final String RECEIPT = "receipt";
	public static final String ADJUST = "adjust";
	public static final String ISSUE = "issue";
	public static final String RESERVED = "reserved";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager; 
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved InvJLocation entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvJLocation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvJLocation entity) {
		LogUtil.log("saving InvJLocation instance", Level.INFO, null);
		try {
			if(entity.getLocationInvId()==null){
			    // 设定主键值
		        entity.setLocationInvId(bll.getMaxId("INV_J_LOCATION", "LOCATION_INV_ID"));
				}
	        // 设定修改时间
	        entity.setLastModifiedDate(new java.util.Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent InvJLocation entity.
	 * 
	 * @param entity
	 *            InvJLocation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJLocation entity) {
		LogUtil.log("deleting InvJLocation instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(InvJLocation.class, entity
					.getLocationInvId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved InvJLocation entity and return it or a copy of
	 * it to the sender. A copy of the InvJLocation entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            InvJLocation entity to update
	 * @return InvJLocation the persisted InvJLocation entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJLocation update(InvJLocation entity) {
		LogUtil.log("updating InvJLocation instance", Level.INFO, null);
		try {
			entity.setLastModifiedDate(new Date());
			InvJLocation result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvJLocation findById(Long id) {
		LogUtil.log("finding InvJLocation instance with id: " + id, Level.INFO,
				null);
		try {
			InvJLocation instance = entityManager.find(InvJLocation.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvJLocation entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJLocation property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJLocation> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvJLocation> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvJLocation instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvJLocation model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

   public List<InvJLocation> findByMaterialId(Object materialId) {
		return findByProperty(MATERIAL_ID, materialId);
	}

	public List<InvJLocation> findByWhsNo(Object whsNo) {
		return findByProperty(WHS_NO, whsNo);
	}

	public List<InvJLocation> findByLocationNo(Object locationNo) {
		return findByProperty(LOCATION_NO, locationNo);
	}

	public List<InvJLocation> findByMaterialNo(Object materialNo) {
		return findByProperty(MATERIAL_NO, materialNo);
	}

	public List<InvJLocation> findByMonthCost(Object monthCost) {
		return findByProperty(MONTH_COST, monthCost);
	}

	public List<InvJLocation> findByYearAmount(Object yearAmount) {
		return findByProperty(YEAR_AMOUNT, yearAmount);
	}

	public List<InvJLocation> findByYearCost(Object yearCost) {
		return findByProperty(YEAR_COST, yearCost);
	}

	public List<InvJLocation> findByOpenBalance(Object openBalance) {
		return findByProperty(OPEN_BALANCE, openBalance);
	}

	public List<InvJLocation> findByReceipt(Object receipt) {
		return findByProperty(RECEIPT, receipt);
	}

	public List<InvJLocation> findByAdjust(Object adjust) {
		return findByProperty(ADJUST, adjust);
	}

	public List<InvJLocation> findByIssue(Object issue) {
		return findByProperty(ISSUE, issue);
	}

	public List<InvJLocation> findByReserved(Object reserved) {
		return findByProperty(RESERVED, reserved);
	}

	public List<InvJLocation> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<InvJLocation> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<InvJLocation> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}
	/**
	 * Find all InvJLocation entities.
	 * 
	 * @return List<InvJLocation> all InvJLocation entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvJLocation> findAll() {
		LogUtil.log("finding all InvJLocation instances", Level.INFO, null);
		try {
			final String queryString = "select model from InvJLocation model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 由仓库编码，库位编码和物料编码查询库位物料记录
	 * @param enterpriseCode 企业编码
	 * @param whsNo 仓库编码
	 * @param locationNo 库位编码
	 * @param materialId 物料流水号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InvJLocation> findByWHLM(String enterpriseCode,String whsNo, String locationNo, Long materialId) {
		String sql;
		if(locationNo == null || "".equals(locationNo))
		{
			sql=
				"select *\n" +
				"   from INV_J_LOCATION t\n" +
				" where t.is_use = 'Y'\n" +
				"   AND t.ENTERPRISE_CODE = ?\n" + 
				"   and t.WHS_NO = ?\n" +
				"   and t.LOCATION_NO is null\n" +
				"   and t.MATERIAL_ID=?";
			return bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, whsNo, materialId}, InvJLocation.class);
		} else {
			sql=
				"select *\n" +
				"   from INV_J_LOCATION t\n" +
				" where t.is_use = 'Y'\n" +
				"   AND t.ENTERPRISE_CODE = ?\n" + 
				"   and t.WHS_NO = ?\n" +
				"   and t.LOCATION_NO=?\n" +
				"   and t.MATERIAL_ID=?";
			return bll.queryByNativeSQL(sql, new Object[]{enterpriseCode, whsNo, locationNo, materialId}, InvJLocation.class);
		}
	}

}