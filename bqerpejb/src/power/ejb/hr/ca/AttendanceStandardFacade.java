/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrCDept;
import power.ejb.hr.HrCDeptFacadeRemote;
import bsh.ParseException;

/**
 * 考勤标准维护 AttendancestandardFacade.
 * 
 * @author chenshoujiang
 */
@Stateless
public class AttendanceStandardFacade implements AttendanceStandardFacadeRemote {

	// fields
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/** 部门设置表 */
	HrCDeptFacadeRemote hrcDeptFacadeRemote;
	/** 考勤部门维护表 */
	HrCAttendancedepFacadeRemote hrcAttendancedepFacadeRemote;

	/** 空值 */
	private String BLANK = "";

	/**
	 * 构造函数
	 */
	public AttendanceStandardFacade() {
		// 部门设置表
		hrcDeptFacadeRemote = (HrCDeptFacadeRemote) (Ejb3Factory.getInstance())
				.getFacadeRemote("HrCDeptFacade");
		// 考勤部门维护表
		hrcAttendancedepFacadeRemote = (HrCAttendancedepFacadeRemote) (Ejb3Factory
				.getInstance()).getFacadeRemote("HrCAttendancedepFacade");
	}

	/**
	 * 查询考勤部门维护信息
	 * 
	 * @param id
	 * @param workcode
	 * @param enterpriseCode
	 * @return
	 * @throws SQLException
	 */
	public void moveDeptData(String workcode, String enterpriseCode,
			String depType) throws SQLException {
//		LogUtil.log("Ejb:查询考勤部门维护信息开始", Level.INFO, null);
//		try {
//			PageObject obj = hrcDeptFacadeRemote.getDeptInfo(enterpriseCode);
//			if (obj.getList() != null && obj.getList().size() > 0) {
//				for (int i = 0; i < obj.getList().size(); i++) {
//					HrCDept dept = (HrCDept) obj.getList().get(i);
//					HrCAttendancedep bean = new HrCAttendancedep();
//					// ID
//					bean.setId(bll.getMaxId("HR_C_ATTENDANCEDEP", "ID") + i);
//					// 考勤部门ID
//					bean.setAttendanceDeptId(dept.getDeptId());
//					// 考勤部门名称
//					if (checkNull(dept.getDeptName())) {
//						bean.setAttendanceDeptName(dept.getDeptName());
//					}
//					// 考勤类别
//					bean.setAttendDepType(depType);
//					// 上级审核部门
//					if (checkNull(dept.getPdeptId().toString())) {
//						bean.setTopCheckDepId(dept.getPdeptId());
//					}
//					// 代考勤部门
//					bean.setReplaceDepId(null);
//					// 考勤登记人
//					bean.setAttendWriterId(null);
//					// 考勤审核人
//					bean.setAttendCheckerId(null);
//					// 上次修改人
//					bean.setLastModifiyBy(workcode);
//					// 企业编码
//					bean.setEnterpriseCode(enterpriseCode);
//					// 是否使用
//					bean.setIsUse("Y");
//					// 保存
//					hrcAttendancedepFacadeRemote.save(bean);
//				}
//			}
//			LogUtil.log(" Ejb:查询考勤部门维护信息结束", Level.INFO, null);
//		} catch (RuntimeException e) {
//			LogUtil.log(" Ejb:查询考勤部门维护信息失败", Level.INFO, null);
//			throw new SQLException(e.getMessage());
//		}
	}

	/**
	 * 通过考勤年份和考勤部门id查询信息
	 * 
	 * @param attendanceYear
	 * @param attendanceDeptId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * @throws SQLException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAttendanceStandard(String attendanceYear,
			String attendanceDeptId, String attendanceDeptName,String isRoot,
			String enterpriseCode) throws SQLException, ParseException {
		LogUtil.log("通过考勤年份和考勤部门id查询信息开始: ", Level.INFO, null);
		try {
			PageObject result = new PageObject();
			Object[] params = new Object[5];
			params[0] = enterpriseCode;
			params[1] = "Y";
			params[2] = enterpriseCode;
			params[3] = attendanceYear;
			params[4] = attendanceDeptId;
			StringBuilder sb = new StringBuilder();
			// 查询sql
			sb
					.append("select A.ATTENDANCESTANDARDID AS attendancestandardid, ");
			sb.append(" A.ATTENDANCE_YEAR AS attendanceYear, ");
			sb.append("A.ATTENDANCE_MONTH AS attendanceMonth, ");
			sb.append(" A.ATTENDANCE_DEPT_ID AS attendanceDeptId, ");
			sb.append(" B.ATTENDANCE_DEPT_NAME AS attendanceDeptName, ");
			sb.append("to_char(A.START_DATE,'yyyy-mm-dd') AS startDate, ");
			sb.append("to_char(A.END_DATE,'yyyy-mm-dd') AS endDate, ");
			sb.append(" A.STANDARD_DAY AS standardDay, ");
			sb.append(" A.AM_BEGING_TIME AS amBegingTime, ");
			sb.append(" A.AM_END_TIME AS amEndTime, ");
			sb.append(" A.PM_BEGING_TIME AS pmBegingTime, ");
			sb.append(" A.PM_END_TIME AS pmEndTime, ");
			sb.append(" A.STANDARD_TIME AS standardTime, ");
			sb
					.append(" to_char(A.LAST_MODIFIY_DATE,'yyyy-mm-dd hh24:mi:ss') AS lastModifyDate ");
			sb.append(" from HR_C_ATTENDANCESTANDARD A ");
			sb.append(" Left join HR_C_ATTENDANCEDEP B  ");
			sb.append(" on A.ATTENDANCE_DEPT_ID = B.ATTENDANCE_DEPT_ID  ");
			sb.append(" AND B.ENTERPRISE_CODE = ? ");
			sb.append(" where A.IS_USE = ? AND A.ENTERPRISE_CODE = ? ");
			sb
					.append(" and A.ATTENDANCE_YEAR = ? AND A.ATTENDANCE_DEPT_ID = ? ");
			sb.append(" order by A.ATTENDANCE_MONTH");

			// 执行查询
			List list = bll.queryByNativeSQL(sb.toString(), params);
			LogUtil.log("EJB-getAttendanceStandard:SQL=" + sb.toString(),
					Level.INFO, null);
			// 创建一个List
			List<AttendanceStandard> arrList = new ArrayList<AttendanceStandard>(
					12);
			Iterator it = list.iterator();
			int month = 1;
			// 记录考勤标准时间
			String standardTime = "";
			Object[][] dataArray = new Object[12][14];
			if (it.hasNext()) {
				while (it.hasNext()) {
					Object[] data = (Object[]) it.next();
					if (data[2] != null && !data[2].toString().equals(BLANK)) {
						dataArray[Integer.parseInt((String) data[2]) - 1] = data;
					}
				}
				for (int i = 0; i < 12; i++) {
					if (dataArray[i][2] == null || dataArray[i][2] == "") {
						// 考勤标准ID
						dataArray[i][0] = "";
						// 考勤年份
						dataArray[i][1] = attendanceYear;
						// 考勤月份
						dataArray[i][2] = Integer.toString(i + 1);
						// 考勤部门ID
						dataArray[i][3] = attendanceDeptId;
						// 考勤部门名称
						dataArray[i][4] = attendanceDeptName;
						if(isRoot.equals("Y")) {
							// 开始日期
							dataArray[i][5] = getYearMonthFirstDay(attendanceYear,
									Integer.toString(i + 1));
							// 结束日期
							dataArray[i][6] = getYearMonthEndDay(attendanceYear,
									Integer.toString(i + 1));
						}else {
							// 开始日期
							dataArray[i][5] = "";
							// 结束日期
							dataArray[i][6] = "";
						}
						dataArray[i][7] = "";
						dataArray[i][8] = "";
						dataArray[i][9] = "";
						dataArray[i][10] = "";
						dataArray[i][11] = "";
						dataArray[i][12] = "";
						dataArray[i][13] = "";
					}
				}
				for (int i = 0; i < 12; i++) {
					// 创建一个bean
					AttendanceStandard info = new AttendanceStandard();
					// 考勤标准ID
					if (dataArray[i][0] != null
							&& !dataArray[i][0].toString().equals(BLANK)) {
						info
								.setAttendancestandardid(dataArray[i][0]
										.toString());
					}
					if (dataArray[i][2] != null
							&& !dataArray[i][2].toString().equals(BLANK)) {
						// modify by liuyi 090908 使2-12月份中的年份，部门id,部门名称，标准出勤时间不为空
//						if (dataArray[i][2].equals("01")
//								|| dataArray[i][2].equals("1")) {
							// 考勤年份
							info.setAttendanceYear(dataArray[i][1].toString());
							// 考勤部门ID
							if (dataArray[i][3] != null
									&& !dataArray[i][3].toString()
											.equals(BLANK)) {
								info.setAttendanceDeptId(dataArray[i][3]
										.toString());
							}
							// 考勤部门名称
							if (dataArray[i][4] != null
									&& !dataArray[i][4].toString()
											.equals(BLANK)) {
								info.setAttendanceDeptName(dataArray[i][4]
										.toString());
							}
							// 标准出勤时间
							if (dataArray[i][12] != null
									&& !dataArray[i][12].toString().equals(
											BLANK)) {
								info.setStandardTime(dataArray[i][12]
										.toString());
								standardTime = dataArray[i][12].toString();
							} else {
								info.setStandardTime("");
							}
						//modify by liuyi 090908 使2-12月份中的年份，部门id,部门名称，标准出勤时间不为空
//						} else {
//							// 考勤年份
//							info.setAttendanceYear("");
//							// 考勤部门ID
//							info.setAttendanceDeptId("");
//							// 考勤部门名称
//							info.setAttendanceDeptName("");
//							// 标准出勤时间
//							info.setStandardTime("");
//							if (dataArray[i][12] != null
//									&& !dataArray[i][12].toString().equals(
//											BLANK)) {
//								standardTime = dataArray[i][12].toString();
//							}
//						}
						// 考勤月份
						if (Integer.parseInt((String) dataArray[i][2]) < 10
								&& dataArray[i][2].toString().length() >= 2) {
							info.setAttendanceMonth(dataArray[i][2].toString()
									.substring(1, 2));
						} else {
							info.setAttendanceMonth(dataArray[i][2].toString());
						}
					}
					// 开始日期
					if (dataArray[i][5] != null
							&& !dataArray[i][5].toString().equals(BLANK)) {
						info.setStartDate(dataArray[i][5].toString());
					}
					// 结束日期
					if (dataArray[i][6] != null
							&& !dataArray[i][6].toString().equals(BLANK)) {
						info.setEndDate(dataArray[i][6].toString());
					}
					// 标准天数
					if (dataArray[i][7] != null
							&& !dataArray[i][7].toString().equals(BLANK)) {
						info.setStandardDay(dataArray[i][7].toString());
					}
					// 上午上班时间
					if (dataArray[i][8] != null
							&& !dataArray[i][8].toString().equals(BLANK)) {
						info.setAmBegingTime(dataArray[i][8].toString());
					}
					// 上午下班时间
					if (dataArray[i][9] != null
							&& !dataArray[i][9].toString().equals(BLANK)) {
						info.setAmEndTime(dataArray[i][9].toString());
					}
					// 下午上班时间
					if (dataArray[i][10] != null
							&& !dataArray[i][10].toString().equals(BLANK)) {
						info.setPmBegingTime(dataArray[i][10].toString());
					}
					// 下午下班时间
					if (dataArray[i][11] != null
							&& !dataArray[i][11].toString().equals(BLANK)) {
						info.setPmEndTime(dataArray[i][11].toString());
					}
					// 上次修改日期
					if (dataArray[i][13] != null
							&& !dataArray[i][13].toString().equals(BLANK)) {
						info.setLastModifiyDate(dataArray[i][13].toString());
					}
					arrList.add(info);
				}
				arrList.get(0).setStandardTime(standardTime);
			} else {
				while (month <= 12) {
					AttendanceStandard entity = new AttendanceStandard();
					if (month == 1) {
						// 考勤年份
						entity.setAttendanceYear(attendanceYear);
						// 考勤部门ID
						entity.setAttendanceDeptId(attendanceDeptId);
						// 考勤部门名称
						entity.setAttendanceDeptName(attendanceDeptName);
					} else {
						// 考勤年份
						entity.setAttendanceYear("");
						// 考勤部门ID
						entity.setAttendanceDeptId("");
						// 考勤部门名称
						entity.setAttendanceDeptName("");
					}
					// 标准出勤时间
					entity.setStandardTime("");
					// 考勤月份
					entity.setAttendanceMonth(Integer.toString(month));
					if(isRoot.equals("Y")) {
					// 开始日期
					entity.setStartDate(getYearMonthFirstDay(attendanceYear,
							Integer.toString(month)));
					// 结束日期
					entity.setEndDate(getYearMonthEndDay(attendanceYear,
							Integer.toString(month)));
					}else{
						// 开始日期
						entity.setStartDate("");
						// 结束日期
						entity.setEndDate("");
					}
					// 上午上班时间
					entity.setAmBegingTime("");
					// 上午下班时间
					entity.setAmEndTime("");
					// 下午上班时间
					entity.setPmBegingTime("");
					// 下午下班时间
					entity.setPmEndTime("");
					arrList.add(entity);
					month++;
				}
			}
			// 查询sql
			StringBuilder sqlCount = new StringBuilder();
			sqlCount.append("select count(A.ATTENDANCESTANDARDID) ");
			sqlCount.append(" from HR_C_ATTENDANCESTANDARD A ");
			sqlCount.append(" Left join HR_C_DEPT B  ");
			sqlCount.append(" on A.ATTENDANCE_DEPT_ID = B.DEPT_ID  ");
			sqlCount.append(" AND B.ENTERPRISE_CODE = ? ");
			sqlCount.append(" where A.IS_USE = ? AND A.ENTERPRISE_CODE = ? ");
			sqlCount
					.append(" and A.ATTENDANCE_YEAR = ? AND A.ATTENDANCE_DEPT_ID = ? ");
			// 执行查询
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount.toString(),
					params).toString());
			// 设置PageObject
			result.setList(arrList);
			result.setTotalCount(totalCount);
			LogUtil.log("通过考勤年份和考勤部门id查询信息结束: ", Level.INFO, null);
			// 返回
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("通过考勤年份和考勤部门id查询信息失败: ", Level.INFO, null);
			throw new SQLException(e.getMessage());
		}
	}

	/**
	 * check字符串是否为空或者null
	 */
	private Boolean checkNull(String chaStr) {
		if (chaStr != null && !chaStr.equals(""))
			return true;
		else
			return false;
	}

	/***************************************************************************
	 * @功能 计算某年某月的开始日期
	 * @return interger
	 * @throws ParseException
	 **************************************************************************/
	public String getYearMonthFirstDay(String tempYear, String tempMonth)
			throws ParseException {

		// 分别取得当前日期的年、月、日
		String tempDay = "01";
		if (tempMonth.length() == 1)
			tempMonth = "0" + tempMonth;
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return tempDate;
	}

	/***************************************************************************
	 * @功能 计算某年某月的结束日期
	 * @return String
	 * @throws ParseException
	 **************************************************************************/
	public String getYearMonthEndDay(String tempYear, String tempMonth)
			throws ParseException {
		// 分别取得当前日期的年、月、日
		String tempDay = "31";
		if (tempMonth.equals("1") || tempMonth.equals("3")
				|| tempMonth.equals("5") || tempMonth.equals("7")
				|| tempMonth.equals("8") || tempMonth.equals("10")
				|| tempMonth.equals("12")) {
			tempDay = "31";
		}
		if (tempMonth.equals("4") || tempMonth.equals("6")
				|| tempMonth.equals("9") || tempMonth.equals("11")) {
			tempDay = "30";
		}
		if (tempMonth.equals("2")) {
			if (new java.util.GregorianCalendar().isLeapYear(Integer
					.parseInt(tempYear))) {
				tempDay = "29";
			} else {
				tempDay = "28";
			}
		}
		if (tempMonth.length() == 1)
			tempMonth = "0" + tempMonth;
		if (tempDay.length() == 1)
			tempDay = "0" + tempDay;
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return tempDate;
	}

}
