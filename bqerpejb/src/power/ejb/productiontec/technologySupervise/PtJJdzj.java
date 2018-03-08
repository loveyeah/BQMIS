package power.ejb.productiontec.technologySupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJJdzj entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_J_JDZJ", schema = "POWER")
public class PtJJdzj implements java.io.Serializable {

	// Fields

	private Long jdzjId;
	private Long jdzyId;
	private String mainTopic;
	private String zjBy;
	private Date zjDate;
	private String content;
	private String memo;
	private String fillBy;
	private Date fillDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJJdzj() {
	}

	/** minimal constructor */
	public PtJJdzj(Long jdzjId) {
		this.jdzjId = jdzjId;
	}

	/** full constructor */
	public PtJJdzj(Long jdzjId, Long jdzyId, String mainTopic, String zjBy,
			Date zjDate, String content, String memo, String fillBy,
			Date fillDate, String enterpriseCode) {
		this.jdzjId = jdzjId;
		this.jdzyId = jdzyId;
		this.mainTopic = mainTopic;
		this.zjBy = zjBy;
		this.zjDate = zjDate;
		this.content = content;
		this.memo = memo;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JDZJ_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJdzjId() {
		return this.jdzjId;
	}

	public void setJdzjId(Long jdzjId) {
		this.jdzjId = jdzjId;
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

	@Column(name = "ZJ_BY", length = 16)
	public String getZjBy() {
		return this.zjBy;
	}

	public void setZjBy(String zjBy) {
		this.zjBy = zjBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ZJ_DATE", length = 7)
	public Date getZjDate() {
		return this.zjDate;
	}

	public void setZjDate(Date zjDate) {
		this.zjDate = zjDate;
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