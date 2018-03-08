package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCStatItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_STAT_ITEM")
public class BpCStatItem implements java.io.Serializable {

	// Fields

	private String itemCode;
	private String itemName;
	private Long unitCode;
	private String itemType;
	private String dataTimeType;
	private String dataCollectWay;
	private String deriveDataType;
	private String dataAttribute;
	private String totalDataType;
	private String ignoreZero;
	private String computeMethod;
	private Long accountOrder;
	private String retrieveCode;
	private String parentItemCode;
	private String isItem;
	private Long orderBy;
	private String enterpriseCode;

	//ITEM_STAT_TYPE 1大指标 2小指标
	private String itemStatType;
	//班组性质
	private String groupNature;
	// Constructors

	/** default constructor */
	public BpCStatItem() {
	}

	/** minimal constructor */
	public BpCStatItem(String itemCode) {
		this.itemCode = itemCode;
	}

	/** full constructor */
	public BpCStatItem(String itemCode, String itemName, Long unitCode,
			String itemType, String dataTimeType, String dataCollectWay,
			String deriveDataType, String dataAttribute, String totalDataType,
			String ignoreZero, String computeMethod, Long accountOrder,
			String retrieveCode, String parentItemCode, String isItem,
			Long orderBy, String enterpriseCode) {
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.unitCode = unitCode;
		this.itemType = itemType;
		this.dataTimeType = dataTimeType;
		this.dataCollectWay = dataCollectWay;
		this.deriveDataType = deriveDataType;
		this.dataAttribute = dataAttribute;
		this.totalDataType = totalDataType;
		this.ignoreZero = ignoreZero;
		this.computeMethod = computeMethod;
		this.accountOrder = accountOrder;
		this.retrieveCode = retrieveCode;
		this.parentItemCode = parentItemCode;
		this.isItem = isItem;
		this.orderBy = orderBy;
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

	@Column(name = "ITEM_NAME", length = 50)
	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "UNIT_CODE", precision = 10, scale = 0)
	public Long getUnitCode() {
		return this.unitCode;
	}

	public void setUnitCode(Long unitCode) {
		this.unitCode = unitCode;
	}

	@Column(name = "ITEM_TYPE", length = 1)
	public String getItemType() {
		return this.itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	@Column(name = "DATA_TIME_TYPE", length = 1)
	public String getDataTimeType() {
		return this.dataTimeType;
	}

	public void setDataTimeType(String dataTimeType) {
		this.dataTimeType = dataTimeType;
	}

	@Column(name = "DATA_COLLECT_WAY", length = 1)
	public String getDataCollectWay() {
		return this.dataCollectWay;
	}

	public void setDataCollectWay(String dataCollectWay) {
		this.dataCollectWay = dataCollectWay;
	}

	@Column(name = "DERIVE_DATA_TYPE", length = 1)
	public String getDeriveDataType() {
		return this.deriveDataType;
	}

	public void setDeriveDataType(String deriveDataType) {
		this.deriveDataType = deriveDataType;
	}

	@Column(name = "DATA_ATTRIBUTE", length = 1)
	public String getDataAttribute() {
		return this.dataAttribute;
	}

	public void setDataAttribute(String dataAttribute) {
		this.dataAttribute = dataAttribute;
	}

	@Column(name = "TOTAL_DATA_TYPE", length = 1)
	public String getTotalDataType() {
		return this.totalDataType;
	}

	public void setTotalDataType(String totalDataType) {
		this.totalDataType = totalDataType;
	}

	@Column(name = "IGNORE_ZERO", length = 1)
	public String getIgnoreZero() {
		return this.ignoreZero;
	}

	public void setIgnoreZero(String ignoreZero) {
		this.ignoreZero = ignoreZero;
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

	@Column(name = "PARENT_ITEM_CODE", length = 40)
	public String getParentItemCode() {
		return this.parentItemCode;
	}

	public void setParentItemCode(String parentItemCode) {
		this.parentItemCode = parentItemCode;
	}

	@Column(name = "IS_ITEM", length = 1)
	public String getIsItem() {
		return this.isItem;
	}

	public void setIsItem(String isItem) {
		this.isItem = isItem;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "ITEM_STAT_TYPE", length = 1)
	public String getItemStatType() {
		return itemStatType;
	}

	public void setItemStatType(String itemStatType) {
		this.itemStatType = itemStatType;
	}
	
	@Column(name = "group_nature", length = 2)
	public String getGroupNature() {
		return groupNature;
	}

	public void setGroupNature(String groupNature) {
		this.groupNature = groupNature;
	}

}