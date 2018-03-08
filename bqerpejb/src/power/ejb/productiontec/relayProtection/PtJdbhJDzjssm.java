package power.ejb.productiontec.relayProtection;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJdbhJDzjssm entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JDBH_J_DZJSSM", schema = "POWER")
public class PtJdbhJDzjssm implements java.io.Serializable {

	// Fields

	private Long dzjssmId;
	private String jssmName;
	private String content;
	private String memo;
	private String fillBy;
	private Date fillDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhJDzjssm() {
	}

	/** minimal constructor */
	public PtJdbhJDzjssm(Long dzjssmId) {
		this.dzjssmId = dzjssmId;
	}

	/** full constructor */
	public PtJdbhJDzjssm(Long dzjssmId, String jssmName, String content,
			String memo, String fillBy, Date fillDate, String enterpriseCode) {
		this.dzjssmId = dzjssmId;
		this.jssmName = jssmName;
		this.content = content;
		this.memo = memo;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DZJSSM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDzjssmId() {
		return this.dzjssmId;
	}

	public void setDzjssmId(Long dzjssmId) {
		this.dzjssmId = dzjssmId;
	}

	@Column(name = "JSSM_NAME", length = 100)
	public String getJssmName() {
		return this.jssmName;
	}

	public void setJssmName(String jssmName) {
		this.jssmName = jssmName;
	}

	@Column(name = "CONTENT", length = 256)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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