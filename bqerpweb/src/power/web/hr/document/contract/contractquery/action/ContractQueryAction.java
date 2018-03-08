/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.contract.contractquery.action;

import java.util.ArrayList;
import java.util.logging.Level;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ContractQueryFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.WriteXls;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 合同台帐查询Action
 * 
 * @author jincong
 * @version 1.0
 */
public class ContractQueryAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 合同台帐查询Remote */
	private ContractQueryFacadeRemote queryRemote;
	/** 开始行 */
	private int start;
	/** 查询行 */
	private int limit;
	/** 开始时间 */
	private String startDate;
	/** 结束时间 */
	private String endDate;
	/** 部门编码 */
	private String deptCode;
	/** 变更前部门编码 */
	private String deptBeforeCode;
	/** 变更后部门编码 */
	private String deptAfterCode;
	/** 合同有效期 */
	private String contractTerm;
	/** 合同形式 */
	private String contractType;
	/** 终止类别 */
	private String stopType;
	/** 合同到期月份 */
	private String duetoTime;
	/** 导出Flag */
	private String flag;
	
	// 常量
	/** 员工合同 */
	public static String EXPORT_EMPLOYEE = "EMPLOYEE";
	public static String WORK_ID_EMPLOYEE = "PD021EMPLOYEE";
	public static String WORK_NAME_EMPLOYEE = "劳动合同台帐";
	/** 续签合同 */
	public static String EXPORT_CONTINUE = "CONTINUE";
	public static String WORK_ID_CONTINUE = "PD021CONTINUE";
	public static String WORK_NAME_CONTINUE = "续签合同台帐";
	/** 变更合同 */
	public static String EXPORT_CHANGE = "CHANGE";
	public static String WORK_ID_CHANGE = "PD021CHANGE";
	public static String WORK_NAME_CHANGE = "合同变更台帐";
	/** 终止合同 */
	public static String EXPORT_STOP = "STOP";
	public static String WORK_ID_STOP = "PD021STOP";
	public static String WORK_NAME_STOP = "合同终止台帐";
	/** 到期合同 */
	public static String EXPORT_DUEDATE = "DUEDATE";
	public static String WORK_NAME_DUEDATE = "到期劳动合同台帐";
	/** 未签合同 */
	public static String EXPORT_NOTSIGN = "NOTSIGN";
	public static String WORK_ID_NOTSIGN = "PD021NOTSIGN";
	public static String WORK_NAME_NOTSIGN = "未签合同员工一览";

	/**
	 * 构造函数
	 */
	public ContractQueryAction() {
		// 接口取得
		queryRemote = (ContractQueryFacadeRemote) factory
				.getFacadeRemote("ContractQueryFacade");
	}

	/**
	 * 查询所有劳动合同有效期维护记录
	 */
	public void getContractTermQuery() {
		try {
			LogUtil.log("Action:劳动合同有效期维护查询开始。", Level.INFO, null);
			// 查询
			PageObject object = queryRemote
					.findContractTerm(employee.getEnterpriseCode());
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:劳动合同有效期维护查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:劳动合同有效期维护查询失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 员工合同查询
	 */
	public void getContractQueryEmployee() {
		try {
			LogUtil.log("Action:员工合同查询开始。", Level.INFO, null);
			// 查询
			PageObject object = queryRemote.getContractQueryEmployee(startDate,
					endDate, deptCode, contractTerm, contractType, null, employee
							.getEnterpriseCode(), start, limit);
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:员工合同查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:员工合同查询失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 续签合同查询
	 */
	public void getContractQueryContinue() {
		try {
			LogUtil.log("Action:续签合同查询开始。", Level.INFO, null);
			// 查询
			PageObject object = queryRemote.getContractQueryContinue(startDate,
					endDate, deptCode, contractTerm, employee
							.getEnterpriseCode(), start, limit);
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:续签合同查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:续签合同查询失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 合同变更查询
	 */
	public void getContractQueryChange() {
		try {
			LogUtil.log("Action:合同变更查询开始。", Level.INFO, null);
			// 查询
			PageObject object = queryRemote.getContractQueryChange(startDate,
					endDate, deptBeforeCode, deptAfterCode, contractTerm,
					employee.getEnterpriseCode(), start, limit);
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:合同变更查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:合同变更查询失败。", Level.SEVERE, e);
		}
	}
	
	/**
	 * 合同终止查询
	 */
	public void getContractQueryStop() {
		try {
			LogUtil.log("Action:合同终止查询开始。", Level.INFO, null);
			// 查询
			PageObject object = queryRemote.getContractQueryStop(startDate,
					endDate, deptCode, contractTerm, stopType,
					employee.getEnterpriseCode(), start, limit);
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:合同终止查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:合同终止查询失败。", Level.SEVERE, e);
		}
	}
	
	/**
	 * 到期合同查询
	 */
	public void getContractQueryDuedate() {
		try {
			LogUtil.log("Action:到期合同查询开始。", Level.INFO, null);
			// 查询
			PageObject object = queryRemote.getContractQueryEmployee(null,
					null, null, null, null, duetoTime, employee
							.getEnterpriseCode(), start, limit);
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:到期合同查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:到期合同查询失败。", Level.SEVERE, e);
		}
	}
	
	/**
	 * 未签合同查询
	 */
	public void getContractQueryNotsign() {
		try {
			LogUtil.log("Action:未签合同查询开始。", Level.INFO, null);
			// 查询
			PageObject object = queryRemote.getContractQueryNotsign(startDate,
					endDate, deptCode, employee.getEnterpriseCode(),
					start, limit);
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:未签合同查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:未签合同查询失败。", Level.SEVERE, e);
		}
	}
	
	/**
	 * 导出合同查询结果
	 */
	@SuppressWarnings("unchecked")
	public void exportContractExcel() {
		LogUtil.log("Action:导出合同查询结果开始。", Level.INFO, null);
		// 查询结果
		PageObject object = new PageObject();
		// 操作excel
		WriteXls xls = new WriteXls(Constants.BLANK_STRING, new ArrayList());
		// 机能ID
		String workId = "";
		// 机能名
		String workName = "";
		String setWidth="";
		// 员工合同
		if(flag.equals(EXPORT_EMPLOYEE)) {
			//add by wpzhu 
			
			 //update by sychen 20100712
//			 setWidth="0,2500;1,8000;2,8000;3,8000;4,8000;5,8000;6,8000;7,8000;" +
//					"8,8000;9,8000;10,8000;11,8000;12,8000;13,8000;14,8000";
			 setWidth="0,1500;1,4000;2,4000;3,4000;4,4000;5,4000;6,4000;7,4000;" +
					"8,4000;9,4000;10,4000;11,4000;12,4000;13,4000;14,4000";
			 //update by sychen 20100712 end 
			object = queryRemote.getContractQueryEmployee(startDate,
					endDate, deptCode, contractTerm, contractType, null, employee
							.getEnterpriseCode());
			workId = WORK_ID_EMPLOYEE;
			workName = WORK_NAME_EMPLOYEE;
		// 续签合同
		} else if(flag.equals(EXPORT_CONTINUE)) {
			setWidth="0,2500;1,10000;2,10000;3,10000;4,10000;5,10000;6,10000;7,10000;" +
			"8,10000;9,10000;10,10000;11,10000;12,10000";
			object = queryRemote.getContractQueryContinue(startDate,
					endDate, deptCode, contractTerm, employee
							.getEnterpriseCode());
			workId = WORK_ID_CONTINUE;
			workName = WORK_NAME_CONTINUE;
		// 变更合同
		} else if(flag.equals(EXPORT_CHANGE)) {
			 setWidth="0,2500;1,10000;2,10000;3,10000;4,10000;5,10000;6,10000;7,10000;" +
				"8,10000;9,10000;10,10000;11,10000;12,10000;13,10000;14,10000;15,10000";
			object = queryRemote.getContractQueryChange(startDate,
					endDate, deptBeforeCode, deptAfterCode, contractTerm,
					employee.getEnterpriseCode());
			workId = WORK_ID_CHANGE;
			workName = WORK_NAME_CHANGE;
		// 终止合同
		} else if(flag.equals(EXPORT_STOP)) {
			 setWidth="0,2500;1,10000;2,10000;3,10000;4,10000;5,10000;6,10000;7,10000;" +
				"8,10000;9,10000;10,10000;11,10000;12,10000";
			object = queryRemote.getContractQueryStop(startDate,
					endDate, deptCode, contractTerm, stopType,
					employee.getEnterpriseCode());
			workId = WORK_ID_STOP;
			workName = WORK_NAME_STOP;
		// 到期合同
		} else if(flag.equals(EXPORT_DUEDATE)) {
			 setWidth="0,2500;1,10000;2,10000;3,10000;4,10000;5,10000;6,10000;7,10000;" +
				"8,10000;9,10000;10,10000;11,10000;12,10000;13,10000;14,10000";
			object = queryRemote.getContractQueryEmployee(null,
					null, null, null, null, duetoTime, employee
							.getEnterpriseCode());
			workId = WORK_ID_EMPLOYEE;
			workName = WORK_NAME_DUEDATE;
		// 未签合同
		} else if(flag.equals(EXPORT_NOTSIGN)) {
			 setWidth="0,2500;1,10000;2,10000;3,10000;4,10000;5,2000;6,10000;7,10000";
			object = queryRemote.getContractQueryNotsign(startDate,
					endDate, deptCode, employee.getEnterpriseCode());
			workId = WORK_ID_NOTSIGN;
			workName = WORK_NAME_NOTSIGN;
		}
		// 生成
		xls = new WriteXls(workId, object.getList());
		// 设置机能名
		xls.setBusinessName(workName);
		try {
			// 导出
			xls.xlsExportFile(response,setWidth);
			LogUtil.log("Action:导出合同查询结果结束。", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("Action:导出合同查询结果失败。", Level.SEVERE, e);
			write(Constants.IO_FAILURE);
		}
	}
	
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the deptCode
	 */
	public String getDeptCode() {
		return deptCode;
	}

	/**
	 * @param deptCode
	 *            the deptCode to set
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	/**
	 * @return the contractTerm
	 */
	public String getContractTerm() {
		return contractTerm;
	}

	/**
	 * @param contractTerm
	 *            the contractTerm to set
	 */
	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}

	/**
	 * @return the contractType
	 */
	public String getContractType() {
		return contractType;
	}

	/**
	 * @param contractType
	 *            the contractType to set
	 */
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	/**
	 * @return the deptBeforeCode
	 */
	public String getDeptBeforeCode() {
		return deptBeforeCode;
	}

	/**
	 * @param deptBeforeCode
	 *            the deptBeforeCode to set
	 */
	public void setDeptBeforeCode(String deptBeforeCode) {
		this.deptBeforeCode = deptBeforeCode;
	}

	/**
	 * @return the deptAfterCode
	 */
	public String getDeptAfterCode() {
		return deptAfterCode;
	}

	/**
	 * @param deptAfterCode
	 *            the deptAfterCode to set
	 */
	public void setDeptAfterCode(String deptAfterCode) {
		this.deptAfterCode = deptAfterCode;
	}

	/**
	 * @return the stopType
	 */
	public String getStopType() {
		return stopType;
	}

	/**
	 * @param stopType the stopType to set
	 */
	public void setStopType(String stopType) {
		this.stopType = stopType;
	}

	/**
	 * @return the duetoTime
	 */
	public String getDuetoTime() {
		return duetoTime;
	}

	/**
	 * @param duetoTime the duetoTime to set
	 */
	public void setDuetoTime(String duetoTime) {
		this.duetoTime = duetoTime;
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
}
