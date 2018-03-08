/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.ca.leave.personalvacationregister.action;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.CACodeConstants;
import power.ejb.hr.ca.EmpVacationInfo;
import power.ejb.hr.ca.HrCAttendancedepFacadeRemote;
import power.ejb.hr.ca.HrCAttendancestandard;
import power.ejb.hr.ca.HrCHolidayFacadeRemote;
import power.ejb.hr.ca.HrCVacationtype;
import power.ejb.hr.ca.HrCVacationtypeFacadeRemote;
import power.ejb.hr.ca.HrCYearplan;
import power.ejb.hr.ca.HrCYearplanFacadeRemote;
import power.ejb.hr.ca.HrJVacation;
import power.ejb.hr.ca.HrJVacationFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.hr.ca.leave.empvacationregister.action.EmpVacationRegisterAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 个人请假登记
 * 
 * @author zhaozhijie
 * @version 1.0
 */
public class PersonalVacationRegisterAction extends AbstractAction  {

    /** serial id */
    private static final long serialVersionUID = 1L;
	/** 请假登记ejb远程对象 */
	protected HrJVacationFacadeRemote remote;
	/** 考勤标准维护ejb远程对象 */
	protected HrCAttendancedepFacadeRemote hrCAttendremote;
	/** 假别编码ejb远程对象 */
	protected HrCVacationtypeFacadeRemote hrCVacationremote;
	/** 节假日维护ejb远程对象 */
	protected HrCHolidayFacadeRemote hrCHolidayremote;
	/** 年初计划ejb远程对象 */
	protected HrCYearplanFacadeRemote hrCYearplanremote;
	/** 请假登记bean */
	private EmpVacationInfo empVacationInfo;
	/** 数据结构 */
	@SuppressWarnings("unchecked")
	private Map originalMap;
	/** exceptionMessage */
	private static final String EXCEPTION_MESSAGE = "计算";
	/** 年末时间 */
	private String YEAR_END_TIME = "-12-31 23:59:59";
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	/** 修改成功补充 */
	private static final String MODIFY_SUCCESS_SUPPLY =  ", 'success' : true,'msg' : 'data'}";
	/** 时长格式化 */
	private static final String PATTERN_NUMBER_TIME = "##############0.0";
	/** 日期补充 */
	private static final String DATE_SUPPLY = ":00";
    /** 请假时长已超出年初计划时长信息*/
    public static final String TIME_SUCCESS = "{success:true,msg:'TIME',";
    /** 请假时间有重复*/
    public static final String REPEAT_SUCCESS = "{success:true,msg:'REPEAT'}";
    /** 常量} */
    public static final String SEPARATOR_PARENTHESIS = "}"; 
    /** 常量data: */
    public static final String SEPARATOR_DATA = "data:"; 
    /** 常量{vacationDays : */
    public static final String SEPARATOR_DAYS = "{vacationDays : "; 
    /** 常量,vacationHours : */
    public static final String SEPARATOR_HOURS = ",vacationHours :"; 
	/** 年初时间 */
	private String YEAR_START_TIME = "-01-01 00:00:01";
    /** 部门id */
    private String deptId;
    /** 人员id */
    private String empId;
    /** 开始时间 */
    private String startTime;
    /** 结束时间 */
    private String endTime;
    /** 开始条数 */
    private String start;
    /** 限制条数 */
    private String limit;
    /** 是否包括周末 */
    private String ifWeekend;
    /** 是否修改 */
    private boolean newFlag;
    /** 是否删除 */
    private boolean flag;

	/**
     * 构造函数
     */
    public PersonalVacationRegisterAction() {
		// 请假登记ejb远程对象
		remote = (HrJVacationFacadeRemote) factory
				.getFacadeRemote("HrJVacationFacade");
		// 考勤标准维护ejb远程对象
		hrCAttendremote = (HrCAttendancedepFacadeRemote) factory
		.getFacadeRemote("HrCAttendancedepFacade");
		// 假别编码ejb远程对象
		hrCVacationremote = (HrCVacationtypeFacadeRemote) factory
		.getFacadeRemote("HrCVacationtypeFacade");
		// 节假日维护ejb远程对象
		hrCHolidayremote = (HrCHolidayFacadeRemote) factory
		.getFacadeRemote("HrCHolidayFacade");
		// 年初计划ejb远程对象
		hrCYearplanremote = (HrCYearplanFacadeRemote) factory
		.getFacadeRemote("HrCYearplanFacade");
		
    }

    /**
     * 查询部门请假信息
     */
    @SuppressWarnings("unchecked")
	public void getPersonalVacationInfo() {
		LogUtil.log("Action:部门请假信息查询开始", Level.INFO, null);
		try{
			PageObject obj = remote.findEmpVacation(deptId, empId, startTime,
					endTime, employee.getEnterpriseCode(), Integer.parseInt(start), Integer.parseInt(limit));
			// 原始数据
			 originalMap = new HashMap();
			if (obj.getList() != null) {
				Iterator<EmpVacationInfo> it = obj.getList().iterator();
				// 部门的请假信息
				Map originMap = new HashMap();
				while(it.hasNext()) {
					EmpVacationInfo empVacationInfo = it.next();
					// 如果包含此员工就加到该员工请假信息里面
					if (originalMap.containsKey(empVacationInfo.getEmpId())) {
						originMap = (Map) originalMap.get(empVacationInfo.getEmpId());
						originMap.put(empVacationInfo.getStartTime().substring(0, 4), 
								Double.parseDouble(empVacationInfo.getVacationHours()));
					// 如果不包含此员工就新建一个
					} else {
						originMap =  new HashMap();
						originMap.put(empVacationInfo.getStartTime().substring(0, 4), 
								Double.parseDouble(empVacationInfo.getVacationHours()));
						originalMap.put(empVacationInfo.getEmpId(), originMap);
					}
				}
			}
            request.getSession().setAttribute("origin", originalMap);
			write(JSONUtil.serialize(obj));
			LogUtil.log("Action:部门请假信息查询结束", Level.INFO, null);
		} catch (SQLException e){
			//显示失败
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:查询部门请假信息失败。", Level.SEVERE, null);
		} catch (JSONException je){
			//显示失败
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询部门请假信息失败。", Level.SEVERE, null);
		}
    }

    /**
     * 查询考勤部门名称
     */
    public void getPersonalAttendanceDept() {
		LogUtil.log("Action:考勤部门名称查询开始", Level.INFO, null);
		try{
			PageObject pobj = new PageObject();
			if (empId != null) { 
				pobj = hrCAttendremote.findAttendanceDept(empId, employee.getEnterpriseCode());
			}
			write(JSONUtil.serialize(pobj));
			LogUtil.log("Action:考勤部门名称查询结束", Level.INFO, null);
		} catch (SQLException e){
			//显示失败
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:查询考勤部门名称失败。", Level.SEVERE, null);
		} catch (JSONException je){
			//显示失败
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询考勤部门名称失败。", Level.SEVERE, null);
		}
    }
    /**
     * 查询系统月份上，下班时间
     */
    public void getPersonalOnDutyTime() {
		LogUtil.log("Action:系统月份上，下班时间查询开始", Level.INFO, null);
		try{
			PageObject obj = new PageObject();
			if (empId != null) {
				obj = hrCAttendremote.findSysOnDutyTime(empId, employee.getEnterpriseCode());
				if (obj.getList() == null || obj.getList().size() == 0 ||
						((HrCAttendancestandard)obj.getList().get(0)).getAmBegingTime() == null) {
					PageObject pobj = hrCAttendremote.findAttendanceDept(empId, employee.getEnterpriseCode());
					write(JSONUtil.serialize(pobj));
				} else {
					write(JSONUtil.serialize(obj));
				}
			} else {
				write(JSONUtil.serialize(obj));
			}
			LogUtil.log("Action:系统月份上，下班时间查询结束", Level.INFO, null);
		} catch (SQLException e){
			//显示失败
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:查询系统月份上，下班时间失败。", Level.SEVERE, null);
		} catch (JSONException je){
			//显示失败
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询系统月份上，下班时间失败。", Level.SEVERE, null);
		}
    }

	/**
	 * 查询请假类别信息
	 */
	@SuppressWarnings("unchecked")
	public void getPersonalVacationType() {
        try {
            LogUtil.log("Action:请假类别查询开始。", Level.INFO, null);
            PageObject obj = new PageObject();
            // 查询奖惩类别维护信息
            obj = hrCVacationremote.findVacationTypeCmb(employee.getEnterpriseCode());
    		// 追加空格
    		if(obj.getList().size() > 0){
    			HrCVacationtype info = new HrCVacationtype();
    			info.setVacationTypeId(null);
    			info.setVacationType("");
    			info.setIfWeekend("");
    			obj.getList().add(0, info);
    		}
            String str = JSONUtil.serialize(obj);
            write(str);
            LogUtil.log("Action:请假类别查询结束。", Level.INFO, null);
        } catch (JSONException jsone) {
            LogUtil.log("Action:请假类别查询失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
        } catch (SQLException sqle) {
            LogUtil.log("Action:请假类别查询失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
        }
	}

	/**
	 * 查询节假日信息
	 */
	@SuppressWarnings("unchecked")
	public void getPersonalHoliday() {
        try {
            LogUtil.log("Action:节假日信息查询开始。", Level.INFO, null);
            PageObject obj = new PageObject();
            // 查询节假日信息
            obj = hrCHolidayremote.findHoliday(employee.getEnterpriseCode());
            String str = JSONUtil.serialize(obj);
            write(str);
            LogUtil.log("Action:节假日信息查询结束。", Level.INFO, null);
        } catch (JSONException jsone) {
            LogUtil.log("Action:节假日信息查询失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
        } catch (SQLException sqle) {
            LogUtil.log("Action:节假日信息查询失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
        } catch (ParseException e) {
            LogUtil.log("Action:节假日信息查询失败。", Level.SEVERE, e);
			write(Constants.DATA_FAILURE);
		}
	}

	/**
	 * 查询部门人员信息
	 */
	@SuppressWarnings("unchecked")
	public void getPersonalEmpInfo() {
        try {
            LogUtil.log("Action:部门人员信息查询开始。", Level.INFO, null);
            PageObject obj = new PageObject();
            if (employee.getDeptId() != null) {
                // 查询部门人员信息
                obj = remote.getDeptEmpInfo(employee.getDeptId(),
                		employee.getWorkerId(), employee.getEnterpriseCode());
            }
            String str = JSONUtil.serialize(obj);
            write(str);
            LogUtil.log("Action:部门人员信息查询结束。", Level.INFO, null);
        } catch (JSONException jsone) {
            LogUtil.log("Action:部门人员信息查询失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
        } catch (SQLException sqle) {
            LogUtil.log("Action:部门人员信息查询失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
        } 
	}

	/**
	 * 新增操作
	 * @param lstSaveEmpRewardRegisterInfo 新增的数据
	 * @throws SQLException 
	 * @throws DataChangeException 
	 */
	@SuppressWarnings("unchecked")
	public void newVacation(List<HrJVacation> lstSaveHrJVacation) throws SQLException, DataChangeException {
		LogUtil.log("Action:新增部门请假登记开始", Level.INFO, null);
		try {
			Map newMap = setMap();
			// 日期的格式化
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
			Set<String> keys = newMap.keySet();
			Object[] arrayKey = (Object[])keys.toArray();
			Arrays.sort(arrayKey);
			for (int i=0; i< arrayKey.length; i++) {
				EmpVacationInfo empVacatiInfo =  (EmpVacationInfo) newMap.get(arrayKey[i].toString());
				HrJVacation entity = setNewHrJVacation();
				// 开始时间
				Date dteStartTime = dateFormat.parse(empVacatiInfo.getStartTime());
				entity.setStartTime(dteStartTime);
				// 结束时间
				Date dteEndTime = dateFormat.parse(empVacatiInfo.getEndTime());
				entity.setEndTime(dteEndTime);
				// 请假天数
				entity.setVacationDays(Double.parseDouble(empVacatiInfo.getVacationDays()));
				// 请假时长
				entity.setVacationTime(Double.parseDouble(empVacatiInfo.getVacationHours()));
				lstSaveHrJVacation.add(entity);
			}
			LogUtil.log("Action:新增部门请假登记结束", Level.INFO, null);
		}catch (ParseException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:新增部门请假登记失败", Level.SEVERE, e);
		}
	}

	/**
	 * 保存操作
	 */
	@SuppressWarnings("unchecked")
	public void savePersonalVacation() {
		try{
			LogUtil.log("Action:部门请假登记操作开始。", Level.INFO, null);
			 // 新增部门请假登记
			 List<HrJVacation> lstSaveHrJVacation = new ArrayList<HrJVacation>();
			 // 更新部门请假登记
			 List<HrJVacation> lstUpdateHrJVacation = new ArrayList<HrJVacation>();
			 // 删除部门请假登记
			 List<HrJVacation> lstDeleteHrJVacation = new ArrayList<HrJVacation>();
			 // 员工id
			 String strEmpId = empVacationInfo.getEmpId();
			 // 部门id
			 String strDeptId = empVacationInfo.getDeptId();
			 // 开始时间
			 String strStartTime = empVacationInfo.getStartTime() + DATE_SUPPLY;
			 // 结束时间
			 String strEndTime = empVacationInfo.getEndTime() + DATE_SUPPLY;
			 // 假别id
			 String strVacationTypeId = empVacationInfo.getVacationTypeId();
			 // 请假id
			 String strVacationId = empVacationInfo.getVacationId();
            PageObject obj = new PageObject();
			 // 判断是否删除
			 if (flag) {
				 // 删除部门请假登记
				 deleteVacation(lstDeleteHrJVacation);
			 } else {
				 // 请假时长已超出年初计划时长check
				 PageObject pobj =hrCYearplanremote.findVacationHours(
						 strEmpId, strStartTime,
						 strVacationTypeId, employee.getEnterpriseCode());
				 if (pobj.getList() !=null && pobj.getList().size() !=0) {
					 HrCYearplan hrCYearplan = (HrCYearplan)pobj.getList().get(0);
					 //  计划时长不为空的场合
					 if(hrCYearplan.getHours() != null) {
						 double dobVacationHours = Double.parseDouble(empVacationInfo.getVacationHours());
						 double dobVacationTimes =0;
						 String patternNum = PATTERN_NUMBER_TIME;
						 DecimalFormat dfNum = new DecimalFormat(patternNum);
						 PageObject pobj1 = remote.findVacationUsedHours(strEmpId, strDeptId,
								 strStartTime, strVacationTypeId, !newFlag,
								 strVacationId, employee.getEnterpriseCode());
						 if (pobj1.getList() != null && pobj1.getList().size() !=0) {
							 HrJVacation hrJVacation = (HrJVacation)pobj1.getList().get(0);
							 dobVacationTimes = hrJVacation.getVacationTime();
						 }
						 // 请假时长>计划时长 - 已用时长 的场合
						 if (dobVacationHours >(hrCYearplan.getHours() -dobVacationTimes)) {
							 LogUtil.log("Action:员工请假登记操作结束。", Level.INFO, null);
							 String returnStr =TIME_SUCCESS + SEPARATOR_DATA+
							 dfNum.format((hrCYearplan.getHours() -dobVacationTimes)) +SEPARATOR_PARENTHESIS;
							 write(returnStr);
							 return;
						 }
					 }
				 }
				 // 判断请假期间重复
				 PageObject pobj2 = remote.findVacationRepeat(strEmpId, strEndTime,
						 strStartTime, strVacationId, employee.getEnterpriseCode(), !newFlag);
				 if (pobj2.getList() != null && pobj2.getList().size() !=0) {
					 EmpVacationInfo empVacaInfo = (EmpVacationInfo) pobj2.getList().get(0);
					 // 重复数〉0
					 if (empVacaInfo.getRepeat() >0) {
						 LogUtil.log("Action:部门请假登记操作结束。", Level.INFO, null);
						 write(REPEAT_SUCCESS);
						 return;
					 }
				 }
				 // 判断新增，更新
				 // 新增部门请假登记
				 if (newFlag) {
					 newVacation(lstSaveHrJVacation);
				 // 更新部门请假登记
				 } else {
					 updateVacation(lstUpdateHrJVacation,lstSaveHrJVacation,lstDeleteHrJVacation);
				 }
			 }
			 List lstReturn = remote.save(lstSaveHrJVacation,lstUpdateHrJVacation,lstDeleteHrJVacation);
			 if (!flag) {
				 // 新增员工请假登记
				 if (newFlag) {
					 obj.setList(new ArrayList());
					 lstSaveHrJVacation.get(0).setVacationid((Long) lstReturn.get(0));
					 obj.getList().add(lstSaveHrJVacation.get(0));
				// 修改
				 } else {
					 obj.setList(new ArrayList());
					 lstUpdateHrJVacation.get(0).setLastModifiyDate((Date)lstReturn.get(1));
					 obj.getList().add(lstUpdateHrJVacation.get(0));
				 }
			 }

            String str = JSONUtil.serialize(obj);
            str = str.substring(0,str.length()-1) + MODIFY_SUCCESS_SUPPLY;
			 write(str);
			 LogUtil.log("Action:部门请假登记操作结束。", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:部门请假登记操作失败。", Level.SEVERE, null);
			if (EXCEPTION_MESSAGE.equals(e.getMessage())) {
				try {
				    PageObject pobj = hrCAttendremote.findAttendanceDept(empId, employee.getEnterpriseCode());
					write(JSONUtil.serialize(pobj));
				} catch (JSONException e1) {
					LogUtil.log("Action:考勤部门名称查询失败。", Level.SEVERE, null);
					write(Constants.SQL_FAILURE);
				} catch (SQLException e2) {
					LogUtil.log("Action:考勤部门名称查询失败。", Level.SEVERE, null);
					write(Constants.SQL_FAILURE);
				}
			} else {
				write(Constants.DATA_USING);
			}
		} catch (SQLException e) {
			LogUtil.log("Action:部门请假登记操作失败。", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		} catch (Exception e) {
			LogUtil.log("Action:部门请假登记操作失败。", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 删除操作
	 * @param  lstDeleteHrJVacation 所需删除的数据
	 * @throws ParseException 
	 */
	private void deleteVacation(
			List<HrJVacation> lstDeleteHrJVacation) throws ParseException {
		LogUtil.log("Action:删除部门请假登记开始", Level.INFO, null);
		HrJVacation entity = new HrJVacation();
		// 部门请假登记实体
		entity = remote.findById(Long.parseLong(empVacationInfo.getVacationId()));
		// 是否使用
		entity.setIsUse(Constants.IS_USE_N);
		// 修改人
		entity.setLastModifiyBy(employee.getWorkerCode());
		// 修改时间
		// 日期的格式化
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
		entity.setLastModifiyDate(dateFormat.parse(empVacationInfo.getLastModifyDate()));

		lstDeleteHrJVacation.add(entity);
		LogUtil.log("Action:删除部门请假登记结束", Level.INFO, null);
	}

	/**
	 * 更新操作
	 * @param lstUpdateHrJVacation  所需更新的数据
	 * @param lstSaveHrJVacation 
	 * @param lstDeleteHrJVacation 
	 * @throws SQLException 
	 * @throws DataChangeException 
	 */
	@SuppressWarnings("unchecked")
	private void updateVacation(
			List<HrJVacation> lstUpdateHrJVacation, List<HrJVacation> lstSaveHrJVacation,
			List<HrJVacation> lstDeleteHrJVacation) throws SQLException, DataChangeException {
		LogUtil.log("Action:更新部门请假登记开始", Level.INFO, null);
		try {
			Map newMap = setMap();
			// 日期的格式化
			SimpleDateFormat dateFormat =
				new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
			Set<String> keys = newMap.keySet();
			Object[] arrayKey = keys.toArray();
			Arrays.sort(arrayKey);	
			for (int i=0; i< arrayKey.length; i++) {
				String key = (String)arrayKey[i];
				EmpVacationInfo empVacatiInfo = (EmpVacationInfo) newMap.get(key);
	
				HrJVacation entity = new HrJVacation();
				// 如果包括就更新
				if (empVacationInfo.getVacationId().equals(empVacatiInfo.getVacationId())) {
					// 部门请假登记实体
					entity = remote.findById(Long.parseLong(empVacationInfo.getVacationId()));
					entity = setModifyHrJVacation(entity);
					// 开始时间
					Date dteStartTime = dateFormat.parse(empVacatiInfo.getStartTime());
					entity.setStartTime(dteStartTime);
					// 结束时间
					Date dteEndTime = dateFormat.parse(empVacatiInfo.getEndTime());
					entity.setEndTime(dteEndTime);
					// 请假天数
					entity.setVacationDays(Double.parseDouble(empVacatiInfo.getVacationDays()));
					// 请假时长
					entity.setVacationTime(Double.parseDouble(empVacatiInfo.getVacationHours()));
					lstUpdateHrJVacation.add(entity);
				// 插入
				} else {
					HrJVacation hrJVacation = setNewHrJVacation();
					// 开始时间
					Date dteStartTime = dateFormat.parse(empVacatiInfo.getStartTime());
					hrJVacation.setStartTime(dteStartTime);
					// 结束时间
					Date dteEndTime = dateFormat.parse(empVacatiInfo.getEndTime());
					hrJVacation.setEndTime(dteEndTime);
					// 请假天数
					hrJVacation.setVacationDays(Double.parseDouble(empVacatiInfo.getVacationDays()));
					// 请假时长
					hrJVacation.setVacationTime(Double.parseDouble(empVacatiInfo.getVacationHours()));
					lstSaveHrJVacation.add(hrJVacation);
				}
			}

			LogUtil.log("Action:更新部门请假登记结束", Level.INFO, null);
		} catch (ParseException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:更新部门请假登记失败", Level.SEVERE, e);
		}
	}

	/**
	 * 设新增值操作
	 * @return 新增的数据
	 */
	public HrJVacation setNewHrJVacation() {
		HrJVacation entity = new HrJVacation();
		// 人员id
		if(empVacationInfo.getEmpId() != null && !"".equals(empVacationInfo.getEmpId())) {
			entity.setEmpId(Long.parseLong(empVacationInfo.getEmpId()));
			empId = empVacationInfo.getEmpId();
		}
		// 部门id
		if(empVacationInfo.getDeptId() !=null && !"".equals(empVacationInfo.getDeptId())) {
			entity.setDeptId(Long.parseLong(empVacationInfo.getDeptId()));
			deptId = empVacationInfo.getDeptId();
		}
		// 假别ID
		if(empVacationInfo.getVacationTypeId() !=null && !"".equals(empVacationInfo.getVacationTypeId())) {
			entity.setVacationTypeId(Long.parseLong(empVacationInfo.getVacationTypeId()));
		}
		// 原因
		entity.setReason(empVacationInfo.getReason());
		// 去向
		entity.setWhither(empVacationInfo.getWhither());
		// 备注
		entity.setMemo(empVacationInfo.getMemo());
		// 是否销假
		entity.setIfClear(CACodeConstants.IF_CLEAR_NO);
		// 销假时间
		entity.setClearDate(null);
		// 签字状态
		entity.setSignState(CACodeConstants.SIGN_STATE_UNREPORT);
		// 工作流序号
		entity.setWorkFlowNo(null);
		// 记录人
		entity.setInsertby(employee.getWorkerCode());
		// 记录日期
		entity.setInsertdate(new java.util.Date());
		// 修改人
		entity.setLastModifiyBy(employee.getWorkerCode());
		// 修改日期
		entity.setLastModifiyDate(new java.util.Date());
		// 企业编码
		entity.setEnterpriseCode(employee.getEnterpriseCode());
		// 是否使用
		entity.setIsUse(Constants.IS_USE_Y);
		return entity;
	}

	/**
	 * 设修改值操作
	 * @param 所需修改的值
	 * @return 修改的数据
	 * @throws ParseException 
	 */
	public HrJVacation setModifyHrJVacation(HrJVacation argEntity) throws ParseException {
		// 日期的格式化
		SimpleDateFormat dateFormat =
			new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
		// 人员id
		if(empVacationInfo.getEmpId() != null && !"".equals(empVacationInfo.getEmpId())) {
			argEntity.setEmpId(Long.parseLong(empVacationInfo.getEmpId()));
			empId = empVacationInfo.getEmpId();
		}
		// 部门id
		if(empVacationInfo.getDeptId() !=null && !"".equals(empVacationInfo.getDeptId())) {
			argEntity.setDeptId(Long.parseLong(empVacationInfo.getDeptId()));
			deptId = empVacationInfo.getDeptId();
		}
		// 假别ID
		if(empVacationInfo.getVacationTypeId() !=null && !"".equals(empVacationInfo.getVacationTypeId())) {
			argEntity.setVacationTypeId(Long.parseLong(empVacationInfo.getVacationTypeId()));
		}
		// 原因
		argEntity.setReason(empVacationInfo.getReason());
		// 去向
		argEntity.setWhither(empVacationInfo.getWhither());
		// 备注
		argEntity.setMemo(empVacationInfo.getMemo());
		// 修改人
		argEntity.setLastModifiyBy(employee.getWorkerCode());
		// 修改时间
		argEntity.setLastModifiyDate(dateFormat.parse(empVacationInfo.getLastModifyDate()));
		return argEntity;
	}

	/**
	 * 计算map
	 * @throws ParseException 
	 * @throws SQLException 
	 * @return 时长的map
	 * @throws DataChangeException 
	 */
	@SuppressWarnings("unchecked")
	public Map setMap() throws ParseException, SQLException, DataChangeException {
		// 开始时间年末
		String strYearEnd = empVacationInfo.getStartTime().substring(0,4);
		strYearEnd =strYearEnd + YEAR_END_TIME;
		// 结束时间年初
		String strYearStart = empVacationInfo.getEndTime().substring(0,4);
		strYearStart =strYearStart + YEAR_START_TIME;
		EmpVacationRegisterAction empAction = new EmpVacationRegisterAction();
		startTime = empVacationInfo.getStartTime();
		endTime = empVacationInfo.getEndTime();
		empId = empVacationInfo.getEmpId();
		deptId = empVacationInfo.getDeptId();
		Map newMap = new HashMap();
		String[] strOutTimes = new String[2];
		EmpVacationInfo empVacaInfo = new EmpVacationInfo();
		// 开始和结束时间是同一年份
		if (startTime.substring(0,4).equals(endTime.substring(0,4))) {
			// 计算
			strOutTimes = empAction.calculateNums(startTime, endTime,
					empId, ifWeekend, employee.getEnterpriseCode());
			// 请假时长
			empVacaInfo.setVacationHours(strOutTimes[0]);
			// 请假天数
			empVacaInfo.setVacationDays(strOutTimes[1]);
			// 开始时间
			empVacaInfo.setStartTime(startTime+ DATE_SUPPLY);
			// 结束时间
			empVacaInfo.setEndTime(endTime+ DATE_SUPPLY);
			empVacaInfo.setVacationId(empVacationInfo.getVacationId());
			newMap.put(startTime.substring(0,4), empVacaInfo);
		} else {
			// 开始时间
			// 计算
			strOutTimes = empAction.calculateNums(startTime, strYearEnd,
					empId, ifWeekend, employee.getEnterpriseCode());
			// 请假时长
			empVacaInfo.setVacationHours(strOutTimes[0]);
			// 请假天数
			empVacaInfo.setVacationDays(strOutTimes[1]);
			// 开始时间
			empVacaInfo.setStartTime(startTime+ DATE_SUPPLY);
			// 结束时间
			empVacaInfo.setEndTime(strYearEnd);
			empVacaInfo.setVacationId(empVacationInfo.getVacationId());
			newMap.put(startTime.substring(0,4), empVacaInfo);
			// 中间年度
			PageObject obj = hrCAttendremote.findBetYear(empVacationInfo.getEmpId(),
					empVacationInfo.getStartTime(),
					empVacationInfo.getEndTime(), employee.getEnterpriseCode());
			if (obj.getList() != null) {
				Iterator<HrCAttendancestandard> it = obj.getList().iterator();
				while (it.hasNext()) {
					EmpVacationInfo empVacaIn = new EmpVacationInfo();
					HrCAttendancestandard enti = it.next();
					// 中间年度的年份
					String strYear = enti.getAttendanceYear();
					// 年初
					String strYeaStart = strYear + YEAR_START_TIME;
					// 年末
					String strYeaEnd = strYear + YEAR_END_TIME;

					// 计算
					strOutTimes = empAction.calculateNums(strYeaStart, strYeaEnd,
							empId, ifWeekend, employee.getEnterpriseCode());
					// 请假时长
					empVacaIn.setVacationHours(strOutTimes[0]);
					// 请假天数
					empVacaIn.setVacationDays(strOutTimes[1]);
					// 开始时间
					empVacaIn.setStartTime(strYeaStart);
					// 结束时间
					empVacaIn.setEndTime(strYeaEnd);
					newMap.put(strYear, empVacaIn);
				}
			}
			EmpVacationInfo empVacatInfo = new EmpVacationInfo();
			// 结束时间
			// 计算
			strOutTimes = empAction.calculateNums(strYearStart, endTime,
					empId, ifWeekend, employee.getEnterpriseCode());
			// 请假时长
			empVacatInfo.setVacationHours(strOutTimes[0]);
			// 请假天数
			empVacatInfo.setVacationDays(strOutTimes[1]);
			// 开始时间
			empVacatInfo.setStartTime(strYearStart);
			// 结束时间
			empVacatInfo.setEndTime(endTime+DATE_SUPPLY);
			newMap.put(endTime.substring(0,4), empVacatInfo);
		} 
		return newMap;
	}

	/**
	 * 计算个人请假时长和天数
	 */
	@SuppressWarnings("unchecked")
	public void calculatePersonal() {
        try {
            LogUtil.log("Action:计算个人请假时长和天数开始。", Level.INFO, null);
    		EmpVacationRegisterAction empAction = new EmpVacationRegisterAction();
    		String[] strOutTimes = empAction.calculateNums(startTime, endTime, empId, ifWeekend, employee.getEnterpriseCode());
			String str = SEPARATOR_DAYS+strOutTimes[1] +SEPARATOR_HOURS +
			strOutTimes[0]+SEPARATOR_PARENTHESIS;
            write(str);
            LogUtil.log("Action:计算个人请假时长和天数结束。", Level.INFO, null);
        } catch (SQLException sqle) {
            LogUtil.log("Action:计算个人请假时长和天数失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
        } catch (ParseException e) {
            LogUtil.log("Action:计算个人请假时长和天数失败。", Level.SEVERE, e);
			write(Constants.DATA_FAILURE);
		} catch (DataChangeException e) {
			if (EXCEPTION_MESSAGE.equals(e.getMessage())) {
				try {
				    PageObject pobj = hrCAttendremote.findAttendanceDept(empId, employee.getEnterpriseCode());
					write(JSONUtil.serialize(pobj));
				} catch (JSONException e1) {
					LogUtil.log("Action:考勤部门名称查询失败。", Level.SEVERE, null);
					write(Constants.SQL_FAILURE);
				} catch (SQLException e2) {
					LogUtil.log("Action:考勤部门名称查询失败。", Level.SEVERE, null);
					write(Constants.SQL_FAILURE);
				}
			} 
		}
	}

	/**
	 * @return 部门id
	 */
	public String getDeptId() {
		return deptId;
	}

	/**
	 * @param 部门id
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/**
	 * @return 人员id
	 */
	public String getEmpId() {
		return empId;
	}

	/**
	 * @param 人员id
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}

	/**
	 * @return 开始时间
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param 开始时间
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return 结束时间
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param 结束时间
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return 开始条数
	 */
	public String getStart() {
		return start;
	}

	/**
	 * @param 开始条数
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * @return 限制条数
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * @param 限制条数
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}

    /**
	 * @return 是否包括周末
	 */
	public String getIfWeekend() {
		return ifWeekend;
	}

	/**
	 * @param 是否包括周末
	 */
	public void setIfWeekend(String ifWeekend) {
		this.ifWeekend = ifWeekend;
	}

	/**
	 * @return 是否修改
	 */
	public boolean isNewFlag() {
		return newFlag;
	}

	/**
	 * @param 是否修改
	 */
	public void setNewFlag(boolean newFlag) {
		this.newFlag = newFlag;
	}

	/**
	 * @return 请假登记bean 
	 */
	public EmpVacationInfo getEmpVacationInfo() {
		return empVacationInfo;
	}

	/**
	 * @param 请假登记bean 
	 */
	public void setEmpVacationInfo(EmpVacationInfo empVacationInfo) {
		this.empVacationInfo = empVacationInfo;
	}

	/**
	 * @return 是否删除
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @param 是否删除
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
