package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJSafepunish entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SP_J_SAFEPUNISH")
public class SpJSafepunish implements java.io.Serializable {

	// Fields

	private Long punishId;
	private String punishDep;
	private String punishReason;
	private String punishOpinion;
	private Date punishDate;
	private String approvalBy;
	private String completeBy;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSafepunish() {
	}

	/** minimal constructor */
	public SpJSafepunish(Long punishId) {
		this.punishId = punishId;
	}

	/** full constructor */
	public SpJSafepunish(Long punishId, String punishDep, String punishReason,
			String punishOpinion, Date punishDate, String approvalBy,
			String completeBy, String enterpriseCode) {
		this.punishId = punishId;
		this.punishDep = punishDep;
		this.punishReason = punishReason;
		this.punishOpinion = punishOpinion;
		this.punishDate = punishDate;
		this.approvalBy = approvalBy;
		this.completeBy = completeBy;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "PUNISH_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPunishId() {
		return this.punishId;
	}

	public void setPunishId(Long punishId) {
		this.punishId = punishId;
	}

	@Column(name = "PUNISH_DEP", length = 20)
	public String getPunishDep() {
		return this.punishDep;
	}

	public void setPunishDep(String punishDep) {
		this.punishDep = punishDep;
	}

	@Column(name = "PUNISH_REASON", length = 256)
	public String getPunishReason() {
		return this.punishReason;
	}

	public void setPunishReason(String punishReason) {
		this.punishReason = punishReason;
	}

	@Column(name = "PUNISH_OPINION", length = 256)
	public String getPunishOpinion() {
		return this.punishOpinion;
	}

	public void setPunishOpinion(String punishOpinion) {
		this.punishOpinion = punishOpinion;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PUNISH_DATE", length = 7)
	public Date getPunishDate() {
		return this.punishDate;
	}

	public void setPunishDate(Date punishDate) {
		this.punishDate = punishDate;
	}

	@Column(name = "APPROVAL_BY", length = 16)
	public String getApprovalBy() {
		return this.approvalBy;
	}

	public void setApprovalBy(String approvalBy) {
		this.approvalBy = approvalBy;
	}

	@Column(name = "COMPLETE_BY", length = 16)
	public String getCompleteBy() {
		return this.completeBy;
	}

	public void setCompleteBy(String completeBy) {
		this.completeBy = completeBy;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}