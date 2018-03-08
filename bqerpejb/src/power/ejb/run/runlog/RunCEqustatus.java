package power.ejb.run.runlog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCEqustatus entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_EQUSTATUS", schema = "POWER")
public class RunCEqustatus implements java.io.Serializable {

	// Fields

	private Long equstatusId;
	private String statusName;
	private String statusDesc;
	private String colorValue;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunCEqustatus() {
	}

	/** minimal constructor */
	public RunCEqustatus(Long equstatusId) {
		this.equstatusId = equstatusId;
	}

	/** full constructor */
	public RunCEqustatus(Long equstatusId, String statusName,
			String statusDesc, String colorValue, String isUse,
			String enterpriseCode) {
		this.equstatusId = equstatusId;
		this.statusName = statusName;
		this.statusDesc = statusDesc;
		this.colorValue = colorValue;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "EQUSTATUS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEqustatusId() {
		return this.equstatusId;
	}

	public void setEqustatusId(Long equstatusId) {
		this.equstatusId = equstatusId;
	}

	@Column(name = "STATUS_NAME", length = 60)
	public String getStatusName() {
		return this.statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@Column(name = "STATUS_DESC", length = 256)
	public String getStatusDesc() {
		return this.statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	@Column(name = "COLOR_VALUE", length = 30)
	public String getColorValue() {
		return this.colorValue;
	}

	public void setColorValue(String colorValue) {
		this.colorValue = colorValue;
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