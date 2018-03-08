package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJBriefnews entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_BRIEFNEWS", schema = "POWER")
public class SpJBriefnews implements java.io.Serializable {

	// Fields

	private Long briefnewsId;
	private Date month;
	private Long issue;
	private String content;
	private String commonBy;
	private Date commonDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJBriefnews() {
	}

	/** minimal constructor */
	public SpJBriefnews(Long briefnewsId) {
		this.briefnewsId = briefnewsId;
	}

	/** full constructor */
	public SpJBriefnews(Long briefnewsId, Date month, Long issue,
			String content, String commonBy, Date commonDate,
			String enterpriseCode) {
		this.briefnewsId = briefnewsId;
		this.month = month;
		this.issue = issue;
		this.content = content;
		this.commonBy = commonBy;
		this.commonDate = commonDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BRIEFNEWS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBriefnewsId() {
		return this.briefnewsId;
	}

	public void setBriefnewsId(Long briefnewsId) {
		this.briefnewsId = briefnewsId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MONTH", length = 7)
	public Date getMonth() {
		return this.month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	@Column(name = "ISSUE", precision = 10, scale = 0)
	public Long getIssue() {
		return this.issue;
	}

	public void setIssue(Long issue) {
		this.issue = issue;
	}

	@Column(name = "CONTENT", length = 2000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "COMMON_BY", length = 16)
	public String getCommonBy() {
		return this.commonBy;
	}

	public void setCommonBy(String commonBy) {
		this.commonBy = commonBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "COMMON_DATE", length = 7)
	public Date getCommonDate() {
		return this.commonDate;
	}

	public void setCommonDate(Date commonDate) {
		this.commonDate = commonDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}