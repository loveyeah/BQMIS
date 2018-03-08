/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 部门考勤登记Facade
 * 
 * @author jincong
 * @version 1.0
 */
@Stateless
public class DeptAttendanceFacade implements DeptAttendanceFacadeRemote {

	@EJB(beanName ="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
	
	/** 是否使用 */
	public static final String IS_USE_Y = "Y";
	/** 考勤类别 */
	public static final String ATTEN_DEPT_TYPE_3 = "3";
	/** 字符串: 0 */
	public static final String STRING_0 = "0";
	/** 字符串: 1 */
	public static final String STRING_1 = "1";
	/** 字符串: 2 */
	public static final String STRING_2 = "2";
	
	/**
	 * 查询所有考勤部门
	 * 
	 * @param loginId 系统登录人
	 * @param enterpriseCode 企业编码
	 * @return 考勤部门
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAttendanceDeptForRegister(String loginId,
			String enterpriseCode) {
		LogUtil.log("EJB:考勤部门查询开始。",
                Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// sql
        	StringBuffer sbSql = new StringBuffer();
        	sbSql.append("SELECT ");
        	sbSql.append("	A.ATTENDANCE_DEPT_ID AS ID, ");
        	sbSql.append("	A.ATTENDANCE_DEPT_NAME AS NAME ");
        	sbSql.append("FROM ");
        	sbSql.append("	HR_C_ATTENDANCEDEP A ");
        	sbSql.append("WHERE ");
        	sbSql.append("	A.ATTEND_WRITER_ID = ? AND ");
        	sbSql.append("	A.ATTEND_DEP_TYPE != ? AND ");
        	sbSql.append("	A.IS_USE = ? AND ");
        	sbSql.append("	A.ENTERPRISE_CODE = ? ");
        	sbSql.append("UNION ");
        	sbSql.append("SELECT ");
        	sbSql.append("	B.ATTENDANCE_DEPT_ID AS ID, ");
        	sbSql.append("	B.ATTENDANCE_DEPT_NAME AS NAME ");
        	sbSql.append("FROM ");
        	sbSql.append("	HR_C_ATTENDANCEDEP B ");
        	sbSql.append("WHERE ");
        	sbSql.append("	B.ATTEND_DEP_TYPE = ? AND ");
        	sbSql.append("	B.REPLACE_DEP_ID IN ");
        	sbSql.append("	(SELECT ");
        	sbSql.append("		C.ATTENDANCE_DEPT_ID ");
        	sbSql.append("	FROM ");
        	sbSql.append("		HR_C_ATTENDANCEDEP C ");
        	sbSql.append("	WHERE ");
        	sbSql.append("		C.ATTEND_WRITER_ID = ? AND ");
        	sbSql.append("		C.IS_USE = ? AND ");
        	sbSql.append("		C.ENTERPRISE_CODE = ?) ");
        	
        	// 查询
        	List list = bll.queryByNativeSQL(sbSql.toString(),
        		new Object[]{loginId, ATTEN_DEPT_TYPE_3, IS_USE_Y,
        		enterpriseCode, ATTEN_DEPT_TYPE_3, loginId, IS_USE_Y,
        		enterpriseCode});
        	Iterator it = list.iterator();
        	List<HrCAttendancedep> arrlist = new ArrayList<HrCAttendancedep>();
            while (it.hasNext()) {
            	HrCAttendancedep info = new HrCAttendancedep();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setAttendanceDeptId(Long.parseLong(data[0].toString()));
            	}
            	if(null != data[1]) {
            		info.setAttendanceDeptName(data[1].toString());
            	}
            	arrlist.add(info);
            }
            object.setList(arrlist);
            object.setTotalCount((long)arrlist.size());
            LogUtil.log("EJB:考勤部门查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:劳考勤部门查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 查询是否已经审核信息
	 * 
	 * @param attendanceYear
	 *            考勤年份
	 * @param attendanceMonth
	 *            考勤月份
	 * @param attendanceDeptId
	 *            考勤部门
	 * @param enterpriseCode
	 *            企业编码
	 * @return 是否已经审核信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject getApprovedForRegister(String attendanceYear,
			String attendanceMonth, String attendanceDeptId,
			String enterpriseCode) {
		LogUtil.log("EJB:判断是否已经审核开始。",
                Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// sql
        	StringBuffer sbSql = new StringBuffer();
        	sbSql.append("SELECT ");
        	sbSql.append("	A.ATTENDANCE_DEP, ");
        	sbSql.append("	A.DEP_CHARGE1, ");
        	sbSql.append("	TO_CHAR(A.CHECKED_DATE1, 'YYYY-MM-DD'), ");
        	sbSql.append("	A.DEP_CHARGE2, ");
        	sbSql.append("	TO_CHAR(A.CHECKED_DATE2, 'YYYY-MM-DD'), ");
        	sbSql.append("	B.ATTEND_DEP_TYPE, ");
        	sbSql.append("	B.TOP_CHECK_DEP_ID, ");
        	sbSql.append("	B.ATTENDANCE_DEPT_NAME, ");
        	sbSql.append("	B.ATTENDANCE_DEPT_ID, ");
        	sbSql.append("	B.ATTEND_WRITER_ID, ");
        	sbSql.append("	B.ATTEND_CHECKER_ID ");
        	sbSql.append("FROM ");
        	sbSql.append("	HR_C_ATTENDANCEDEP B ");
        	sbSql.append("	LEFT JOIN HR_J_ATTENDANCECHECK A ");
        	sbSql.append("	ON A.ATTENDANCE_YEAR = ? AND ");
        	sbSql.append("	A.ATTENDANCE_MONTH = ? AND ");
        	sbSql.append("	B.ATTENDANCE_DEPT_ID = A.ATTENDANCE_DEP AND ");
        	sbSql.append("	A.IS_USE = ? AND ");
        	sbSql.append("	A.ENTERPRISE_CODE = ? ");
        	sbSql.append("START WITH ");
        	sbSql.append("	B.ATTENDANCE_DEPT_ID = ? ");
        	sbSql.append("CONNECT BY ");
        	sbSql.append("	ATTENDANCE_DEPT_ID = PRIOR TOP_CHECK_DEP_ID AND ");
        	sbSql.append("	B.IS_USE = ? AND ");
        	sbSql.append("	B.ENTERPRISE_CODE = ? ");
        	
        	// 查询
        	List list = bll.queryByNativeSQL(sbSql.toString(),
        		new Object[]{attendanceYear, attendanceMonth,
        		IS_USE_Y, enterpriseCode, attendanceDeptId,
        		IS_USE_Y, enterpriseCode});
        	Iterator it = list.iterator();
        	List<DeptAttendance> arrlist = new ArrayList<DeptAttendance>();
            while (it.hasNext()) {
            	DeptAttendance info = new DeptAttendance();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setAttendanceDept(data[0].toString());
            	}
            	if(null != data[1]) {
            		info.setDeptCharge1(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setCheckedDate1(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setDeptCharge2(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setCheckedDate2(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setAttendDeptType(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setTopCheckDeptId(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setAttendanceDeptName(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setAttendanceDeptId(data[8].toString());
            	}
            	if(null != data[9]) {
            		info.setAttendWriterId(data[9].toString());
            	}
            	if(null != data[10]) {
            		info.setAttendCheckerId(data[10].toString());
            	}
            	arrlist.add(info);
            }
            object.setList(arrlist);
            object.setTotalCount((long)arrlist.size());
            LogUtil.log("EJB:判断是否已经审核结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:判断是否已经审核失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 查询所有假别
	 * 
	 * @param enterpriseCode 企业编码
	 * @return 所有假别 
	 */
	@SuppressWarnings("unchecked")
	public PageObject getVacationTypeCommon(String enterpriseCode) {
		LogUtil.log("EJB:所有假别查询开始。",
                Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// sql
        	StringBuffer sbSql = new StringBuffer();
        	sbSql.append("SELECT * ");
        	sbSql.append("FROM ");
        	sbSql.append("	HR_C_VACATIONTYPE A ");
        	sbSql.append("WHERE ");
        	sbSql.append("	A.IS_USE = ? AND ");
        	sbSql.append("	A.ENTERPRISE_CODE = ? ");
        	sbSql.append("ORDER BY ");
        	sbSql.append("	A.VACATION_TYPE_ID ");
        	
        	// 查询
        	List<HrCVacationtype> list = bll.queryByNativeSQL(sbSql.toString(),
        		new Object[]{IS_USE_Y, enterpriseCode},
        		HrCVacationtype.class);
            if(list == null) {
            	list = new ArrayList<HrCVacationtype>();
            }
            object.setList(list);
            object.setTotalCount((long)list.size());
            LogUtil.log("EJB:所有假别查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:所有假别查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 查询所有基本天数 add by liuyi 20100202
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @return 所有基本天数
	 */
	public PageObject getBasicDaysCommon(String enterpriseCode)
	{
		LogUtil.log("EJB:所有基本天数查询开始。",
                Level.INFO, null);
        try {
			PageObject object = new PageObject();
			String sql = "select * from hr_c_day a where a.is_use='Y' and a.enterprise_code='"
					+ enterpriseCode + "'";

			// 查询
			List<HrCDay> list = bll.queryByNativeSQL(sql, HrCDay.class);
			if (list == null) {
				list = new ArrayList<HrCDay>();
			}
			object.setList(list);
			object.setTotalCount((long) list.size());
			LogUtil.log("EJB:所有基本天数查询结束。", Level.INFO, null);
			return object;
		} catch (RuntimeException e) {
            LogUtil.log("EJB:所有基本天数查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	/**
	 * 查询所有加班类别
	 * 
	 * @param enterpriseCode 企业编码
	 * @return 所有加班类别
	 */
	@SuppressWarnings("unchecked")
	public PageObject getOvertimeTypeCommon(String enterpriseCode) {
		LogUtil.log("EJB:所有加班类别查询开始。",
                Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// sql
        	StringBuffer sbSql = new StringBuffer();
        	sbSql.append("SELECT * ");
        	sbSql.append("FROM ");
        	sbSql.append("	HR_C_OVERTIME A ");
        	sbSql.append("WHERE ");
        	sbSql.append("	A.IS_USE = ? AND ");
        	sbSql.append("	A.ENTERPRISE_CODE = ? ");
        	sbSql.append("ORDER BY ");
        	sbSql.append("	A.OVERTIME_TYPE_ID ");
        	
        	// 查询
        	List<HrCOvertime> list = bll.queryByNativeSQL(sbSql.toString(),
        		new Object[]{IS_USE_Y, enterpriseCode},
        		HrCOvertime.class);
            if(list == null) {
            	list = new ArrayList<HrCOvertime>();
            }
            object.setList(list);
            object.setTotalCount((long)list.size());
            LogUtil.log("EJB:所有加班类别查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:所有加班类别查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 查询所有运行班类别
	 * 
	 * @param enterpriseCode 企业编码
	 * @return 所有运行班类别
	 */
	@SuppressWarnings("unchecked")
	public PageObject getWorkshiftTypeCommon(String enterpriseCode) {
		LogUtil.log("EJB:所有运行班类别查询开始。",
                Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// sql
        	StringBuffer sbSql = new StringBuffer();
        	sbSql.append("SELECT * ");
        	sbSql.append("FROM ");
        	sbSql.append("	HR_C_WORKSHIFT A ");
        	sbSql.append("WHERE ");
        	sbSql.append("	A.IS_USE = ? AND ");
        	sbSql.append("	A.ENTERPRISE_CODE = ? ");
        	sbSql.append("ORDER BY ");
        	sbSql.append("	A.WORK_SHIFT_ID ");
        	
        	// 查询
        	List<HrCWorkshift> list = bll.queryByNativeSQL(sbSql.toString(),
        		new Object[]{IS_USE_Y, enterpriseCode},
        		HrCWorkshift.class);
            if(list == null) {
            	list = new ArrayList<HrCWorkshift>();
            }
            object.setList(list);
            object.setTotalCount((long)list.size());
            LogUtil.log("EJB:所有运行班类别查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:所有运行班类别查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 已审核明细部查询
	 * 
	 * @param attendanceDate
	 *            考勤日期
	 * @param attendanceDeptId
	 *            考勤部门
	 * @param enterpriseCode
	 *            企业编码
	 * @return 明细部信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject getApprovedDetailInfoForRegister(String attendanceDate,
			String attendanceDeptId, String enterpriseCode) {
		LogUtil.log("EJB:已审核明细部查询开始。",
                Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// sql
        	StringBuffer sbSql = new StringBuffer();
        	sbSql.append("SELECT ");
        	sbSql.append("	A.DEPT_ID, ");
        	sbSql.append("	A.DEPT_NAME, ");
        	sbSql.append("	B.EMP_ID, ");
        	sbSql.append("	B.CHS_NAME, ");
        	sbSql.append("	C.AM_BEGING_TIME, ");
        	sbSql.append("	C.AM_END_TIME, ");
        	sbSql.append("	C.PM_BEGING_TIME, ");
        	sbSql.append("	C.PM_END_TIME, ");
        	sbSql.append("	C.VACATION_TYPE_ID, ");
        	sbSql.append("	C.OVERTIME_TYPE_ID, ");
        	sbSql.append("	C.WORK_SHIFT_ID, ");
        	sbSql.append("	C.REST_TYPE, ");
        	sbSql.append("	C.ABSENT_WORK, ");
        	sbSql.append("	C.EVECTION_TYPE, ");
        	sbSql.append("	C.OUT_WORK, ");
        	sbSql.append("	C.MEMO ");
        	
        	// add by liuyi 20100203 
        	sbSql.append("   ,c.overtime_time_id,c.sick_leave_time_id,c.event_time_id,c.absent_time_id,c.other_time_id ");
        	sbSql.append("FROM ");
        	sbSql.append("	HR_C_DEPT A, ");
        	sbSql.append("	HR_J_EMP_INFO B ");
        	sbSql.append("	LEFT JOIN HR_J_WORKATTENDANCE C ");
        	sbSql.append("	ON B.EMP_ID = C.EMP_ID AND ");
        	sbSql.append("	TO_CHAR(C.ATTENDANCE_DATE , 'YYYY-MM-DD') = ? AND ");
        	sbSql.append("	C.IS_USE = ? AND ");
        	sbSql.append("	C.ENTERPRISE_CODE = ? ");
        	sbSql.append("WHERE ");
        	sbSql.append("	NVL(B.ATTENDANCE_DEPT_ID, B.DEPT_ID) = ? AND ");
        	sbSql.append("	B.DEPT_ID = A.DEPT_ID AND ");
        	sbSql.append("	A.IS_USE = ? AND ");
        	sbSql.append("	A.ENTERPRISE_CODE = ? AND ");
        	sbSql.append("	B.IS_USE = ? AND ");
        	sbSql.append("	B.ENTERPRISE_CODE = ? ");
        	sbSql.append("ORDER BY ");
        	sbSql.append("	A.DEPT_ID, ");
        	sbSql.append("	B.EMP_ID ");
        	
        	
        	// 查询
        	List list = bll.queryByNativeSQL(sbSql.toString(),
        		new Object[]{attendanceDate, IS_USE_Y, enterpriseCode,
        		attendanceDeptId, "Y", enterpriseCode,//modify by qxjiao20100903
        		IS_USE_Y, enterpriseCode});
        	Iterator it = list.iterator();
        	List<DeptAttendance> arrlist = new ArrayList<DeptAttendance>();
            while (it.hasNext()) {
            	DeptAttendance info = new DeptAttendance();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setDeptId(data[0].toString());
            	}
            	if(null != data[1]) {
            		info.setDeptName(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setEmpId(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setChsName(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setAmBeginTime(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setAmEndTime(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setPmBeginTime(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setPmEndTime(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setVacationTypeId(data[8].toString());
            	}
            	if(null != data[9]) {
            		info.setOverTimeId(data[9].toString());
            	}
            	if(null != data[10]) {
            		info.setWorkShiftId(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setRestType(data[11].toString());
            	}
            	if(null != data[12]) {
            		info.setAbsentWork(data[12].toString());
            	}
            	if(null != data[13]) {
            		info.setEvectionType(data[13].toString());
            	}
            	if(null != data[14]) {
            		info.setOutWork(data[14].toString());
            	}
            	if(null != data[15]) {
            		info.setMemo(data[15].toString());
            	}
            	
            	//add by liuyi 20100203
            	if(null != data[16])
            		info.setOvertimeTimeId(data[16].toString());
            	if(null != data[17])
            		info.setSickLeaveTimeId(data[17].toString());
            	if(null != data[18])
            		info.setEventTimeId(data[18].toString());
            	if(null != data[19])
            		info.setAbsentTimeId(data[19].toString());
            	if(null != data[20])
            		info.setOtherTimeId(data[20].toString());
            	
            	
            	info.setGridEditAble(false);
            	arrlist.add(info);
            }
            object.setList(arrlist);
            object.setTotalCount((long)arrlist.size());
            LogUtil.log("EJB:已审核明细部查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:已审核明细部查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 未审核明细部查询
	 * 
	 * @param attendanceDate
	 *            考勤日期
	 * @param attendanceYear
	 *            考勤年份
	 * @param attendanceMonth
	 *            考勤月份
	 * @param attendanceDeptId
	 *            考勤部门
	 * @param workResetFlag
	 *            上班休息标识
	 * @param enterpriseCode
	 *            企业编码
	 * @return 未审核明细部信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject getDetailInfoForRegister(String attendanceDate,
			String attendanceYear, String attendanceMonth,
			String attendanceDeptId, String workResetFlag, String enterpriseCode) {
		LogUtil.log("EJB:未审核明细部查询开始。",
                Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// sql
        	StringBuffer sbSql = new StringBuffer();
        	sbSql.append("SELECT ");
        	sbSql.append("	A.DEPT_ID, ");
        	sbSql.append("	A.DEPT_NAME, ");
        	sbSql.append("	B.EMP_ID, ");
        	sbSql.append("	B.CHS_NAME, ");
        	sbSql.append("	DECODE(C.EMP_ID, NULL, ");
        	sbSql.append(STRING_0);
        	sbSql.append("	, ");
        	sbSql.append(STRING_1);
        	sbSql.append("	) AS EXIST_FLAG, ");
        	sbSql.append("	DECODE(C.EMP_ID, NULL, DECODE(?, ");
        	sbSql.append(STRING_2);
        	sbSql.append("	, DECODE(F.WORK_KIND, NULL, D.AM_BEGING_TIME, ");
        	sbSql.append(STRING_1);
        	sbSql.append("	, D.AM_BEGING_TIME)), C.AM_BEGING_TIME), ");
        	sbSql.append("	DECODE(C.EMP_ID, NULL, DECODE(?, ");
        	sbSql.append(STRING_2);
        	sbSql.append("	, DECODE(F.WORK_KIND, NULL, D.AM_END_TIME, ");
        	sbSql.append(STRING_1);
        	sbSql.append("	, D.AM_END_TIME)), C.AM_END_TIME), ");
        	sbSql.append("	DECODE(C.EMP_ID, NULL, DECODE(?, ");
        	sbSql.append(STRING_2);
        	sbSql.append("	, DECODE(F.WORK_KIND, NULL, D.PM_BEGING_TIME, ");
        	sbSql.append(STRING_1);
        	sbSql.append("	, D.PM_BEGING_TIME)), C.PM_BEGING_TIME), ");
        	sbSql.append("	DECODE(C.EMP_ID, NULL, DECODE(?, ");
        	sbSql.append(STRING_2);
        	sbSql.append("	, DECODE(F.WORK_KIND, NULL, D.PM_END_TIME, ");
        	sbSql.append(STRING_1);
        	sbSql.append("	, D.PM_END_TIME)), C.PM_END_TIME), ");
        	sbSql.append("	DECODE(C.EMP_ID, NULL, DECODE(?, ");
        	sbSql.append(STRING_2);
        	sbSql.append("	, E.VACATION_TYPE_ID , DECODE(E.IF_WEEKEND, ");
        	sbSql.append(STRING_1);
        	sbSql.append("	, E.VACATION_TYPE_ID)), C.VACATION_TYPE_ID), ");
        	sbSql.append("	C.OVERTIME_TYPE_ID, ");
        	sbSql.append("	C.WORK_SHIFT_ID, ");
        	sbSql.append("	DECODE(C.EMP_ID, NULL, DECODE(?, ");
        	sbSql.append(STRING_1);
        	sbSql.append("	, DECODE(");
        	sbSql.append("	DECODE(C.EMP_ID, NULL, DECODE(?, ");
        	sbSql.append(STRING_2);
        	sbSql.append("	, E.VACATION_TYPE_ID , DECODE(E.IF_WEEKEND, ");
        	sbSql.append(STRING_1);
        	sbSql.append("	, E.VACATION_TYPE_ID)), C.VACATION_TYPE_ID), NULL, ");
        	sbSql.append(STRING_1);
        	sbSql.append("	 )), C.REST_TYPE), ");
        	sbSql.append("	C.ABSENT_WORK, ");
        	sbSql.append("	C.EVECTION_TYPE, ");
        	sbSql.append("	C.OUT_WORK, ");
        	sbSql.append("	C.MEMO, ");
        	sbSql.append("	D.AM_BEGING_TIME, ");
        	sbSql.append("	D.AM_END_TIME, ");
        	sbSql.append("	D.PM_BEGING_TIME, ");
        	sbSql.append("	D.PM_END_TIME, ");
        	sbSql.append("	TO_CHAR(C.LAST_MODIFIY_DATE, 'YYYY-MM-DD hh24:mi:ss') ");
        	
        	// add by liuyi 20100202 
        	sbSql.append("   ,c.overtime_time_id,c.sick_leave_time_id,c.event_time_id,c.absent_time_id,c.other_time_id ");
        	sbSql.append("FROM ");
        	sbSql.append("	HR_C_DEPT A, ");
        	sbSql.append("	HR_J_EMP_INFO B ");
        	sbSql.append("	LEFT JOIN HR_J_WORKATTENDANCE C ");
        	sbSql.append("	ON B.EMP_ID = C.EMP_ID AND ");
        	sbSql.append("	TO_CHAR(C.ATTENDANCE_DATE , 'YYYY-MM-DD') = ? AND ");
        	sbSql.append("	C.IS_USE = ? AND ");
        	sbSql.append("	C.ENTERPRISE_CODE = ? ");
        	sbSql.append("	LEFT JOIN HR_C_ATTENDANCESTANDARD D ");
        	sbSql.append("	ON D.ATTENDANCE_DEPT_ID = NVL(B.ATTENDANCE_DEPT_ID, B.DEPT_ID) AND ");
        	sbSql.append("	D.ATTENDANCE_YEAR = ? AND ");
        	sbSql.append("	D.ATTENDANCE_MONTH = ? AND ");
        	sbSql.append("	D.IS_USE = ? AND ");
        	sbSql.append("	D.ENTERPRISE_CODE = ? ");
        	sbSql.append("	LEFT JOIN HR_J_VACATION H ");
        	sbSql.append("	ON H.EMP_ID = B.EMP_ID AND ");
        	sbSql.append("	TO_CHAR(H.START_TIME , 'YYYY-MM-DD') <= ? AND ");
        	sbSql.append("	TO_CHAR(H.END_TIME , 'YYYY-MM-DD') >= ? AND ");
        	sbSql.append("	H.IS_USE = ? AND ");
        	sbSql.append("	H.ENTERPRISE_CODE = ? ");
        	sbSql.append("	LEFT JOIN HR_C_VACATIONTYPE E ");
        	sbSql.append("	ON E.VACATION_TYPE_ID = H.VACATION_TYPE_ID AND ");
        	sbSql.append("	E.ENTERPRISE_CODE = ? ");
        	sbSql.append("	LEFT JOIN HR_C_STATION F ");
        	sbSql.append("	ON B.STATION_ID = F.STATION_ID AND ");
        	sbSql.append("	F.IS_USE = ? AND ");
        	sbSql.append("	F.ENTERPRISE_CODE = ? ");
        	sbSql.append("WHERE ");
        	sbSql.append("	NVL(B.ATTENDANCE_DEPT_ID, B.DEPT_ID) = ? AND ");
        	sbSql.append("	B.DEPT_ID = A.DEPT_ID AND ");
        	sbSql.append("	A.IS_USE = ? AND ");
        	sbSql.append("	A.ENTERPRISE_CODE = ? AND ");
        	sbSql.append("	B.IS_USE = ? AND ");
        	sbSql.append("	B.ENTERPRISE_CODE = ? ");
        	sbSql.append("ORDER BY ");
        	sbSql.append("	A.DEPT_ID, ");
        	sbSql.append("	B.EMP_ID ");
        	
        	// 查询
        	List list = bll.queryByNativeSQL(sbSql.toString(),
        		new Object[]{workResetFlag, workResetFlag, workResetFlag,
        		workResetFlag, workResetFlag, workResetFlag, workResetFlag,
        		attendanceDate, IS_USE_Y, enterpriseCode,
        		attendanceYear, attendanceMonth,
        		IS_USE_Y, enterpriseCode, attendanceDate,
        		attendanceDate, IS_USE_Y, enterpriseCode,
        		enterpriseCode, IS_USE_Y, enterpriseCode,
        		// modify by liuyi 090922 部门表中U标识使用中
//        		attendanceDeptId, IS_USE_Y, enterpriseCode,
        		attendanceDeptId, "Y", enterpriseCode,//modify by qxjiao20100903
        		IS_USE_Y, enterpriseCode});
        	Iterator it = list.iterator();
        	List<DeptAttendance> arrlist = new ArrayList<DeptAttendance>();
            while (it.hasNext()) {
            	DeptAttendance info = new DeptAttendance();
            	Object[] data = (Object[]) it.next();
            	if(null != data[0]) {
            		info.setDeptId(data[0].toString());
            	}
            	if(null != data[1]) {
            		info.setDeptName(data[1].toString());
            	}
            	if(null != data[2]) {
            		info.setEmpId(data[2].toString());
            	}
            	if(null != data[3]) {
            		info.setChsName(data[3].toString());
            	}
            	if(null != data[4]) {
            		info.setExistFlag(data[4].toString());
            	}
            	if(null != data[5]) {
            		info.setAmBeginTime(data[5].toString());
            	}
            	if(null != data[6]) {
            		info.setAmEndTime(data[6].toString());
            	}
            	if(null != data[7]) {
            		info.setPmBeginTime(data[7].toString());
            	}
            	if(null != data[8]) {
            		info.setPmEndTime(data[8].toString());
            	}
            	if(null != data[9]) {
            		info.setVacationTypeId(data[9].toString());
            	}
            	if(null != data[10]) {
            		info.setOverTimeId(data[10].toString());
            	}
            	if(null != data[11]) {
            		info.setWorkShiftId(data[11].toString());
            	}
            	if(null != data[12]) {
            		info.setRestType(data[12].toString());
            	}
            	if(null != data[13]) {
            		info.setAbsentWork(data[13].toString());
            	}
            	if(null != data[14]) {
            		info.setEvectionType(data[14].toString());
            	}
            	if(null != data[15]) {
            		info.setOutWork(data[15].toString());
            	}
            	if(null != data[16]) {
            		info.setMemo(data[16].toString());
            	}
            	if(null != data[17]) {
            		info.setStandardAmBeginTime(data[17].toString());
            	}
            	if(null != data[18]) {
            		info.setStandardAmEndTime(data[18].toString());
            	}
            	if(null != data[19]) {
            		info.setStandardPmBeginTime(data[19].toString());
            	}
            	if(null != data[20]) {
            		info.setStandardPmEndTime(data[20].toString());
            	}
            	if(null != data[21]) {
            		info.setLastModifiyDate(data[21].toString());
            	}
            	
            	//add by liuyi 20100202 
            	if(null != data[22])
            		info.setOvertimeTimeId(data[22].toString());
            	if(null != data[23])
            		info.setSickLeaveTimeId(data[23].toString());
            	if(null != data[24])
            		info.setEventTimeId(data[24].toString());
            	if(null != data[25])
            		info.setAbsentTimeId(data[25].toString());
            	if(null != data[26])
            		info.setOtherTimeId(data[26].toString());
            	
            	
            	
            	info.setGridEditAble(true);
            	arrlist.add(info);
            }
            object.setList(arrlist);
            object.setTotalCount((long)arrlist.size());
            LogUtil.log("EJB:未审核明细部查询结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:未审核明细部查询失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 查询符合条件的人员个数
	 * 
	 * @param attendanceDate 考勤日期
	 * @param attendanceDeptId 考勤部门
	 * @param enterpriseCode 企业编码
	 * @return 个数
	 */
	public int getEmpCountForRegister(String attendanceDate,
			String attendanceDeptId, String enterpriseCode) {
		LogUtil.log("EJB:查询符合条件的人员个数开始。",
                Level.INFO, null);
        try {
        	int count = 0;
        	// sql
        	StringBuffer sbSql = new StringBuffer();
        	sbSql.append("SELECT ");
        	sbSql.append("	COUNT(A.EMP_ID) ");
        	sbSql.append("FROM ");
        	sbSql.append("	HR_J_WORKATTENDANCE A, ");
        	sbSql.append("	HR_J_EMP_INFO B ");
        	sbSql.append("WHERE ");
        	sbSql.append("	TO_CHAR(A.ATTENDANCE_DATE , 'YYYY-MM-DD') = ? AND ");
        	sbSql.append("	A.EMP_ID = B.EMP_ID AND ");
        	sbSql.append("	NVL(B.ATTENDANCE_DEPT_ID, B.DEPT_ID) = ? AND ");
        	sbSql.append("	A.IS_USE = ? AND ");
        	sbSql.append("	A.ENTERPRISE_CODE = ? AND ");
        	sbSql.append("	B.IS_USE = ? AND ");
        	sbSql.append("	B.ENTERPRISE_CODE = ? ");
        	
        	// 查询
        	count = Integer.parseInt(bll.getSingal(sbSql.toString(),
        		new Object[]{attendanceDate, attendanceDeptId, IS_USE_Y,
        		enterpriseCode, IS_USE_Y, enterpriseCode}).toString());
            LogUtil.log("EJB:查询符合条件的人员个数结束。", Level.INFO, null);
            return count;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:查询符合条件的人员个数失败。", Level.SEVERE, e);
            throw e;
        }
	}
	
	/**
	 * 查询节假日信息
	 * 
	 * @param attendanceDate 考勤日期
	 * @param enterpriseCode 企业编码
	 * @return 节假日信息
	 */
	@SuppressWarnings("unchecked")
	public PageObject getHolidayForRegister(String attendanceDate,
			String enterpriseCode) {
		LogUtil.log("EJB:查询节假日信息开始。",
                Level.INFO, null);
        try {
        	PageObject object = new PageObject();
        	// sql
        	StringBuffer sbSql = new StringBuffer();
        	sbSql.append("SELECT * ");
        	sbSql.append("FROM ");
        	sbSql.append("	HR_C_HOLIDAY A ");
        	sbSql.append("WHERE ");
        	sbSql.append("	TO_CHAR(A.HOLIDAY_DATE , 'YYYY-MM-DD') = ? AND ");
        	sbSql.append("	A.IS_USE = ? AND ");
        	sbSql.append("	A.ENTERPRISE_CODE = ? ");
        	sbSql.append("ORDER BY ");
        	sbSql.append("	A.HOLIDAY_ID ");
        	
        	// 查询
        	List<HrCHoliday> list = bll.queryByNativeSQL(sbSql.toString(),
        		new Object[]{attendanceDate, IS_USE_Y, enterpriseCode},
        		HrCHoliday.class);
            if(list == null) {
            	list = new ArrayList<HrCHoliday>();
            }
            object.setList(list);
            object.setTotalCount((long)list.size());
            LogUtil.log("EJB:查询节假日信息结束。", Level.INFO, null);
            return object;
        } catch (RuntimeException e) {
            LogUtil.log("EJB:查询节假日信息失败。", Level.SEVERE, e);
            throw e;
        }
	}
}
