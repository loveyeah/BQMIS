package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCCostItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
//@SuppressWarnings("serial")
@Entity
@Table(name = "CBM_C_COST_ITEM")
public class CbmCCostItem implements java.io.Serializable {

	// Fields

	private Long itemId;
	private String itemCode;
	private String itemName;
	private Long PItemId;
	private Long unitCode;
	private String comeFrom;
	private Long orderBy;
	private String itemMemo;
	private String itemType;
	private Long accountOrder;
	private String itemExplain;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmCCostItem() {
	}

	/** minimal constructor */
	public CbmCCostItem(Long itemId) {
		this.itemId = itemId;
	}

	/** full constructor */
	public CbmCCostItem(Long itemId, String itemCode, String itemName,
			Long PItemId, Long unitCode, String comeFrom, Long orderBy,
			String itemMemo, String itemType, Long accountOrder,
			String itemExplain, String isUse, String enterpriseCode) {
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.PItemId = PItemId;
		this.unitCode = unitCode;
		this.comeFrom = comeFrom;
		this.orderBy = orderBy;
		this.itemMemo = itemMemo;
		this.itemType = itemType;
		this.accountOrder = accountOrder;
		this.itemExplain = itemExplain;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
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

	@Column(name = "ITEM_CODE", length = 20)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "ITEM_NAME", length = 100)
	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "P_ITEM_ID", precision = 10, scale = 0)
	public Long getPItemId() {
		return this.PItemId;
	}

	public void setPItemId(Long PItemId) {
		this.PItemId = PItemId;
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

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "ITEM_MEMO", length = 100)
	public String getItemMemo() {
		return this.itemMemo;
	}

	public void setItemMemo(String itemMemo) {
		this.itemMemo = itemMemo;
	}

	@Column(name = "ITEM_TYPE", length = 1)
	public String getItemType() {
		return this.itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	@Column(name = "ACCOUNT_ORDER", precision = 10, scale = 0)
	public Long getAccountOrder() {
		return this.accountOrder;
	}

	public void setAccountOrder(Long accountOrder) {
		this.accountOrder = accountOrder;
	}

	@Column(name = "ITEM_EXPLAIN", length = 300)
	public String getItemExplain() {
		return this.itemExplain;
	}

	public void setItemExplain(String itemExplain) {
		this.itemExplain = itemExplain;
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