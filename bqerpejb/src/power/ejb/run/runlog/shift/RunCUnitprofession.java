package power.ejb.run.runlog.shift;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCUnitprofession entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_UNITPROFESSION", uniqueConstraints = {})
public class RunCUnitprofession implements java.io.Serializable {

	// Fields

	private Long id;
	private String specialityCode;
	private String specialityName;
	private String teamCheckmark;
	private String isUse;
	private Long displayNo;
	private String enterpriseCode;
	private String HSpecialityCode;

	// Constructors

	/** default constructor */
	public RunCUnitprofession() {
	}

	/** minimal constructor */
	public RunCUnitprofession(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RunCUnitprofession(Long id, String specialityCode,
			String specialityName, String teamCheckmark, String isUse,
			Long displayNo, String enterpriseCode, String HSpecialityCode) {
		this.id = id;
		this.specialityCode = specialityCode;
		this.specialityName = specialityName;
		this.teamCheckmark = teamCheckmark;
		this.isUse = isUse;
		this.displayNo = displayNo;
		this.enterpriseCode = enterpriseCode;
		this.HSpecialityCode = HSpecialityCode;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SPECIALITY_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getSpecialityCode() {
		return this.specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	@Column(name = "SPECIALITY_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getSpecialityName() {
		return this.specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	@Column(name = "TEAM_CHECKMARK", unique = false, nullable = true, insertable = true, updatable = true, length = 2)
	public String getTeamCheckmark() {
		return this.teamCheckmark;
	}

	public void setTeamCheckmark(String teamCheckmark) {
		this.teamCheckmark = teamCheckmark;
	}

	@Column(name = "IS_USE", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "DISPLAY_NO", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	@Column(name = "H_SPECIALITY_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getHSpecialityCode() {
		return this.HSpecialityCode;
	}

	public void setHSpecialityCode(String HSpecialityCode) {
		this.HSpecialityCode = HSpecialityCode;
	}

}