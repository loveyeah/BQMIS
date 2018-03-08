package power.ejb.manage.client;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJClientsContact entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_CLIENTS_CONTACT")
public class ConJClientsContact implements java.io.Serializable {

	// Fields

	private Long contactId;
	private Long cliendId;
	private String contactName;
	private String sex;
	private Long age;
	private String position;
	private Long workAge;
	private String department;
	private String identityCard;
	private String address;
	private String officephone;
	private String cellphone;
	private String fax;
	private Long qq;
	private String email;
	private String memo;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public ConJClientsContact() {
	}

	/** minimal constructor */
	public ConJClientsContact(Long contactId) {
		this.contactId = contactId;
	}

	/** full constructor */
	public ConJClientsContact(Long contactId, Long cliendId,
			String contactName, String sex, Long age, String position,
			Long workAge, String department, String identityCard,
			String address, String officephone, String cellphone, String fax,
			Long qq, String email, String memo, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode) {
		this.contactId = contactId;
		this.cliendId = cliendId;
		this.contactName = contactName;
		this.sex = sex;
		this.age = age;
		this.position = position;
		this.workAge = workAge;
		this.department = department;
		this.identityCard = identityCard;
		this.address = address;
		this.officephone = officephone;
		this.cellphone = cellphone;
		this.fax = fax;
		this.qq = qq;
		this.email = email;
		this.memo = memo;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
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

	@Column(name = "CLIEND_ID", precision = 10, scale = 0)
	public Long getCliendId() {
		return this.cliendId;
	}

	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}

	@Column(name = "CONTACT_NAME", length = 16)
	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@Column(name = "SEX", length = 1)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "AGE", precision = 3, scale = 0)
	public Long getAge() {
		return this.age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	@Column(name = "POSITION", length = 20)
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "WORK_AGE", precision = 2, scale = 0)
	public Long getWorkAge() {
		return this.workAge;
	}

	public void setWorkAge(Long workAge) {
		this.workAge = workAge;
	}

	@Column(name = "DEPARTMENT", length = 20)
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "IDENTITY_CARD", length = 18)
	public String getIdentityCard() {
		return this.identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	@Column(name = "ADDRESS", length = 100)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "OFFICEPHONE", length = 20)
	public String getOfficephone() {
		return this.officephone;
	}

	public void setOfficephone(String officephone) {
		this.officephone = officephone;
	}

	@Column(name = "CELLPHONE", length = 20)
	public String getCellphone() {
		return this.cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	@Column(name = "FAX", length = 20)
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "QQ", precision = 10, scale = 0)
	public Long getQq() {
		return this.qq;
	}

	public void setQq(Long qq) {
		this.qq = qq;
	}

	@Column(name = "EMAIL", length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}