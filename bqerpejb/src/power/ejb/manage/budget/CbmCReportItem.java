package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCReportItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_REPORT_ITEM")
public class CbmCReportItem implements java.io.Serializable {

	// Fields

	private Long reportItemId;
	private String isItem;
	private Long reportType;
	private Long itemId;
	private String itemName;
	private Long unitId;
	private Long dataType;
	private Long displayNo;
	private String isUse;
	private String enterpriseCode;
	private Long parentId;

	// Constructors

	/** default constructor */
	public CbmCReportItem() {
	}

	/** minimal constructor */
	public CbmCReportItem(Long reportItemId) {
		this.reportItemId = reportItemId;
	}

	/** full constructor */
	public CbmCReportItem(Long reportItemId, String isItem, Long reportType,
			Long itemId, String itemName, Long unitId, Long dataType,
			Long displayNo, String isUse, String enterpriseCode, Long parentId) {
		this.reportItemId = reportItemId;
		this.isItem = isItem;
		this.reportType = reportType;
		this.itemId = itemId;
		this.itemName = itemName;
		this.unitId = unitId;
		this.dataType = dataType;
		this.displayNo = displayNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.parentId = parentId;
	}

	// Property accessors
	@Id
	@Column(name = "REPORT_ITEM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getReportItemId() {
		return this.reportItemId;
	}

	public void setReportItemId(Long reportItemId) {
		this.reportItemId = reportItemId;
	}

	@Column(name = "IS_ITEM", length = 1)
	public String getIsItem() {
		return this.isItem;
	}

	public void setIsItem(String isItem) {
		this.isItem = isItem;
	}

	@Column(name = "REPORT_TYPE", precision = 4, scale = 0)
	public Long getReportType() {
		return this.reportType;
	}

	public void setReportType(Long reportType) {
		this.reportType = reportType;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
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

	@Column(name = "DATA_TYPE", precision = 1, scale = 0)
	public Long getDataType() {
		return this.dataType;
	}

	public void setDataType(Long dataType) {
		this.dataType = dataType;
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

	@Column(name = "PARENT_ID", precision = 10, scale = 0)
	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}