package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJSafetyMonth entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_SAFETY_MONTH", schema = "POWER")
public class SpJSafetyMonth implements java.io.Serializable {

	// Fields

	private Long safetymonthId;
	private Date month;
	private String subject;
	private String content;
	private String summary;
	private String fillBy;
	private Date fillDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSafetyMonth() {
	}

	/** minimal constructor */
	public SpJSafetyMonth(Long safetymonthId) {
		this.safetymonthId = safetymonthId;
	}

	/** full constructor */
	public SpJSafetyMonth(Long safetymonthId, Date month, String subject,
			String content, String summary, String fillBy, Date fillDate,
			String enterpriseCode) {
		this.safetymonthId = safetymonthId;
		this.month = month;
		this.subject = subject;
		this.content = content;
		this.summary = summary;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SAFETYMONTH_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSafetymonthId() {
		return this.safetymonthId;
	}

	public void setSafetymonthId(Long safetymonthId) {
		this.safetymonthId = safetymonthId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MONTH", length = 7)
	public Date getMonth() {
		return this.month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	@Column(name = "SUBJECT", length = 100)
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "CONTENT", length = 2000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "SUMMARY", length = 1000)
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}