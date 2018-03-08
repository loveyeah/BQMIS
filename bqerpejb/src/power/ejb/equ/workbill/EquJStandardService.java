package power.ejb.equ.workbill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquJStandardService entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_J_STANDARD_SERVICE", schema = "POWER")
public class EquJStandardService implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String operationStep;
	private String planServiceCode;
	private String planServiceUnit;
	private Double planFee;
	private String factServiceCode;
	private String factServiceUnit;
	private Double factFee;
	private Long orderby;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquJStandardService() {
	}

	/** minimal constructor */
	public EquJStandardService(Long id, String operationStep,
			String enterprisecode) {
		this.id = id;
		this.operationStep = operationStep;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquJStandardService(Long id, String woCode, String operationStep,
			String planServiceCode, String planServiceUnit, Double planFee,
			String factServiceCode, String factServiceUnit, Double factFee,
			Long orderby, String enterprisecode, String ifUse) {
		this.id = id;
		this.woCode = woCode;
		this.operationStep = operationStep;
		this.planServiceCode = planServiceCode;
		this.planServiceUnit = planServiceUnit;
		this.planFee = planFee;
		this.factServiceCode = factServiceCode;
		this.factServiceUnit = factServiceUnit;
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

	@Column(name = "PLAN_SERVICE_CODE", length = 15)
	public String getPlanServiceCode() {
		return this.planServiceCode;
	}

	public void setPlanServiceCode(String planServiceCode) {
		this.planServiceCode = planServiceCode;
	}

	@Column(name = "PLAN_SERVICE_UNIT", length = 10)
	public String getPlanServiceUnit() {
		return this.planServiceUnit;
	}

	public void setPlanServiceUnit(String planServiceUnit) {
		this.planServiceUnit = planServiceUnit;
	}

	@Column(name = "PLAN_FEE", precision = 18, scale = 5)
	public Double getPlanFee() {
		return this.planFee;
	}

	public void setPlanFee(Double planFee) {
		this.planFee = planFee;
	}

	@Column(name = "FACT_SERVICE_CODE", length = 15)
	public String getFactServiceCode() {
		return this.factServiceCode;
	}

	public void setFactServiceCode(String factServiceCode) {
		this.factServiceCode = factServiceCode;
	}

	@Column(name = "FACT_SERVICE_UNIT", length = 10)
	public String getFactServiceUnit() {
		return this.factServiceUnit;
	}

	public void setFactServiceUnit(String factServiceUnit) {
		this.factServiceUnit = factServiceUnit;
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