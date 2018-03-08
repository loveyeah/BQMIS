package power.ejb.productiontec.relayProtection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJdbhCDzxmwh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JDBH_C_DZXMWH", schema = "POWER")
public class PtJdbhCDzxmwh implements java.io.Serializable {

	// Fields

	private Long fixvalueItemId;
	private Long protectTypeId;
	private String fixvalueItemName;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhCDzxmwh() {
	}

	/** minimal constructor */
	public PtJdbhCDzxmwh(Long fixvalueItemId) {
		this.fixvalueItemId = fixvalueItemId;
	}

	/** full constructor */
	public PtJdbhCDzxmwh(Long fixvalueItemId, Long protectTypeId,
			String fixvalueItemName, String enterpriseCode) {
		this.fixvalueItemId = fixvalueItemId;
		this.protectTypeId = protectTypeId;
		this.fixvalueItemName = fixvalueItemName;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "FIXVALUE_ITEM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFixvalueItemId() {
		return this.fixvalueItemId;
	}

	public void setFixvalueItemId(Long fixvalueItemId) {
		this.fixvalueItemId = fixvalueItemId;
	}

	@Column(name = "PROTECT_TYPE_ID", precision = 10, scale = 0)
	public Long getProtectTypeId() {
		return this.protectTypeId;
	}

	public void setProtectTypeId(Long protectTypeId) {
		this.protectTypeId = protectTypeId;
	}

	@Column(name = "FIXVALUE_ITEM_NAME", length = 50)
	public String getFixvalueItemName() {
		return this.fixvalueItemName;
	}

	public void setFixvalueItemName(String fixvalueItemName) {
		this.fixvalueItemName = fixvalueItemName;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}