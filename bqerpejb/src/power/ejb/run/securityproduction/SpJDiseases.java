package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJDiseases entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_DISEASES")
public class SpJDiseases implements java.io.Serializable {

	// Fields

	private Long medicalId;
	private String workerCode;
	private String contactHarm;
	private Long contactYear;
	private Date checkDate;
	private String hospital;
	private String content;
	private String checkResult;
	private String memo;
	private String enterpriseCode;
	private String depCode;

	// Constructors

	/** default constructor */
	public SpJDiseases() {
	}

	/** minimal constructor */
	public SpJDiseases(Long medicalId) {
		this.medicalId = medicalId;
	}

	/** full constructor */
	public SpJDiseases(Long medicalId, String workerCode, String contactHarm,
			Long contactYear, Date checkDate, String hospital,
			String content, String checkResult, String memo,
			String enterpriseCode,String depCode) {
		this.medicalId = medicalId;
		this.workerCode = workerCode;
		this.contactHarm = contactHarm;
		this.contactYear = contactYear;
		this.checkDate = checkDate;
		this.hospital = hospital;
		this.content = content;
		this.checkResult = checkResult;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.depCode = depCode;
	}

	// Property accessors
	@Id
	@Column(name = "MEDICAL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMedicalId() {
		return this.medicalId;
	}

	public void setMedicalId(Long medicalId) {
		this.medicalId = medicalId;
	}

	@Column(name = "WORKER_CODE", length = 16)
	public String getWorkerCode() {
		return this.workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	@Column(name = "CONTACT_HARM", length = 30)
	public String getContactHarm() {
		return this.contactHarm;
	}

	public void setContactHarm(String contactHarm) {
		this.contactHarm = contactHarm;
	}

	@Column(name = "CONTACT_YEAR", precision = 2, scale = 0)
	public Long getContactYear() {
		return this.contactYear;
	}

	public void setContactYear(Long contactYear) {
		this.contactYear = contactYear;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECK_DATE", length = 7)
	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Column(name = "HOSPITAL", length = 64)
	public String getHospital() {
		return this.hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	@Column(name = "CONTENT", length = 256)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "CHECK_RESULT", length = 128)
	public String getCheckResult() {
		return this.checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	@Column(name = "MEMO", length = 100)
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
	@Column(name = "DEP_CODE", length = 16)
	public String getDepCode() {
		return depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

}