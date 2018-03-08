package power.ejb.productiontec.retrenchenergy;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJnjdJJnfx entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JNJD_J_JNFX")
public class PtJnjdJJnfx implements java.io.Serializable {

	// Fields

	private Long analyseId;
	private String title;
	private Date month;
	private String content;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJnjdJJnfx() {
	}

	/** minimal constructor */
	public PtJnjdJJnfx(Long analyseId) {
		this.analyseId = analyseId;
	}

	/** full constructor */
	public PtJnjdJJnfx(Long analyseId, String title, Date month,
			String content, String memo, String enterpriseCode) {
		this.analyseId = analyseId;
		this.title = title;
		this.month = month;
		this.content = content;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ANALYSE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAnalyseId() {
		return this.analyseId;
	}

	public void setAnalyseId(Long analyseId) {
		this.analyseId = analyseId;
	}

	@Column(name = "TITLE", length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MONTH", length = 7)
	public Date getMonth() {
		return this.month;
	}

	public void setMonth(Date month) {
		this.month = month;
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