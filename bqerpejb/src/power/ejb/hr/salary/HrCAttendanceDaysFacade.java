package power.ejb.hr.salary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.salary.form.AttendanceDaysForm;

/**
 * Facade for entity HrCAttendanceDays.
 * 
 * @see power.ejb.hr.salary.HrCAttendanceDays
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCAttendanceDaysFacade implements HrCAttendanceDaysFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 保存一条出勤天数记录
	 * 
	 * @param entity
	 * @return HrCAttendanceDays
	 */
	public HrCAttendanceDays save(HrCAttendanceDays entity) {
		LogUtil.log("saving HrCAttendanceDays instance", Level.INFO, null);
		try {
			entity.setAttendanceDaysId(bll.getMaxId("hr_c_attendance_days",
					"attendance_days_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除出勤天数记录
	 * 
	 * @param attendanceDaysId String
	 */
	public void deleteAttendanceDays(String attendanceDaysId) {
		LogUtil.log("deleting HrCAttendanceDays instance", Level.INFO, null);
		String sql = "update hr_c_attendance_days t\n"
				+ "   set t.is_use = 'N'\n"
				+ " where t.attendance_days_id in (" + attendanceDaysId + ")";

		bll.exeNativeSQL(sql);
	}

	/**
	 * 更新一条出勤天数记录
	 * 
	 * @param entity
	 * @return HrCAttendanceDays
	 */
	public HrCAttendanceDays update(HrCAttendanceDays entity) {
		LogUtil.log("updating HrCAttendanceDays instance", Level.INFO, null);
		try {
			HrCAttendanceDays result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据主key查询
	 * 
	 * @param id Long 出勤天数ID
	 * @return HrCAttendanceDays
	 */
	public HrCAttendanceDays findById(Long id) {
		LogUtil.log("finding HrCAttendanceDays instance with id: " + id,
				Level.INFO, null);
		try {
			HrCAttendanceDays instance = entityManager.find(
					HrCAttendanceDays.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询该月的出勤天数是否已经维护
	 * 
	 * @param month String
	 * @return true：已经维护、false：没有维护
	 */
	@SuppressWarnings("unchecked")
	public boolean findByMonth(String month) {
		LogUtil.log("finding HrCAttendanceDays instance with month: ",
				Level.INFO, null);
		try {
			String queryString = "select t.* from hr_c_attendance_days t "
					+ " where to_char(t.month,'yyyy-MM')='" + month + "'"
					+ " and t.is_use = 'Y'";
			List list = bll.queryByNativeSQL(queryString);
			if (list.size() >= 1) {
				return true;
			}
			return false;
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据条件查询出勤天数List
	 * 
	 * @param sMonth String 月份
	 * @param enterpriseCode 
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAttendanceDaysList(String sMonth,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.attendance_days_id,\n"
				+ " to_char(t.month, 'yyyy-MM'),\n"
				+ " t.attendance_days,\n"
				+ " t.memo,\n"
				+ " to_char(t.start_date, 'yyyy-MM'),\n"//add by sychen 20100806
				+ " to_char(t.end_date, 'yyyy-MM')\n"//add by sychen 20100806
				+ " from hr_c_attendance_days t\n"
                + " where t.is_use = 'Y'\n"
				+ " and t.enterprise_code = '" + enterpriseCode + "'";

		String sqlCount = "select count(1)\n"
				+ " from hr_c_attendance_days t\n" + " where t.is_use = 'Y'\n"
				+ " and t.enterprise_code = '" + enterpriseCode + "'";

		String whereStr = "";
		if (sMonth != null && sMonth.length() > 0) {
//			whereStr += " and to_char(t.month,'yyyy-MM') = '" + sMonth + "'";
			whereStr +=" and to_char(t.start_date,'yyyy-MM') <= '"+sMonth+"%'";
			whereStr +=" and to_char(t.end_date,'yyyy-MM') >= '"+sMonth+"%'";
		}

		sql += whereStr;
		sqlCount += whereStr;
//		sql += " order by t.month";
		sql += " order by t.start_date";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<AttendanceDaysForm> arrlist = new ArrayList();
		while (it.hasNext()) {
			AttendanceDaysForm form = new AttendanceDaysForm();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				form.setAttendanceDaysId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				form.setMonth(data[1].toString());
			if (data[2] != null)
				form.setAttendanceDays(Double.parseDouble(data[2].toString()));
			if (data[3] != null)
				form.setMemo(data[3].toString());
			if (data[4] != null)
				form.setStartDate(data[4].toString());
			if (data[5] != null)
				form.setEndDate(data[5].toString());
			arrlist.add(form);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

}