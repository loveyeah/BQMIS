package power.ejb.run.repair;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCRepairType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_REPAIR_TYPE")
public class RunCRepairType implements java.io.Serializable {

	// Fields

	private Long repairTypeId;
	private String repairTypeName;
	private String entryBy;
	private Date entryDate;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunCRepairType() {
	}

	/** minimal constructor */
	public RunCRepairType(Long repairTypeId) {
		this.repairTypeId = repairTypeId;
	}

	/** full constructor */
	public RunCRepairType(Long repairTypeId, String repairTypeName,
			String entryBy, Date entryDate, String memo, String isUse,
			String enterpriseCode) {
		this.repairTypeId = repairTypeId;
		this.repairTypeName = repairTypeName;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "REPAIR_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRepairTypeId() {
		return this.repairTypeId;
	}

	public void setRepairTypeId(Long repairTypeId) {
		this.repairTypeId = repairTypeId;
	}

	@Column(name = "REPAIR_TYPE_NAME", length = 50)
	public String getRepairTypeName() {
		return this.repairTypeName;
	}

	public void setRepairTypeName(String repairTypeName) {
		this.repairTypeName = repairTypeName;
	}

	@Column(name = "ENTRY_BY", length = 20)
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

	@Column(name = "MEMO", length = 100)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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