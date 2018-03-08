package power.ejb.manage.stat.form;

import java.util.Date;

public class BpCSmallitemReportForm implements java.io.Serializable {
	private Long reportId;
	private String reportName;
	private String dataType;
	private String rowHeadName;
	private Long columnNum;
	private String modifyBy;
	private Date modifyDate;
	private String enterpriseCode;
	private String isUse;
	private String typeCode;

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getRowHeadName() {
		return rowHeadName;
	}

	public void setRowHeadName(String rowHeadName) {
		this.rowHeadName = rowHeadName;
	}

	public Long getColumnNum() {
		return columnNum;
	}

	public void setColumnNum(Long columnNum) {
		this.columnNum = columnNum;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

}
