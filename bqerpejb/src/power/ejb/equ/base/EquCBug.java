package power.ejb.equ.base;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquCBug entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_C_BUG")
public class EquCBug implements java.io.Serializable {

	// Fields

	private Long bugId;
	private String bugCode;
	private Long PBugId;
	private String bugName;
	private String bugDesc;
	private String ifLeaf;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCBug() {
	}

	/** minimal constructor */
	public EquCBug(Long bugId, String bugCode) {
		this.bugId = bugId;
		this.bugCode = bugCode;
	}

	/** full constructor */
	public EquCBug(Long bugId, String bugCode, Long PBugId, String bugName,
			String bugDesc, String ifLeaf, String entryBy, Date entryDate,
			String enterpriseCode, String isUse) {
		this.bugId = bugId;
		this.bugCode = bugCode;
		this.PBugId = PBugId;
		this.bugName = bugName;
		this.bugDesc = bugDesc;
		this.ifLeaf = ifLeaf;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "BUG_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBugId() {
		return this.bugId;
	}

	public void setBugId(Long bugId) {
		this.bugId = bugId;
	}

	@Column(name = "BUG_CODE", nullable = false, length = 30)
	public String getBugCode() {
		return this.bugCode;
	}

	public void setBugCode(String bugCode) {
		this.bugCode = bugCode;
	}

	@Column(name = "P_BUG_ID", precision = 10, scale = 0)
	public Long getPBugId() {
		return this.PBugId;
	}

	public void setPBugId(Long PBugId) {
		this.PBugId = PBugId;
	}

	@Column(name = "BUG_NAME", length = 100)
	public String getBugName() {
		return this.bugName;
	}

	public void setBugName(String bugName) {
		this.bugName = bugName;
	}

	@Column(name = "BUG_DESC", length = 1000)
	public String getBugDesc() {
		return this.bugDesc;
	}

	public void setBugDesc(String bugDesc) {
		this.bugDesc = bugDesc;
	}

	@Column(name = "IF_LEAF", length = 1)
	public String getIfLeaf() {
		return this.ifLeaf;
	}

	public void setIfLeaf(String ifLeaf) {
		this.ifLeaf = ifLeaf;
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