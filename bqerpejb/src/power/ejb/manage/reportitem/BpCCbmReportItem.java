package power.ejb.manage.reportitem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCCbmReportItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_CBM_REPORT_ITEM", schema = "POWER")
public class BpCCbmReportItem implements java.io.Serializable {

	// Fields

	private Long itemId;
	private Long reportId;
	private String itemCode;
	private String itemName;
	private Long unitId;
	private String alias;
	private Long topicId;
	private String timeType;
	private Long displayNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCCbmReportItem() {
	}

	/** minimal constructor */
	public BpCCbmReportItem(Long itemId) {
		this.itemId = itemId;
	}

	/** full constructor */
	public BpCCbmReportItem(Long itemId, Long reportId, String itemCode,
			String itemName, Long unitId, String alias, Long topicId,
			String timeType, Long displayNo, String isUse, String enterpriseCode) {
		this.itemId = itemId;
		this.reportId = reportId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.unitId = unitId;
		this.alias = alias;
		this.topicId = topicId;
		this.timeType = timeType;
		this.displayNo = displayNo;
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

	@Column(name = "REPORT_ID", precision = 10, scale = 0)
	public Long getReportId() {
		return this.reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
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

	@Column(name = "TOPIC_ID", precision = 10, scale = 0)
	public Long getTopicId() {
		return this.topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	@Column(name = "TIME_TYPE", length = 1)
	public String getTimeType() {
		return this.timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
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