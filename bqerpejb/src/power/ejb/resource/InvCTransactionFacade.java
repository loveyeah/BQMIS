package power.ejb.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.resource.form.BookCheckInfo;
import power.ejb.resource.form.MaterialForPartInfo;
import power.ejb.resource.form.TransActionPartByCode;

/**
 * Facade for entity InvCTransaction.
 * 
 * @see power.ejb.resource.InvCTransaction
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class InvCTransactionFacade implements InvCTransactionFacadeRemote {
	// property constants
	public static final String TRANS_CODE = "transCode";
	public static final String TRANS_NAME = "transName";
	public static final String TRANS_DESC = "transDesc";
	public static final String IS_OPEN_BALANCE = "isOpenBalance";
	public static final String IS_RECEIVE = "isReceive";
	public static final String IS_ADJUST = "isAdjust";
	public static final String IS_ISSUES = "isIssues";
	public static final String IS_RESERVED = "isReserved";
	public static final String IS_INSPECTION = "isInspection";
	public static final String IS_SALE_AMOUNT = "isSaleAmount";
	public static final String IS_ENTRY_COST = "isEntryCost";
	public static final String IS_PO_COST = "isPoCost";
	public static final String IS_AJUST_COST = "isAjustCost";
	public static final String IS_ACTUAL_COST = "isActualCost";
	public static final String IS_CHECK_PO = "isCheckPo";
	public static final String IS_PO_QUANTITY = "isPoQuantity";
	public static final String IS_SHOP_ORDER = "isShopOrder";
	public static final String IS_CHECK_SHOP_ORDER = "isCheckShopOrder";
	public static final String IS_SHOP_ORDER_ISSUE = "isShopOrderIssue";
	public static final String LAST_MODIFIED_BY = "lastModifiedBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved InvCTransaction entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvCTransaction entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvCTransaction entity) {
		LogUtil.log("saving InvCTransaction instance", Level.INFO, null);
		try {
			if (entity.getTransId() == null) {
                // 设定主键值
                entity.setTransId(bll.getMaxId("INV_C_TRANSACTION",
                        "TRANS_ID"));
            }
            // 设定修改时间
            entity.setLastModifiedDate(new java.util.Date());
            // 设定是否使用
            entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent InvCTransaction entity.
	 * 
	 * @param entity
	 *            InvCTransaction entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvCTransaction entity) {
		LogUtil.log("deleting InvCTransaction instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(InvCTransaction.class, entity
					.getTransId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved InvCTransaction entity and return it or a copy
	 * of it to the sender. A copy of the InvCTransaction entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            InvCTransaction entity to update
	 * @return InvCTransaction the persisted InvCTransaction entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvCTransaction update(InvCTransaction entity) {
		LogUtil.log("updating InvCTransaction instance", Level.INFO, null);
		try {
			entity.setLastModifiedDate(new java.util.Date());
			InvCTransaction result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public InvCTransaction findById(Long id) {
		LogUtil.log("finding InvCTransaction instance with id: " + id,
				Level.INFO, null);
		try {
			InvCTransaction instance = entityManager.find(
					InvCTransaction.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all InvCTransaction entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCTransaction property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCTransaction> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<InvCTransaction> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding InvCTransaction instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from InvCTransaction model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<InvCTransaction> findByTransCode(Object transCode) {
		return findByProperty(TRANS_CODE, transCode);
	}

	public List<InvCTransaction> findByTransName(Object transName) {
		return findByProperty(TRANS_NAME, transName);
	}

	public List<InvCTransaction> findByTransDesc(Object transDesc) {
		return findByProperty(TRANS_DESC, transDesc);
	}

	public List<InvCTransaction> findByIsOpenBalance(Object isOpenBalance) {
		return findByProperty(IS_OPEN_BALANCE, isOpenBalance);
	}

	public List<InvCTransaction> findByIsReceive(Object isReceive) {
		return findByProperty(IS_RECEIVE, isReceive);
	}

	public List<InvCTransaction> findByIsAdjust(Object isAdjust) {
		return findByProperty(IS_ADJUST, isAdjust);
	}

	public List<InvCTransaction> findByIsIssues(Object isIssues) {
		return findByProperty(IS_ISSUES, isIssues);
	}

	public List<InvCTransaction> findByIsReserved(Object isReserved) {
		return findByProperty(IS_RESERVED, isReserved);
	}

	public List<InvCTransaction> findByIsInspection(Object isInspection) {
		return findByProperty(IS_INSPECTION, isInspection);
	}

	public List<InvCTransaction> findByIsSaleAmount(Object isSaleAmount) {
		return findByProperty(IS_SALE_AMOUNT, isSaleAmount);
	}

	public List<InvCTransaction> findByIsEntryCost(Object isEntryCost) {
		return findByProperty(IS_ENTRY_COST, isEntryCost);
	}

	public List<InvCTransaction> findByIsPoCost(Object isPoCost) {
		return findByProperty(IS_PO_COST, isPoCost);
	}

	public List<InvCTransaction> findByIsAjustCost(Object isAjustCost) {
		return findByProperty(IS_AJUST_COST, isAjustCost);
	}

	public List<InvCTransaction> findByIsActualCost(Object isActualCost) {
		return findByProperty(IS_ACTUAL_COST, isActualCost);
	}

	public List<InvCTransaction> findByIsCheckPo(Object isCheckPo) {
		return findByProperty(IS_CHECK_PO, isCheckPo);
	}

	public List<InvCTransaction> findByIsPoQuantity(Object isPoQuantity) {
		return findByProperty(IS_PO_QUANTITY, isPoQuantity);
	}

	public List<InvCTransaction> findByIsShopOrder(Object isShopOrder) {
		return findByProperty(IS_SHOP_ORDER, isShopOrder);
	}

	public List<InvCTransaction> findByIsCheckShopOrder(Object isCheckShopOrder) {
		return findByProperty(IS_CHECK_SHOP_ORDER, isCheckShopOrder);
	}

	public List<InvCTransaction> findByIsShopOrderIssue(Object isShopOrderIssue) {
		return findByProperty(IS_SHOP_ORDER_ISSUE, isShopOrderIssue);
	}

	public List<InvCTransaction> findByLastModifiedBy(Object lastModifiedBy) {
		return findByProperty(LAST_MODIFIED_BY, lastModifiedBy);
	}

	public List<InvCTransaction> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<InvCTransaction> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all InvCTransaction entities.
	 * 
	 * @return List<InvCTransaction> all InvCTransaction entities
	 */
	@SuppressWarnings("unchecked")
	public List<InvCTransaction> findAll() {
		LogUtil.log("finding all InvCTransaction instances", Level.INFO, null);
		try {
			final String queryString = "select model from InvCTransaction model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 通过事务编码查询事务信息
	 * @param code 事务编码
	 * @return 事务信息
	 */
	public InvCTransaction findByCode(String code, String ... args) {
		String sql = 
			"SELECT *\n" + 
			"FROM INV_C_TRANSACTION A\n" + 
			"WHERE A.TRANS_CODE = ? AND A.IS_USE = 'Y'";
		if(args != null && args.length > 0) {
			sql += " AND A.enterprise_code='" + args[0] + "'";
		}
		List <InvCTransaction> arrlist = bll.queryByNativeSQL(sql, new Object[]{code});
		if (arrlist.size() > 0) {
			Iterator it = arrlist.iterator();
			while (it.hasNext()) {
				InvCTransaction trans = new InvCTransaction();
				Object[] data = (Object[]) it.next();
				// 物料盘点明细表流水号
				if(null != data[0])
					// 事务ID
					trans.setTransId(Long.parseLong(data[0].toString()));
				if(null != data[4])
					// 是否影响期初
					trans.setIsOpenBalance(data[4].toString());
				if(null != data[5])
					// 是否影响接收
					trans.setIsReceive(data[5].toString());
				if(null != data[6])
					// 是否影响调整
					trans.setIsAdjust(data[6].toString());
				if(null != data[7])
					// 是否影响出库
					trans.setIsIssues(data[7].toString());
				return trans;
			}
		}
		return null;
	}
	
	/**
	 * 模糊查询所有事物作用信息
	 * @param fuzzy 事务编码/名称
	 * @return PageObject 事务作用信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByFuzzy(String fuzzy,String enterpriseCode,final int... rowStartIdxAndCount) {
		LogUtil.log("finding all InvCTransaction instances", Level.INFO, null);
		try {
			PageObject result = new PageObject();;
			// sql查询
			String sql = 
				"SELECT * FROM INV_C_TRANSACTION A \n" +
				"WHERE A.IS_USE = 'Y' \n" + 
				"and (A.TRANS_CODE like '%" + fuzzy + "%' or A.TRANS_NAME like '%" + fuzzy + "%') \n" +
				"and A.enterprise_code='" + enterpriseCode + "'\n" +
				"order by A.TRANS_CODE \n";
			// 数量
			String sqlCount = 
				"SELECT count(A.TRANS_ID) FROM INV_C_TRANSACTION A \n" +
				"WHERE A.IS_USE = 'Y' \n" + 
				"and (A.TRANS_CODE like '%" + fuzzy + "%' or A.TRANS_NAME like '%" + fuzzy + "%') \n" +
				"and A.enterprise_code='" + enterpriseCode + "'\n" +
				"order by A.TRANS_CODE \n";
			List<InvCTransaction> arrlist = bll.queryByNativeSQL(sql, InvCTransaction.class, rowStartIdxAndCount);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(arrlist);
			result.setTotalCount(totalCount);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 查询事物编码是否是唯一的
	 * 
	 * @param transCode
	 * @param enterpriseCode          
	 * @return boolean false 唯一
	 */
	public boolean checkTransCode(String transCode,String enterpriseCode) {
		boolean isSame = false;
        String sql = "select count(TRANS_CODE) from INV_C_TRANSACTION t\n"
                + "where t.TRANS_CODE='" + transCode + "'\n"
                + "and t.enterprise_code='" + enterpriseCode + "'\n"
                + "and t.is_use='Y'";
        if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
            isSame = true;
        }
        return isSame;
	}
	
	/**
	 * 查询所有事物作用信息
	 * @return PageObject 事务作用信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject findTransCodeByAll(String enterpriseCode) {
		LogUtil.log("finding all InvCTransaction instances", Level.INFO, null);
		try {
			PageObject result = new PageObject();;
			// sql查询
			String sql = 
				"SELECT distinct TRANS_CODE,TRANS_NAME FROM INV_C_TRANSACTION A \n" +
				"where A.enterprise_code='" + enterpriseCode + "'\n" +
				"order by A.TRANS_CODE \n";
			List list=bll.queryByNativeSQL(sql);
			 // 给InvCMaterial赋值
            List arrlist = new ArrayList();
			if(list !=null && list.size()>0)
			{
				result = new PageObject();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					TransActionPartByCode model = new TransActionPartByCode();
					Object[] data = (Object[]) it.next();
					if (data[0] != null)
						model.setTransCode(data[0].toString());
					if (data[1] != null)
						model.setTransName(data[1].toString());
					
					arrlist.add(model);
				}
				result.setList(arrlist);
			} 
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 查询事物名称是否是唯一的
	 * 
	 * @param transName
	 * @param enterpriseCode          
	 * @return boolean
	 */
	public boolean checkTransName(String transName,String enterpriseCode) {
		boolean isSame = false;
        String sql = "select count(t.TRANS_NAME) from INV_C_TRANSACTION  t\n"
                + "where t.TRANS_NAME='" + transName + "'\n"
                + "and t.enterprise_code='" + enterpriseCode + "'\n"
                + "and t.is_use='Y'";
        if (Long.parseLong((bll.getSingal(sql).toString())) > 0) {
            isSame = true;
        }
        return isSame;
	}
	 /**
     * 删除一条记录
     *
     * @param transId 流水号
     * @throws CodeRepeatException
     * @throws RuntimeException
     *             when the operation fails
     */
    public void deleteOfLogic(Long transId) throws CodeRepeatException {
        LogUtil.log("deleting InvCTransaction instance", Level.INFO, null);
        try {
        	InvCTransaction entity=this.findById(transId);
             if(entity!=null)
             {
                 // is_use设为N
                 entity.setIsUse("N");
                 this.update(entity);
                 LogUtil.log("delete successful", Level.INFO, null);
             }
        } catch (RuntimeException re) {
            LogUtil.log("delete failed", Level.SEVERE, re);
            throw re;
        }
    }
    
    /**
     * 根据事务编号查找记录
     * @param transCode
     */
    public InvCTransaction findByTransCode(String enterpriseCode, String transCode) {
        LogUtil.log("finding InvCTransaction instance with whsNo: " + transCode,
                Level.INFO, null);
        try {
        	InvCTransaction instance = null;
            // 查询sql
            String sql=
                "select * from INV_C_TRANSACTION t\n" +
                "where  t.enterprise_code='"+enterpriseCode + "'\n" +
                "and t.TRANS_CODE='" + transCode +  "'\n" +
                "and t.is_use='Y' ";
            // 执行查询
            List<InvCTransaction> list=bll.queryByNativeSQL(sql, InvCTransaction.class);
            // 如果查到结果
            if(list!=null && list.size()> 0){
                instance = list.get(0);
            }
            // 返回
            return instance;
        } catch (RuntimeException re) {
            LogUtil.log("find failed", Level.SEVERE, re);
            throw re;
        }
    }
    
    /**
	 * 更新
     * 查询事物作用名称是否是唯一的
     * @param transName
     * @
     * @return list 
     */
	@SuppressWarnings("unchecked")
	public InvCTransaction checkTransNameForUpdate(String transName, String enterpriseCode) {
		try {
			List<InvCTransaction> list = null;
        String sql = 
        	"select * from INV_C_TRANSACTION \n" +
        	"where TRANS_NAME = '"+transName+"' \n" +
        	"and  is_use = 'Y' \n" +
        	"and  ENTERPRISE_CODE like '"+enterpriseCode+"'";
       // 执行查询
        list = bll.queryByNativeSQL(sql, InvCTransaction.class);
        if(list.size()!= 0) {
        	return list.get(0);
        }
        return null;
     } catch (RuntimeException re) {
        LogUtil.log("find failed", Level.SEVERE, re);
        throw re;
     }
    }

}