package power.ejb.manage.project;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PrjJStartContent entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRJ_J_START_CONTENT")
public class PrjJStartContent implements java.io.Serializable {

	// Fields

	private Long reoprtContentId;
	private Long reportId;
	private String reoprtContent;
	private Long orderBy;
	private String contentType;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public PrjJStartContent() {
	}

	/** minimal constructor */
	public PrjJStartContent(Long reoprtContentId) {
		this.reoprtContentId = reoprtContentId;
	}

	/** full constructor */
	public PrjJStartContent(Long reoprtContentId, Long reportId,
			String reoprtContent, Long orderBy, String contentType,
			String entryBy, Date entryDate, String enterpriseCode, String isUse) {
		this.reoprtContentId = reoprtContentId;
		this.reportId = reportId;
		this.reoprtContent = reoprtContent;
		this.orderBy = orderBy;
		this.contentType = contentType;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "REOPRT_CONTENT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getReoprtContentId() {
		return this.reoprtContentId;
	}

	public void setReoprtContentId(Long reoprtContentId) {
		this.reoprtContentId = reoprtContentId;
	}

	@Column(name = "REPORT_ID", precision = 10, scale = 0)
	public Long getReportId() {
		return this.reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	@Column(name = "REOPRT_CONTENT", length = 100)
	public String getReoprtContent() {
		return this.reoprtContent;
	}

	public void setReoprtContent(String reoprtContent) {
		this.reoprtContent = reoprtContent;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "CONTENT_TYPE", length = 1)
	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
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

}