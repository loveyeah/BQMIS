package power.ejb.manage.project;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PrjJRegister entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRJ_J_REGISTER", schema = "POWER")
public class PrjJRegister implements java.io.Serializable {

	// Fields

	private Long prjId;
	private String prjNo;
	private String prjName;
	private String prjDept;
	private Long prjTypeId;
	private Double applyFunds;
	private Double approvedFunds;
	private String isFundsFinish;
	private String prjYear;
	private String duration;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;
	private String isUse;
	private String prjBy;
	private Long statusId;
	private String annex;

	// Constructors

	/** default constructor */
	public PrjJRegister() {
	}

	/** minimal constructor */
	public PrjJRegister(Long prjId) {
		this.prjId = prjId;
	}

	/** full constructor */
	public PrjJRegister(Long prjId, String prjNo, String prjName,
			String prjDept, Long prjTypeId, Double applyFunds,
			Double approvedFunds, String isFundsFinish, String prjYear,
			String duration, String entryBy, Date entryDate,
			String enterpriseCode, String isUse, String prjBy, Long statusId,
			String annex) {
		this.prjId = prjId;
		this.prjNo = prjNo;
		this.prjName = prjName;
		this.prjDept = prjDept;
		this.prjTypeId = prjTypeId;
		this.applyFunds = applyFunds;
		this.approvedFunds = approvedFunds;
		this.isFundsFinish = isFundsFinish;
		this.prjYear = prjYear;
		this.duration = duration;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.prjBy = prjBy;
		this.statusId = statusId;
		this.annex = annex;
	}

	// Property accessors
	@Id
	@Column(name = "PRJ_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrjId() {
		return this.prjId;
	}

	public void setPrjId(Long prjId) {
		this.prjId = prjId;
	}

	@Column(name = "PRJ_NO", length = 50)
	public String getPrjNo() {
		return this.prjNo;
	}

	public void setPrjNo(String prjNo) {
		this.prjNo = prjNo;
	}

	@Column(name = "PRJ_NAME", length = 100)
	public String getPrjName() {
		return this.prjName;
	}

	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}

	@Column(name = "PRJ_DEPT", length = 20)
	public String getPrjDept() {
		return this.prjDept;
	}

	public void setPrjDept(String prjDept) {
		this.prjDept = prjDept;
	}

	@Column(name = "PRJ_TYPE_ID", precision = 10, scale = 0)
	public Long getPrjTypeId() {
		return this.prjTypeId;
	}

	public void setPrjTypeId(Long prjTypeId) {
		this.prjTypeId = prjTypeId;
	}

	@Column(name = "APPLY_FUNDS", precision = 15, scale = 4)
	public Double getApplyFunds() {
		return this.applyFunds;
	}

	public void setApplyFunds(Double applyFunds) {
		this.applyFunds = applyFunds;
	}

	@Column(name = "APPROVED_FUNDS", precision = 15, scale = 4)
	public Double getApprovedFunds() {
		return this.approvedFunds;
	}

	public void setApprovedFunds(Double approvedFunds) {
		this.approvedFunds = approvedFunds;
	}

	@Column(name = "IS_FUNDS_FINISH", length = 1)
	public String getIsFundsFinish() {
		return this.isFundsFinish;
	}

	public void setIsFundsFinish(String isFundsFinish) {
		this.isFundsFinish = isFundsFinish;
	}

	@Column(name = "PRJ_YEAR", length = 4)
	public String getPrjYear() {
		return this.prjYear;
	}

	public void setPrjYear(String prjYear) {
		this.prjYear = prjYear;
	}

	@Column(name = "DURATION", length = 20)
	public String getDuration() {
		return this.duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "PRJ_BY", length = 30)
	public String getPrjBy() {
		return this.prjBy;
	}

	public void setPrjBy(String prjBy) {
		this.prjBy = prjBy;
	}

	@Column(name = "STATUS_ID", precision = 2, scale = 0)
	public Long getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}

	@Column(name = "ANNEX", length = 400)
	public String getAnnex() {
		return this.annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

}