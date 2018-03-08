/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.ca.leave.empvacationregister.action;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 员工请假登记
 * 
 * @author zhaozhijie
 * @version 1.0
 */
public class EmpVacationRegisterAction extends AbstractAction  {

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
	/** 年末日期 */
	private String YEAR_END = "-12-31";
	/** 年末时间 */
	private String YEAR_END_TIME = "-12-31 23:59:59";
	/** 天数格式化 */
	private static final String PATTERN_NUMBER_DAY = "##############0.00";
	/** 时长格式化 */
	private static final String PATTERN_NUMBER_TIME = "##############0.0";
	/** exceptionMessage */
	private static final String EXCEPTION_MESSAGE = "计算";
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	/** 日期形式字符串 */
	private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	/** 修改成功补充 */
	private static final String MODIFY_SUCCESS_SUPPLY =  ", 'success' : true,'msg' : 'data'}";
	/** 正则表达式匹配字符 */
	private static final String REGEX_PATTERN =  "(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2})(:\\d{2})?";
	/** 日期补充 */
	private static final String DATE_SUPPLY = ":00";
    /** 请假时长已超出年初计划时长信息*/
    public static final String TIME_SUCCESS = "{success:true,msg:'TIME',";
    /** 请假时间有重复*/
    public static final String REPEAT_SUCCESS = "{success:true,msg:'REPEAT'}";
    /** 常量: */
    public static final String SEPARATOR_COLON = ":"; 
    /** 常量0.0*/
    public static final String SEPARATOR_ONE_DECIMAL = "0.0"; 
    /** 常量0.00 */
    public static final String SEPARATOR_TWO_DECIMAL = "0.00"; 
    /** 常量00 */
    public static final String SEPARATOR_TWO = "00"; 
    /** 常量0 */
    public static final String SEPARATOR_ONE = "0"; 
    /** 常量} */
    public static final String SEPARATOR_PARENTHESIS = "}"; 
    /** 常量data: */
    public static final String SEPARATOR_DATA = "data:"; 
    /** 常量{vacationDays : */
    public static final String SEPARATOR_DAYS = "{vacationDays : "; 
    /** 常量,vacationHours : */
    public static final String SEPARATOR_HOURS = ",vacationHours :"; 
	/** 年初日期 */
	private String YEAR_START = "-01-01";
	/** 年初时间 */
	private String YEAR_START_TIME = "-01-01 00:00:01";
	/** 包含周末 */
	private String IF_WEEKEND = "1";
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
    public EmpVacationRegisterAction() {
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
     * 查询员工请假信息
     */
    @SuppressWarnings("unchecked")
	public void getVacationInfo() {
		LogUtil.log("Action:员工请假信息查询开始", Level.INFO, null);
		try{
			PageObject obj = remote.findEmpVacation(deptId, empId, startTime,
					endTime, employee.getEnterpriseCode(), Integer.parseInt(start), Integer.parseInt(limit));
			// 原始数据
			 originalMap = new HashMap();
			if (obj.getList() != null) {
				Iterator<EmpVacationInfo> it = obj.getList().iterator();
				// 员工的请假信息
				Map originMap = new HashMap();
				while(it.hasNext()) {
					EmpVacationInfo empVacationInfo = it.next();
					// 如果包含此员工就加到该员工请假信息里面
					if (originalMap.containsKey(empVacationInfo.getVacationId())) {
						originMap = (Map) originalMap.get(empVacationInfo.getVacationId());
						originMap.put(empVacationInfo.getStartTime().substring(0, 4), 
								Double.parseDouble(empVacationInfo.getVacationHours()));
					// 如果不包含此员工就新建一个
					} else {
						originMap =  new HashMap();
						originMap.put(empVacationInfo.getStartTime().substring(0, 4), 
								Double.parseDouble(empVacationInfo.getVacationHours()));
						originalMap.put(empVacationInfo.getVacationId(), originMap);
					}
				}
			}
            request.getSession().setAttribute("origin", originalMap);
			write(JSONUtil.serialize(obj));
			LogUtil.log("Action:员工请假信息查询结束", Level.INFO, null);
		} catch (SQLException e){
			//显示失败
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:查询员工请假信息失败。", Level.SEVERE, null);
		} catch (JSONException je){
			//显示失败
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询员工请假信息失败。", Level.SEVERE, null);
		}
    }

    /**
     * 查询考勤部门名称
     */
    public void getAttendanceDept() {
		LogUtil.log("Action:考勤部门名称查询开始", Level.INFO, null);
		try{
			PageObject pobj = hrCAttendremote.findAttendanceDept(empId, employee.getEnterpriseCode());
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
    public void getOnDutyTime() {
		LogUtil.log("Action:系统月份上，下班时间查询开始", Level.INFO, null);
		try{
			PageObject obj = hrCAttendremote.findSysOnDutyTime(empId, employee.getEnterpriseCode());
			if (obj.getList() == null || obj.getList().size() == 0 ||
					((HrCAttendancestandard)obj.getList().get(0)).getAmBegingTime() == null) {
				PageObject pobj = hrCAttendremote.findAttendanceDept(empId, employee.getEnterpriseCode());
				write(JSONUtil.serialize(pobj));
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
	public void getVacationType() {
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
	public void getHoliday() {
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
	 * 新增操作
	 * @param lstSaveEmpRewardRegisterInfo 新增的数据
	 * @throws SQLException 
	 * @throws DataChangeException 
	 */
	@SuppressWarnings("unchecked")
	public void newVacation(List<HrJVacation> lstSaveHrJVacation) throws SQLException, DataChangeException {
		LogUtil.log("Action:新增员工请假登记开始", Level.INFO, null);
		try {
			Map newMap = setMap();
			// 日期的格式化
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
			Set<String> keys = newMap.keySet();
			Object[] arrayKey = keys.toArray();
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
			LogUtil.log("Action:新增员工请假登记结束", Level.INFO, null);
		}catch (ParseException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:新增员工请假登记失败", Level.SEVERE, e);
		}
	}

	/**
	 * 保存操作
	 */
	@SuppressWarnings("unchecked")
	public void saveVacation() {
		try{
			LogUtil.log("Action:员工请假登记操作开始。", Level.INFO, null);
			 // 新增员工请假登记
			 List<HrJVacation> lstSaveHrJVacation = new ArrayList<HrJVacation>();
			 // 更新员工请假登记
			 List<HrJVacation> lstUpdateHrJVacation = new ArrayList<HrJVacation>();
			 // 删除员工请假登记
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
				 // 删除员工请假登记
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
						 LogUtil.log("Action:员工请假登记操作结束。", Level.INFO, null);
						 write(REPEAT_SUCCESS);
						 return;
					 }
				 }
				 // 判断新增，更新
				 // 新增员工请假登记
				 if (newFlag) {
					 newVacation(lstSaveHrJVacation);
				 // 更新员工请假登记
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
			 LogUtil.log("Action:员工请假登记操作结束。", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:员工请假登记操作失败。", Level.SEVERE, null);
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
			LogUtil.log("Action:员工请假登记操作失败。", Level.SEVERE, null);
			write(Constants.SQL_FAILURE);
		} catch (Exception e) {
			LogUtil.log("Action:员工请假登记操作失败。", Level.SEVERE, null);
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
		LogUtil.log("Action:删除员工请假登记开始", Level.INFO, null);
		HrJVacation entity = new HrJVacation();
		// 员工请假登记实体
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
		LogUtil.log("Action:删除员工请假登记结束", Level.INFO, null);
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
		LogUtil.log("Action:更新员工请假登记开始", Level.INFO, null);
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
					// 员工请假登记实体
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

			LogUtil.log("Action:更新员工请假登记结束", Level.INFO, null);
		} catch (ParseException e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:更新员工请假登记失败", Level.SEVERE, e);
		}
	}
	/**
	 * 设新增值操作
	 * @return 新增数据
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
	 * @param 所需修改的数据
	 * @throws ParseException
	 * @return 修改的数据 
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
	 * @return 时长map
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
			strOutTimes = calculateNums(startTime, endTime, empId, ifWeekend, employee.getEnterpriseCode());
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
			strOutTimes = calculateNums(startTime, strYearEnd, empId, ifWeekend, employee.getEnterpriseCode());
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
					strOutTimes = calculateNums(strYeaStart, strYeaEnd, empId, ifWeekend, employee.getEnterpriseCode());
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
			strOutTimes = calculateNums(strYearStart, endTime, empId, ifWeekend, employee.getEnterpriseCode());
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
	 * 计算请假时长和天数
	 */
	@SuppressWarnings("unchecked")
	public void calculate() {
        try {
            LogUtil.log("Action:计算员工请假时长和天数开始。", Level.INFO, null);
        	String[] strOutTimes = calculateNums(startTime, endTime, empId,
        			ifWeekend, employee.getEnterpriseCode());
        	// 时长
        	if (strOutTimes[0] == null || "".equals(strOutTimes[0])) {
        		strOutTimes[0] = SEPARATOR_ONE_DECIMAL;
        	}
        	// 天数
        	if (strOutTimes[1] == null || "".equals(strOutTimes[1])) {
        		strOutTimes[1] = SEPARATOR_TWO_DECIMAL;
        	}
			String str = SEPARATOR_DAYS+strOutTimes[1] +SEPARATOR_HOURS +
			strOutTimes[0]+SEPARATOR_PARENTHESIS;
            write(str);
            LogUtil.log("Action:计算员工请假时长和天数结束。", Level.INFO, null);
        } catch (SQLException sqle) {
            LogUtil.log("Action:计算员工请假时长和天数失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
        } catch (ParseException e) {
            LogUtil.log("Action:计算员工请假时长和天数失败。", Level.SEVERE, e);
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
	 *  计算请假时长和天数
	 *  @param argStartTime 开始时间
	 *  @param argEndTime 结束时间
	 *  @param argEmpId 员工id
	 *  @param argIfWeekend 是否包含周末
	 *  @return 请假时长和天数
	 * @throws SQLException 
	 * @throws ParseException 
	 * @throws DataChangeException 
	 */
	public String[] calculateNums(String argStartTime, String argEndTime, 
			String argEmpId, String argIfWeekend, String strEnterpriseCode)
	throws SQLException, ParseException, DataChangeException {
		String[] strOutTimes = new String[2];
        LogUtil.log("Action:计算请假时长和天数开始。", Level.INFO, null);
		// 数字输出格式化
		String patternNumber = PATTERN_NUMBER_DAY;
		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
		String patternNum = PATTERN_NUMBER_TIME;
		DecimalFormat dfNum = new DecimalFormat(patternNum);
		// 格式不正确
		if ("".equals(formatDate(argStartTime)) || "".equals(formatDate(argEndTime))){
			return strOutTimes;
		}
		double[] strTimes = new double[2];
		double[] dobTimes = new double[2];
		argStartTime = formatDate(argStartTime);
		argEndTime = formatDate(argEndTime);
		// 开始时间年末
		String strYearEnd = argStartTime.substring(0,4);
		strYearEnd =strYearEnd + YEAR_END;
		// 开始时间
		String strStartTime = argStartTime.substring(0,10);
		// 结束时间
		String strEndTime = argEndTime.substring(0,10);
		// 结束时间年初
		String strYearStart = argEndTime.substring(0,4);
		strYearStart =strYearStart + YEAR_START;
		// 中间时间
		SimpleDateFormat dteForm = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
		Calendar cMiddle = Calendar.getInstance();
		cMiddle.setTime(dteForm.parse(strEndTime));
		cMiddle.add(Calendar.DAY_OF_YEAR, -1);
		Date dateMiddle = cMiddle.getTime();
		String strMid = dteForm.format(dateMiddle);
		// 跨年计算
		if((strEndTime.compareTo(strYearEnd))>0) {
			// 计算开始时间到年末
			strTimes = calculateStart(argEmpId, argStartTime, strYearEnd, argIfWeekend, strEnterpriseCode);
			// 计算中间年度
			dobTimes = calculateYear(argEmpId, strStartTime, strEndTime, argIfWeekend, strEnterpriseCode);
			// 计算中间年度时长
			strTimes[0] += dobTimes[0];
			// 计算中间年度天数
			strTimes[1] += dobTimes[1];
			// 计算结束时间年初到结束时间
			dobTimes = calculateEnd(argEmpId, strYearStart, argEndTime, argIfWeekend, strEnterpriseCode);
			// 计算结束时间时长
			strTimes[0] += dobTimes[0];
			// 计算结束时间天数
			strTimes[1] += dobTimes[1];
		} else {
			// 如果开始日期和结束日期两个相等
			if (strEndTime.equals(strStartTime)) {
				strTimes = calculateSame(argEmpId, argStartTime, argEndTime,strEnterpriseCode);
			} else {
				strTimes = calculateStart(argEmpId, argStartTime, strMid, argIfWeekend, strEnterpriseCode);
				// 计算结束时间
				dobTimes = calculateEnd(argEmpId, argEndTime, argEndTime, argIfWeekend, strEnterpriseCode);
				// 计算结束时间时长
				strTimes[0] += dobTimes[0];
				// 计算结束时间天数
				strTimes[1] += dobTimes[1];
			}
		}
		strOutTimes[0] = dfNum.format(strTimes[0]);
		strOutTimes[1] = dfNumber.format(strTimes[1]);
        LogUtil.log("Action:计算请假时长和天数结束。", Level.INFO, null);
		return strOutTimes;
	}

	/**
	 * 计算开始时间到结束或年末的时长和天数
	 * @param argEmpId 员工id
	 * @param argStartTime 开始时间
	 * @param argDate 结束或年末的日期
	 * @param argIfWeekend 是否包含周末
	 * @param strEnterpriseCode 企业代码
	 * @return 开始时间到结束或年末的时长和天数
	 * @throws SQLException 
	 * @throws DataChangeException 
	 */
	public double[] calculateStart(String argEmpId, String argStartTime,
			String argDate, String argIfWeekend, String strEnterpriseCode) throws SQLException, DataChangeException {
		double[] strStarts = new double[2];
		strStarts[0] =0;
		strStarts[1] =0;
		// 上下班时间数组
		String[] strTimes = new String[4];
		LogUtil.log("Action:计算开始时间到结束或年末的时长和天数开始", Level.INFO, null);
		PageObject obj = hrCAttendremote.findOnDutyTime(argEmpId, argStartTime, strEnterpriseCode);
		if (obj.getList() != null && obj.getList().size() != 0 &&
				((HrCAttendancestandard)obj.getList().get(0)).getAmBegingTime() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getAmEndTime() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getPmBegingTime() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getPmEndTime() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getStandardTime() != null) {
			HrCAttendancestandard entity = (HrCAttendancestandard)obj.getList().get(0);
			// 上午上班时间
			strTimes[0] = formatTime(entity.getAmBegingTime());
			// 上午下班时间
			strTimes[1] = formatTime(entity.getAmEndTime());
			// 下午上班时间
			strTimes[2] = formatTime(entity.getPmBegingTime());
			// 下午下班时间
			strTimes[3] = formatTime(entity.getPmEndTime());
			// 标准出勤时间
			double dobStandardTime = entity.getStandardTime();
			// 开始日期的时间
			String strStartTime = argStartTime.substring(11,argStartTime.length());
			String strStartDate = argStartTime.substring(0,10);
			// 请假时长
			double vacationHours = 0;
			// 请假天数
			double vacationDays = 0;
		
			// 当天请假时长
			vacationHours = calIntraDay(strTimes,true,strStartTime);
			vacationHours +=  getDayTimesNums(strStartDate, argDate, dobStandardTime,
					argIfWeekend, strEnterpriseCode);
			vacationDays = vacationHours/dobStandardTime;
			strStarts[0] = vacationHours;
			strStarts[1] = vacationDays;
		} else {
			throw new DataChangeException(EXCEPTION_MESSAGE);
		}
		LogUtil.log("Action:计算开始时间到结束或年末的时长和天数结束", Level.INFO, null);
		return strStarts;
	}

	/**
	 * 计算中间年度的时长和天数
	 * @param argEmpId 员工id
	 * @param argDate1 开始日期
	 * @param argDate2 结束日期
	 * @param argIfWeekend 是否包含周末
	 * @param strEnterpriseCode 企业代码
	 * @return 中间年度的时长和天数
	 * @throws SQLException 
	 * @throws DataChangeException 
	 */
	@SuppressWarnings("unchecked")
	private double[] calculateYear(String argEmpId, String argDate1, 
			String argDate2, String argIfWeekend,String strEnterpriseCode)
	throws SQLException, DataChangeException {
		LogUtil.log("Action:计算中间年度的时长和天数开始", Level.INFO, null);
		double[] strStarts = new double[2];
		strStarts[0] =0;
		strStarts[1] =0;
		// 请假时长
		double vacationHours = 0;
		// 请假天数
		double vacationDays = 0;

		PageObject obj = hrCAttendremote.findBetYear(argEmpId, argDate1, argDate2, strEnterpriseCode);
		if (obj.getList() != null && obj.getList().size() != 0 &&
				((HrCAttendancestandard)obj.getList().get(0)).getAttendanceYear() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getStandardTime() != null) {
			Iterator<HrCAttendancestandard> it = obj.getList().iterator();
			while (it.hasNext()) {
				HrCAttendancestandard entity = it.next();
				// 中间年度的年份
				String strYear = entity.getAttendanceYear();
				// 标准出勤时间
				double dobStandardTime = entity.getStandardTime();
				// 年初
				String strYearStart = strYear + YEAR_START;
				// 年末
				String strYearEnd = strYear + YEAR_END;
				// 年度请假时长
				double vacationYearHours = 0;
				// 年度请假天数
				double vacationYearDays = 0;
				vacationYearHours +=  getDayTimesNums(strYearStart, strYearEnd,dobStandardTime,
						argIfWeekend, strEnterpriseCode);
				vacationYearDays = vacationYearHours/dobStandardTime;
				vacationHours += vacationYearHours;
				vacationDays += vacationYearDays;
			}
		} else if (obj.getList() != null && obj.getList().size() != 0) {
			throw new DataChangeException(EXCEPTION_MESSAGE);
		}
		strStarts[0] = vacationHours;
		strStarts[1] = vacationDays;
		LogUtil.log("Action:计算中间年度的时长和天数结束", Level.INFO, null);
		return strStarts;
	}

	/**
	 * 计算结束时间到结束或年末的时长和天数
	 * @param argEmpId 员工id
	 * @param argDate 结束或年末的日期
	 * @param argEndTime 结束时间
	 * @param argIfWeekend 是否包含周末
	 * @param strEnterpriseCode 企业代码
	 * @return 开始时间到结束或年末的时长和天数
	 * @throws SQLException 
	 * @throws ParseException 
	 * @throws DataChangeException 
	 */
	public double[] calculateEnd(String argEmpId, String argDate,
			String argEndTime, String argIfWeekend, String strEnterpriseCode)
	throws SQLException, ParseException, DataChangeException {
		LogUtil.log("Action:计算结束时间到结束或年末的时长和天数开始", Level.INFO, null);
		double[] strStarts = new double[2];
		strStarts[0] =0;
		strStarts[1] =0;
		// 中间时间
		String strMiddle = argEndTime.substring(0,10);
		SimpleDateFormat dteForm = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
		Calendar cMiddle = Calendar.getInstance();
		cMiddle.setTime(dteForm.parse(strMiddle));
		cMiddle.add(Calendar.DAY_OF_YEAR, -1);
		Date dateMiddle = cMiddle.getTime();
		String strMid = dteForm.format(dateMiddle);
		// 上下班时间数组
		String[] strTimes = new String[4];

		PageObject obj = hrCAttendremote.findOnDutyTime(argEmpId, argEndTime, strEnterpriseCode);
		if (obj.getList() != null && obj.getList().size() != 0  &&
				((HrCAttendancestandard)obj.getList().get(0)).getAmBegingTime() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getAmEndTime() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getPmBegingTime() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getPmEndTime() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getStandardTime() != null) {
			HrCAttendancestandard entity = (HrCAttendancestandard)obj.getList().get(0);
			// 上午上班时间
			strTimes[0] = formatTime(entity.getAmBegingTime());
			// 上午下班时间
			strTimes[1] = formatTime(entity.getAmEndTime());
			// 下午上班时间
			strTimes[2] = formatTime(entity.getPmBegingTime());
			// 下午下班时间
			strTimes[3] = formatTime(entity.getPmEndTime());
			// 标准出勤时间
			double dobStandardTime = entity.getStandardTime();
			// 结束日期的时间
			String strEndTime = argEndTime.substring(11,argEndTime.length());
			// 请假时长
			double vacationHours = 0;
			// 请假天数
			double vacationDays = 0;
		
			// 当天请假时长
			vacationHours = calIntraDay(strTimes,false,strEndTime);
			if (!argDate.equals(argEndTime.substring(0,10))) {
				vacationHours +=  getDayTimesNums(argDate, strMid,dobStandardTime,
						argIfWeekend, strEnterpriseCode) + dobStandardTime*1;
			}
			vacationDays = vacationHours/dobStandardTime;
			strStarts[0] = vacationHours;
			strStarts[1] = vacationDays;
		} else {
			throw new DataChangeException(EXCEPTION_MESSAGE);
		}

		LogUtil.log("Action:计算结束时间到结束或年末的时长和天数结束", Level.INFO, null);
		return strStarts;
	}

	/**
	 * 计算结束日期和开始日期相等的时长和天数
	 * @param argEmpId 员工id
	 * @param argStartTime 开始时间
	 * @param argEndTime 结束时间
	 * @param strEnterpriseCode 企业代码
	 * @return 结束日期和开始日期相等的时长和天数
	 * @throws SQLException 
	 * @throws DataChangeException 
	 */
	public double[] calculateSame(String argEmpId,String argStartTime,
			String argEndTime, String strEnterpriseCode) throws SQLException, DataChangeException {
		LogUtil.log("Action:计算结束日期和开始日期相等的时长和天数开始", Level.INFO, null);
		double[] strStarts = new double[2];
		strStarts[0] =0;
		strStarts[1] =0;
		// 上下班时间数组
		String[] strTimes = new String[4];

		PageObject obj = hrCAttendremote.findOnDutyTime(argEmpId, argEndTime, strEnterpriseCode);
		if (obj.getList() != null && obj.getList().size() != 0 &&
				((HrCAttendancestandard)obj.getList().get(0)).getAmBegingTime() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getAmEndTime() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getPmBegingTime() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getPmEndTime() != null
				&& ((HrCAttendancestandard)obj.getList().get(0)).getStandardTime() != null) {
			HrCAttendancestandard entity = (HrCAttendancestandard)obj.getList().get(0);
			// 上午上班时间
			strTimes[0] = formatTime(entity.getAmBegingTime());
			// 上午下班时间
			strTimes[1] = formatTime(entity.getAmEndTime());
			// 下午上班时间
			strTimes[2] = formatTime(entity.getPmBegingTime());
			// 下午下班时间
			strTimes[3] = formatTime(entity.getPmEndTime());
			// 标准出勤时间
			double dobStandardTime = entity.getStandardTime();
			// 请假时长
			double vacationHours = 0;
			// 请假天数
			double vacationDays = 0;
		
			// 当天请假时长
			vacationHours = getOneDayTimeNums(strTimes,argStartTime,argEndTime);
			vacationDays = vacationHours/dobStandardTime;
			strStarts[0] = vacationHours;
			strStarts[1] = vacationDays;
		} else {
			throw new DataChangeException(EXCEPTION_MESSAGE);
		}

		LogUtil.log("Action:计算结束日期和开始日期相等的时长和天数结束", Level.INFO, null);
		return strStarts;
	}

	/**
	 * 根据开始天数和结束天数算时长
	 * @param argDate1 开始日期
	 * @param argDate2 结束日期
	 * @param argStandardTime 标准出勤时间
	 * @param argIfWeekend 是否包含周末
	 * @param strEnterpriseCode 企业代码
	 * @return 相距时长
	 * @throws SQLException 
	 */
	private double getDayTimesNums(String argDate1, String argDate2,
			double argStandardTime, String argIfWeekend, String strEnterpriseCode) throws SQLException {
		double result = 0;
		// 当天到结束/年末的天数
		long lngDistanceDays = 0;
		// 节假日天数
		long lngHolidayNums = 0;
		// 非节假日周末上班时间
		long lngHolidayWeekendNums = 0;
		// 中间周末的天数
		int weekNums = 0;
		lngDistanceDays = getDayNums(argDate1 ,argDate2);
		lngHolidayNums = hrCHolidayremote.getHolidayDays(argDate1, argDate2, strEnterpriseCode);
		lngHolidayWeekendNums = hrCHolidayremote.getHolidayWeekendDays(
				argDate1, argDate2, strEnterpriseCode);
		// 包含周末
		if (!IF_WEEKEND.equals(argIfWeekend)) {
			weekNums = getRestDayNums(argDate1,argDate2);
			result = (lngDistanceDays -lngHolidayNums -weekNums + 2*lngHolidayWeekendNums)*argStandardTime;
		} else {
			result = (lngDistanceDays)*argStandardTime;
		}
		return result;
	}

	/**
	 * 格式化时间
	 * @param argTime 所需格式化的时间
	 * @return 所格式化的时间
	 */
	private String formatTime(String argTime) {
		if (argTime != null && !"".equals(argTime)) {
			String[] strTimes = argTime.split(SEPARATOR_COLON);
			// 00:00的场合
			if(strTimes[0].length() < 1) {
				strTimes[0] = SEPARATOR_TWO;
			// 10点以前的场合
			} else if (strTimes[0].length() == 1) {
				strTimes[0] = SEPARATOR_ONE + strTimes[0];
			}
			return strTimes[0] + SEPARATOR_COLON +strTimes[1];
		}
		return "";
	}

	/**
	 * 格式化日期
	 * @param argDate 所需格式化的日期
	 */
	public String formatDate(String argDate) {
		// 正则表达式的形式
		Pattern pattern  = Pattern.compile(REGEX_PATTERN);
		Matcher  matcher  = pattern.matcher(argDate);
		Boolean flag = matcher.matches();
		String result = "";
		if (flag) {
			result = matcher.group(1);
		}
		return result;
	}

	/**
	 * 算当天请假时间
	 * @param argTimes 上班时间
	 * @param argFlag 开始还是结束时间
	 * @param argTime 时间
	 * @return 当天请假时间
	 */
	private double calIntraDay(String[] argTimes, boolean argFlag, String argTime) {
		// 当天请假时长
		double result = 0;
		// 完整时间
		double wholeTime = 0;
		// 休息时间
		double restTime = 0;

		// 开始时间
		if (argFlag) {
			// 在上午上班时间以前
			if((0 >= argTime.compareTo(argTimes[0]))){
				wholeTime= getBetTimeNums(argTimes[0], argTimes[3]);
				restTime = getBetTimeNums(argTimes[1], argTimes[2]);
				result = wholeTime -  restTime;
			}
			// 在上午上班时间内
			if ((0 < argTime.compareTo(argTimes[0])) && (argTime.compareTo(argTimes[1]) <0)) {
				wholeTime= getBetTimeNums(argTime, argTimes[1]);
				restTime = getBetTimeNums(argTimes[2], argTimes[3]);
				result = wholeTime + restTime;
			}
			// 在中间休息时间
			if ((0 <= argTime.compareTo(argTimes[1])) && (argTime.compareTo(argTimes[2]) <=0)) {
				result = getBetTimeNums(argTimes[2], argTimes[3]);
			}
			// 在下午上班时间内
			if ((0 < argTime.compareTo(argTimes[2])) && (argTime.compareTo(argTimes[3]) <0)) {
				result = getBetTimeNums(argTime, argTimes[3]);
			}
			// 在下午下班时间之后
			if ((0 <= argTime.compareTo(argTimes[3]))){
				result = 0;
			}
		} else {
			// 在上午上班时间以前
			if((0 >= argTime.compareTo(argTimes[0]))){
				result = 0;
			}
			// 在上午上班时间内
			if ((0 < argTime.compareTo(argTimes[0])) && (argTime.compareTo(argTimes[1]) <0)) {
				result = getBetTimeNums(argTimes[0], argTime);
			}
			// 在中间休息时间
			if ((0 <= argTime.compareTo(argTimes[1])) && (argTime.compareTo(argTimes[2]) <= 0)) {
				result = getBetTimeNums(argTimes[0], argTimes[1]);
			}
			// 在下午上班时间内
			if ((0 < argTime.compareTo(argTimes[2])) && (argTime.compareTo(argTimes[3]) <0)) {
				wholeTime= getBetTimeNums(argTimes[2], argTime);
				restTime =  getBetTimeNums(argTimes[0], argTimes[1]);
				result = wholeTime + restTime;
			}
			// 在下午下班时间之后
			if ((0 <= argTime.compareTo(argTimes[3]))){
				wholeTime= getBetTimeNums(argTimes[0], argTimes[3]);
				restTime = getBetTimeNums(argTimes[1], argTimes[2]);
				result = wholeTime -  restTime;
			}
		}
		return result;
	}
	/**
	 * 根据开始时间和结束时间算时长
	 * @param argTime1 开始时间
	 * @param argTime2 结束时间
	 * @return 相距时长
	 */
	private static double getBetTimeNums(String argTime1, String argTime2) {
		// 开始时间
		String[] strTime1s = argTime1.split(SEPARATOR_COLON);
		// 结束时间
		String[] strTime2s = argTime2.split(SEPARATOR_COLON);
		//小时
		int intHour = Integer.parseInt(strTime2s[0])- Integer.parseInt(strTime1s[0]); 
		// 分钟
		double intMin = Integer.parseInt(strTime2s[1]) - Integer.parseInt(strTime1s[1]);
		// 相距时长
		return intHour + intMin/60;
	}

	/**
	 * 根据开始日期和结束日期计算在这期间的日的个数<br/>
	 * 包括开始日期和结束日期
	 * @param argDate1 开始日期(字符串形式或日期形式)
	 * @param argDate2 结束日期(字符串形式或日期形式)
	 * @return 日的个数
	 */
	public static long getDayNums(String argDate1, String argDate2) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
		long result = 0;
		try {
			Calendar cStart = Calendar.getInstance();
			cStart.setTime(sdf.parse(argDate1));
			
			Calendar cEnd = Calendar.getInstance();
			cEnd.setTime(sdf.parse(argDate2));
			
			// 两个日期之间相差的天数
			long days = (cEnd.getTimeInMillis() - cStart.getTimeInMillis()) / (1000 * 60 * 60) / 24;
			result = days;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}

	/**
	 * 开始时间和结束时间为同一天的时候，计算请假时间
	 * @param argTimes 上班时间
	 * @param argStartTime 开始时间
	 * @param argEndTime 结束时间
	 * @return 相距时长
	 */
	private double getOneDayTimeNums(String[] argTimes, String argStartTime, String argEndTime) {
		// 存储变换后的开始时间和结束时间
		String sumStartTime = null;
		boolean startTimeChange = false;
		// 开始日期的时间
		String strStartTime = argStartTime.substring(11,argStartTime.length());
		String sumEndTime = null;
		boolean endTimeChage = false;
		// 结束日期的时间
		String strEndTime = argEndTime.substring(11,argEndTime.length());
		/** 开始时间处理 */
		// 在上午上班时间以前
		if ((0 >= strStartTime.substring(0).compareTo(argTimes[0]))) {
			// 上午上班时间
			sumStartTime = argTimes[0];
			startTimeChange = true;
		}
		// 在中午休息时间内（大于上午上班时间，小于下午上班时间）
		if (0 < strStartTime.compareTo(argTimes[1])
				&& 0 > strStartTime.compareTo(argTimes[2])) {
			// 下午上班时间
			sumStartTime = argTimes[2];
			startTimeChange = true;
		}

		// 在下午下班之后
		if (0 <= strStartTime.compareTo(argTimes[3])) {
			// 下午下班时间
			sumStartTime = argTimes[3];
			startTimeChange = true;
		}

		if (!startTimeChange) {
			sumStartTime = strStartTime;
		}

		/** 结束时间处理 */
		// 在上午上班时间以前
		if ((0 >= strEndTime.compareTo(argTimes[0]))) {
			// 上午上班时间
			sumEndTime = argTimes[0];
			endTimeChage = true;
		}

		// 在中午休息时间内（大于上午上班时间，小于下午上班时间）
		if (0 < strEndTime.compareTo(argTimes[1])
				&& 0 > strEndTime.compareTo(argTimes[2])) {
			// 上午下班时间
			sumEndTime = argTimes[1];
			endTimeChage = true;
		}

		// 在下午下班之后
		if (0 <= strEndTime.compareTo(argTimes[3])) {
			// 下午下班时间
			sumEndTime = argTimes[3];
			endTimeChage = true;
		}

		if (!endTimeChage) {
			sumEndTime = strEndTime;
		}

		/** 返回值处理 */
		// 开始时间在结束时间之后
		if (sumStartTime.compareTo(sumEndTime) >= 0) {
			return 0;
		}

		// 当开始时间和结束时间都在上午的时候
		if (sumStartTime.compareTo(argTimes[1]) <= 0
				&& sumEndTime.compareTo(argTimes[1]) <= 0) {
			return getBetTimeNums(sumStartTime, sumEndTime);
		}

		// 当开始时间和结束时间都在下午的时候
		if (sumStartTime.compareTo(argTimes[2]) >= 0
				&& sumEndTime.compareTo(argTimes[2]) >= 0) {
			return getBetTimeNums(sumStartTime, sumEndTime);
		}

		// 当开始时间在上午，但是结束时间在下午的时候
		if (sumStartTime.compareTo(argTimes[1]) <= 0
				&& sumEndTime.compareTo(argTimes[2]) >= 0) {
			return getBetTimeNums(sumStartTime, sumEndTime)
					- getBetTimeNums(argTimes[1], argTimes[2]);
		}
		
		return 0;
	}

	/**
	 * 根据开始日期和结束日期计算在这期间的休息日的个数<br/>
	 * 包括开始日期和结束日期
	 * @param argDate1 开始日期(字符串形式或日期形式)
	 * @param argDate2 结束日期(字符串形式或日期形式)
	 * @return 休息日的个数
	 */
	public static int getRestDayNums(String argDate1, String argDate2) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
		int result = 0;
		try {
			result = getRestDayNums(sdf.parse(argDate1), sdf.parse(argDate2));
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}

	/**
	 * 根据开始日期和结束日期计算在这期间的休息日的个数<br/>
	 * 包括开始日期和结束日期
	 * @param argDate1 开始日期(字符串形式或日期形式)
	 * @param argDate2 结束日期(字符串形式或日期形式)
	 * @return 休息日的个数
	 */
	public static int getRestDayNums(Date argDate1, Date argDate2) {
		int result = 0;
		Calendar cStart = Calendar.getInstance();
		cStart.setTime(argDate1);
		
		Calendar cEnd = Calendar.getInstance();
		cEnd.setTime(argDate2);
		
		// 两个日期之间相差的天数
		long days = 1 + (cEnd.getTimeInMillis() - cStart.getTimeInMillis()) / (1000 * 60 * 60) / 24;
		result = ((int) days) / 7 * 2;
		int rest = ((int) days) % 7;
		
		if (rest == 1) {
			if (getRestDay(cStart, rest, 1) > 0 || getRestDay(cEnd, rest, -1) > 0) {
				result++;
			}
		} else if (rest > 1) {
			result += Math.max(getRestDay(cStart, rest, 1), getRestDay(cEnd, rest, -1));
		}
		
		return result;
	}

	/**
	 * 得到休息天数
	 * @param argDate 日期
	 * @param limit 限制
	 * @param step 步骤
	 * @return 休息天数
	 */
	private static int getRestDay(Calendar argDate, int limit, int step) {
		int result = 0;
		Calendar date = (Calendar) argDate.clone();
		
		while (limit > 0) {
			if (isRestDay(date)) {
				result ++;
			}
			
			date.add(Calendar.DAY_OF_YEAR, step);
			limit --;
		}
		return result;
	}
	
	/**
	 * 判断日期是否是周未
	 * @param argDate 日期
	 * @return 周未标志符
	 */
	public static boolean isRestDay(Calendar argDate) {
		boolean result = false;
		int day = argDate.get(Calendar.DAY_OF_WEEK);
		
		// 判断日期是否是周未
		result = day == 1 || day == 7;
		return result;
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
