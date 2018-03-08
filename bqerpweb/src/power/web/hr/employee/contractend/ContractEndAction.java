/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.employee.contractend;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJContractstop;
import power.ejb.hr.HrJContractstopFacadeRemote;
import power.ejb.hr.HrJWorkcontractFacadeRemote;
import power.ejb.hr.HrJWorkcontract;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sun.corba.se.impl.orbutil.closure.Constant;

/**
 * 终止合同登记Action
 * 
 * @author liuxin
 * 
 */
public class ContractEndAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private HrJContractstop entity1;
	private HrJWorkcontract entity2;
	private HrJContractstopFacadeRemote remote1;
	private HrJWorkcontractFacadeRemote remote2;
	/** 合同号ID */
	private String contractId;
	/** 人员ID */
	private String empId;
	private String realEndTime;
	private String contractStopType;
	private String dossierDirection;
	private String releaseReason;
	private String societyInsuranceDirection;
	private String memo;
	private String deptId;
	private String stationId;
	private String startDate;
	private String insertby;
	private String endDate;
	// 合同解除文号 add by ywliu 20100612
	private String terminatedCode;
	// 日期转化格式
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	// 劳动合同是否正在执行
	private static final String IF_EXECUTE = "0";

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public ContractEndAction() {
		remote1 = (HrJContractstopFacadeRemote) factory
				.getFacadeRemote("HrJContractstopFacade");
		remote2 = (HrJWorkcontractFacadeRemote) factory
				.getFacadeRemote("HrJWorkcontractFacade");
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
	 * 按合同号查找
	 */
	public void findByContractId() {
		LogUtil.log("Action:按合同号查找开始。", Level.INFO, null);
		PageObject obj = new PageObject();
		List<HrJContractstop> list = new ArrayList<HrJContractstop>();
		list = remote1.findByIdIsUse(new Long(contractId));
		obj.setList(list);

		// 找到该合同
		if (list.size() == 1)
			obj.setTotalCount(new Long(1));
		else
			obj.setTotalCount(new Long(0));
		String str = null;
		try {
			str = JSONUtil.serialize(obj);
			LogUtil.log("Action:按合同号查找成功。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:按合同号查找失败。", Level.SEVERE, null);
		}
		write(str);
	}

	/**
	 * 劳动合同终止
	 * 
	 * @throws DataFormatException
	 */
	public void endContract() throws DataFormatException {
		LogUtil.log("Action:解除劳动合同开始。", Level.INFO, null);
		// 更新劳动合同登记表
		try {
			entity2 = new HrJWorkcontract();
			entity2 = remote2
					.findByEmpId(new Long(empId), new Long(contractId));
			entity2.setIsUse(Constants.IS_USE_N);
			entity2.setIfExecute(IF_EXECUTE);
			entity2.setLastModifiedBy(employee.getWorkerCode());
			entity1 = new HrJContractstop();
			if (contractId != null)
				entity1.setWorkcontractid(new Long(contractId));
			entity1.setEnterpriseCode(employee.getEnterpriseCode());
			entity1
					.setRealEndTime(formatStringToDate(realEndTime, DATE_FORMAT));
			entity1.setContractStopType(contractStopType);
			entity1.setReleaseReason(releaseReason);
			entity1.setDossierDirection(dossierDirection);
			entity1.setSocietyInsuranceDirection(societyInsuranceDirection);
			entity1.setMemo(memo);
			entity1.setIsUse(Constants.IS_USE_Y);
			if (empId != null)
				entity1.setEmpId(new Long(empId));
			if (deptId != null)
				entity1.setDeptId(new Long(deptId));
			if (stationId != null) {
				entity1.setStationId(new Long(stationId));
			}
			entity1.setStartDate(formatStringToDate(startDate, DATE_FORMAT));
			entity1.setEndDate(formatStringToDate(endDate, DATE_FORMAT));
			entity1.setInsertby(employee.getWorkerCode());
			entity1.setLastModifiedBy(employee.getWorkerCode());
			entity1.setLastModifiedDate(new java.util.Date());
			entity1.setInsertdate(new java.util.Date());
			entity1.setEnterpriseCode(employee.getEnterpriseCode());
			entity1.setIsUse(Constants.IS_USE_Y);
			// add by ywliu 20100612
			entity1.setContractTerminatedCode(terminatedCode);
			remote1.contractEnd(entity1, entity2);
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:解除劳动合同结束。", Level.INFO, null);
		} catch (DataChangeException de) {
			write(Constants.DATA_USING);
		} catch (Exception e) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:操作数据库出现异常。", Level.SEVERE, e);
		}
	}

	/**
	 * 按员工ID进行检索
	 * 
	 * @return
	 */
	public void findByEmpId() {
		LogUtil.log("Action:按员工ID进行检索开始。", Level.INFO, null);
		try {
			PageObject pobj = remote1.searchEmpContract(new Long(empId));
			String str = null;
			str = JSONUtil.serialize(pobj);
			LogUtil.log("Action:按员工ID进行检索结束。", Level.INFO, null);
			write(str);
		} catch (Exception e) {
			LogUtil.log("Action:按员工ID进行检索失败。", Level.SEVERE, null);
		}
	}

	public String getRealEndTime() {
		return realEndTime;
	}

	public void setRealEndTime(String realEndTime) {
		this.realEndTime = realEndTime;
	}

	public String getContractStopType() {
		return contractStopType;
	}

	public void setContractStopType(String contractStopType) {
		this.contractStopType = contractStopType;
	}

	public String getDossierDirection() {
		return dossierDirection;
	}

	public void setDossierDirection(String dossierDirection) {
		this.dossierDirection = dossierDirection;
	}

	public String getReleaseReason() {
		return releaseReason;
	}

	public void setReleaseReason(String releaseReason) {
		this.releaseReason = releaseReason;
	}

	public String getSocietyInsuranceDirection() {
		return societyInsuranceDirection;
	}

	public void setSocietyInsuranceDirection(String societyInsuranceDirection) {
		this.societyInsuranceDirection = societyInsuranceDirection;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getInsertby() {
		return insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTerminatedCode() {
		return terminatedCode;
	}

	public void setTerminatedCode(String terminatedCode) {
		this.terminatedCode = terminatedCode;
	}
	
}