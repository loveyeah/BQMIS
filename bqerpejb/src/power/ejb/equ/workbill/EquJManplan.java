package power.ejb.equ.workbill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquJManplan entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_J_MANPLAN", schema = "POWER")
public class EquJManplan implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String woticketCode;
	private String operationStep;
	private String planLaborCode;
	private Long planLaborQty;
//	private Long planLaborHrs;
	private Double planLaborHrs;// modified by liuyi 20100519
	private String planVendor;
	private Double planFee;
	private String factLaborCode;
	private Long factLaborQty;
	private Long factLaborHrs;
	private String factVendor;
	private Double factFee;
	private Long orderby;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquJManplan() {
	}

	/** minimal constructor */
	public EquJManplan(Long id, String woCode, String operationStep,
			String enterprisecode) {
		this.id = id;
		this.woCode = woCode;
		this.operationStep = operationStep;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquJManplan(Long id, String woCode, String woticketCode,
			String operationStep, String planLaborCode, Long planLaborQty,
			Double planLaborHrs, String planVendor, Double planFee,
			String factLaborCode, Long factLaborQty, Long factLaborHrs,
			String factVendor, Double factFee, Long orderby,
			String enterprisecode, String ifUse) {
		this.id = id;
		this.woCode = woCode;
		this.woticketCode = woticketCode;
		this.operationStep = operationStep;
		this.planLaborCode = planLaborCode;
		this.planLaborQty = planLaborQty;
		this.planLaborHrs = planLaborHrs;
		this.planVendor = planVendor;
		this.planFee = planFee;
		this.factLaborCode = factLaborCode;
		this.factLaborQty = factLaborQty;
		this.factLaborHrs = factLaborHrs;
		this.factVendor = factVendor;
		this.factFee = factFee;
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

	@Column(name = "WO_CODE", nullable = false, length = 20)
	public String getWoCode() {
		return this.woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	@Column(name = "WOTICKET_CODE", length = 30)
	public String getWoticketCode() {
		return this.woticketCode;
	}

	public void setWoticketCode(String woticketCode) {
		this.woticketCode = woticketCode;
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

	@Column(name = "PLAN_LABOR_QTY", precision = 10, scale = 0)
	public Long getPlanLaborQty() {
		return this.planLaborQty;
	}

	public void setPlanLaborQty(Long planLaborQty) {
		this.planLaborQty = planLaborQty;
	}

//	@Column(name = "PLAN_LABOR_HRS", precision = 10, scale = 0)
//	public Long getPlanLaborHrs() {
//		return this.planLaborHrs;
//	}
//
//	public void setPlanLaborHrs(Long planLaborHrs) {
//		this.planLaborHrs = planLaborHrs;
//	}
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

	@Column(name = "FACT_LABOR_CODE", length = 15)
	public String getFactLaborCode() {
		return this.factLaborCode;
	}

	public void setFactLaborCode(String factLaborCode) {
		this.factLaborCode = factLaborCode;
	}

	@Column(name = "FACT_LABOR_QTY", precision = 10, scale = 0)
	public Long getFactLaborQty() {
		return this.factLaborQty;
	}

	public void setFactLaborQty(Long factLaborQty) {
		this.factLaborQty = factLaborQty;
	}

	@Column(name = "FACT_LABOR_HRS", precision = 10, scale = 0)
	public Long getFactLaborHrs() {
		return this.factLaborHrs;
	}

	public void setFactLaborHrs(Long factLaborHrs) {
		this.factLaborHrs = factLaborHrs;
	}

	@Column(name = "FACT_VENDOR", length = 10)
	public String getFactVendor() {
		return this.factVendor;
	}

	public void setFactVendor(String factVendor) {
		this.factVendor = factVendor;
	}

	@Column(name = "FACT_FEE", precision = 18, scale = 5)
	public Double getFactFee() {
		return this.factFee;
	}

	public void setFactFee(Double factFee) {
		this.factFee = factFee;
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