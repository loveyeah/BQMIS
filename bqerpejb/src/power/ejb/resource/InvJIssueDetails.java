package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvJIssueDetails entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_J_ISSUE_DETAILS")
public class InvJIssueDetails implements java.io.Serializable {

	// Fields

	private Long issueDetailsId;
	private Long issueHeadId;
	private Long requirementDetailId;
	private Long materialId;
	private Long costItemId;
	private Double appliedCount;
	private Double approvedCount;
	private Double actIssuedCount;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;
	private Long planOriginalId;
	private String itemCode;
	private String modifyMemo; //add by fyyang 100112 核准信息

	// Constructors

	/** default constructor */
	public InvJIssueDetails() {
	}

	/** minimal constructor */
	public InvJIssueDetails(Long issueDetailsId, Long issueHeadId,
			Double appliedCount, Double approvedCount, Double actIssuedCount,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.issueDetailsId = issueDetailsId;
		this.issueHeadId = issueHeadId;
		this.appliedCount = appliedCount;
		this.approvedCount = approvedCount;
		this.actIssuedCount = actIssuedCount;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public InvJIssueDetails(Long issueDetailsId, Long issueHeadId,
			Long requirementDetailId, Long materialId, Long costItemId,
			Double appliedCount, Double approvedCount, Double actIssuedCount,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse, Long planOriginalId,
			String itemCode,String modifyMemo) {
		this.issueDetailsId = issueDetailsId;
		this.issueHeadId = issueHeadId;
		this.requirementDetailId = requirementDetailId;
		this.materialId = materialId;
		this.costItemId = costItemId;
		this.appliedCount = appliedCount;
		this.approvedCount = approvedCount;
		this.actIssuedCount = actIssuedCount;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.planOriginalId = planOriginalId;
		this.itemCode = itemCode;
		this.modifyMemo=modifyMemo;
	}

	// Property accessors
	@Id
	@Column(name = "ISSUE_DETAILS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getIssueDetailsId() {
		return this.issueDetailsId;
	}

	public void setIssueDetailsId(Long issueDetailsId) {
		this.issueDetailsId = issueDetailsId;
	}

	@Column(name = "ISSUE_HEAD_ID", nullable = false, precision = 10, scale = 0)
	public Long getIssueHeadId() {
		return this.issueHeadId;
	}

	public void setIssueHeadId(Long issueHeadId) {
		this.issueHeadId = issueHeadId;
	}

	@Column(name = "REQUIREMENT_DETAIL_ID", precision = 10, scale = 0)
	public Long getRequirementDetailId() {
		return this.requirementDetailId;
	}

	public void setRequirementDetailId(Long requirementDetailId) {
		this.requirementDetailId = requirementDetailId;
	}

	@Column(name = "MATERIAL_ID", precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "COST_ITEM_ID", precision = 10, scale = 0)
	public Long getCostItemId() {
		return this.costItemId;
	}

	public void setCostItemId(Long costItemId) {
		this.costItemId = costItemId;
	}

	@Column(name = "APPLIED_COUNT", nullable = false, precision = 15, scale = 4)
	public Double getAppliedCount() {
		return this.appliedCount;
	}

	public void setAppliedCount(Double appliedCount) {
		this.appliedCount = appliedCount;
	}

	@Column(name = "APPROVED_COUNT", nullable = false, precision = 15, scale = 4)
	public Double getApprovedCount() {
		return this.approvedCount;
	}

	public void setApprovedCount(Double approvedCount) {
		this.approvedCount = approvedCount;
	}

	@Column(name = "ACT_ISSUED_COUNT", nullable = false, precision = 15, scale = 4)
	public Double getActIssuedCount() {
		return this.actIssuedCount;
	}

	public void setActIssuedCount(Double actIssuedCount) {
		this.actIssuedCount = actIssuedCount;
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

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 10)
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

	@Column(name = "PLAN_ORIGINAL_ID", precision = 10, scale = 0)
	public Long getPlanOriginalId() {
		return this.planOriginalId;
	}

	public void setPlanOriginalId(Long planOriginalId) {
		this.planOriginalId = planOriginalId;
	}

	@Column(name = "ITEM_CODE", length = 20)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "MODIFY_MEMO", length = 500)
	public String getModifyMemo() {
		return modifyMemo;
	}

	public void setModifyMemo(String modifyMemo) {
		this.modifyMemo = modifyMemo;
	}

}