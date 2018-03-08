package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdCCarwhPro entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_C_CARWH_PRO")
public class AdCCarwhPro implements java.io.Serializable {

	// Fields

	private Long id;
	private String payCode;
	private String proCode;
	private String proName;
	private String haveLise;
	private String isUse;
	private String updateUser;
	private Date updateTime;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdCCarwhPro() {
	}

	/** minimal constructor */
	public AdCCarwhPro(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdCCarwhPro(Long id, String payCode, String proCode, String proName,
			String haveLise, String isUse, String updateUser, Date updateTime,
			String enterpriseCode) {
		this.id = id;
		this.payCode = payCode;
		this.proCode = proCode;
		this.proName = proName;
		this.haveLise = haveLise;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.enterpriseCode = enterpriseCode;
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

	@Column(name = "PAY_CODE", length = 2)
	public String getPayCode() {
		return this.payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	@Column(name = "PRO_CODE", length = 4)
	public String getProCode() {
		return this.proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	@Column(name = "PRO_NAME", length = 50)
	public String getProName() {
		return this.proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	@Column(name = "HAVE_LISE", length = 1)
	public String getHaveLise() {
		return this.haveLise;
	}

	public void setHaveLise(String haveLise) {
		this.haveLise = haveLise;
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

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}