package power.ejb.productiontec.relayProtection;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * PtJdbhCLbyxmdy entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JDBH_C_LBYXMDY")
public class PtJdbhCLbyxmdy implements java.io.Serializable {

	// Fields

	private PtJdbhCLbyxmdyId id;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhCLbyxmdy() {
	}

	/** minimal constructor */
	public PtJdbhCLbyxmdy(PtJdbhCLbyxmdyId id) {
		this.id = id;
	}

	/** full constructor */
	public PtJdbhCLbyxmdy(PtJdbhCLbyxmdyId id, String enterpriseCode) {
		this.id = id;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "sylbId", column = @Column(name = "SYLB_ID", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "syxmId", column = @Column(name = "SYXM_ID", nullable = false, precision = 10, scale = 0)) })
	public PtJdbhCLbyxmdyId getId() {
		return this.id;
	}

	public void setId(PtJdbhCLbyxmdyId id) {
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