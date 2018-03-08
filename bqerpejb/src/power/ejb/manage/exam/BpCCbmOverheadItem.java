package power.ejb.manage.exam;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import power.ejb.manage.system.BpCMeasureUnit;

/**
 * BpCCbmOverheadItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_CBM_OVERHEAD_ITEM")
public class BpCCbmOverheadItem implements java.io.Serializable {

	// Fields

	private Long overheadItemId;
	private String itemCode;
	private String itemName;
	//private Long unitId;
	private String isUse;
	private String enterpriseCode;
	private BpCMeasureUnit unit;

	// Constructors
	@ManyToOne(cascade=CascadeType.REFRESH, optional=true)
	@JoinColumn(name = "unit_Id")  
	public BpCMeasureUnit getUnit() {
		return unit;
	}

	public void setUnit(BpCMeasureUnit unit) {
		this.unit = unit;
	}

	/** default constructor */
	public BpCCbmOverheadItem() {
	}

	/** minimal constructor */
	public BpCCbmOverheadItem(Long overheadItemId) {
		this.overheadItemId = overheadItemId;
	} 
	// Property accessors
	@Id
	@Column(name = "OVERHEAD_ITEM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getOverheadItemId() {
		return this.overheadItemId;
	}

	public void setOverheadItemId(Long overheadItemId) {
		this.overheadItemId = overheadItemId;
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

//	@Column(name = "UNIT_ID", precision = 10, scale = 0)
//	public Long getUnitId() {
//		return this.unitId;
//	}
//
//	public void setUnitId(Long unitId) {
//		this.unitId = unitId;
//	}

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