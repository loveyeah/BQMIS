package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCMasterItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_MASTER_ITEM")
public class CbmCMasterItem implements java.io.Serializable {

	// Fields

	private Long masterId;
	private Long centerId;
	private Long itemId;
	private String itemAlias;
	private String masterMode;
	private String dataType;
	private String sysSource;
	private String comeFrom;
	private Long displayNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmCMasterItem() {
	}

	/** minimal constructor */
	public CbmCMasterItem(Long masterId) {
		this.masterId = masterId;
	}

	/** full constructor */
	public CbmCMasterItem(Long masterId, Long centerId, Long itemId,
			String itemAlias, String masterMode, String dataType,
			String sysSource, String comeFrom, Long displayNo, String isUse,
			String enterpriseCode) {
		this.masterId = masterId;
		this.centerId = centerId;
		this.itemId = itemId;
		this.itemAlias = itemAlias;
		this.masterMode = masterMode;
		this.dataType = dataType;
		this.sysSource = sysSource;
		this.comeFrom = comeFrom;
		this.displayNo = displayNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "MASTER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMasterId() {
		return this.masterId;
	}

	public void setMasterId(Long masterId) {
		this.masterId = masterId;
	}

	@Column(name = "CENTER_ID", precision = 10, scale = 0)
	public Long getCenterId() {
		return this.centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
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

	@Column(name = "MASTER_MODE", length = 1)
	public String getMasterMode() {
		return this.masterMode;
	}

	public void setMasterMode(String masterMode) {
		this.masterMode = masterMode;
	}

	@Column(name = "DATA_TYPE", length = 1)
	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "SYS_SOURCE", length = 1)
	public String getSysSource() {
		return this.sysSource;
	}

	public void setSysSource(String sysSource) {
		this.sysSource = sysSource;
	}

	@Column(name = "COME_FROM", length = 1)
	public String getComeFrom() {
		return this.comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
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