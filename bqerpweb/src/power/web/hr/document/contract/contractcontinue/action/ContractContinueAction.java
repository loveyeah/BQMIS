/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.contract.contractcontinue.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.DataChangeException;
import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJWorkcontract;
import power.ejb.hr.HrJWorkcontractFacadeRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.WorkContractFacadeRemote;
import power.ejb.hr.WorkContractI;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;



/**
 * 合同续签管理
 * 
 * @author sufeiyu
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ContractContinueAction extends AbstractAction {

	/**取得合同实现**/
	private WorkContractFacadeRemote wRemote;
	/**新增合同实现**/
	private HrJWorkcontractFacadeRemote hRemote;
	/**劳动合同ID**/
	private String strWorkcontractid;
	/**上次修改时间**/
	private String strLastModifiedDate;
	/**续签bean**/
	private WorkContractI bean;
	/**员工ID**/
	private String strEmpId;
	/** 所属部门（因为控件为disabled，所以无法submit，只好手动传值） */
	private String strDeptId;
	/** 所属岗位（因为控件为disabled，所以无法submit，只好手动传值） */
	private String strStationId;
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
    private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    /** 日期形式字符串 DATE_FORMAT_YYYYMMDD*/
    private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	
	/**
	 * 构造函数
	 */
	public ContractContinueAction() {
		wRemote = (WorkContractFacadeRemote) factory
				.getFacadeRemote("WorkContractFacade");
		hRemote = (HrJWorkcontractFacadeRemote) factory
		.getFacadeRemote("HrJWorkcontractFacade");
	}
	
	/**
	 * 查询合同信息
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void getContractByEId() {
		    // 得到当前用户的code
			employee = (Employee) session.getAttribute("employee");
			String strEnterpriseCode = employee.getEnterpriseCode();
			PageObject objResult = new PageObject();

			LogUtil.log("Action:查询合同信息开始", Level.INFO, null);
			try {
				Long lngEmpID = Long.parseLong(strEmpId);
				objResult = wRemote.getContractDetail(strEnterpriseCode, lngEmpID);
				// 要写出的数据
				String strRecord = "";
				if (objResult.getTotalCount() > 0) {
					strRecord = JSONUtil.serialize(objResult);
				} else {
					strRecord = "{\"list\":[],\"totalCount\":null}";
				}
				write(strRecord);
				LogUtil.log("Action:查询合同信息正常结束", Level.INFO, null);
			} catch (SQLException e) {
				LogUtil.log("Action:查询合同信息异常结束", Level.SEVERE, null);
				write(Constants.SQL_FAILURE);
			} catch (JSONException e) {
				LogUtil.log("Action:查询合同信息异常结束", Level.SEVERE, null);
				write(Constants.DATA_FAILURE);
			}
	}
	
	/**
	 * 确认按钮操作
	 */
	public void continueConstract() {
		// 得到当前用户的code
		employee = (Employee) session.getAttribute("employee");
		String strEmployee = employee.getWorkerCode();
		String strEnterpriseCode = employee.getEnterpriseCode();

		try {
			UserTransaction tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");

			try {
				// 事务处理起点
				tx.begin();
				LogUtil.log("Action:确认按钮操作开始", Level.INFO, null);
				HrJWorkcontract objTemp = new HrJWorkcontract();
				objTemp = hRemote.findById(Long.parseLong(strWorkcontractid),
						strEnterpriseCode);
				SimpleDateFormat dateFm = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
				SimpleDateFormat sdfFrom = new SimpleDateFormat(
						DATE_FORMAT_YYYYMMDD_HHMMSS);
				// 续签的合同
				HrJWorkcontract objNewWorkcontract = objTemp;
				// 之前的合同
				HrJWorkcontract objOldHrJWorkcontract = objTemp;

				objOldHrJWorkcontract.setWorkcontractid(Long
						.parseLong(strWorkcontractid));
				objOldHrJWorkcontract.setIfExecute("0");
				objOldHrJWorkcontract.setLastModifiedDate(sdfFrom
						.parse(strLastModifiedDate));
				objOldHrJWorkcontract.setLastModifiedBy(strEmployee);

				hRemote.update1(objOldHrJWorkcontract);

				objNewWorkcontract.setWorkcontractid(null);
				objNewWorkcontract.setContractTermId(bean.getContractTermId());
				objNewWorkcontract.setWorkSignDate(dateFm.parse(bean
						.getWorkSignDate()));
				objNewWorkcontract.setStartDate(dateFm.parse(bean
						.getStartDate()));
				objNewWorkcontract.setEndDate(dateFm.parse(bean.getEndDate()));
				// 设置部门为续签信息里的部门
				if (null != strDeptId && !"".equals(strDeptId)) {
					objNewWorkcontract.setDeptId(Long.parseLong(strDeptId));
				}
				// 设置岗位为续签信息里的岗位
				if (null != strStationId && !"".equals(strStationId)) {
					objNewWorkcontract.setStationId(Long.parseLong(strStationId));
				}
				objNewWorkcontract.setIfExecute("1");
				objNewWorkcontract.setContractContinueMark("1");
				objNewWorkcontract.setMemo(bean.getMemo());
				objNewWorkcontract.setInsertby(strEmployee);
				objNewWorkcontract.setEnterpriseCode(strEnterpriseCode);
				objNewWorkcontract.setLastModifiedBy(strEmployee);

				hRemote.save(objNewWorkcontract);
				tx.commit();
				write(Constants.ADD_SUCCESS);
				LogUtil.log("Action:确认按钮操作正常结束", Level.INFO, null);
			} catch (NumberFormatException e) {
				tx.rollback();
				LogUtil.log("Action:确认按钮操作异常结束", Level.SEVERE, e);
				write(Constants.SQL_FAILURE);
			} catch (ParseException e) {
				tx.rollback();
				LogUtil.log("Action:确认按钮操作异常结束", Level.SEVERE, e);
				write(Constants.SQL_FAILURE);
			} catch (DataChangeException e) {
				tx.rollback();
				LogUtil.log("Action:确认按钮操作异常结束", Level.SEVERE, e);
				write(Constants.DATA_USING);
			}
		} catch (Exception e) {
			LogUtil.log("Action:确认按钮操作异常结束", Level.SEVERE, e);
		}
	}
	
	public void getDeptAndStation() {
		// 得到当前用户的code
		employee = (Employee) session.getAttribute("employee");
		String strEnterpriseCode = employee.getEnterpriseCode();
		
		Long lngEmpId = Long.parseLong(strEmpId);
		Object[] objTemp = wRemote.getDeptAndStation(strEnterpriseCode, lngEmpId);
		
		// 编码转换
		StringBuffer JSONStr = new StringBuffer();
		JSONStr.append("[");
		JSONStr.append("{deptIdC:'" + (objTemp[0] == null? "":objTemp[0])
				+ "', deptNameC:'" + (objTemp[1] == null? "":objTemp[1])
				+ "', stationIdC:'" + (objTemp[2] == null? "":objTemp[2])
				+ "',stationNameC:'" + (objTemp[3] == null? "":objTemp[3]) + "'}");
		JSONStr.append("]");
		write("{\"list\":" + JSONStr + "}");
	}

	/**
	 * @return 员工ID
	 */
	public String getStrEmpId() {
		return strEmpId;
	}

	/**
	 * @param 员工ID
	 */
	public void setStrEmpId(String strEmpId) {
		this.strEmpId = strEmpId;
	}

	/**
	 * @return 劳动合同ID
	 */
	public String getStrWorkcontractid() {
		return strWorkcontractid;
	}

	/**
	 * @param 劳动合同ID
	 */
	public void setStrWorkcontractid(String strWorkcontractid) {
		this.strWorkcontractid = strWorkcontractid;
	}

	/**
	 * @return 上次修改时间
	 */
	public String getStrLastModifiedDate() {
		return strLastModifiedDate;
	}

	/**
	 * @param 上次修改时间
	 */
	public void setStrLastModifiedDate(String strLastModifiedDate) {
		this.strLastModifiedDate = strLastModifiedDate;
	}

	/**
	 * @return 续签bean
	 */
	public WorkContractI getBean() {
		return bean;
	}

	/**
	 * @param 续签bean
	 */
	public void setBean(WorkContractI bean) {
		this.bean = bean;
	}

	/**
	 * @return the strDeptId
	 */
	public String getStrDeptId() {
		return strDeptId;
	}

	/**
	 * @param strDeptId the strDeptId to set
	 */
	public void setStrDeptId(String strDeptId) {
		this.strDeptId = strDeptId;
	}

	/**
	 * @return the strStationId
	 */
	public String getStrStationId() {
		return strStationId;
	}

	/**
	 * @param strStationId the strStationId to set
	 */
	public void setStrStationId(String strStationId) {
		this.strStationId = strStationId;
	}

}
