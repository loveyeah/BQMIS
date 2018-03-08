package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * MrpJPlanRequirementHead entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MRP_J_PLAN_REQUIREMENT_HEAD")
public class MrpJPlanRequirementHead implements java.io.Serializable {

	// Fields

	private Long requirementHeadId;
	private String mrNo;
	private Date mrDate;
	private String woNo;
	private Long planOriginalId;
	private String itemCode;
	private String mrBy;
	private String mrDept;
	private Date dueDate;
	private Long planGrade;
	private String costDept;
	private String costSpecial;
	private String mrReason;
	private String mrType;
	private Long wfNo;
	private String mrStatus;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;
	private String planDateMemo;
	private String prjNo; //add by fyyang 20100507

	// Constructors

	/** default constructor */
	public MrpJPlanRequirementHead() {
	}

	/** minimal constructor */
	public MrpJPlanRequirementHead(Long requirementHeadId, String mrNo,
			Date mrDate, Long planOriginalId, String itemCode, String mrStatus,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.requirementHeadId = requirementHeadId;
		this.mrNo = mrNo;
		this.mrDate = mrDate;
		this.planOriginalId = planOriginalId;
		this.itemCode = itemCode;
		this.mrStatus = mrStatus;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public MrpJPlanRequirementHead(Long requirementHeadId, String mrNo,
			Date mrDate, String woNo, Long planOriginalId, String itemCode,
			String mrBy, String mrDept, Date dueDate, Long planGrade,
			String costDept, String costSpecial, String mrReason,
			String mrType, Long wfNo, String mrStatus, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse,
			String planDateMemo,String prjNo) {
		this.requirementHeadId = requirementHeadId;
		this.mrNo = mrNo;
		this.mrDate = mrDate;
		this.woNo = woNo;
		this.planOriginalId = planOriginalId;
		this.itemCode = itemCode;
		this.mrBy = mrBy;
		this.mrDept = mrDept;
		this.dueDate = dueDate;
		this.planGrade = planGrade;
		this.costDept = costDept;
		this.costSpecial = costSpecial;
		this.mrReason = mrReason;
		this.mrType = mrType;
		this.wfNo = wfNo;
		this.mrStatus = mrStatus;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.planDateMemo = planDateMemo;
		this.prjNo=prjNo;
	}

	// Property accessors
	@Id
	@Column(name = "REQUIREMENT_HEAD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRequirementHeadId() {
		return this.requirementHeadId;
	}

	public void setRequirementHeadId(Long requirementHeadId) {
		this.requirementHeadId = requirementHeadId;
	}

	@Column(name = "MR_NO", nullable = false, length = 20)
	public String getMrNo() {
		return this.mrNo;
	}

	public void setMrNo(String mrNo) {
		this.mrNo = mrNo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MR_DATE", nullable = false, length = 7)
	public Date getMrDate() {
		return this.mrDate;
	}

	public void setMrDate(Date mrDate) {
		this.mrDate = mrDate;
	}

	@Column(name = "WO_NO", length = 30)
	public String getWoNo() {
		return this.woNo;
	}

	public void setWoNo(String woNo) {
		this.woNo = woNo;
	}

	@Column(name = "PLAN_ORIGINAL_ID", nullable = false, precision = 10, scale = 0)
	public Long getPlanOriginalId() {
		return this.planOriginalId;
	}

	public void setPlanOriginalId(Long planOriginalId) {
		this.planOriginalId = planOriginalId;
	}

	@Column(name = "ITEM_CODE", nullable = false, length = 20)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "MR_BY", length = 16)
	public String getMrBy() {
		return this.mrBy;
	}

	public void setMrBy(String mrBy) {
		this.mrBy = mrBy;
	}

	@Column(name = "MR_DEPT", length = 20)
	public String getMrDept() {
		return this.mrDept;
	}

	public void setMrDept(String mrDept) {
		this.mrDept = mrDept;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DUE_DATE", length = 7)
	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Column(name = "PLAN_GRADE", precision = 10, scale = 0)
	public Long getPlanGrade() {
		return this.planGrade;
	}

	public void setPlanGrade(Long planGrade) {
		this.planGrade = planGrade;
	}

	@Column(name = "COST_DEPT", length = 20)
	public String getCostDept() {
		return this.costDept;
	}

	public void setCostDept(String costDept) {
		this.costDept = costDept;
	}

	@Column(name = "COST_SPECIAL", length = 10)
	public String getCostSpecial() {
		return this.costSpecial;
	}

	public void setCostSpecial(String costSpecial) {
		this.costSpecial = costSpecial;
	}

	@Column(name = "MR_REASON", length = 300)
	public String getMrReason() {
		return this.mrReason;
	}

	public void setMrReason(String mrReason) {
		this.mrReason = mrReason;
	}

	@Column(name = "MR_TYPE", length = 4)
	public String getMrType() {
		return this.mrType;
	}

	public void setMrType(String mrType) {
		this.mrType = mrType;
	}

	@Column(name = "WF_NO", precision = 22, scale = 0)
	public Long getWfNo() {
		return this.wfNo;
	}

	public void setWfNo(Long wfNo) {
		this.wfNo = wfNo;
	}

	@Column(name = "MR_STATUS", nullable = false, length = 1)
	public String getMrStatus() {
		return this.mrStatus;
	}

	public void setMrStatus(String mrStatus) {
		this.mrStatus = mrStatus;
	}

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", nullable = false, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "PLAN_DATE_MEMO", length = 30)
	public String getPlanDateMemo() {
		return this.planDateMemo;
	}

	public void setPlanDateMemo(String planDateMemo) {
		this.planDateMemo = planDateMemo;
	}

	@Column(name = "PRJ_NO", length = 50)
	public String getPrjNo() {
		return prjNo;
	}

	public void setPrjNo(String prjNo) {
		this.prjNo = prjNo;
	}

}