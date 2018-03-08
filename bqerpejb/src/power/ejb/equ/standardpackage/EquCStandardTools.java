package power.ejb.equ.standardpackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCStandardTools entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_STANDARD_TOOLS")
public class EquCStandardTools implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String operationStep;
	private String planToolCode;
	private String planLocationId;
	private Long planToolQty;
	private Double planToolHrs;
	private String planVendor;
	private Double planToolPrice;
	private String planToolDescription;
	private Long orderby;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquCStandardTools() {
	}

	/** minimal constructor */
	public EquCStandardTools(Long id, String woCode, String operationStep,
			String enterprisecode) {
		this.id = id;
		this.woCode = woCode;
		this.operationStep = operationStep;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquCStandardTools(Long id, String woCode, String operationStep,
			String planToolCode, String planLocationId, Long planToolQty,
			Double planToolHrs, String planVendor, Double planToolPrice,
			String planToolDescription, Long orderby, String enterprisecode,
			String ifUse) {
		this.id = id;
		this.woCode = woCode;
		this.operationStep = operationStep;
		this.planToolCode = planToolCode;
		this.planLocationId = planLocationId;
		this.planToolQty = planToolQty;
		this.planToolHrs = planToolHrs;
		this.planVendor = planVendor;
		this.planToolPrice = planToolPrice;
		this.planToolDescription = planToolDescription;
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

	@Column(name = "OPERATION_STEP", nullable = false, length = 20)
	public String getOperationStep() {
		return this.operationStep;
	}

	public void setOperationStep(String operationStep) {
		this.operationStep = operationStep;
	}

	@Column(name = "PLAN_TOOL_CODE", length = 10)
	public String getPlanToolCode() {
		return this.planToolCode;
	}

	public void setPlanToolCode(String planToolCode) {
		this.planToolCode = planToolCode;
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

	@Column(name = "PLAN_TOOL_HRS", precision = 18, scale = 5)
	public Double getPlanToolHrs() {
		return this.planToolHrs;
	}

	public void setPlanToolHrs(Double planToolHrs) {
		this.planToolHrs = planToolHrs;
	}

	@Column(name = "PLAN_VENDOR", length = 10)
	public String getPlanVendor() {
		return this.planVendor;
	}

	public void setPlanVendor(String planVendor) {
		this.planVendor = planVendor;
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