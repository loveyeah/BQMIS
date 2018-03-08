package power.ejb.productiontec.relayProtection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJdbhCBhlxwh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JDBH_C_BHLXWH", schema = "POWER")
public class PtJdbhCBhlxwh implements java.io.Serializable {

	// Fields

	private Long protectTypeId;
	private String protectTypeName;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhCBhlxwh() {
	}

	/** minimal constructor */
	public PtJdbhCBhlxwh(Long protectTypeId) {
		this.protectTypeId = protectTypeId;
	}

	/** full constructor */
	public PtJdbhCBhlxwh(Long protectTypeId, String protectTypeName,
			String enterpriseCode) {
		this.protectTypeId = protectTypeId;
		this.protectTypeName = protectTypeName;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "PROTECT_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getProtectTypeId() {
		return this.protectTypeId;
	}

	public void setProtectTypeId(Long protectTypeId) {
		this.protectTypeId = protectTypeId;
	}

	@Column(name = "PROTECT_TYPE_NAME", length = 50)
	public String getProtectTypeName() {
		return this.protectTypeName;
	}

	public void setProtectTypeName(String protectTypeName) {
		this.protectTypeName = protectTypeName;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}