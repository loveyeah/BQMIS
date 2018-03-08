package power.ejb.equ.planrepair;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCPlanType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_PLAN_TYPE")
public class EquCPlanType implements java.io.Serializable {

	// Fields

	private Long planTypeId;
	private String planTypeName;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public EquCPlanType() {
	}

	/** minimal constructor */
	public EquCPlanType(Long planTypeId) {
		this.planTypeId = planTypeId;
	}

	/** full constructor */
	public EquCPlanType(Long planTypeId, String planTypeName, String memo,
			String isUse, String enterpriseCode) {
		this.planTypeId = planTypeId;
		this.planTypeName = planTypeName;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "PLAN_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPlanTypeId() {
		return this.planTypeId;
	}

	public void setPlanTypeId(Long planTypeId) {
		this.planTypeId = planTypeId;
	}

	@Column(name = "PLAN_TYPE_NAME", length = 50)
	public String getPlanTypeName() {
		return this.planTypeName;
	}

	public void setPlanTypeName(String planTypeName) {
		this.planTypeName = planTypeName;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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