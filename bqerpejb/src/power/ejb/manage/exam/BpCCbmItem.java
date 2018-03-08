package power.ejb.manage.exam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCCbmItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_CBM_ITEM")
public class BpCCbmItem implements java.io.Serializable {

	// Fields

	private Long itemId;
	private String isItem;
	private Long FItemId;
	private String itemCode;
	private String itemName;
	private Long unitId;
	private String alias;
//	private String ifBranchItem;
	private String dataType;
	private Long accountOrder;
	private Long displayNo;
	private String isUse;
	private String enterpriseCode;
	// add by liuyi 091207 
	private Long topicId;

	// Constructors

	

	/** default constructor */
	public BpCCbmItem() {
	}

	/** minimal constructor */
	public BpCCbmItem(Long itemId) {
		this.itemId = itemId;
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

	@Column(name = "IS_ITEM", length = 1)
	public String getIsItem() {
		return this.isItem;
	}

	public void setIsItem(String isItem) {
		this.isItem = isItem;
	}

	@Column(name = "F_ITEM_ID", precision = 10, scale = 0)
	public Long getFItemId() {
		return this.FItemId;
	}

	public void setFItemId(Long FItemId) {
		this.FItemId = FItemId;
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

	@Column(name = "UNIT_ID", precision = 10, scale = 0)
	public Long getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Column(name = "ALIAS", length = 100)
	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "DATA_TYPE", length = 1)
	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "ACCOUNT_ORDER", precision = 10, scale = 0)
	public Long getAccountOrder() {
		return this.accountOrder;
	}

	public void setAccountOrder(Long accountOrder) {
		this.accountOrder = accountOrder;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
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

//	@Column(name = "IF_BRANCH_ITEM", length = 1)
//	public String getIfBranchItem() {
//		return ifBranchItem;
//	}
//
//	public void setIfBranchItem(String ifBranchItem) {
//		this.ifBranchItem = ifBranchItem;
//	}
	
	@Column(name = "TOPIC_ID", precision = 10, scale = 0)
	public Long getTopicId() {
		return topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
}