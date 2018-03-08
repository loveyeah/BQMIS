package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCBusinessItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_BUSINESS_ITEM")
public class CbmCBusinessItem implements java.io.Serializable {

	// Fields

	private Long businessItemId;
	private String itemName;
	private Long unitId;
	private Long itemId1;
	private Long itemId2;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmCBusinessItem() {
	}

	/** minimal constructor */
	public CbmCBusinessItem(Long businessItemId) {
		this.businessItemId = businessItemId;
	}

	/** full constructor */
	public CbmCBusinessItem(Long businessItemId, String itemName, Long unitId,
			Long itemId1, Long itemId2, String isUse, String enterpriseCode) {
		this.businessItemId = businessItemId;
		this.itemName = itemName;
		this.unitId = unitId;
		this.itemId1 = itemId1;
		this.itemId2 = itemId2;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BUSINESS_ITEM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBusinessItemId() {
		return this.businessItemId;
	}

	public void setBusinessItemId(Long businessItemId) {
		this.businessItemId = businessItemId;
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

	@Column(name = "ITEM_ID1", precision = 10, scale = 0)
	public Long getItemId1() {
		return this.itemId1;
	}

	public void setItemId1(Long itemId1) {
		this.itemId1 = itemId1;
	}

	@Column(name = "ITEM_ID2", precision = 10, scale = 0)
	public Long getItemId2() {
		return this.itemId2;
	}

	public void setItemId2(Long itemId2) {
		this.itemId2 = itemId2;
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