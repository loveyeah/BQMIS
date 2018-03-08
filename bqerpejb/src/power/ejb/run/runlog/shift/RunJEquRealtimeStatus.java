package power.ejb.run.runlog.shift;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunJEquRealtimeStatus entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_EQU_REALTIME_STATUS", schema = "POWER")
public class RunJEquRealtimeStatus implements java.io.Serializable {

	// Fields

	private Long realtimeStatusId;
	private String attributeCode;
	private String equName;
	private Long equRunStatusId;
	private String statusName;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunJEquRealtimeStatus() {
	}

	/** minimal constructor */
	public RunJEquRealtimeStatus(Long realtimeStatusId) {
		this.realtimeStatusId = realtimeStatusId;
	}

	/** full constructor */
	public RunJEquRealtimeStatus(Long realtimeStatusId, String attributeCode,
			String equName, Long equRunStatusId, String statusName,
			String isUse, String enterpriseCode) {
		this.realtimeStatusId = realtimeStatusId;
		this.attributeCode = attributeCode;
		this.equName = equName;
		this.equRunStatusId = equRunStatusId;
		this.statusName = statusName;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "REALTIME_STATUS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRealtimeStatusId() {
		return this.realtimeStatusId;
	}

	public void setRealtimeStatusId(Long realtimeStatusId) {
		this.realtimeStatusId = realtimeStatusId;
	}

	@Column(name = "ATTRIBUTE_CODE", length = 30)
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	@Column(name = "EQU_NAME", length = 100)
	public String getEquName() {
		return this.equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	@Column(name = "EQU_RUN_STATUS_ID", precision = 10, scale = 0)
	public Long getEquRunStatusId() {
		return this.equRunStatusId;
	}

	public void setEquRunStatusId(Long equRunStatusId) {
		this.equRunStatusId = equRunStatusId;
	}

	@Column(name = "STATUS_NAME", length = 60)
	public String getStatusName() {
		return this.statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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