/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.document.contract.contractchangemanage.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ContractChangeManage;
import power.ejb.hr.ContractChangeManageFacadeRemote;
import power.ejb.hr.HrJContractchange;
import power.ejb.hr.HrJContractchangeFacadeRemote;
import power.ejb.hr.HrJDepstationcorrespondFacadeRemote;
import power.ejb.hr.HrJWorkcontract;
import power.ejb.hr.HrJWorkcontractFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 合同变更管理Action
 * 
 * @author jincong
 * @version 1.0
 */
public class ContractChangeManageAction extends AbstractAction {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** 查询结果为空 */
	public static final String BLANK_RESULT = "{\"list\":[],\"totalCount\":0}";
	/** 日期格式 */
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	/** 时间格式 */
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/** 人员id */
	private String empId;
	/** 部门Id */
	private String deptId;
	/** 合同签订Id */
	private String workContractId;
	/** 合同变更管理Bean */
	private ContractChangeManage contractChange;
	/** 合同变更管理Remote */
	private ContractChangeManageFacadeRemote changeRemote;
	/** 部门岗位对应Remote */
	private HrJDepstationcorrespondFacadeRemote correspondRemote;
	/** 合同变更Remote */
	private HrJContractchangeFacadeRemote remote;
	/** 劳动合同登记Remote */
	private HrJWorkcontractFacadeRemote workcontractRemote;

	/**
	 * 构造函数
	 */
	public ContractChangeManageAction() {
		// 取得接口
		changeRemote = (ContractChangeManageFacadeRemote) factory
				.getFacadeRemote("ContractChangeManageFacade");
		correspondRemote = (HrJDepstationcorrespondFacadeRemote) factory
				.getFacadeRemote("HrJDepstationcorrespondFacade");
		remote = (HrJContractchangeFacadeRemote) factory
				.getFacadeRemote("HrJContractchangeFacade");
		workcontractRemote = (HrJWorkcontractFacadeRemote) factory
				.getFacadeRemote("HrJWorkcontractFacade");
	}

	/**
	 * 根据人员Id获得变更合同的信息
	 */
	public void getContractChangeInfo() {
		try {
			LogUtil.log("Action:根据人员Id获得变更合同的信息开始。", Level.INFO, null);
			// 查询
			PageObject object = changeRemote.getContractChangeInfo(empId,
					workContractId, employee.getEnterpriseCode());
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:根据人员Id获得变更合同的信息结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:根据人员Id获得变更合同的信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 根据人员Id获得登记合同的信息
	 */
	public void getContractInfoForChange() {
		try {
			LogUtil.log("Action:根据人员Id获得登记合同的信息开始。", Level.INFO, null);
			// 查询
			PageObject object = changeRemote.getContractInfoForChange(empId,
					employee.getEnterpriseCode());
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:根据人员Id获得登记合同的信息结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:根据人员Id获得登记合同的信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 查找合同有效期数据
	 */
	public void getContractTermForChange() {
		try {
			LogUtil.log("Action:劳动合同有效期维护查询开始。", Level.INFO, null);
			// 查询
			PageObject object = changeRemote.findContractTerm(employee
					.getEnterpriseCode());
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:劳动合同有效期维护查询结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:劳动合同有效期维护查询失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 根据部门查找岗位信息
	 */
	public void getStationByDeptForChange() {
		try {
			LogUtil.log("Action:根据部门查找岗位信息开始。", Level.INFO, null);
			// 查询
			PageObject object = correspondRemote.findByDeptId(deptId, employee
					.getEnterpriseCode());
			// 输出
			String strOutput = BLANK_RESULT;
			if (object.getList() != null && (object.getList().size() > 0)) {
				strOutput = JSONUtil.serialize(object);
			}
			write(strOutput);
			LogUtil.log("Action:根据部门查找岗位信息结束。", Level.INFO, null);
		} catch (SQLException e) {
			LogUtil.log("Action:根据部门查找岗位信息失败。", Level.SEVERE, e);
		} catch (JSONException e) {
			LogUtil.log("Action:根据部门查找岗位信息失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 根据人员Id取得部门和岗位
	 */
	public void getBaseInfoForChange() {
		try {
			LogUtil.log("Action:根据人员Id取得部门和岗位开始。", Level.INFO, null);
			// 查询
			PageObject object = changeRemote.getBaseInfoForChange(empId,
					employee.getEnterpriseCode());
			// 输出
			write(JSONUtil.serialize(object));
			LogUtil.log("Action:根据人员Id取得部门和岗位结束。", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:根据人员Id取得部门和岗位失败。", Level.SEVERE, e);
		}
	}

	/**
	 * 保存合同变更信息
	 * 
	 * @throws Exception
	 */
	public void saveContractChange() throws Exception {
		UserTransaction tx = null;
		try {
			LogUtil.log("Action:保存合同变更信息开始。", Level.INFO, null);
			tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			// 事务开始
			tx.begin();

			// 新增一条合同变更记录
			// 合同变更bean
			HrJContractchange bean = new HrJContractchange();
			// 劳动合同签订ID
			bean.setWorkcontractid(Long.parseLong(contractChange
					.getWorkContractId()));
			// 人员ID
			bean.setEmpId2(Long.parseLong(contractChange.getEmpId()));
			// 变更前部门
			if (!Constants.BLANK_STRING.equals(contractChange.getDeptId())
					&& !(contractChange.getDeptId() == null)) {
				bean.setOldDepCode(Long.parseLong(contractChange.getDeptId()));
			}
			// 变更后部门
			if (!Constants.BLANK_STRING.equals(contractChange.getNewDeptId())
					&& !(contractChange.getNewDeptId() == null)) {
				bean.setNewDepCode(Long
						.parseLong(contractChange.getNewDeptId()));
			}
			// 变更前岗位
			if (!Constants.BLANK_STRING.equals(contractChange.getStationId())
					&& !(contractChange.getStationId() == null)) {
				bean.setOldStationCode(Long.parseLong(contractChange
						.getStationId()));
			}
			// 变更后岗位
			if (!Constants.BLANK_STRING
					.equals(contractChange.getNewStationId())
					&& !(contractChange.getNewStationId() == null)) {
				bean.setNewStationCode(Long.parseLong(contractChange
						.getNewStationId()));
			}
			// 变更前合同期限
			if (!Constants.BLANK_STRING.equals(contractChange
					.getContractTermId())
					&& !(contractChange.getContractTermId() == null)) {
				bean.setOldContractCode(Long.parseLong(contractChange
						.getContractTermId()));
			}
			// 变更后合同期限
			if (!Constants.BLANK_STRING.equals(contractChange
					.getNewContractTermId())
					&& !(contractChange.getNewContractTermId() == null)) {
				bean.setNewContractCode(Long.parseLong(contractChange
						.getNewContractTermId()));
			}
			// 变更时间
			bean.setChargeDate(formatStringToDate(contractChange
					.getChangeDate(), DATE_FORMAT));
			// 变更前合同起始时间
			bean.setOldStateTime(formatStringToDate(contractChange
					.getStartDate(), DATE_FORMAT));
			// 变更后合同起始时间
			bean.setNewStateTime(formatStringToDate(contractChange
					.getNewStartDate(), DATE_FORMAT));
			// 变更前合同结束时间
			bean.setOldEndTime(formatStringToDate(contractChange.getEndDate(),
					DATE_FORMAT));
			// 变更后合同结束时间
			bean.setNewEndTime(formatStringToDate(contractChange
					.getNewEndDate(), DATE_FORMAT));
			// 变更原因
			bean.setChangeReason(contractChange.getChangeReason());
			// 备注
			bean.setMemo(contractChange.getMemo());
			// 记录人
			bean.setInsertby(employee.getWorkerCode());
			// 记录日期
			bean.setInsertdate(new Date());
			// 企业编码
			bean.setEnterpriseCode(employee.getEnterpriseCode());
			// 是否使用
			bean.setIsUse(Constants.IS_USE_Y);
			// 上次修改人
			bean.setLastModifiedBy(employee.getWorkerCode());
			// 上次修改日期
			bean.setLastModifiedDate(new Date());
			// 新增
			remote.save(bean);

			// 更新劳动合同登记表开始
			HrJWorkcontract workcontract = new HrJWorkcontract();
			// 查询
			workcontract = workcontractRemote.findById(Long
					.parseLong(contractChange.getWorkContractId()), employee
					.getEnterpriseCode());
			// 排他
			if (workcontract == null) {
				throw new DataChangeException(Constants.BLANK_STRING);
			}
			// 劳动合同有效期ID
			if (!Constants.BLANK_STRING.equals(contractChange
					.getNewContractTermId())
					&& !(contractChange.getNewContractTermId() == null)) {
				workcontract.setContractTermId(Long.parseLong(contractChange
						.getNewContractTermId()));
			} else {
				workcontract.setContractTermId(null);
			}
			// 岗位ID
			if (!Constants.BLANK_STRING
					.equals(contractChange.getNewStationId())
					&& !(contractChange.getNewStationId() == null)) {
				workcontract.setStationId(Long.parseLong(contractChange
						.getNewStationId()));
			} else {
				workcontract.setStationId(null);
			}
			// 部门ID
			if (!Constants.BLANK_STRING.equals(contractChange.getNewDeptId())
					&& !(contractChange.getNewDeptId() == null)) {
				workcontract.setDeptId(Long.parseLong(contractChange
						.getNewDeptId()));
			} else {
				workcontract.setDeptId(null);
			}
			// 开始日期
			workcontract.setStartDate(formatStringToDate(contractChange
					.getNewStartDate(), DATE_FORMAT));
			// 结束日期
			workcontract.setEndDate(formatStringToDate(contractChange
					.getNewEndDate(), DATE_FORMAT));
			// 上次修改人
			workcontract.setLastModifiedBy(employee.getWorkerCode());
			// 上次修改日期
			workcontract.setLastModifiedDate(formatStringToDate(contractChange
					.getLastModifiedDate(), TIME_FORMAT));
			// 更新
			workcontractRemote.update1(workcontract);

			// 提交事务
			tx.commit();
			// 以html方式输出字符串
			write("{success:true,msg:true}");
			LogUtil.log("Action:保存合同变更信息结束。", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("Action:保存合同变更信息失败。", Level.SEVERE, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			write(Constants.DATA_USING);
			throw e;
		} catch (RuntimeException e) {
			LogUtil.log("Action:保存合同变更信息失败。", Level.SEVERE, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
			write(Constants.SQL_FAILURE);
			throw e;
		} catch (DataFormatException e) {
			LogUtil.log("Action:保存合同变更信息失败。", Level.SEVERE, e);
			if (tx != null) {
				// 事务回滚
				tx.rollback();
			}
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
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}

	/**
	 * @param empId
	 *            the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}

	/**
	 * @return the deptId
	 */
	public String getDeptId() {
		return deptId;
	}

	/**
	 * @param deptId
	 *            the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/**
	 * @return the contractChange
	 */
	public ContractChangeManage getContractChange() {
		return contractChange;
	}

	/**
	 * @param contractChange
	 *            the contractChange to set
	 */
	public void setContractChange(ContractChangeManage contractChange) {
		this.contractChange = contractChange;
	}

	/**
	 * @return the workContractId
	 */
	public String getWorkContractId() {
		return workContractId;
	}

	/**
	 * @param workContractId the workContractId to set
	 */
	public void setWorkContractId(String workContractId) {
		this.workContractId = workContractId;
	}
}
