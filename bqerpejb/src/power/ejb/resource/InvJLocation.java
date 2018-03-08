package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvJLocation entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_J_LOCATION")
public class InvJLocation implements java.io.Serializable {

	// Fields

	private Long locationInvId;
	private Long materialId;
	private String whsNo;
	private String locationNo;
//	private String materialNo;
	private Double monthCost;
	private Double yearAmount;
	private Double yearCost;
	private Double openBalance;
	private Double receipt;
	private Double adjust;
	private Double issue;
	private Double reserved;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public InvJLocation() {
	}

	/** minimal constructor */
	public InvJLocation(Long locationInvId, Long materialId, String whsNo,
			String locationNo,  Double receipt,
			Double adjust, Double issue, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.locationInvId = locationInvId;
		this.materialId = materialId;
		this.whsNo = whsNo;
		this.locationNo = locationNo;
//		this.materialNo = materialNo;
		this.receipt = receipt;
		this.adjust = adjust;
		this.issue = issue;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public InvJLocation(Long locationInvId, Long materialId, String whsNo,
			String locationNo,  Double monthCost,
			Double yearAmount, Double yearCost, Double openBalance,
			Double receipt, Double adjust, Double issue, Double reserved,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.locationInvId = locationInvId;
		this.materialId = materialId;
		this.whsNo = whsNo;
		this.locationNo = locationNo;
//		this.materialNo = materialNo;
		this.monthCost = monthCost;
		this.yearAmount = yearAmount;
		this.yearCost = yearCost;
		this.openBalance = openBalance;
		this.receipt = receipt;
		this.adjust = adjust;
		this.issue = issue;
		this.reserved = reserved;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "LOCATION_INV_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getLocationInvId() {
		return this.locationInvId;
	}

	public void setLocationInvId(Long locationInvId) {
		this.locationInvId = locationInvId;
	}

	@Column(name = "MATERIAL_ID", nullable = false, precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "WHS_NO", nullable = false, length = 10)
	public String getWhsNo() {
		return this.whsNo;
	}

	public void setWhsNo(String whsNo) {
		this.whsNo = whsNo;
	}

	@Column(name = "LOCATION_NO", nullable = false, length = 10)
	public String getLocationNo() {
		return this.locationNo;
	}

	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}

//	@Column(name = "MATERIAL_NO", nullable = false, length = 30)
//	public String getMaterialNo() {
//		return this.materialNo;
//	}
//
//	public void setMaterialNo(String materialNo) {
//		this.materialNo = materialNo;
//	}

	@Column(name = "MONTH_COST", precision = 18, scale = 5)
	public Double getMonthCost() {
		return this.monthCost;
	}

	public void setMonthCost(Double monthCost) {
		this.monthCost = monthCost;
	}

	@Column(name = "YEAR_AMOUNT", precision = 18, scale = 5)
	public Double getYearAmount() {
		return this.yearAmount;
	}

	public void setYearAmount(Double yearAmount) {
		this.yearAmount = yearAmount;
	}

	@Column(name = "YEAR_COST", precision = 18, scale = 5)
	public Double getYearCost() {
		return this.yearCost;
	}

	public void setYearCost(Double yearCost) {
		this.yearCost = yearCost;
	}

	@Column(name = "OPEN_BALANCE", precision = 15, scale = 4)
	public Double getOpenBalance() {
		return this.openBalance;
	}

	public void setOpenBalance(Double openBalance) {
		this.openBalance = openBalance;
	}

	@Column(name = "RECEIPT", nullable = false, precision = 15, scale = 4)
	public Double getReceipt() {
		return this.receipt;
	}

	public void setReceipt(Double receipt) {
		this.receipt = receipt;
	}

	@Column(name = "ADJUST", nullable = false, precision = 15, scale = 4)
	public Double getAdjust() {
		return this.adjust;
	}

	public void setAdjust(Double adjust) {
		this.adjust = adjust;
	}

	@Column(name = "ISSUE", nullable = false, precision = 15, scale = 4)
	public Double getIssue() {
		return this.issue;
	}

	public void setIssue(Double issue) {
		this.issue = issue;
	}

	@Column(name = "RESERVED", precision = 15, scale = 4)
	public Double getReserved() {
		return this.reserved;
	}

	public void setReserved(Double reserved) {
		this.reserved = reserved;
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