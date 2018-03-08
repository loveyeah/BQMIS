package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCReportNew entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C__REPORT_NEW", schema = "POWER")
public class BpCReportNew implements java.io.Serializable {

	// Fields

	private Long reportCode;
	private String reportName;
	private String reportType;
	private Long timeDelay;
	private String timeUnit;
	private String enterpriseCode;
	private Long displayNo;
	// Constructors

	/** default constructor */
	public BpCReportNew() {
	}

	/** minimal constructor */
	public BpCReportNew(Long reportCode, String reportName, Long timeDelay,
			String timeUnit) {
		this.reportCode = reportCode;
		this.reportName = reportName;
		this.timeDelay = timeDelay;
		this.timeUnit = timeUnit;
	}

	/** full constructor */
	public BpCReportNew(Long reportCode, String reportName, String reportType,
			Long timeDelay, String timeUnit, String enterpriseCode) {
		this.reportCode = reportCode;
		this.reportName = reportName;
		this.reportType = reportType;
		this.timeDelay = timeDelay;
		this.timeUnit = timeUnit;
		this.enterpriseCode = enterpriseCode;
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

	@Column(name = "REPORT_NAME", nullable = false, length = 100)
	public String getReportName() {
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Column(name = "REPORT_TYPE", length = 1)
	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	@Column(name = "TIME_DELAY", nullable = true, precision = 10, scale = 0)
	public Long getTimeDelay() {
		return this.timeDelay;
	}

	public void setTimeDelay(Long timeDelay) {
		this.timeDelay = timeDelay;
	}

	@Column(name = "TIME_UNIT", nullable = true, length = 1)
	public String getTimeUnit() {
		return this.timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	
	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}
}