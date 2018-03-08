/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.web.hr.ca.attendance.attendancestandard.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.AttendanceStandard;
import power.ejb.hr.ca.AttendanceStandardFacadeRemote;
import power.ejb.hr.ca.HrCAttendancedep;
import power.ejb.hr.ca.HrCAttendancedepFacadeRemote;
import power.ejb.hr.ca.HrCAttendancestandard;
import power.ejb.hr.ca.HrCAttendancestandardFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 考勤标准维护
 * @author chen shoujiang
 *  
 */
public class AttendanceStandardAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	/** 日期格式 */
	private static final String DATE_FORMAT_YYYYMMDD="yyyy-MM-dd";
	/** 根节点默认id */
	private static final String STR_ROOT_ID = "0";
	/** 返回前台的空值*/
	private static final String STR_NULL_NODE = "[]";
	/** 月份的长度1 */
	private static final int STR_MONTH_1 = 1;
	/** 月份的长度2 */
	private static final int STR_MONTH_2 = 2;
	/** 月份前面添加的0 */
	private static final String STR_MONTH_0 = "0";
	/** 保存时是否包含子节点Y */
	private static final String STR_INCLUDE_Y = "1";
	/** 是否是根节点N */
	private static final String STR_ROOT_N = "N";
	/** 是否是根节点N */
	private static final String STR_ROOT_Y = "Y";
	 /** 查询结果为null,传到前台的string */
    private static String STR_JSON_NULL = "{\"list\":[],\"totalCount\":0}";
	/**考勤标准维护(自定义)Remote*/
	private AttendanceStandardFacadeRemote attendanceStandardFacadeRemote;
	/**考勤部门维护表*/
	HrCAttendancedepFacadeRemote hrcAttendancedepFacadeRemote;
	/**考勤标准维护表*/
	private HrCAttendancestandardFacadeRemote hrcAttendancestandardFacadeRemote;
	/**上级部门ID */
	private String node;
	/**考勤年份*/
	private String attendanceYear;
	/**考勤部门ID*/
	private String attendanceDeptId;
	/** 考勤部门名称 */
	private String attendanceDeptName;
	/**批量处理记录*/
	private String records;
	/** 是否包含子部门*/
	private String flag;
	/**是否是根节点*/
	private String isRoot;
	
	/**
	 * 构造函数
	 */
	public AttendanceStandardAction() {
		// 考勤标准维护(自定义)Remote
		attendanceStandardFacadeRemote = (AttendanceStandardFacadeRemote) factory
			.getFacadeRemote("AttendanceStandardFacade");
		// 考勤部门维护表
		hrcAttendancedepFacadeRemote = (HrCAttendancedepFacadeRemote)(Ejb3Factory.getInstance())
			.getFacadeRemote("HrCAttendancedepFacade");
		// 考勤标准维护表
		hrcAttendancestandardFacadeRemote = (HrCAttendancestandardFacadeRemote)(Ejb3Factory.getInstance())
			.getFacadeRemote("HrCAttendancestandardFacade");
	}

	/**
	 * 查询考勤部门维护信息
	 * @throws JSONException
	 * @throws Exception
	 */
	public void getAttendanceDeptInfo() throws JSONException, Exception {
		try{
			LogUtil.log("Action:查询考勤部门维护信息开始", Level.INFO, null);
			// 如果节点为空或者“”就设置为-1
			if (node == null || Constants.BLANK_STRING.equals(node)) {
				node = STR_ROOT_ID;
			}
			// 查询考勤部门维护信息
			List<TreeNode> list = hrcAttendancedepFacadeRemote
					.getDeptsByTopDeptid(Long.parseLong(node), employee.getEnterpriseCode());
			// 如果查询到的数据非空的话，就返回页面
			if (list != null && list.size() > 0) {
				LogUtil.log("Action:查询考勤部门维护信息结束", Level.INFO, null);
				super.write(JSONUtil.serialize(list));
			} else {
				// 将部门设置表里的数据插入到考勤部门维护表
				attendanceStandardFacadeRemote.moveDeptData(employee.getWorkerCode(), employee.getEnterpriseCode(), "2");
				// 查询考勤部门维护信息
				List<TreeNode> listTree = hrcAttendancedepFacadeRemote
						.getDeptsByTopDeptid(Long.parseLong(node),
								employee.getEnterpriseCode());
				// 如果查询到的数据非空的话，就返回页面
				if (listTree != null) {
					LogUtil.log(" Action:查询考勤部门维护信息结束", Level.INFO, null);
					super.write(JSONUtil.serialize(listTree));
				} else {
					LogUtil.log(" Action:查询考勤部门维护信息结束", Level.INFO, null);
					// 否则设为空值
					super.write(STR_NULL_NODE);
				}
				LogUtil.log("Action:查询考勤部门维护信息结束", Level.INFO, null);
			}
		} catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
            super.write(STR_NULL_NODE);
			LogUtil.log("Action:查询查询考勤部门维护信息失败", Level.SEVERE, null);
		} catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            super.write(STR_NULL_NODE);
            LogUtil.log("Action:查询查询考勤部门维护信息失败", Level.SEVERE, null);
		}
	}

	/**
	 * 查询考勤标准维护信息
	 * @throws JSONException
	 * @throws Exception
	 */
	public void getAttendanceStandardInfo() throws JSONException, Exception {
		try{
			LogUtil.log("Action:查询考勤标准维护信息开始", Level.INFO, null);
			// 分页信息
			PageObject obj = new PageObject();
			// 无分页信息时执行
			obj = attendanceStandardFacadeRemote.getAttendanceStandard(attendanceYear, attendanceDeptId,
					attendanceDeptName,isRoot,
					employee.getEnterpriseCode());
			// 输出
			String strOutput = Constants.BLANK_STRING;
			//　要是查询结果不为空的话，就赋值
			if(obj.getList() != null && obj.getList().size() > 0) {
				strOutput = JSONUtil.serialize(obj);
			} else {
				// 否则设为空值
				strOutput = STR_JSON_NULL;
			}
			write(strOutput);
			LogUtil.log("Action:查询考勤标准维护信息结束", Level.INFO, null);
		} catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询考勤标准维护信息失败", Level.SEVERE, null);
		} catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:查询考勤标准维护信息失败", Level.SEVERE, null);
            throw sqle;
		}
	}
	
	/**
	 * 批处理时
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void saveAttendanceStandard() throws Exception {
		try {
			LogUtil.log("Action:批量新增劳动合同开始。", Level.INFO, null);
	        List<HrCAttendancestandard> list = new ArrayList<HrCAttendancestandard>();
	        // 获取前台传来的所有员工信息
	        List<Map> attendanceDatas = (List<Map>) JSONUtil.deserialize(records);
	     // 循环赋值员工信息添加到list
	        for (int i = 0; i < attendanceDatas.size(); i++) {
	        	HrCAttendancestandard attendanceBean = new HrCAttendancestandard();
	            Map<Object, Object> info = attendanceDatas.get(i);
	            // 考勤标准ID
	            if (info.get("attendancestandardid") != null && !Constants.BLANK_STRING.equals(info.get("attendancestandardid")))
	            	attendanceBean =hrcAttendancestandardFacadeRemote.findById(Long.parseLong(info.get("attendancestandardid").toString()));
	            // 考勤年份
	            if (attendanceDatas.get(0).get("attendanceYear") != null && !Constants.BLANK_STRING.equals(attendanceDatas.get(0).get("attendanceYear")))
	            	attendanceBean.setAttendanceYear(attendanceDatas.get(0).get("attendanceYear").toString());
	            // 考勤月份
	            if (info.get("attendanceMonth") != null && !Constants.BLANK_STRING.equals(info.get("attendanceMonth"))){
	            	if(info.get("attendanceMonth").toString().length() == STR_MONTH_1) {
	            		attendanceBean.setAttendanceMonth(STR_MONTH_0+info.get("attendanceMonth").toString());
	            	}else if(info.get("attendanceMonth").toString().length() == STR_MONTH_2) {
	            		attendanceBean.setAttendanceMonth(info.get("attendanceMonth").toString());
	            	}
	            }
	            // 考勤部门ID
	            if (attendanceDatas.get(0).get("attendanceDeptId") != null && !Constants.BLANK_STRING.equals(attendanceDatas.get(0).get("attendanceDeptId")))
	            	attendanceBean.setAttendanceDeptId(Long.parseLong(attendanceDatas.get(0).get("attendanceDeptId").toString()));
	            // 开始日期
	            if (info.get("startDate") != null && !Constants.BLANK_STRING.equals(info.get("startDate")))
	            	attendanceBean.setStartDate(formatStringToDate(info.get("startDate").toString(),DATE_FORMAT_YYYYMMDD));
	            // 结束日期
	            if (info.get("endDate") != null && !Constants.BLANK_STRING.equals(info.get("endDate")))
	            	attendanceBean.setEndDate(formatStringToDate(info.get("endDate").toString(),DATE_FORMAT_YYYYMMDD));
	            // 标准天数
	            if (info.get("standardDay") != null && !Constants.BLANK_STRING.equals(info.get("standardDay"))){
	            	attendanceBean.setStandardDay(Double.parseDouble(info.get("standardDay").toString()));
	            }
	            else 
	            	attendanceBean.setStandardDay(null);
	            // 上午上班时间
	            if (info.get("amBegingTime") != null && !Constants.BLANK_STRING.equals(info.get("amBegingTime")))
	            	attendanceBean.setAmBegingTime(info.get("amBegingTime").toString());
	            else attendanceBean.setAmBegingTime(Constants.BLANK_STRING);
	            // 上午上班时间
	            if (info.get("amEndTime") != null && !Constants.BLANK_STRING.equals(info.get("amEndTime")))
	            	attendanceBean.setAmEndTime(info.get("amEndTime").toString());
	            else attendanceBean.setAmEndTime(Constants.BLANK_STRING);
	            // 下午上班时间
	            if (info.get("pmBegingTime") != null && !Constants.BLANK_STRING.equals(info.get("pmBegingTime")))
	            	attendanceBean.setPmBegingTime(info.get("pmBegingTime").toString());
	            else attendanceBean.setPmBegingTime(Constants.BLANK_STRING);
	            // 下午下班时间
	            if (info.get("pmEndTime") != null && !Constants.BLANK_STRING.equals(info.get("pmEndTime")))
	            	attendanceBean.setPmEndTime(info.get("pmEndTime").toString());
	            else attendanceBean.setPmEndTime(Constants.BLANK_STRING);
	            // 标准出勤时间
//	            if (attendanceDatas.get(0).get("standardTime") != null && !Constants.BLANK_STRING.equals(attendanceDatas.get(0).get("standardTime")))
//	            	attendanceBean.setStandardTime(Double.parseDouble(attendanceDatas.get(0).get("standardTime").toString()));
	            if (info.get("standardTime") != null && !Constants.BLANK_STRING.equals(info.get("standardTime")))
	            	attendanceBean.setStandardTime(Double.parseDouble(info.get("standardTime").toString()));
	            else attendanceBean.setStandardTime(null);
	            // 上次修改人
	            attendanceBean.setLastModifiyBy(employee.getWorkerCode());
	            // 加入list
	            list.add(attendanceBean);
	        }
	        if(flag != null && !flag.equals(Constants.BLANK_STRING)) {
				if(flag.equals(STR_INCLUDE_Y)) {
					List<HrCAttendancedep> listDept = hrcAttendancedepFacadeRemote.findAllChildrenDeptInfo(attendanceDatas.get(0).get("attendanceDeptId").toString(), employee.getEnterpriseCode());
					if(listDept != null && listDept.size() > 0) {
						for(int i =0 ; i < listDept.size(); i++) {
								PageObject obj = new PageObject();
								// 无分页信息时执行
								obj = attendanceStandardFacadeRemote.getAttendanceStandard(list.get(0).getAttendanceYear(),
										listDept.get(i).getAttendanceDeptId().toString(),Constants.BLANK_STRING,STR_ROOT_N,employee.getEnterpriseCode());
								if(obj != null && obj.getList() != null && obj.getList().size() > 0) {
									List<AttendanceStandard> arrList = obj.getList();
									for(int k = 0 ; k < arrList.size(); k++) {
										AttendanceStandard bean = new AttendanceStandard();
										bean = arrList.get(k);
										HrCAttendancestandard attendance = new HrCAttendancestandard();
										if(bean.getAttendancestandardid() != null && !bean.getAttendancestandardid().equals(Constants.BLANK_STRING)) {
											attendance = hrcAttendancestandardFacadeRemote.findById(Long.parseLong(bean.getAttendancestandardid()));
										}else{
											 // 考勤年份
								            if (list.get(k).getAttendanceYear()!= null && !Constants.BLANK_STRING.equals(list.get(k).getAttendanceYear().toString()))
								            	attendance.setAttendanceYear(list.get(k).getAttendanceYear());
								            // 考勤月份
								            if (list.get(k).getAttendanceMonth() != null && !Constants.BLANK_STRING.equals(list.get(k).getAttendanceMonth().toString()))
								            	attendance.setAttendanceMonth(list.get(k).getAttendanceMonth());
								            // 考勤部门ID
							            	attendance.setAttendanceDeptId(listDept.get(i).getAttendanceDeptId());
										}
										if(isRoot.equals(STR_ROOT_Y)) {
								            // 开始日期
								            if (list.get(k).getStartDate()!= null && !Constants.BLANK_STRING.equals(list.get(k).getStartDate().toString()))
								            	attendance.setStartDate(list.get(k).getStartDate());
								            // 结束日期
								            if (list.get(k).getEndDate()!= null && !Constants.BLANK_STRING.equals(list.get(k).getEndDate().toString()))
								            	attendance.setEndDate(list.get(k).getEndDate());
								            // 标准天数
								            if (list.get(k).getStandardDay() != null && !Constants.BLANK_STRING.equals(list.get(k).getStandardDay().toString()))
								            	attendance.setStandardDay(list.get(k).getStandardDay());
								            else attendance.setStandardDay(null);
										}
							            // 上午上班时间
							            if (list.get(k).getAmBegingTime() != null && !Constants.BLANK_STRING.equals(list.get(k).getAmBegingTime().toString()))
							            	attendance.setAmBegingTime(list.get(k).getAmBegingTime());
							            else attendance.setAmBegingTime(Constants.BLANK_STRING);
							            // 上午上班时间
							            if (list.get(k).getAmEndTime()!= null && !Constants.BLANK_STRING.equals(list.get(k).getAmEndTime().toString()))
							            	attendance.setAmEndTime(list.get(k).getAmEndTime());
							            else attendance.setAmEndTime(Constants.BLANK_STRING);
							            // 下午上班时间
							            if (list.get(k).getPmBegingTime() != null && !Constants.BLANK_STRING.equals(list.get(k).getPmBegingTime().toString()))
							            	attendance.setPmBegingTime(list.get(k).getPmBegingTime());
							            else attendance.setPmBegingTime(Constants.BLANK_STRING);
							            // 下午下班时间
							            if (list.get(k).getPmEndTime()!= null && !Constants.BLANK_STRING.equals(list.get(k).getPmEndTime().toString()))
							            	attendance.setPmEndTime(list.get(k).getPmEndTime());
							            else attendance.setPmEndTime(Constants.BLANK_STRING);
							            // 标准出勤时间
							            if (list.get(k).getStandardTime() != null && !Constants.BLANK_STRING.equals(list.get(k).getStandardTime().toString()))
							            	attendance.setStandardTime(list.get(k).getStandardTime());
							            else attendance.setStandardTime(null);
							            // 上次修改人
							            attendance.setLastModifiyBy(employee.getWorkerCode());
							            list.add(attendance);
									}
								}
							}
					}
				}else {
					// 如果是根节点
					if(isRoot.equals(STR_ROOT_Y)) {
						List<HrCAttendancedep> listDept = hrcAttendancedepFacadeRemote.findAllChildrenDeptInfo(attendanceDatas.get(0).get("attendanceDeptId").toString(), employee.getEnterpriseCode());
						if(listDept != null && listDept.size() > 0) {
							for(int i =0 ; i < listDept.size(); i++) {
									PageObject obj = new PageObject();
									// 无分页信息时执行
									obj = attendanceStandardFacadeRemote.getAttendanceStandard(list.get(0).getAttendanceYear(),
											listDept.get(i).getAttendanceDeptId().toString(),Constants.BLANK_STRING,STR_ROOT_N,employee.getEnterpriseCode());
									if(obj != null && obj.getList() != null && obj.getList().size() > 0) {
										List<AttendanceStandard> arrList = obj.getList();
										for(int k = 0 ; k < arrList.size(); k++) {
											AttendanceStandard bean = new AttendanceStandard();
											bean = arrList.get(k);
											HrCAttendancestandard attendance = new HrCAttendancestandard();
											if(bean.getAttendancestandardid() != null && !bean.getAttendancestandardid().equals("")) {
												attendance = hrcAttendancestandardFacadeRemote.findById(Long.parseLong(bean.getAttendancestandardid()));
											}else{
												 // 考勤年份
									            if (list.get(k).getAttendanceYear()!= null && !Constants.BLANK_STRING.equals(list.get(k).getAttendanceYear().toString()))
									            	attendance.setAttendanceYear(list.get(k).getAttendanceYear());
									            // 考勤月份
									            if (list.get(k).getAttendanceMonth() != null && !Constants.BLANK_STRING.equals(list.get(k).getAttendanceMonth().toString()))
									            	attendance.setAttendanceMonth(list.get(k).getAttendanceMonth());
									            // 考勤部门ID
								            	attendance.setAttendanceDeptId(listDept.get(i).getAttendanceDeptId());
											}
								            // 开始日期
								            if (list.get(k).getStartDate()!= null && !Constants.BLANK_STRING.equals(list.get(k).getStartDate().toString()))
								            	attendance.setStartDate(list.get(k).getStartDate());
								            // 结束日期
								            if (list.get(k).getEndDate()!= null && !Constants.BLANK_STRING.equals(list.get(k).getEndDate().toString()))
								            	attendance.setEndDate(list.get(k).getEndDate());
								            // 标准天数
								            if (list.get(k).getStandardDay() != null && !Constants.BLANK_STRING.equals(list.get(k).getStandardDay().toString()))
								            	attendance.setStandardDay(list.get(k).getStandardDay());
								            else attendance.setStandardDay(null); 
								            // 上次修改人
								            attendance.setLastModifiyBy(employee.getWorkerCode());
								            list.add(attendance);
										}
									}
								}
						}
					}
				}
	        }
	        hrcAttendancestandardFacadeRemote.saveBat(list, employee.getEnterpriseCode(),flag,employee.getWorkerCode());
	        write(Constants.ADD_SUCCESS);
	        LogUtil.log("Action:批量新增劳动合同结束。", Level.INFO, null);
		} catch (RuntimeException e) {
            // ejb失败
            LogUtil.log("Action:批量新增劳动合同失败。", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
        } catch (JSONException e) {
            // 格式化失败
            LogUtil.log("Action:批量新增劳动合同失败。", Level.SEVERE, null);
            write(Constants.DATA_FAILURE);
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

		try {
			sdfFrom = new SimpleDateFormat(argFormat);
			// 格式化日期
			dtresult = sdfFrom.parse(argDateStr);
		} catch (Exception e) {
			dtresult = null;
		} finally {
			sdfFrom = null;
		}

		return dtresult;
	}
	/**
	 * @return the node
	 */
	public String getNode() {
		return node;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(String node) {
		this.node = node;
	}
	
	/**
	 * @return the attendanceYear
	 */
	public String getAttendanceYear() {
		return attendanceYear;
	}

	/**
	 * @param attendanceYear the attendanceYear to set
	 */
	public void setAttendanceYear(String attendanceYear) {
		this.attendanceYear = attendanceYear;
	}

	/**
	 * @return the attendanceDeptId
	 */
	public String getAttendanceDeptId() {
		return attendanceDeptId;
	}

	/**
	 * @param attendanceDeptId the attendanceDeptId to set
	 */
	public void setAttendanceDeptId(String attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}
	/**
	 * @return the attendanceDeptName
	 */
	public String getAttendanceDeptName() {
		return attendanceDeptName;
	}

	/**
	 * @param attendanceDeptName the attendanceDeptName to set
	 */
	public void setAttendanceDeptName(String attendanceDeptName) {
		this.attendanceDeptName = attendanceDeptName;
	}
	
	/**
	 * @return the records
	 */
	public String getRecords() {
		return records;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(String records) {
		this.records = records;
	}
	


	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * @return the isRoot
	 */
	public String getIsRoot() {
		return isRoot;
	}

	/**
	 * @param isRoot the isRoot to set
	 */
	public void setIsRoot(String isRoot) {
		this.isRoot = isRoot;
	}

}
