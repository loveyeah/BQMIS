package power.ejb.productiontec.technologySupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJSjfx entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_J_SJFX")
public class PtJSjfx implements java.io.Serializable {

	// Fields

	private Long sjfxId;
	private Long jdzyId;
	private String mainTopic;
	private String fxBy;
	private Date fxDate;
	private String content;
	private String memo;
	private String fillBy;
	private Date fillDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJSjfx() {
	}

	/** minimal constructor */
	public PtJSjfx(Long sjfxId) {
		this.sjfxId = sjfxId;
	}

	/** full constructor */
	public PtJSjfx(Long sjfxId, Long jdzyId, String mainTopic, String fxBy,
			Date fxDate, String content, String memo, String fillBy,
			Date fillDate, String enterpriseCode) {
		this.sjfxId = sjfxId;
		this.jdzyId = jdzyId;
		this.mainTopic = mainTopic;
		this.fxBy = fxBy;
		this.fxDate = fxDate;
		this.content = content;
		this.memo = memo;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SJFX_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSjfxId() {
		return this.sjfxId;
	}

	public void setSjfxId(Long sjfxId) {
		this.sjfxId = sjfxId;
	}

	@Column(name = "JDZY_ID", precision = 2, scale = 0)
	public Long getJdzyId() {
		return this.jdzyId;
	}

	public void setJdzyId(Long jdzyId) {
		this.jdzyId = jdzyId;
	}

	@Column(name = "MAIN_TOPIC", length = 100)
	public String getMainTopic() {
		return this.mainTopic;
	}

	public void setMainTopic(String mainTopic) {
		this.mainTopic = mainTopic;
	}

	@Column(name = "FX_BY", length = 16)
	public String getFxBy() {
		return this.fxBy;
	}

	public void setFxBy(String fxBy) {
		this.fxBy = fxBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FX_DATE", length = 7)
	public Date getFxDate() {
		return this.fxDate;
	}

	public void setFxDate(Date fxDate) {
		this.fxDate = fxDate;
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