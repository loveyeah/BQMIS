package power.ejb.resource;

import java.util.Date;
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
import power.ejb.resource.SysCCurrency;
import power.ejb.workticket.business.RunJWorktickets;

/**
 * Facade for entity SysCCurrency.
 * 
 * @see power.ejb.resource.SysCCurrency
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SysCCurrencyFacade implements SysCCurrencyFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved SysCCurrency entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SysCCurrency entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(SysCCurrency entity) {
		LogUtil.log("saving SysCCurrency instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent SysCCurrency entity.
	 * 
	 * @param entity
	 *            SysCCurrency entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SysCCurrency entity) {
		LogUtil.log("deleting SysCCurrency instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SysCCurrency.class, entity
					.getCurrencyId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved SysCCurrency entity and return it or a copy of
	 * it to the sender. A copy of the SysCCurrency entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            SysCCurrency entity to update
	 * @return SysCCurrency the persisted SysCCurrency entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SysCCurrency update(SysCCurrency entity) {
		LogUtil.log("updating SysCCurrency instance", Level.INFO, null);
		try {
			SysCCurrency result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysCCurrency findById(Long id) {
		LogUtil.log("finding SysCCurrency instance with id: " + id, Level.INFO,
				null);
		try {
			SysCCurrency instance = entityManager.find(SysCCurrency.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all SysCCurrency entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SysCCurrency property to query
	 * @param value
	 *            the property value to match
	 * @return List<SysCCurrency> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<SysCCurrency> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding SysCCurrency instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SysCCurrency model where model."
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
	 * Find all SysCCurrency entities.
	 * 
	 * @return List<SysCCurrency> all SysCCurrency entities
	 */
	@SuppressWarnings("unchecked")
	public List<SysCCurrency> findAll() {
		LogUtil.log("finding all SysCCurrency instances", Level.INFO, null);
		try {
			final String queryString = "select model from SysCCurrency model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 逻辑删除一条币种信息.
	 * 
	 * @param entity
	 *            SysCCurrency entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void deleteAdd(Long id,String workerCode) {
		LogUtil.log("delete SysCCurrency instance", Level.INFO, null);
		try {
			SysCCurrency result = this.findById(id);
			result.setLastModifiedBy(workerCode);
			result.setLastModifiedDate(new Date());
			result.setIsUse("N");
			entityManager.merge(result);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 查找币种名称是否已经存在
	 * 
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public SysCCurrency findByName(String currencyName) {
		LogUtil.log("finding SysCCurrency instance with currencyName: " + currencyName, Level.INFO,
				null);
		String sql = "select * from sys_c_currency t\n"
			 + "where t.is_use = 'Y'\n"
			 + "and t.currency_Name = '"+currencyName+ "'";
		try {
			List<SysCCurrency> list = bll.queryByNativeSQL(sql,
					SysCCurrency.class);
			if(list.size() != 0){
				SysCCurrency instance = list.get(0);
				return instance;
			} else {
				return null;
			}
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查找币种编码是否已经存在
	 * 
	 * @return SysCCurrency
	 */
	// 检查类型编码是否已经存在
	@SuppressWarnings("unchecked")
	public SysCCurrency findByNo(String currencyNo) {
		LogUtil.log("finding SysCCurrency instance with currencyName: " + currencyNo, Level.INFO,
				null);
		String sql = "select * from sys_c_currency t\n"
			 + "where t.is_use = 'Y'\n"
			 + "and t.currency_No = ?";
		sql += " order by t.currency_No"; 
		try {
			List<SysCCurrency> list = bll.queryByNativeSQL(sql, new Object[]{currencyNo},SysCCurrency.class);
			if(list.size()!= 0){
				SysCCurrency instance = list.get(0);
				return instance;
			} else {
				return null;
			}
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 更新币种信息
	 * 
	 * @return void
	 * @throws CodeRepeatException 
	 */
	public int updateAdd(SysCCurrency entity) throws CodeRepeatException {
		LogUtil.log("saving SysCCurrency instance", Level.INFO, null);
			SysCCurrency instance = this.findByNo(entity.getCurrencyNo());
			if(instance != null) {
				if(!instance.getCurrencyId().equals(entity.getCurrencyId())) {
					return 0;
				}
			}
			instance = this.findByName(entity.getCurrencyName());
			if(instance != null) {
				if(!instance.getCurrencyId().equals(entity.getCurrencyId())) {
					//throw new CodeRepeatException("名称已存在。请重新输入。");
					return 1;
				}
			}
			this.update(entity);
			return 2;
	}
	
	/**
     * 增加一条记录
     *
     * @return void
     * @param entity 要增加的记录
     * @throws CodeRepeatException
     */
    public int insert(SysCCurrency entity) throws CodeRepeatException {
        LogUtil.log("saving SysCCurrency instance", Level.INFO, null);
            // 编号和名称是否唯一
        	SysCCurrency instance = this.findByNo(entity.getCurrencyNo());
			if(instance != null) {
//					throw new CodeRepeatException("编码已存在。请重新输入。");
				return 0;
			}
			instance = this.findByName(entity.getCurrencyName());
			if(instance != null) {
//					throw new CodeRepeatException("名称已存在。请重新输入。");
				return 1;
			}
        
            entity.setCurrencyId(bll.getMaxId("sys_c_currency",
                        "CURRENCY_ID"));
            // 设定修改时间
            entity.setLastModifiedDate(new java.util.Date());
            // 设定是否使用
            entity.setIsUse("Y");
            // 保存
            this.save(entity);
            return 2;
    }
    /**
     * 模糊查询币别信息
     * 
     * @param 
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
	public  PageObject findAllAdd(String enterpriseCode, final int... rowStartIdxAndCount ) {
    	LogUtil.log("finding all SysCCurrency instances", Level.INFO, null);
		try {
            PageObject result = new PageObject();
            // 查询sql
            String sql=
              "select t.* from sys_c_currency t\n" +
              "where  t.is_use='Y' \n" +
              "and t.ENTERPRISE_CODE = '"+enterpriseCode+"' \n" +
              "order by t.currency_No";
            String sqlCount = "select count(CURRENCY_NO) \n"
				+ "  from sys_c_currency t where t.is_use='Y' ";
            List<SysCCurrency> list=bll.queryByNativeSQL(sql,SysCCurrency.class,rowStartIdxAndCount);
            Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
			
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
    }
}