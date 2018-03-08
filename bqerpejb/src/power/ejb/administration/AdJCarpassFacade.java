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

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;


/**
 * 进出车辆登记AdJCarpassFacade
 * @author li chensheng
 *  
 */
@Stateless
public class AdJCarpassFacade implements AdJCarpassFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved AdJCarpass entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJCarpass entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJCarpass entity) {
		LogUtil.log("保存进出车辆信息开始。", Level.INFO, null);
		try {
			// 序号的取得
			Long id = bll.getMaxId("AD_J_CARPASS", "id");
			entity.setId(id);
			entity.setUpdateTime(new Date());
			entityManager.persist(entity);
			LogUtil.log("保存成功", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("保存失败", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent AdJCarpass entity.
	 * 
	 * @param entity
	 *            AdJCarpass entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Long id, String strEmployee,String updateTime) throws SQLException, DataChangeException{
		LogUtil.log("删除进出车辆信息开始。", Level.INFO, null);
		try {
			// 取得原数据
			AdJCarpass objBean = this.findById(id);
			String dbUpdateTime = objBean.getUpdateTime().toString().substring(0, 19);
			if (!updateTime.equals(dbUpdateTime)) {
				throw new DataChangeException("");
			}
			objBean.setUpdateTime(new Date());
			objBean.setUpdateUser(strEmployee);
			objBean.setIsUse("N");
			entityManager.merge(objBean);
			LogUtil.log("删除成功", Level.INFO, null);
		} catch (DataChangeException de) {
			LogUtil.log("EJB：删除进出车辆信息失败", Level.SEVERE, de);
			throw de;
		} catch (Exception e) {
			LogUtil.log("EJB：删除进出车辆信息失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * Persist a previously saved AdJCarpass entity and return it or a copy of
	 * it to the sender. A copy of the AdJCarpass entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJCarpass entity to update
	 * @return AdJCarpass the persisted AdJCarpass entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJCarpass update(AdJCarpass entity,String updateTime) throws SQLException, DataChangeException {
		LogUtil.log("更新进出车辆信息开始。", Level.INFO, null);
		try {
			AdJCarpass adjCarBean = this.findById(entity.getId());
			// 取得DB中原数据的更新时间
			String dbUpdateTime = adjCarBean.getUpdateTime().toString()
            .substring(0, 19);
			// 排他处理
			if (!dbUpdateTime.equals(updateTime)) {
				throw new DataChangeException("");
			}
			entity.setUpdateTime(new Date());
			AdJCarpass result = entityManager.merge(entity);
			LogUtil.log("更新进出车辆信息结束。", Level.INFO, null);
			return result;
		} catch (DataChangeException de) {
			LogUtil.log("EJB：更新进出车辆信息失败", Level.SEVERE, de);
			throw de;
		} catch (Exception e) {
			LogUtil.log("EJB：更新进出车辆信息失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	public AdJCarpass findById(Long id) {
		LogUtil.log("finding AdJCarpass instance with id: " + id, Level.INFO,
				null);
		try {
			AdJCarpass instance = entityManager.find(AdJCarpass.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJCarpass entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJCarpass property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJCarpass> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJCarpass> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJCarpass instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJCarpass model where model."
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
	 * Find all AdJCarpass entities.
	 * 
	 * @return List<AdJCarpass> all AdJCarpass entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJCarpass> findAll() {
		LogUtil.log("finding all AdJCarpass instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJCarpass model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
     * 查询一个月内进出车辆信息
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
	public  PageObject findAllCar(String enterpriseCode,final int... rowStartIdxAndCount ) throws SQLException {
    	LogUtil.log("取得所有值开始。", Level.INFO, null);
		try { 
            PageObject  result = new PageObject();
         // SQL语句参数
			Object[] params = new Object[2];
			params[0] = "Y";
			params[1] = enterpriseCode;
            // 查询sql
            String strSql="SELECT "
            	          + "T.ID, "
            	          + "T.PASSCODE, "
            	          + "T.PASSTIME, "
            	          + "T.CAR_NO, "
            	          + "T.PAPER_ID, "
            	          + "T.FIRM, "
            	          + "T.PREMAN, "
            	          + "T.PAPERTYPE_CD, "
            	          + "T.GIVE_DATE, "
            	          + "T.POSTMAN, "
            	          + "T.IS_USE, "
            	          + "T.CRT_USER, "
            	          + "T.DCM_STATUS, "
            	          + "T.UPDATE_USER, "
            	          + "T.UPDATE_TIME, "
            	          + "T.ENTERPRISE_CODE "
            	          + "FROM AD_J_CARPASS T "
            	          + "WHERE T.PASSTIME >= SYSDATE - 30 "
            	          + "AND T.IS_USE = ? "
            	          + "AND T.ENTERPRISE_CODE = ? ";
             String sqlCount = "SELECT "
            	             + "COUNT(1) "
            	             + "FROM AD_J_CARPASS T "
            	             + "WHERE T.PASSTIME >= SYSDATE - 30 "
            	             + "AND T.IS_USE = ? "
               	             + "AND T.ENTERPRISE_CODE = ? ";
	        LogUtil.log("SQL=" + strSql, Level.INFO, null);
	        LogUtil.log("SQL=" + sqlCount, Level.INFO, null);
			List<AdJCarpass> list = bll.queryByNativeSQL(strSql,params,AdJCarpass.class,rowStartIdxAndCount);
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount, params).toString());
            result = new PageObject();
			result.setList(list);
			result.setTotalCount(totalCount);
			LogUtil.log("取得所有值结束。", Level.INFO, null);
			// 返回查询结果          	
			return result;
		} catch (Exception e) { 
			LogUtil.log("查找失败", Level.SEVERE, e);
			throw new SQLException();
        }
    }
}