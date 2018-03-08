package power.ejb.run.securityproduction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SpJSafeawardDetails entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SP_J_SAFEAWARD_DETAILS")
public class SpJSafeawardDetails implements java.io.Serializable {

	// Fields

	private Long awardDetailsId;
	private Long safeawardId;
	private String encourageMan;
	private String encourageWay;
	private Double encourageMoney;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSafeawardDetails() {
	}

	/** minimal constructor */
	public SpJSafeawardDetails(Long awardDetailsId, Long safeawardId) {
		this.awardDetailsId = awardDetailsId;
		this.safeawardId = safeawardId;
	}

	/** full constructor */
	public SpJSafeawardDetails(Long awardDetailsId, Long safeawardId,
			String encourageMan, String encourageWay, Double encourageMoney,
			String enterpriseCode) {
		this.awardDetailsId = awardDetailsId;
		this.safeawardId = safeawardId;
		this.encourageMan = encourageMan;
		this.encourageWay = encourageWay;
		this.encourageMoney = encourageMoney;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "AWARD_DETAILS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAwardDetailsId() {
		return this.awardDetailsId;
	}

	public void setAwardDetailsId(Long awardDetailsId) {
		this.awardDetailsId = awardDetailsId;
	}

	@Column(name = "SAFEAWARD_ID", nullable = false, precision = 10, scale = 0)
	public Long getSafeawardId() {
		return this.safeawardId;
	}

	public void setSafeawardId(Long safeawardId) {
		this.safeawardId = safeawardId;
	}

	@Column(name = "ENCOURAGE_MAN", length = 16)
	public String getEncourageMan() {
		return this.encourageMan;
	}

	public void setEncourageMan(String encourageMan) {
		this.encourageMan = encourageMan;
	}

	@Column(name = "ENCOURAGE_WAY", length = 32)
	public String getEncourageWay() {
		return this.encourageWay;
	}

	public void setEncourageWay(String encourageWay) {
		this.encourageWay = encourageWay;
	}

	@Column(name = "ENCOURAGE_MONEY", precision = 15, scale = 4)
	public Double getEncourageMoney() {
		return this.encourageMoney;
	}

	public void setEncourageMoney(Double encourageMoney) {
		this.encourageMoney = encourageMoney;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}