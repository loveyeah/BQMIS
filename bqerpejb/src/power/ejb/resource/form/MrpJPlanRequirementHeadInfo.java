/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.form;

/**
 * 物料需求计划主表Bean
 * 
 * @author 
 * @version 1.0
 */
public class MrpJPlanRequirementHeadInfo implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// 物料需求计划主表
	/** 申请单编号 */
	private String mrNo;
	/** 工单编号 */
	private String woNo;
	/** 费用来源 */
	private String itemIdHead;//modify by ywliu  2009/7/6
	/** 申请人 */
	private String mrBy;
	/** 申请部门 */
	private String mrDept;
	/** 需求日期 */
	private String dueDate;
	/** 归口专业 */
	private String costSpecial;
	/** 归口部门 */
	private String costDept;
	/** 申请单ID */
	private Long requimentHeadId;
	/** 上次修改日期 */
	private String lastModifiedDate;
	/** 计划来源 */
	private Long planOriginalId;
	// 工单
	/** 项目编号 */
	private String projectNo;
	
	//add by fyyang 090512
	/** 申请人姓名 */
	private String mrByName;
	/** 申请部门 */
	private String mrDeptName;
	/** 归口专业名称 */
	private String costSpecialName;
	
	/** 归口部门名称 */
	private String costDeptName;
	
	/** 费用来源名称 */
	private Long itemIdHeadName;
	
	/** 申请原因 */
	private String mrReason;

	/**
	 * @return the mrNo
	 */
	public String getMrNo() {
		return mrNo;
	}

	/**
	 * @param mrNo the mrNo to set
	 */
	public void setMrNo(String mrNo) {
		this.mrNo = mrNo;
	}

	/**
	 * @return the woNo
	 */
	public String getWoNo() {
		return woNo;
	}

	/**
	 * @param woNo the woNo to set
	 */
	public void setWoNo(String woNo) {
		this.woNo = woNo;
	}

	/**
	 * @return the itemIdHead
	 */
	public String getItemIdHead() {
		return itemIdHead;
	}

	/**
	 * @param itemIdHead the itemIdHead to set
	 */
	public void setItemIdHead(String itemIdHead) {
		this.itemIdHead = itemIdHead;
	}

	/**
	 * @return the mrBy
	 */
	public String getMrBy() {
		return mrBy;
	}

	/**
	 * @param mrBy the mrBy to set
	 */
	public void setMrBy(String mrBy) {
		this.mrBy = mrBy;
	}

	/**
	 * @return the mrDept
	 */
	public String getMrDept() {
		return mrDept;
	}

	/**
	 * @param mrDept the mrDept to set
	 */
	public void setMrDept(String mrDept) {
		this.mrDept = mrDept;
	}

	/**
	 * @return the dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the costSpecial
	 */
	public String getCostSpecial() {
		return costSpecial;
	}

	/**
	 * @param costSpecial the costSpecial to set
	 */
	public void setCostSpecial(String costSpecial) {
		this.costSpecial = costSpecial;
	}

	/**
	 * @return the costDept
	 */
	public String getCostDept() {
		return costDept;
	}

	/**
	 * @param costDept the costDept to set
	 */
	public void setCostDept(String costDept) {
		this.costDept = costDept;
	}

	/**
	 * @return the projectNo
	 */
	public String getProjectNo() {
		return projectNo;
	}

	/**
	 * @param projectNo the projectNo to set
	 */
	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

	/**
	 * @return the requimentHeadId
	 */
	public Long getRequimentHeadId() {
		return requimentHeadId;
	}

	/**
	 * @param requimentHeadId the requimentHeadId to set
	 */
	public void setRequimentHeadId(Long requimentHeadId) {
		this.requimentHeadId = requimentHeadId;
	}

	/**
	 * @return the lastModifiedDate
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getPlanOriginalId() {
		return planOriginalId;
	}

	public void setPlanOriginalId(Long planOriginalId) {
		this.planOriginalId = planOriginalId;
	}

	public String getMrByName() {
		return mrByName;
	}

	public void setMrByName(String mrByName) {
		this.mrByName = mrByName;
	}

	public String getMrDeptName() {
		return mrDeptName;
	}

	public void setMrDeptName(String mrDeptName) {
		this.mrDeptName = mrDeptName;
	}

	public String getCostSpecialName() {
		return costSpecialName;
	}

	public void setCostSpecialName(String costSpecialName) {
		this.costSpecialName = costSpecialName;
	}

	public String getCostDeptName() {
		return costDeptName;
	}

	public void setCostDeptName(String costDeptName) {
		this.costDeptName = costDeptName;
	}

	public Long getItemIdHeadName() {
		return itemIdHeadName;
	}

	public void setItemIdHeadName(Long itemIdHeadName) {
		this.itemIdHeadName = itemIdHeadName;
	}

	public String getMrReason() {
		return mrReason;
	}

	public void setMrReason(String mrReason) {
		this.mrReason = mrReason;
	}
}
