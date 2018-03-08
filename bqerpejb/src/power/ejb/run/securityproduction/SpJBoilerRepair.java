package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJBoilerRepair entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SP_J_BOILER_REPAIR", schema = "POWER")
public class SpJBoilerRepair implements java.io.Serializable {

	// Fields

	private Long boilerRepairId;
	private Long boilerId;
	private String taskSource;
	private String repairRecord;
	private Date repairTime;
	private String repairBy;
	private String isUse;
	private String enterpriseCode;
	private String fillBy;//add by ltong
	private Date fillTime;//add by ltong

	// Constructors

	@Column(name = "FILL_BY", length = 20)
	public String getFillBy() {
		return fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FILL_TIME", length = 7)
	public Date getFillTime() {
		return fillTime;
	}

	public void setFillTime(Date fillTime) {
		this.fillTime = fillTime;
	}

	/** default constructor */
	public SpJBoilerRepair() {
	}

	/** minimal constructor */
	public SpJBoilerRepair(Long boilerRepairId) {
		this.boilerRepairId = boilerRepairId;
	}

	/** full constructor */
	public SpJBoilerRepair(Long boilerRepairId, Long boilerId,
			String taskSource, String repairRecord, Date repairTime,
			String repairBy, String isUse, String enterpriseCode) {
		this.boilerRepairId = boilerRepairId;
		this.boilerId = boilerId;
		this.taskSource = taskSource;
		this.repairRecord = repairRecord;
		this.repairTime = repairTime;
		this.repairBy = repairBy;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BOILER_REPAIR_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBoilerRepairId() {
		return this.boilerRepairId;
	}

	public void setBoilerRepairId(Long boilerRepairId) {
		this.boilerRepairId = boilerRepairId;
	}

	@Column(name = "BOILER_ID", precision = 10, scale = 0)
	public Long getBoilerId() {
		return this.boilerId;
	}

	public void setBoilerId(Long boilerId) {
		this.boilerId = boilerId;
	}

	@Column(name = "TASK_SOURCE", length = 1)
	public String getTaskSource() {
		return this.taskSource;
	}

	public void setTaskSource(String taskSource) {
		this.taskSource = taskSource;
	}

	@Column(name = "REPAIR_RECORD", length = 500)
	public String getRepairRecord() {
		return this.repairRecord;
	}

	public void setRepairRecord(String repairRecord) {
		this.repairRecord = repairRecord;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REPAIR_TIME", length = 7)
	public Date getRepairTime() {
		return this.repairTime;
	}

	public void setRepairTime(Date repairTime) {
		this.repairTime = repairTime;
	}

	@Column(name = "REPAIR_BY", length = 20)
	public String getRepairBy() {
		return this.repairBy;
	}

	public void setRepairBy(String repairBy) {
		this.repairBy = repairBy;
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