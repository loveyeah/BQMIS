/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration;

import java.sql.SQLException;
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
 * Facade for entity AdJDriverfile.
 * 
 * @see power.ejb.administration.AdJDriverfile
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJDriverfileFacade implements AdJDriverfileFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 保存司机档案
	 * 
	 * @param entity 司机实体
	 */
	public void save(AdJDriverfile entity) throws SQLException {
		LogUtil.log("EJB:新增司机实体开始", Level.INFO, null);
		try {
			entity.setId(bll.getMaxId("AD_J_DRIVERFILE", "ID"));
			entityManager.persist(entity);
			LogUtil.log("EJB:新增司机实体结束", Level.INFO, null);
		} catch (Exception e){
			LogUtil.log("EJB:新增失败", Level.SEVERE, null);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent AdJDriverfile entity.
	 * 
	 * @param entity
	 *            AdJDriverfile entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJDriverfile entity) {
		LogUtil.log("deleting AdJDriverfile instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJDriverfile.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新司机档案
	 * 
	 * @param entity 司机实体
	 * @return 司机实体
	 */
	public AdJDriverfile update(AdJDriverfile entity) throws SQLException {
		LogUtil.log("EJB:更新司机实体开始", Level.INFO, null);
		try {
			AdJDriverfile result = entityManager.merge(entity);
			LogUtil.log("EJB:更新司机实体结束", Level.INFO, null);
			return result;
		} catch (Exception e){
			LogUtil.log("EJB:更新失败", Level.SEVERE, null);
			throw new SQLException();
		}
	}

	/**
	 * 按序号查找司机实体
	 * 
	 * @param id 序号
	 * @return 司机实体
	 */
	public AdJDriverfile findById(Long id) throws SQLException {
		LogUtil.log("EJB:按序号查找司机实体开始 id=" + id, Level.INFO, null);
		try {
			AdJDriverfile instance = entityManager
					.find(AdJDriverfile.class, id);
			LogUtil.log("EJB:按序号查找司机实体结束", Level.INFO, null);
			return instance;
		} catch (Exception e){
			LogUtil.log("EJB:查找失败", Level.SEVERE, null);
			throw new SQLException();
		}
	}

	/**
	 * Find all AdJDriverfile entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJDriverfile property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJDriverfile> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJDriverfile> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJDriverfile instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJDriverfile model where model."
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
	 * Find all AdJDriverfile entities.
	 * 
	 * @return List<AdJDriverfile> all AdJDriverfile entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJDriverfile> findAll() {
		LogUtil.log("finding all AdJDriverfile instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJDriverfile model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 司机档案是否重复检查
	 * 
	 * @param strDriverCode 司机编码
	 * @param strEnterpriseCode 企业代码
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean checkDriver(String strDriverCode, String strEnterpriseCode) throws SQLException {
		try {
			LogUtil.log("EJB:司机档案重复检查开始", Level.INFO, null);
			// 是否重复标志
			boolean isOnly = false;
			// 查询SQL语句
			String strSql = "";
			// 构造查询SQL语句
			strSql = "SELECT "
				+ "COUNT(A.ID) "
				+ "FROM AD_J_DRIVERFILE A "
				+ "WHERE "
				+ "A.DRIVER_CODE=? "
				+ "AND A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE=?";
			// SQL语句参数
			Object[] params = new Object[3];
			params[0] = strDriverCode;
			params[1] = "Y";
			params[2] = strEnterpriseCode;
			LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			// 执行查询
			Long lngTotalCount = Long.parseLong(bll.getSingal(strSql, params)
					.toString());
			// 是否存在记录
			if (lngTotalCount == 0) {
				isOnly = true;
			}
			LogUtil.log("EJB:司机档案重复检查结束", Level.INFO, null);
			return isOnly;
		} catch (Exception re) {
			LogUtil.log("EJB:司机档案重复检查败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

}