package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCCenterItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_CENTER_ITEM")
public class CbmCCenterItem implements java.io.Serializable {

	// Fields

	private Long centerItemId;
	private Long centerTopicId;
	private Long itemId;
	private String itemAlias;
	private String dataSource;
	private Long displayNo;
	private String isUse;
	private String enterpriseCode;
	private String masterMode;//控制模式
	private String dataType;//数据类别

	// Constructors

	/** default constructor */
	public CbmCCenterItem() {
	}

	/** minimal constructor */
	public CbmCCenterItem(Long centerItemId) {
		this.centerItemId = centerItemId;
	}

	/** full constructor */
	public CbmCCenterItem(Long centerItemId, Long centerTopicId, Long itemId,
			String itemAlias, String dataSource, Long displayNo, String isUse,
			String enterpriseCode,String masterMode,String dataType) {
		this.centerItemId = centerItemId;
		this.centerTopicId = centerTopicId;
		this.itemId = itemId;
		this.itemAlias = itemAlias;
		this.dataSource = dataSource;
		this.displayNo = displayNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.masterMode=masterMode;
		this.dataType=dataType;
	}

	// Property accessors
	@Id
	@Column(name = "CENTER_ITEM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCenterItemId() {
		return this.centerItemId;
	}

	public void setCenterItemId(Long centerItemId) {
		this.centerItemId = centerItemId;
	}

	@Column(name = "CENTER_TOPIC_ID", precision = 10, scale = 0)
	public Long getCenterTopicId() {
		return this.centerTopicId;
	}

	public void setCenterTopicId(Long centerTopicId) {
		this.centerTopicId = centerTopicId;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "ITEM_ALIAS", length = 80)
	public String getItemAlias() {
		return this.itemAlias;
	}

	public void setItemAlias(String itemAlias) {
		this.itemAlias = itemAlias;
	}

	@Column(name = "DATA_SOURCE", length = 1)
	public String getDataSource() {
		return this.dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
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

	@Column(name = "MASTER_MODE", length = 1)
	public String getMasterMode() {
		return masterMode;
	}

	public void setMasterMode(String masterMode) {
		this.masterMode = masterMode;
	}

	@Column(name = "DATA_TYPE", length = 1)
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

}