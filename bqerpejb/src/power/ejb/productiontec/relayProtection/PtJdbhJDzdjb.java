package power.ejb.productiontec.relayProtection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJdbhJDzdjb entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_JDBH_J_DZDJB", schema = "POWER")
public class PtJdbhJDzdjb implements java.io.Serializable {

	// Fields

	private Long dzdjbId;
	private Long dztzdId;
	private Long fixvalueItemId;
	private String wholeFixvalue;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhJDzdjb() {
	}

	/** minimal constructor */
	public PtJdbhJDzdjb(Long dzdjbId) {
		this.dzdjbId = dzdjbId;
	}

	/** full constructor */
	public PtJdbhJDzdjb(Long dzdjbId, Long dztzdId, Long fixvalueItemId,
			String wholeFixvalue, String memo, String enterpriseCode) {
		this.dzdjbId = dzdjbId;
		this.dztzdId = dztzdId;
		this.fixvalueItemId = fixvalueItemId;
		this.wholeFixvalue = wholeFixvalue;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DZDJB_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDzdjbId() {
		return this.dzdjbId;
	}

	public void setDzdjbId(Long dzdjbId) {
		this.dzdjbId = dzdjbId;
	}

	@Column(name = "DZTZD_ID", precision = 10, scale = 0)
	public Long getDztzdId() {
		return this.dztzdId;
	}

	public void setDztzdId(Long dztzdId) {
		this.dztzdId = dztzdId;
	}

	@Column(name = "FIXVALUE_ITEM_ID", precision = 10, scale = 0)
	public Long getFixvalueItemId() {
		return this.fixvalueItemId;
	}

	public void setFixvalueItemId(Long fixvalueItemId) {
		this.fixvalueItemId = fixvalueItemId;
	}

	@Column(name = "WHOLE_FIXVALUE", length = 30)
	public String getWholeFixvalue() {
		return this.wholeFixvalue;
	}

	public void setWholeFixvalue(String wholeFixvalue) {
		this.wholeFixvalue = wholeFixvalue;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}