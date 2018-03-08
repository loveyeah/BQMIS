package power.ejb.manage.stat;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ZbCReport entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "ZB_C_REPORT")
public class ZbCReport implements java.io.Serializable {

	// Fields

	private Long reportId;
	private String reportCode;
	private String reportName;
	private String faReprotCode;
	private String reportType;
	private String lastModifierBy;
	private Date lastModifierDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public ZbCReport() {
	}

	/** minimal constructor */
	public ZbCReport(Long reportId) {
		this.reportId = reportId;
	}

	/** full constructor */
	public ZbCReport(Long reportId, String reportCode, String reportName,
			String faReprotCode, String reportType, String lastModifierBy,
			Date lastModifierDate, String isUse, String enterpriseCode) {
		this.reportId = reportId;
		this.reportCode = reportCode;
		this.reportName = reportName;
		this.faReprotCode = faReprotCode;
		this.reportType = reportType;
		this.lastModifierBy = lastModifierBy;
		this.lastModifierDate = lastModifierDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "REPORT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getReportId() {
		return this.reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	@Column(name = "REPORT_CODE", length = 20)
	public String getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	@Column(name = "REPORT_NAME", length = 100)
	public String getReportName() {
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Column(name = "FA_REPROT_CODE", length = 20)
	public String getFaReprotCode() {
		return this.faReprotCode;
	}

	public void setFaReprotCode(String faReprotCode) {
		this.faReprotCode = faReprotCode;
	}

	@Column(name = "REPORT_TYPE", length = 1)
	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	@Column(name = "LAST_MODIFIER_BY", length = 20)
	public String getLastModifierBy() {
		return this.lastModifierBy;
	}

	public void setLastModifierBy(String lastModifierBy) {
		this.lastModifierBy = lastModifierBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFIER_DATE", length = 7)
	public Date getLastModifierDate() {
		return this.lastModifierDate;
	}

	public void setLastModifierDate(Date lastModifierDate) {
		this.lastModifierDate = lastModifierDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}