package power.ejb.productiontec.metalSupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJReport entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_J_REPORT")
public class PtJReport implements java.io.Serializable {

	// Fields

	private Long reportId;
	private String reportType;
	private String topic;
	private String content;
	private String year;
	private String smartDate;
	private Long timeType;
	private Long workFlowNo;
	private Long checkMark;
	private String fillBy;
	private Date fillDate;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJReport() {
	}

	/** minimal constructor */
	public PtJReport(Long reportId) {
		this.reportId = reportId;
	}

	/** full constructor */
	public PtJReport(Long reportId, String reportType, String topic,
			String content, String year, String smartDate, Long timeType,
			Long workFlowNo, Long checkMark, String fillBy, Date fillDate,
			String memo, String enterpriseCode) {
		this.reportId = reportId;
		this.reportType = reportType;
		this.topic = topic;
		this.content = content;
		this.year = year;
		this.smartDate = smartDate;
		this.timeType = timeType;
		this.workFlowNo = workFlowNo;
		this.checkMark = checkMark;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.memo = memo;
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

	@Column(name = "REPORT_TYPE", length = 100)
	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	@Column(name = "TOPIC", length = 100)
	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Column(name = "CONTENT", length = 256)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "YEAR", length = 4)
	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Column(name = "SMART_DATE", length = 2)
	public String getSmartDate() {
		return this.smartDate;
	}

	public void setSmartDate(String smartDate) {
		this.smartDate = smartDate;
	}

	@Column(name = "TIME_TYPE", precision = 1, scale = 0)
	public Long getTimeType() {
		return this.timeType;
	}

	public void setTimeType(Long timeType) {
		this.timeType = timeType;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "CHECK_MARK", length = 1)
	public Long getCheckMark() {
		return this.checkMark;
	}

	public void setCheckMark(Long checkMark) {
		this.checkMark = checkMark;
	}

	@Column(name = "FILL_BY", length = 16)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FILL_DATE", length = 7)
	public Date getFillDate() {
		return this.fillDate;
	}

	public void setFillDate(Date fillDate) {
		this.fillDate = fillDate;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}