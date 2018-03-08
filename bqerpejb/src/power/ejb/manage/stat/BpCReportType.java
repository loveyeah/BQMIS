package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCReportType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_REPORT_TYPE")
public class BpCReportType implements java.io.Serializable {

	// Fields

	private Long id;
	private Long reportId;
	// 此指标在页面别名为typeCode
	private String typeName;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCReportType() {
	}

	/** minimal constructor */
	public BpCReportType(Long id, Long reportId) {
		this.id = id;
		this.reportId = reportId;
	}

	/** full constructor */
	public BpCReportType(Long id, Long reportId, String typeName,
			String enterpriseCode) {
		this.id = id;
		this.reportId = reportId;
		this.typeName = typeName;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "REPORT_ID", nullable = false, precision = 10, scale = 0)
	public Long getReportId() {
		return this.reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	@Column(name = "TYPE_NAME", length = 30)
	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}