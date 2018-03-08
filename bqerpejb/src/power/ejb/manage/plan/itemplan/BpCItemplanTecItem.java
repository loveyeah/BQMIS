package power.ejb.manage.plan.itemplan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "BP_C_ITEMPLAN_TEC_ITEM")
public class BpCItemplanTecItem implements java.io.Serializable {

	// Fields

	private Long technologyItemId;
	private String itemCode;
	private String itemName;
	private String alias;
	private Long unitId;
	private Long displayNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCItemplanTecItem() {
	}

	/** minimal constructor */
	public BpCItemplanTecItem(Long technologyItemId) {
		this.technologyItemId = technologyItemId;
	}

	/** full constructor */
	public BpCItemplanTecItem(Long technologyItemId, String itemCode,
			String itemName, String alias, Long unitId, Long displayNo,
			String isUse, String enterpriseCode) {
		this.technologyItemId = technologyItemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.alias = alias;
		this.unitId = unitId;
		this.displayNo = displayNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TECHNOLOGY_ITEM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTechnologyItemId() {
		return this.technologyItemId;
	}

	public void setTechnologyItemId(Long technologyItemId) {
		this.technologyItemId = technologyItemId;
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