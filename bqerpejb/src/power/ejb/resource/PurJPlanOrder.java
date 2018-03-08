package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PurJPlanOrder entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PUR_J_PLAN_ORDER")
public class PurJPlanOrder implements java.io.Serializable {

	// Fields

	private Long planOrderId;
	private Long requirementDetailId;
	private String purNo;
	private Long purOrderDetailsId;
	private Double mrQty;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public PurJPlanOrder() {
	}

	/** minimal constructor */
	public PurJPlanOrder(Long planOrderId, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.planOrderId = planOrderId;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public PurJPlanOrder(Long planOrderId, Long requirementDetailId,
			String purNo, Long purOrderDetailsId, Double mrQty,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.planOrderId = planOrderId;
		this.requirementDetailId = requirementDetailId;
		this.purNo = purNo;
		this.purOrderDetailsId = purOrderDetailsId;
		this.mrQty = mrQty;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "PLAN_ORDER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPlanOrderId() {
		return this.planOrderId;
	}

	public void setPlanOrderId(Long planOrderId) {
		this.planOrderId = planOrderId;
	}

	@Column(name = "REQUIREMENT_DETAIL_ID", precision = 10, scale = 0)
	public Long getRequirementDetailId() {
		return this.requirementDetailId;
	}

	public void setRequirementDetailId(Long requirementDetailId) {
		this.requirementDetailId = requirementDetailId;
	}

	@Column(name = "PUR_NO", length = 20)
	public String getPurNo() {
		return this.purNo;
	}

	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}

	@Column(name = "PUR_ORDER_DETAILS_ID", precision = 10, scale = 0)
	public Long getPurOrderDetailsId() {
		return this.purOrderDetailsId;
	}

	public void setPurOrderDetailsId(Long purOrderDetailsId) {
		this.purOrderDetailsId = purOrderDetailsId;
	}

	@Column(name = "MR_QTY", precision = 10, scale = 3)
	public Double getMrQty() {
		return this.mrQty;
	}

	public void setMrQty(Double mrQty) {
		this.mrQty = mrQty;
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

}