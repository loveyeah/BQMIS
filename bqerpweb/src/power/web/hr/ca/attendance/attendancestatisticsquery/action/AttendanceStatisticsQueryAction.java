/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.ca.attendance.attendancestatisticsquery.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.AttendanceStatisticsQueryFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.ExportXsl;
import power.web.comm.WriteXls;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 考勤统计查询Action
 * 
 * @author fangjihu
 * 
 */
@SuppressWarnings("serial")
public class AttendanceStatisticsQueryAction extends AbstractAction{

	/** 远程接口 */
	protected AttendanceStatisticsQueryFacadeRemote remote; 
	/** 部门id */
	private String deptId;
	/** 考勤年份 */
	private String year;
	/** 考勤月份 */
	private String month;
	/** 开始年月 */
	private String yearMonth;
	/** 审批状态 */
	private String signState;
	/** 类别 */
	private String type;
	
	private static String ONE = "1";
	private static String TWO = "2";
	private static String WORK_ID_DEPTLEAVE ="KQ008DEPTLEAVE";
	private static String WORK_NAME_DEPTLEAVE = "部门请假单";
	private static String WORK_ID_DEPTONDUTY ="KQ008DEPTONDUTY";
	private static String WORK_NAME_DEPTONDUTY = "部门出勤统计";
	private static String FILE_NAME_LEAVE ="部门职工请假统计";
	private static String FILE_NAME_WORKOVERTIME ="部门职工加班统计";
	private static String FILE_NAME_WORKSHIFT ="部门职工运行班统计";
	  
	public AttendanceStatisticsQueryAction(){
		// 获取远程接口
		remote = (AttendanceStatisticsQueryFacadeRemote)factory.getFacadeRemote("AttendanceStatisticsQueryFacade");
	}

	/**
	 * 部门出勤统计查询
	 */
	public void getDeptOndutyStatisticsQueryInfo(){
		try {
			LogUtil.log("Action:部门出勤统计查询开始。", Level.INFO, null);
			// 查询
			PageObject object = remote.findDeptOndutyStatisticsQueryInfo(
					deptId, year, month, employee.getEnterpriseCode());
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:部门出勤统计查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:部门出勤统计查询失败。", Level.SEVERE, e);
		}
	}
	
	/**
	 * 部门请假单查询
	 */
	public void getDeptleaveStatisticsQueryInfo() {

		try {
			LogUtil.log("Action:部门请假单查询开始。", Level.INFO, null);
			// 查询
			PageObject object = remote.findDeptleaveStatisticsQueryInfo(deptId,
					yearMonth, signState, employee.getEnterpriseCode());

			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:部门请假单查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:部门请假单查询失败。", Level.SEVERE, e);
		}
	}
	
	/**
	 * 部门请假单数据导出
	 */
	@SuppressWarnings("unchecked")
	public void exportDeptleaveStatisticsQueryInfo(){

		LogUtil.log("Action:部门请假单数据导出开始。", Level.INFO, null);
		// 查询结果
		PageObject object = new PageObject();
		object = remote.findDeptleaveStatisticsQueryInfo(deptId, yearMonth, signState, employee.getEnterpriseCode());
		// 操作excel
		WriteXls xls = new WriteXls(Constants.BLANK_STRING, new ArrayList());
		// 生成
		xls = new WriteXls(WORK_ID_DEPTLEAVE, object.getList());
		// 设置机能名
		xls.setBusinessName(WORK_NAME_DEPTLEAVE);
		try {
			// 导出
			xls.xlsExportFile(response);
			LogUtil.log("Action:部门请假单数据导出结束。", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("Action:部门请假单数据导出失败。", Level.SEVERE, e);
			write(Constants.IO_FAILURE);
		}
	}
	
	/**
	 * 部门出勤统计数据导出
	 */
	@SuppressWarnings("unchecked")
	public void exportDeptOndutyStatisticsQueryInfo(){

		LogUtil.log("Action:部门请假单数据导出开始。", Level.INFO, null);
		// 查询结果
		PageObject object = new PageObject();
		object = remote.findDeptOndutyStatisticsQueryInfo(deptId, year, month, employee.getEnterpriseCode());
		// 操作excel
		WriteXls xls = new WriteXls(Constants.BLANK_STRING, new ArrayList());
		// 生成
		xls = new WriteXls(WORK_ID_DEPTONDUTY, object.getList());
		// 设置机能名
		xls.setBusinessName(WORK_NAME_DEPTONDUTY);
		try {
			// 导出
			xls.xlsExportFile(response);
			LogUtil.log("Action:部门请假单数据导出结束。", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("Action:部门请假单数据导出失败。", Level.SEVERE, e);
			write(Constants.IO_FAILURE);
		}
	}
	
	/**
	 * 请假统计数据导出
	 */
	@SuppressWarnings("unchecked")
	public void exportLeaveStatisticsQueryInfo(){

        LogUtil.log("Action:导出文件开始", Level.INFO, null);
        try{
        	PageObject obj = new PageObject();
        	if(ONE.equals(type)){
        		obj = remote.findLeaveStatisticsQueryInfo(year, month, employee.getEnterpriseCode());
        	}else if(TWO.equals(type)){
        		obj = remote.findWorkOvertimeStatisticsQueryInfo(year, month, employee.getEnterpriseCode());	
        	}else{
        		obj = remote.findWorkshiftStatisticsQueryInfo(year, month,  employee.getEnterpriseCode());	
        	}
        	List list = obj.getList();
            // 操作excel文件对象
            ExportXsl exsl = new ExportXsl();
            // 设置response
            exsl.setResponse(response);
            // 当前时间作为文件名以部分
            Date dte = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String strDate = sdf.format(dte);
            // 设定文件名
            String fileName ="";
            if(ONE.equals(type)){
        		fileName =FILE_NAME_LEAVE;
        	}else if(TWO.equals(type)){
        		fileName = FILE_NAME_WORKOVERTIME;
        	}else{
        		fileName = FILE_NAME_WORKSHIFT;
        	}
            exsl.setStrFileName(fileName + strDate);
            // excel文件每列标题头
            List<String> lstHeader = new ArrayList<String>();
            lstHeader = (List<String>)list.get(list.size()-1);
            exsl.setLstTitle(lstHeader);
            // excel文件中的一行
            @SuppressWarnings("unused")
			List<String> lstRow = null;
            // excel文件中的所有行集合
            List lstRowSet = new ArrayList();
            for(int i =0;i<list.size()-1;i++){
            	lstRowSet.add(list.get(i));
            }
            // 设置所有行内容
            exsl.setLstRow(lstRowSet);
            // 创建导出excel文件
            exsl.createXsl();
            LogUtil.log("Action:导出文件结束", Level.INFO, null);
        }catch (Exception e) {
            LogUtil.log("Action:导出文件失败。", Level.SEVERE, e);
        }
	}
	
	
	/**
	 * @return the deptId
	 */
	public String getDeptId() {
		return deptId;
	}

	/**
	 * @param deptId the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}
	
	

	/**
	 * @return the yearMonth
	 */
	public String getYearMonth() {
		return yearMonth;
	}

	/**
	 * @param yearMonth the yearMonth to set
	 */
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	/**
	 * @return the signState
	 */
	public String getSignState() {
		return signState;
	}

	/**
	 * @param signState the signState to set
	 */
	public void setSignState(String signState) {
		this.signState = signState;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
