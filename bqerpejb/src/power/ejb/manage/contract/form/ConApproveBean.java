package power.ejb.manage.contract.form;

import java.util.List;

import com.opensymphony.engineassistant.po.WfJHistoryoperation;

@SuppressWarnings("serial")
public class ConApproveBean implements java.io.Serializable{
//	合同ID
	private Long conId;
//	合同编号
	private String conttreesNo;
//	合同名称
	private String contractName;
//	供应商ID
	private Long cliendId;
//	合同类别ID
	private Long conTypeId;
//	供应商名称
	private String clientName;
//	合同类别名称
	private String conTypeName;
//	合同简介
	private String conAbstract;
//	经办人编码
	private String operateBy;
//	经办人部门编码 （申请部门）
	private String operateDepCode;
//	经办人名称
	private String operateName;
//	经办人部门名称（申请部门名称）
	private String operateDepName;
//	负责人编码
	private String operateLeadBy;
//	负责人名称
	private String operateLeadName;
//	申请日期
	private String signStartDate;
//  审批意见	

	private List<WfJHistoryoperation> approveList;
	
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	public String getConttreesNo() {
		return conttreesNo;
	}
	public void setConttreesNo(String conttreesNo) {
		this.conttreesNo = conttreesNo;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public Long getCliendId() {
		return cliendId;
	}
	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}
	public Long getConTypeId() {
		return conTypeId;
	}
	public void setConTypeId(Long conTypeId) {
		this.conTypeId = conTypeId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getConTypeName() {
		return conTypeName;
	}
	public void setConTypeName(String conTypeName) {
		this.conTypeName = conTypeName;
	}
	public String getConAbstract() {
		return conAbstract;
	}
	public void setConAbstract(String conAbstract) {
		this.conAbstract = conAbstract;
	}
	public String getOperateBy() {
		return operateBy;
	}
	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}
	public String getOperateDepCode() {
		return operateDepCode;
	}
	public void setOperateDepCode(String operateDepCode) {
		this.operateDepCode = operateDepCode;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public String getOperateDepName() {
		return operateDepName;
	}
	public void setOperateDepName(String operateDepName) {
		this.operateDepName = operateDepName;
	}
	public List<WfJHistoryoperation> getApproveList() {
		return approveList;
	}
	public void setApproveList(List<WfJHistoryoperation> approveList) {
		this.approveList = approveList;
	}
	public String getOperateLeadBy() {
		return operateLeadBy;
	}
	public void setOperateLeadBy(String operateLeadBy) {
		this.operateLeadBy = operateLeadBy;
	}
	public String getOperateLeadName() {
		return operateLeadName;
	}
	public void setOperateLeadName(String operateLeadName) {
		this.operateLeadName = operateLeadName;
	}
	public String getSignStartDate() {
		return signStartDate;
	}
	public void setSignStartDate(String signStartDate) {
		this.signStartDate = signStartDate;
	}
}
