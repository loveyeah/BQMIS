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
import power.ejb.administration.form.OutQuestInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity AdJOutQuest.
 * 
 * @see power.ejb.administration.AdJOutQuest
 * @author sufeiyu
 */
@Stateless
public class AdJOutQuestFacade implements AdJOutQuestFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 增加一条签报申请对象
	 * 
	 * @param entity 签报申请对象
	 *            
	 * @throws SQLException 
	 * @throws Exception
	 *            
	 */
	public void save(AdJOutQuest entity) throws SQLException {
		LogUtil.log("EJB：签报申请数据增加操作开始", Level.INFO, null);
		try {
			if (entity.getId() == null) {
				entity.setId(bll.getMaxId("AD_J_OUT_QUEST", "ID"));
			}
			entity.setUpdateTime(new Date());
			entityManager.persist(entity);
			LogUtil.log("EJB：签报申请数据增加操作正常结束", Level.INFO, null);
		} catch (Exception re) {
			LogUtil.log("EJB：签报申请数据增加异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	/**
	 * Delete a persistent AdJOutQuest entity.
	 * 
	 * @param entity
	 *            AdJOutQuest entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJOutQuest entity) {
		LogUtil.log("deleting AdJOutQuest instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(AdJOutQuest.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新签报申请表
	 * 
	 * @param entity  签报申请对象 
	 * @param strUpdateTime 上次修改时间
	 *            
	 * @throws DataChangeException 
	 * @throws SQLException 
	 * @throws Exception
	 *           
	 */
	public void update(AdJOutQuest entity, String strUpdateTime) throws DataChangeException, SQLException {
		LogUtil.log("EJB：签报申请数据更新操作开始", Level.INFO, null);
		try {
			AdJOutQuest objOld = new AdJOutQuest();
			objOld = this.findById(entity.getId());
			String strLastmodifiedDate = objOld.getUpdateTime().toString().substring(0,19);
			if (strLastmodifiedDate.equals(strUpdateTime.substring(0, 19))) {
				entity.setUpdateTime(new Date());
				entityManager.merge(entity);
				LogUtil.log("EJB：签报申请数据更新操作正常结束", Level.INFO, null);
			} else {
				throw new DataChangeException("");
			}
		} catch (DataChangeException e) {
			throw new DataChangeException("");
		}catch (Exception re) {
			LogUtil.log("EJB：签报申请数据更新操作异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	public AdJOutQuest findById(Long id) {
		LogUtil.log("finding AdJOutQuest instance with id: " + id, Level.INFO,
				null);
		try {
			AdJOutQuest instance = entityManager.find(AdJOutQuest.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all AdJOutQuest entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJOutQuest property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJOutQuest> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<AdJOutQuest> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding AdJOutQuest instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from AdJOutQuest model where model."
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
	 * Find all AdJOutQuest entities.
	 * 
	 * @return List<AdJOutQuest> all AdJOutQuest entities
	 */
	@SuppressWarnings("unchecked")
	public List<AdJOutQuest> findAll() {
		LogUtil.log("finding all AdJOutQuest instances", Level.INFO, null);
		try {
			final String queryString = "select model from AdJOutQuest model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 取得所有的申报申请
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject getOutQuest(String strEnterpriseCode, String strWorkCode,
			final int... rowStartIdxAndCount) throws SQLException {
		PageObject objResult = new PageObject();
		try {
			// 取得所有的申报申请的sql
			String strSql   = "SELECT"
							+ " A.ID,"
							+ " A.APPLY_ID,"
							+ " A.APPLY_MAN,"
							+ " C.CHS_NAME,"
							+ " B.DEPT_NAME,"
							+ " A.APPLY_TOPIC,"
							+ " A.APPLY_TEXT,"
							+ " TO_CHAR(A.APPLY_DATE,'YYYY-MM-DD'),"
							+ " A.CHECKED_MAN,"
							+ " A.REPORT_ID,"
							+ " A.DCM_STATUS,"
							+ " TO_CHAR(A.UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS'),"
							+ " A.APP_TYPE "
							+ "FROM"
							+ " AD_J_OUT_QUEST A "
							+ " LEFT JOIN "
							+ "     HR_J_EMP_INFO C "
							+ " ON"
							+ "     A.APPLY_MAN =  C.EMP_CODE "
							+ " AND"
							+ "     C.ENTERPRISE_CODE = ?"
							+ " LEFT JOIN "
							+ "      B "
							+ " ON "
							+ "     B.DEPT_ID = C.DEPT_ID"
							+ " AND"
							+ "     B.ENTERPRISE_CODE = ? "
							+ " WHERE"
							+ "     A.IS_USE = ? AND"
							+ "     A.APP_TYPE = ? AND"
							+ "     A.DCM_STATUS IN (?,?) AND"
							+ "     A.ENTERPRISE_CODE = ? AND"
							+ "     A.APPLY_MAN = ? AND "
							+ "     ADD_MONTHS(SYSDATE,-1) <= A.APPLY_DATE AND"
							+ "     A.APPLY_DATE <= SYSDATE";
			Object[] params = new Object[8];
			params[0] = strEnterpriseCode;
			params[1] = strEnterpriseCode;
			params[2] = "Y";
			params[3] = "I";
			params[4] = "0";
			params[5] = "3";
			params[6] = strEnterpriseCode;
			params[7] = strWorkCode;
			
			// 取得所有的申报申请数目的sql
			String strSqlCount  = "SELECT COUNT(A.ID)"
								+ "FROM"
								+ " AD_J_OUT_QUEST A "
								+ " LEFT JOIN "
								+ "     HR_J_EMP_INFO C "
								+ " ON"
								+ "     A.APPLY_MAN =  C.EMP_CODE "
								+ " AND"
								+ "     C.ENTERPRISE_CODE = ?"
								+ " LEFT JOIN "
								+ "      B "
								+ " ON "
								+ "     B.DEPT_ID = C.DEPT_ID"
								+ " AND"
								+ "     B.ENTERPRISE_CODE = ? "
								+ " WHERE"
								+ "     A.IS_USE = ? AND"
								+ "     A.APP_TYPE = ? AND"
								+ "     A.DCM_STATUS IN (?,?) AND"
								+ "     A.ENTERPRISE_CODE = ? AND"
								+ "     A.APPLY_MAN = ? AND "
								+ "     ADD_MONTHS(SYSDATE,-1) <= A.APPLY_DATE AND"
								+ "     A.APPLY_DATE <= SYSDATE";
			
			LogUtil.log("EJB:查询申报申请开始。SQL：", Level.INFO, null);
			LogUtil.log(strSql, Level.INFO, null);
			List lstQuery = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<OutQuestInfo> arrQuery = new ArrayList<OutQuestInfo>();
			
			if (lstQuery != null && lstQuery.size() > 0) {
				Iterator it = lstQuery.iterator();
				while (it.hasNext()) {
					OutQuestInfo model = new OutQuestInfo();
					Object[] data = (Object[]) it.next();
					model.setId(Long.parseLong(data[0].toString()));
					// 申请单号
					if (data[1] != null)
						model.setApplyId(data[1].toString());
					// 申报人code
					if (data[2] != null)
						model.setApplyMan(data[2].toString());
					// 申报人名
					if (data[3] != null)
						model.setApplyManName(data[3].toString());
					// 申报人部门名
					if (data[4] != null)
						model.setApplyManDeptName(data[4].toString());
					// 呈报主题
					if (data[5] != null)
						model.setApplyTopic(data[5].toString());
					// 呈报内容
					if (data[6] != null)
						model.setApplyText(data[6].toString());
					// 呈报日期
					if (data[7] != null)
						model.setApplyDate(data[7].toString());
					// 核稿人
					if (data[8] != null)
						model.setCheckedMan(data[8].toString());
					// 签报编号
					if (data[9] != null)
						model.setReportId(data[9].toString());
					// 单据状态
					if (data[10] != null)
						model.setDcmStatus(data[10].toString());
					// 修改时间
					if (data[11] != null)
						model.setUpdateTime(data[11].toString());
					// 签报种类
					if (data[12] != null)
						model.setAppType(data[12].toString());
					arrQuery.add(model);
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
		    LogUtil.log("EJB:查询申报申请正常结束", Level.INFO, null);
			return objResult;
	    } catch (NumberFormatException e) {
		    LogUtil.log("EJB:查询申报申请异常结束", Level.SEVERE, e);
		    throw new SQLException("");
	    }
	}
	
	/**
	 * 根据人员编码取得部门名称
	 * @param strWorkCode 人员编码
	 * @return  部门名称
	 */public String getDeptNameById(String strWorkCode) {
		String strDeptName = "";
		
		// 取得所在部门
		String strSql   = "SELECT"
						+ " DEPT_NAME "
						+ "FROM"
						+ "  A,"
						+ " HR_J_EMP_INFO B "
						+ "WHERE"
						+ " A.DEPT_ID = B.DEPT_ID AND"
						+ " A.IS_USE = ? AND"
						+ " B.IS_USE = ? AND"
						+ " B.EMP_CODE = ?";
		Object[] params = new Object[3];
		params[0] = "Y";
		params[1] = "Y";
		params[2] = strWorkCode;
		
		if (bll.getSingal(strSql, params) != null) {
			strDeptName = bll.getSingal(strSql, params).toString();
		}
		return strDeptName;
	}
	 
	 /**
	  * 根据code得到人名
	  */
	 public String getReaderManName(String strReaderMan) {
		 String strReaderManName = "";
		 
		 String strSql  = "SELECT"
						+ " CHS_NAME "
						+ "FROM"
						+ " HR_J_EMP_INFO "
						+ "WHERE"
						+ " EMP_CODE = ?";
		 
		Object[] params = new Object[1];
		params[0] = strReaderMan;

		if (bll.getSingal(strSql, params) != null) {
			strReaderManName = bll.getSingal(strSql, params).toString();
		}
		return strReaderManName;
	 }
	 
		/**
		 * 取得所有的申报申请
		 * @throws SQLException 
		 */
		@SuppressWarnings("unchecked")
		public PageObject getDirectorOutQuest(String strEnterpriseCode,
			String strWorkCode, final int... rowStartIdxAndCount)
			throws SQLException {
			PageObject objResult = new PageObject();
			try {
				// 取得所有的申报申请的sql
				String strSql   = "SELECT"
								+ " A.ID,"
								+ " A.APPLY_ID,"
								+ " A.APPLY_MAN,"
								+ " C.CHS_NAME,"
								+ " B.DEPT_NAME,"
								+ " A.APPLY_TOPIC,"
								+ " A.APPLY_TEXT,"
								+ " TO_CHAR(A.APPLY_DATE,'YYYY-MM-DD'),"
								+ " A.CHECKED_MAN,"
								+ " A.REPORT_ID,"
								+ " A.DCM_STATUS,"
								+ " TO_CHAR(A.UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS'),"
								+ " A.APP_TYPE "
								+ "FROM"
								+ " AD_J_OUT_QUEST A "
								+ " LEFT JOIN "
								+ "     HR_J_EMP_INFO C "
								+ " ON"
								+ "     A.APPLY_MAN =  C.EMP_CODE "
								+ " AND"
								+ "     C.ENTERPRISE_CODE = ?"
								+ " LEFT JOIN "
								+ "      B "
								+ " ON "
								+ "     B.DEPT_ID = C.DEPT_ID"
								+ " AND"
								+ "     B.ENTERPRISE_CODE = ? "
								+ " WHERE"
								+ "     A.IS_USE = ? AND"
								+ "     A.APP_TYPE = ? AND"
								+ "     A.DCM_STATUS IN (?,?) AND"
								+ "     A.ENTERPRISE_CODE = ? AND"
								+ "     A.APPLY_MAN = ? AND "
								+ "     ADD_MONTHS(SYSDATE,-1) <= A.APPLY_DATE AND"
								+ "     A.APPLY_DATE <= SYSDATE";
				Object[] params = new Object[8];
				params[0] = strEnterpriseCode;
				params[1] = strEnterpriseCode;
				params[2] = "Y";
				params[3] = "D";
				params[4] = "0";
				params[5] = "3";
				params[6] = strEnterpriseCode;
				params[7] = strWorkCode;
				
				// 取得所有的申报申请数目的sql
				String strSqlCount  = "SELECT COUNT(A.ID)"
									+ "FROM"
									+ " AD_J_OUT_QUEST A "
									+ " LEFT JOIN "
									+ "     HR_J_EMP_INFO C "
									+ " ON"
									+ "     A.APPLY_MAN =  C.EMP_CODE "
									+ " AND"
									+ "     C.ENTERPRISE_CODE = ?"
									+ " LEFT JOIN "
									+ "      B "
									+ " ON "
									+ "     B.DEPT_ID = C.DEPT_ID"
									+ " AND"
									+ "     B.ENTERPRISE_CODE = ? "
									+ " WHERE"
									+ "     A.IS_USE = ? AND"
									+ "     A.APP_TYPE = ? AND"
									+ "     A.DCM_STATUS IN (?,?) AND"
									+ "     A.ENTERPRISE_CODE = ? AND"
									+ "     A.APPLY_MAN = ? AND "
									+ "     ADD_MONTHS(SYSDATE,-1) <= A.APPLY_DATE AND"
									+ "     A.APPLY_DATE <= SYSDATE";
				
				LogUtil.log("EJB:查询申报申请开始。SQL：", Level.INFO, null);
				LogUtil.log(strSql, Level.INFO, null);
				List lstQuery = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
				List<OutQuestInfo> arrQuery = new ArrayList<OutQuestInfo>();
				
				if (lstQuery != null && lstQuery.size() > 0) {
					Iterator it = lstQuery.iterator();
					while (it.hasNext()) {
						OutQuestInfo model = new OutQuestInfo();
						Object[] data = (Object[]) it.next();
						model.setId(Long.parseLong(data[0].toString()));
						// 申请单号
						if (data[1] != null)
							model.setApplyId(data[1].toString());
						// 申报人code
						if (data[2] != null)
							model.setApplyMan(data[2].toString());
						// 申报人名
						if (data[3] != null)
							model.setApplyManName(data[3].toString());
						// 申报人部门名
						if (data[4] != null)
							model.setApplyManDeptName(data[4].toString());
						// 呈报主题
						if (data[5] != null)
							model.setApplyTopic(data[5].toString());
						// 呈报内容
						if (data[6] != null)
							model.setApplyText(data[6].toString());
						// 呈报日期
						if (data[7] != null)
							model.setApplyDate(data[7].toString());
						// 核稿人
						if (data[8] != null)
							model.setCheckedMan(data[8].toString());
						// 签报编号
						if (data[9] != null)
							model.setReportId(data[9].toString());
						// 单据状态
						if (data[10] != null)
							model.setDcmStatus(data[10].toString());
						// 修改时间
						if (data[11] != null)
							model.setUpdateTime(data[11].toString());
						// 签报种类
						if (data[12] != null)
							model.setAppType(data[12].toString());
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
			    LogUtil.log("EJB:查询申报申请正常结束", Level.INFO, null);
				return objResult;
		    } catch (NumberFormatException e) {
			    LogUtil.log("EJB:查询申报申请异常结束", Level.SEVERE, e);
			    throw new SQLException("");
		    }
		}	 
}