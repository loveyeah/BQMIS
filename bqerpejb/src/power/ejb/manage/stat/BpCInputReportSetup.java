package power.ejb.manage.stat;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * BpCInputReportSetup entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_INPUT_REPORT_SETUP")
public class BpCInputReportSetup implements java.io.Serializable {

	// Fields

	private BpCInputReportSetupId id;
	private String ifCollect;
	private String timeType;
	private String ifAutoSetup;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCInputReportSetup() {
	}

	/** minimal constructor */
	public BpCInputReportSetup(BpCInputReportSetupId id) {
		this.id = id;
	}

	/** full constructor */
	public BpCInputReportSetup(BpCInputReportSetupId id, String ifCollect,
			String timeType, String ifAutoSetup, String enterpriseCode) {
		this.id = id;
		this.ifCollect = ifCollect;
		this.timeType = timeType;
		this.ifAutoSetup = ifAutoSetup;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "reportCode", column = @Column(name = "REPORT_CODE", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "dataTimeDot", column = @Column(name = "DATA_TIME_DOT", nullable = false, precision = 22, scale = 0)) })
	public BpCInputReportSetupId getId() {
		return this.id;
	}

	public void setId(BpCInputReportSetupId id) {
		this.id = id;
	}

	@Column(name = "IF_COLLECT", length = 1)
	public String getIfCollect() {
		return this.ifCollect;
	}

	public void setIfCollect(String ifCollect) {
		this.ifCollect = ifCollect;
	}

	@Column(name = "TIME_TYPE", length = 1)
	public String getTimeType() {
		return this.timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	@Column(name = "IF_AUTO_SETUP", length = 1)
	public String getIfAutoSetup() {
		return this.ifAutoSetup;
	}

	public void setIfAutoSetup(String ifAutoSetup) {
		this.ifAutoSetup = ifAutoSetup;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}