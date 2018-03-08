package power.ejb.equ.standardpackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCStandardManplan entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_STANDARD_MANPLAN")
public class EquCStandardManplan implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String operationStep;
	private String planLaborCode;
	private String planLaborLevel;
	private Long planLaborQty;
	private Double planLaborHrs;//modify by wpzhu
	private String planVendor;
	private Double planFee;
	private Long orderby;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquCStandardManplan() {
	}

	/** minimal constructor */
	public EquCStandardManplan(Long id, String operationStep,
			String enterprisecode) {
		this.id = id;
		this.operationStep = operationStep;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquCStandardManplan(Long id, String woCode, String operationStep,
			String planLaborCode, String planLaborLevel, Long planLaborQty,
			Double planLaborHrs, String planVendor, Double planFee, Long orderby,
			String enterprisecode, String ifUse) {
		this.id = id;
		this.woCode = woCode;
		this.operationStep = operationStep;
		this.planLaborCode = planLaborCode;
		this.planLaborLevel = planLaborLevel;
		this.planLaborQty = planLaborQty;
		this.planLaborHrs = planLaborHrs;
		this.planVendor = planVendor;
		this.planFee = planFee;
		this.orderby = orderby;
		this.enterprisecode = enterprisecode;
		this.ifUse = ifUse;
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

	@Column(name = "WO_CODE", length = 20)
	public String getWoCode() {
		return this.woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	@Column(name = "OPERATION_STEP", nullable = false, length = 20)
	public String getOperationStep() {
		return this.operationStep;
	}

	public void setOperationStep(String operationStep) {
		this.operationStep = operationStep;
	}

	@Column(name = "PLAN_LABOR_CODE", length = 15)
	public String getPlanLaborCode() {
		return this.planLaborCode;
	}

	public void setPlanLaborCode(String planLaborCode) {
		this.planLaborCode = planLaborCode;
	}

	@Column(name = "PLAN_LABOR_LEVEL", length = 30)
	public String getPlanLaborLevel() {
		return this.planLaborLevel;
	}

	public void setPlanLaborLevel(String planLaborLevel) {
		this.planLaborLevel = planLaborLevel;
	}

	@Column(name = "PLAN_LABOR_QTY", precision = 10, scale = 0)
	public Long getPlanLaborQty() {
		return this.planLaborQty;
	}

	public void setPlanLaborQty(Long planLaborQty) {
		this.planLaborQty = planLaborQty;
	}

	@Column(name = "PLAN_LABOR_HRS", precision = 18, scale = 5)
	public Double getPlanLaborHrs() {
		return this.planLaborHrs;
	}

	public void setPlanLaborHrs(Double planLaborHrs) {
		this.planLaborHrs = planLaborHrs;
	}

	@Column(name = "PLAN_VENDOR", length = 10)
	public String getPlanVendor() {
		return this.planVendor;
	}

	public void setPlanVendor(String planVendor) {
		this.planVendor = planVendor;
	}

	@Column(name = "PLAN_FEE", precision = 18, scale = 5)
	public Double getPlanFee() {
		return this.planFee;
	}

	public void setPlanFee(Double planFee) {
		this.planFee = planFee;
	}

	@Column(name = "ORDERBY", precision = 10, scale = 0)
	public Long getOrderby() {
		return this.orderby;
	}

	public void setOrderby(Long orderby) {
		this.orderby = orderby;
	}

	@Column(name = "ENTERPRISECODE", nullable = false, length = 20)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

	@Column(name = "IF_USE", length = 1)
	public String getIfUse() {
		return this.ifUse;
	}

	public void setIfUse(String ifUse) {
		this.ifUse = ifUse;
	}

}