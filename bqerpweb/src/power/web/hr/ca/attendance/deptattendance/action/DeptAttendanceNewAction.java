package power.web.hr.ca.attendance.deptattendance.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.DeptAttendanceFacadeRemote;
import power.ejb.hr.ca.HrCAttendancedepFacadeRemote;
import power.ejb.hr.ca.HrJAttendanceApprove;
import power.ejb.hr.ca.HrJAttendanceApproveFacadeRemote;
import power.ejb.hr.ca.HrJWorkattendancenew;
import power.ejb.hr.ca.HrJWorkattendancenewFacadeRemote;
import power.ejb.hr.ca.HrJWorkattendancenewId;
import power.ejb.manage.plan.BpJPlanJobCompleteMain;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.PostMessage;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

/**
 * 部门考勤登记Action
 * 
 * @author jincong
 * @version 1.0
 */
public class DeptAttendanceNewAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// 常量
	/** 数字: 0 */
	private static final int INT_0 = 0;
	/** 数字: 4 */
	private static final int INT_4 = 4;
	/** 数字: 5 */
	private static final int INT_5 = 5;
	/** 数字: 7 */
	private static final int INT_7 = 7;
	/** 标识: 0 */
	private static final String FLAG_0 = "0";
	/** 标识: 1 */
	private static final String FLAG_1 = "1";
	/** 返回结果 */
	private static final String RESULT_BEGIN = "{success:true,msg:";
	/** 返回结果 */
	private static final String RESULT_END = "}";
	/** 日期格式 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	/** 时间格式 */
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/** 部门考勤登记Remote */
	private DeptAttendanceFacadeRemote remote;
	/** 考勤日期 */
	private String attendanceDate;
	/** 考勤部门 */
	private String attendanceDeptId;
	/** 上班休息标识 */
	private String workRestFlag = "";
	/** 考勤星期 */
	private int attendanceWeekday;
	/** 审核标识 */
	private boolean flag;
	/** 部门考勤记录 */
	private String records;

	private HrJWorkattendancenewFacadeRemote newRemote;

	private HrCAttendancedepFacadeRemote newDeptRemote;
	private HrJAttendanceApproveFacadeRemote approveRemote;

	/**
	 * 构造函数
	 */
	public DeptAttendanceNewAction() {
		// 取得接口
		remote = (DeptAttendanceFacadeRemote) factory
				.getFacadeRemote("DeptAttendanceFacade");
		newRemote = (HrJWorkattendancenewFacadeRemote) factory
				.getFacadeRemote("HrJWorkattendancenewFacade");
		newDeptRemote = (HrCAttendancedepFacadeRemote) factory
				.getFacadeRemote("HrCAttendancedepFacade");
		approveRemote=(HrJAttendanceApproveFacadeRemote)factory.getFacadeRemote("HrJAttendanceApproveFacade");
	}

	/**
	 * 查询所有考勤部门
	 */
	public void getNewAttendanceDeptForRegister() {
		try {
			LogUtil.log("Action:考勤部门查询开始。", Level.INFO, null);
			// 查询
			PageObject object = newDeptRemote.getAttendanceDeptForRegister(
					employee.getWorkerId().toString(), employee
							.getEnterpriseCode());
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:考勤部门查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:考勤部门查询失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 判断是否已经审核
	 */
	public void getNewApprovedForRegister() {
		// 查询
		boolean flag = newRemote.getApprovedForRegister(attendanceDate
				.substring(INT_0, INT_7), attendanceDeptId);
		// 输出
		write(RESULT_BEGIN + flag + RESULT_END);
		LogUtil.log("Action:判断是否已经审核结束。", Level.INFO, null);
	}

	/**
	 * 部门审核明细部查询
	 */
	@SuppressWarnings("unchecked")
	public void getDetailInfoForRegisterNew() {
		try {
			LogUtil.log("Action:部门审核明细部查询开始。", Level.INFO, null);
			PageObject object = new PageObject();
			// 已审核
//			if (flag) {
//				object = remote.getApprovedDetailInfoForRegister(
//						attendanceDate, attendanceDeptId, employee
//								.getEnterpriseCode());
//			} else {
				// 查询
				object = newRemote.getDetailInfoForRegister(attendanceDate,
						attendanceDate.substring(INT_0, INT_4), attendanceDate
								.substring(INT_5, INT_7), attendanceDeptId,
						workRestFlag, employee.getEnterpriseCode());
//			}
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:部门审核明细部查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:部门审核明细部查询失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 查询符合条件的人员个数
	 */
	public void getNewEmpExistForRegister() {
		LogUtil.log("Action:查询符合条件的人员个数开始。", Level.INFO, null);
		// 查询
		int count = remote.getEmpCountForRegister(attendanceDate,
				attendanceDeptId, employee.getEnterpriseCode());
		// 是否已经审核标识
		boolean flag = false;
		// 存在这样的人员
		if (count > 0) {
			flag = true;
		}
		// 输出
		write(RESULT_BEGIN + flag + RESULT_END);
		LogUtil.log("Action:查询符合条件的人员个数结束。", Level.INFO, null);
	}

	/**
	 * 保存部门考勤记录
	 */
	@SuppressWarnings("unchecked")
	public void saveNewDeptAttendanceForRegister() throws Exception {
		UserTransaction tx = null;
		try {
			LogUtil.log("Action:保存部门考勤记录开始。", Level.INFO, null);

			tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			// 事务开始
			tx.begin();

			// 将记录字符串转化为数组
			ArrayList arrayList = (ArrayList) JSONUtil.deserialize(records);
			// 循环保存
			for (int i = 0; i < arrayList.size(); i++) {
				Map map = (Map) arrayList.get(i);
				HrJWorkattendancenew bean = new HrJWorkattendancenew();
				// 更新一条记录
				if (FLAG_1.equals(objectToString(map.get("existFlag")))) {
					HrJWorkattendancenewId workAttendanceId = new HrJWorkattendancenewId();
					// 设置ID
					workAttendanceId.setEmpId(Long.parseLong(objectToString(map
							.get("empId"))));
					// 设置考勤日期
					workAttendanceId.setAttendanceDate(formatStringToDate(
							attendanceDate, DATE_FORMAT));
					// 根据人员ID和考勤日期查询考勤记录
					bean = newRemote.findById(workAttendanceId, employee
							.getEnterpriseCode());
					// 结果为空
					if (bean == null) {
						throw new DataChangeException(Constants.BLANK_STRING);
					}
				}

				// //部门ID
				// if(!Constants.BLANK_STRING.equals(
				// objectToString(map.get("deptId")))){
				// bean.setDeptId(Long.parseLong(objectToString(map.get("deptId"))));
				// }else{
				// bean.setDeptId(null);
				// }
				// //考勤部门ID
				// if(!Constants.BLANK_STRING.equals(
				// objectToString(map.get("attendanceDeptId")))){
				// bean.setAttendanceDeptId(Long.parseLong(objectToString(attendanceDeptId)));
				//					
				// }else{
				// bean.setAttendanceDeptId(null);
				//					
				// }
				// 出勤
				if (Constants.BLANK_STRING.equals(objectToString(map
						.get("work")))) {
					bean.setWork(FLAG_0);
				} else {
					bean.setWork(objectToString(map.get("work")));
				}
				// 运行班类别ID
				if (!Constants.BLANK_STRING.equals(objectToString(map
						.get("workShiftId")))) {
					bean.setWorkShiftId(Long.parseLong(objectToString(map
							.get("workShiftId"))));
				} else {
					bean.setWorkShiftId(null);
				}
				// 加班类别ID
				if (!Constants.BLANK_STRING.equals(objectToString(map
						.get("overtimeTypeId")))) {
					bean.setOvertimeTypeId(Long.parseLong(objectToString(map
							.get("overtimeTypeId"))));
				} else {
					bean.setOvertimeTypeId(null);
				}

				// 加班时间id
				if (!objectToString(map.get("overtimeTimeId")).equals(""))
					bean.setOvertimeTimeId(Long.parseLong(objectToString(map
							.get("overtimeTimeId"))));

				// 旷工时间ID
				if (!objectToString(map.get("absentTimeId")).equals(""))
					bean.setAbsentTimeId(Long.parseLong(objectToString(map
							.get("absentTimeId"))));

				// 假别ID(请假事项)
				if (!Constants.BLANK_STRING.equals(objectToString(map
						.get("vacationTypeId")))) {
					bean.setVacationTypeId(Long.parseLong(objectToString(map
							.get("vacationTypeId"))));
				} else {
					bean.setVacationTypeId(null);
				}

				// 请假时间ID
				if (!objectToString(map.get("otherTimeId")).equals(""))
					bean.setOtherTimeId(Long.parseLong(objectToString(map
							.get("otherTimeId"))));

				// 换休
				if (Constants.BLANK_STRING.equals(objectToString(map
						.get("changeRest")))) {
					bean.setChangeRest(FLAG_0);
				} else {
					bean.setChangeRest(objectToString(map.get("changeRest")));
				}
				// 年休
				if (Constants.BLANK_STRING.equals(objectToString(map
						.get("yearRest")))) {
					bean.setYearRest(FLAG_0);
				} else {
					bean.setYearRest(objectToString(map.get("yearRest")));
				}
				// 出差
				if (Constants.BLANK_STRING.equals(objectToString(map
						.get("evectionType")))) {
					bean.setEvectionType(FLAG_0);
				} else {
					bean
							.setEvectionType(objectToString(map
									.get("evectionType")));
				}
				// 外借
				if (Constants.BLANK_STRING.equals(objectToString(map
						.get("outWork")))) {
					bean.setOutWork(FLAG_0);
				} else {
					bean.setOutWork(objectToString(map.get("outWork")));
				}

				// 设置备注
				bean.setMemo(objectToString(map.get("memo")));
				// 设置上次修改人
				bean.setLastModifiyBy(employee.getWorkerCode());
				Date date = new Date();
				bean.setLastModifiyDate(date);
				// 设置是否使用
				bean.setIsUse(Constants.IS_USE_Y);
				// 设置企业编码
				bean.setEnterpriseCode(employee.getEnterpriseCode());
				// 新增
				if (FLAG_0.equals(objectToString(map.get("existFlag")))) {
					HrJWorkattendancenewId workAttendanceId = new HrJWorkattendancenewId();
					// 设置人员ID
					workAttendanceId.setEmpId(Long.parseLong(objectToString(map
							.get("empId"))));
					// 设置考勤日期
					workAttendanceId.setAttendanceDate(formatStringToDate(
							attendanceDate, DATE_FORMAT));
					// 设置ID
					bean.setId(workAttendanceId);
					// 设置部门ID
					if (!Constants.BLANK_STRING.equals(objectToString(map
							.get("deptId")))) {
						bean.setDeptId(Long.parseLong(objectToString(map
								.get("deptId"))));
					} else {
						bean.setDeptId(null);
					}
					// 设置考勤部门
					bean.setAttendanceDeptId(Long.parseLong(attendanceDeptId));
					// 设置记录人
					bean.setInsertby(employee.getWorkerCode());
					// 设置记录日期
					bean.setInsertdate(new Date());
					// 设置上次修改日期
					bean.setLastModifiyDate(new Date());
					// 保存
					newRemote.save(bean);
					// 更新
				} else if (FLAG_1.equals(objectToString(map.get("existFlag")))) {
					// 设置上次修改时间
					bean.setLastModifiyDate(formatStringToDate(
							objectToString(map.get("lastModifiyDate")),
							TIME_FORMAT));
					// 更新
					newRemote.update(bean);
				}
			}

			// 提交事务
			tx.commit();
			// 返回结果
			write(RESULT_BEGIN + true + RESULT_END);
			LogUtil.log("Action:保存部门考勤记录结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:保存部门考勤记录失败。", Level.SEVERE, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			write(Constants.DATA_FAILURE);
			throw e;
		} catch (RuntimeException e) {
			LogUtil.log("Action:保存部门考勤记录失败。", Level.SEVERE, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			write(Constants.SQL_FAILURE);
			throw e;
		} catch (DataChangeException e) {
			LogUtil.log("Action:保存部门考勤记录失败。", Level.SEVERE, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			write(Constants.DATA_USING);
			throw e;
		} catch (DataFormatException e) {
			LogUtil.log("Action:保存部门考勤记录失败。", Level.SEVERE, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			write(Constants.DATA_FAILURE);
			throw e;
		}
	}

	// 考勤部门上报 add by fyyang 20100709

	public void reportAttendanceDept() {
		String attendanceDeptId = request.getParameter("attendanceDeptId");
		String month = request.getParameter("month");
		String nextRoles = request.getParameter("nextRoles");
		String remark = request.getParameter("approveText");
		String actionId = request.getParameter("actionId");
		approveRemote.reportAttendanceDept(Long.parseLong(attendanceDeptId),
				employee.getWorkerCode(), Long.parseLong(actionId), nextRoles,
				remark, month,employee.getEnterpriseCode());
		write("{success: true,msg:'上报成功！'}");

	}
	
	//// 考勤部门审批 add by fyyang 20100709
	public void approveAttendance()
	{

		String strMonth = request.getParameter("strMonth");
		String nextRole = request.getParameter("nextRoles");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String workerCode = request.getParameter("workerCode");
		String eventIdentify = request.getParameter("eventIdentify");
//		//add by sychen 20100730  delete by fyyang 20100803
//		String nextRoles ="";
//        if(actionId!=null&&actionId.equals("45")){
//        	 nextRoles  = approveRemote.getnextRolesList(nextRole.substring(0, nextRole.length()-1),employee.getDeptId());
//        	 nextRoles  =nextRoles.substring(0, nextRoles.length()-1);
//     	}
//		else {
//			 nextRoles  =nextRole;
//		}	
//
//		//add by sychen 20100730 end 
		
		
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
  		String entryIds = workflowService.getAvailableWorkflow(new String[] {
  				"bqWorkAttendance"}, employee.getWorkerCode());
  		
		String msg=approveRemote.approveAttendance(employee.getDeptId(),strMonth, approveText, workerCode, Long.parseLong(actionId), nextRole, eventIdentify,entryIds);
		write("{success: true,msg:'"+msg+"'}");
	}
	
	public void getAttendanceInfo() throws JSONException
	{
		String strMonth = request.getParameter("strMonth");
		String strDeptId=request.getParameter("deptId");
		Long deptId=null;
		if(strDeptId!=null&&!strDeptId.equals("")&&!strDeptId.equals("null"))
		{
			deptId=Long.parseLong(strDeptId);
		}
		
		List<HrJAttendanceApprove> list=approveRemote.getAttendanceApprove(strMonth, deptId);
		HrJAttendanceApprove model=new HrJAttendanceApprove();
		if(list!=null&&list.size()>0)
		{
			model=list.get(0);
		}
		write(JSONUtil.serialize(model));
	}
	
	public void getAttendanceApproveInfo() throws JSONException
	{
		String strMonth = request.getParameter("strMonth");
		
		  WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
  		String entryIds = workflowService.getAvailableWorkflow(new String[] {
  				"bqWorkAttendance"}, employee.getWorkerCode());
		
		List<HrJAttendanceApprove> list=approveRemote.getAttendanceListForApprove(strMonth, entryIds, employee.getDeptId());
		HrJAttendanceApprove model=new HrJAttendanceApprove();
		if(list!=null&&list.size()>0)
		{
			model=list.get(0);
		}
		 write(JSONUtil.serialize(model));
	}

	/**
	 * 判断值是否为空
	 * 
	 * @param strValue
	 *            值
	 * @return 是否为空
	 */
	public boolean isEmpty(String strValue) {
		return Constants.BLANK_STRING.equals(strValue) || strValue == null;
	}

	/**
	 * 判断值是否为空
	 * 
	 * @param strValue
	 *            值
	 * @return 是否为空
	 */
	public boolean isEmpty(Object objValue) {
		return Constants.BLANK_STRING.equals(objValue.toString())
				|| objValue == null;
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
			return Constants.BLANK_STRING;
		} else {
			return objValue.toString();
		}
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
	 * 取得考勤日期
	 * 
	 * @return 考勤日期
	 */
	public String getAttendanceDate() {
		return attendanceDate;
	}

	/**
	 * 设置考勤日期
	 * 
	 * @param 考勤日期
	 */
	public void setAttendanceDate(String attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	/**
	 * 取得考勤部门
	 * 
	 * @return 考勤部门
	 */
	public String getAttendanceDeptId() {
		return attendanceDeptId;
	}

	/**
	 * 设置考勤部门
	 * 
	 * @param 考勤部门
	 */
	public void setAttendanceDeptId(String attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}

	/**
	 * 取得上班休息标识
	 * 
	 * @return 上班休息标识
	 */
	public String getWorkRestFlag() {
		return workRestFlag;
	}

	/**
	 * 设置上班休息标识
	 * 
	 * @param 上班休息标识
	 */
	public void setWorkRestFlag(String workRestFlag) {
		this.workRestFlag = workRestFlag;
	}

	/**
	 * 取得审核标识
	 * 
	 * @return 审核标识
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * 设置审核标识
	 * 
	 * @param 审核标识
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * 取得考勤记录
	 * 
	 * @return 考勤记录
	 */
	public String getRecords() {
		return records;
	}

	/**
	 * 设置考勤记录
	 * 
	 * @param 考勤记录
	 */
	public void setRecords(String records) {
		this.records = records;
	}

	/**
	 * 取得考勤星期
	 * 
	 * @return 考勤星期
	 */
	public int getAttendanceWeekday() {
		return attendanceWeekday;
	}

	/**
	 * 设置考勤星期
	 * 
	 * @param 考勤星期
	 */
	public void setAttendanceWeekday(int attendanceWeekday) {
		this.attendanceWeekday = attendanceWeekday;
	}
}
