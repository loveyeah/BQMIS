package power.ejb.resource;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
/**
 * Facade for entity InvJLot.
 * @see power.ejb.resource.InvJLot
  * @author MyEclipse Persistence Tools 
 */
@Stateless

public class InvJLotFacade  implements InvJLotFacadeRemote {
	//property constants
	public static final String MATERIAL_ID = "materialId";
	public static final String LOT_NO = "lotNo";
	public static final String WHS_NO = "whsNo";
	public static final String LOCATION_NO = "locationNo";
	public static final String MONTH_AMOUNT = "monthAmount";
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

	/** 事务历史表处理远程接口 */
	@EJB(beanName = "InvJTransactionHisFacade")
	protected InvJTransactionHisFacadeRemote transactionHisFacadeRemote;
	/** 库存物料记录处理远程接口 */
	@EJB(beanName = "InvJWarehouseFacade")
	protected InvJWarehouseFacadeRemote warehouseMaterielRemote;
	/** 库位物料记录处理远程接口 */
	@EJB(beanName = "InvJLocationFacade")
	protected InvJLocationFacadeRemote locationFacadeRemote;
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;


   /**
	 * Perform an initial save of a previously unsaved InvJLot entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvJLot entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvJLot entity) {
		LogUtil.log("saving InvJLot instance", Level.INFO, null);
		try {
			if(entity.getLotInvId()==null){
				// 设定主键值
		        entity.setLotInvId((bll.getMaxId("INV_J_LOT", "LOT_INV_ID")));
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
	 * Delete a persistent InvJLot entity.
	 * 
	 * @param entity
	 *            InvJLot entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJLot entity) {
		LogUtil.log("deleting InvJLot instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(InvJLot.class, entity
					.getLotInvId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved InvJLot entity and return it or a copy of it
	 * to the sender. A copy of the InvJLot entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            InvJLot entity to update
	 * @return InvJLot the persisted InvJLot entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJLot update(InvJLot entity) {
		LogUtil.log("updating InvJLot instance", Level.INFO, null);
		try {
			entity.setLastModifiedDate(new java.util.Date());
			InvJLot result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvJLot findById(Long id) {
		LogUtil
				.log("finding InvJLot instance with id: " + id, Level.INFO,
						null);
		try {
			InvJLot instance = entityManager.find(InvJLot.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvJLot entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJLot property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJLot> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvJLot> findByProperty(String propertyName, final Object value) {
		LogUtil.log("finding InvJLot instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvJLot model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<InvJLot> findByMaterialId(Object materialId) {
		return findByProperty(MATERIAL_ID, materialId);
	}

	public List<InvJLot> findByLotNo(Object lotNo) {
		return findByProperty(LOT_NO, lotNo);
	}

	public List<InvJLot> findByWhsNo(Object whsNo) {
		return findByProperty(WHS_NO, whsNo);
	}

	public List<InvJLot> findByLocationNo(Object locationNo) {
		return findByProperty(LOCATION_NO, locationNo);
	}

	public List<InvJLot> findByMonthAmount(Object monthAmount) {
		return findByProperty(MONTH_AMOUNT, monthAmount);
	}

	public List<InvJLot> findByMonthCost(Object monthCost) {
		return findByProperty(MONTH_COST, monthCost);
	}

	public List<InvJLot> findByYearAmount(Object yearAmount) {
		return findByProperty(YEAR_AMOUNT, yearAmount);
	}

	public List<InvJLot> findByYearCost(Object yearCost) {
		return findByProperty(YEAR_COST, yearCost);
	}

	public List<InvJLot> findByOpenBalance(Object openBalance) {
		return findByProperty(OPEN_BALANCE, openBalance);
	}

	public List<InvJLot> findByReceipt(Object receipt) {
		return findByProperty(RECEIPT, receipt);
	}

	public List<InvJLot> findByAdjust(Object adjust) {
		return findByProperty(ADJUST, adjust);
	}

	public List<InvJLot> findByIssue(Object issue) {
		return findByProperty(ISSUE, issue);
	}

	public List<InvJLot> findByReserved(Object reserved) {
		return findByProperty(RESERVED, reserved);
	}

	public List<InvJLot> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<InvJLot> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<InvJLot> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all InvJLot entities.
	 * 
	 * @return List<InvJLot> all InvJLot entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvJLot> findAll() {
		LogUtil.log("finding all InvJLot instances", Level.INFO, null);
		try {
			final String queryString = "select model from InvJLot model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 由批号，仓库编码，库位编码和物料编码查询批号物料记录
	 * @param enterpriseCode 企业编码
	 * @param lotNo 批号
	 * @param whsNo 仓库编码
	 * @param locationNo 库位编码
	 * @param materialId 物料流水号
	 * @return 批号物料记录bean
	 */
	@SuppressWarnings("unchecked")
	public List<InvJLot> findByLWHLM(String enterpriseCode, String lotNo, String whsNo, String locationNo, Long materialId) {
		String sql = "";
		// 判断库位是否为空
		if (null != locationNo && !"".equals(locationNo)) {
			sql=
				"select *\n" +
				"   from INV_J_LOT t\n" + 
				" where t.is_use = 'Y'\n" + 
				"   AND t.ENTERPRISE_CODE = ?\n" + 
				"   and t.LOT_NO = ?\n" + 
				"   and t.WHS_NO = ?\n" + 
				"   and t.LOCATION_NO=?\n" + 
				"   and t.MATERIAL_ID=?";
			return bll.queryByNativeSQL(sql, new Object[]{enterpriseCode,lotNo,whsNo,locationNo, materialId}, InvJLot.class);
		} else {
			sql=
				"select *\n" +
				"   from INV_J_LOT t\n" + 
				" where t.is_use = 'Y'\n" + 
				"   AND t.ENTERPRISE_CODE = ?\n" + 
				"   and t.LOT_NO = ?\n" + 
				"   and t.WHS_NO = ?\n" +
				"   and t.LOCATION_NO is null\n" +
				"   and t.MATERIAL_ID=?";
			return bll.queryByNativeSQL(sql, new Object[]{enterpriseCode,lotNo, whsNo, materialId}, InvJLot.class);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateInvLotRelatedInfor(
			InvJTransactionHis entityInvJTransactionHis,
			InvJWarehouse entityInInvJWarehouse,
			InvJWarehouse entityInOutInvJWarehouse,
			InvJLocation entityInInvJLocatione,
			InvJLocation entityOutInvJLocatione, InvJLot entityInInvJLot,
			InvJLot entityOutInvJLot) {
		// 历史事务表的登录
		transactionHisFacadeRemote.save(entityInvJTransactionHis);
		
		// 调入仓库的库存物料记录的处理
		// 主键不为空的时后，更新否则登陆
		if(entityInInvJWarehouse != null){
			if(entityInInvJWarehouse.getWarehouseInvId() != null){
				warehouseMaterielRemote.update(entityInInvJWarehouse);
			}else{
				warehouseMaterielRemote.save(entityInInvJWarehouse);
			}	
		}
		
		if(entityInOutInvJWarehouse != null){
			// 调出仓库的库存物料记录的处理
			warehouseMaterielRemote.update(entityInOutInvJWarehouse);	
		}
		
		
		// 调入库位物料记录的处理
		// 不为空的时候，做相应的处理
		if(entityInInvJLocatione != null){
			//  如果主键不为空，则更新否则登陆
			if(entityInInvJLocatione.getLocationInvId() != null){
				locationFacadeRemote.update(entityInInvJLocatione);
			}else{
				locationFacadeRemote.save(entityInInvJLocatione);
			}
			
		}
		
		// 调出库位的库位物料记录的处理
		// 不为空的时候，做相应的处理
		if(entityOutInvJLocatione != null){
			locationFacadeRemote.update(entityOutInvJLocatione);
		}
		
		// 调入批号记录表的处理
	    //  如果主键不为空，则更新否则登陆
		if(entityInInvJLot.getLotInvId() != null){
			update(entityInInvJLot);
		}else{
			save(entityInInvJLot);
		}
		
		// 调出仓库的批号记录表的处理
		update(entityOutInvJLot);
	}
}