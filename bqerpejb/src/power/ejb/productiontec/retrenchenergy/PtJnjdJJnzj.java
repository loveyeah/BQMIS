package power.ejb.productiontec.retrenchenergy;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJnjdJJnzj entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_JNJD_J_JNZJ")
public class PtJnjdJJnzj implements java.io.Serializable {

	// Fields

	private Long summaryId;
	private String title;
	private Date year;
	private String content;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJnjdJJnzj() {
	}

	/** minimal constructor */
	public PtJnjdJJnzj(Long summaryId) {
		this.summaryId = summaryId;
	}

	/** full constructor */
	public PtJnjdJJnzj(Long summaryId, String title, Date year, String content,
			String memo, String enterpriseCode) {
		this.summaryId = summaryId;
		this.title = title;
		this.year = year;
		this.content = content;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SUMMARY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSummaryId() {
		return this.summaryId;
	}

	public void setSummaryId(Long summaryId) {
		this.summaryId = summaryId;
	}

	@Column(name = "TITLE", length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "YEAR", length = 7)
	public Date getYear() {
		return this.year;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	@Column(name = "CONTENT", length = 256)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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