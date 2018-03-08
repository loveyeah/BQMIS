package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_ITEM")
public class CbmCItem implements java.io.Serializable {

	// Fields

	private Long itemId;
	private String itemCode;
	private String itemName;
	private String itemType;
	private String belongDepartment;
	private String dutyDepcode;
	private String ifDispart;
	private String timeType;
	private Long unitCode;
	private String comeFrom;
	private String factFrom;
	private String formulaType;
	private String dataAttribute;
	private String forecastType;
	private String ifTotal;
	private String computeMethod;
	private Long accountOrder;
	private Long factOrder;
	private String retrieveCode;
	private String itemExplain;
	private String factExplain;
	private String isUse;
	private String enterpriseCode;
	private Double firstclassValue; //add by fyyang 20100902一流值/挖潜值
	private Double createValue;//add by fyyang 20100902 创造值/挖潜值
	private Long centerId;//add by kzhang 20100903 费用管理部门
	private Long orderBy;//add by kzhang 20100917 排序

	// Constructors

	

	/** default constructor */
	public CbmCItem() {
	}

	/** minimal constructor */
	public CbmCItem(Long itemId) {
		this.itemId = itemId;
	}

	/** full constructor */
	public CbmCItem(Long itemId, String itemCode, String itemName,
			String itemType, String belongDepartment, String dutyDepcode,
			String ifDispart, String timeType, Long unitCode, String comeFrom,
			String factFrom, String formulaType, String dataAttribute,
			String forecastType, String ifTotal, String computeMethod,
			Long accountOrder, Long factOrder, String retrieveCode,
			String itemExplain, String factExplain, String isUse,
			String enterpriseCode, Double firstclassValue, Double createValue,
			Long centerId, Long orderBy) {
		super();
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.itemType = itemType;
		this.belongDepartment = belongDepartment;
		this.dutyDepcode = dutyDepcode;
		this.ifDispart = ifDispart;
		this.timeType = timeType;
		this.unitCode = unitCode;
		this.comeFrom = comeFrom;
		this.factFrom = factFrom;
		this.formulaType = formulaType;
		this.dataAttribute = dataAttribute;
		this.forecastType = forecastType;
		this.ifTotal = ifTotal;
		this.computeMethod = computeMethod;
		this.accountOrder = accountOrder;
		this.factOrder = factOrder;
		this.retrieveCode = retrieveCode;
		this.itemExplain = itemExplain;
		this.factExplain = factExplain;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.firstclassValue = firstclassValue;
		this.createValue = createValue;
		this.centerId = centerId;
		this.orderBy = orderBy;
	}


	// Property accessors
	@Id
	@Column(name = "ITEM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "ITEM_CODE", length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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

	@Column(name = "BELONG_DEPARTMENT", length = 20)
	public String getBelongDepartment() {
		return this.belongDepartment;
	}

	public void setBelongDepartment(String belongDepartment) {
		this.belongDepartment = belongDepartment;
	}

	@Column(name = "DUTY_DEPCODE", length = 20)
	public String getDutyDepcode() {
		return this.dutyDepcode;
	}

	public void setDutyDepcode(String dutyDepcode) {
		this.dutyDepcode = dutyDepcode;
	}

	@Column(name = "IF_DISPART", length = 1)
	public String getIfDispart() {
		return this.ifDispart;
	}

	public void setIfDispart(String ifDispart) {
		this.ifDispart = ifDispart;
	}

	@Column(name = "TIME_TYPE", length = 1)
	public String getTimeType() {
		return this.timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}

	@Column(name = "UNIT_CODE", precision = 10, scale = 0)
	public Long getUnitCode() {
		return this.unitCode;
	}

	public void setUnitCode(Long unitCode) {
		this.unitCode = unitCode;
	}

	@Column(name = "COME_FROM", length = 1)
	public String getComeFrom() {
		return this.comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	@Column(name = "FACT_FROM", length = 1)
	public String getFactFrom() {
		return this.factFrom;
	}

	public void setFactFrom(String factFrom) {
		this.factFrom = factFrom;
	}

	@Column(name = "FORMULA_TYPE", length = 1)
	public String getFormulaType() {
		return this.formulaType;
	}

	public void setFormulaType(String formulaType) {
		this.formulaType = formulaType;
	}

	@Column(name = "DATA_ATTRIBUTE", length = 1)
	public String getDataAttribute() {
		return this.dataAttribute;
	}

	public void setDataAttribute(String dataAttribute) {
		this.dataAttribute = dataAttribute;
	}

	@Column(name = "FORECAST_TYPE", length = 1)
	public String getForecastType() {
		return this.forecastType;
	}

	public void setForecastType(String forecastType) {
		this.forecastType = forecastType;
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

	@Column(name = "FACT_ORDER", precision = 10, scale = 0)
	public Long getFactOrder() {
		return this.factOrder;
	}

	public void setFactOrder(Long factOrder) {
		this.factOrder = factOrder;
	}

	@Column(name = "RETRIEVE_CODE", length = 8)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

	@Column(name = "ITEM_EXPLAIN", length = 800)
	public String getItemExplain() {
		return this.itemExplain;
	}

	public void setItemExplain(String itemExplain) {
		this.itemExplain = itemExplain;
	}

	@Column(name = "FACT_EXPLAIN", length = 800)
	public String getFactExplain() {
		return this.factExplain;
	}

	public void setFactExplain(String factExplain) {
		this.factExplain = factExplain;
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

	@Column(name = "FIRSTCLASS_VALUE", precision = 15, scale = 2)
	public Double getFirstclassValue() {
		return firstclassValue;
	}

	public void setFirstclassValue(Double firstclassValue) {
		this.firstclassValue = firstclassValue;
	}
	@Column(name = "CREATE_VALUE", precision = 15, scale = 2)
	public Double getCreateValue() {
		return createValue;
	}

	public void setCreateValue(Double createValue) {
		this.createValue = createValue;
	}
	@Column(name = "CENTER_ID", precision = 10, scale = 0)
	public Long getCenterId() {
		return centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}
	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

}