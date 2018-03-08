package power.ejb.equ.workbill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquJTools entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_J_TOOLS", schema = "POWER")
public class EquJTools implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String woticketCode;
	private String operationStep;
	private String planToolNum;
	private String planLocationId;
	private Long planToolQty;
	private Double planToolHrs;
	private Double planToolPrice;
	private String planToolDescription;
	private String factToolNum;
	private String factLocationId;
	private Long factToolQty;
	private Double factToolHrs;
	private Double factToolPrice;
	private String factToolDescription;
	private Long orderby;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquJTools() {
	}

	/** minimal constructor */
	public EquJTools(Long id, String woCode, String operationStep,
			String enterprisecode) {
		this.id = id;
		this.woCode = woCode;
		this.operationStep = operationStep;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquJTools(Long id, String woCode, String woticketCode,
			String operationStep, String planToolNum, String planLocationId,
			Long planToolQty, Double planToolHrs, Double planToolPrice,
			String planToolDescription, String factToolNum,
			String factLocationId, Long factToolQty, Double factToolHrs,
			Double factToolPrice, String factToolDescription, Long orderby,
			String enterprisecode, String ifUse) {
		this.id = id;
		this.woCode = woCode;
		this.woticketCode = woticketCode;
		this.operationStep = operationStep;
		this.planToolNum = planToolNum;
		this.planLocationId = planLocationId;
		this.planToolQty = planToolQty;
		this.planToolHrs = planToolHrs;
		this.planToolPrice = planToolPrice;
		this.planToolDescription = planToolDescription;
		this.factToolNum = factToolNum;
		this.factLocationId = factLocationId;
		this.factToolQty = factToolQty;
		this.factToolHrs = factToolHrs;
		this.factToolPrice = factToolPrice;
		this.factToolDescription = factToolDescription;
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

	@Column(name = "PLAN_TOOL_NUM", length = 10)
	public String getPlanToolNum() {
		return this.planToolNum;
	}

	public void setPlanToolNum(String planToolNum) {
		this.planToolNum = planToolNum;
	}

	@Column(name = "PLAN_LOCATION_ID", length = 20)
	public String getPlanLocationId() {
		return this.planLocationId;
	}

	public void setPlanLocationId(String planLocationId) {
		this.planLocationId = planLocationId;
	}

	@Column(name = "PLAN_TOOL_QTY", precision = 10, scale = 0)
	public Long getPlanToolQty() {
		return this.planToolQty;
	}

	public void setPlanToolQty(Long planToolQty) {
		this.planToolQty = planToolQty;
	}

	@Column(name = "PLAN_TOOL_HRS", precision = 15, scale = 4)
	public Double getPlanToolHrs() {
		return this.planToolHrs;
	}

	public void setPlanToolHrs(Double planToolHrs) {
		this.planToolHrs = planToolHrs;
	}

	@Column(name = "PLAN_TOOL_PRICE", precision = 18, scale = 5)
	public Double getPlanToolPrice() {
		return this.planToolPrice;
	}

	public void setPlanToolPrice(Double planToolPrice) {
		this.planToolPrice = planToolPrice;
	}

	@Column(name = "PLAN_TOOL_DESCRIPTION", length = 200)
	public String getPlanToolDescription() {
		return this.planToolDescription;
	}

	public void setPlanToolDescription(String planToolDescription) {
		this.planToolDescription = planToolDescription;
	}

	@Column(name = "FACT_TOOL_NUM", length = 10)
	public String getFactToolNum() {
		return this.factToolNum;
	}

	public void setFactToolNum(String factToolNum) {
		this.factToolNum = factToolNum;
	}

	@Column(name = "FACT_LOCATION_ID", length = 20)
	public String getFactLocationId() {
		return this.factLocationId;
	}

	public void setFactLocationId(String factLocationId) {
		this.factLocationId = factLocationId;
	}

	@Column(name = "FACT_TOOL_QTY", precision = 10, scale = 0)
	public Long getFactToolQty() {
		return this.factToolQty;
	}

	public void setFactToolQty(Long factToolQty) {
		this.factToolQty = factToolQty;
	}

	@Column(name = "FACT_TOOL_HRS", precision = 15, scale = 4)
	public Double getFactToolHrs() {
		return this.factToolHrs;
	}

	public void setFactToolHrs(Double factToolHrs) {
		this.factToolHrs = factToolHrs;
	}

	@Column(name = "FACT_TOOL_PRICE", precision = 18, scale = 5)
	public Double getFactToolPrice() {
		return this.factToolPrice;
	}

	public void setFactToolPrice(Double factToolPrice) {
		this.factToolPrice = factToolPrice;
	}

	@Column(name = "FACT_TOOL_DESCRIPTION", length = 200)
	public String getFactToolDescription() {
		return this.factToolDescription;
	}

	public void setFactToolDescription(String factToolDescription) {
		this.factToolDescription = factToolDescription;
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