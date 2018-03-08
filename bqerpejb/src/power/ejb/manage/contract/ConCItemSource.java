package power.ejb.manage.contract;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ConCItemSource entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "CON_C_ITEM_SOURCE")
public class ConCItemSource implements java.io.Serializable {

	// Fields

	private Long itemId;
	private Long PItemId;
	private String itemCode;
	private String itemName;
	private String memo;
	private Long displayNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public ConCItemSource() {
	}

	/** minimal constructor */
	public ConCItemSource(Long itemId, Long PItemId) {
		this.itemId = itemId;
		this.PItemId = PItemId;
	}

	/** full constructor */
	public ConCItemSource(Long itemId, Long PItemId, String itemCode,
			String itemName, String memo, Long displayNo, String isUse,
			String enterpriseCode) {
		this.itemId = itemId;
		this.PItemId = PItemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.memo = memo;
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

	@Column(name = "P_ITEM_ID", nullable = false, precision = 10, scale = 0)
	public Long getPItemId() {
		return this.PItemId;
	}

	public void setPItemId(Long PItemId) {
		this.PItemId = PItemId;
	}

	@Column(name = "ITEM_CODE", length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "ITEM_NAME", length = 100)
	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "DISPLAY_NO", precision = 4, scale = 0)
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