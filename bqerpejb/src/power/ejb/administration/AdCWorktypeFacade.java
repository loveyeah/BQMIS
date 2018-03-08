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
 * 工作类别维护Facade
 * 
 * @author chaihao
 * 
 */
@Stateless
public class AdCWorktypeFacade implements AdCWorktypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增工作类别维护
	 * 
	 * @param entity 新增工作类别维护实体
	 */
	public void save(AdCWorktype entity) {
		LogUtil.log("EJB:新增定期工作类别开始", Level.INFO, null);
		try {
			// 设置新增工作类别序号
			entity.setId(bll.getMaxId("AD_C_WORKTYPE", "id"));
			entityManager.persist(entity);
			LogUtil.log("EJB:新增定期工作类别结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:新增失败", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除工作类别维护
	 * 
	 * @param entity
	 */
	public void delete(AdCWorktype entity) {
		LogUtil.log("EJB:删除定期工作类别开始", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdCWorktype.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("EJB:删除定期工作类别结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:删除失败", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新工作类别维护
	 * 
	 * @param entity 更新工作类别维护实体
	 * @return
	 */
	public AdCWorktype update(AdCWorktype entity) throws SQLException {
		LogUtil.log("EJB:更新工作类别维护开始", Level.INFO, null);
		try {
			AdCWorktype result = entityManager.merge(entity);
			LogUtil.log("EJB:更新工作类别维护结束", Level.INFO, null);
			return result;
		} catch (Exception re) {
			LogUtil.log("EJB:更新失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * 按序号查找工作类别维护
	 * 
	 * @param id 序号
	 * @return
	 */
	public AdCWorktype findById(Long id) throws SQLException {
		LogUtil.log("EJB:按序号查找定期工作类别开始 id: " + id, Level.INFO,
				null);
		try {
			AdCWorktype instance = entityManager.find(AdCWorktype.class, id);
			LogUtil.log("EJB:按序号查找定期工作类别结束", Level.INFO, null);
			return instance;
		} catch (Exception re) {
			LogUtil.log("EJB:查找失败", Level.SEVERE, re);
			throw new SQLException();
		}
	}

	/**
	 * 按指定属性查找工作类别维护
	 * 
	 * @param strValue 属性值
	 * @param strEnterpriseCode 企业代码
	 * @param rowStartIdxAndCount 检索数据附加参数
	 * @return PageObject 检索结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject findByProperty(String strValue, String strEnterpriseCode,
			int... rowStartIdxAndCount) throws SQLException {
		LogUtil.log("EJB:按属性查找定期工作类别开始 worktype_code: " + strValue, Level.INFO, null);
		try {
			// 查询SQL语句
			String strSql = "";
			// 查询行数SQL语句
			String strSqlCount = "";
			// 需要返回的结果
			PageObject result = new PageObject();
			// 构造查询SQL语句
			strSql = "SELECT "
				+ "A.ID,"
				+ "A.WORKTYPE_CODE,"
				+ "A.WORKTYPE_NAME,"
				+ "A.SUB_WORKTYPE_CODE,"
				+ "A.SUB_WORKTYPE_NAME,"
				+ "A.IS_USE,"
				+ "A.RETRIEVE_CODE,"
				+ "A.UPDATE_USER,"
				+ "A.UPDATE_TIME,"
				+ "A.ENTERPRISE_CODE "
				+ "FROM AD_C_WORKTYPE A "
		        + "WHERE A.IS_USE=? "
		        + "AND A.ENTERPRISE_CODE=?";
			// 构造查询行数SQL语句
			strSqlCount = "SELECT "
				+ "COUNT(A.ID) "
				+ "FROM AD_C_WORKTYPE A "
			    + "WHERE A.IS_USE=? "
			    + "AND A.ENTERPRISE_CODE=?";
			// SQL语句参数
			List lstParams = new ArrayList();
			lstParams.add("Y");
			lstParams.add(strEnterpriseCode);
			// 参数是否为空
			if (strValue != null && strValue.length() > 0) {
				strSql += " AND A.WORKTYPE_CODE=?";
				strSqlCount += " AND A.WORKTYPE_CODE=?";
				lstParams.add(strValue);
			}
			Object[] params = lstParams.toArray();
			LogUtil.log("EJB:strSql=" + strSql, Level.INFO, null);
			// 行数
			Long lngTotalCount=Long.parseLong(bll.getSingal(strSqlCount, params).toString());
			LogUtil.log("EJB:strSqlCount=" + strSqlCount, Level.INFO, null);
			// 执行查询
			List<AdCWorktype> list = bll.queryByNativeSQL(strSql, params, AdCWorktype.class, rowStartIdxAndCount);
			// 设置查询结果集
			result.setList(list);
			// 设置行数
			result.setTotalCount(lngTotalCount);
			LogUtil.log("EJB:按属性查找定期工作类别结束", Level.INFO, null);
			// 返回查询结果
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:按属性查找失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 查找所有内容
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AdCWorktype> findAll() {
		LogUtil.log("EJB:查找所有内容开始", Level.INFO, null);
		try {
			final String queryString = "select model from AdCWorktype model";
			Query query = entityManager.createQuery(queryString);
			LogUtil.log("EJB:查找所有内容结束", Level.INFO, null);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("EJB:查找所有内容失败", Level.SEVERE, re);
			throw re;
		}
	}

}