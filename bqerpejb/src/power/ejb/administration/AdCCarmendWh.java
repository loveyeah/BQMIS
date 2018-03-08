package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdCCarmendWh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_C_CARMEND_WH")
public class AdCCarmendWh implements java.io.Serializable {

	// Fields

	private Long id;
	private String cpCode;
	private String cpName;
	private String cpAddress;
	private String conTel;
	private String connman;
	private String bsnRanger;
	private String retrieveCode;
	private String isUse;
	private String updateUser;
	private Date updateTime;
	private String enterPriseCode;

	// Constructors

	/** default constructor */
	public AdCCarmendWh() {
	}

	/** minimal constructor */
	public AdCCarmendWh(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdCCarmendWh(Long id, String cpCode, String cpName,
			String cpAddress, String conTel, String connman, String bsnRanger,
			String retrieveCode, String isUse, String updateUser,
			Date updateTime,String enterPriseCode) {
		this.id = id;
		this.cpCode = cpCode;
		this.cpName = cpName;
		this.cpAddress = cpAddress;
		this.conTel = conTel;
		this.connman = connman;
		this.bsnRanger = bsnRanger;
		this.retrieveCode = retrieveCode;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.enterPriseCode = enterPriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CP_CODE", length = 6)
	public String getCpCode() {
		return this.cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	@Column(name = "CP_NAME", length = 50)
	public String getCpName() {
		return this.cpName;
	}

	public void setCpName(String cpName) {
		this.cpName = cpName;
	}

	@Column(name = "CP_ADDRESS", length = 100)
	public String getCpAddress() {
		return this.cpAddress;
	}

	public void setCpAddress(String cpAddress) {
		this.cpAddress = cpAddress;
	}

	@Column(name = "CON_TEL", length = 20)
	public String getConTel() {
		return this.conTel;
	}

	public void setConTel(String conTel) {
		this.conTel = conTel;
	}

	@Column(name = "CONNMAN", length = 10)
	public String getConnman() {
		return this.connman;
	}

	public void setConnman(String connman) {
		this.connman = connman;
	}

	@Column(name = "BSN_RANGER", length = 1000)
	public String getBsnRanger() {
		return this.bsnRanger;
	}

	public void setBsnRanger(String bsnRanger) {
		this.bsnRanger = bsnRanger;
	}

	@Column(name = "RETRIEVE_CODE", length = 6)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the enterPriseCode
	 */
	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterPriseCode() {
		return this.enterPriseCode;
	}

	/**
	 * @param enterPriseCode the enterPriseCode to set
	 */
	public void setEnterPriseCode(String enterPriseCode) {
		this.enterPriseCode = enterPriseCode;
	}

}