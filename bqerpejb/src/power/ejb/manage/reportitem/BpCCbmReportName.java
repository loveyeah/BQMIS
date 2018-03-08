package power.ejb.manage.reportitem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCCbmReportName entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_CBM_REPORT_NAME", schema = "POWER")
public class BpCCbmReportName implements java.io.Serializable {

	// Fields

	private Long reportId;
	private String reportName;
	private String reportType;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCCbmReportName() {
	}

	/** minimal constructor */
	public BpCCbmReportName(Long reportId) {
		this.reportId = reportId;
	}

	/** full constructor */
	public BpCCbmReportName(Long reportId, String reportName,
			String reportType, String isUse, String enterpriseCode) {
		this.reportId = reportId;
		this.reportName = reportName;
		this.reportType = reportType;
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

	@Column(name = "REPORT_NAME", length = 80)
	public String getReportName() {
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Column(name = "REPORT_TYPE", length = 2)
	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
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