package power.ejb.run.runlog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCShiftEqu entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_SHIFT_EQU", schema = "POWER")
public class RunCShiftEqu implements java.io.Serializable {

	// Fields

	private Long runEquId;
	private String specialityCode;
	private Long runKeyId;
	private String attributeCode;
	private String equName;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunCShiftEqu() {
	}

	/** minimal constructor */
	public RunCShiftEqu(Long runEquId) {
		this.runEquId = runEquId;
	}

	/** full constructor */
	public RunCShiftEqu(Long runEquId, String specialityCode, Long runKeyId,
			String attributeCode, String equName, String isUse,
			String enterpriseCode) {
		this.runEquId = runEquId;
		this.specialityCode = specialityCode;
		this.runKeyId = runKeyId;
		this.attributeCode = attributeCode;
		this.equName = equName;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RUN_EQU_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRunEquId() {
		return this.runEquId;
	}

	public void setRunEquId(Long runEquId) {
		this.runEquId = runEquId;
	}

	@Column(name = "SPECIALITY_CODE", length = 10)
	public String getSpecialityCode() {
		return this.specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	@Column(name = "RUN_KEY_ID", precision = 10, scale = 0)
	public Long getRunKeyId() {
		return this.runKeyId;
	}

	public void setRunKeyId(Long runKeyId) {
		this.runKeyId = runKeyId;
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