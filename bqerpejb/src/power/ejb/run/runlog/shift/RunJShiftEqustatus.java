package power.ejb.run.runlog.shift;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunJShiftEqustatus entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_SHIFT_EQUSTATUS", schema = "POWER")
public class RunJShiftEqustatus implements java.io.Serializable {

	// Fields

	private Long shiftEqustatusId;
	private Long runLogid;
	private String runLogno;
	private String specialityCode;
	private String attributeCode;
	private String equName;
	private Long equStatusId;
	private String equStatusName;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunJShiftEqustatus() {
	}

	/** minimal constructor */
	public RunJShiftEqustatus(Long shiftEqustatusId, Long runLogid) {
		this.shiftEqustatusId = shiftEqustatusId;
		this.runLogid = runLogid;
	}

	/** full constructor */
	public RunJShiftEqustatus(Long shiftEqustatusId, Long runLogid,
			String runLogno, String specialityCode, String attributeCode,
			String equName, Long equStatusId, String equStatusName,
			String isUse, String enterpriseCode) {
		this.shiftEqustatusId = shiftEqustatusId;
		this.runLogid = runLogid;
		this.runLogno = runLogno;
		this.specialityCode = specialityCode;
		this.attributeCode = attributeCode;
		this.equName = equName;
		this.equStatusId = equStatusId;
		this.equStatusName = equStatusName;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SHIFT_EQUSTATUS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getShiftEqustatusId() {
		return this.shiftEqustatusId;
	}

	public void setShiftEqustatusId(Long shiftEqustatusId) {
		this.shiftEqustatusId = shiftEqustatusId;
	}

	@Column(name = "RUN_LOGID", nullable = false, precision = 10, scale = 0)
	public Long getRunLogid() {
		return this.runLogid;
	}

	public void setRunLogid(Long runLogid) {
		this.runLogid = runLogid;
	}

	@Column(name = "RUN_LOGNO", length = 15)
	public String getRunLogno() {
		return this.runLogno;
	}

	public void setRunLogno(String runLogno) {
		this.runLogno = runLogno;
	}

	@Column(name = "SPECIALITY_CODE", length = 10)
	public String getSpecialityCode() {
		return this.specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
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

	@Column(name = "EQU_STATUS_ID", precision = 10, scale = 0)
	public Long getEquStatusId() {
		return this.equStatusId;
	}

	public void setEquStatusId(Long equStatusId) {
		this.equStatusId = equStatusId;
	}

	@Column(name = "EQU_STATUS_NAME", length = 60)
	public String getEquStatusName() {
		return this.equStatusName;
	}

	public void setEquStatusName(String equStatusName) {
		this.equStatusName = equStatusName;
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