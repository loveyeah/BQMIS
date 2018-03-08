package power.ejb.run.runlog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCSpecials entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_SPECIALS", schema = "POWER")
public class RunCSpecials implements java.io.Serializable {

	// Fields

	private Long specialityId;
	private String specialityCode;
	private String specialityName;
	private String PSpecialityCode;
	private String specialityType;
	private String specialityChar;
	private Long displayNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunCSpecials() {
	}

	/** minimal constructor */
	public RunCSpecials(Long specialityId, String specialityCode) {
		this.specialityId = specialityId;
		this.specialityCode = specialityCode;
	}

	/** full constructor */
	public RunCSpecials(Long specialityId, String specialityCode,
			String specialityName, String PSpecialityCode,
			String specialityType, String specialityChar, Long displayNo,
			String isUse, String enterpriseCode) {
		this.specialityId = specialityId;
		this.specialityCode = specialityCode;
		this.specialityName = specialityName;
		this.PSpecialityCode = PSpecialityCode;
		this.specialityType = specialityType;
		this.specialityChar = specialityChar;
		this.displayNo = displayNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SPECIALITY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSpecialityId() {
		return this.specialityId;
	}

	public void setSpecialityId(Long specialityId) {
		this.specialityId = specialityId;
	}

	@Column(name = "SPECIALITY_CODE", nullable = false, length = 10)
	public String getSpecialityCode() {
		return this.specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	@Column(name = "SPECIALITY_NAME", length = 100)
	public String getSpecialityName() {
		return this.specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	@Column(name = "P_SPECIALITY_CODE", length = 10)
	public String getPSpecialityCode() {
		return this.PSpecialityCode;
	}

	public void setPSpecialityCode(String PSpecialityCode) {
		this.PSpecialityCode = PSpecialityCode;
	}

	@Column(name = "SPECIALITY_TYPE", length = 2)
	public String getSpecialityType() {
		return this.specialityType;
	}

	public void setSpecialityType(String specialityType) {
		this.specialityType = specialityType;
	}

	@Column(name = "SPECIALITY_CHAR", length = 2)
	public String getSpecialityChar() {
		return this.specialityChar;
	}

	public void setSpecialityChar(String specialityChar) {
		this.specialityChar = specialityChar;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}