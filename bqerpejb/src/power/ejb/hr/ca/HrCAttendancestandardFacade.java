package power.ejb.hr.ca;

import java.sql.SQLException;
import java.util.Date;
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
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity HrCAttendancestandard.
 * 
 * @see power.ejb.hr.HrCAttendancestandard
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCAttendancestandardFacade implements
		HrCAttendancestandardFacadeRemote {
	// property constants
	public static final String ATTENDANCE_YEAR = "attendanceYear";
	public static final String ATTENDANCE_MONTH = "attendanceMonth";
	public static final String ATTENDANCE_DEPT_ID = "attendanceDeptId";
	public static final String STANDARD_DAY = "standardDay";
	public static final String AM_BEGING_TIME = "amBegingTime";
	public static final String AM_END_TIME = "amEndTime";
	public static final String PM_BEGING_TIME = "pmBegingTime";
	public static final String PM_END_TIME = "pmEndTime";
	public static final String STANDARD_TIME = "standardTime";
	public static final String LAST_MODIFIY_BY = "lastModifiyBy";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	/**考勤部门维护表*/
	HrCAttendancedepFacadeRemote hrcAttendancedepFacadeRemote;
	
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 构造函数
	 */
	public HrCAttendancestandardFacade() {
		hrcAttendancedepFacadeRemote = (HrCAttendancedepFacadeRemote)(Ejb3Factory.getInstance())
		.getFacadeRemote("HrCAttendancedepFacade");
	}
	/**
	 * Perform an initial save of a previously unsaved HrCAttendancestandard
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrCAttendancestandard entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */

	public void save(HrCAttendancestandard entity) throws RuntimeException{
		LogUtil.log("EJB:考勤标准维护保存开始", Level.INFO, null);
		try {
			 // 取得最大的id
			if(entity.getAttendancestandardid() == null) {
				entity.setAttendancestandardid(bll.getMaxId("HR_C_ATTENDANCESTANDARD", "ATTENDANCESTANDARDID"));
			}
            // 设置最后修改时间
            entity.setLastModifiyDate(new Date());
            entityManager.persist(entity);
            LogUtil.log("EJB:考勤标准维护保存结束", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:考勤标准维护保存失败", Level.INFO, null);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCAttendancestandard entity.
	 * 
	 * @param entity
	 *            HrCAttendancestandard entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCAttendancestandard entity) {
		LogUtil
				.log("deleting HrCAttendancestandard instance", Level.INFO,
						null);
		try {
			entity = entityManager.getReference(HrCAttendancestandard.class,
					entity.getAttendancestandardid());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCAttendancestandard entity and return it or
	 * a copy of it to the sender. A copy of the HrCAttendancestandard entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCAttendancestandard entity to update
	 * @return HrCAttendancestandard the persisted HrCAttendancestandard entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public void update(HrCAttendancestandard entity, String strUpdateTime) throws DataChangeException, SQLException {
		LogUtil.log("EJB：更新考勤标准维护开始", Level.INFO, null);
		try {
				entity.setLastModifiyDate(new Date());
				entityManager.merge(entity);
				LogUtil.log("EJB：更新考勤标准维护正常结束", Level.INFO, null);
		} catch (Exception re) {
			LogUtil.log("EJB：更新考勤标准维护异常结束", Level.SEVERE, re);
			throw new SQLException("");
		}
	}

	/**
	 * 批量增加修改考勤标准维护信息
	 * @param list
	 * @param enterpriseCode
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveBat(List<HrCAttendancestandard> list,String enterpriseCode,String flag,String workerCode) throws Exception {
		LogUtil.log("EJB:批量增加修改考勤标准维护信息开始。", Level.INFO, null);
		try {
			Long maxId = bll.getMaxId("HR_C_ATTENDANCESTANDARD", "ATTENDANCESTANDARDID");
			int count = 0 ;
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getAttendancestandardid() != null && !list.get(i).getAttendancestandardid().equals("")){
					list.get(i).setLastModifiyDate(new Date());
					this.update(list.get(i),list.get(i).getLastModifiyDate().toString());
				}else {
					list.get(i).setAttendancestandardid(maxId + count);
					list.get(i).setLastModifiyDate(new Date());
					list.get(i).setEnterpriseCode(enterpriseCode);
					list.get(i).setIsUse("Y");
					this.save(list.get(i));
					count++;
				}
			}
			LogUtil.log("EJB:批量新增劳动合同结束。", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("EJB:批量新增劳动合同错误。", Level.SEVERE, re);
			throw re;
		} catch (DataChangeException e) {
			LogUtil.log("EJB:批量新增劳动合同错误。", Level.SEVERE, null);
			throw e;
		}
	}
	
	public HrCAttendancestandard findById(Long id) {
		LogUtil.log("finding HrCAttendancestandard instance with id: " + id,
				Level.INFO, null);
		try {
			HrCAttendancestandard instance = entityManager.find(
					HrCAttendancestandard.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCAttendancestandard entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCAttendancestandard property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCAttendancestandard> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCAttendancestandard> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrCAttendancestandard instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCAttendancestandard model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrCAttendancestandard> findByAttendanceYear(
			Object attendanceYear) {
		return findByProperty(ATTENDANCE_YEAR, attendanceYear);
	}

	public List<HrCAttendancestandard> findByAttendanceMonth(
			Object attendanceMonth) {
		return findByProperty(ATTENDANCE_MONTH, attendanceMonth);
	}

	public List<HrCAttendancestandard> findByAttendanceDeptId(
			Object attendanceDeptId) {
		return findByProperty(ATTENDANCE_DEPT_ID, attendanceDeptId);
	}

	public List<HrCAttendancestandard> findByStandardDay(Object standardDay) {
		return findByProperty(STANDARD_DAY, standardDay);
	}

	public List<HrCAttendancestandard> findByAmBegingTime(Object amBegingTime) {
		return findByProperty(AM_BEGING_TIME, amBegingTime);
	}

	public List<HrCAttendancestandard> findByAmEndTime(Object amEndTime) {
		return findByProperty(AM_END_TIME, amEndTime);
	}

	public List<HrCAttendancestandard> findByPmBegingTime(Object pmBegingTime) {
		return findByProperty(PM_BEGING_TIME, pmBegingTime);
	}

	public List<HrCAttendancestandard> findByPmEndTime(Object pmEndTime) {
		return findByProperty(PM_END_TIME, pmEndTime);
	}

	public List<HrCAttendancestandard> findByStandardTime(Object standardTime) {
		return findByProperty(STANDARD_TIME, standardTime);
	}

	public List<HrCAttendancestandard> findByLastModifiyBy(Object lastModifiyBy) {
		return findByProperty(LAST_MODIFIY_BY, lastModifiyBy);
	}

	public List<HrCAttendancestandard> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	public List<HrCAttendancestandard> findByEnterpriseCode(
			Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all HrCAttendancestandard entities.
	 * 
	 * @return List<HrCAttendancestandard> all HrCAttendancestandard entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCAttendancestandard> findAll() {
		LogUtil.log("finding all HrCAttendancestandard instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from HrCAttendancestandard model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}