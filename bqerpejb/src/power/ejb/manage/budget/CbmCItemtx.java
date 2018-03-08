package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCItemtx entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_ITEMTX")
public class CbmCItemtx implements java.io.Serializable {

	// Fields

	private Long zbbmtxId;
	private String zbbmtxCode;
	private String zbbmtxName;
	private Long itemId;
	private String isItem;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmCItemtx() {
	}

	/** minimal constructor */
	public CbmCItemtx(Long zbbmtxId) {
		this.zbbmtxId = zbbmtxId;
	}

	/** full constructor */
	public CbmCItemtx(Long zbbmtxId, String zbbmtxCode, String zbbmtxName,
			Long itemId, String isItem, String isUse, String enterpriseCode) {
		this.zbbmtxId = zbbmtxId;
		this.zbbmtxCode = zbbmtxCode;
		this.zbbmtxName = zbbmtxName;
		this.itemId = itemId;
		this.isItem = isItem;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ZBBMTX_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getZbbmtxId() {
		return this.zbbmtxId;
	}

	public void setZbbmtxId(Long zbbmtxId) {
		this.zbbmtxId = zbbmtxId;
	}

	@Column(name = "ZBBMTX_CODE", length = 90)
	public String getZbbmtxCode() {
		return this.zbbmtxCode;
	}

	public void setZbbmtxCode(String zbbmtxCode) {
		this.zbbmtxCode = zbbmtxCode;
	}

	@Column(name = "ZBBMTX_NAME", length = 50)
	public String getZbbmtxName() {
		return this.zbbmtxName;
	}

	public void setZbbmtxName(String zbbmtxName) {
		this.zbbmtxName = zbbmtxName;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
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