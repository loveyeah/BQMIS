package power.ejb.productiontec.relayProtection;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * PtJdbhJZzdylx entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JDBH_J_ZZDYLX", schema = "POWER")
public class PtJdbhJZzdylx implements java.io.Serializable {

	// Fields

	private PtJdbhJZzdylxId id;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhJZzdylx() {
	}

	/** minimal constructor */
	public PtJdbhJZzdylx(PtJdbhJZzdylxId id) {
		this.id = id;
	}

	/** full constructor */
	public PtJdbhJZzdylx(PtJdbhJZzdylxId id, String enterpriseCode) {
		this.id = id;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "deviceId", column = @Column(name = "DEVICE_ID", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "protectTypeId", column = @Column(name = "PROTECT_TYPE_ID", nullable = false, precision = 10, scale = 0)) })
	public PtJdbhJZzdylxId getId() {
		return this.id;
	}

	public void setId(PtJdbhJZzdylxId id) {
		this.id = id;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}