package power.ejb.productiontec.relayProtection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJdbhCSyxmwh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JDBH_C_SYXMWH", schema = "POWER")
public class PtJdbhCSyxmwh implements java.io.Serializable {

	// Fields

	private Long syxmId;
	private String syxmName;
	private Long displayNo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhCSyxmwh() {
	}

	/** minimal constructor */
	public PtJdbhCSyxmwh(Long syxmId) {
		this.syxmId = syxmId;
	}

	/** full constructor */
	public PtJdbhCSyxmwh(Long syxmId, String syxmName, Long displayNo,
			String enterpriseCode) {
		this.syxmId = syxmId;
		this.syxmName = syxmName;
		this.displayNo = displayNo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SYXM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSyxmId() {
		return this.syxmId;
	}

	public void setSyxmId(Long syxmId) {
		this.syxmId = syxmId;
	}

	@Column(name = "SYXM_NAME", length = 50)
	public String getSyxmName() {
		return this.syxmName;
	}

	public void setSyxmName(String syxmName) {
		this.syxmName = syxmName;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}