package power.ejb.hr.ca;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCDay entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_DAY", schema = "POWER")
public class HrCDay implements java.io.Serializable {

	// Fields

	private Long id;
	private Double baseDays;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCDay() {
	}

	/** minimal constructor */
	public HrCDay(Long id) {
		this.id = id;
	}

	/** full constructor */
	public HrCDay(Long id, Double baseDays, String isUse, String enterpriseCode) {
		this.id = id;
		this.baseDays = baseDays;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "BASE_DAYS", precision = 10, scale = 4)
	public Double getBaseDays() {
		return this.baseDays;
	}

	public void setBaseDays(Double baseDays) {
		this.baseDays = baseDays;
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