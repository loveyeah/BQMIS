package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * MrpJPlanRequirementDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MRP_J_PLAN_REQUIREMENT_DETAIL")
public class MrpJPlanRequirementDetail implements java.io.Serializable {

	// Fields

	private Long requirementDetailId;
	private Long requirementHeadId;
	private Long woLine;
	private Long equSparepartId;
	private Long materialId;
	private Long planOriginalId;
	private Double appliedQty;
	private Double approvedQty;
	private Double purQty;
	private Double issQty;
	private Double estimatedPrice;
	private Long planGrade;
	private String usage;
	private Date dueDate;
	private String supplier;
	private String memo;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String isGenerated;
	private String enterpriseCode;
	private String isUse;
	private String itemCode;
	private String cancelReason; //add by fyyang 090807 作废原因
	private String modifyMemo; //add by fyyang 20100112 核准数量改变情况记录
	private Date cancelDate;//add by fyyang 20100407 作废时间
	

	// Constructors

	/** default constructor */
	public MrpJPlanRequirementDetail() {
	}

	/** minimal constructor */
	public MrpJPlanRequirementDetail(Long requirementDetailId,
			Long requirementHeadId, Long woLine, Long materialId,
			Double appliedQty, Date dueDate, String lastModifiedBy,
			Date lastModifiedDate, String isGenerated, String enterpriseCode,
			String isUse) {
		this.requirementDetailId = requirementDetailId;
		this.requirementHeadId = requirementHeadId;
		this.woLine = woLine;
		this.materialId = materialId;
		this.appliedQty = appliedQty;
		this.dueDate = dueDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isGenerated = isGenerated;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public MrpJPlanRequirementDetail(Long requirementDetailId,
			Long requirementHeadId, Long woLine, Long equSparepartId,
			Long materialId, Long planOriginalId, Double appliedQty,
			Double approvedQty, Double purQty, Double issQty,
			Double estimatedPrice, Long planGrade, String usage, Date dueDate,
			String supplier, String memo, String lastModifiedBy,
			Date lastModifiedDate, String isGenerated, String enterpriseCode,
			String isUse, String itemCode,String cancelReason,Date cancelDate) {
		this.requirementDetailId = requirementDetailId;
		this.requirementHeadId = requirementHeadId;
		this.woLine = woLine;
		this.equSparepartId = equSparepartId;
		this.materialId = materialId;
		this.planOriginalId = planOriginalId;
		this.appliedQty = appliedQty;
		this.approvedQty = approvedQty;
		this.purQty = purQty;
		this.issQty = issQty;
		this.estimatedPrice = estimatedPrice;
		this.planGrade = planGrade;
		this.usage = usage;
		this.dueDate = dueDate;
		this.supplier = supplier;
		this.memo = memo;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isGenerated = isGenerated;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.itemCode = itemCode;
		this.cancelReason=cancelReason;
		this.cancelDate=cancelDate;
	}

	// Property accessors
	@Id
	@Column(name = "REQUIREMENT_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRequirementDetailId() {
		return this.requirementDetailId;
	}

	public void setRequirementDetailId(Long requirementDetailId) {
		this.requirementDetailId = requirementDetailId;
	}

	@Column(name = "REQUIREMENT_HEAD_ID", nullable = false, precision = 10, scale = 0)
	public Long getRequirementHeadId() {
		return this.requirementHeadId;
	}

	public void setRequirementHeadId(Long requirementHeadId) {
		this.requirementHeadId = requirementHeadId;
	}

	@Column(name = "WO_LINE", nullable = false, precision = 10, scale = 0)
	public Long getWoLine() {
		return this.woLine;
	}

	public void setWoLine(Long woLine) {
		this.woLine = woLine;
	}

	@Column(name = "EQU_SPAREPART_ID", precision = 10, scale = 0)
	public Long getEquSparepartId() {
		return this.equSparepartId;
	}

	public void setEquSparepartId(Long equSparepartId) {
		this.equSparepartId = equSparepartId;
	}

	@Column(name = "MATERIAL_ID", nullable = false, precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "PLAN_ORIGINAL_ID", precision = 10, scale = 0)
	public Long getPlanOriginalId() {
		return this.planOriginalId;
	}

	public void setPlanOriginalId(Long planOriginalId) {
		this.planOriginalId = planOriginalId;
	}

	@Column(name = "APPLIED_QTY", nullable = false, precision = 15, scale = 4)
	public Double getAppliedQty() {
		return this.appliedQty;
	}

	public void setAppliedQty(Double appliedQty) {
		this.appliedQty = appliedQty;
	}

	@Column(name = "APPROVED_QTY", precision = 15, scale = 4)
	public Double getApprovedQty() {
		return this.approvedQty;
	}

	public void setApprovedQty(Double approvedQty) {
		this.approvedQty = approvedQty;
	}

	@Column(name = "PUR_QTY", precision = 15, scale = 4)
	public Double getPurQty() {
		return this.purQty;
	}

	public void setPurQty(Double purQty) {
		this.purQty = purQty;
	}

	@Column(name = "ISS_QTY", precision = 15, scale = 4)
	public Double getIssQty() {
		return this.issQty;
	}

	public void setIssQty(Double issQty) {
		this.issQty = issQty;
	}

	@Column(name = "ESTIMATED_PRICE", precision = 18, scale = 5)
	public Double getEstimatedPrice() {
		return this.estimatedPrice;
	}

	public void setEstimatedPrice(Double estimatedPrice) {
		this.estimatedPrice = estimatedPrice;
	}

	@Column(name = "PLAN_GRADE", precision = 10, scale = 0)
	public Long getPlanGrade() {
		return this.planGrade;
	}

	public void setPlanGrade(Long planGrade) {
		this.planGrade = planGrade;
	}

	@Column(name = "USAGE", length = 200)
	public String getUsage() {
		return this.usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DUE_DATE", nullable = false, length = 7)
	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Column(name = "SUPPLIER", length = 100)
	public String getSupplier() {
		return this.supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	@Column(name = "IS_GENERATED", nullable = false, length = 1)
	public String getIsGenerated() {
		return this.isGenerated;
	}

	public void setIsGenerated(String isGenerated) {
		this.isGenerated = isGenerated;
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

	@Column(name = "ITEM_CODE", length = 20)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "CANCEL_REASON", length = 200)
	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	@Column(name = "MODIFY_MEMO", length = 500)
	public String getModifyMemo() {
		return modifyMemo;
	}

	public void setModifyMemo(String modifyMemo) {
		this.modifyMemo = modifyMemo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CANCEL_DATE", nullable = true, length = 7)
	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

}