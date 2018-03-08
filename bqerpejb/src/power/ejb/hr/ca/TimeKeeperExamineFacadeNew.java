/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.DataChangeException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * 考勤员审核方法体
 * 
 * @author zhouxu
 */
@Stateless
public class TimeKeeperExamineFacadeNew implements
		TimeKeeperExamineFacadeNewRemote {
	@Resource
	SessionContext ctx;
	/** db操作类接口 */
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 考勤登记表操作类接口 */
	@EJB(beanName = "HrJWorkattendanceFacade")
	protected HrJWorkattendanceFacadeRemote empAttendanceManager;
	/** 考勤审核表操作类接口 */
	@EJB(beanName = "HrJAttendancecheckFacade")
	protected HrJAttendancecheckFacadeRemote attendanceCheckRemote;
	/** 加班统计表操作类接口 */
	@EJB(beanName = "HrDOvertimetotalFacade")
	protected HrDOvertimetotalFacadeRemote overTimeTotalManager;
	/** 运行班统计表操作类接口 */
	@EJB(beanName = "HrDWorkshifttotalFacade")
	protected HrDWorkshifttotalFacadeRemote workShiftTotalManager;
	/** 请假统计表操作接口 */
	@EJB(beanName = "HrDVacationtotalFacade")
	protected HrDVacationtotalFacadeRemote vacationTotalManager;
	/** 出勤统计表操作接口 */
	@EJB(beanName = "HrDWorkdaysFacade")
	protected HrDWorkdaysFacadeRemote workDaysTotalManager;
	/** 是否使用: 是 */
	private static final String IS_USE_Y = "Y";
	/** 标识: 0 */
	private static final String FLAG_0 = "0";
	/** 标识: 1 */
	private static final String FLAG_1 = "1";
	/** 标识: 2 */
	private static final String FLAG_2 = "2";
	/** flag: 是 */
	private static final String FLAG_Y = "Y";
	/** flag: 否 */
	private static final String FLAG_N = "N";
	/** 审核部门类别 */
	private static final String ATTEND_DEP_TYPE_2 = "2";
	/** 日期格式 */
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	/** 时间格式 */
	private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 空字符串 */
	private static final String BLANK_STRING = "";
	/** 没有结果抛出错误类型 */
	private static final String NO_RESULT_N = "N";
	/** 日期列dataindex标识符 */
	private static final String DATE_DATAINDEX = "D";
	/** 合计项列dataindex标识符 */
	private static final String TOTAL_DATAINDEX = "C";
	/** *出勤合计* */
	private static final String TOTAL_TATAINDEX1 = " E";
	/** 未审核部门分隔符 */
	private static final String UNCHECK_CUT = ",";
	/** 日期列开始位置 */
	private static final int DATE_START_LOCAL = 6;
	/** 员工信息列宽度 */
	private static final int WIDTH_EMP_INFO = 100;
	/** 日期列宽度 */
	private static final int WIDTH_DATE = 75;
	/** 合计项列宽度 */
	private static final int WIDTH_TOTAL = 95;

	/** 开始日期结束日期集 */
	private List<Map<String, Object>> dayFields = new ArrayList<Map<String, Object>>();
	/** 合计项表头集 */
	private List<HrCVacationtype> totalNameList = new ArrayList<HrCVacationtype>();

	@SuppressWarnings("unchecked")
	public TimeKeeperExamineForm getExamineInfo(Long empId,String examineDate,
			String argDeptId, String argEnterpriseCode, Properties p,String flag,String ... entryIds)
			throws Exception {
		LogUtil.log("EJB:考勤员审核查询开始", Level.INFO, null);
		try {
			TimeKeeperExamineForm dataAll = new TimeKeeperExamineForm();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			StoreObject obj = new StoreObject();
			String strStartDate = examineDate + "-01";
			Calendar end = Calendar.getInstance();
			end.setTime(sdf.parse(strStartDate));
			int dayOfMonthCount = end.getActualMaximum(Calendar.DAY_OF_MONTH);
			String strEndDate = examineDate + "-" + dayOfMonthCount;
			// 标准出勤时间
			String standardTime = BLANK_STRING;
			// 设置返回值的metadata 表头
			obj.setMetaData(creatGridHeader(strStartDate, strEndDate,
					argEnterpriseCode, p));
			// 查询所有员工的考勤信息
			List<TimeKeeperExamineContentNew> arrListContent = allEmpAttendanceList(empId,
					strStartDate, strEndDate, argDeptId, argEnterpriseCode, p,flag,entryIds);
			List resultList = new ArrayList();
			Calendar cs = Calendar.getInstance();
			Calendar cn = Calendar.getInstance();
			Date s = sdf.parse(strStartDate);
			cs.setTime(s);
			// 设置天数
			int daysCount = dayFields.size();
			// 定义查询的记录集开始位置
			int i = 0;
			// 获取查询记录集数量
			int recordsCount = arrListContent.size();
//			if (recordsCount == 0)
//			{
//				return null;
//				//throw new DataChangeException(NO_RESULT_N);
//				
//			}

			// 如果还有记录
			while (i < recordsCount) {
				// 定义单元格
				Map<Object, Object> cell = new HashMap<Object, Object>();
				cell.put("empName", arrListContent.get(i).getEmpName());
				cell.put("empId", arrListContent.get(i).getEmpId());
				cell.put("deptName", arrListContent.get(i).getDeptName());
				cell.put("deptId", arrListContent.get(i).getDeptId());
				cell.put("attendanceDeptId", arrListContent.get(i)
						.getAttendanceDeptId());

//				// 请假类别统计项
				for (int j1 = 0; j1 < totalNameList.size(); j1++) {
					cell.put(obj.getMetaData().getFields().get(
									j1 + DATE_START_LOCAL + daysCount).get(
									"dataIndex"), getMontDaysByAttendance(
									examineDate, argDeptId, arrListContent.get(
											i).getEmpId(), arrListContent
											.get(i).getOtherTimeId(),totalNameList.get(j1).getVacationTypeId().toString()));
				}
				// checkCloum 统计项
				for (int e2 = 0; e2 < 5; e2++) {
					cell.put(obj.getMetaData().getFields().get(
							e2 + DATE_START_LOCAL + daysCount
									+ totalNameList.size()).get("dataIndex"),
							getMonthTotalByAttendance(examineDate, argDeptId,
									arrListContent.get(i).getEmpId(), e2));
				}

				// 如果该员工的考勤时间为空，则添加空白记录
				if (isEmpty(arrListContent.get(i).getAtendanceDate())) {
					for (int j = 0; j < daysCount; j++) {
						cell.put(obj.getMetaData().getFields().get(
								j + DATE_START_LOCAL).get("dataIndex"),
								BLANK_STRING);
						countTotalItem(arrListContent.get(i), cell,
								totalNameList, standardTime);
					}
					resultList.add(cell);
					i++;
					continue;
				}
				cs.setTime(s);
				// 以天数作循环
				for (int j = 0; j < daysCount; j++) {
					Date e;
					if (i < recordsCount
							&& !isEmpty(arrListContent.get(i)
									.getAtendanceDate())) {
						e = sdf.parse(arrListContent.get(i).getAtendanceDate());
						cn.setTime(e);
					} else {
						cn.setTimeInMillis((long) 0);
					}
					// 如果有当天的记录
					if (i < recordsCount
							&& cs.equals(cn)
							&& cell.get("empId").equals(
									arrListContent.get(i).getEmpId())) {
						countTotalItem(arrListContent.get(i), cell,
								totalNameList, standardTime);
						cell.put(obj.getMetaData().getFields().get(
								j + DATE_START_LOCAL).get("dataIndex"),
								arrListContent.get(i).getMark());
						i++;
					} else {
						// 没有当天的记录则插入空白
						cell.put(obj.getMetaData().getFields().get(
								j + DATE_START_LOCAL).get("dataIndex"),
								BLANK_STRING);
					}
					cs.add(Calendar.DAY_OF_MONTH, 1);
				}

				// 将该行记录插入整个数据集
				resultList.add(cell);
			}

			// 设置list
			obj.setList(resultList);
			// 设置总数
			obj.setTotalCount((long) recordsCount);
			dataAll.setStore(obj);
			dataAll.setWorkOrRestList(dayFields);
			dataAll.setStrColor(p.getProperty("COLOR"));
			LogUtil.log("EJB:考勤员审核查询结束。", Level.INFO, null);
			// }
			return dataAll;
		} catch (DataChangeException e) {
			LogUtil.log("EJB:考勤员审核查询错误。子部门未审核。", Level.SEVERE, e);
			ctx.setRollbackOnly();
			throw e;
		} catch (ParseException e) {
			LogUtil.log("EJB:考勤员审核查询错误，日期转换失败。", Level.SEVERE, e);
			ctx.setRollbackOnly();
			throw e;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:没有对应的考勤日期。", Level.SEVERE, e);
			ctx.setRollbackOnly();
			throw e;
		} catch (Exception e) {
			LogUtil.log("EJB:考勤员审核查询错误。", Level.SEVERE, e);
			ctx.setRollbackOnly();
			throw e;
		}

	}

	public String getMontDaysByAttendance(String examineDate, String argDeptId,
			String empId, String otherTimeId,String vacationTypeId) {
		
		if (vacationTypeId == null ) {
			return "0";
		} else {
		String sqlString =
		"select nvl(sum(GETHRDAYSBYID(a.other_time_id)), 0)\n" +
		"  from hr_j_workattendancenew a\n" + 
		" where to_char(a.attendance_date, 'yyyy-mm') = '"+examineDate+"'\n" + 
		"   and a.vacation_type_id = '"+vacationTypeId+"'\n" + 
		"   and a.emp_id = '"+empId+"'\n" + 
		"   and a.is_use = 'Y'";
		
		return bll.getSingal(sqlString).toString();
		}
	}

	/**
	 * 根据部门，时间，员工，统计类型去统计合计
	 * modify by fyyang 20100717
	 * @param examineDate
	 * @param argDeptId
	 * @param empId
	 * @param e2
	 * @return
	 */
	private Double getMonthTotalByAttendance(String examineDate,
			String argDeptId, String empId, int e2) {
		String strWhere = "";
		
		if (e2 == 0)
		{
			String sql=
				"select sum(decode(decode(t.year_rest,\n" +
				"                         '1',\n" + 
				"                         1,\n" + 
				"                         decode(t.change_rest,\n" + 
				"                                '1',\n" + 
				"                                1,\n" + 
				"                                decode(t.evection_type,\n" + 
				"                                       '1',\n" + 
				"                                       1,\n" + 
				"                                       decode(t.out_work, '1', 1, 0)))),\n" + 
				"                  1,\n" + 
				"                  0,\n" + 
				"                  (1 + GETHRDAYSBYID(t.overtime_time_id) -\n" + 
				"                  GETHRDAYSBYID(t.absent_time_id) -\n" + 
				"                  GETHRDAYSBYID(t.other_time_id))))\n" + 
				"  from HR_J_WORKATTENDANCENEW t\n" + 
				" where t.emp_id = "+empId+"\n" + 
				"   and to_char(t.attendance_date, 'yyyy-MM') = '"+examineDate+"'\n" + 
				"   and t.is_use = 'Y'";

			Object obj=bll.getSingal(sql);
			if(obj!=null&&!obj.equals(""))
			{
				return Double.parseDouble(obj.toString());
			}else
			{
				return 0d;
			}
		}
		else
		{
		
		if (e2 == 1) {
			strWhere = "and t.CHANGE_REST= '1'";
		}
		else if (e2 == 2) {
			strWhere = "and t.YEAR_REST= '1'";
		}
		else if (e2 == 3) {
			strWhere = "and t.EVECTION_TYPE= '1'";
		}
		else if (e2 == 4) {
			strWhere = "and t.OUT_WORK= '1'";
		}
		else  {
			strWhere = "";
		}
		String sqlString = "select count(1)\n"
			+ "  from hr_j_workattendancenew t\n" + " where\n"
			+ "    to_char(t.attendance_date,'yyyy-mm')= '" + examineDate
			+ "'\n" 
			//"   and t.attendance_dept_id = '" + argDeptId + "'\n"
			+ "   and  t.emp_id = '" + empId + "'\n";
	     sqlString += strWhere;
	     return Double.parseDouble(bll.getSingal(sqlString).toString());
		}

		

	}

	/**
	 * 根据部门id、年份、月份查询部门grid
	 * 
	 * @param strYear
	 * @param strMonth
	 * @param argEnterpriseCode
	 * @param argDeptId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TimeKeeperExamine> gridMainList(String strYear,
			String strMonth, String argEnterpriseCode, String argDeptId)
			throws Exception {
		return null;
	}

	/**
	 * 根据部门id、开始结束日期、查询所有员工考勤登记记录
	 * flag----‘1’ 登记的上报页面查询、及查询页面   ‘approve’ 审批
	 * @param deptIds
	 * @param strStartDate
	 * @param strEndDate
	 * @param argEnterpriseCode
	 * @param p
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TimeKeeperExamineContentNew> allEmpAttendanceList(Long empId,
			String strStartDate, String strEndDate, String argDeptId,
			String argEnterpriseCode, Properties p,String flag,String ... entryIds) throws Exception {
		try {
			List<TimeKeeperExamineContentNew> arrListContent = new ArrayList<TimeKeeperExamineContentNew>();
			String sql = "select * from (select distinct a.dept_id,\n"
					+ "        a.dept_name,\n"
					+ "        b.emp_id,\n"
					+ "        b.chs_name,\n"
					+ "        to_char(t.attendance_date, 'yyyy-mm-dd')  attendance_date,\n"
					+ "        t.attendance_dept_id,\n"
					+ "        c.attendance_dept_name,\n"
					+ "        t.work,\n"
					+ "        t.work_shift_id,\n"
					+ "        t.overtime_type_id,\n"
					+ "        t.overtime_time_id,\n"
					+ "        t.absent_time_id,\n"
					+ "        t.vacation_type_id,\n"
					+ "        t.other_time_id,\n"
					+ "        t.change_rest,\n"
					+ "        t.year_rest,\n"
					+ "        t.evection_type,\n"
					+ "        t.out_work\n"
					+",GETHRVACATIONBYDAY(t.emp_id,to_char(t.attendance_date, 'yyyy-mm-dd')) \n"  //add by fyyang 20100716
					+ "   from hr_j_workattendancenew t\n"
					+ "   left join hr_c_dept a on a.dept_id = t.dept_id\n"
					+ "   left join hr_j_emp_info b on b.emp_id = t.emp_id\n"
					+ "   left join hr_c_attendancedep c on c.attendance_dept_id = t.attendance_dept_id  \n"
					+"    left join HR_J_ATTENDANCE_APPROVE d on d.attendance_dept_id=t.attendance_dept_id and  d.approve_month=to_char(t.attendance_date,'yyyy-MM') \n"
					+ "   where\n" 
					+ "    to_char(t.attendance_date,'yyyy-mm-dd') >= '"
					+ strStartDate + "'\n"
					+ "   and to_char(t.attendance_date,'yyyy-mm-dd') <= '"
					+ strEndDate + "' \n";
					

			 if(argDeptId!=null&&!argDeptId.equals("-1")&&!argDeptId.equals(""))
		     {
		    	 sql+=	"   and t.attendance_dept_id in\n" + 
					"       (select b.attendance_dept_id\n" + 
					"          from HR_C_ATTENDANCEDEP b\n" + 
					"         where (b.attendance_dept_id = "+argDeptId+" or b.top_check_dep_id = "+argDeptId+"))\n";
		     }
			 
			 if(flag!=null&&flag.equals("approve"))
			 {
				 //审批查询时empId为登录人，其他情况为null
				 sql+=" and  d.work_flow_no in ("+entryIds[0]+") \n"+
             //add by fyyang 20100803 加一级部门过滤
				 "and decode(d.send_state,\n" +
				 "              '3',\n" + 
				 "              d.report_by,\n" + 
				 "              (SELECT a.dept_code\n" + 
				 "                 FROM hr_c_dept a\n" + 
				 "                where a.dept_level = 1\n" + 
				 "                  and rownum = 1\n" + 
				 "                START WITH a.dept_id =(select c.dept_id\n" + 
				 "                                          from hr_j_emp_info c\n" + 
				 "                                         where c.emp_code=d.report_by\n" + 
				 "                                           and rownum = 1)\n" + 
				 "               CONNECT BY PRIOR a.pdept_id = a.dept_id)) =\n" + 
				 "       decode(d.send_state,\n" + 
				 "              '3',\n" + 
				 "              d.report_by,\n" + 
				 "              (SELECT a.dept_code\n" + 
				 "                 FROM hr_c_dept a\n" + 
				 "                where a.dept_level = 1\n" + 
				 "                  and rownum = 1\n" + 
				 "                START WITH a.dept_id = (select c.dept_id\n" + 
				 "                                          from hr_j_emp_info c\n" + 
				 "                                         where c.emp_id="+empId+"\n" + 
				 "                                           and rownum = 1)\n" + 
				 "               CONNECT BY PRIOR a.pdept_id = a.dept_id)) \n";

			 }
//		     if(empId!=null&&!empId.equals(""))//add by wpzhu 20100714 delete by fyyang 0802
//		     {
//		    	 sql+="and c.attend_writer_id='"+empId+"'\n";
//		     }
		
//				sql += "  )tt   order by  tt.emp_id, tt.attendance_date ";//update by sychen 20100907
				sql += "  )tt   order by  tt.chs_name,tt.emp_id, tt.attendance_date ";
//				System.out.println("the sql"+sql);
			// 查询一条有参数sql语句
			List listContent = bll.queryByNativeSQL(sql);
			Iterator itContent = listContent.iterator();
			while (itContent.hasNext()) {
				TimeKeeperExamineContentNew tempBean = new TimeKeeperExamineContentNew();
				Object[] data = (Object[]) itContent.next();
				// 人员工号
				if (null != data[0]) {
					tempBean.setDeptId(data[0].toString());
				}
				if (null != data[1]) {
					tempBean.setDeptName(data[1].toString());
				}
				if (null != data[2]) {
					tempBean.setEmpId(data[2].toString());
				}
				if (null != data[3]) {
					tempBean.setEmpName(data[3].toString());
				}
				if (null != data[4]) {
					tempBean.setAtendanceDate(data[4].toString());
				}
				if (null != data[5]) {
					tempBean.setAttendanceDeptId(data[5].toString());
				}
				if (null != data[6]) {
					tempBean.setAttendanceDeptName(data[6].toString());
				}
				if (null != data[7]) {
					tempBean.setWork(data[7].toString());
				}
				if (null != data[8]) {
					tempBean.setWorkShiftId(data[8].toString());
				}
				if (null != data[9]) {
					tempBean.setOvertimeTypeId(data[9].toString());
				}
				if (null != data[10]) {
					tempBean.setOvertimeTimeId(data[10].toString());
				}
				if (null != data[11]) {
					tempBean.setAbsentTimeId(data[11].toString());
				}
				if (null != data[12]) {
					tempBean.setVacationTypeId(data[12].toString());
				}
				if (null != data[13]) {
					tempBean.setOtherTimeId(data[13].toString());
				}
				if (null != data[14]) {
					tempBean.setChangeRest(data[14].toString());
				}
				if (null != data[15]) {
					tempBean.setYearRest(data[15].toString());
				}
				if (null != data[16]) {
					tempBean.setEvectionType(data[16].toString());
				}
				if (null != data[17]) {
					tempBean.setOutWork(data[17].toString());
				}
				// 设置考勤标志
				//tempBean.setMark(totalMark(tempBean, p));
				//modify by fyyang 20100716
				if(null!=data[18])
				{
					tempBean.setMark(data[18].toString());
				}
				else
				{	
					//modify by kzhang 20100906
					//判断是否双休日，双休日默认为”休“，其它日为”出“
					 SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");
					  Calendar calendar = Calendar.getInstance();
					  Date date = new Date();
					  try {
					   date = sdfInput.parse(tempBean.getAtendanceDate());
					  } catch (ParseException e) {
					   e.printStackTrace();
					  }
					calendar.setTime(date);
					int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
					if(dayOfWeek<0)dayOfWeek=0;
					if (dayOfWeek==0||dayOfWeek==6) {
						tempBean.setMark("休");
					}else{
						tempBean.setMark("出");
					}
					//tempBean.setMark("出");
					//----------------------------------------
				}
				arrListContent.add(tempBean);
			}
			LogUtil.log("EJB:根据部门id、开始结束日期、查询所有员工考勤登记记录结束", Level.INFO, null);
			return arrListContent;
		} catch (Exception e) {
			LogUtil.log("EJB:根据部门id、开始结束日期、查询所有员工考勤登记记录错误", Level.SEVERE, null);
			throw e;
		}
	}

	/**
	 * 设置考勤标志
	 * 
	 * @param tempBean
	 * @param p
	 * @return
	 */
	public String totalMark(TimeKeeperExamineContentNew tempBean, Properties p) {
		String returnMark = BLANK_STRING;
		if (isEmpty(tempBean.getWorkShiftId()))
			returnMark += returnMark(tempBean.getWork(), p, "work");
		returnMark += returnMark(tempBean.getChangeRest(), p, "changeRest");
		returnMark += returnMark(tempBean.getYearRest(), p, "yearRest");
		returnMark += returnMark(tempBean.getEvectionType(), p, "evectionType");
		returnMark += returnMark(tempBean.getOutWork(), p, "outWork");
		if (tempBean.getMark() != null) {
			return tempBean.getMark() + returnMark;
		} else {
			return returnMark;
		}
	}

	/**
	 * 获取考勤标志
	 * 
	 * @param value
	 * @param p
	 * @param col
	 * @return
	 */
	public String returnMark(String value, Properties p, String col) {
		if (FLAG_0.equals(value)) {
			return p.getProperty(col + FLAG_0);
		} else {
			return p.getProperty(col + FLAG_1);
		}
	}

	/**
	 * 计算合计项
	 * 
	 * @param statitem
	 *            合计项bean
	 * @param Datas
	 *            数据
	 * @return 计算结果
	 */
	private void countTotalItem(TimeKeeperExamineContentNew content,
			Map<Object, Object> map, List<HrCVacationtype> totalNameList,
			String standardTime) {
		// 合计项
		HrCVacationtype statItem = null;
		// 合计项对应id
		String statItemId = null;
		// 合计项类型
		String statItemType = null;
		// 出勤flg
		String work_flg = FLAG_1;
		for (int index = 0; index < totalNameList.size(); index++) {

			statItem = totalNameList.get(index);
			statItemId = statItem.getVacationTypeId().toString();
			statItemType = statItem.getVacationType();

			String key = TOTAL_DATAINDEX + index;
			boolean isAdd = false;

			// 加班合计
			if (CACodeConstants.STAT_ITEM_TYPE_OVERTIME.equals(statItemType)) {
				// 加班类别id相同
				if (statItemId != null
						&& statItemId.equals(content.getOvertimeTypeId())) {
					isAdd = true;
				}
			}// 运行班合计
			else if (CACodeConstants.STAT_ITEM_TYPE_RUN.equals(statItemType)) {
				if (statItemId != null
						&& statItemId.equals(content.getWorkShiftId())) {
					isAdd = true;
				}
			}// 假别统计
			else if (CACodeConstants.STAT_ITEM_TYPE_VACATIONID
					.equals(statItemType)) {
				if (statItemId != null
						&& statItemId.equals(content.getVacationTypeId())) {
					isAdd = true;
				}
			}// 出勤统计
			else if (CACodeConstants.STAT_ITEM_TYPE_WORK.equals(statItemType)) {
				// 出勤
				if (CACodeConstants.ATTENDANCE_TYPE_WORK.equals(statItemId)
						&& work_flg.equals(content.getWork())) {
					isAdd = true;
				}
				// 换休
				else if (CACodeConstants.ATTENDANCE_TYPE_REST
						.equals(statItemId)
						&& CACodeConstants.REST_FLG_YES.equals(content
								.getChangeRest())) {
					isAdd = true;
				}
				// 年休
				else if (CACodeConstants.ATTENDANCE_TYPE_ABSENT_WORK
						.equals(statItemId)
						&& CACodeConstants.ABSENT_WORK_FLG_YES.equals(content
								.getYearRest())) {
					isAdd = true;
				}
				// 出差
				else if (CACodeConstants.ATTENDANCE_TYPE_LATE_WORK
						.equals(statItemId)
						&& CACodeConstants.LATE_FLG_YES.equals(content
								.getEvectionType())) {
					isAdd = true;
				}
				// 外借
				else if (CACodeConstants.ATTENDANCE_TYPE_LEAVE_EARLY
						.equals(statItemId)
						&& CACodeConstants.LEAVE_EARLY_FLG_YES.equals(content
								.getOutWork())) {
					isAdd = true;
				}
			}
			if (isAdd) {
				AddTotalItemValue(map, key, standardTime);
			}
		}

	}

	/**
	 * 合计项值加1
	 * 
	 * @param map
	 *            map
	 * @param key
	 *            map key
	 * @param statItemUnit
	 *            单位
	 */
	private void AddTotalItemValue(Map<Object, Object> map, Object key,
			String standardTime) {
		double addValue = 1;
		Double value = (Double) map.get(key);
		if (value == null) {
			map.put(key, addValue);
		} else {
			map.put(key, value + addValue);
		}
	}

	/**
	 * 获得考勤合计项信息
	 * modify by fyyang 20100717
	 * '公伤','产假','婚假','丧假','公休假','哺乳假' 不用合计
	 * @param enterpriseCode
	 *            企业编码
	 * @return List<StatItemNameInfo>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HrCVacationtype> getStatItemList(String enterpriseCode)
			throws Exception {
		LogUtil.log("EJB:考勤合计项维护初始化开始。", Level.INFO, null);
		try {
			StringBuilder sbd = new StringBuilder();
			sbd.append("SELECT   * ");
			sbd.append("    FROM HR_C_VACATIONTYPE  T    ");
			sbd.append("    WHERE T.IS_USE = ?  ");
			sbd.append("    AND T.ENTERPRISE_CODE = ?  " );
				//	"  and t.vacation_type not in ('公伤','产假','婚假','丧假','公休假','哺乳假') ");
			// 查询参数数组
			Object[] params = new Object[2];
			params[0] = IS_USE_Y;
			params[1] = enterpriseCode;
			LogUtil.log("EJB:SQL= " + sbd.toString(), Level.INFO, null);
			List<HrCVacationtype> arrList = bll.queryByNativeSQL(
					sbd.toString(), params, HrCVacationtype.class);
			// 按查询结果集设置返回结果
			LogUtil.log("EJB:考勤合计项维护初始化结束。", Level.INFO, null);
			return arrList;
		} catch (Exception e) {
			LogUtil.log("EJB:考勤合计项维护初始化失败。", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * 根据开始日期结束日期生成表头。
	 * 
	 * @param strStartDate
	 * @param strEndDate
	 * @return
	 * @throws Exception
	 */
	private MetaData creatGridHeader(String strStartDate, String strEndDate,
			String enterpriseCode, Properties p) throws Exception {
		try {
			List<Map<String, Object>> fields = new ArrayList<Map<String, Object>>();
			dayFields.clear();
			totalNameList.clear();
			String root = "list";
			String totalProperty = "totalCount";
			MetaData metas = new MetaData();
			String[] days = { BLANK_STRING, "一", "二", "三", "四", "五", "六", "七",
					"八", "九", "十", "十一", "十二", "十三", "十四", "十五", "十六", "十七",
					"十八", "十九", "二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六",
					"二十七", "二十八", "二十九", "三十", "三十一" };
			String[] weeks = { BLANK_STRING, "周日 ", "周一 ", "周二 ", "周三 ", "周四 ",
					"周五 ", "周六 " };
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			Calendar cs = Calendar.getInstance();
			Calendar ce = Calendar.getInstance();
			Calendar ch = Calendar.getInstance();
			Date s = sdf.parse(strStartDate);
			Date e = sdf.parse(strEndDate);
			cs.setTime(s);
			ce.setTime(e);
			ce.add(Calendar.DAY_OF_MONTH, 1);
			String headerName = DATE_DATAINDEX;
			String headerCountName = TOTAL_DATAINDEX;
			String heaedrTotaleName = TOTAL_TATAINDEX1;
			int i = 0;
			int j = 0;
			Map<String, Object> tempLine = new HashMap<String, Object>();
			tempLine.put("name", "lineNum");
			fields.add(tempLine);
			Map<String, Object> tempEmpName = new HashMap<String, Object>();
			tempEmpName.put("name", "empName");
			tempEmpName.put("dataIndex", "empName");
			tempEmpName.put("header",
					"<span style='line-height:2.6em'>员工姓名</span>");
			tempEmpName.put("align", "left");
			tempEmpName.put("sortable", "true");
			tempEmpName.put("width", WIDTH_EMP_INFO);
			tempEmpName.put("locked", true);//add bysychen 20100730
			fields.add(tempEmpName);
			Map<String, Object> tempEmpId = new HashMap<String, Object>();
			tempEmpId.put("name", "empId");
			tempEmpId.put("dataIndex", "empId");
			tempEmpId.put("hidden", "true");
			fields.add(tempEmpId);
			Map<String, Object> tempEmpDept = new HashMap<String, Object>();
			tempEmpDept.put("name", "deptName");
			tempEmpDept.put("dataIndex", "deptName");
			tempEmpDept.put("header",
					"<span style='line-height:2.6em'>所属部门</span>");
			tempEmpDept.put("sortable", "true");
			tempEmpDept.put("align", "left");
			tempEmpDept.put("width", WIDTH_EMP_INFO);
			tempEmpDept.put("locked", true);//add bysychen 20100730
			fields.add(tempEmpDept);
			Map<String, Object> tempEmpDeptId = new HashMap<String, Object>();
			tempEmpDeptId.put("name", "deptId");
			tempEmpDeptId.put("dataIndex", "deptId");
			tempEmpDeptId.put("hidden", "true");
			fields.add(tempEmpDeptId);
			Map<String, Object> tempEmpCheckDeptId = new HashMap<String, Object>();
			tempEmpCheckDeptId.put("name", "attendanceDeptId");
			tempEmpCheckDeptId.put("dataIndex", "attendanceDeptId");
			tempEmpCheckDeptId.put("hidden", "true");
			fields.add(tempEmpCheckDeptId);
			List<HrCHoliday> holidaysList = getHolidays(strStartDate,
					strEndDate, enterpriseCode);
			do {
				Map<String, Object> tempDayInfo = new HashMap<String, Object>();
				tempDayInfo.put("day", cs.get(Calendar.DAY_OF_MONTH));
				tempDayInfo.put("week", cs.get(Calendar.DAY_OF_WEEK));
				if (j < holidaysList.size())
					ch.setTime(holidaysList.get(j).getHolidayDate());
				String workOrRestFlag = BLANK_STRING;
				if (cs.get(Calendar.DAY_OF_WEEK) == 1
						|| cs.get(Calendar.DAY_OF_WEEK) == 7) {
					workOrRestFlag = FLAG_1;
					if (ch.equals(cs)
							&& FLAG_2.equals(holidaysList.get(j)
									.getHolidayType())) {
						workOrRestFlag = FLAG_2;
						j++;
					}
				} else {
					workOrRestFlag = FLAG_2;
					if (ch.equals(cs)
							&& FLAG_1.equals(holidaysList.get(j)
									.getHolidayType())) {
						workOrRestFlag = FLAG_1;
						j++;
					}
				}
				tempDayInfo.put("workOrRest", workOrRestFlag);
				dayFields.add(tempDayInfo);
				Map<String, Object> tempCm = new HashMap<String, Object>();
				tempCm.put("name", headerName + i);
				tempCm.put("dataIndex", headerName + i);
				tempCm.put("header",
						workOrRestFlag.equals(FLAG_1) ? "<font color='"
								+ p.getProperty("COLOR") + "'>"
								+ days[cs.get(Calendar.DAY_OF_MONTH)]
								+ "<br />"
								+ weeks[cs.get(Calendar.DAY_OF_WEEK)]
								+ "</font>" : days[cs
								.get(Calendar.DAY_OF_MONTH)]
								+ "<br />"
								+ weeks[cs.get(Calendar.DAY_OF_WEEK)]);
				tempCm.put("align", "Center");
				tempCm.put("width", WIDTH_DATE);
				tempCm.put("renderer", "setColor");
				fields.add(tempCm);
				cs.add(Calendar.DAY_OF_MONTH, 1);
				i++;
			} while (cs.before(ce));
			// 获取合计项列名信息
			totalNameList = getStatItemList(enterpriseCode);
			for (int index = 0; index < totalNameList.size(); index++) {
				String formatTime = BLANK_STRING;
				String strTotalCountName = BLANK_STRING;
				formatTime = "setTotalCountNumber2";
				if (isEmpty(totalNameList.get(index).getVacationType())) {
					strTotalCountName = totalNameList.get(index)
							.getVacationType();
				} else {
					strTotalCountName = totalNameList.get(index)
							.getVacationType();
				}
				Map<String, Object> tempCm = new HashMap<String, Object>();
				tempCm.put("name", headerCountName + index);
				tempCm.put("dataIndex", headerCountName + index);
				tempCm.put("header", "<span style='line-height:2.6em'>"
						+ strTotalCountName + "</span>");
				tempCm.put("align", "right");
				tempCm.put("renderer", formatTime);
				tempCm.put("sortable", "true");
				tempCm.put("width", WIDTH_TOTAL);
				fields.add(tempCm);
			}

			for (int e1 = 0; e1 < 5; e1++) {
				Map<String, Object> tempCm = new HashMap<String, Object>();
				String headerName1 = "";
				if (e1 == 0) {
					headerName1 = "出勤";
				}
				if (e1 == 1) {
					headerName1 = "换休";
				}
				if (e1 == 2) {
					headerName1 = "年休";
				}
				if (e1 == 3) {
					headerName1 = "出差";
				}
				if (e1 == 4) {
					headerName1 = "外借";
				}
				tempCm.put("name", heaedrTotaleName + e1);
				tempCm.put("dataIndex", heaedrTotaleName + e1);
				tempCm.put("header", "<span style='line-height:2.6em'>"
						+ headerName1 + "</span>");
				tempCm.put("align", "right");
				tempCm.put("sortable", "true");
				tempCm.put("width", WIDTH_TOTAL);
				fields.add(tempCm);
			}

			metas.setFields(fields);
			metas.setId("emp");
			metas.setRoot(root);
			metas.setTotalProperty(totalProperty);
			return metas;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 根据开始日期结束日期查询假日
	 * 
	 * @param strStartDate
	 * @param strEndDate
	 * @param strEnterpriseCode
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HrCHoliday> getHolidays(String strStartDate, String strEndDate,
			String strEnterpriseCode) throws Exception {
		try {
			LogUtil.log("EJB:根据开始日期结束日期查询假日开始。", Level.INFO, null);
			// 查询sql
			StringBuffer sbd = new StringBuffer();
			// SELECT文
			sbd.append("SELECT *");
			// FROM文
			sbd.append("FROM HR_C_HOLIDAY A ");
			sbd.append("WHERE ");
			sbd.append("A.ENTERPRISE_CODE = ? ");
			sbd.append("AND A.IS_USE = ? ");
			sbd.append("AND TO_CHAR(A.HOLIDAY_DATE,'yyyy-mm-dd') >= ? ");
			sbd.append("AND TO_CHAR(A.HOLIDAY_DATE,'yyyy-mm-dd') <= ? ");
			sbd.append("ORDER BY A.HOLIDAY_DATE ");
			// 查询参数数组
			Object[] params = new Object[4];
			int i = 0;
			params[i++] = strEnterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = strStartDate;
			params[i++] = strEndDate;
			List<HrCHoliday> arrList = bll.queryByNativeSQL(sbd.toString(),
					params, HrCHoliday.class);
			LogUtil.log("EJB:根据开始日期结束日期查询假日结束。", Level.INFO, null);
			return arrList;
		} catch (Exception e) {
			LogUtil.log("EJB:根据开始日期结束日期查询假日错误。", Level.SEVERE, null);
			throw e;
		}
	}

	/**
	 * 通过上级审核部门取得考勤部门
	 * 
	 * @param pid
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject getDeptsByTopDeptid(String enterpriseCode, Long loginEmp) {
		return null;
	}

	/**
	 * 查询职工考勤记录
	 * 
	 * @param empId
	 * @param enterpriseCode
	 * @param strStartDate
	 * @param strEndDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TimeKeeperExamineStandardTime getEmpAttendance(String empId,
			String enterpriseCode, String strStartDate, String strEndDate,
			String checkFlag, List<Map<String, Object>> workOrRestList,
			String attendanceDeptId, String deptId) throws Exception {
		return null;
	}

	/**
	 * 判断员工是否为行政班
	 * 
	 * @param empId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean checkWorkKind(String empId, String strAttendanceDate) {
		return false;
	}

	/**
	 * 查询该员工的请假信息
	 * 
	 * @param empId
	 * @param enterpriseCode
	 * @param strAttendanceDate
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TimeKeeperExamineEmpHoliday> getEmpHoliday(String empId,
			String enterpriseCode, String strAttendanceDate) throws Exception {
		return null;
	}

	/**
	 * String 转 Calendar
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public Calendar stringToCalendar(String strDate) throws ParseException {
		Calendar cn = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Date s = sdf.parse(strDate);
		cn.setTime(s);
		return cn;
	}

	/**
	 * 返回同月两个日期间的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public int dateFromTo(String startDate, String endDate)
			throws ParseException {
		return stringToCalendar(endDate).get(Calendar.DAY_OF_MONTH)
				- stringToCalendar(startDate).get(Calendar.DAY_OF_MONTH) + 1;
	}

	/**
	 * 比较两个日期的先后，如果date2等于date1，则返回 0 值；如果date1 的时间在date2之前，则返回小于 0
	 * 的值；如果date1在date2之后，则返回大于 0 的值。
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public int dateCompare(String date1, String date2) throws ParseException {
		return stringToCalendar(date1).compareTo(stringToCalendar(date2));
	}

	/**
	 * * 比较两个日期的先后，如果date2等于date1，则返回 0 值；如果date1 的时间在date2之前，则返回小于 0
	 * 的值；如果date1在date2之后，则返回大于 0 的值。
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public int dateCompare(Calendar date1, Date date2) throws ParseException {
		Calendar ct = Calendar.getInstance();
		ct.setTime(date2);
		return date1.compareTo(ct);
	}

	/**
	 * * 比较两个日期的先后，如果date2等于date1，则返回 0 值；如果date1 的时间在date2之前，则返回小于 0
	 * 的值；如果date1在date2之后，则返回大于 0 的值。
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public int dateCompare(String date1, Date date2) throws ParseException {
		Calendar ct = Calendar.getInstance();
		ct.setTime(date2);
		return stringToCalendar(date1).compareTo(ct);
	}

	/**
	 * * 比较两个日期的先后，如果date2等于date1，则返回 0 值；如果date1 的时间在date2之前，则返回小于 0
	 * 的值；如果date1在date2之后，则返回大于 0 的值。
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public int dateCompare(Calendar date1, String date2) throws ParseException {
		return date1.compareTo(stringToCalendar(date2));
	}

	/**
	 * 批量保存员工考勤记录
	 * 
	 * @param records
	 * @param enterpriseCode
	 * @param workerCode
	 * @param empId
	 * @throws DataChangeException
	 * @throws DataFormatException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveEmpAttendance(ArrayList<Map<String, Object>> records,
			String enterpriseCode, String workerCode, String empId)
			throws DataChangeException, DataFormatException {
	}

	/**
	 * 根据日期日期字符串和形式返回日期
	 * 
	 * @param argDateStr
	 *            日期字符串
	 * @param argFormat
	 *            日期形式字符串
	 * @return 日期
	 */
	private Date formatStringToDate(String argDateStr, String argFormat)
			throws DataFormatException {
		if (argDateStr == null || argDateStr.trim().length() < 1) {
			return null;
		}
		argDateStr = argDateStr.replace('T', ' ');
		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回日期
		Date dtresult = null;

		sdfFrom = new SimpleDateFormat(argFormat);
		// 格式化日期
		try {
			dtresult = sdfFrom.parse(argDateStr);
		} catch (ParseException e) {
			throw new DataFormatException();
		}
		return dtresult;
	}

	/**
	 * 判断值是否为空
	 * 
	 * @param strValue
	 *            值
	 * @return 是否为空
	 */
	public boolean isEmpty(Object objValue) {
		return objValue == null || BLANK_STRING.equals(objValue.toString());
	}

	/**
	 * 将对象转化为字符
	 * 
	 * @param objValue
	 *            对象
	 * @return 字符
	 */
	public String objectToString(Object objValue) {
		if (objValue == null) {
			return BLANK_STRING;
		} else {
			return objValue.toString();
		}
	}

	/**
	 * 撤销前回审核
	 * 
	 * @param attendanceDeptId
	 * @param checkYear
	 * @param checkMonth
	 * @throws DataChangeException
	 * @throws DataFormatException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void cancelLastCheck(ArrayList<Map<String, Object>> arrlist,
			String enterpriseCode, String workerCode)
			throws DataChangeException, DataFormatException {
	}

	public void saveTotalCountByEmp(List<String> deptIds, String strStartDate,
			String strEndDate, String argEnterpriseCode, Properties p,
			String workerCode, String attendanceDeptId, Long checkEmpId)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
