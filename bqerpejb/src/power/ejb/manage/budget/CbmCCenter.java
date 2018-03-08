package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCCenter entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_CENTER")
public class CbmCCenter implements java.io.Serializable {

	// Fields

	private Long centerId;
	private String depCode;
	private String depName;
	private String manager;
	private String ifDuty;
	private String isUse;
	private String enterpriseCode;
	private String costCode;//成本中心编码 add by fyyang 20100429

	// Constructors

	/** default constructor */
	public CbmCCenter() {
	}

	/** minimal constructor */
	public CbmCCenter(Long centerId) {
		this.centerId = centerId;
	}

	/** full constructor */
	public CbmCCenter(Long centerId, String depCode, String depName,
			String manager, String ifDuty, String isUse, String enterpriseCode,String costCode) {
		this.centerId = centerId;
		this.depCode = depCode;
		this.depName = depName;
		this.manager = manager;
		this.ifDuty = ifDuty;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.costCode=costCode;
	}

	// Property accessors
	@Id
	@Column(name = "CENTER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCenterId() {
		return this.centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}

	@Column(name = "DEP_CODE", length = 20)
	public String getDepCode() {
		return this.depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	@Column(name = "DEP_NAME", length = 60)
	public String getDepName() {
		return this.depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	@Column(name = "MANAGER", length = 16)
	public String getManager() {
		return this.manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	@Column(name = "IF_DUTY", length = 1)
	public String getIfDuty() {
		return this.ifDuty;
	}

	public void setIfDuty(String ifDuty) {
		this.ifDuty = ifDuty;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "COST_CODE", length = 20)
	public String getCostCode() {
		return costCode;
	}

	public void setCostCode(String costCode) {
		this.costCode = costCode;
	}

}