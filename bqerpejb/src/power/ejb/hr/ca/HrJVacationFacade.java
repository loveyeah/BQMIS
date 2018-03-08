/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.sql.SQLException;
import java.text.ParseException;
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
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrJVacation.
 *
 * @see power.ejb.hr.ca.HrJVacation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJVacationFacade implements HrJVacationFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	private static final String BLANK = "";
	/** 是否使用 */
	private String IS_USE_Y = "Y";
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	/** 字符串: 空字符串 */
	public final String STRING_BLANK = "";

	/**
	 * Perform an initial save of a previously unsaved HrJVacation entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrJVacation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJVacation entity) {
		LogUtil.log("saving HrJVacation instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrJVacation entity.
	 *
	 * @param entity
	 *            HrJVacation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJVacation entity) {
		LogUtil.log("deleting HrJVacation instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJVacation.class, entity
					.getVacationid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrJVacation entity and return it or a copy of
	 * it to the sender. A copy of the HrJVacation entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrJVacation entity to update
	 * @return HrJVacation the persisted HrJVacation entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJVacation update(HrJVacation entity) {
		LogUtil.log("updating HrJVacation instance", Level.INFO, null);
		try {
			HrJVacation result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 请假登记表更新
	 * @param  HrJVacation entity
	 *          请假登记表bean
	 * @author liuxin
	 * @throws DataChangeException
	 *          数据被修改异常
	 * @return  HrJVacation
	 */
	public HrJVacation update1(HrJVacation entity) throws DataChangeException,
			ParseException {
		LogUtil.log("updating HrJVacation instance", Level.INFO, null);
		try {
			// 排他处理
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			HrJVacation lastBean = this.findById(entity.getVacationid());
			String lastTime = lastBean.getLastModifiyDate().toString();
			String thisTime = sdf.format(entity.getLastModifiyDate());
			Date lastDate = sdf.parse(lastTime);
			Date thisDate = sdf.parse(thisTime);
			if (lastDate.getTime() != thisDate.getTime()) {
				throw new DataChangeException(null);
			}
			entity.setLastModifiyDate(new Date());
			HrJVacation result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJVacation findById(Long id) {
		LogUtil.log("finding HrJVacation instance with id: " + id, Level.INFO,
				null);
		try {
			HrJVacation instance = entityManager.find(HrJVacation.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJVacation entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrJVacation property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJVacation> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJVacation> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJVacation instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJVacation model where model."
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
	 * Find all HrJVacation entities.
	 *
	 * @return List<HrJVacation> all HrJVacation entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJVacation> findAll() {
		LogUtil.log("finding all HrJVacation instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJVacation model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询员工请假信息
	 *
	 * @param strDeptId
	 *            部门id
	 * @param strEmpId
	 *            人员id
	 * @param strStartTime
	 *            开始时间
	 * @param strEndTime
	 *            结束时间
	 * @param strEnterpriseCode
	 *            企业代码
	 * @return PageObject 员工请假信息
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findEmpVacation(String strDeptId, String strEmpId,
			String strStartTime, String strEndTime, String strEnterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException {
		LogUtil.log("EJB:员工请假信息查询正常开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		try {

			int N = 7;
			// 查询sql
			StringBuffer sbd = new StringBuffer();
			// SELECT文
			sbd.append("SELECT ");
			sbd.append("A.EMP_ID , ");
			sbd.append("B.CHS_NAME, ");
			sbd.append("A.DEPT_ID , ");
			sbd.append("C.DEPT_NAME, ");
			sbd.append("A.SIGN_STATE , ");
			sbd.append("TO_CHAR(A.START_TIME, 'yyyy-mm-dd hh24:mi'), ");
			sbd.append("TO_CHAR(A.END_TIME, 'yyyy-mm-dd hh24:mi') , ");
			sbd.append("A.VACATION_DAYS, ");
			sbd.append("A.VACATION_TIME , ");
			sbd.append("D.VACATION_TYPE_ID, ");
			sbd.append("D.VACATION_TYPE, ");
			sbd.append("D.IF_WEEKEND, ");
			sbd.append("A.REASON, ");
			sbd.append("A.WHITHER, ");
			sbd.append("A.MEMO, ");
			sbd
					.append("TO_CHAR(A.LAST_MODIFIY_DATE, 'yyyy-mm-dd hh24:mi:ss'), ");
			sbd.append("A.VACATIONID ");
			int intLength = sbd.length();

			// FROM文
			sbd.append("FROM HR_J_VACATION A ");
			sbd.append("LEFT JOIN HR_J_EMP_INFO B ");
			sbd.append("ON A.EMP_ID = B.EMP_ID ");
			sbd.append("AND B.ENTERPRISE_CODE = ? ");
			sbd.append("LEFT JOIN HR_C_DEPT C ");
			sbd.append("ON A.DEPT_ID = C.DEPT_ID ");
			sbd.append("AND C.ENTERPRISE_CODE = ? ");
			sbd.append("AND C.IS_USE = ? ");
			sbd.append("LEFT JOIN HR_C_VACATIONTYPE D ");
			sbd.append("ON A.VACATION_TYPE_ID = D.VACATION_TYPE_ID ");
			sbd.append("AND D.ENTERPRISE_CODE = ? ");
			sbd.append("AND D.IS_USE = ? ");
			sbd.append("WHERE ");
			sbd.append("A.ENTERPRISE_CODE = ? ");
			sbd.append("AND A.IS_USE = ? ");

			// 人员id
			if (strEmpId != null && !"".equals(strEmpId)) {
				sbd.append("AND A.EMP_ID = ? ");
				N++;
			}
			// 部门id
			if (strDeptId != null && !"".equals(strDeptId)) {
				sbd.append("AND A.DEPT_ID = ? ");
				N++;
			}
			// 开始时间
			if (strStartTime != null && !"".equals(strStartTime)) {
				sbd.append("AND TO_CHAR(A.START_TIME,'yyyy-mm-dd') >= ? ");
				N++;
			}
			// 结束时间
			if (strEndTime != null && !"".equals(strEndTime)) {
				sbd.append("AND TO_CHAR(A.START_TIME,'yyyy-mm-dd') <= ? ");
				N++;
			}
			// ORDER文
			sbd.append("ORDER BY A.DEPT_ID, ");
			sbd.append("A.EMP_ID, ");
			sbd.append("A.START_TIME ");

			// 查询参数数组
			Object[] params = new Object[N];
			int i = 0;
			params[i++] = strEnterpriseCode;
			params[i++] = strEnterpriseCode;
			params[i++] = "Y";// update by sychen 20100831 
//			params[i++] = "U";
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			// 人员id
			if (strEmpId != null && !"".equals(strEmpId)) {
				params[i++] = strEmpId;
			}
			// 部门id
			if (strDeptId != null && !"".equals(strDeptId)) {
				params[i++] = strDeptId;
			}
			// 开始时间
			if (strStartTime != null && !"".equals(strStartTime)) {
				params[i++] = strStartTime;
			}
			// 结束时间
			if (strEndTime != null && !"".equals(strEndTime)) {
				params[i++] = strEndTime;
			}
			// 记录数
			String sqlCount = "SELECT " + " COUNT(A.EMP_ID) "
					+ sbd.substring(intLength, sbd.length());
			List<EmpVacationInfo> list = bll.queryByNativeSQL(sbd.toString(),
					params, rowStartIdxAndCount);
			LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());
			LogUtil.log("EJB: SQL =" + sqlCount, Level.INFO, null);
			List<EmpVacationInfo> arrList = new ArrayList<EmpVacationInfo>();
			if (list != null) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					EmpVacationInfo empVacationInfo = new EmpVacationInfo();
					Object[] data = (Object[]) it.next();
					// 人员id
					if (data[0] != null) {
						empVacationInfo.setEmpId(data[0].toString());
					}
					// 人员名称
					if (data[1] != null) {
						empVacationInfo.setEmpName(data[1].toString());
					}
					// 部门id
					if (data[2] != null) {
						empVacationInfo.setDeptId(data[2].toString());
					}
					// 部门名称
					if (data[3] != null) {
						empVacationInfo.setDeptName(data[3].toString());
					}
					// 审批状态
					if (data[4] != null) {
						empVacationInfo.setSignState(data[4].toString());
					}
					// 开始时间
					if (data[5] != null) {
						empVacationInfo.setStartTime(data[5].toString());
					}
					// 结束时间
					if (data[6] != null) {
						empVacationInfo.setEndTime(data[6].toString());
					}
					// 请假天数
					if (data[7] != null) {
						empVacationInfo.setVacationDays(data[7].toString());
					}
					// 请假时长
					if (data[8] != null) {
						empVacationInfo.setVacationHours(data[8].toString());
					}
					// 假别id
					if (data[9] != null) {
						empVacationInfo.setVacationTypeId(data[9].toString());
					}
					// 请假类别
					if (data[10] != null) {
						empVacationInfo.setVacationType(data[10].toString());
					}
					// 是否包括周未
					if (data[11] != null) {
						empVacationInfo.setIfWeekend(data[11].toString());
					}
					// 原因
					if (data[12] != null) {
						empVacationInfo.setReason(data[12].toString());
					}
					// 去向
					if (data[13] != null) {
						empVacationInfo.setWhither(data[13].toString());
					}
					// 备注
					if (data[14] != null) {
						empVacationInfo.setMemo(data[14].toString());
					}
					// 上次修改时间
					if (data[15] != null) {
						empVacationInfo.setLastModifyDate(data[15].toString());
					}
					// 请假id
					if (data[16] != null) {
						empVacationInfo.setVacationId(data[16].toString());
					}
					arrList.add(empVacationInfo);
				}
			}
			pobj.setList(arrList);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:员工请假信息查询正常结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("EJB:员工请假信息查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
		return pobj;
	}

	/**
	 * 查询假别的已用时长信息
	 *
	 * @param strEmpId
	 *            人员id
	 * @param strDeptId
	 *            部门id
	 * @param strStartTime
	 *            开始时间
	 * @param strVacationTypeId
	 *            假别id
	 * @param blnFlag
	 *            是否修改
	 * @param strVacationId
	 *            请假id
	 * @param strEnterpriseCode
	 *            企业代码
	 * @return PageObject 假别的时长信息
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findVacationUsedHours(String strEmpId, String strDeptId,
			String strStartTime, String strVacationTypeId, boolean blnFlag,
			String strVacationId, String strEnterpriseCode, final int... rowStartIdxAndCount)
			throws SQLException {
		LogUtil.log("EJB:假别的已用时长信息查询正常开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		try {

			int N = 6;
			// 查询sql
			StringBuffer sbd = new StringBuffer();
			// SELECT文
			sbd.append("SELECT ");
			sbd.append("SUM(A.VACATION_TIME) ");

			// FROM文
			sbd.append("FROM HR_J_VACATION A ");
			sbd.append("WHERE ");
			sbd.append("A.ENTERPRISE_CODE = ? ");
			sbd.append("AND A.IS_USE = ? ");
			sbd.append("AND A.EMP_ID = ? ");
			sbd.append("AND A.DEPT_ID = ? ");
			sbd.append("AND TO_CHAR(A.START_TIME,'yyyy')  = ? ");
			sbd.append("AND A.VACATION_TYPE_ID = ? ");
			if (blnFlag) {
				sbd.append("AND A.VACATIONID  <> ? ");
				N = 7;
			}
			sbd.append("GROUP BY A.EMP_ID");
			// 查询参数数组
			Object[] params = new Object[N];
			int i = 0;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strEmpId;
			params[i++] = strDeptId;
			params[i++] = strStartTime.substring(0, 4);
			params[i++] = strVacationTypeId;
			if (blnFlag) {
				params[i++] = strVacationId;
			}

			List<HrJVacation> list = bll.queryByNativeSQL(sbd.toString(),
					params, rowStartIdxAndCount);
			LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
			List<HrJVacation> arrList = new ArrayList<HrJVacation>();
			if (list != null) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					HrJVacation hrJVacation = new HrJVacation();
					Object data = it.next();
					// 计划时长
					if (data!= null) {
						hrJVacation.setVacationTime(Double.parseDouble(data
								.toString()));
					}
					arrList.add(hrJVacation);
				}
			}
			pobj.setList(arrList);
			LogUtil.log("EJB:假别的已用时长信息查询正常结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("EJB:假别的已用时长信息查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
		return pobj;
	}

	/**
	 * 查询请假期间重复信息
	 *
	 * @param strEmpId
	 *            人员id
	 * @param strEndTime
	 *            结束时间
	 * @param strStartTime
	 *            开始时间
	 * @param strVacationTypeId
	 *            请假id
	 * @param strEnterpriseCode
	 *            企业代码
	 * @param blnFlag
	 *            是否修改
	 * @return PageObject 假别的时长信息
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findVacationRepeat(String strEmpId, String strEndTime,
			String strStartTime, String strVacationId,
			String strEnterpriseCode, boolean blnFlag,
			final int... rowStartIdxAndCount) throws SQLException {
		LogUtil.log("EJB:请假期间重复信息查询正常开始", Level.INFO, null);
		PageObject pobj = new PageObject();
		try {

			int N = 5;
			// 查询sql
			StringBuffer sbd = new StringBuffer();
			// SELECT文
			sbd.append("SELECT ");
			sbd.append("COUNT(A.VACATIONID) ");
			int intLength = sbd.length();

			// FROM文
			sbd.append("FROM HR_J_VACATION A ");
			sbd.append("WHERE ");
			sbd.append("A.ENTERPRISE_CODE = ? ");
			sbd.append("AND A.IS_USE = ? ");
			sbd.append("AND A.EMP_ID = ? ");
			sbd
					.append("AND (TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') - NVL(A.CLEAR_DATE,A.END_TIME))");
			sbd
					.append("*(TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS') - A.START_TIME) < 0 ");
			if (blnFlag) {
				sbd.append("AND A.VACATIONID  <> ? ");
				N = 6;
			}

			// 查询参数数组
			Object[] params = new Object[N];
			int i = 0;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strEmpId;
			params[i++] = strStartTime;
			params[i++] = strEndTime;
			if (blnFlag) {
				params[i++] = strVacationId;
			}

			// 记录数
			String sqlCount = "SELECT " + " COUNT(A.VACATIONID) "
					+ sbd.substring(intLength, sbd.length());
			List<EmpVacationInfo> list = bll.queryByNativeSQL(sbd.toString(),
					params, rowStartIdxAndCount);
			LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
					.toString());
			LogUtil.log("EJB: SQL =" + sqlCount, Level.INFO, null);
			List<EmpVacationInfo> arrList = new ArrayList<EmpVacationInfo>();
			if (list != null) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					EmpVacationInfo empVacationInfo = new EmpVacationInfo();
					Object data = it.next();
					// 重复数
					if (data != null) {
						empVacationInfo.setRepeat(Long.parseLong(data
								.toString()));
					}
					arrList.add(empVacationInfo);
				}
			}
			pobj.setList(arrList);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:请假期间重复信息查询正常结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("EJB:请假期间重复信息查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
		return pobj;
	}

	/**
	 * 保存操作
	 *
	 * @param lstSaveHrJVacation
	 *            新增数据
	 * @param lstUpdateHrJVacation
	 *            修改数据
	 * @param lstDeleteHrJVacation
	 *            删除数据
	 * @throws DataChangeException
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public List save(List<HrJVacation> lstSaveHrJVacation,
			List<HrJVacation> lstUpdateHrJVacation,
			List<HrJVacation> lstDeleteHrJVacation) throws DataChangeException,
			SQLException {
		try {
			LogUtil.log("EJB:员工请假登记保存处理开始。", Level.INFO, null);
			Long returnId = 0l;
			List lstReturn = new ArrayList();
			Date returnDate = new Date();
			// 日期的格式化
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					DATE_FORMAT_YYYYMMDD_HHMMSS);
			// 增加部门设置表信息
			if (lstSaveHrJVacation.size() > 0) {
				Long id = bll.getMaxId("HR_J_VACATION", "VACATIONID");
				for (int i = 0; i < lstSaveHrJVacation.size(); i++) {
					HrJVacation entity = lstSaveHrJVacation.get(i);
					if (i == 0) {
						returnId = id;
					}
					entity.setVacationid(id++);
					save(entity);
				}
			}
			// 更新部门设置表信息
			if (lstUpdateHrJVacation.size() > 0) {
				for (int i = 0; i < lstUpdateHrJVacation.size(); i++) {
					HrJVacation entity = lstUpdateHrJVacation.get(i);
					// 先前的日期
					Date frontDate = entity.getLastModifiyDate();
					String strFrontDate = dateFormat.format(frontDate);
					// 请假ID
					Long lgnVacationid = entity.getVacationid();
					// 数据库中的日期
					HrJVacation entityDB = findById(lgnVacationid);
					Date newDate = entityDB.getLastModifiyDate();
					String strNewDate = dateFormat.format(newDate);
					// 排他操作
					if (!strNewDate.equals(strFrontDate)) {
						throw new DataChangeException(null);
					} else {
						LogUtil.log("EJB:更新员工请假登记数据开始。", Level.INFO, null);
						// 修改时间
						entity.setLastModifiyDate(new java.util.Date());
						if (i == 0) {
							returnDate = new java.util.Date();
						}
						update(entity);
						LogUtil.log("EJB:更新员工请假登记数据结束。", Level.INFO, null);
					}
				}
			}
			// 删除部门设置表信息
			if (lstDeleteHrJVacation.size() > 0) {
				for (int i = 0; i < lstDeleteHrJVacation.size(); i++) {
					HrJVacation entity = lstDeleteHrJVacation.get(i);
					// 先前的日期
					Date frontDate = entity.getLastModifiyDate();
					String strFrontDate = dateFormat.format(frontDate);
					// 请假ID
					Long lgnVacationid = entity.getVacationid();
					// 数据库中的日期
					HrJVacation entityDB = findById(lgnVacationid);
					Date newDate = entityDB.getLastModifiyDate();
					String strNewDate = dateFormat.format(newDate);
					// 排他操作
					if (!strNewDate.equals(strFrontDate)) {
						throw new DataChangeException(null);
					} else {
						LogUtil.log("EJB:删除员工请假登记数据开始。", Level.INFO, null);
						// 修改时间
						entity.setLastModifiyDate(new java.util.Date());
						update(entity);
						LogUtil.log("EJB:删除员工请假登记数据结束。", Level.INFO, null);
					}
					update(entity);
				}
			}

			LogUtil.log("EJB:员工请假登记保存处理结束。", Level.INFO, null);
			lstReturn.add(returnId);
			lstReturn.add(returnDate);
			return lstReturn;
		} catch (DataChangeException de) {
			LogUtil.log("EJB:员工请假登记保存处理失败。", Level.SEVERE, null);
			throw de;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:员工请假登记保存处理失败。", Level.SEVERE, null);
			throw new SQLException("数据库操作异常!");
		}
	}

	/**
	 * 检索员工销假登记信息
	 *
	 * @param enterpriseCode
	 *            企业编码 empId 员工Id deptId 部门Id
	 * @return List<employeeLeaveBean>
	 * @author liuxin
	 */

	@SuppressWarnings( { "unchecked", "unchecked" })
	public List<employeeLeaveBean> getLeaveInfo(String enterpriseCode,
			String empId, String deptId, int... rowStartIdxAndCount) {
		// 签字状态（"2"为"已终结"）
		final String SIGN_STATE = "2";
		// 是否销假 （"0"为"否"）
		final String IF_CLEAR = "0";
		LogUtil.log("EJB:员工请假登记查询开始。", Level.INFO, null);
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT B.EMP_ID");
			sql.append(",B.CHS_NAME");
			sql.append(" ,C.DEPT_ID");
			sql.append(" ,C.DEPT_NAME");
			sql.append(",D.VACATION_TYPE_ID");
			sql.append(",D.VACATION_TYPE");
			sql.append(",A.VACATIONID");
			sql.append(",to_char(A.START_TIME,'YYYY-MM-DD HH24:MI')");
			sql.append(",to_char(A.END_TIME,'YYYY-MM-DD HH24:MI')");
			sql.append(",A.VACATION_DAYS");
			sql.append(",A.VACATION_TIME");
			sql.append(",A.REASON");
			sql.append(" ,A.WHITHER");
			sql.append(",A.MEMO");
			sql.append(",to_char(A.LAST_MODIFIY_DATE,'YYYY-MM-DD HH24:MI:SS')");
			sql.append(",D.IF_WEEKEND");
			sql.append(" FROM HR_J_VACATION A");
			sql.append(" LEFT JOIN HR_J_EMP_INFO B ON A.EMP_ID =");
			sql.append("B.EMP_ID  AND B.ENTERPRISE_CODE = ?");
			sql.append(" LEFT JOIN HR_C_DEPT C ON A.DEPT_ID ");
			sql.append("=C.DEPT_ID AND C.ENTERPRISE_CODE = ?");
			sql.append("  LEFT JOIN HR_C_VACATIONTYPE D ON ");
			sql.append("A.VACATION_TYPE_ID = D.VACATION_TYPE_ID");
			sql.append(" AND D.ENTERPRISE_CODE = ?");
//			sql.append(" AND D.IS_USE =? ");
			sql.append(" WHERE A.SIGN_STATE = ?");
			sql.append(" AND A.IF_CLEAR = ?");
			sql.append(" AND A.ENTERPRISE_CODE = ?");
			sql.append(" AND A.IS_USE =? ");

			if (empId != null && !"".equals(empId)) {
				String strEmp = " AND A.EMP_ID =" + empId;
				sql.append(strEmp);
			}
			if (deptId != null && !"".equals(deptId)) {
				String strDept = " AND A.DEPT_ID =" + deptId;
				sql.append(strDept);
			}
			sql.append(" ORDER BY A.VACATIONID");
			Object[] params = new Object[7];
			params[0] = enterpriseCode;
			params[1] = enterpriseCode;
			params[2] = enterpriseCode;
//			params[3] = IS_USE_Y;
			params[3] = SIGN_STATE;
			params[4] = IF_CLEAR;
			params[5] = enterpriseCode;
			params[6] = IS_USE_Y;
			List list = bll.queryByNativeSQL(sql.toString(), params,
					rowStartIdxAndCount);
			List<employeeLeaveBean> arrlist = new ArrayList<employeeLeaveBean>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				employeeLeaveBean entity = new employeeLeaveBean();
				// 员工id
				if (data[0] != null && !data[0].toString().equals(BLANK)) {
					entity.setEmpId(data[0].toString());
				}
				// 员工姓名
				if (data[1] != null && !data[1].toString().equals(BLANK)) {
					entity.setEmpName(data[1].toString());
				}
				// 部门id
				if (data[2] != null && !data[2].toString().equals(BLANK)) {
					entity.setDeptId(data[2].toString());
				}
				// 部门名
				if (data[3] != null && !data[3].toString().equals(BLANK)) {
					entity.setDeptName(data[3].toString());
				}
				// 假别id
				if (data[4] != null && !data[4].toString().equals(BLANK)) {
					entity.setLeaveTypeId(data[4].toString());
				}
				// 假别
				if (data[5] != null && !data[5].toString().equals(BLANK)) {
					entity.setLeaveType(data[5].toString());
				}
				// 请假ID
				if (data[6] != null && !data[6].toString().equals(BLANK)) {
					entity.setVacationId(data[6].toString());
				}
				// 请假开始时间
				if (data[7] != null && !data[7].toString().equals(BLANK)) {
					entity.setStartTime(data[7].toString());
				}
				// 请假结束时间
				if (data[8] != null && !data[8].toString().equals(BLANK)) {
					entity.setEndTime(data[8].toString());
				}
				// 请假天数
				if (data[9] != null && !data[9].toString().equals(BLANK)) {
					entity.setLeaveDays(data[9].toString());
				}
				// 请假时长
				if (data[10] != null && !data[10].toString().equals(BLANK)) {
					entity.setLeaveTime(data[10].toString());
				}
				// 原因
				if (data[11] != null && !data[11].toString().equals(BLANK)) {
					entity.setReason(data[11].toString());
				}
				// 去向
				if (data[12] != null && !data[12].toString().equals(BLANK)) {
					entity.setGoWhere(data[12].toString());
				}
				// 备注
				if (data[13] != null && !data[13].toString().equals(BLANK)) {
					entity.setMemo(data[13].toString());
				}
				// 上次修改时间
				if (data[14] != null && !data[14].toString().equals(BLANK)) {
					entity.setLastModifyTime(data[14].toString());
				}
				// 是否包含周末
				if (data[15] != null && !data[15].toString().equals(BLANK)) {
					entity.setIfWeekend(data[15].toString());
				}
				arrlist.add(entity);
			}

			LogUtil.log("EJB:员工请假登记查询结束。", Level.INFO, null);
			return arrlist;
		} catch (Exception e) {
			LogUtil.log("EJB:员工请假登记查询失败。", Level.SEVERE, null);
			return null;
		}
	}

	/**
	 * 检索员工销假登记信息当部门为空时
	 *
	 * @param enterpriseCode
	 *            企业编码 empId 员工Id
	 * @return List<employeeLeaveBean>
	 * @author liuxin
	 */

	@SuppressWarnings( { "unchecked", "unchecked" })
	public List<employeeLeaveBean> getLeaveInfoDeptNull(String enterpriseCode,
			String empId, int... rowStartIdxAndCount) {
		// 签字状态（"2"为"已终结"）
		final String SIGN_STATE = "2";
		// 是否销假 （"0"为"否"）
		final String IF_CLEAR = "0";
		LogUtil.log("EJB:员工请假登记查询开始。", Level.INFO, null);
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT B.EMP_ID");
			sql.append(",B.CHS_NAME");
			sql.append(",D.VACATION_TYPE_ID");
			sql.append(",D.VACATION_TYPE");
			sql.append(",A.VACATIONID");
			sql.append(",to_char(A.START_TIME,'YYYY-MM-DD HH24:MI')");
			sql.append(",to_char(A.END_TIME,'YYYY-MM-DD HH24:MI')");
			sql.append(",A.VACATION_DAYS");
			sql.append(",A.VACATION_TIME");
			sql.append(",A.REASON");
			sql.append(" ,A.WHITHER");
			sql.append(",A.MEMO");
			sql.append(",to_char(A.LAST_MODIFIY_DATE,'YYYY-MM-DD HH24:MI:SS')");
			sql.append(",D.IF_WEEKEND");
			sql.append(" FROM HR_J_VACATION A");
			sql.append(" LEFT JOIN HR_J_EMP_INFO B ON A.EMP_ID =");
			sql.append("B.EMP_ID  AND B.ENTERPRISE_CODE = ?");
			sql.append("  LEFT JOIN HR_C_VACATIONTYPE D ON ");
			sql.append("A.VACATION_TYPE_ID = D.VACATION_TYPE_ID");
			sql.append(" AND D.ENTERPRISE_CODE = ?");
//			sql.append(" AND D.IS_USE =? ");
			sql.append(" WHERE A.SIGN_STATE = ?");
			sql.append(" AND A.IF_CLEAR = ?");
			sql.append(" AND A.ENTERPRISE_CODE = ?");
			sql.append(" AND A.IS_USE =? ");
			sql.append(" AND A.DEPT_ID is null ");

			if (empId != null && !"".equals(empId)) {
				String strEmp = " AND A.EMP_ID =" + empId;
				sql.append(strEmp);
			}
			sql.append(" ORDER BY A.VACATIONID");
			Object[] params = new Object[6];
			params[0] = enterpriseCode;
			params[1] = enterpriseCode;
//			params[2] = IS_USE_Y;
			params[2] = SIGN_STATE;
			params[3] = IF_CLEAR;
			params[4] = enterpriseCode;
			params[5] = IS_USE_Y;
			List list = bll.queryByNativeSQL(sql.toString(), params,
					rowStartIdxAndCount);
			List<employeeLeaveBean> arrlist = new ArrayList<employeeLeaveBean>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				employeeLeaveBean entity = new employeeLeaveBean();
				// 员工id
				if (data[0] != null && !data[0].toString().equals(BLANK)) {
					entity.setEmpId(data[0].toString());
				}
				// 员工姓名
				if (data[1] != null && !data[1].toString().equals(BLANK)) {
					entity.setEmpName(data[1].toString());
				}
				// 假别id
				if (data[2] != null && !data[2].toString().equals(BLANK)) {
					entity.setLeaveTypeId(data[2].toString());
				}
				// 假别
				if (data[3] != null && !data[3].toString().equals(BLANK)) {
					entity.setLeaveType(data[3].toString());
				}
				// 请假ID
				if (data[4] != null && !data[4].toString().equals(BLANK)) {
					entity.setVacationId(data[4].toString());
				}
				// 请假开始时间
				if (data[5] != null && !data[5].toString().equals(BLANK)) {
					entity.setStartTime(data[5].toString());
				}
				// 请假结束时间
				if (data[6] != null && !data[6].toString().equals(BLANK)) {
					entity.setEndTime(data[6].toString());
				}
				// 请假天数
				if (data[7] != null && !data[7].toString().equals(BLANK)) {
					entity.setLeaveDays(data[7].toString());
				}
				// 请假时长
				if (data[8] != null && !data[8].toString().equals(BLANK)) {
					entity.setLeaveTime(data[8].toString());
				}
				// 原因
				if (data[9] != null && !data[9].toString().equals(BLANK)) {
					entity.setReason(data[9].toString());
				}
				// 去向
				if (data[10] != null && !data[10].toString().equals(BLANK)) {
					entity.setGoWhere(data[10].toString());
				}
				// 备注
				if (data[11] != null && !data[11].toString().equals(BLANK)) {
					entity.setMemo(data[11].toString());
				}
				// 上次修改时间
				if (data[12] != null && !data[12].toString().equals(BLANK)) {
					entity.setLastModifyTime(data[12].toString());
				}
				// 是否包含周末
				if (data[13] != null && !data[13].toString().equals(BLANK)) {
					entity.setIfWeekend(data[13].toString());
				}
				arrlist.add(entity);
			}

			LogUtil.log("EJB:员工请假登记查询结束。", Level.INFO, null);
			return arrlist;
		} catch (Exception e) {
			LogUtil.log("EJB:员工请假登记查询失败。", Level.SEVERE, null);
			return null;
		}
	}

	/**
	 * 获取员工姓名,ID,部门ID,名称
	 *
	 * @param enterpriseCode
	 *            企业编码 deptId 部门Id
	 * @return List<deptClearLeaveEmp>
	 */
	@SuppressWarnings("unchecked")
	public List<DeptClearLeaveEmp> getEmpIdName(String enterpriseCode,
			String deptId) {
		LogUtil.log("EJB:获取员工姓名,ID,部门ID,名称开始。", Level.INFO, null);
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT A.EMP_ID");
			sql.append(",A.CHS_NAME");
			sql.append(",B.DEPT_ID");
			sql.append(",B.DEPT_NAME");
			sql.append(" FROM HR_J_EMP_INFO A");
			sql.append(",HR_C_DEPT B ");
			sql.append("WHERE A.IS_USE = ?");
			sql.append(" AND B.IS_USE = ? ");
			sql.append(" AND A.ENTERPRISE_CODE = ?");
			sql.append(" AND B.ENTERPRISE_CODE = ?");
			sql.append(" AND A.DEPT_ID = B.DEPT_ID");
			sql.append(" AND B.DEPT_ID = ?");
			sql.append(" ORDER BY A.EMP_ID");
			Object[] params = new Object[5];
			params[0] = IS_USE_Y;
			params[1] = "Y";// update by sychen 20100831 
//			params[1] = "U";
			params[2] = enterpriseCode;
			params[3] = enterpriseCode;
			params[4] = deptId;
			List list = bll.queryByNativeSQL(sql.toString(), params);
			List<DeptClearLeaveEmp> arrlist = new ArrayList<DeptClearLeaveEmp>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DeptClearLeaveEmp bean = new DeptClearLeaveEmp();
				if (data[0] != null && !data[0].toString().equals(BLANK)) {
					bean.setEmpId(data[0].toString());
				}
				if (data[1] != null && !data[1].toString().equals(BLANK)) {
					bean.setEmpName(data[1].toString());
				}
				if (data[2] != null && !data[2].toString().equals(BLANK)) {
					bean.setDeptId(data[2].toString());
				}
				if (data[3] != null && !data[3].toString().equals(BLANK)) {
					bean.setDeptName(data[3].toString());
				}
				arrlist.add(bean);
			}
			LogUtil.log("EJB:获取员工姓名,ID,部门ID,名称结束。", Level.INFO, null);
			return arrlist;
		} catch (Exception e) {
			LogUtil.log("EJB:获取员工姓名,ID,部门ID,名称失败。", Level.SEVERE, null);
			return null;
		}
	}

	/**
	 * 获取员工姓名,ID,当部门为空时
	 *
	 * @param enterpriseCode
	 *            企业编码 deptId 部门Id
	 * @return List<deptClearLeaveEmp>
	 */
	@SuppressWarnings("unchecked")
	public List<DeptClearLeaveEmp> getEmpIdNameDeptNull(String enterpriseCode) {
		LogUtil.log("EJB:获取员工姓名,ID,当部门为空时开始。", Level.INFO, null);
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT A.EMP_ID");
			sql.append(",A.CHS_NAME");
			sql.append(" FROM HR_J_EMP_INFO A");
			sql.append(" WHERE A.IS_USE = ?");
			sql.append(" AND A.ENTERPRISE_CODE = ?");
			sql.append(" AND A.DEPT_ID is null");
			sql.append(" ORDER BY A.EMP_ID");
			Object[] params = new Object[2];
			params[0] = IS_USE_Y;
			params[1] = enterpriseCode;
			List list = bll.queryByNativeSQL(sql.toString(), params);
			List<DeptClearLeaveEmp> arrlist = new ArrayList<DeptClearLeaveEmp>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DeptClearLeaveEmp bean = new DeptClearLeaveEmp();
				if (data[0] != null && !data[0].toString().equals(BLANK)) {
					bean.setEmpId(data[0].toString());
				}
				if (data[1] != null && !data[1].toString().equals(BLANK)) {
					bean.setEmpName(data[1].toString());
				}
				arrlist.add(bean);
			}
			LogUtil.log("EJB:获取员工姓名,ID,当部门为空时结束。", Level.INFO, null);
			return arrlist;
		} catch (Exception e) {
			LogUtil.log("EJB:获取员工姓名,ID,当部门为空时失败。", Level.SEVERE, null);
			return null;
		}
	}

	/**
	 * 获取请假统计报表详细信息
	 *
	 * @param enterpriseCode
	 *            企业编码
	 * @param year
	 *            统计年份
	 * @param month
	 *            统计月份
	 * @return
	 *           PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAttendentStaticDetailInfo(String enterpriseCode,String year,String month){
		LogUtil.log("EJB:获取请假统计报表详细信息开始。", Level.INFO, null);
		PageObject pobj = new PageObject();
		try{
			String sql = "SELECT V.EMP_ID " +
                         ",E.CHS_NAME " +
                         ",D.PDEPT_ID " +
                         ",D1.DEPT_NAME AS PDEPT_NAME " +
                         ",D.DEPT_ID " +
                         ",D.DEPT_NAME " +
                         ",V.VACATION_TYPE_ID " +
                         ",V.DAYS " +
                         ",F.VACATION_TYPE " +
                         "FROM HR_D_VACATIONTOTAL V " +
                         ",HR_C_DEPT D " +
                         ",HR_C_DEPT D1 " +
                         ",HR_J_EMP_INFO E " +
                         ",HR_C_VACATIONTYPE F " +
                         "WHERE V.IS_USE = ? " +
                         "AND D.IS_USE = ? " +
                         "AND D1.IS_USE(+) = ? " +
                         "AND E.IS_USE = ? " +
                         "AND F.IS_USE = ? " +
                         "AND V.ENTERPRISE_CODE = ? " +
                         "AND D.ENTERPRISE_CODE = ? " +
                         "AND D1.ENTERPRISE_CODE(+) = ? " +
                         "AND E.ENTERPRISE_CODE = ? " +
                         "AND F.ENTERPRISE_CODE = ? " +
                         "AND V.ATTENDANCE_YEAR = ? " +
                         "AND V.ATTENDANCE_MONTH = ? " +
                         "AND V.EMP_ID = E.EMP_ID " +
                         "AND V.DEPT_ID = D.DEPT_ID " +
                         "AND D1.DEPT_ID(+) = D.PDEPT_ID " +
                         "AND F.VACATION_TYPE_ID = V.VACATION_TYPE_ID " +
                         "AND V.DAYS > 0 " +
                         "ORDER BY D.PDEPT_ID, D.DEPT_ID, V.EMP_ID";
			Object[] params = new Object[12];
			params[0] = IS_USE_Y;
			params[1] = "Y";// update by sychen 20100831 
			params[2] = "Y";
//			params[1] = "U";
//			params[2] = "U";
			params[3] = IS_USE_Y;
			params[4] = IS_USE_Y;
			params[5] = enterpriseCode;
			params[6] = enterpriseCode;
			params[7] = enterpriseCode;
			params[8] = enterpriseCode;
			params[9] = enterpriseCode;
			params[10] = year;
			params[11] = month;
			List list = bll.queryByNativeSQL(sql, params);
			List<StaticDetailBean> arrlist = new ArrayList<StaticDetailBean>();
			LogUtil.log("EJB: SQL =" + sql, Level.INFO, null);
			Iterator it = list.iterator();
			while (it.hasNext()){
				Object[] data = (Object[]) it.next();
				StaticDetailBean bean = new StaticDetailBean();
				// 员工ID
				if(data[0]!=null){
					bean.setEmpId(data[0].toString());
				}
				// 员工姓名
				if(data[1]!=null){
					bean.setChsName(data[1].toString());
				}
				// 上级部门ID
				if(data[2]!=null){
					bean.setPdeptId(data[2].toString());
				}
				// 上级部门名称
				if(data[3]!=null){
					bean.setPdeptName(data[3].toString());
				}
				// 部门ID
				if(data[4]!=null){
					bean.setDeptId(data[4].toString());
				}
				// 部门名称
				if(data[5]!=null){
					bean.setDeptName(data[5].toString());
				}
				// 统计分类ID
				if(data[6]!=null){
					bean.setVacationTypeId(data[6].toString());
				}
				// 天数
				if(data[7]!=null){
					bean.setDays(data[7].toString());
				}
				// 统计分类名称
				if(data[8]!=null){
					bean.setVacationType(data[8].toString());
				}
				arrlist.add(bean);
			}
			// 补上在假别维护表存在但是在请假统计表里没有的假别
			if(arrlist!=null&&arrlist.size()>0){
				String sql2 = "SELECT " +
                              "F.VACATION_TYPE_ID " +
                              ",F.VACATION_TYPE " +
                              "FROM " +
                              "HR_C_VACATIONTYPE F " +
                              "WHERE F.IS_USE = ? " +
                              "AND F.ENTERPRISE_CODE = ? ";
				Object[] params2 = new Object[2];
				params2[0] = IS_USE_Y;
				params2[1] = enterpriseCode;
				List list2 = bll.queryByNativeSQL(sql2, params2);
				LogUtil.log("EJB: SQL2 =" + sql2, Level.INFO, null);
				StaticDetailBean tempBean = arrlist.get(0);
				Iterator it2 = list2.iterator();
				while(it2.hasNext()){
					// 复制之前某一个员工的资料
					Object[] data = (Object[]) it2.next();
					StaticDetailBean bean = new StaticDetailBean();
					bean.setEmpId(tempBean.getEmpId());
					bean.setChsName(tempBean.getChsName());
					bean.setDeptId(tempBean.getDeptId());
					bean.setDeptName(tempBean.getDeptName());
					bean.setPdeptId(tempBean.getPdeptId());
					bean.setPdeptName(tempBean.getPdeptName());
					// 补上假别
					if(data[0]!=null){
						bean.setVacationTypeId(data[0].toString());
					}
					if(data[1]!=null){
						bean.setVacationType(data[1].toString());
					}
					arrlist.add(bean);
				}
			}
			pobj.setList(arrlist);
			LogUtil.log("EJB:获取请假统计报表详细信息结束。", Level.INFO, null);
		}catch (Exception e) {
			LogUtil.log("EJB:获取请假统计报表详细信息失败。", Level.INFO, null);
			return null;
		}
		return pobj;
	}

	 /**
     * 查询部门员工信息
     * @param lgnDeptId 部门id
     * @param lgnEmpId 人员id
     * @param strEnterpriseCode 企业代码
     * @return PageObject 部门员工信息
     * @throws SQLException
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public PageObject getDeptEmpInfo(Long lgnDeptId, Long lgnEmpId, String strEnterpriseCode)
	    throws SQLException {
	LogUtil.log("EJB:部门员工信息查询正常开始", Level.INFO, null);
	PageObject pobj = new PageObject();
	try {
	    // 查询sql
	    StringBuffer sbd = new StringBuffer();
	    int N =5;
	    // SELECT文
	    sbd.append("SELECT A.EMP_ID");
	    sbd.append(",A.CHS_NAME");
	    sbd.append(",B.DEPT_ID");
	    sbd.append(",B.DEPT_NAME");
	    int intLength = sbd.length();

	    // FROM文
	    sbd.append(" FROM HR_J_EMP_INFO A");
	    sbd.append(",HR_C_DEPT B ");
	    sbd.append("WHERE A.IS_USE = ?");
	    sbd.append(" AND B.IS_USE = ? ");
	    sbd.append(" AND A.ENTERPRISE_CODE = ?");
	    sbd.append(" AND B.ENTERPRISE_CODE = ?");
	    sbd.append(" AND A.DEPT_ID = B.DEPT_ID");
	    sbd.append(" AND B.DEPT_ID = ?");
	    if (lgnEmpId != null) {
		    sbd.append(" AND A.EMP_ID = ?");
		    N++;
	    }

	    // 查询参数数组
	    Object[] params = new Object[N];
	    int i = 0;
	    params[i++] = IS_USE_Y;
	    params[i++] = "Y";// update by sychen 20100831 
//	    params[i++] = "U";
	    params[i++] = strEnterpriseCode;
	    params[i++] = strEnterpriseCode;
	    params[i++] = lgnDeptId;
	    if (lgnEmpId != null) {
	    	params[i++] = lgnEmpId;
	    }

	    // 记录数
	    String sqlCount = "SELECT " + " COUNT(A.EMP_ID) "
		    + sbd.substring(intLength, sbd.length());
	    List<DeptClearLeaveEmp> list = bll.queryByNativeSQL(sbd.toString(), params);
	    LogUtil.log("EJB: SQL =" + sbd.toString(), Level.INFO, null);
	    Long totalCount = Long.parseLong(bll.getSingal(sqlCount, params)
		    .toString());
	    LogUtil.log("EJB: SQL =" + sqlCount, Level.INFO, null);
	    List<DeptClearLeaveEmp> arrList = new ArrayList<DeptClearLeaveEmp>();
	    if (list != null) {
		Iterator it = list.iterator();
		while (it.hasNext()) {
			DeptClearLeaveEmp deptClearLeaveEmp = new DeptClearLeaveEmp();
		    Object[] data = (Object[]) it.next();
		    // 人员id
		    if (data[0] != null) {
		    	deptClearLeaveEmp.setEmpId(data[0].toString());
		    }
		    // 人员姓名
		    if (data[1] != null) {
			deptClearLeaveEmp.setEmpName(data[1].toString());
		    }
		    // 部门id
		    if (data[2] != null) {
		    	deptClearLeaveEmp.setDeptId(data[2].toString());
		    }
		    // 部门名称
		    if (data[3] != null) {
		    	deptClearLeaveEmp.setDeptName(data[3].toString());
		    }
		    arrList.add(deptClearLeaveEmp);
		}
	    }
	    pobj.setList(arrList);
	    pobj.setTotalCount(totalCount);
	    LogUtil.log("EJB:部门员工信息查询正常结束", Level.INFO, null);
	} catch (RuntimeException e) {
	    LogUtil.log("EJB:部门员工信息查询失败", Level.SEVERE, e);
	    throw new SQLException();
	}
	return pobj;
    }

    /**
	 * 获取加班统计报表详细信息
	 *
	 * @param enterpriseCode
	 *            企业编码
	 * @param year
	 *            统计年份
	 * @param month
	 *            统计月份
	 * @return
	 *           PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorkOvertimeStaticDetailInfo(String enterpriseCode,String year,String month){
		LogUtil.log("EJB:获取加班统计报表详细信息开始。", Level.INFO, null);
		PageObject pobj = new PageObject();
		try{
			String sql = "SELECT V.EMP_ID " +
                         ",E.CHS_NAME " +
                         ",D.PDEPT_ID " +
                         ",D1.DEPT_NAME AS PDEPT_NAME " +
                         ",D.DEPT_ID " +
                         ",D.DEPT_NAME " +
                         ",V.OVERTIME_TYPE_ID " +
                         ",V.DAYS " +
                         ",F.OVERTIME_TYPE " +
                         "FROM HR_D_OVERTIMETOTAL V " +
                         ",HR_C_DEPT D " +
                         ",HR_C_DEPT D1 " +
                         ",HR_J_EMP_INFO E " +
                         ",HR_C_OVERTIME F " +
                         "WHERE V.IS_USE = ? " +
                         "AND D.IS_USE = ? " +
                         "AND D1.IS_USE(+) = ? " +
                         "AND E.IS_USE = ? " +
                         "AND F.IS_USE = ? " +
                         "AND V.ENTERPRISE_CODE = ? " +
                         "AND D.ENTERPRISE_CODE = ? " +
                         "AND D1.ENTERPRISE_CODE(+) = ? " +
                         "AND E.ENTERPRISE_CODE = ? " +
                         "AND F.ENTERPRISE_CODE = ? " +
                         "AND V.ATTENDANCE_YEAR = ? " +
                         "AND V.ATTENDANCE_MONTH = ? " +
                         "AND V.EMP_ID = E.EMP_ID " +
                         "AND V.DEPT_ID = D.DEPT_ID " +
                         "AND D1.DEPT_ID(+) = D.PDEPT_ID " +
                         "AND F.OVERTIME_TYPE_ID = V.OVERTIME_TYPE_ID " +
                         "AND V.DAYS > 0 " +
                         "ORDER BY D.PDEPT_ID, D.DEPT_ID, V.EMP_ID";
			Object[] params = new Object[12];
			params[0] = IS_USE_Y;
			params[1] = "Y";// update by sychen 20100831 
			params[2] = "Y";
//			params[1] = "U";
//			params[2] = "U";
			params[3] = IS_USE_Y;
			params[4] = IS_USE_Y;
			params[5] = enterpriseCode;
			params[6] = enterpriseCode;
			params[7] = enterpriseCode;
			params[8] = enterpriseCode;
			params[9] = enterpriseCode;
			params[10] = year;
			params[11] = month;
			List list = bll.queryByNativeSQL(sql, params);
			List<StaticDetailBean> arrlist = new ArrayList<StaticDetailBean>();
			LogUtil.log("EJB: SQL =" + sql, Level.INFO, null);
			Iterator it = list.iterator();
			while (it.hasNext()){
				Object[] data = (Object[]) it.next();
				StaticDetailBean bean = new StaticDetailBean();
				// 员工ID
				if(data[0]!=null){
					bean.setEmpId(data[0].toString());
				}
				// 员工姓名
				if(data[1]!=null){
					bean.setChsName(data[1].toString());
				}
				// 上级部门ID
				if(data[2]!=null){
					bean.setPdeptId(data[2].toString());
				}
				// 上级部门名称
				if(data[3]!=null){
					bean.setPdeptName(data[3].toString());
				}
				// 部门ID
				if(data[4]!=null){
					bean.setDeptId(data[4].toString());
				}
				// 部门名称
				if(data[5]!=null){
					bean.setDeptName(data[5].toString());
				}
				// 统计分类ID
				if(data[6]!=null){
					bean.setVacationTypeId(data[6].toString());
				}
				// 天数
				if(data[7]!=null){
					bean.setDays(data[7].toString());
				}
				// 统计分类名称
				if(data[8]!=null){
					bean.setVacationType(data[8].toString());
				}
				arrlist.add(bean);
			}
			// 补上在加班维护表存在但是在加班统计表里没有的假别
			if(arrlist!=null&&arrlist.size()>0){
				String sql2 = "SELECT " +
                              "F.OVERTIME_TYPE_ID " +
                              ",F.OVERTIME_TYPE " +
                              "FROM " +
                              "HR_C_OVERTIME F " +
                              "WHERE F.IS_USE = ? " +
                              "AND F.ENTERPRISE_CODE = ? ";
				Object[] params2 = new Object[2];
				params2[0] = IS_USE_Y;
				params2[1] = enterpriseCode;
				List list2 = bll.queryByNativeSQL(sql2, params2);
				LogUtil.log("EJB: SQL2 =" + sql2, Level.INFO, null);
				StaticDetailBean tempBean = arrlist.get(0);
				Iterator it2 = list2.iterator();
				while(it2.hasNext()){
					// 复制之前某一个员工的资料
					Object[] data = (Object[]) it2.next();
					StaticDetailBean bean = new StaticDetailBean();
					bean.setEmpId(tempBean.getEmpId());
					bean.setChsName(tempBean.getChsName());
					bean.setDeptId(tempBean.getDeptId());
					bean.setDeptName(tempBean.getDeptName());
					bean.setPdeptId(tempBean.getPdeptId());
					bean.setPdeptName(tempBean.getPdeptName());
					// 补上加班类别
					if(data[0]!=null){
						bean.setVacationTypeId(data[0].toString());
					}
					if(data[1]!=null){
						bean.setVacationType(data[1].toString());
					}
					arrlist.add(bean);
				}
			}
			pobj.setList(arrlist);
			LogUtil.log("EJB:获取加班统计报表详细信息结束。", Level.INFO, null);
		}catch (Exception e) {
			LogUtil.log("EJB:获取加班统计报表详细信息失败。", Level.INFO, null);
			return null;
		}
		return pobj;
	}

	/**
	 * 获取运行班统计报表详细信息
	 *
	 * @param enterpriseCode
	 *            企业编码
	 * @param year
	 *            统计年份
	 * @param month
	 *            统计月份
	 * @return
	 *           PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorkShiftStaticDetailInfo(String enterpriseCode,String year,String month){
		LogUtil.log("EJB:获取运行班统计报表详细信息开始。", Level.INFO, null);
		PageObject pobj = new PageObject();
		try{
			String sql = "SELECT V.EMP_ID " +
                         ",E.CHS_NAME " +
                         ",D.PDEPT_ID " +
                         ",D1.DEPT_NAME AS PDEPT_NAME " +
                         ",D.DEPT_ID " +
                         ",D.DEPT_NAME " +
                         ",V.WORK_SHIFT_ID " +
                         ",V.DAYS " +
                         ",F.WORK_SHIFT " +
                         "FROM HR_D_WORKSHIFTTOTAL V " +
                         ",HR_C_DEPT D " +
                         ",HR_C_DEPT D1 " +
                         ",HR_J_EMP_INFO E " +
                         ",HR_C_WORKSHIFT F " +
                         "WHERE V.IS_USE = ? " +
                         "AND D.IS_USE = ? " +
                         "AND D1.IS_USE(+) = ? " +
                         "AND E.IS_USE = ? " +
                         "AND F.IS_USE = ? " +
                         "AND V.ENTERPRISE_CODE = ? " +
                         "AND D.ENTERPRISE_CODE = ? " +
                         "AND D1.ENTERPRISE_CODE(+) = ? " +
                         "AND E.ENTERPRISE_CODE = ? " +
                         "AND F.ENTERPRISE_CODE = ? " +
                         "AND V.ATTENDANCE_YEAR = ? " +
                         "AND V.ATTENDANCE_MONTH = ? " +
                         "AND V.EMP_ID = E.EMP_ID " +
                         "AND V.DEPT_ID = D.DEPT_ID " +
                         "AND D1.DEPT_ID(+) = D.PDEPT_ID " +
                         "AND F.WORK_SHIFT_ID = V.WORK_SHIFT_ID " +
                         "AND V.DAYS > 0 " +
                         "ORDER BY D.PDEPT_ID, D.DEPT_ID, V.EMP_ID";
			Object[] params = new Object[12];
			params[0] = IS_USE_Y;
			params[1] = "Y";// update by sychen 20100831 
			params[2] = "Y";
//			params[1] = "U";
//			params[2] = "U";
			params[3] = IS_USE_Y;
			params[4] = IS_USE_Y;
			params[5] = enterpriseCode;
			params[6] = enterpriseCode;
			params[7] = enterpriseCode;
			params[8] = enterpriseCode;
			params[9] = enterpriseCode;
			params[10] = year;
			params[11] = month;
			List list = bll.queryByNativeSQL(sql, params);
			List<StaticDetailBean> arrlist = new ArrayList<StaticDetailBean>();
			LogUtil.log("EJB: SQL =" + sql, Level.INFO, null);
			Iterator it = list.iterator();
			while (it.hasNext()){
				Object[] data = (Object[]) it.next();
				StaticDetailBean bean = new StaticDetailBean();
				// 员工ID
				if(data[0]!=null){
					bean.setEmpId(data[0].toString());
				}
				// 员工姓名
				if(data[1]!=null){
					bean.setChsName(data[1].toString());
				}
				// 上级部门ID
				if(data[2]!=null){
					bean.setPdeptId(data[2].toString());
				}
				// 上级部门名称
				if(data[3]!=null){
					bean.setPdeptName(data[3].toString());
				}
				// 部门ID
				if(data[4]!=null){
					bean.setDeptId(data[4].toString());
				}
				// 部门名称
				if(data[5]!=null){
					bean.setDeptName(data[5].toString());
				}
				// 统计分类ID
				if(data[6]!=null){
					bean.setVacationTypeId(data[6].toString());
				}
				// 天数
				if(data[7]!=null){
					bean.setDays(data[7].toString());
				}
				// 统计分类名称
				if(data[8]!=null){
					bean.setVacationType(data[8].toString());
				}
				arrlist.add(bean);
			}
			// 补上在运行班维护表存在但是在请假统计表里没有的假别
			if(arrlist!=null&&arrlist.size()>0){
				String sql2 = "SELECT " +
                              "F.WORK_SHIFT_ID " +
                              ",F.WORK_SHIFT " +
                              "FROM " +
                              "HR_C_WORKSHIFT F " +
                              "WHERE F.IS_USE = ? " +
                              "AND F.ENTERPRISE_CODE = ? ";
				Object[] params2 = new Object[2];
				params2[0] = IS_USE_Y;
				params2[1] = enterpriseCode;
				List list2 = bll.queryByNativeSQL(sql2, params2);
				LogUtil.log("EJB: SQL2 =" + sql2, Level.INFO, null);
				StaticDetailBean tempBean = arrlist.get(0);
				Iterator it2 = list2.iterator();
				while(it2.hasNext()){
					// 复制之前某一个员工的资料
					Object[] data = (Object[]) it2.next();
					StaticDetailBean bean = new StaticDetailBean();
					bean.setEmpId(tempBean.getEmpId());
					bean.setChsName(tempBean.getChsName());
					bean.setDeptId(tempBean.getDeptId());
					bean.setDeptName(tempBean.getDeptName());
					bean.setPdeptId(tempBean.getPdeptId());
					bean.setPdeptName(tempBean.getPdeptName());
					// 补上运行班类别
					if(data[0]!=null){
						bean.setVacationTypeId(data[0].toString());
					}
					if(data[1]!=null){
						bean.setVacationType(data[1].toString());
					}
					arrlist.add(bean);
				}
			}
			pobj.setList(arrlist);
			LogUtil.log("EJB:获取运行班统计报表详细信息结束。", Level.INFO, null);
		}catch (Exception e) {
			LogUtil.log("EJB:获取运行班统计报表详细信息失败。", Level.INFO, null);
			return null;
		}
		return pobj;
	}

	/**
	 * 请假查询登记，查询信息
	 *
	 * @param enterpriseCode
	 *            企业编码
	 * @param startTime
	 *            请假开始日期from
	 * @param endTime
	 *            请假开始日期to
	 * @return
	 *           PageObject
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAllVacations(String enterpriseCode,String startTime,String endTime,final int... rowStartIdxAndCount) throws SQLException{
		LogUtil.log("EJB:请假登记查询开始。", Level.INFO, null);
		PageObject pobj = new PageObject();
		int paramsNum = 8;
		try{
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT V.VACATIONID      \n")
			    .append("  ,V.SIGN_STATE         \n")
				.append("  ,D.DEPT_NAME          \n")
				.append("  ,E.CHS_NAME           \n")
				.append("  ,TO_CHAR(V.START_TIME, 'yyyy-MM-dd HH24:mi')  \n")
				.append("  ,TO_CHAR(V.END_TIME, 'yyyy-MM-dd HH24:mi')    \n")
				.append("  ,C.VACATION_TYPE      \n")
				.append("  ,V.VACATION_DAYS      \n")
				.append("  ,V.VACATION_TIME      \n")
				.append("  ,V.REASON             \n")
				.append("  ,V.IF_CLEAR           \n")
				.append("  ,V.MEMO               \n")
				.append("  ,V.WHITHER            \n")
				.append("  ,TO_CHAR(V.CLEAR_DATE, 'yyyy-MM-dd HH24:mi')  \n")
				.append("FROM HR_J_VACATION V  \n")
				.append("LEFT JOIN HR_J_EMP_INFO E ON(")
				.append(    "V.EMP_ID = E.EMP_ID AND E.IS_USE = ? AND E.ENTERPRISE_CODE = ? )")
				.append("LEFT JOIN HR_C_DEPT D ON(")
				.append(    "D.DEPT_ID = V.DEPT_ID AND D.IS_USE = ? AND D.ENTERPRISE_CODE = ? )")
				.append("LEFT JOIN HR_C_VACATIONTYPE C ON( ")
				.append(    "C.VACATION_TYPE_ID = V.VACATION_TYPE_ID AND C.IS_USE = ? AND C.ENTERPRISE_CODE = ? )")
				.append("WHERE V.IS_USE = ? ").append(
						     " AND V.ENTERPRISE_CODE = ?");
			if( startTime!= null && !STRING_BLANK.equals(startTime) ) {
				paramsNum ++;
				sql.append( "AND TO_CHAR(V.START_TIME, 'YYYY-MM-DD') >= ?" );
			}
			if( endTime!= null && !STRING_BLANK.equals(endTime) ) {
				paramsNum ++;
				sql.append( "AND TO_CHAR(V.START_TIME, 'YYYY-MM-DD') <= ?" );
			}
			// 排序
			sql.append(" order by V.DEPT_ID,V.EMP_ID,V.START_TIME ");
			// 查询参数数组
            Object[] params = new Object[paramsNum];
            int i =0;
            params[i++] = IS_USE_Y;
            params[i++] = enterpriseCode;
            params[i++] = "Y";// update by sychen 20100831 
//            params[i++] = "U";
            params[i++] = enterpriseCode;
            params[i++] = IS_USE_Y;
            params[i++] = enterpriseCode;
            params[i++] = IS_USE_Y;
            params[i++] = enterpriseCode;
            //请假开始日期
            if (startTime != null && !STRING_BLANK.equals(startTime)) {
                params[i++] = startTime;
            }
            // 请假结束日期
            if (endTime != null && !STRING_BLANK.equals(endTime)) {
                params[i++] = endTime;
            }
            // 打印sql文
            LogUtil.log("EJB: SQL =" + sql.toString(), Level.INFO, null);
            // 查询
            List list = bll.queryByNativeSQL(sql.toString(), params, rowStartIdxAndCount);
            StringBuffer sqlCount = new StringBuffer();
            sqlCount.append("SELECT COUNT ( V.VACATIONID )      \n")
				.append("    FROM HR_J_VACATION V               \n")
				.append("    LEFT JOIN HR_J_EMP_INFO E ON(      \n ")
				.append("        V.EMP_ID = E.EMP_ID AND V.IS_USE = ? AND V.ENTERPRISE_CODE = ?  \n)")
				.append("    LEFT JOIN HR_C_DEPT D ON(")
				.append("        D.DEPT_ID = V.DEPT_ID AND D.IS_USE = ? AND D.ENTERPRISE_CODE = ? \n)")
				.append("    LEFT JOIN HR_C_VACATIONTYPE C ON(    \n")
				.append("        C.VACATION_TYPE_ID = V.VACATION_TYPE_ID AND C.IS_USE = ? AND C.ENTERPRISE_CODE = ? )")
				.append("WHERE V.IS_USE = ? ").append(
						     " AND V.ENTERPRISE_CODE = ?");
			if( startTime!= null && !STRING_BLANK.equals(startTime) ) {
				sqlCount.append( "AND TO_CHAR(V.START_TIME, 'YYYY-MM-DD') >= ?" );
			}
			if( endTime!= null && !STRING_BLANK.equals(endTime) ) {
				sqlCount.append( "AND TO_CHAR(V.START_TIME, 'YYYY-MM-DD') <= ?" );
			}
			// 打印sql文
            LogUtil.log("EJB: SQL =" + sqlCount.toString(), Level.INFO, null);
			// 查询总条数
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount.toString(),params).toString());

            // 组装查询结果
            List<HrJVacationByW> arrList = new ArrayList<HrJVacationByW>();
            if(list!=null) {
            	Iterator it = list.iterator();
                while(it.hasNext()) {
                	HrJVacationByW vacation = new HrJVacationByW();
                	Object[] data = (Object[]) it.next();
                	// 流水号
                    if(data[0] != null) {
                    	vacation.setVacationid(Long.parseLong(data[0].toString()));
                    }
                    // 审批状态
                    if (data[1] != null) {
                    	vacation.setSignState(data[1].toString());
                    }
                    // 部门
                    if(data[2] != null) {
                    	vacation.setDeptName(data[2].toString());
                    }
                    // 姓名
                    if (data[3] != null) {
                    	vacation.setChsName(data[3].toString());
                    }
                    // 开始日期
                    if(data[4] != null) {
                    	vacation.setStartTime(data[4].toString());
                    }
                    // 结束日期
                    if (data[5] != null) {
                    	vacation.setEndTime(data[5].toString());
                    }
                    // 假别
                    if(data[6] != null) {
                    	vacation.setVacationType(data[6].toString());
                    }
                    // 请假天数
                    if (data[7] != null) {
                    	vacation.setVacationDays(data[7].toString());
                    }
                    // 请假时长
                    if(data[8] != null) {
                    	vacation.setVacationTime(data[8].toString());
                    }
                    // 原因
                    if (data[9] != null) {
                    	vacation.setReason(data[9].toString());
                    }
                    // 是否销假
                    if(data[10] != null) {
                    	vacation.setIfClear(data[10].toString());
                    }
                    // 备注
                    if (data[11] != null) {
                    	vacation.setMemo(data[11].toString());
                    }
                    // 去向
                    if (data[12] != null) {
                    	vacation.setWhither(data[12].toString());
                    }
                    // 销假时间
                    if (data[13] != null) {
                    	vacation.setClearDate(data[13].toString());
                    }
                    arrList.add(vacation);
                }
            }
			pobj.setList(arrList);
			pobj.setTotalCount(totalCount);
			LogUtil.log("EJB:请假登记查询开始结束。", Level.INFO, null);
		}catch (Exception e) {
			LogUtil.log("EJB:请假登记查询开始失败。", Level.SEVERE, e);
			 throw new SQLException();
		}
		return pobj;
	}

	/**
	 * 获取全部假别
	 *
	 * @param enterpriseCode 企业代码
	 * @return  PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getVacationType(String enterpriseCode){
		LogUtil.log("EJB:获取假别查询开始。", Level.INFO, null);
		PageObject pobj = new PageObject();
		try{
			String sql = "SELECT A.VACATION_TYPE " +
                         "FROM HR_C_VACATIONTYPE A " +
                         "WHERE A.IS_USE = ? " +
                         "AND A.ENTERPRISE_CODE = ? " +
                         "ORDER BY A.VACATION_TYPE_ID";
			Object[] params = new Object[2];
			params[0] = IS_USE_Y;
			params[1] = enterpriseCode;
			List list = bll.queryByNativeSQL(sql, params);
			List<String> arrlist = new ArrayList<String>();
			LogUtil.log("EJB: SQL =" + sql, Level.INFO, null);
			if(list!=null&&list.size()>0){
				for(int i = 0;i <list.size();i++){
					if(list.get(i)!=null){
						arrlist.add(list.get(i).toString());
					}
				}
			}
			pobj.setList(arrlist);
			LogUtil.log("EJB:获取假别查询结束。", Level.INFO, null);
		}catch (Exception e) {
			LogUtil.log("EJB:获取假别查询失败。", Level.INFO, null);
			return null;
		}
		return pobj;
	}

	/**
	 * 获取全部加班类别
	 *
	 * @param enterpriseCode 企业代码
	 * @return  PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorkOvertimeType(String enterpriseCode){
		LogUtil.log("EJB:获取加班类别查询开始。", Level.INFO, null);
		PageObject pobj = new PageObject();
		try{
			String sql = "SELECT A.OVERTIME_TYPE " +
                         "FROM HR_C_OVERTIME A " +
                         "WHERE A.IS_USE = ? " +
                         "AND A.ENTERPRISE_CODE = ? " +
                         "ORDER BY A.OVERTIME_TYPE_ID";
			Object[] params = new Object[2];
			params[0] = IS_USE_Y;
			params[1] = enterpriseCode;
			List list = bll.queryByNativeSQL(sql, params);
			List<String> arrlist = new ArrayList<String>();
			LogUtil.log("EJB: SQL =" + sql, Level.INFO, null);
			if(list!=null&&list.size()>0){
				for(int i = 0;i <list.size();i++){
					if(list.get(i)!=null){
						arrlist.add(list.get(i).toString());
					}
				}
			}
			pobj.setList(arrlist);
			LogUtil.log("EJB:获取加班类别查询结束。", Level.INFO, null);
		}catch (Exception e) {
			LogUtil.log("EJB:获取加班类别查询失败。", Level.INFO, null);
			return null;
		}
		return pobj;
	}

	/**
	 * 获取全部运行班类别
	 *
	 * @param enterpriseCode 企业代码
	 * @return  PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorkShiftType(String enterpriseCode){
		LogUtil.log("EJB:获取运行班类别查询开始。", Level.INFO, null);
		PageObject pobj = new PageObject();
		try{
			String sql = "SELECT A.WORK_SHIFT " +
                         "FROM HR_C_WORKSHIFT A " +
                         "WHERE A.IS_USE = ? " +
                         "AND A.ENTERPRISE_CODE = ? " +
                         "ORDER BY A.WORK_SHIFT_ID";
			Object[] params = new Object[2];
			params[0] = IS_USE_Y;
			params[1] = enterpriseCode;
			List list = bll.queryByNativeSQL(sql, params);
			List<String> arrlist = new ArrayList<String>();
			LogUtil.log("EJB: SQL =" + sql, Level.INFO, null);
			if(list!=null&&list.size()>0){
				for(int i = 0;i <list.size();i++){
					if(list.get(i)!=null){
						arrlist.add(list.get(i).toString());
					}
				}
			}
			pobj.setList(arrlist);
			LogUtil.log("EJB:获取运行班类别查询结束。", Level.INFO, null);
		}catch (Exception e) {
			LogUtil.log("EJB:获取运行班类别查询失败。", Level.INFO, null);
			return null;
		}
		return pobj;
	}
}