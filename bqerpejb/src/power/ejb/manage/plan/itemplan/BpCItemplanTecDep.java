package power.ejb.manage.plan.itemplan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BP_C_ITEMPLAN_TEC_DEP")
public class BpCItemplanTecDep implements java.io.Serializable {

	// Fields

	private Long depId;
	private String depCode;
	private String depName;
	private Long displayNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCItemplanTecDep() {
	}

	/** minimal constructor */
	public BpCItemplanTecDep(Long depId) {
		this.depId = depId;
	}

	/** full constructor */
	public BpCItemplanTecDep(Long depId, String depCode, String depName,
			Long displayNo, String isUse, String enterpriseCode) {
		this.depId = depId;
		this.depCode = depCode;
		this.depName = depName;
		this.displayNo = displayNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DEP_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDepId() {
		return this.depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	@Column(name = "DEP_CODE", length = 30)
	public String getDepCode() {
		return this.depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	@Column(name = "DEP_NAME", length = 50)
	public String getDepName() {
		return this.depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
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

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}