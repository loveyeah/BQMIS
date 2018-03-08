package power.ejb.hr.ca;

import java.text.SimpleDateFormat;
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
 * Facade for entity HrJWorkattendance.
 * 
 * @see power.ejb.hr.ca.HrJWorkattendance
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJWorkattendanceFacade implements HrJWorkattendanceFacadeRemote {
	// property constants
	public static final String DEPT_ID = "deptId";
	public static final String ATTENDANCE_DEPT_ID = "attendanceDeptId";
	public static final String AM_BEGING_TIME = "amBegingTime";
	public static final String AM_END_TIME = "amEndTime";
	public static final String PM_BEGING_TIME = "pmBegingTime";
	public static final String PM_END_TIME = "pmEndTime";
	public static final String WORK_SHIFT_ID = "workShiftId";
	public static final String VACATION_TYPE_ID = "vacationTypeId";
	public static final String OVERTIME_TYPE_ID = "overtimeTypeId";
	public static final String WORK = "work";
	public static final String REST_TYPE = "restType";
	public static final String ABSENT_WORK = "absentWork";
	public static final String LATE_WORK = "lateWork";
	public static final String LEAVE_EARLY = "leaveEarly";
	public static final String OUT_WORK = "outWork";
	public static final String EVECTION_TYPE = "evectionType";
	public static final String MEMO = "memo";
	public static final String INSERTBY = "insertby";
	public static final String LAST_MODIFIY_BY = "lastModifiyBy";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * Perform an initial save of a previously unsaved HrJWorkattendance entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJWorkattendance entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJWorkattendance entity) {
		LogUtil.log("saving HrJWorkattendance instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrJWorkattendance entity.
	 * 
	 * @param entity
	 *            HrJWorkattendance entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJWorkattendance entity) {
		LogUtil.log("deleting HrJWorkattendance instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJWorkattendance.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新考勤记录
	 * 
	 * @param entity
	 *            需要被更新的bean的内容
	 * @throws RuntimeException
	 *             更新失败
	 * @throws DataChangeException
	 *             排他失败
	 */
	public void update(HrJWorkattendance entity) throws DataChangeException {
		LogUtil.log("EJB:更新考勤记录开始。", Level.INFO, null);
		try {
			HrJWorkattendance lastBean = this.findById(
					entity.getId(), entity.getEnterpriseCode());
			SimpleDateFormat sdfFrom = new SimpleDateFormat(
					DATE_FORMAT_YYYYMMDD_HHMMSS);
			if (null != lastBean.getLastModifiyDate()) {
				String dtNowTime = sdfFrom.format(lastBean.getLastModifiyDate());
				String dtOldTime = sdfFrom.format(entity.getLastModifiyDate());
				if (dtNowTime.equals(dtOldTime)) {
					// 排他成功
					entity.setLastModifiyDate(new Date());
					entityManager.merge(entity);
				} else {
					// 排他失败
					throw new DataChangeException(null);
				}
			} else {
				entity.setLastModifiyDate(new Date());
				entityManager.merge(entity);
			}
			LogUtil.log("EJB:更新考勤记录结束。", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:更新考勤记录失败。", Level.SEVERE, re);
			throw re;
		} catch (DataChangeException e) {
			LogUtil.log("EJB:更新考勤记录失败。", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 根据ID查找考勤记录
	 * 
	 * @param id ID
	 * @param enterpriseCode 企业编码
	 * @return 考勤记录
	 */
	@SuppressWarnings("unchecked")
	public HrJWorkattendance findById(HrJWorkattendanceId id,
			String enterpriseCode) {
		LogUtil.log("EJB:根据ID查找考勤记录开始。", Level.INFO, null);
		try {
			HrJWorkattendance bean = new HrJWorkattendance();
			// sql
        	StringBuffer sbSql = new StringBuffer();
        	sbSql.append("SELECT * ");
        	sbSql.append("FROM ");
        	sbSql.append("	HR_J_WORKATTENDANCE A ");
        	sbSql.append("WHERE ");
        	sbSql.append("	A.EMP_ID = ? AND ");
        	sbSql.append("	A.ATTENDANCE_DATE = ? AND ");
        	sbSql.append("	A.IS_USE = ? AND ");
        	sbSql.append("	A.ENTERPRISE_CODE = ? ");
        	sbSql.append("ORDER BY ");
        	sbSql.append("	A.EMP_ID, ");
        	sbSql.append("	A.ATTENDANCE_DATE ");
        	
        	// 查询
        	List<HrJWorkattendance> list = bll.queryByNativeSQL(sbSql.toString(),
        		new Object[]{id.getEmpId(), id.getAttendanceDate(),
        		"Y", enterpriseCode},
        		HrJWorkattendance.class);
            if(list == null) {
            	bean = null;
            } else {
            	bean = list.get(0);
            }
            LogUtil.log("EJB:根据ID查找考勤记录结束。", Level.INFO, null);
			return bean;
		} catch (RuntimeException re) {
			LogUtil.log("EJB:根据ID查找考勤记录失败。", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrJWorkattendance entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJWorkattendance property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJWorkattendance> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrJWorkattendance> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJWorkattendance instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJWorkattendance model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrJWorkattendance> findByDeptId(Object deptId) {
		return findByProperty(DEPT_ID, deptId);
	}

	public List<HrJWorkattendance> findByAttendanceDeptId(
			Object attendanceDeptId) {
		return findByProperty(ATTENDANCE_DEPT_ID, attendanceDeptId);
	}

	public List<HrJWorkattendance> findByAmBegingTime(Object amBegingTime) {
		return findByProperty(AM_BEGING_TIME, amBegingTime);
	}

	public List<HrJWorkattendance> findByAmEndTime(Object amEndTime) {
		return findByProperty(AM_END_TIME, amEndTime);
	}

	public List<HrJWorkattendance> findByPmBegingTime(Object pmBegingTime) {
		return findByProperty(PM_BEGING_TIME, pmBegingTime);
	}

	public List<HrJWorkattendance> findByPmEndTime(Object pmEndTime) {
		return findByProperty(PM_END_TIME, pmEndTime);
	}

	public List<HrJWorkattendance> findByWorkShiftId(Object workShiftId) {
		return findByProperty(WORK_SHIFT_ID, workShiftId);
	}

	public List<HrJWorkattendance> findByVacationTypeId(Object vacationTypeId) {
		return findByProperty(VACATION_TYPE_ID, vacationTypeId);
	}

	public List<HrJWorkattendance> findByOvertimeTypeId(Object overtimeTypeId) {
		return findByProperty(OVERTIME_TYPE_ID, overtimeTypeId);
	}

	public List<HrJWorkattendance> findByWork(Object work) {
		return findByProperty(WORK, work);
	}

	public List<HrJWorkattendance> findByRestType(Object restType) {
		return findByProperty(REST_TYPE, restType);
	}

	public List<HrJWorkattendance> findByAbsentWork(Object absentWork) {
		return findByProperty(ABSENT_WORK, absentWork);
	}

	public List<HrJWorkattendance> findByLateWork(Object lateWork) {
		return findByProperty(LATE_WORK, lateWork);
	}

	public List<HrJWorkattendance> findByLeaveEarly(Object leaveEarly) {
		return findByProperty(LEAVE_EARLY, leaveEarly);
	}

	public List<HrJWorkattendance> findByOutWork(Object outWork) {
		return findByProperty(OUT_WORK, outWork);
	}

	public List<HrJWorkattendance> findByEvectionType(Object evectionType) {
		return findByProperty(EVECTION_TYPE, evectionType);
	}

	public List<HrJWorkattendance> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<HrJWorkattendance> findByInsertby(Object insertby) {
		return findByProperty(INSERTBY, insertby);
	}

	public List<HrJWorkattendance> findByLastModifiyBy(Object lastModifiyBy) {
		return findByProperty(LAST_MODIFIY_BY, lastModifiyBy);
	}

	public List<HrJWorkattendance> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<HrJWorkattendance> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all HrJWorkattendance entities.
	 * 
	 * @return List<HrJWorkattendance> all HrJWorkattendance entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrJWorkattendance> findAll() {
		LogUtil
				.log("finding all HrJWorkattendance instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from HrJWorkattendance model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}