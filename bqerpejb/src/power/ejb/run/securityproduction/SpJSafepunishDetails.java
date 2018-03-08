package power.ejb.run.securityproduction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SpJSafepunishDetails entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SP_J_SAFEPUNISH_DETAILS", schema = "POWER")
public class SpJSafepunishDetails implements java.io.Serializable {

	// Fields

	private Long punishDetailsId;
	private Long punishId;
	private String punishMan;
	private String punishType;
	private Double punishMoney;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSafepunishDetails() {
	}

	/** minimal constructor */
	public SpJSafepunishDetails(Long punishDetailsId, Long punishId) {
		this.punishDetailsId = punishDetailsId;
		this.punishId = punishId;
	}

	/** full constructor */
	public SpJSafepunishDetails(Long punishDetailsId, Long punishId,
			String punishMan, String punishType, Double punishMoney,
			String enterpriseCode) {
		this.punishDetailsId = punishDetailsId;
		this.punishId = punishId;
		this.punishMan = punishMan;
		this.punishType = punishType;
		this.punishMoney = punishMoney;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "PUNISH_DETAILS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPunishDetailsId() {
		return this.punishDetailsId;
	}

	public void setPunishDetailsId(Long punishDetailsId) {
		this.punishDetailsId = punishDetailsId;
	}

	@Column(name = "PUNISH_ID", nullable = false, precision = 10, scale = 0)
	public Long getPunishId() {
		return this.punishId;
	}

	public void setPunishId(Long punishId) {
		this.punishId = punishId;
	}

	@Column(name = "PUNISH_MAN", length = 16)
	public String getPunishMan() {
		return this.punishMan;
	}

	public void setPunishMan(String punishMan) {
		this.punishMan = punishMan;
	}

	@Column(name = "PUNISH_TYPE", length = 32)
	public String getPunishType() {
		return this.punishType;
	}

	public void setPunishType(String punishType) {
		this.punishType = punishType;
	}

	@Column(name = "PUNISH_MONEY", precision = 15, scale = 4)
	public Double getPunishMoney() {
		return this.punishMoney;
	}

	public void setPunishMoney(Double punishMoney) {
		this.punishMoney = punishMoney;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}