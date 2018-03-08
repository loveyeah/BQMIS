package power.ejb.manage.client;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJClientsQualification entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_CLIENTS_QUALIFICATION")
public class ConJClientsQualification implements java.io.Serializable {

	// Fields

	private Long qualificationId;
	private Long cliendId;
	private String aptitudeName;
	private String qualificationOrg;
	private Date sendPaperDate;
	private Date beginDate;
	private Date endDate;
	private String memo;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public ConJClientsQualification() {
	}

	/** minimal constructor */
	public ConJClientsQualification(Long qualificationId) {
		this.qualificationId = qualificationId;
	}

	/** full constructor */
	public ConJClientsQualification(Long qualificationId, Long cliendId,
			String aptitudeName, String qualificationOrg, Date sendPaperDate,
			Date beginDate, Date endDate, String memo, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode) {
		this.qualificationId = qualificationId;
		this.cliendId = cliendId;
		this.aptitudeName = aptitudeName;
		this.qualificationOrg = qualificationOrg;
		this.sendPaperDate = sendPaperDate;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.memo = memo;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "QUALIFICATION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getQualificationId() {
		return this.qualificationId;
	}

	public void setQualificationId(Long qualificationId) {
		this.qualificationId = qualificationId;
	}

	@Column(name = "CLIEND_ID", precision = 10, scale = 0)
	public Long getCliendId() {
		return this.cliendId;
	}

	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}

	@Column(name = "APTITUDE_NAME", length = 100)
	public String getAptitudeName() {
		return this.aptitudeName;
	}

	public void setAptitudeName(String aptitudeName) {
		this.aptitudeName = aptitudeName;
	}

	@Column(name = "QUALIFICATION_ORG", length = 100)
	public String getQualificationOrg() {
		return this.qualificationOrg;
	}

	public void setQualificationOrg(String qualificationOrg) {
		this.qualificationOrg = qualificationOrg;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "SEND_PAPER_DATE", length = 7)
	public Date getSendPaperDate() {
		return this.sendPaperDate;
	}

	public void setSendPaperDate(Date sendPaperDate) {
		this.sendPaperDate = sendPaperDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BEGIN_DATE", length = 7)
	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "MEMO", length = 300)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}