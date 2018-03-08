package power.ejb.hr.archives;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCContact entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_CONTACT")
public class HrCContact implements java.io.Serializable {

	// Fields

	private Long contactId;
	private Long empId;
	private String contactMobile;
	private String contactCarrier;
	private String contactTel;
	private String contactEmail;
	private String contactPostalcode;
	private String contactAddress;
	private String isUse;
	private String enterpriseCode;
	private String newEmpCode;
	
	

	// Constructors

	/** default constructor */
	public HrCContact() {
	}

	/** minimal constructor */
	public HrCContact(Long contactId) {
		this.contactId = contactId;
	}

	/** full constructor */
	public HrCContact(Long contactId, Long empId, String contactMobile,
			String contactCarrier, String contactTel, String contactEmail,
			String contactPostalcode, String contactAddress, String isUse,
			String enterpriseCode, String newEmpCode) {
		this.contactId = contactId;
		this.empId = empId;
		this.contactMobile = contactMobile;
		this.contactCarrier = contactCarrier;
		this.contactTel = contactTel;
		this.contactEmail = contactEmail;
		this.contactPostalcode = contactPostalcode;
		this.contactAddress = contactAddress;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.newEmpCode = newEmpCode;
	}

	// Property accessors
	@Id
	@Column(name = "CONTACT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getContactId() {
		return this.contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "CONTACT_MOBILE", length = 20)
	public String getContactMobile() {
		return this.contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	@Column(name = "CONTACT_CARRIER", length = 20)
	public String getContactCarrier() {
		return this.contactCarrier;
	}

	public void setContactCarrier(String contactCarrier) {
		this.contactCarrier = contactCarrier;
	}

	@Column(name = "CONTACT_TEL", length = 20)
	public String getContactTel() {
		return this.contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	@Column(name = "CONTACT_EMAIL", length = 50)
	public String getContactEmail() {
		return this.contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	@Column(name = "CONTACT_POSTALCODE", length = 20)
	public String getContactPostalcode() {
		return this.contactPostalcode;
	}

	public void setContactPostalcode(String contactPostalcode) {
		this.contactPostalcode = contactPostalcode;
	}

	@Column(name = "CONTACT_ADDRESS", length = 50)
	public String getContactAddress() {
		return this.contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
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
	@Column(name = "NEW_EMP_CODE", length = 20)
	public String getNewEmpCode() {
		return this.newEmpCode;
	}

	public void setNewEmpCode(String newEmpCode) {
		this.newEmpCode = newEmpCode;
	}

}