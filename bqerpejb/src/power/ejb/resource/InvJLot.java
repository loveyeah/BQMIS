package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvJLot entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_J_LOT")
public class InvJLot implements java.io.Serializable {

	// Fields

	private Long lotInvId;
	private Long materialId;
	private String lotNo;
	private String whsNo;
	private String locationNo;
	private Double monthAmount;
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
	public InvJLot() {
	}

	/** minimal constructor */
	public InvJLot(Long lotInvId, Long materialId, String lotNo, String whsNo,
			Double openBalance, Double receipt, Double adjust, Double issue,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.lotInvId = lotInvId;
		this.materialId = materialId;
		this.lotNo = lotNo;
		this.whsNo = whsNo;
		this.openBalance = openBalance;
		this.receipt = receipt;
		this.adjust = adjust;
		this.issue = issue;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public InvJLot(Long lotInvId, Long materialId, String lotNo, String whsNo,
			String locationNo, Double monthAmount, Double monthCost,
			Double yearAmount, Double yearCost, Double openBalance,
			Double receipt, Double adjust, Double issue, Double reserved,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.lotInvId = lotInvId;
		this.materialId = materialId;
		this.lotNo = lotNo;
		this.whsNo = whsNo;
		this.locationNo = locationNo;
		this.monthAmount = monthAmount;
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
	@Column(name = "LOT_INV_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getLotInvId() {
		return this.lotInvId;
	}

	public void setLotInvId(Long lotInvId) {
		this.lotInvId = lotInvId;
	}

	@Column(name = "MATERIAL_ID", nullable = false, precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "LOT_NO", nullable = false, length = 30)
	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	@Column(name = "WHS_NO", nullable = false, length = 10)
	public String getWhsNo() {
		return this.whsNo;
	}

	public void setWhsNo(String whsNo) {
		this.whsNo = whsNo;
	}

	@Column(name = "LOCATION_NO", length = 10)
	public String getLocationNo() {
		return this.locationNo;
	}

	public void setLocationNo(String locationNo) {
		this.locationNo = locationNo;
	}

	@Column(name = "MONTH_AMOUNT", precision = 18, scale = 5)
	public Double getMonthAmount() {
		return this.monthAmount;
	}

	public void setMonthAmount(Double monthAmount) {
		this.monthAmount = monthAmount;
	}

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

	@Column(name = "OPEN_BALANCE", nullable = false, precision = 15, scale = 4)
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