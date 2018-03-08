/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;

import java.sql.SQLException;
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

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.RestaurantPlanInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJRestaurantPlan.
 * 
 * @see power.ejb.administration.AdJRestaurantPlan
 * @author sufeiyu
 */
@Stateless
public class AdJRestaurantPlanFacade implements AdJRestaurantPlanFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 增加餐厅计划
	 * @author sufeiyu
	 * @param entity
	 *            餐厅计划 entity 
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJRestaurantPlan entity) throws SQLException {
		LogUtil.log("EJB：餐厅计划数据增加操作开始", Level.INFO, null);
		try {
			entity.setUpdateTime(new Date());
			entityManager.persist(entity);
			LogUtil.log("EJB：餐厅计划数据增加操作正常结束", Level.INFO, null);
		} catch (Exception re) {
			LogUtil.log("EJB：餐厅计划数据增加异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	/**
	 * 删除一条餐厅计划
	 * @author sufeiyu
	 * @param strEmployee 修改人
	 * @param lngId 序号
	 * @param strUpdateTime 上次修改时间
	 * @throws SQLException 
	 * 
	 */
	public void delete(String strEmployee, Long lngId, String strUpdateTime)
	throws DataChangeException, SQLException {
		try {
			AdJRestaurantPlan objOld = new AdJRestaurantPlan();
			objOld = this.findById(lngId);
			String strLastmodifiedDate = objOld.getUpdateTime().toString().substring(0,19);
			if (strLastmodifiedDate.equals(strUpdateTime.substring(0, 19))) {
				String sql = "UPDATE AD_J_RESTAURANT_PLAN T "
						+ "SET T.UPDATE_USER = ?" 
						+ ",   T.UPDATE_TIME = SYSDATE "
						+ ",   T.IS_USE = ? " 
						+ "WHERE T.ID =?";
				Object[] params = new Object[3];
				params[0] = strEmployee;
				params[1] = "N";	
				params[2] = lngId;	
				LogUtil.log("EJB:餐厅计划数据删除操作开始。SQL：", Level.INFO, null);
				LogUtil.log(sql, Level.INFO, null);
				bll.exeNativeSQL(sql, params);
				LogUtil.log("EJB：餐厅计划数据删除操作正常结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}
		} catch (DataChangeException e) {
			LogUtil.log("EJB：值班人员数据删除异常结束", Level.SEVERE, e);
			throw new DataChangeException("");
		} catch (Exception re) {
			LogUtil.log("EJB：值班人员数据删除异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	/**
	 * 修改餐厅计划数据
	 * @author sufeiyu
	 * @param entity
	 *            餐厅计划entity
	 * @return 餐厅计划object
	 * @throws DataChangeException 
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public void update(AdJRestaurantPlan entity, String strUpdateTime)
			throws DataChangeException, SQLException {
		LogUtil.log("EJB：餐厅计划数据更新操作开始", Level.INFO, null);
		try {
			AdJRestaurantPlan objOld = new AdJRestaurantPlan();
			objOld = this.findById(entity.getId());
			String strLastmodifiedDate = objOld.getUpdateTime().toString()
					.substring(0, 19);
			if (strLastmodifiedDate.equals(strUpdateTime.substring(0, 19))) {
				entity.setUpdateTime(new Date());
				entityManager.merge(entity);
				LogUtil.log("EJB：餐厅计划数据更新操作正常结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}
		} catch (DataChangeException e) {
			LogUtil.log("EJB：餐厅计划数据更新操作异常结束", Level.SEVERE, e);
			throw new DataChangeException("");
		} catch (Exception re) {
			LogUtil.log("EJB：餐厅计划数据更新操作异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	public AdJRestaurantPlan findById(Long id) {
		LogUtil.log("finding AdJRestaurantPlan instance with id: " + id,
				Level.INFO, null);
		try {
			AdJRestaurantPlan instance = entityManager.find(
					AdJRestaurantPlan.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJRestaurantPlan entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJRestaurantPlan property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJRestaurantPlan> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJRestaurantPlan> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJRestaurantPlan instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJRestaurantPlan model where model."
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
	 * Find all AdJRestaurantPlan entities.
	 * 
	 * @return List<AdJRestaurantPlan> all AdJRestaurantPlan entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJRestaurantPlan> findAll() {
		LogUtil
				.log("finding all AdJRestaurantPlan instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from AdJRestaurantPlan model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 取得餐厅计划的详细信息
	 * 
	 * @param planDate 日期
	 * 
	 * @return PageObject 餐厅计划信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject getRestaurantPlan(String strEnterpriseCode, String planDate, final int... rowStartIdxAndCount) {
		PageObject objResult = new PageObject();
		
		try {
			// 取得餐厅计划的详细信息的sql
			String strSql   = " SELECT"
							+  " A.ID,"
							+  " A.PLAN_DATE,"
							+  " A.MENU_TYPE,"
							+  " A.MENU_PRICE,"
							+  " A.MEMO,"
							+  " A.MEMU_CODE,"
							+  " B.MENU_NAME,"
							+  " B.MENUTYPE_CODE,"
							+  " TO_CHAR(A.UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS'),"
							+  " C.MENUTYPE_NAME "
							+  "FROM"
							+  " AD_J_RESTAURANT_PLAN A"
							+  " LEFT JOIN AD_C_MENU_WH B"
							+  " ON A.MEMU_CODE = B.MENU_CODE AND"
							+  " B.ENTERPRISE_CODE = ? "
							+  " LEFT JOIN AD_C_MENU_TYPE C"
							+  " ON B.MENUTYPE_CODE = C.MENUTYPE_CODE AND"
							+  " C.ENTERPRISE_CODE = ? "
							+  "WHERE"
							+  " A.ENTERPRISE_CODE = ? AND"
							+  " A.IS_USE = ? ";
			
			// 取得餐厅计划的详细信息数量的sql
			String strSqlCount  = "SELECT COUNT(A.ID)"
								+  "FROM"
								+  " AD_J_RESTAURANT_PLAN A"
								+  " LEFT JOIN AD_C_MENU_WH B"
								+  " ON A.MEMU_CODE = B.MENU_CODE AND"
								+  " B.ENTERPRISE_CODE = ? "
								+  " LEFT JOIN AD_C_MENU_TYPE C"
								+  " ON B.MENUTYPE_CODE = C.MENUTYPE_CODE AND"
								+  " C.ENTERPRISE_CODE = ? "
								+  "WHERE"
								+  " A.ENTERPRISE_CODE = ? AND"
								+  " A.IS_USE = ? ";
			int intLength = 4;
			if ((planDate != null) && (!planDate.equals(""))) {
				strSql += "AND TO_CHAR(A.PLAN_DATE,"
					+  " 'YYYY-MM-DD') = ?";
				strSqlCount += "AND TO_CHAR(A.PLAN_DATE,"
					+  " 'YYYY-MM-DD') = ?";
				intLength = intLength + 1;
			}
			
			Object[] params = new Object[intLength];
			params[0] = strEnterpriseCode;
			params[1] = strEnterpriseCode;
			params[2] = strEnterpriseCode;
			params[3] = "Y";
			
			if (intLength == 5) {
				params[4] = planDate;
			}
			
			LogUtil.log("EJB:取得餐厅计划数据开始。", Level.INFO, null);
			LogUtil.log("SQL：" + strSql, Level.INFO, null);
			
			// 取得数据
			List lstQuery = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<RestaurantPlanInfo> arrQuery = new ArrayList<RestaurantPlanInfo>();
			
			LogUtil.log("EJB:取得餐厅计划数据结束", Level.INFO, null);
			
			// 格式转换
			if(lstQuery != null && lstQuery.size() > 0) {
				AdJRestaurantPlan test = new AdJRestaurantPlan();
				Iterator it = lstQuery.iterator();
				while (it.hasNext()) {
					RestaurantPlanInfo model = new RestaurantPlanInfo();
					Object[] data = (Object[]) it.next();
					model.setId(Long.parseLong(data[0].toString()));
					// 日期
					if (data[1] != null)
						model.setPlanDate(data[1].toString());
					// 类别
					if (data[2] != null)
						model.setMenuType(data[2].toString());
					// 价格
					if (data[3] != null)
						model.setMenuPrice(Double.parseDouble(data[3]
								.toString()));
					// 备注
					if (data[4] != null)
						model.setMemo(data[4].toString());
					// 菜谱编码
					if (data[5] != null)
						model.setmenuCode(data[5].toString());
					// 菜谱名称
					if (data[6] != null)
						model.setmenuName(data[6].toString());
					// 菜谱类别
					if (data[7] != null)
						model.setMenuTypeCode(data[7].toString());
					// 修改时间
					if (data[8] != null)
						test = this.findById(model.getId());
					    model.setUpdateTime(test.getUpdateTime().toString());
					// 类别名称
					if (data[9] != null)
						model.setMenuTypeName(data[9].toString());
					arrQuery.add(model);
					model = null;
				}
				if (arrQuery.size() > 0) {
					Long totalCount = Long.parseLong(bll.getSingal(strSqlCount, params).toString());
					objResult.setList(arrQuery);
					objResult.setTotalCount(totalCount);
				} 
			} else {
				Long lngZero = new Long(0);
				objResult.setTotalCount(lngZero);
			}
		} catch (NumberFormatException e) {
			LogUtil.log("EJB:取得餐厅计划数据失败", Level.SEVERE, e);
		}
		return objResult;
	}
	
	/**
	 *  获得表中最大的ID
	 */
	public Long getMaxId() {
		Long lngMaxIdLong = new Long(-1);
		lngMaxIdLong = bll.getMaxId("AD_J_RESTAURANT_PLAN", "ID");
		return lngMaxIdLong;
	}
	
}