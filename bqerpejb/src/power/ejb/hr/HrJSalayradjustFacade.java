/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HRCodeConstants;

/**
 * Facade for entity HrJSalayradjust.
 * 
 * @see power.ejb.hr.HrJSalayradjust
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJSalayradjustFacade implements HrJSalayradjustFacadeRemote {
	/** 日期形式字符串 yyyy-MM-dd HH:mm:ss */
    private static final String DATE_FORMAT_YYYYMMDD_TIME_SEC = "yyyy-MM-dd HH:mm:ss";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved HrJSalayradjust entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJSalayradjust entity to persist
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJSalayradjust entity) throws SQLException {
		LogUtil.log("Ejb:新增薪酬变动单申报开始", Level.INFO, null);
		try {
			if (entity.getSalayradjustid() == null) {
                // 设定主键值
                entity.setSalayradjustid(bll.getMaxId("HR_J_SALAYRADJUST",
                        "SALAYRADJUSTID"));
                entity.setLastModifiedDate(new Date());
                entity.setInsertdate(new Date());
				entity.setIsUse("Y");
				entityManager.persist(entity);
			}
			LogUtil.log("新增薪酬变动单申报结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("新增薪酬变动单申报失败", Level.SEVERE, re);
			throw new SQLException(re.getMessage());
		}
	}

	/**
	 * Delete a persistent HrJSalayradjust entity.
	 * 
	 * @param entity
	 *            HrJSalayradjust entity to delete
	 * @throws SQLException 
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJSalayradjust entity) throws SQLException {
		 LogUtil.log("EJB:删除薪酬变动登记开始。", Level.INFO, null);
		try {
			entity.setLastModifiedDate(new Date());
			entity.setIsUse("N");
			entityManager.merge(entity);
			LogUtil.log("EJB:删除薪酬变动登记结束。", Level.INFO, null);
		} catch (RuntimeException re) {
			 LogUtil.log("EJB:删除薪酬变动登记失败。", Level.INFO, null);
			 throw new SQLException(re.getMessage());
		}
	}

	 /**
	     * 上报岗位调动单
	     * @param entity 岗位调动单bean
	     * @throws SQLException
	     */
		    public void report(HrJSalayradjust entity) throws SQLException {
				 LogUtil.log("EJB:上报薪酬变动登记开始。", Level.INFO, null);
			try {
				entity.setLastModifiedDate(new Date());
				entity.setDcmState("1");
				entityManager.merge(entity);
			    LogUtil.log("EJB:上报薪酬变动登记结束。", Level.INFO, null);
			} catch (RuntimeException re) {
				 LogUtil.log("EJB:上报薪酬变动登记失败。", Level.INFO, null);
			    throw new SQLException(re.getMessage());
			}
		    }
		 
	/**
	 * Persist a previously saved HrJSalayradjust entity and return it or a copy
	 * of it to the sender. A copy of the HrJSalayradjust entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJSalayradjust entity to update
	 * @return HrJSalayradjust the persisted HrJSalayradjust entity instance,
	 *         may not be the same
	 * @throws DataChangeException 
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJSalayradjust update(HrJSalayradjust entity) throws SQLException, DataChangeException {
		LogUtil.log("Ejb:更新薪酬变动单申报开始", Level.INFO, null);
		// 排他
		
		// 得到数据库中的这个记录
		HrJSalayradjust salary = this.findById(entity.getSalayradjustid());

		// 排他
		if (!formatDate(salary.getLastModifiedDate(), DATE_FORMAT_YYYYMMDD_TIME_SEC)
				.equals(formatDate(entity.getLastModifiedDate(), DATE_FORMAT_YYYYMMDD_TIME_SEC))) {
			throw new DataChangeException("排它处理");
		}
		
		try {
			// 更新时间
			entity.setLastModifiedDate(new Date());
			HrJSalayradjust result = entityManager.merge(entity);
			LogUtil.log("Ejb:更新薪酬变动单申报结束", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 更新数据
	 * @param entity
	 * @param date
	 * @throws SQLException
	 * @throws DataChangeException
	 */
	public void updateData(HrJSalayradjust entity,String date)  throws SQLException, DataChangeException{
		LogUtil.log("EJB:更新薪酬变动登记开始。", Level.INFO, null);
		try {
			String dbUpdateTime = entity.getLastModifiedDate().toString().substring(0, 19);
			if (!date.equals(dbUpdateTime)) {
				throw new DataChangeException("");
			}else{
				entity.setLastModifiedDate(new Date());
				entityManager.merge(entity);
			}
		    LogUtil.log("EJB:更新薪酬变动登记结束。", Level.INFO, null);
		} catch (RuntimeException re) {
			 LogUtil.log("EJB:更新薪酬变动登记失败。", Level.INFO, null);
		    throw new SQLException(re.getMessage());
		}
	}

	public HrJSalayradjust findById(Long id) {
		LogUtil.log("finding HrJSalayradjust instance with id: " + id,
				Level.INFO, null);
		try {
			HrJSalayradjust instance = entityManager.find(
					HrJSalayradjust.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJSalayradjust entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJSalayradjust property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJSalayradjust> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJSalayradjust> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJSalayradjust instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJSalayradjust model where model."
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
	 * Find all HrJSalayradjust entities.
	 * 
	 * @return List<HrJSalayradjust> all HrJSalayradjust entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJSalayradjust> findAll() {
		LogUtil.log("finding all HrJSalayradjust instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJSalayradjust model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}


	/**
	 * 通过查询条件获得相应的薪酬变动申请上报单信息数据
	 * 
	 * @param strStartDate
	 * @param strEndDate
	 * @param strDeptCode
	 * @param strDcmStatus
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getSalaryChangeList(String strStartDate,
			String strEndDate, String strDeptCode, String strDcmStatus,
			String enterpriseCode, final int... rowStartIdxAndCount)
			throws SQLException {
		try {
			PageObject pobj = new PageObject();
			// 查询参数数量
			int paramsCnt = 6;
			// 查询sql
			StringBuffer strSql = new StringBuffer().append("SELECT S.SALAYRADJUSTID, ").
					append( "  E.CHS_NAME, ").
					append( "  to_char(S.DO_DATE,'yyyy-mm-dd'), ").
					append( "  D.DEPT_NAME,").
					append( "  S.OLD_CHECK_STATION_GRADE, ").
					append( "  S.NEW_CHECK_STATION_GRADE, ").
					append( "  S.OLD_STATION_GRADE, ").
					append( "  S.NEW_STATION_GRADE, ").
					append( "  S.OLD_SALARY_GRADE, ").
					append( "  S.NEW_SALARY_GRADE, ").
					append( "  S.ADJUST_TYPE, ").
					append( "  S.STATION_CHANGE_TYPE, ").
					append( "  S.REASON, ").
					append( "  S.DCM_STATE, ").
					append( "  S.MEMO ,").
					append( "  to_char(S.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss') ").
					append( "from " ).append( "  ( HR_J_SALAYRADJUST S " ).
					append( "  INNER JOIN ").
					append( "      HR_J_EMP_INFO E ON").
					append( "      S.EMP_ID = E.EMP_ID AND E.IS_USE= ? ").
					append("       AND E.ENTERPRISE_CODE = ? )").
					append( "LEFT JOIN  " ).append( "      HR_C_DEPT D on ").
					append( "      E.dept_id =D.dept_id ").
					append(" AND D.ENTERPRISE_CODE = ? ");

			StringBuffer strSqlWhere =new StringBuffer().append(" where S.IS_USE = ? ").
					append( " AND S.ENTERPRISE_CODE = ?  ").
					append( " AND S.STATIONREMOVEID = ? ");

			// 如果部门不为空，进行部门查询
			if (checkIsNotNull(strDeptCode)) {
				paramsCnt++;
				strSqlWhere.append("and E.DEPT_ID = ? ");
			}

			if (checkIsNotNull(strStartDate)) {
				paramsCnt++;
			}
			if (checkIsNotNull(strEndDate)) {
				paramsCnt++;
			}
			if (checkIsNotNull(strDcmStatus)) {
				paramsCnt++;
			}

			// 查询参数数组
			Object[] params = new Object[paramsCnt];
			int i = 0;
			params[i++] = "Y";
			params[i++] = enterpriseCode;
			params[i++] = enterpriseCode;
			params[i++] = "Y";
			params[i++] = enterpriseCode;
			params[i++] = "0";
			// 部门id
			if (checkIsNotNull(strDeptCode)) {
				params[i++] = strDeptCode;
			}
			// 起薪开始日期
			if (checkIsNotNull(strStartDate)) {
				strSqlWhere.append("  and to_char(S.DO_DATE,'yyyy-mm-dd') >= ? ");
				params[i++] = strStartDate;
			}
			// 起薪结束日期
			if (checkIsNotNull(strEndDate)) {
				strSqlWhere.append("  and to_char(S.DO_DATE,'yyyy-mm-dd') <= ? ");
				params[i++] = strEndDate;
			}
			// 单据状态
			if (checkIsNotNull(strDcmStatus)) {
				strSqlWhere.append("  and S.DCM_STATE = ? ");
				params[i++] = strDcmStatus;
			}
			// 拼接查询sql
			strSql.append(strSqlWhere);
			strSql.append(" ORDER BY S.SALAYRADJUSTID");
			LogUtil.log("EJB:薪酬变动单申报信息查询开始。", Level.INFO, null);
			LogUtil.log("SQL=" + strSql.toString(), Level.INFO, null);

			// list
			List list = bll.queryByNativeSQL(strSql.toString(), params,
					rowStartIdxAndCount);
			List<HrJSalayradjustForMe> arrList = new ArrayList<HrJSalayradjustForMe>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				HrJSalayradjustForMe info = new HrJSalayradjustForMe();
				// 薪酬变动ID
				if (data[0] != null) {
					info.setSalayradjustid(Long.parseLong(data[0].toString()));
				}
				// 员工姓名
				if (data[1] != null) {
					info.setChsName(data[1].toString());
				}
				// 起薪日期
				if (data[2] != null) {
					info.setDoDate(data[2].toString());
				}
				// 部门名称
				if (data[3] != null) {
					info.setDeptName(data[3].toString());
				}
				// 变更前执行岗级
				if (data[4] != null) {
					info.setOldCheckStationGrade(Long.parseLong(data[4]
							.toString()));
				}
				// 变更后执行岗级
				if (data[5] != null) {
					info.setNewCheckStationGrade(Long.parseLong(data[5]
							.toString()));
				}
				// 变更前标准岗级
				if (data[6] != null) {
					info.setOldStationGrade(Long.parseLong(data[6].toString()));
				}
				// 变更后标准岗级
				if (data[7] != null) {
					info.setNewStationGrade(Long.parseLong(data[7].toString()));
				}
				// 变更前薪级
				if (data[8] != null) {
					info.setOldSalaryGrade(Long.parseLong(data[8].toString()));
				}
				// 变更后薪级
				if (data[9] != null) {
					info.setNewSalaryGrade(Long.parseLong(data[9].toString()));
				}
				// 变动类别
				if (data[10] != null) {
					info.setAdjustType(data[10].toString());
				}
				// 岗薪变化类别
				if (data[11] != null) {
					info.setStationChangeType(data[11].toString());
				}
				// 原因
				if (data[12] != null) {
					info.setReason(data[12].toString());
				}
				// 单据状态
				if (data[13] != null) {
					info.setDcmState(data[13].toString());
				}
				// 备注
				if (data[14] != null) {
					info.setMemo(data[14].toString());
				}
				// 更新时间，用于排他
				if (data[15] != null) {
					info.setUpdateTime(data[15].toString());
				}
				arrList.add(info);
			}
			// 分页查询
			// 查询参数数量
//			int paramsCntTwo = 6;
			// 查询sql
			StringBuffer strSqlCount = new StringBuffer().append("SELECT COUNT(S.SALAYRADJUSTID) ").
					append( "from " ).append( "  ( HR_J_SALAYRADJUST S " ).
					append( "  INNER JOIN ").
					append( "      HR_J_EMP_INFO E ON").
					append( "      S.EMP_ID = E.EMP_ID AND E.IS_USE= ? ").
					append("       AND E.ENTERPRISE_CODE = ? )").
					append( "LEFT JOIN  " ).append( "      HR_C_DEPT D on ").
					append( "      E.dept_id =D.dept_id ").
					append(" AND D.ENTERPRISE_CODE = ? ").
					append(" where S.IS_USE = ? ").
					append( " AND S.ENTERPRISE_CODE = ?  ").
					append( " AND S.STATIONREMOVEID = ? ");

			// 如果部门不为空，进行部门查询
			if (checkIsNotNull(strDeptCode)) {
				strSqlCount.append("and E.DEPT_ID = ? ");
			}

			// 查询参数数组
			Object[] paramsTwo = new Object[paramsCnt];
			int j = 0;
			paramsTwo[j++] = "Y";
			paramsTwo[j++] = enterpriseCode;
			paramsTwo[j++] = enterpriseCode;
			paramsTwo[j++] = "Y";
			paramsTwo[j++] = enterpriseCode;
			paramsTwo[j++] = "0";
			// 部门id
			if (checkIsNotNull(strDeptCode)) {
				paramsTwo[j++] = strDeptCode;
			}
			// 起薪开始日期
			if (checkIsNotNull(strStartDate)) {
				strSqlCount.append("  and to_char(S.DO_DATE,'yyyy-mm-dd') >= ? ");
				paramsTwo[j++] = strStartDate;
			}
			// 起薪结束日期
			if (checkIsNotNull(strEndDate)) {
				strSqlCount.append("  and to_char(S.DO_DATE,'yyyy-mm-dd') <= ? ");
				paramsTwo[j++] = strEndDate;
			}
			// 单据状态
			if (checkIsNotNull(strDcmStatus)) {
				strSqlCount.append("  and S.DCM_STATE = ? ");
				paramsTwo[j++] = strDcmStatus;
			}
			Long totalCount=Long.parseLong(bll.getSingal(strSqlCount.toString(),paramsTwo).toString());
			// 按查询结果集设置返回结果
			if (arrList.size() == 0) {
				// 设置查询结果集
				pobj.setList(null);
				// 设置行数
				pobj.setTotalCount(Long.parseLong(0 + ""));
			} else {
				pobj.setList(arrList);
				pobj.setTotalCount(totalCount);
			}
			LogUtil.log("EJB:薪酬变动单申报信息查询结束。", Level.INFO, null);
			// 返回PageObject
			return pobj;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:薪酬变动单申报信息查询失败。", Level.SEVERE, e);
			throw new SQLException();
		}

	}
	/**
	 * 员工名称重复查询
	 * 
	 * @param empId,enterpriseCode
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean empInfoIsReapeat(Long empId,String enterpriseCode) throws SQLException {
		String strSql = "select * \n "
			+ " from HR_J_SALAYRADJUST S  " 
			+ " where S.EMP_ID = ? and S.DCM_STATE <> ? and S.IS_USE = ? and S.ENTERPRISE_CODE = ?"
			+ " AND S.STATIONREMOVEID = ? ";
		LogUtil.log("EJB:员工名称重复查询开始", Level.INFO, null);
		LogUtil.log("SQL=" + strSql, Level.INFO, null);
		try {
			// list
			List lst = bll.queryByNativeSQL(strSql, new Object[] { empId,HRCodeConstants.DCM_STATUS_COMPLETE,'Y',enterpriseCode,'0' });
			if(lst == null || lst.size()>0){
				return true;
			} else {
			    LogUtil.log("EJB:员工名称重复查询结束", Level.INFO, null);
			    return false;
			}
		}catch(Exception e) {
			LogUtil.log("EJB:查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	/**
	 * 根据薪酬变动单id查找记录的最后更新时间
	 * @param salaryId
	 * @return String 薪酬变动单的最后更新时间
	 */
	@SuppressWarnings("unchecked")
	public String myFindTimeById(Long salaryId) {
		LogUtil.log("EJB:根据薪酬变动单id查找记录的最后更新时间开始", Level.INFO, null);
		try {
			String sql = 
			"SELECT " + 
			   "TO_CHAR(A.LAST_MODIFIED_DATE, 'yyyy-mm-dd hh24:mi:ss') " +
			"FROM " +
			   "HR_J_SALAYRADJUST A " +
			"WHERE " +
			   "A.SALAYRADJUSTID = ? AND " +
			   "A.IS_USE = 'Y'";
			Object obj = bll.getSingal(sql, new Object[]{salaryId});
			if (obj != null) {
				String time = obj.toString();
				LogUtil.log("EJB:根据薪酬变动单id查找记录的最后更新时间结束", Level.INFO, null);
				return time;
			}
			LogUtil.log("EJB:根据薪酬变动单id查找记录的最后更新时间结束", Level.INFO, null);
			return null;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:根据薪酬变动单id查找记录的最后更新时间失败", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 为空判断
	 * 
	 * @param value
	 * @return boolean
	 */
	private boolean checkIsNotNull(String value) {
		if (value != null && !"".equals(value)) {
			return true;
		} else {
			return false;
		}
	}
	 /**
     * 根据日期和形式返回日期字符串
     * @param argDate 日期
     * @param argFormat 日期形式字符串
     * @return 日期字符串
     */
    private String formatDate(Date argDate, String argFormat) {
        if (argDate == null) {
            return "";
        }
        
        // 日期形式
        SimpleDateFormat sdfFrom = null;
        // 返回字符串
        String strResult = null;

        try {
            sdfFrom = new SimpleDateFormat(argFormat);
            // 格式化日期
            strResult = sdfFrom.format(argDate).toString();
        } catch (Exception e) {
            strResult = "";
        } finally {
            sdfFrom = null;
        }

        return strResult;
    }

    /**
	 * 根据人员id取得执行岗级，标准岗级，薪级
	 * 
	 * @param empId
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEmpInfoMsg(String empId) throws SQLException {
		PageObject pobj = new PageObject();
		// 查询sql
		String strSql = "select distinct e.CHECK_STATION_GRADE ,e.STATION_GRADE ,e.SALARY_LEVEL "
				+ " from HR_J_EMP_INFO e " + " where e.EMP_ID = ? ";

		LogUtil.log("EJB:员工信息查询开始", Level.INFO, null);
		LogUtil.log("SQL=" + strSql, Level.INFO, null);
		try {
			// list
			List lst = bll.queryByNativeSQL(strSql, new Object[] { empId });
			List<String> lstInfor = new ArrayList<String>();
			if (lst != null && lst.size() != 0) {
				Object[] obj = (Object[]) lst.get(0);
				// 设置执行岗级
				if (null != obj[0]) {
					lstInfor.add(obj[0].toString());
				} else {
					lstInfor.add("");
				}
				// 设置标准岗级
				if (null != obj[1]) {
					lstInfor.add(obj[1].toString());
				} else {
					lstInfor.add("");
				}
				// 设置薪级
				if (null != obj[1]) {
					lstInfor.add(obj[2].toString());
				} else {
					lstInfor.add("");
				}
			}
			pobj.setList(lstInfor);
			LogUtil.log("EJB:员工信息查询结束", Level.INFO, null);
			return pobj;
		} catch (Exception e) {
			LogUtil.log("EJB:查询失败", Level.SEVERE, e);
			throw new SQLException();
		}

	}

	/**
     * 根据岗位调动单ID查找酬变动记录信息
     * @param enterpriseCode 企业编码
     * @return
     * @throws SQLException
     */
	@SuppressWarnings("unchecked")
	public PageObject getSalayAdjustByRemoveId(String stationRemoveId,String enterpriseCode) throws SQLException {
		LogUtil.log("根据岗位调动单ID查找酬变动记录信息开始: ",Level.INFO, null);
        try {
        	PageObject result = new PageObject();
        	Object[] params = new Object[3];
			params[0] = enterpriseCode;
			params[1] = 'Y';
			params[2] = Long.parseLong(stationRemoveId);
            // 查询sql
            String sql=
                "select  * from HR_J_SALAYRADJUST  t\n" +
                "where  t.enterprise_code= ? \n" +
                "and t.is_use= ? \n" +
                "and t.STATIONREMOVEID= ? \n" +
                " order by t.SALAYRADJUSTID";
            // 打印sql文
			LogUtil.log("sql 文："+sql, Level.INFO, null);
            // 执行查询
            List<HrJSalayradjust> list = bll.queryByNativeSQL(sql,params, HrJSalayradjust.class);
            // 查询sql
            String sqlCount =
            	"select count(t.SALAYRADJUSTID) from HR_J_SALAYRADJUST  t\n" +
                "where  t.enterprise_code= ? \n" +
                "and t.is_use= ? \n" +
                "and t.EMP_ID= ? \n" +
                " order by t.SALAYRADJUSTID";
            // 执行查询
            Long totalCount = Long
				.parseLong(bll.getSingal(sqlCount,params).toString());
            // 设置PageObject
            result.setList(list);
            result.setTotalCount(totalCount);
            // 返回
            LogUtil.log("根据岗位调动单ID查找酬变动记录信息结束: ",Level.INFO, null);
            return result;
        }catch(RuntimeException e){
        	LogUtil.log("根据岗位调动单ID查找酬变动记录信息失败: ",Level.INFO, null);
			throw new SQLException(e.getMessage());
		}
	}
}