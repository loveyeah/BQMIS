package power.ejb.manage.plan.itemplan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "BP_C_ITEMPLAN_ECO_ITEM")
public class BpCItemplanEcoItem implements java.io.Serializable {

	// Fields

	private Long economicItemId;
	private Long topicId;
	private String itemCode;
	private String itemName;
	private String alias;
	private Long unitId;
	private String itemType;
	private Long displayNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCItemplanEcoItem() {
	}

	/** minimal constructor */
	public BpCItemplanEcoItem(Long economicItemId, Long topicId) {
		this.economicItemId = economicItemId;
		this.topicId = topicId;
	}

	/** full constructor */
	public BpCItemplanEcoItem(Long economicItemId, Long topicId,
			String itemCode, String itemName, String alias, Long unitId,
			String itemType, Long displayNo, String isUse, String enterpriseCode) {
		this.economicItemId = economicItemId;
		this.topicId = topicId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.alias = alias;
		this.unitId = unitId;
		this.itemType = itemType;
		this.displayNo = displayNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ECONOMIC_ITEM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEconomicItemId() {
		return this.economicItemId;
	}

	public void setEconomicItemId(Long economicItemId) {
		this.economicItemId = economicItemId;
	}

	@Column(name = "TOPIC_ID", nullable = false, precision = 10, scale = 0)
	public Long getTopicId() {
		return this.topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
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

	@Column(name = "ALIAS", length = 50)
	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Column(name = "UNIT_ID", precision = 10, scale = 0)
	public Long getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Column(name = "ITEM_TYPE", length = 20)
	public String getItemType() {
		return this.itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
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

}