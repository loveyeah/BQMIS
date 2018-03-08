package power.ejb.administration;

import java.sql.SQLException;
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

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdCCarwhPay.
 * 
 * @see power.ejb.administration.AdCCarwhPay
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdCCarwhPayFacade implements AdCCarwhPayFacadeRemote {
	// property constants
	public static final String PAY_CODE = "payCode";
	public static final String PAY_NAME = "payName";
	public static final String IS_USE = "isUse";
	public static final String UPDATE_USER = "updateUser";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved AdCCarwhPay entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdCCarwhPay entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdCCarwhPay entity) throws SQLException{
		LogUtil.log("EJB:维修项目维护开始", Level.INFO, null);
		try {
			entity.setId(bll.getMaxId("AD_C_CARWH_PAY", "ID"));
			String code = bll.getMaxId("AD_C_CARWH_PAY", "PAY_CODE").toString();
			entity.setIsUse("Y");
		    if(code.length() <= 1){
		    	 entity.setPayCode("0"+code);
		    }else{
		    	entity.setPayCode(code);	
		    }
			entity.setUpdateTime(new Date());
			entityManager.merge(entity);
			
			LogUtil.log("EJB:维修项目维护结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:维修项目维护失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent AdCCarwhPay entity.
	 * 
	 * @param entity
	 *            AdCCarwhPay entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(AdCCarwhPay entity) throws SQLException{
		LogUtil.log("EJB:删除费用维护项目开始", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			Object[] params = new Object[2];
			params[0] = 'N';
			params[1] = entity.getPayCode();
			String delString = "UPDATE AD_C_CARWH_PRO t "
						+ " SET t.IS_USE = ? "
						+ " WHERE t.PAY_CODE = ?";
			//逻辑删除
			entity.setIsUse("N");
			entity.setUpdateTime(new Date());
			entityManager.merge(entity);
			int a = bll.exeNativeSQL(delString,params);
			LogUtil.log("删除费用维护项目结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("删除费用维护项目失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Persist a previously saved AdCCarwhPay entity and return it or a copy of
	 * it to the sender. A copy of the AdCCarwhPay entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdCCarwhPay entity to update
	 * @return AdCCarwhPay the persisted AdCCarwhPay entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdCCarwhPay update(AdCCarwhPay entity) throws SQLException {
		LogUtil.log("EJB:更新费用类别开始", Level.INFO, null);
		try {
			entity.setIsUse("Y");
			entity.setUpdateTime(new Date());
			AdCCarwhPay result = entityManager.merge(entity);
			LogUtil.log("EJB:更新费用类别结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:更新费用类别失败", Level.INFO, null);
			throw new SQLException();
		}
	}

	public AdCCarwhPay findById(Long id) {
		LogUtil.log("finding AdCCarwhPay instance with id: " + id, Level.INFO,
				null);
		try {
			AdCCarwhPay instance = entityManager.find(AdCCarwhPay.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdCCarwhPay entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdCCarwhPay property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdCCarwhPay> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdCCarwhPay> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdCCarwhPay instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdCCarwhPay model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<AdCCarwhPay> findByPayCode(Object payCode) {
		return findByProperty(PAY_CODE, payCode);
	}

	public List<AdCCarwhPay> findByPayName(Object payName) {
		return findByProperty(PAY_NAME, payName);
	}

	public List<AdCCarwhPay> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<AdCCarwhPay> findByUpdateUser(Object updateUser) {
		return findByProperty(UPDATE_USER, updateUser);
	}

	/**
	 * Find all AdCCarwhPay entities.
	 * 
	 * @return List<AdCCarwhPay> all AdCCarwhPay entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,final int... rowStartIdxAndCount) throws SQLException{
		LogUtil.log("EJB:查找所有费用类别开始", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			Object[] params = new Object[2];
			params[0] = 'Y';
			params[1] = enterpriseCode;
            // 查询sql
            String sql= "SELECT * "
            		+ " FROM AD_C_CARWH_PAY t"
            		+ " WHERE t.IS_USE=? "
            		+ " and  t.ENTERPRISE_CODE=? "
            		+ " ORDER BY t.PAY_CODE";
            String sqlCount = "SELECT COUNT(PAY_CODE)"
            		+ " FROM AD_C_CARWH_PAY t "
            		+ " WHERE t.IS_USE=? "
            		+ " and  t.enterprise_code=? ";
            // 执行查询
            List<AdCCarwhPay> list = bll.queryByNativeSQL(sql,params, AdCCarwhPay.class,rowStartIdxAndCount);
            Long totalCount = Long.parseLong(bll.getSingal(sqlCount,params)
					.toString());
            // 设置PageObject
            result.setList(list);
            result.setTotalCount(totalCount);
            LogUtil.log("EJB:查找所有费用类别结束", Level.INFO, null);
            // 返回
            return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:查找所有费用类别失败", Level.INFO, re);
			throw new SQLException();
		}
	}

	/**
	 * Find all AdCCarwhPay entities.
	 * 
	 * @return List<AdCCarwhPay> all AdCCarwhPay entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllData(String enterpriseCode) throws SQLException{
		LogUtil.log("EJB:查找所有费用类别显示数据开始", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			Object[] params = new Object[1];
			params[0] = enterpriseCode;
            // 查询sql
            String sql=
                "SELECT * FROM AD_C_CARWH_PAY t where t.enterprise_code=?";
            // 执行查询
            List<AdCCarwhPay> list = bll.queryByNativeSQL(sql,params, AdCCarwhPay.class);
            // 设置PageObject
            result.setList(list);
            result.setTotalCount((long)(list.size()));
            LogUtil.log("EJB:查找所有费用类别显示数据结束", Level.INFO, null);
            // 返回
            return result;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:查找所有费用类别显示数据失败", Level.INFO, re);
			throw new SQLException();
		}
	}
}