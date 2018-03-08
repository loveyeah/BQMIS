package power.ejb.equ.standardpackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCStandardMainmat entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_C_STANDARD_MAINMAT")
public class EquCStandardMainmat implements java.io.Serializable {

	// Fields

	private Long id;
	private String woCode;
	private String operationStep;
	private Long materialId;
	private Double planItemQty;
	private Double planMaterialPrice;
	private Long planVendor;
	private Long orderBy;
	private String directReq;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquCStandardMainmat() {
	}

	/** minimal constructor */
	public EquCStandardMainmat(Long id, String enterprisecode) {
		this.id = id;
		this.enterprisecode = enterprisecode;
	}

	/** full constructor */
	public EquCStandardMainmat(Long id, String woCode, String operationStep,
			Long materialId, Double planItemQty, Double planMaterialPrice,
			Long planVendor, Long orderBy, String directReq,
			String enterprisecode, String ifUse) {
		this.id = id;
		this.woCode = woCode;
		this.operationStep = operationStep;
		this.materialId = materialId;
		this.planItemQty = planItemQty;
		this.planMaterialPrice = planMaterialPrice;
		this.planVendor = planVendor;
		this.orderBy = orderBy;
		this.directReq = directReq;
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

	@Column(name = "OPERATION_STEP", length = 20)
	public String getOperationStep() {
		return this.operationStep;
	}

	public void setOperationStep(String operationStep) {
		this.operationStep = operationStep;
	}

	@Column(name = "MATERIAL_id", precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "PLAN_ITEM_QTY", precision = 15, scale = 4)
	public Double getPlanItemQty() {
		return this.planItemQty;
	}

	public void setPlanItemQty(Double planItemQty) {
		this.planItemQty = planItemQty;
	}

	@Column(name = "PLAN_MATERIAL_PRICE", precision = 18, scale = 5)
	public Double getPlanMaterialPrice() {
		return this.planMaterialPrice;
	}

	public void setPlanMaterialPrice(Double planMaterialPrice) {
		this.planMaterialPrice = planMaterialPrice;
	}

	@Column(name = "PLAN_VENDOR", precision = 10, scale = 0)
	public Long getPlanVendor() {
		return this.planVendor;
	}

	public void setPlanVendor(Long planVendor) {
		this.planVendor = planVendor;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "DIRECT_REQ", length = 1)
	public String getDirectReq() {
		return this.directReq;
	}

	public void setDirectReq(String directReq) {
		this.directReq = directReq;
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