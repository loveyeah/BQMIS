package power.ejb.productiontec.insulation;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJyjdJQxdj entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JYJD_J_QXDJ")
public class PtJyjdJQxdj implements java.io.Serializable {

	// Fields

	private Long jyqxId;
	private String accidentTitle;
	private String equCode;
	private String equName;
	private Date findTime;
	private Date clearTime;
	private String reasonAnalyse;
	private String bugStatus;
	private String memo;
	private String annex;
	private String fillBy;
	private Date fillDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJyjdJQxdj() {
	}

	/** minimal constructor */
	public PtJyjdJQxdj(Long jyqxId) {
		this.jyqxId = jyqxId;
	}

	/** full constructor */
	public PtJyjdJQxdj(Long jyqxId, String accidentTitle, String equCode,
			String equName, Date findTime, Date clearTime,
			String reasonAnalyse, String bugStatus, String memo, String annex,
			String fillBy, Date fillDate, String enterpriseCode) {
		this.jyqxId = jyqxId;
		this.accidentTitle = accidentTitle;
		this.equCode = equCode;
		this.equName = equName;
		this.findTime = findTime;
		this.clearTime = clearTime;
		this.reasonAnalyse = reasonAnalyse;
		this.bugStatus = bugStatus;
		this.memo = memo;
		this.annex = annex;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JYQX_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJyqxId() {
		return this.jyqxId;
	}

	public void setJyqxId(Long jyqxId) {
		this.jyqxId = jyqxId;
	}

	@Column(name = "ACCIDENT_TITLE", length = 200)
	public String getAccidentTitle() {
		return this.accidentTitle;
	}

	public void setAccidentTitle(String accidentTitle) {
		this.accidentTitle = accidentTitle;
	}

	@Column(name = "EQU_NAME", length = 200)
	public String getEquName() {
		return this.equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FIND_TIME", length = 7)
	public Date getFindTime() {
		return this.findTime;
	}

	public void setFindTime(Date findTime) {
		this.findTime = findTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CLEAR_TIME", length = 7)
	public Date getClearTime() {
		return this.clearTime;
	}

	public void setClearTime(Date clearTime) {
		this.clearTime = clearTime;
	}

	@Column(name = "REASON_ANALYSE", length = 1000)
	public String getReasonAnalyse() {
		return this.reasonAnalyse;
	}

	public void setReasonAnalyse(String reasonAnalyse) {
		this.reasonAnalyse = reasonAnalyse;
	}

	@Column(name = "BUG_STATUS", length = 1)
	public String getBugStatus() {
		return this.bugStatus;
	}

	public void setBugStatus(String bugStatus) {
		this.bugStatus = bugStatus;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ANNEX", length = 256)
	public String getAnnex() {
		return this.annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	@Column(name = "FILL_BY", length = 16)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
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
	
	@Column(name = "EQU_CODE", length = 30)
	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

}