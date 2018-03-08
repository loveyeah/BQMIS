/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
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
import power.ejb.hr.ca.CACodeConstants;
import power.ejb.hr.ca.DeptAttendance;
import power.ejb.hr.ca.DeptAttendanceFacadeRemote;
import power.ejb.hr.ca.HrCHoliday;
import power.ejb.hr.ca.HrJWorkattendance;
import power.ejb.hr.ca.HrJWorkattendanceFacadeRemote;
import power.ejb.hr.ca.HrJWorkattendanceId;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 部门考勤登记Action
 * 
 * @author jincong
 * @version 1.0
 */
public class DeptAttendanceAction extends AbstractAction {

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
	/** 数字: 6 */
	private static final int INT_6 = 6;
	/** 数字: 7 */
	private static final int INT_7 = 7;
	/** 上班休息标识: 1 */
	private static final String WORK_REST_FLAG_1 = "1";
	/** 上班休息标识: 2 */
	private static final String WORK_REST_FLAG_2 = "2";
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
	/** 考勤登记Remote */
	private HrJWorkattendanceFacadeRemote workAttendanceRemote;
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

	/**
	 * 构造函数
	 */
	public DeptAttendanceAction() {
		// 取得接口
		remote = (DeptAttendanceFacadeRemote) factory
				.getFacadeRemote("DeptAttendanceFacade");
		workAttendanceRemote = (HrJWorkattendanceFacadeRemote) factory
			.getFacadeRemote("HrJWorkattendanceFacade");
	}

	/**
	 * 查询所有考勤部门
	 */
	public void getAttendanceDeptForRegister() {
		try {
			LogUtil.log("Action:考勤部门查询开始。", Level.INFO, null);
			// 查询
			PageObject object = remote.getAttendanceDeptForRegister(employee
					.getWorkerId().toString(), employee.getEnterpriseCode());
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
	public void getApprovedForRegister() {
		LogUtil.log("Action:判断是否已经审核开始。", Level.INFO, null);
		// 查询
		PageObject object = remote.getApprovedForRegister(
				attendanceDate.substring(INT_0, INT_4),
				attendanceDate.substring(INT_5, INT_7),
				attendanceDeptId, employee.getEnterpriseCode());
		// 是否已经审核标识
		boolean flag = isApproved(object);
		// 输出
		write(RESULT_BEGIN  + flag + RESULT_END);
		LogUtil.log("Action:判断是否已经审核结束。", Level.INFO, null);
	}
	
	/**
	 * 判断是否已经审核
	 * 
	 * @param object 查询结果
	 * @return 是否已经审核
	 */
	@SuppressWarnings("unchecked")
	public boolean isApproved(PageObject object) {
		// 审核部门ID
		String checkDeptId = "";
		// 检索结果的List
		List<DeptAttendance> list = object.getList();
		// 检索结果的审核部门 = 画面.考勤部门的记录存在
		if (list.size() > 0) {
			DeptAttendance deptAttendance = list.get(0);
			// 审核人1或审核人2不为空的场合
			if (!isEmpty(deptAttendance.getDeptCharge1())
					|| !isEmpty(deptAttendance.getDeptCharge2())) {
				// 已审核
				return true;
			// 考勤类别 = ‘1’（考勤部门）或者 ‘3’（代考勤部门）
			} else if (CACodeConstants.ATTEND_DEP_TYPE_ATTEND
					.equals(deptAttendance.getAttendDeptType())
					|| CACodeConstants.ATTEND_DEP_TYPE_PROXY_ATTEND
							.equals(deptAttendance.getAttendDeptType())) {
				// 审核部门ID = 选择的考勤部门.上级审核部门
				checkDeptId = deptAttendance.getTopCheckDeptId();
				// 循环
				for (int i = 1; i < list.size(); i++) {
					DeptAttendance infoTop = list.get(i);
					// 检索结果的审核部门 = 审核部门ID
					if (checkDeptId.equals(infoTop.getAttendanceDept())) {
						// 考勤类别 = ‘1’ 或者 考勤类别 = ‘3’
						if (CACodeConstants.ATTEND_DEP_TYPE_ATTEND
								.equals(infoTop.getAttendDeptType())
								|| CACodeConstants.ATTEND_DEP_TYPE_PROXY_ATTEND
										.equals(infoTop.getAttendDeptType())) {
							// 审核部门ID = 上级审核部门
							checkDeptId = infoTop.getTopCheckDeptId();
							continue;
						// 考勤类别 = ‘2’
						} else if (CACodeConstants.ATTEND_DEP_TYPE_ATTEND_CHECKE
								.equals(infoTop.getAttendDeptType())) {
							// 审核人1或审核人2不为空的场合
							if (!isEmpty(infoTop.getDeptCharge1())
									|| !isEmpty(infoTop.getDeptCharge2())) {
								// 已审核
								return true;
							// 审核人1和审核人2都为空的场合
							} else {
								return false;
							}
						}
					}
				}
			}
		// 不存在, 则未审核
		} else {
			return false;
		}
		return false;
	}
	
	/**
	 * 部门审核明细部查询
	 */
	@SuppressWarnings("unchecked")
	public void getDetailInfoForRegister() {
		try {
			LogUtil.log("Action:部门审核明细部查询开始。", Level.INFO, null);
			PageObject object = new PageObject();
			// 已审核
			if(flag) {
				object = remote.getApprovedDetailInfoForRegister(
						attendanceDate, attendanceDeptId,
						employee.getEnterpriseCode());
			} else {
				// 上班休息标识查询
				getWorkRestFlagForRegister();
				// 查询
				object = remote.getDetailInfoForRegister(attendanceDate,
						attendanceDate.substring(INT_0, INT_4),
						attendanceDate.substring(INT_5, INT_7),
						attendanceDeptId, workRestFlag,
						employee.getEnterpriseCode());
				List<DeptAttendance> list = object.getList();
				if(list.size() > 1) {
					// 每个人员只显示一条记录
					for(int i = 0; i < list.size(); i++) {
						DeptAttendance info1 = list.get(i);
						for(int j = i + 1; j < list.size(); j++) {
							DeptAttendance info2 = list.get(j);
							if(info1.getEmpId().equals(info2.getEmpId())) {
								list.remove(j);
								j--;
							}
						}
					}
					object.setList(list);
					object.setTotalCount((long)list.size());
				}
			}
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
	public void getEmpExistForRegister() {
		LogUtil.log("Action:查询符合条件的人员个数开始。", Level.INFO, null);
		// 查询
		int count = remote.getEmpCountForRegister(attendanceDate,
				attendanceDeptId, employee.getEnterpriseCode());
		// 是否已经审核标识
		boolean flag = false;
		// 存在这样的人员
		if(count > 0) {
			flag = true;
		}
		// 输出
		write(RESULT_BEGIN  + flag + RESULT_END);
		LogUtil.log("Action:查询符合条件的人员个数结束。", Level.INFO, null);
	}
	
	/**
	 * 获得上班休息标识
	 */
	@SuppressWarnings("unchecked")
	public void getWorkRestFlagForRegister() {
		LogUtil.log("Action:查询上班休息标识开始。", Level.INFO, null);
		// 查询
		PageObject object = remote.getHolidayForRegister(attendanceDate,
				employee.getEnterpriseCode());
		List<HrCHoliday> list = object.getList();
		// 考勤星期是周末的场合
		if (INT_0 == attendanceWeekday || INT_6 == attendanceWeekday) {
			// 上班休息FLG = 1（休息）
			setWorkRestFlag(WORK_REST_FLAG_1);
			if (list.size() > 0) {
				HrCHoliday info = list.get(0);
				// 节假日类别 = 2（周末上班）
				if (CACodeConstants.HOLIDAY_TYPE_WEEKEND_WORK.equals(info
						.getHolidayType())) {
					// 上班休息FLG = 2（上班）
					setWorkRestFlag(WORK_REST_FLAG_2);
				}
			}
		// 考勤星期是非周末的场合
		} else {
			// 上班休息FLG = 2（上班）
			setWorkRestFlag(WORK_REST_FLAG_2);
			if (list.size() > 0) {
				HrCHoliday info = list.get(0);
				// 节假日类别 = 1（非周末休息）
				if (CACodeConstants.HOLIDAY_TYPE_WEEKDAY_REST.equals(info
						.getHolidayType())) {
					// 上班休息FLG = 1（休息）
					setWorkRestFlag(WORK_REST_FLAG_1);
				}
			}
		}
		LogUtil.log("Action:查询上班休息标识结束。", Level.INFO, null);
	}
	
	/**
	 * 保存部门考勤记录
	 */
	@SuppressWarnings("unchecked")
	public void saveDeptAttendanceForRegister() throws Exception {
		UserTransaction tx = null;
		try {
			LogUtil.log("Action:保存部门考勤记录开始。", Level.INFO, null);
			
			tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			// 事务开始
			tx.begin();
			
			// 将记录字符串转化为数组
			ArrayList arrayList = (ArrayList)JSONUtil.deserialize(records);
			// 循环保存
			for(int i = 0; i < arrayList.size(); i++) {
				Map map = (Map)arrayList.get(i);
				HrJWorkattendance bean = new HrJWorkattendance();
				// 更新一条记录
				if (FLAG_1.equals(objectToString(map.get("existFlag")))) {
					HrJWorkattendanceId workAttendanceId =
						new HrJWorkattendanceId();
					// 设置ID
					workAttendanceId.setEmpId(
							Long.parseLong(objectToString(map.get("empId"))));
					// 设置考勤日期
					workAttendanceId.setAttendanceDate(
							formatStringToDate(attendanceDate, DATE_FORMAT));
					// 根据人员ID和考勤日期查询考勤记录
					bean = workAttendanceRemote.findById(workAttendanceId,
							employee.getEnterpriseCode());
					// 结果为空
					if(bean == null) {
						throw new DataChangeException(Constants.BLANK_STRING);
					}
				}
				
				// 设置上午上班时间
				bean.setAmBegingTime(objectToString(map.get("amBeginTime")));
				// 设置上午下班时间
				bean.setAmEndTime(objectToString(map.get("amEndTime")));
				// 设置下午上班时间
				bean.setPmBegingTime(objectToString(map.get("pmBeginTime")));
				// 设置下午下班时间
				bean.setPmEndTime(objectToString(map.get("pmEndTime")));
				// 设置运行班类别
				if(!Constants.BLANK_STRING.equals(
						objectToString(map.get("workShiftId")))) {
					bean.setWorkShiftId(Long.parseLong(
							objectToString(map.get("workShiftId"))));
				} else {
					bean.setWorkShiftId(null);
				}
				// 设置假别ID
				if(!Constants.BLANK_STRING.equals(
						objectToString(map.get("vacationTypeId")))) {
					bean.setVacationTypeId(Long.parseLong(
							objectToString(map.get("vacationTypeId"))));
				} else {
					bean.setVacationTypeId(null);
				}
				// 设置加班类别ID
				if(!Constants.BLANK_STRING.equals(
						objectToString(map.get("overTimeId")))) {
					bean.setOvertimeTypeId(Long.parseLong(
							objectToString(map.get("overTimeId"))));
				} else {
					bean.setOvertimeTypeId(null);
				}
				// 设置出勤
				bean.setWork(objectToString(map.get("attendanceFlag")));
				// 设置休息
				if(Constants.BLANK_STRING.equals(
						objectToString(map.get("restType")))) {
					bean.setRestType(FLAG_0);
				} else {
					bean.setRestType(objectToString(map.get("restType")));
				}
				// 设置旷工
				if(Constants.BLANK_STRING.equals(
						objectToString(map.get("absentWork")))) {
					bean.setAbsentWork(FLAG_0);
				} else {
					bean.setAbsentWork(objectToString(map.get("absentWork")));
				}
				// 设置迟到
				bean.setLateWork(objectToString(map.get("lateWorkFlag")));
				// 设置早退
				bean.setLeaveEarly(objectToString(map.get("leaveEarlyFlag")));
				// 设置外借
				if(Constants.BLANK_STRING.equals(
						objectToString(map.get("outWork")))) {
					bean.setOutWork(FLAG_0);
				} else {
					bean.setOutWork(objectToString(map.get("outWork")));
				}
				// 设置出差
				if(Constants.BLANK_STRING.equals(
						objectToString(map.get("evectionType")))) {
					bean.setEvectionType(FLAG_0);
				} else {
					bean.setEvectionType(objectToString(map.get("evectionType")));
				}
				
				// add by liuyi 20100202 
				// 设置加班时间Id
				if(!objectToString(map.get("overtimeTimeId")).equals(""))
					bean.setOvertimeTimeId(Long.parseLong(objectToString(map.get("overtimeTimeId"))));
				// 设置病假时间id
				if(!objectToString(map.get("sickLeaveTimeId")).equals(""))
					bean.setSickLeaveTimeId(Long.parseLong(objectToString(map.get("sickLeaveTimeId"))));
				// 事假时间Id
				if(!objectToString(map.get("eventTimeId")).equals(""))
					bean.setEventTimeId(Long.parseLong(objectToString(map.get("eventTimeId"))));
				// 旷工时间Id
				if(!objectToString(map.get("absentTimeId")).equals(""))
					bean.setAbsentTimeId(Long.parseLong(objectToString(map.get("absentTimeId"))));
				// 其他请假时间Id
				if(!objectToString(map.get("otherTimeId")).equals(""))
					bean.setOtherTimeId(Long.parseLong(objectToString(map.get("otherTimeId"))));
				// add by liuyi 20100202 增加四个时间id结束
				
				// 设置备注
				bean.setMemo(objectToString(map.get("memo")));
				// 设置上次修改人
				bean.setLastModifiyBy(employee.getWorkerCode());
				// 设置是否使用
				bean.setIsUse(Constants.IS_USE_Y);
				// 设置企业编码
				bean.setEnterpriseCode(employee.getEnterpriseCode());
				
				// 新增
				if (FLAG_0.equals(objectToString(map.get("existFlag")))) {
					HrJWorkattendanceId workAttendanceId = new HrJWorkattendanceId();
					// 设置人员ID
					workAttendanceId.setEmpId(
							Long.parseLong(objectToString(map.get("empId"))));
					// 设置考勤日期
					workAttendanceId.setAttendanceDate(
							formatStringToDate(attendanceDate, DATE_FORMAT));
					// 设置ID
					bean.setId(workAttendanceId);
					// 设置部门ID
					if(!Constants.BLANK_STRING.equals(
							objectToString(map.get("deptId")))) {
						bean.setDeptId(
								Long.parseLong(objectToString(map.get("deptId"))));
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
					workAttendanceRemote.save(bean);
				// 更新
				} else if (FLAG_1.equals(objectToString(map.get("existFlag")))) {
					// 设置上次修改时间
					bean.setLastModifiyDate(formatStringToDate(
							objectToString(map.get("lastModifiyDate")), TIME_FORMAT));
					// 更新
					workAttendanceRemote.update(bean);
				}
			}
			
			// 提交事务
			tx.commit();
			// 返回结果
			write(RESULT_BEGIN  + true + RESULT_END);
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
	
	/**
	 * 判断值是否为空
	 * 
	 * @param strValue
	 *            值
	 * @return 是否为空
	 */
	public boolean isEmpty(String strValue) {
		return Constants.BLANK_STRING.equals(strValue)
			|| strValue == null;
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
	 * @param objValue 对象
	 * @return 字符
	 */
	public String objectToString(Object objValue) {
		if(objValue == null) {
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
