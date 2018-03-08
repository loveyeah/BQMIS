package power.ejb.run.repair;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJRepairTasklist entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_REPAIR_TASKLIST")
public class RunJRepairTasklist implements java.io.Serializable {

	// Fields

	private Long tasklistId;
	private Date tasklistYear;
	private String tasklistName;
	private String entryBy;
	private Date entryDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunJRepairTasklist() {
	}

	/** minimal constructor */
	public RunJRepairTasklist(Long tasklistId) {
		this.tasklistId = tasklistId;
	}

	/** full constructor */
	public RunJRepairTasklist(Long tasklistId, Date tasklistYear,
			String tasklistName, String entryBy, Date entryDate, String isUse,
			String enterpriseCode) {
		this.tasklistId = tasklistId;
		this.tasklistYear = tasklistYear;
		this.tasklistName = tasklistName;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TASKLIST_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTasklistId() {
		return this.tasklistId;
	}

	public void setTasklistId(Long tasklistId) {
		this.tasklistId = tasklistId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "TASKLIST_YEAR", length = 7)
	public Date getTasklistYear() {
		return this.tasklistYear;
	}

	public void setTasklistYear(Date tasklistYear) {
		this.tasklistYear = tasklistYear;
	}

	@Column(name = "TASKLIST_NAME", length = 100)
	public String getTasklistName() {
		return this.tasklistName;
	}

	public void setTasklistName(String tasklistName) {
		this.tasklistName = tasklistName;
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