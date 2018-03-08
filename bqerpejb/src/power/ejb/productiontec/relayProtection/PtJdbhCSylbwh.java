package power.ejb.productiontec.relayProtection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJdbhCSylbwh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_JDBH_C_SYLBWH")
public class PtJdbhCSylbwh implements java.io.Serializable {

	// Fields

	private Long sylbId;
	private String sylbName;
	private Long displayNo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhCSylbwh() {
	}

	/** minimal constructor */
	public PtJdbhCSylbwh(Long sylbId) {
		this.sylbId = sylbId;
	}

	/** full constructor */
	public PtJdbhCSylbwh(Long sylbId, String sylbName, Long displayNo,
			String enterpriseCode) {
		this.sylbId = sylbId;
		this.sylbName = sylbName;
		this.displayNo = displayNo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SYLB_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSylbId() {
		return this.sylbId;
	}

	public void setSylbId(Long sylbId) {
		this.sylbId = sylbId;
	}

	@Column(name = "SYLB_NAME", length = 50)
	public String getSylbName() {
		return this.sylbName;
	}

	public void setSylbName(String sylbName) {
		this.sylbName = sylbName;
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