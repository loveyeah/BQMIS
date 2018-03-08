package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJSafeaward entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SP_J_SAFEAWARD")
public class SpJSafeaward implements java.io.Serializable {

	// Fields

	private Long awardId;
	private String encourageItem;
	private String encourageReason;
	private Date encourageDate;
	private String approvalBy;
	private String completeBy;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSafeaward() {
	}

	/** minimal constructor */
	public SpJSafeaward(Long awardId) {
		this.awardId = awardId;
	}

	/** full constructor */
	public SpJSafeaward(Long awardId, String encourageItem,
			String encourageReason, Date encourageDate, String approvalBy,
			String completeBy, String enterpriseCode) {
		this.awardId = awardId;
		this.encourageItem = encourageItem;
		this.encourageReason = encourageReason;
		this.encourageDate = encourageDate;
		this.approvalBy = approvalBy;
		this.completeBy = completeBy;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "AWARD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAwardId() {
		return this.awardId;
	}

	public void setAwardId(Long awardId) {
		this.awardId = awardId;
	}

	@Column(name = "ENCOURAGE_ITEM", length = 100)
	public String getEncourageItem() {
		return this.encourageItem;
	}

	public void setEncourageItem(String encourageItem) {
		this.encourageItem = encourageItem;
	}

	@Column(name = "ENCOURAGE_REASON", length = 800)
	public String getEncourageReason() {
		return this.encourageReason;
	}

	public void setEncourageReason(String encourageReason) {
		this.encourageReason = encourageReason;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENCOURAGE_DATE", length = 7)
	public Date getEncourageDate() {
		return this.encourageDate;
	}

	public void setEncourageDate(Date encourageDate) {
		this.encourageDate = encourageDate;
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