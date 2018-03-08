package power.ejb.equ.base;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquCBugreason entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_C_BUGREASON")
public class EquCBugreason implements java.io.Serializable {

	// Fields

	private Long bugReasonId;
	private String bugCode;
	private String bugReasonDesc;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCBugreason() {
	}

	/** minimal constructor */
	public EquCBugreason(Long bugReasonId) {
		this.bugReasonId = bugReasonId;
	}

	/** full constructor */
	public EquCBugreason(Long bugReasonId, String bugCode,
			String bugReasonDesc, String entryBy, Date entryDate,
			String enterpriseCode, String isUse) {
		this.bugReasonId = bugReasonId;
		this.bugCode = bugCode;
		this.bugReasonDesc = bugReasonDesc;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "BUG_REASON_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBugReasonId() {
		return this.bugReasonId;
	}

	public void setBugReasonId(Long bugReasonId) {
		this.bugReasonId = bugReasonId;
	}

	@Column(name = "BUG_CODE", length = 30)
	public String getBugCode() {
		return this.bugCode;
	}

	public void setBugCode(String bugCode) {
		this.bugCode = bugCode;
	}

	@Column(name = "BUG_REASON_DESC", length = 1000)
	public String getBugReasonDesc() {
		return this.bugReasonDesc;
	}

	public void setBugReasonDesc(String bugReasonDesc) {
		this.bugReasonDesc = bugReasonDesc;
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

}