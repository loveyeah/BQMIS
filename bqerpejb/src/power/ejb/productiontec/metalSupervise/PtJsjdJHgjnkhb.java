package power.ejb.productiontec.metalSupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJsjdJHgjnkhb entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_JSJD_J_HGJNKHB")
public class PtJsjdJHgjnkhb implements java.io.Serializable {

	// Fields

	private Long hgjnkhId;
	private Long weldId;
	private Date examDate;
	private Date fetchDate;
	private String checkUnit;
	private String sendUnit;
	private String cardCode;
	private String steelCode;
	private Date nextCheckDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJsjdJHgjnkhb() {
	}

	/** minimal constructor */
	public PtJsjdJHgjnkhb(Long hgjnkhId) {
		this.hgjnkhId = hgjnkhId;
	}

	/** full constructor */
	public PtJsjdJHgjnkhb(Long hgjnkhId, Long weldId, Date examDate,
			Date fetchDate, String checkUnit, String sendUnit, String cardCode,
			String steelCode, Date nextCheckDate, String enterpriseCode) {
		this.hgjnkhId = hgjnkhId;
		this.weldId = weldId;
		this.examDate = examDate;
		this.fetchDate = fetchDate;
		this.checkUnit = checkUnit;
		this.sendUnit = sendUnit;
		this.cardCode = cardCode;
		this.steelCode = steelCode;
		this.nextCheckDate = nextCheckDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "HGJNKH_ID", nullable = false, precision = 10, scale = 0)
	public Long getHgjnkhId() {
		return this.hgjnkhId;
	}

	public void setHgjnkhId(Long hgjnkhId) {
		this.hgjnkhId = hgjnkhId;
	}

	@Column(name = "WELD_ID", precision = 10, scale = 0)
	public Long getWeldId() {
		return this.weldId;
	}

	public void setWeldId(Long weldId) {
		this.weldId = weldId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EXAM_DATE", length = 7)
	public Date getExamDate() {
		return this.examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FETCH_DATE", length = 7)
	public Date getFetchDate() {
		return this.fetchDate;
	}

	public void setFetchDate(Date fetchDate) {
		this.fetchDate = fetchDate;
	}

	@Column(name = "CHECK_UNIT", length = 60)
	public String getCheckUnit() {
		return this.checkUnit;
	}

	public void setCheckUnit(String checkUnit) {
		this.checkUnit = checkUnit;
	}

	@Column(name = "SEND_UNIT", length = 60)
	public String getSendUnit() {
		return this.sendUnit;
	}

	public void setSendUnit(String sendUnit) {
		this.sendUnit = sendUnit;
	}

	@Column(name = "CARD_CODE", length = 15)
	public String getCardCode() {
		return this.cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	@Column(name = "STEEL_CODE", length = 15)
	public String getSteelCode() {
		return this.steelCode;
	}

	public void setSteelCode(String steelCode) {
		this.steelCode = steelCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "NEXT_CHECK_DATE", length = 7)
	public Date getNextCheckDate() {
		return this.nextCheckDate;
	}

	public void setNextCheckDate(Date nextCheckDate) {
		this.nextCheckDate = nextCheckDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}