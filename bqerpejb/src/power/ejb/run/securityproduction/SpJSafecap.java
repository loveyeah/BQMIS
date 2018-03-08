package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJSafecap entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_SAFECAP")
public class SpJSafecap implements java.io.Serializable {

	// Fields

	private Long safecapOfferId;
	private String safeCapNo;
	private String safeCapType;
	private String safeCapColor;
	private String recipientsBy;
	private String offerBy;
	private String writeBy;
	private Date recipientsDate;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSafecap() {
	}

	/** minimal constructor */
	public SpJSafecap(Long safecapOfferId) {
		this.safecapOfferId = safecapOfferId;
	}

	/** full constructor */
	public SpJSafecap(Long safecapOfferId, String safeCapNo,
			String safeCapType, String safeCapColor, String recipientsBy,
			String offerBy, String writeBy, Date recipientsDate, String memo,
			String enterpriseCode) {
		this.safecapOfferId = safecapOfferId;
		this.safeCapNo = safeCapNo;
		this.safeCapType = safeCapType;
		this.safeCapColor = safeCapColor;
		this.recipientsBy = recipientsBy;
		this.offerBy = offerBy;
		this.writeBy = writeBy;
		this.recipientsDate = recipientsDate;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SAFECAP_OFFER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSafecapOfferId() {
		return this.safecapOfferId;
	}

	public void setSafecapOfferId(Long safecapOfferId) {
		this.safecapOfferId = safecapOfferId;
	}

	@Column(name = "SAFE_CAP_NO", length = 20)
	public String getSafeCapNo() {
		return this.safeCapNo;
	}

	public void setSafeCapNo(String safeCapNo) {
		this.safeCapNo = safeCapNo;
	}

	@Column(name = "SAFE_CAP_TYPE", length = 100)
	public String getSafeCapType() {
		return this.safeCapType;
	}

	public void setSafeCapType(String safeCapType) {
		this.safeCapType = safeCapType;
	}

	@Column(name = "SAFE_CAP_COLOR", length = 8)
	public String getSafeCapColor() {
		return this.safeCapColor;
	}

	public void setSafeCapColor(String safeCapColor) {
		this.safeCapColor = safeCapColor;
	}

	@Column(name = "RECIPIENTS_BY", length = 16)
	public String getRecipientsBy() {
		return this.recipientsBy;
	}

	public void setRecipientsBy(String recipientsBy) {
		this.recipientsBy = recipientsBy;
	}

	@Column(name = "OFFER_BY", length = 16)
	public String getOfferBy() {
		return this.offerBy;
	}

	public void setOfferBy(String offerBy) {
		this.offerBy = offerBy;
	}

	@Column(name = "WRITE_BY", length = 16)
	public String getWriteBy() {
		return this.writeBy;
	}

	public void setWriteBy(String writeBy) {
		this.writeBy = writeBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECIPIENTS_DATE", length = 7)
	public Date getRecipientsDate() {
		return this.recipientsDate;
	}

	public void setRecipientsDate(Date recipientsDate) {
		this.recipientsDate = recipientsDate;
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