/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration;

import java.sql.SQLException;
import java.util.ArrayList;
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
 * 值别维护Remote
 * @author chaihao
 * 
 */
@Stateless
public class AdCDutytypeFacade implements AdCDutytypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增值别
	 * 
	 * @param entity 新增值别实体
	 */
	public void save(AdCDutytype entity) throws SQLException {
		LogUtil.log("EJB:新增值别开始", Level.INFO, null);
		try {
			// 设置新增值别序号
			entity.setId(bll.getMaxId("AD_C_DUTYTYPE", "ID"));
			entityManager.persist(entity);
			LogUtil.log("EJB:新增值别结束", Level.INFO, null);
		} catch (Exception re) {
			LogUtil.log("EJB:新增失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * 删除值别
	 * 
	 * @param entity
	 */
	public void delete(AdCDutytype entity) {
		LogUtil.log("EJB:删除值别开始", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdCDutytype.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("EJB:删除值别结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:删除失败", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新值别
	 * 
	 * @param entity 更新值别实体
	 * @return
	 */
	public AdCDutytype update(AdCDutytype entity) throws SQLException {
		LogUtil.log("EJB:更新值别开始", Level.INFO, null);
		try {
			AdCDutytype result = entityManager.merge(entity);
			LogUtil.log("EJB:更新值别结束", Level.INFO, null);
			return result;
		} catch (Exception re) {
			LogUtil.log("EJB:新增失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * 按序号查找值别
	 * 
	 * @param id 序号
	 * @return
	 */
	public AdCDutytype findById(Long id) throws SQLException {
		LogUtil.log("EJB:按序号查找值别开始 id: " + id, Level.INFO,
				null);
		try {
			AdCDutytype instance = entityManager.find(AdCDutytype.class, id);
			LogUtil.log("EJB:按序号查找值别结束 id: " + id, Level.INFO, null);
			return instance;
		} catch (Exception re) {
			LogUtil.log("EJB:新增失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * 按指定属性查找值别
	 * 
	 * @param strValue 属性值
	 * @param rowStartIdxAndCount 检索数据附加参数
	 * @return PageObject 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByProperty(final String strValue, String strEnterpriseCode, int... rowStartIdxAndCount) throws SQLException {
		LogUtil.log("EJB:按属性查找值别开始 WORKTYPE_CODE: " + strValue, Level.INFO, null);
		try {
			// 查询SQL文
			String strSql = "";
			// 查询行数SQL文
			String strSqlCount = "";
			// 需要返回的结果
			PageObject result = new PageObject();
			// 构造查询SQL文
			strSql = "SELECT "
				    + "A.ID,"
				    + "A.WORKTYPE_CODE,"
				    + "A.DUTY_TYPE,"
				    + "A.DUTY_TYPE_NAME,"
				    + "A.START_TIME,"
				    + "A.END_TIME,"
				    + "A.IS_USE,"
				    + "A.UPDATE_USER,"
				    + "A.UPDATE_TIME,"
				    + "A.ENTERPRISE_CODE "
					+ "FROM AD_C_DUTYTYPE A "
					+ "WHERE "
					+ "A.ENTERPRISE_CODE=? "
					+ "AND A.WORKTYPE_CODE=? "
					+ "AND A.IS_USE=?";
			// 构造查询行数SQL文
			strSqlCount = "SELECT "
			        + "COUNT(ID) "
				    + "FROM AD_C_DUTYTYPE A "
				    + "WHERE "
				    + "A.ENTERPRISE_CODE=? "
				    + "AND A.WORKTYPE_CODE=? "
					+ "AND A.IS_USE=?";
			List lstParams = new ArrayList();
			lstParams.add(strEnterpriseCode);
			lstParams.add(strValue);
			lstParams.add("Y");
			Object[] params = lstParams.toArray();
			LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			// 行数
			Long lngTotalCount=Long.parseLong(bll.getSingal(strSqlCount, params).toString());
			// 执行查询
			List<AdCDutytype> list = bll.queryByNativeSQL(strSql, params,
					AdCDutytype.class, rowStartIdxAndCount);

			// 设置查询结果集
			result.setList(list);
			// 设置行数
			result.setTotalCount(lngTotalCount);
			LogUtil.log("EJB:按属性查找值别维护结束", Level.INFO, null);
			// 返回查询结果
			return result;
		} catch (Exception re) {
			LogUtil.log("EJB:查找失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * finding all
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AdCDutytype> findAll() {
		LogUtil.log("finding all AdCDutytype instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdCDutytype model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}