package power.ejb.manage.plan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCPlanItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_PLAN_ITEM")
public class BpCPlanItem implements java.io.Serializable {

	// Fields

	private String itemCode;
	private String FItemCode;
	private String isItem;
	private String itemName;
	private String itemType;
	private Long unitCode;
	private String ifBudget;
	private String collectWay;
	private String formulaType;
	private String ifTotal;
	private String computeMethod;
	private Long accountOrder;
	private String retrieveCode;
	private Long orderBy;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCPlanItem() {
	}

	/** minimal constructor */
	public BpCPlanItem(String itemCode) {
		this.itemCode = itemCode;
	}

	/** full constructor */
	public BpCPlanItem(String itemCode, String FItemCode, String isItem,
			String itemName, String itemType, Long unitCode, String ifBudget,
			String collectWay, String formulaType, String ifTotal,
			String computeMethod, Long accountOrder, String retrieveCode,
			Long orderBy, String memo, String enterpriseCode) {
		this.itemCode = itemCode;
		this.FItemCode = FItemCode;
		this.isItem = isItem;
		this.itemName = itemName;
		this.itemType = itemType;
		this.unitCode = unitCode;
		this.ifBudget = ifBudget;
		this.collectWay = collectWay;
		this.formulaType = formulaType;
		this.ifTotal = ifTotal;
		this.computeMethod = computeMethod;
		this.accountOrder = accountOrder;
		this.retrieveCode = retrieveCode;
		this.orderBy = orderBy;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ITEM_CODE", unique = true, nullable = false, length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "F_ITEM_CODE", length = 40)
	public String getFItemCode() {
		return this.FItemCode;
	}

	public void setFItemCode(String FItemCode) {
		this.FItemCode = FItemCode;
	}

	@Column(name = "IS_ITEM", length = 1)
	public String getIsItem() {
		return this.isItem;
	}

	public void setIsItem(String isItem) {
		this.isItem = isItem;
	}

	@Column(name = "ITEM_NAME", length = 50)
	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "ITEM_TYPE", length = 1)
	public String getItemType() {
		return this.itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	@Column(name = "UNIT_CODE", precision = 10, scale = 0)
	public Long getUnitCode() {
		return this.unitCode;
	}

	public void setUnitCode(Long unitCode) {
		this.unitCode = unitCode;
	}

	@Column(name = "IF_BUDGET", length = 1)
	public String getIfBudget() {
		return this.ifBudget;
	}

	public void setIfBudget(String ifBudget) {
		this.ifBudget = ifBudget;
	}

	@Column(name = "COLLECT_WAY", length = 1)
	public String getCollectWay() {
		return this.collectWay;
	}

	public void setCollectWay(String collectWay) {
		this.collectWay = collectWay;
	}

	@Column(name = "FORMULA_TYPE", length = 1)
	public String getFormulaType() {
		return this.formulaType;
	}

	public void setFormulaType(String formulaType) {
		this.formulaType = formulaType;
	}

	@Column(name = "IF_TOTAL", length = 1)
	public String getIfTotal() {
		return this.ifTotal;
	}

	public void setIfTotal(String ifTotal) {
		this.ifTotal = ifTotal;
	}

	@Column(name = "COMPUTE_METHOD", length = 1)
	public String getComputeMethod() {
		return this.computeMethod;
	}

	public void setComputeMethod(String computeMethod) {
		this.computeMethod = computeMethod;
	}

	@Column(name = "ACCOUNT_ORDER", precision = 10, scale = 0)
	public Long getAccountOrder() {
		return this.accountOrder;
	}

	public void setAccountOrder(Long accountOrder) {
		this.accountOrder = accountOrder;
	}

	@Column(name = "RETRIEVE_CODE", length = 8)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "MEMO")
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}