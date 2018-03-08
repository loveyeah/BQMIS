package power.ejb.productiontec.technologySupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJJdhdjl entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_J_JDHDJL")
public class PtJJdhdjl implements java.io.Serializable {

	// Fields

	private Long jdhdId;
	private Long jdzyId;
	private String mainTopic;
	private Date hdDate;
	private String emceeMan;
	private String joinMan;
	private String place;
	private String content;
	private String memo;
	private String fillBy;
	private Date fillDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJJdhdjl() {
	}

	/** minimal constructor */
	public PtJJdhdjl(Long jdhdId) {
		this.jdhdId = jdhdId;
	}

	/** full constructor */
	public PtJJdhdjl(Long jdhdId, Long jdzyId, String mainTopic, Date hdDate,
			String emceeMan, String joinMan, String place, String content,
			String memo, String fillBy, Date fillDate, String enterpriseCode) {
		this.jdhdId = jdhdId;
		this.jdzyId = jdzyId;
		this.mainTopic = mainTopic;
		this.hdDate = hdDate;
		this.emceeMan = emceeMan;
		this.joinMan = joinMan;
		this.place = place;
		this.content = content;
		this.memo = memo;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JDHD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJdhdId() {
		return this.jdhdId;
	}

	public void setJdhdId(Long jdhdId) {
		this.jdhdId = jdhdId;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "HD_DATE", length = 7)
	public Date getHdDate() {
		return this.hdDate;
	}

	public void setHdDate(Date hdDate) {
		this.hdDate = hdDate;
	}

	@Column(name = "EMCEE_MAN", length = 20)
	public String getEmceeMan() {
		return this.emceeMan;
	}

	public void setEmceeMan(String emceeMan) {
		this.emceeMan = emceeMan;
	}

	@Column(name = "JOIN_MAN", length = 200)
	public String getJoinMan() {
		return this.joinMan;
	}

	public void setJoinMan(String joinMan) {
		this.joinMan = joinMan;
	}

	@Column(name = "PLACE", length = 256)
	public String getPlace() {
		return this.place;
	}

	public void setPlace(String place) {
		this.place = place;
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