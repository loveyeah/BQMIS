package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * MrpJPlanGather entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MRP_J_PLAN_GATHER", schema = "POWER")
public class MrpJPlanGather implements java.io.Serializable {

	// Fields

	private Long gatherId;
	private Long materialId;
	private Double appliedQty;
	private String gatherBy;
	private Date gatherTime;
	private String requirementDetailIds;
	private String isEnquire;
	private String enterpriseCode;
	private String isUse;
	private String buyer;
	private String memo;
	private String isReturn;
	private String returnReason;

	// Constructors

	/** default constructor */
	public MrpJPlanGather() {
	}

	/** minimal constructor */
	public MrpJPlanGather(Long gatherId, String enterpriseCode, String isUse) {
		this.gatherId = gatherId;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public MrpJPlanGather(Long gatherId, Long materialId, Double appliedQty,
			String gatherBy, Date gatherTime, String requirementDetailIds,
			String isEnquire, String enterpriseCode, String isUse,
			String buyer, String memo, String isReturn, String returnReason) {
		this.gatherId = gatherId;
		this.materialId = materialId;
		this.appliedQty = appliedQty;
		this.gatherBy = gatherBy;
		this.gatherTime = gatherTime;
		this.requirementDetailIds = requirementDetailIds;
		this.isEnquire = isEnquire;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.buyer = buyer;
		this.memo = memo;
		this.isReturn = isReturn;
		this.returnReason = returnReason;
	}

	// Property accessors
	@Id
	@Column(name = "GATHER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getGatherId() {
		return this.gatherId;
	}

	public void setGatherId(Long gatherId) {
		this.gatherId = gatherId;
	}

	@Column(name = "MATERIAL_ID", precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "APPLIED_QTY", precision = 15, scale = 4)
	public Double getAppliedQty() {
		return this.appliedQty;
	}

	public void setAppliedQty(Double appliedQty) {
		this.appliedQty = appliedQty;
	}

	@Column(name = "GATHER_BY", length = 30)
	public String getGatherBy() {
		return this.gatherBy;
	}

	public void setGatherBy(String gatherBy) {
		this.gatherBy = gatherBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "GATHER_TIME", length = 7)
	public Date getGatherTime() {
		return this.gatherTime;
	}

	public void setGatherTime(Date gatherTime) {
		this.gatherTime = gatherTime;
	}

	@Column(name = "REQUIREMENT_DETAIL_IDS", length = 200)
	public String getRequirementDetailIds() {
		return this.requirementDetailIds;
	}

	public void setRequirementDetailIds(String requirementDetailIds) {
		this.requirementDetailIds = requirementDetailIds;
	}

	@Column(name = "IS_ENQUIRE", length = 1)
	public String getIsEnquire() {
		return this.isEnquire;
	}

	public void setIsEnquire(String isEnquire) {
		this.isEnquire = isEnquire;
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

	@Column(name = "BUYER", length = 30)
	public String getBuyer() {
		return this.buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "IS_RETURN", length = 1)
	public String getIsReturn() {
		return this.isReturn;
	}

	public void setIsReturn(String isReturn) {
		this.isReturn = isReturn;
	}

	@Column(name = "RETURN_REASON", length = 200)
	public String getReturnReason() {
		return this.returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

}