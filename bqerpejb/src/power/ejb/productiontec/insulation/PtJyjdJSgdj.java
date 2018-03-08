package power.ejb.productiontec.insulation;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJyjdJSgdj entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JYJD_J_SGDJ")
public class PtJyjdJSgdj implements java.io.Serializable {

	// Fields

	private Long jysgId;
	private String accidentTitle;
	private String equCode;
	private String equName;
	private Date happenDate;
	private Date handleDate;
	private String reasonAnalyse;
	private String handleStatus;
	private String memo;
	private String annex;
	private String fillBy;
	private Date fillDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJyjdJSgdj() {
	}

	/** minimal constructor */
	public PtJyjdJSgdj(Long jysgId) {
		this.jysgId = jysgId;
	}

	/** full constructor */
	public PtJyjdJSgdj(Long jysgId, String accidentTitle, String equCode,
			String equName, Date happenDate, Date handleDate,
			String reasonAnalyse, String handleStatus, String memo,
			String annex, String fillBy, Date fillDate, String enterpriseCode) {
		this.jysgId = jysgId;
		this.accidentTitle = accidentTitle;
		this.equCode = equCode;
		this.equName = equName;
		this.happenDate = happenDate;
		this.handleDate = handleDate;
		this.reasonAnalyse = reasonAnalyse;
		this.handleStatus = handleStatus;
		this.memo = memo;
		this.annex = annex;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JYSG_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJysgId() {
		return this.jysgId;
	}

	public void setJysgId(Long jysgId) {
		this.jysgId = jysgId;
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
	@Column(name = "HAPPEN_DATE", length = 7)
	public Date getHappenDate() {
		return this.happenDate;
	}

	public void setHappenDate(Date happenDate) {
		this.happenDate = happenDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HANDLE_DATE", length = 7)
	public Date getHandleDate() {
		return this.handleDate;
	}

	public void setHandleDate(Date handleDate) {
		this.handleDate = handleDate;
	}

	@Column(name = "REASON_ANALYSE", length = 1000)
	public String getReasonAnalyse() {
		return this.reasonAnalyse;
	}

	public void setReasonAnalyse(String reasonAnalyse) {
		this.reasonAnalyse = reasonAnalyse;
	}

	@Column(name = "HANDLE_STATUS", length = 1)
	public String getHandleStatus() {
		return this.handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
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