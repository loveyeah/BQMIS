/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 进出车辆登记AdCPaperFacade
 * @author li chensheng
 *  
 */
@Stateless
public class AdCPaperFacade implements AdCPaperFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * Perform an initial save of a previously unsaved AdCPaper entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdCPaper entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdCPaper entity) throws SQLException{
		LogUtil.log("EJB：保存证件类别信息开始。", Level.INFO, null);
		try {
			Long id = bll.getMaxId("AD_C_PAPER", "id");
			entity.setId(id);
			entity.setUpdateTime(new Date());
			entityManager.persist(entity);
			LogUtil.log("EJB：保存证件类别信息成功", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("EJB：保存证件类别信息失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent AdCPaper entity.
	 * 
	 * @param entity
	 *            AdCPaper entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdCPaper entity) {
		LogUtil.log("deleting AdCPaper instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdCPaper.class, entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved AdCPaper entity and return it or a copy of it
	 * to the sender. A copy of the AdCPaper entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdCPaper entity to update
	 * @return AdCPaper the persisted AdCPaper entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdCPaper update(AdCPaper entity) throws SQLException {
		LogUtil.log("EJB：更新证件类别信息开始。", Level.INFO, null);
		try {
			entity.setUpdateTime(new Date());
			AdCPaper result = entityManager.merge(entity);
			LogUtil.log("EJB：更新证件类别信息结束。", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB：更新证件类别信息失败", Level.SEVERE, e);
			throw  new SQLException();
		}
	}

	public AdCPaper findById(Long id) {
		LogUtil.log("finding AdCPaper instance with id: " + id, Level.INFO,
				null);
		try {
			AdCPaper instance = entityManager.find(AdCPaper.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdCPaper entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdCPaper property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdCPaper> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdCPaper> findByProperty(String propertyName, final Object value) {
		LogUtil.log("finding AdCPaper instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdCPaper model where model."
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
	 * Find all AdCPaper entities.
	 * 
	 * @return List<AdCPaper> all AdCPaper entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdCPaper> findAll() {
		LogUtil.log("finding all AdCPaper instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdCPaper model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
     * 查询所有证件类别信息
     * 
     * @param 
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
	public  PageObject findAllPaper(String enterpriseCode,
			final int... rowStartIdxAndCount ) throws SQLException{
    	LogUtil.log("EJB：取得证件类别信息所有值开始。", Level.INFO, null);
		try {
			PageObject  result = new PageObject();
			// SQL语句参数
			Object[] params = new Object[2];
			params[0] = "Y";
			params[1] = enterpriseCode;
            // 查询sql
            String sql = "SELECT "
            	         + "T.ID, "
            	         + "T.PAPERTYPE_CODE, " 
            	         + "T.PAPERTYPE_NAME, "
            	         + "T.RETRIEVE_CODE, "
            	         + "T.IS_USE, "
            	         + "T.UPDATE_USER, "
            	         + "T.UPDATE_TIME, "
            	         + "T.ENTERPRISE_CODE "
            	         + "FROM AD_C_PAPER T " 
            	         + "WHERE T.IS_USE = ? "
           	             + "AND T.ENTERPRISE_CODE = ? ";
            String sqlCount = "SELECT "
   	                          + "COUNT(1) "
	                          + "FROM AD_C_PAPER T " 
	                          + "WHERE T.IS_USE = ? "
	            	          + "AND T.ENTERPRISE_CODE = ? ";
            List<AdCPaper> list=bll.queryByNativeSQL(sql,params,AdCPaper.class,rowStartIdxAndCount);
            Long totalCount=Long.parseLong(bll.getSingal(sqlCount,params).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			LogUtil.log("EJB：取得证件类别信息所有值结束。", Level.INFO, null);
			return result;			
		} catch (Exception e) { 
			LogUtil.log("EJB：查找证件类别信息失败", Level.SEVERE, e);
			throw new SQLException();
        }
    }
    /**
	 * 逻辑删除一条证件类别信息.
	 * 
	 * @param entity
	 *            AdCPaper entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void logicDelete(AdCPaper entity)throws SQLException {
		LogUtil.log("EJB：删除证件类别信息开始。", Level.INFO, null);
		try {
			AdCPaper objDelEntity = this.findById(entity.getId());
			objDelEntity.setUpdateUser(entity.getUpdateUser());
			objDelEntity.setIsUse("N");
			objDelEntity.setUpdateTime(new Date());
			entityManager.persist(objDelEntity);
			LogUtil.log("EJB：删除证件类别信息成功", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("EJB：删除证件类别信息失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 获取证件类别编码
	 * 
	 * @return String
	 * @throws SQLException
	 */
	public String getPaperTypeCode() throws SQLException {
		LogUtil.log("EJB：获取证件类别编码开始。", Level.INFO, null);
		try {
			Long lngPaperTypeCode = bll.getMaxId("AD_C_PAPER", "PAPERTYPE_CODE");
			String strPaperTypeCode = String.valueOf(lngPaperTypeCode);
			LogUtil.log("EJB：获取证件类别编码成功", Level.INFO, null);
			return strPaperTypeCode;
		} catch (Exception e) {
			LogUtil.log("EJB：获取证件类别编码失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

}