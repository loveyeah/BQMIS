package power.ejb.run.runlog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCEquRunstatus entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_EQU_RUNSTATUS", schema = "POWER")
public class RunCEquRunstatus implements java.io.Serializable {

	// Fields

	private Long runstatusId;
	private Long runEquId;
	private Long equstatusId;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunCEquRunstatus() {
	}

	/** minimal constructor */
	public RunCEquRunstatus(Long runstatusId) {
		this.runstatusId = runstatusId;
	}

	/** full constructor */
	public RunCEquRunstatus(Long runstatusId, Long runEquId, Long equstatusId,
			String isUse, String enterpriseCode) {
		this.runstatusId = runstatusId;
		this.runEquId = runEquId;
		this.equstatusId = equstatusId;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RUNSTATUS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRunstatusId() {
		return this.runstatusId;
	}

	public void setRunstatusId(Long runstatusId) {
		this.runstatusId = runstatusId;
	}

	@Column(name = "RUN_EQU_ID", precision = 10, scale = 0)
	public Long getRunEquId() {
		return this.runEquId;
	}

	public void setRunEquId(Long runEquId) {
		this.runEquId = runEquId;
	}

	@Column(name = "EQUSTATUS_ID", precision = 10, scale = 0)
	public Long getEqustatusId() {
		return this.equstatusId;
	}

	public void setEqustatusId(Long equstatusId) {
		this.equstatusId = equstatusId;
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