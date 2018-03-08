package power.ejb.run.runlog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunJShiftParm entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_SHIFT_PARM")
public class RunJShiftParm implements java.io.Serializable {

	// Fields

	private Long shiftParmId;
	private Long runLogid;
	private Long runlogParmId;
	private Double itemNumberValue;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunJShiftParm() {
	}

	/** minimal constructor */
	public RunJShiftParm(Long shiftParmId) {
		this.shiftParmId = shiftParmId;
	}

	/** full constructor */
	public RunJShiftParm(Long shiftParmId, Long runLogid, Long runlogParmId,
			Double itemNumberValue, String isUse, String enterpriseCode) {
		this.shiftParmId = shiftParmId;
		this.runLogid = runLogid;
		this.runlogParmId = runlogParmId;
		this.itemNumberValue = itemNumberValue;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SHIFT_PARM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getShiftParmId() {
		return this.shiftParmId;
	}

	public void setShiftParmId(Long shiftParmId) {
		this.shiftParmId = shiftParmId;
	}

	@Column(name = "RUN_LOGID", precision = 10, scale = 0)
	public Long getRunLogid() {
		return this.runLogid;
	}

	public void setRunLogid(Long runLogid) {
		this.runLogid = runLogid;
	}

	@Column(name = "RUNLOG_PARM_ID", precision = 10, scale = 0)
	public Long getRunlogParmId() {
		return this.runlogParmId;
	}

	public void setRunlogParmId(Long runlogParmId) {
		this.runlogParmId = runlogParmId;
	}

	@Column(name = "ITEM_NUMBER_VALUE", precision = 15, scale = 4)
	public Double getItemNumberValue() {
		return this.itemNumberValue;
	}

	public void setItemNumberValue(Double itemNumberValue) {
		this.itemNumberValue = itemNumberValue;
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