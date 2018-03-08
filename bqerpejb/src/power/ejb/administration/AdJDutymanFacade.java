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
import power.ejb.administration.form.DutymanInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJDutyman.
 * 
 * @see power.ejb.administration.AdJDutyman
 * @author sufeiyu
 */
@Stateless
public class AdJDutymanFacade implements AdJDutymanFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 增加值班人员
	 * 
	 * @param entity
	 *            值班人员entity
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJDutyman entity) throws SQLException {
		LogUtil.log("EJB：值班人员数据增加操作开始", Level.INFO, null);
		try {
			if (entity.getId() == null) {
				entity.setId(bll.getMaxId("AD_J_DUTYMAN", "ID"));
			}
			entity.setUpdateTime(new Date());
			entityManager.persist(entity);
			LogUtil.log("EJB：值班人员数据增加操作正常结束", Level.INFO, null);
		} catch (Exception re) {
			LogUtil.log("EJB：值班人员数据增加异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	/**
	 * 删除值班人员
	 * 
	 * @param entity
	 *            值班人员entity
	 * @throws DataChangeException 
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(String strEmployee, Long personId, String strUpdateTime)
			throws DataChangeException, SQLException {
		try {
			AdJDutyman objOld = new AdJDutyman();
			objOld = this.findById(personId);
			String strLastmodifiedDate = objOld.getUpdateTime().toString().substring(0, 19);
			if (strLastmodifiedDate.equals(strUpdateTime.substring(0, 19))) {
				String sql = "UPDATE AD_J_DUTYMAN T "
						+ "SET T.UPDATE_USER = ?" 
						+ ",   T.UPDATE_TIME = SYSDATE "
						+ ",   T.IS_USE = ? " 
						+ "WHERE T.ID =?";
				Object[] params = new Object[3];
				params[0] = strEmployee;
				params[1] = "N";
				params[2] = personId;	
				LogUtil.log("EJB:值班人员数据删除操作开始。SQL：", Level.INFO, null);
				LogUtil.log(sql, Level.INFO, null);
				bll.exeNativeSQL(sql, params);
				LogUtil.log("EJB：值班人员数据删除操作正常结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}
		} catch(DataChangeException e) {
			LogUtil.log("EJB：值班人员数据删除异常结束", Level.SEVERE, e);
			throw new DataChangeException("");
		}
		catch (Exception re) {
			LogUtil.log("EJB：值班人员数据删除异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	/**
	 * 修改值班人员数据
	 * 
	 * @param entity
	 *            值班人员entity
	 * @return 值班人员object
	 * @throws DataChangeException 
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public void update(AdJDutyman entity, String strUpdateTime)
			throws DataChangeException, SQLException {
		LogUtil.log("EJB：值班人员数据更新操作开始", Level.INFO, null);
		try {
			AdJDutyman objOld = new AdJDutyman();
			objOld = this.findById(entity.getId());
			String strLastmodifiedDate = objOld.getUpdateTime().toString().substring(0,19);
			if (strLastmodifiedDate.equals(strUpdateTime.substring(0, 19))) {
				entity.setUpdateTime(new Date());
				entityManager.merge(entity);
				LogUtil.log("EJB：值班人员数据更新操作正常结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}
		} catch(DataChangeException e) {
			LogUtil.log("EJB：值班人员数据更新操作异常结束", Level.SEVERE, e);
			throw new DataChangeException("");
		} catch (Exception re) {
			LogUtil.log("EJB：值班人员数据更新操作异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	public AdJDutyman findById(Long id) {
		LogUtil.log("finding AdJDutyman instance with id: " + id, Level.INFO,
				null);
		try {
			AdJDutyman instance = entityManager.find(AdJDutyman.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJDutyman entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJDutyman property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJDutyman> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJDutyman> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJDutyman instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJDutyman model where model."
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
	 * Find all AdJDutyman entities.
	 * 
	 * @return List<AdJDutyman> all AdJDutyman entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJDutyman> findAll() {
		LogUtil.log("finding all AdJDutyman instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJDutyman model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 取得值班人员的详细信息
	 * @param worktypeCode 工作类别
	 * @return PageObject 值班人员信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject getPerson(String strEnterpriseCode, String worktypeCode, final int... rowStartIdxAndCount) {
		PageObject objResult = new PageObject();
		
		try {
			// 取得数据的sql
			String strSql = "SELECT"
						  +  " A.ID, "
						  +  " TO_CHAR(A.DUTYTIME,'YYYY-MM-DD HH24:MI'),"
						  +  " A.WORKTYPE_CODE,"
						  +  " A.SUB_WORKTYPE_CODE,"
						  +  " B.SUB_WORKTYPE_NAME,"
						  +  " A.DUTYTYPE,"
						  +  " C.DUTY_TYPE_NAME,"
						  +  " A.DUTYMAN,"
						  +  " TO_CHAR(A.UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS'),"
						  +  " A.POSITION,"
						  +  " A.REPLACEMAN,"
						  +  " A.LEAVEMAN,"
						  +  " A.REASON "
						  +  " FROM "
						  +  " AD_J_DUTYMAN A"
						  +  " LEFT JOIN"
					      +  " AD_C_WORKTYPE B"
					      +  " ON"
					      +  " B.ENTERPRISE_CODE = ? AND"
					      +  " A.SUB_WORKTYPE_CODE = B.SUB_WORKTYPE_CODE "
					      +  " LEFT JOIN" 
					      +  " AD_C_DUTYTYPE C"
					      +  " ON"
					      +  " C.ENTERPRISE_CODE = ? AND "
					      +  " A.DUTYTYPE = C.DUTY_TYPE "
					      +  "WHERE"
					      +  " A.IS_USE = ?"
					      +  " AND"
					      +  " TO_CHAR(A.DUTYTIME,'YYYY-MM-DD')="   
					      +  " TO_CHAR(SYSDATE,'YYYY-MM-DD')"
					      +  " AND"
					      +  " A.ENTERPRISE_CODE = ? AND"
						  +  " A.WORKTYPE_CODE = ?";
			Object[] params = new Object[5];
			params[0] = strEnterpriseCode;
			params[1] = strEnterpriseCode;
			params[2] = "Y";
			params[3] = strEnterpriseCode;
			params[4] = worktypeCode;
	
			// 取得数目的sql
			String strSqlCount    = "SELECT COUNT(A.ID) "
								  +  " FROM "
								  +  " AD_J_DUTYMAN A"
								  +  " LEFT JOIN"
							      +  " AD_C_WORKTYPE B"
							      +  " ON"
							      +  " B.ENTERPRISE_CODE = ? AND"
							      +  " A.SUB_WORKTYPE_CODE = B.SUB_WORKTYPE_CODE "
							      +  " LEFT JOIN" 
							      +  " AD_C_DUTYTYPE C"
							      +  " ON"
							      +  " C.ENTERPRISE_CODE = ? AND "
							      +  " A.DUTYTYPE = C.DUTY_TYPE "
							      +  "WHERE"
							      +  " A.IS_USE = ?"
							      +  " AND"
							      +  " TO_CHAR(A.DUTYTIME,'YYYY-MM-DD')="   
							      +  " TO_CHAR(SYSDATE,'YYYY-MM-DD')"
							      +  " AND"
							      +  " A.ENTERPRISE_CODE = ? AND"
								  +  " A.WORKTYPE_CODE = ?";
				
			
			LogUtil.log("EJB:查询值班记事开始。SQL：", Level.INFO, null);
			LogUtil.log(strSql, Level.INFO, null);
			List lstQuery = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<DutymanInfo> arrQuery = new ArrayList<DutymanInfo>();
			
			if (lstQuery != null && lstQuery.size() > 0) {
				AdJDutyman test = new AdJDutyman();
				Iterator it = lstQuery.iterator();
				while (it.hasNext()) {
					DutymanInfo model = new DutymanInfo();
					Object[] data = (Object[]) it.next();
					model.setPersonId(Long.parseLong(data[0].toString()));
					// 值班日期
					if (data[1] != null)
						model.setDutytime(data[1].toString());
					// 工作类别code
					if (data[2] != null)
						model.setWorktypeCode(data[2].toString());
					// 子工作类别code
					if (data[3] != null)
						model.setSubWorktypeCode(data[3].toString());
					// 子工作类别名
					if (data[4] != null)
						model.setSubWorktypeName(data[4].toString());
					// 值别code
					if (data[5] != null)
						model.setDutytype(data[5].toString());
					// 值别
					if (data[6] != null)
						model.setDutytypeName(data[6].toString());
					// 值班人
					if (data[7] != null)
						model.setDutyman(data[7].toString());
					// 修改时间
					if (data[8] != null)
						test = this.findById(model.getPersonId());
					    model.setUpdateTime(test.getUpdateTime().toString());
					// 岗位
					if (data[9] != null)
						model.setPosition(data[9].toString());
					// 替班人员
					if (data[10] != null)
						model.setReplaceman(data[10].toString());
					// 缺勤人员
					if (data[11] != null)
						model.setLeaveman(data[11].toString());
					// 缺勤原因
					if (data[12] != null)
						model.setReason(data[12].toString());
					arrQuery.add(model);
					model = null;
				}
				if (arrQuery.size() > 0) {
					Long totalCount = Long.parseLong(bll.getSingal(strSqlCount, params)
							.toString());
					objResult.setList(arrQuery);
					objResult.setTotalCount(totalCount);
				}
			}  else {
				Long lngZero = new Long(0);
				objResult.setTotalCount(lngZero);
			}
		    LogUtil.log("EJB:查询值班记事正常结束", Level.INFO, null);
			return objResult;
		} catch (NumberFormatException e) {
			LogUtil.log("EJB:查询值班记事异常结束", Level.SEVERE, e);
			throw e;
		}
	}
}