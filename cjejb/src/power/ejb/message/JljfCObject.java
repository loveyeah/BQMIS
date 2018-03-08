package power.ejb.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JljfCObject entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "JLJF_C_OBJECT")
public class JljfCObject implements java.io.Serializable {

	// Fields

	private String zbbmtxCode;
	private String zbbmtxName;
	private String zbbmHaveData;
	private String itemtype;
	private String itemCode;
	private String zbbmtxAlias;
	private String zbbmtxAlias1;
	private String isUse;

	// Constructors

	/** default constructor */
	public JljfCObject() {
	}

	/** minimal constructor */
	public JljfCObject(String zbbmtxCode) {
		this.zbbmtxCode = zbbmtxCode;
	}

	/** full constructor */
	public JljfCObject(String zbbmtxCode, String zbbmtxName,
			String zbbmHaveData, String itemtype, String itemCode,
			String zbbmtxAlias, String zbbmtxAlias1, String isUse) {
		this.zbbmtxCode = zbbmtxCode;
		this.zbbmtxName = zbbmtxName;
		this.zbbmHaveData = zbbmHaveData;
		this.itemtype = itemtype;
		this.itemCode = itemCode;
		this.zbbmtxAlias = zbbmtxAlias;
		this.zbbmtxAlias1 = zbbmtxAlias1;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "ZBBMTX_CODE", unique = true, nullable = false, length = 90)
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

	@Column(name = "ZBBM_HAVE_DATA", length = 1)
	public String getZbbmHaveData() {
		return this.zbbmHaveData;
	}

	public void setZbbmHaveData(String zbbmHaveData) {
		this.zbbmHaveData = zbbmHaveData;
	}

	@Column(name = "ITEMTYPE", length = 2)
	public String getItemtype() {
		return this.itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	@Column(name = "ITEM_CODE", length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "ZBBMTX_ALIAS", length = 150)
	public String getZbbmtxAlias() {
		return this.zbbmtxAlias;
	}

	public void setZbbmtxAlias(String zbbmtxAlias) {
		this.zbbmtxAlias = zbbmtxAlias;
	}

	@Column(name = "ZBBMTX_ALIAS1", length = 40)
	public String getZbbmtxAlias1() {
		return this.zbbmtxAlias1;
	}

	public void setZbbmtxAlias1(String zbbmtxAlias1) {
		this.zbbmtxAlias1 = zbbmtxAlias1;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}