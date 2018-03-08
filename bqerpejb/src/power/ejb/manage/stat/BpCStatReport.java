package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCStatReport entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_STAT_REPORT")
public class BpCStatReport implements java.io.Serializable {

	// Fields

	private Long reportCode;
	private String reportName;
	private String enterpriseCode;
	private String reportType;

	// Constructors

	/** default constructor */
	public BpCStatReport() {
	}

	/** minimal constructor */
	public BpCStatReport(Long reportCode) {
		this.reportCode = reportCode;
	}

	/** full constructor */
	public BpCStatReport(Long reportCode, String reportName,
			String enterpriseCode, String reportType) {
		this.reportCode = reportCode;
		this.reportName = reportName;
		this.enterpriseCode = enterpriseCode;
		this.reportType = reportType;
	}

	// Property accessors
	@Id
	@Column(name = "REPORT_CODE", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(Long reportCode) {
		this.reportCode = reportCode;
	}

	@Column(name = "REPORT_NAME", length = 100)
	public String getReportName() {
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "REPORT_TYPE", length = 1)
	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

}