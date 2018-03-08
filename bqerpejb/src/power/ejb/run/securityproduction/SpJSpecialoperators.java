package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJSpecialoperators entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_SPECIALOPERATORS")
public class SpJSpecialoperators implements java.io.Serializable {

	// Fields

	private Long offerId;
	private String workerCode;
	private String projectOperation;
	private Double postYear;
	private String offerName;
	private String offerCode;
	private Date offerDate;
	private Date offerStartDate;
	private Date offerEndDate;
	private Date medicalDate;
	private String medicalResult;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSpecialoperators() {
	}

	/** minimal constructor */
	public SpJSpecialoperators(Long offerId) {
		this.offerId = offerId;
	}

	/** full constructor */
	public SpJSpecialoperators(Long offerId, String workerCode,
			String projectOperation, Double postYear, String offerName,
			String offerCode, Date offerDate, Date offerStartDate,
			Date offerEndDate, Date medicalDate, String medicalResult,
			String memo, String enterpriseCode) {
		this.offerId = offerId;
		this.workerCode = workerCode;
		this.projectOperation = projectOperation;
		this.postYear = postYear;
		this.offerName = offerName;
		this.offerCode = offerCode;
		this.offerDate = offerDate;
		this.offerStartDate = offerStartDate;
		this.offerEndDate = offerEndDate;
		this.medicalDate = medicalDate;
		this.medicalResult = medicalResult;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "OFFER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getOfferId() {
		return this.offerId;
	}

	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}

	@Column(name = "WORKER_CODE", length = 16)
	public String getWorkerCode() {
		return this.workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	@Column(name = "PROJECT_OPERATION", length = 32)
	public String getProjectOperation() {
		return this.projectOperation;
	}

	public void setProjectOperation(String projectOperation) {
		this.projectOperation = projectOperation;
	}

	@Column(name = "POST_YEAR", precision = 2, scale = 0)
	public Double getPostYear() {
		return this.postYear;
	}

	public void setPostYear(Double postYear) {
		this.postYear = postYear;
	}

	@Column(name = "OFFER_NAME", length = 100)
	public String getOfferName() {
		return this.offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	@Column(name = "OFFER_CODE", length = 50)
	public String getOfferCode() {
		return this.offerCode;
	}

	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OFFER_DATE", length = 7)
	public Date getOfferDate() {
		return this.offerDate;
	}

	public void setOfferDate(Date offerDate) {
		this.offerDate = offerDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OFFER_START_DATE", length = 7)
	public Date getOfferStartDate() {
		return this.offerStartDate;
	}

	public void setOfferStartDate(Date offerStartDate) {
		this.offerStartDate = offerStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OFFER_END_DATE", length = 7)
	public Date getOfferEndDate() {
		return this.offerEndDate;
	}

	public void setOfferEndDate(Date offerEndDate) {
		this.offerEndDate = offerEndDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MEDICAL_DATE", length = 7)
	public Date getMedicalDate() {
		return this.medicalDate;
	}

	public void setMedicalDate(Date medicalDate) {
		this.medicalDate = medicalDate;
	}

	@Column(name = "MEDICAL_RESULT", length = 64)
	public String getMedicalResult() {
		return this.medicalResult;
	}

	public void setMedicalResult(String medicalResult) {
		this.medicalResult = medicalResult;
	}

	@Column(name = "MEMO", length = 512)
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