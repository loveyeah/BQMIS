package power.ejb.manage.stat;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpCSmallitemReport entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_SMALLITEM_REPORT")
public class BpCSmallitemReport implements java.io.Serializable {

	// Fields

	private Long reportId;
	private String reportName;
	private String dataType;
	private String rowHeadName;
	private Long columnNum;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public BpCSmallitemReport() {
	}

	/** minimal constructor */
	public BpCSmallitemReport(Long reportId) {
		this.reportId = reportId;
	}

	/** full constructor */
	public BpCSmallitemReport(Long reportId, String reportName,
			String dataType, String rowHeadName, Long columnNum,
			String modifyBy, Date modifyDate, String enterpriseCode,
			String isUse) {
		this.reportId = reportId;
		this.reportName = reportName;
		this.dataType = dataType;
		this.rowHeadName = rowHeadName;
		this.columnNum = columnNum;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
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

	@Column(name = "REPORT_NAME", length = 100)
	public String getReportName() {
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Column(name = "DATA_TYPE", length = 1)
	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "ROW_HEAD_NAME", length = 40)
	public String getRowHeadName() {
		return this.rowHeadName;
	}

	public void setRowHeadName(String rowHeadName) {
		this.rowHeadName = rowHeadName;
	}

	@Column(name = "COLUMN_NUM", precision = 10, scale = 0)
	public Long getColumnNum() {
		return this.columnNum;
	}

	public void setColumnNum(Long columnNum) {
		this.columnNum = columnNum;
	}

	@Column(name = "MODIFY_BY", length = 15)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
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