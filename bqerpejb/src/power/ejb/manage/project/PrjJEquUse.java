package power.ejb.manage.project;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PrjJEquUse entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRJ_J_EQU_USE")
public class PrjJEquUse implements java.io.Serializable {

	// Fields

	private Long id;
	private Long checkSignId;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;
	private String isUse;
	private Long deptId;
	private String checkText;
	private String signBy;
	private Date signDate;

	// Constructors

	/** default constructor */
	public PrjJEquUse() {
	}

	/** minimal constructor */
	public PrjJEquUse(Long id) {
		this.id = id;
	}

	/** full constructor */
	public PrjJEquUse(Long id, Long checkSignId, String entryBy,
			Date entryDate, String enterpriseCode, String isUse, Long deptId,
			String checkText, String signBy, Date signDate) {
		this.id = id;
		this.checkSignId = checkSignId;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.deptId = deptId;
		this.checkText = checkText;
		this.signBy = signBy;
		this.signDate = signDate;
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

	@Column(name = "CHECK_SIGN_ID", precision = 10, scale = 0)
	public Long getCheckSignId() {
		return this.checkSignId;
	}

	public void setCheckSignId(Long ignId) {
		this.checkSignId = ignId;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "DEPT_ID", precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Column(name = "CHECK_TEXT", length = 200)
	public String getCheckText() {
		return this.checkText;
	}

	public void setCheckText(String checkText) {
		this.checkText = checkText;
	}

	@Column(name = "SIGN_BY", length = 20)
	public String getSignBy() {
		return this.signBy;
	}

	public void setSignBy(String signBy) {
		this.signBy = signBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "SIGN_DATE", length = 7)
	public Date getSignDate() {
		return this.signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

}