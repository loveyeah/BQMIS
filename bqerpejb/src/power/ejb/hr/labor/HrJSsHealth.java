package power.ejb.hr.labor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrJSsHealth entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_SS_HEALTH")
public class HrJSsHealth implements java.io.Serializable {

	// Fields

	private Long detailId;
	private Long mainId;
	private String deptName;
	private String personelName;
	private String manualNumber;
	private String medicareCardNumber;
	private String identityCardNumber;
	private Double accountNum;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJSsHealth() {
	}

	/** minimal constructor */
	public HrJSsHealth(Long detailId) {
		this.detailId = detailId;
	}

	/** full constructor */
	public HrJSsHealth(Long detailId, Long mainId, String deptName,
			String personelName, String manualNumber,
			String medicareCardNumber, String identityCardNumber,
			Double accountNum, String isUse, String enterpriseCode) {
		this.detailId = detailId;
		this.mainId = mainId;
		this.deptName = deptName;
		this.personelName = personelName;
		this.manualNumber = manualNumber;
		this.medicareCardNumber = medicareCardNumber;
		this.identityCardNumber = identityCardNumber;
		this.accountNum = accountNum;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDetailId() {
		return this.detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	@Column(name = "MAIN_ID", precision = 10, scale = 0)
	public Long getMainId() {
		return this.mainId;
	}

	public void setMainId(Long mainId) {
		this.mainId = mainId;
	}

	@Column(name = "DEPT_NAME", length = 30)
	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "PERSONEL_NAME", length = 30)
	public String getPersonelName() {
		return this.personelName;
	}

	public void setPersonelName(String personelName) {
		this.personelName = personelName;
	}

	@Column(name = "MANUAL_NUMBER", length = 30)
	public String getManualNumber() {
		return this.manualNumber;
	}

	public void setManualNumber(String manualNumber) {
		this.manualNumber = manualNumber;
	}

	@Column(name = "MEDICARE_CARD_NUMBER", length = 30)
	public String getMedicareCardNumber() {
		return this.medicareCardNumber;
	}

	public void setMedicareCardNumber(String medicareCardNumber) {
		this.medicareCardNumber = medicareCardNumber;
	}

	@Column(name = "IDENTITY_CARD_NUMBER", length = 30)
	public String getIdentityCardNumber() {
		return this.identityCardNumber;
	}

	public void setIdentityCardNumber(String identityCardNumber) {
		this.identityCardNumber = identityCardNumber;
	}

	@Column(name = "ACCOUNT_NUM", precision = 15, scale = 4)
	public Double getAccountNum() {
		return this.accountNum;
	}

	public void setAccountNum(Double accountNum) {
		this.accountNum = accountNum;
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

}