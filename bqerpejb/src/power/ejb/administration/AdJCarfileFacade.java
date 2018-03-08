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
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJCarfile.
 * 
 * @see power.ejb.administration.AdJCarfile
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class AdJCarfileFacade implements AdJCarfileFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 保存车辆档案
	 * 
	 * @param entity 车辆档案实体
	 * @throws SQLException
	 */
	public void save(AdJCarfile entity) throws SQLException {
		LogUtil.log("EJB:保存车辆档案开始", Level.INFO, null);
		try {
			// 设置序号
			entity.setId(bll.getMaxId("AD_J_CARFILE", "ID"));
			entityManager.persist(entity);
			LogUtil.log("EJB:保存车辆档案结束", Level.INFO, null);
		} catch (Exception re) {
			LogUtil.log("EJB:保存失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Delete a persistent AdJCarfile entity.
	 * 
	 * @param entity
	 *            AdJCarfile entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJCarfile entity) {
		LogUtil.log("deleting AdJCarfile instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJCarfile.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新车辆档案
	 * 
	 * @param entity 车辆档案实体
	 * @return 车辆档案实体
	 * @throws SQLException
	 */
	public AdJCarfile update(AdJCarfile entity) throws SQLException {
		LogUtil.log("EJB:更新车辆档案开始", Level.INFO, null);
		try {
			AdJCarfile result = entityManager.merge(entity);
			LogUtil.log("EJB:更新车辆档案结束", Level.INFO, null);
			return result;
		} catch (Exception re) {
			LogUtil.log("EJB:更新失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * 更新车辆档案(要求排他)
	 * @author xstan
	 * @param entity 车辆档案实体
	 * @param strUpdateTime 车辆档案实体取得时的更新时间
	 * @return 车辆档案实体
	 * @throws SQLException
	 * @throws DataChangeException
	 */
	public AdJCarfile update(AdJCarfile entity, Long updateTime) throws SQLException, DataChangeException {
		LogUtil.log("EJB:更新车辆档案开始", Level.INFO, null);
		try {
			AdJCarfile objOld = this.findById(entity.getId());
			// 取得DB中原数据的更新时间
			Long lngUpdateTimeOld = objOld.getUpdateTime().getTime();
			// 排他处理
			if (!lngUpdateTimeOld.equals(updateTime)) {
				throw new DataChangeException("");
			}
			// 设置更新时间
			entity.setUpdateTime(new Date());
			AdJCarfile result = entityManager.merge(entity);
			LogUtil.log("EJB:更新车辆档案结束", Level.INFO, null);
			return result;
		} catch (DataChangeException de) {
			LogUtil.log("EJB：更新失败", Level.SEVERE, de);
			throw de;
		}  catch (Exception re) {
			LogUtil.log("EJB:更新失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * 按序号查找车辆档案
	 * 
	 * @param id 序号
	 * @return 车辆档案实体
	 * @throws SQLException
	 */
	public AdJCarfile findById(Long id) throws SQLException {
		LogUtil.log("EJB:按序号查找车辆档案开始 序号=" + id, Level.INFO, null);
		try {
			AdJCarfile instance = entityManager.find(AdJCarfile.class, id);
			LogUtil.log("EJB:按序号查找车辆档案结束", Level.INFO, null);
			return instance;
		} catch (Exception re) {
			LogUtil.log("EJB:查找失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * Find all AdJCarfile entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJCarfile property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJCarfile> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJCarfile> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJCarfile instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJCarfile model where model."
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
	 * Find all AdJCarfile entities.
	 * 
	 * @return List<AdJCarfile> all AdJCarfile entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJCarfile> findAll() {
		LogUtil.log("finding all AdJCarfile instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJCarfile model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 按车牌号查找车辆档案
	 * @author xstan 
	 * @param strCarNo 车牌号
	 * @return 车辆档案实体
	 * @throws SQLException
	 */
	public AdJCarfile findByCarNo(String strCarNo) throws SQLException {
		LogUtil.log("EJB:按车牌号查找车辆档案开始 车牌号=" + strCarNo, Level.INFO, null);
		try {
			String strSql = "SELECT * FROM AD_J_CARFILE WHERE CAR_NO = ? ";
			Object[] params = new Object[1];
			params[1] = strCarNo;
			AdJCarfile objRes = (AdJCarfile)bll.getSingal(strSql, params);
			LogUtil.log("EJB:按车牌号查找车辆档案结束", Level.INFO, null);
			return objRes;
		} catch (Exception re) {
			LogUtil.log("EJB:查找失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * 车辆档案是否重复检查
	 * 
	 * @param strCarNo 车牌号
	 * @param strEnterpriseCode 企业代码
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean checkCar(String strCarNo, String strEnterpriseCode)
			throws SQLException {
		try {
			LogUtil.log("EJB:车辆档案重复检查开始", Level.INFO, null);
			// 是否重复标志
			boolean isOnly = false;
			// 查询SQL语句
			String strSql = "";
			// 构造查询SQL语句
			strSql = "SELECT "
				+ "COUNT(A.ID) "
				+ "FROM AD_J_CARFILE A "
				+ "WHERE "
				+ "A.CAR_NO=? "
				+ "AND A.IS_USE=? "
				+ "AND A.ENTERPRISE_CODE=?";
			// SQL语句参数
			Object[] params = new Object[3];
			params[0] = strCarNo;
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
			LogUtil.log("EJB:车辆档案重复检查结束", Level.INFO, null);
			return isOnly;
		} catch (Exception re) {
			LogUtil.log("EJB:车辆档案重复检查败", Level.SEVERE, re);
			throw new SQLException();
		}
	}
}