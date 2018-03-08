package power.ejb.manage.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_ITEM")
public class BpCItem implements java.io.Serializable {

	// Fields

	private String itemCode;
	private String itemName;
	private String isItem;
	private Long unitCode;
	private String itemMemo;
	private String retrieveCode;
	private Long orderBy;
	private String enterpriseCode;
	private String parentItemCode;

	// Constructors

	/** default constructor */
	public BpCItem() {
	}

	/** minimal constructor */
	public BpCItem(String itemCode) {
		this.itemCode = itemCode;
	}

	/** full constructor */
	public BpCItem(String itemCode, String itemName, String isItem,
			Long unitCode, String itemMemo, String retrieveCode, Long orderBy,
			String enterpriseCode, String parentItemCode) {
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.isItem = isItem;
		this.unitCode = unitCode;
		this.itemMemo = itemMemo;
		this.retrieveCode = retrieveCode;
		this.orderBy = orderBy;
		this.enterpriseCode = enterpriseCode;
		this.parentItemCode = parentItemCode;
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

	@Column(name = "IS_ITEM", length = 1)
	public String getIsItem() {
		return this.isItem;
	}

	public void setIsItem(String isItem) {
		this.isItem = isItem;
	}

	@Column(name = "UNIT_CODE", precision = 10, scale = 0)
	public Long getUnitCode() {
		return this.unitCode;
	}

	public void setUnitCode(Long unitCode) {
		this.unitCode = unitCode;
	}

	@Column(name = "ITEM_MEMO", length = 100)
	public String getItemMemo() {
		return this.itemMemo;
	}

	public void setItemMemo(String itemMemo) {
		this.itemMemo = itemMemo;
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

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "PARENT_ITEM_CODE", length = 40)
	public String getParentItemCode() {
		return this.parentItemCode;
	}

	public void setParentItemCode(String parentItemCode) {
		this.parentItemCode = parentItemCode;
	}

}