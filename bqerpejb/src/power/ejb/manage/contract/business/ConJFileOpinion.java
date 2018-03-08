package power.ejb.manage.contract.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJFileOpinion entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_FILE_OPINION")
public class ConJFileOpinion implements java.io.Serializable {

	// Fields

	private Long opinionId;
	private Long keyId;
	private String fileType;
	private String opinion;
	private String gdWorker;
	private Date withdrawalTime;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public ConJFileOpinion() {
	}

	/** minimal constructor */
	public ConJFileOpinion(Long opinionId, Long keyId, String fileType,
			String enterpriseCode, String isUse) {
		this.opinionId = opinionId;
		this.keyId = keyId;
		this.fileType = fileType;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public ConJFileOpinion(Long opinionId, Long keyId, String fileType,
			String opinion, String gdWorker, Date withdrawalTime,
			String enterpriseCode, String isUse) {
		this.opinionId = opinionId;
		this.keyId = keyId;
		this.fileType = fileType;
		this.opinion = opinion;
		this.gdWorker = gdWorker;
		this.withdrawalTime = withdrawalTime;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "OPINION_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getOpinionId() {
		return this.opinionId;
	}

	public void setOpinionId(Long opinionId) {
		this.opinionId = opinionId;
	}

	@Column(name = "KEY_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getKeyId() {
		return this.keyId;
	}

	public void setKeyId(Long keyId) {
		this.keyId = keyId;
	}

	@Column(name = "FILE_TYPE", unique = false, nullable = false, insertable = true, updatable = true, length = 5)
	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Column(name = "OPINION", unique = false, nullable = true, insertable = true, updatable = true, length = 1000)
	public String getOpinion() {
		return this.opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	@Column(name = "GD_WORKER", unique = false, nullable = true, insertable = true, updatable = true, length = 18)
	public String getGdWorker() {
		return this.gdWorker;
	}

	public void setGdWorker(String gdWorker) {
		this.gdWorker = gdWorker;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "WITHDRAWAL_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getWithdrawalTime() {
		return this.withdrawalTime;
	}

	public void setWithdrawalTime(Date withdrawalTime) {
		this.withdrawalTime = withdrawalTime;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}