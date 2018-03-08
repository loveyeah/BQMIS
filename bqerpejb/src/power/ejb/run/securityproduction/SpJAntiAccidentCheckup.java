package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJAntiAccidentCheckup entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_ANTI_ACCIDENT_CHECKUP", schema = "POWER")
public class SpJAntiAccidentCheckup implements java.io.Serializable {

	// Fields

	private Long checkupId;
	private String measureCode;
	private String isProblem;
	private String checkBy;
	private Date checkDate;
	private String approveBy;
	private Date approveDate;
	private String approveText;
	private String approveStatus;
	private String season;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public SpJAntiAccidentCheckup() {
	}

	/** minimal constructor */
	public SpJAntiAccidentCheckup(Long checkupId) {
		this.checkupId = checkupId;
	}

	/** full constructor */
	public SpJAntiAccidentCheckup(Long checkupId, String measureCode,
			String isProblem, String checkBy, Date checkDate, String approveBy,
			Date approveDate, String approveText, String approveStatus,
			String season, String modifyBy, Date modifyDate,
			String enterpriseCode, String isUse) {
		this.checkupId = checkupId;
		this.measureCode = measureCode;
		this.isProblem = isProblem;
		this.checkBy = checkBy;
		this.checkDate = checkDate;
		this.approveBy = approveBy;
		this.approveDate = approveDate;
		this.approveText = approveText;
		this.approveStatus = approveStatus;
		this.season = season;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "CHECKUP_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCheckupId() {
		return this.checkupId;
	}

	public void setCheckupId(Long checkupId) {
		this.checkupId = checkupId;
	}

	@Column(name = "MEASURE_CODE", length = 6)
	public String getMeasureCode() {
		return this.measureCode;
	}

	public void setMeasureCode(String measureCode) {
		this.measureCode = measureCode;
	}

	@Column(name = "IS_PROBLEM", length = 1)
	public String getIsProblem() {
		return this.isProblem;
	}

	public void setIsProblem(String isProblem) {
		this.isProblem = isProblem;
	}

	@Column(name = "CHECK_BY", length = 30)
	public String getCheckBy() {
		return this.checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CHECK_DATE", length = 7)
	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Column(name = "APPROVE_BY", length = 30)
	public String getApproveBy() {
		return this.approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "APPROVE_DATE", length = 7)
	public Date getApproveDate() {
		return this.approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	@Column(name = "APPROVE_TEXT", length = 100)
	public String getApproveText() {
		return this.approveText;
	}

	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}

	@Column(name = "APPROVE_STATUS", length = 1)
	public String getApproveStatus() {
		return this.approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	@Column(name = "SEASON", length = 5)
	public String getSeason() {
		return this.season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
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

}