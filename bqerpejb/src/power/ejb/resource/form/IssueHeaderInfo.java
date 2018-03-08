/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource.form;

import java.io.Serializable;
import java.util.Date;

/**
 * 领料单表头信息
 * @author qzhang
 */
@SuppressWarnings("serial")
public class IssueHeaderInfo  implements Serializable{
	/**流水号*/
	private Long issueHeadId;
	/** 编号*/
	private String issueNo;
	/** 申请领用日期 */
	private Date dueDate;
	/** 计划来源 */
	private String planOriginalDesc;
	/** 费用来源ID */
	//private Long itemId;
	// TODO 费用来源名称
	/** 备注 */
	private String memo;
	/** 领用部门*/
	private String receiptDep;
	/** 申请领料人*/
	private String receiptBy;
	/** 是否紧急领用*/
	private String	isEmergency;
	/** 工单编号*/
	private String woNo;
	/** 需求单编号*/
	private String mrNo;
	/** 归口部门*/
	private String feeByDep;
	/** 上次修改时间*/
	private String lastModifiedDate;

	/** 费用来源编码  modify by fyyang 090707 费用来源id改为费用来源编码*/
	private String itemCode;
	/** 申请领料人  add by ywliu 090710*/
	private String receiptByName;
	/** 领用部门名称  add by ywliu 090710*/
	private String receiptDepName;
	
	/**a工作流编号  add by drdu 091103**/
	private String workFlowNo;
	
	private String projectCode;//项目编号 add by fyyang 20100107
	
	/** 关联单号*/
	private String refIssueNo; // add by ywliu 20100203
	
	/** 关联单号*/
	private String actIssuedCount; // add by ywliu 20100203
	
	/**
	 * 获取
	 * @return lastModifiedDate
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	/**
	 * 设置
	 * @param lastModifiedDate
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	/**
	 * @return the feeByDep
	 */
	public String getFeeByDep() {
		return feeByDep;
	}
	/**
	 * @param feeByDep the feeByDep to set
	 */
	public void setFeeByDep(String feeByDep) {
		this.feeByDep = feeByDep;
	}
	/**
	 * 获取流水号
	 * @return issueHeadId 流水号
	 */
	public Long getIssueHeadId() {
		return issueHeadId;
	}
	/**
	 * 设置流水号
	 * @param issueHeadId 流水号
	 */
	public void setIssueHeadId(Long issueHeadId) {
		this.issueHeadId = issueHeadId;
	}
	/**
	 * 获取编号
	 * @return issueNo 编号
	 */
	public String getIssueNo() {
		return issueNo;
	}
	/**
	 * 设置编号
	 * @param issueNo 编号
	 */
	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}
	/**
	 * 获取申请领用日期
	 * @return dueDate 申请领用日期
	 */
	public Date getDueDate() {
		return dueDate;
	}
	/**
	 * 设置申请领用日期
	 * @param dueDate 申请领用日期
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	/**
	 * 获取计划来源
	 * @return planOriginalDesc 计划来源
	 */
	public String getPlanOriginalDesc() {
		return planOriginalDesc;
	}
	/**
	 * 设置计划来源
	 * @param planOriginalDesc 计划来源
	 */
	public void setPlanOriginalDesc(String planOriginalDesc) {
		this.planOriginalDesc = planOriginalDesc;
	}
//	/**
//	 * 获取费用来源ID
//	 * @return itemId 费用来源ID
//	 */
//	public Long getItemId() {
//		return itemId;
//	}
//	/**
//	 * 设置费用来源ID
//	 * @param itemId 费用来源ID
//	 */
//	public void setItemId(Long itemId) {
//		this.itemId = itemId;
//	}
	/**
	 * 获取备注
	 * @return memo 备注
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * 设置备注
	 * @param memo 备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * 获取领用部门
	 * @return receiptDep 领用部门
	 */
	public String getReceiptDep() {
		return receiptDep;
	}
	/**
	 * 设置领用部门
	 * @param receiptDep 领用部门
	 */
	public void setReceiptDep(String receiptDep) {
		this.receiptDep = receiptDep;
	}
	/**
	 * 获取申请领料人
	 * @return receiptBy 申请领料人
	 */
	public String getReceiptBy() {
		return receiptBy;
	}
	/**
	 * 设置申请领料人
	 * @param receiptBy 申请领料人
	 */
	public void setReceiptBy(String receiptBy) {
		this.receiptBy = receiptBy;
	}
	/**
	 * 获取是否紧急领用
	 * @return isEmergency 是否紧急领用
	 */
	public String getIsEmergency() {
		return isEmergency;
	}
	/**
	 * 设置是否紧急领用
	 * @param isEmergency 是否紧急领用
	 */
	public void setIsEmergency(String isEmergency) {
		this.isEmergency = isEmergency;
	}
	/**
	 * 获取工单编号
	 * @return woNo 工单编号
	 */
	public String getWoNo() {
		return woNo;
	}
	/**
	 * 设置工单编号
	 * @param woNo 工单编号
	 */
	public void setWoNo(String woNo) {
		this.woNo = woNo;
	}
	/**
	 * 获取
	 * @return mrNo
	 */
	public String getMrNo() {
		return mrNo;
	}
	/**
	 * 设置
	 * @param mrNo
	 */
	public void setMrNo(String mrNo) {
		this.mrNo = mrNo;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	/**
	 * @return the receiptByName
	 */
	public String getReceiptByName() {
		return receiptByName;
	}
	/**
	 * @param receiptByName the receiptByName to set
	 */
	public void setReceiptByName(String receiptByName) {
		this.receiptByName = receiptByName;
	}
	/**
	 * @return the receiptDepName
	 */
	public String getReceiptDepName() {
		return receiptDepName;
	}
	/**
	 * @param receiptDepName the receiptDepName to set
	 */
	public void setReceiptDepName(String receiptDepName) {
		this.receiptDepName = receiptDepName;
	}
	public String getWorkFlowNo() {
		return workFlowNo;
	}
	public void setWorkFlowNo(String workFlowNo) {
		this.workFlowNo = workFlowNo;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	/**
	 * @return the refIssueNo
	 */
	public String getRefIssueNo() {
		return refIssueNo;
	}
	/**
	 * @param refIssueNo the refIssueNo to set
	 */
	public void setRefIssueNo(String refIssueNo) {
		this.refIssueNo = refIssueNo;
	}
	/**
	 * @return the actIssuedCount
	 */
	public String getActIssuedCount() {
		return actIssuedCount;
	}
	/**
	 * @param actIssuedCount the actIssuedCount to set
	 */
	public void setActIssuedCount(String actIssuedCount) {
		this.actIssuedCount = actIssuedCount;
	}

}
