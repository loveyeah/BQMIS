package power.ejb.productiontec.relayProtection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJdbhCSydwh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JDBH_C_SYDWH", schema = "POWER")
public class PtJdbhCSydwh implements java.io.Serializable {

	// Fields

	private Long sydId;
	private Long syxmId;
	private String sydName;
	private Long unitId;
	private Double minimum;
	private Double maximum;
	private Long displayNo;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhCSydwh() {
	}

	/** minimal constructor */
	public PtJdbhCSydwh(Long sydId) {
		this.sydId = sydId;
	}

	/** full constructor */
	public PtJdbhCSydwh(Long sydId, Long syxmId, String sydName, Long unitId,
			Double minimum, Double maximum, Long displayNo, String memo,
			String enterpriseCode) {
		this.sydId = sydId;
		this.syxmId = syxmId;
		this.sydName = sydName;
		this.unitId = unitId;
		this.minimum = minimum;
		this.maximum = maximum;
		this.displayNo = displayNo;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SYD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSydId() {
		return this.sydId;
	}

	public void setSydId(Long sydId) {
		this.sydId = sydId;
	}

	@Column(name = "SYXM_ID", precision = 10, scale = 0)
	public Long getSyxmId() {
		return this.syxmId;
	}

	public void setSyxmId(Long syxmId) {
		this.syxmId = syxmId;
	}

	@Column(name = "SYD_NAME", length = 50)
	public String getSydName() {
		return this.sydName;
	}

	public void setSydName(String sydName) {
		this.sydName = sydName;
	}

	@Column(name = "UNIT_ID", precision = 10, scale = 0)
	public Long getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Column(name = "MINIMUM", precision = 10)
	public Double getMinimum() {
		return this.minimum;
	}

	public void setMinimum(Double minimum) {
		this.minimum = minimum;
	}

	@Column(name = "MAXIMUM", precision = 10)
	public Double getMaximum() {
		return this.maximum;
	}

	public void setMaximum(Double maximum) {
		this.maximum = maximum;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
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