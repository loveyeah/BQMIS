package power.ejb.hr.ca;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.DataChangeException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrCExchangetorest.
 *
 * @see power.ejb.hr.ca.HrCExchangetorest
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCExchangetorestFacade implements HrCExchangetorestFacadeRemote {
	// property constants
	public static final String EXCHANGEREST_HOURS = "exchangerestHours";
	public static final String SIGN_STATE = "signState";
	public static final String WORK_FLOW_NO = "workFlowNo";
	public static final String LAST_MODIFIY_BY = "lastModifiyBy";
	public static final String IS_USE = "isuse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	private static final String PATTERN_NUMBER_TIME = "##############0.0";
	// 月累计加班时间为0
	private static final String NON_SUMOVER_TIME = "0.0";
	public static final String IS_USE_Y = "Y";
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	private static final String BLANK = "";
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved HrCExchangetorest entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCExchangetorest entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCExchangetorest entity) {
		LogUtil.log("saving HrCExchangetorest instance", Level.INFO, null);
		try {
			entity.setLastModifiyDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCExchangetorest entity.
	 *
	 * @param entity
	 *            HrCExchangetorest entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCExchangetorest entity) {
		LogUtil.log("deleting HrCExchangetorest instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCExchangetorest.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCExchangetorest entity and return it or a
	 * copy of it to the sender. A copy of the HrCExchangetorest entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 *
	 * @param entity
	 *            HrCExchangetorest entity to update
	 * @return HrCExchangetorest the persisted HrCExchangetorest entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public HrCExchangetorest update(HrCExchangetorest entity) {
		LogUtil.log("updating HrCExchangetorest instance", Level.INFO, null);
		try {
			entity.setLastModifiyDate(new java.util.Date());
			HrCExchangetorest result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 排他比较
	 *
	 * @params HrCExchangetorest entity
	 * @throws DataChangeException
	 */
	public void ifDataChange(HrCExchangetorest entity)
			throws DataChangeException {
		HrCExchangetorest lastBean = new HrCExchangetorest();
		lastBean = this.findById(entity.getId());
		SimpleDateFormat sdfFrom = new SimpleDateFormat(
				DATE_FORMAT_YYYYMMDD_HHMMSS);
		String dtNowTime = sdfFrom.format(lastBean.getLastModifiyDate());
		String dtOldTime = sdfFrom.format(entity.getLastModifiyDate());
		if (!dtNowTime.equals(dtOldTime)) {
			throw new DataChangeException(null);
		}
	}

	public HrCExchangetorest findById(HrCExchangetorestId id) {
		LogUtil.log("finding HrCExchangetorest instance with id: " + id,
				Level.INFO, null);
		try {
			HrCExchangetorest instance = entityManager.find(
					HrCExchangetorest.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCExchangetorest entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCExchangetorest property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCExchangetorest> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCExchangetorest> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCExchangetorest instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCExchangetorest model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrCExchangetorest> findByExchangerestHours(
			Object exchangerestHours) {
		return findByProperty(EXCHANGEREST_HOURS, exchangerestHours);
	}

	public List<HrCExchangetorest> findBySignState(Object signState) {
		return findByProperty(SIGN_STATE, signState);
	}

	public List<HrCExchangetorest> findByWorkFlowNo(Object workFlowNo) {
		return findByProperty(WORK_FLOW_NO, workFlowNo);
	}

	public List<HrCExchangetorest> findByLastModifiyBy(Object lastModifiyBy) {
		return findByProperty(LAST_MODIFIY_BY, lastModifiyBy);
	}

	public List<HrCExchangetorest> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<HrCExchangetorest> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all HrCExchangetorest entities.
	 *
	 * @return List<HrCExchangetorest> all HrCExchangetorest entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCExchangetorest> findAll() {
		LogUtil
				.log("finding all HrCExchangetorest instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from HrCExchangetorest model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<OverTimeRegiste> searchLastMonth(String depId, String year,
			String month, String enterpriseCode) {
		LogUtil.log("EJB:加班换休登记查询开始", Level.INFO, null);
		// 数据只保留一位小数
		DecimalFormat dfNum = new DecimalFormat(PATTERN_NUMBER_TIME);
		// 查询SQL
		StringBuffer sql1 = new StringBuffer();
		sql1.append("SELECT EMP_ID ");
		sql1.append(",CHS_NAME ");
		sql1.append(",DEPT_ID ");
		sql1.append(",DEPT_NAME ");
		sql1.append(",SUM(DAYS) * MAX(STANDARD_TIME) ");
		sql1.append("FROM( ");
		sql1.append("SELECT ");
		sql1.append("A.EMP_ID EMP_ID ");
		sql1.append(",A.CHS_NAME CHS_NAME ");
		sql1.append(",D.DEPT_ID  DEPT_ID ");
		sql1.append(",D.DEPT_NAME DEPT_NAME ");
		sql1.append(",B.DAYS DAYS ");
		sql1.append(" ,C.STANDARD_TIME STANDARD_TIME ");
		sql1.append(" FROM HR_J_EMP_INFO A ");
		sql1.append(" ,HR_D_OVERTIMETOTAL B ");
		sql1.append(",HR_C_ATTENDANCESTANDARD C ");
		sql1.append(" ,HR_C_DEPT D ");
		sql1.append("WHERE A.EMP_ID = B.EMP_ID ");
		sql1.append("AND B.ATTENDANCE_DEPT_ID = C.ATTENDANCE_DEPT_ID ");
		sql1.append("AND A.DEPT_ID = D.DEPT_ID ");
		sql1.append("AND A.IS_USE = ? ");
		sql1.append("AND B.IS_USE = ? ");
		sql1.append("AND C.IS_USE = ? ");
		sql1.append("AND D.IS_USE = ? ");
		sql1.append("AND A.ENTERPRISE_CODE = ? ");
		sql1.append("AND B.ENTERPRISE_CODE = ? ");
		sql1.append("AND C.ENTERPRISE_CODE = ? ");
		sql1.append("AND D.ENTERPRISE_CODE = ? ");
		sql1.append("AND B.ATTENDANCE_YEAR = ? ");
		sql1
				.append("AND DECODE(LENGTH(B.ATTENDANCE_MONTH),1,'0'||B.ATTENDANCE_MONTH,2,B.ATTENDANCE_MONTH) = ? ");
		sql1.append(" AND C.ATTENDANCE_YEAR = ? ");
		sql1
				.append(" AND DECODE(LENGTH(C.ATTENDANCE_MONTH),1,'0'||C.ATTENDANCE_MONTH,2,C.ATTENDANCE_MONTH) = ? ");
		// 查找该部门及其子部门
		if (depId != null && !"".equals(depId)) {
			String addStr = " AND D.DEPT_ID  IN (SELECT T.DEPT_ID\n"
					+ "                       FROM HR_C_DEPT t\n"
					+ "                      WHERE T.IS_USE = ? AND T.ENTERPRISE_CODE =? \n"
					+ "                      START WITH T.DEPT_ID=? \n"
					+ "                     CONNECT BY PRIOR T.DEPT_ID=T.PDEPT_ID)\n";
			sql1.append(addStr);
		}
		sql1.append(") E GROUP BY EMP_ID,CHS_NAME,DEPT_ID,DEPT_NAME ");
		sql1.append(" ORDER BY DEPT_ID , EMP_ID");
		Object[] params1;
		if (depId != null && !"".equals(depId)) {
			params1 = new Object[15];
			params1[12] = "Y";
			params1[13] = enterpriseCode;
			params1[14] = depId;
		} else
			params1 = new Object[12];
		params1[0] = IS_USE_Y;
		params1[1] = IS_USE_Y;
		params1[2] = IS_USE_Y;
//		params1[3] = IS_USE_Y;
		params1[3] = "Y";//update by sychen 20100831//Modify by qxjiao20100903
//		params1[3] = "U";
		params1[4] = enterpriseCode;
		params1[5] = enterpriseCode;
		params1[6] = enterpriseCode;
		params1[7] = enterpriseCode;
		params1[8] = year;
		params1[9] = month;
		params1[10] = year;
		params1[11] = month;
		List list1 = bll.queryByNativeSQL(sql1.toString(), params1);
		List<OverTimeRegiste> arrList = new ArrayList<OverTimeRegiste>();
		Iterator it1 = list1.iterator();
		while (it1.hasNext()) {
			Object[] data = (Object[]) it1.next();
			OverTimeRegiste entity = new OverTimeRegiste();
			// 人员ID
			if (data[0] != null && !data[0].toString().equals(BLANK)) {
				entity.setEmpId(data[0].toString());
			}
			// 人员姓名
			if (data[1] != null && !data[1].toString().equals(BLANK)) {
				entity.setEmpName(data[1].toString());
			}
			// 部门ID
			if (data[2] != null && !data[2].toString().equals(BLANK)) {
				entity.setDeptId(data[2].toString());
			}
			// 部门名称
			if (data[3] != null && !data[3].toString().equals(BLANK)) {
				entity.setDepartment(data[3].toString());
			}
			// 月累计加班时间
			if (data[4] != null && !data[4].toString().equals(BLANK)) {

				entity.setSumOverTime(dfNum.format(new Double(data[4]
						.toString())));
				entity.setExchangHours(dfNum.format(new Double(data[4]
						.toString())));
			} else {
				entity.setSumOverTime(NON_SUMOVER_TIME);
				entity.setExchangHours(NON_SUMOVER_TIME);
			}
			// 出勤年份
			entity.setAttendanceYear(year);
			// 出勤月份
			entity.setAttendanceMonth(month);

			entity.setApprovalStates("0");
			arrList.add(entity);
		}
		// 查询换休时间和审批状态
		StringBuffer sql2 = new StringBuffer();
		sql2.append("SELECT A.EMP_ID ");
		sql2.append(",B.EXCHANGEREST_HOURS ");
		sql2.append(",B.SIGN_STATE ");
		sql2.append(",TO_CHAR(B.LAST_MODIFIY_DATE, 'YYYY-MM-DD HH24:MI:SS') ");
		sql2.append(",B.ATTENDANCE_YEAR ");
		sql2.append(",B.ATTENDANCE_MONTH ");
		sql2.append("FROM HR_J_EMP_INFO A ");
		sql2.append(",HR_C_EXCHANGETOREST B ");
		sql2.append("WHERE A.EMP_ID = B.EMP_ID ");
		sql2.append("AND B.ATTENDANCE_YEAR = ? ");
		sql2
				.append("AND DECODE(LENGTH(B.ATTENDANCE_MONTH),1,'0'||B.ATTENDANCE_MONTH,2,B.ATTENDANCE_MONTH) = ? ");
		sql2.append("  AND A.IS_USE = ? ");
		sql2.append("AND B.IS_USE = ? ");
		sql2.append("AND A.ENTERPRISE_CODE = ? ");
		sql2.append("AND B.ENTERPRISE_CODE = ? ");
		Object[] params2 = new Object[6];
		params2[0] = year;
		params2[1] = month;
		params2[2] = IS_USE_Y;
		params2[3] = IS_USE_Y;
		params2[4] = enterpriseCode;
		params2[5] = enterpriseCode;
		List list2 = bll.queryByNativeSQL(sql2.toString(), params2);
		Iterator it2 = list2.iterator();
		// 将换休时间和审批状态放到bean中
		for (int i = 0; i < arrList.size(); i++) {
			while (it2.hasNext()) {
				Object[] data = (Object[]) it2.next();
				// 根据员工ID将换休时间和审批状态放到正确位置
				if (data[0] != null
						&& data[0].toString().equals(arrList.get(i).getEmpId())) {
					// 换休时间
					if (data[1] != null)
						arrList.get(i).setExchangHours(
								dfNum.format(new Double(data[1].toString())));
					else
						arrList.get(i).setExchangHours(NON_SUMOVER_TIME);
					// 签字状态
					if (data[2] != null)
						arrList.get(i).setApprovalStates(data[2].toString());
					// 上次修改时间
					if (data[3] != null)
						arrList.get(i).setLastModifyTime(data[3].toString());
				}
			}
			it2 = list2.iterator();
		}
		return arrList;
	}

	/**
	 * 加班换休登记表插入或更新
	 *
	 * @param List
	 *            <HrCExchangetorest> list 页面上多条数据
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean saveOrUpdate(List<HrCExchangetorest> list)
			throws DataChangeException, RuntimeException, Exception {
		try {
			LogUtil.log("EJB:加班换休登记表插入或更新开始。", Level.INFO, null);
			// 排他处理
			for (int i = 0; i < list.size(); i++) {
				HrCExchangetorest tempBean = new HrCExchangetorest();
				tempBean = this.findById(list.get(i).getId());
				// 改纪录在数据库中存在，排他处理
				if (tempBean != null)
					this.ifDataChange(list.get(i));
			}
			for (int i = 0; i < list.size(); i++) {
				// 判断改条记录在数据库中是否存在，存在的情况下更新数据库，否则插入数据库
				HrCExchangetorest tempBean = new HrCExchangetorest();
				tempBean = this.findById(list.get(i).getId());
				if (tempBean != null) {
					this.update(list.get(i));
				} else
					this.save(list.get(i));
			}
			LogUtil.log("EJB:加班换休登记表插入或更新成功。", Level.INFO, null);
			return true;

		} catch (Exception e) {
			throw e;
		}
	}
}