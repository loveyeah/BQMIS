package power.ejb.productiontec.relayProtection;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJdbhJDztzd entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JDBH_J_DZTZD", schema = "POWER")
public class PtJdbhJDztzd implements java.io.Serializable {

	// Fields

	private Long dztzdId;
	private Long deviceId;
	private Long dzjssmId;
	private String dztzdCode;
	private String ctCode;
	private String ptCode;
	private String memo;
	private Date effectiveDate;
	private String fillBy;
	private String useStatus;
	private String saveBy;
	private String saveMark;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhJDztzd() {
	}

	/** minimal constructor */
	public PtJdbhJDztzd(Long dztzdId) {
		this.dztzdId = dztzdId;
	}

	/** full constructor */
	public PtJdbhJDztzd(Long dztzdId, Long deviceId, Long dzjssmId,
			String dztzdCode, String ctCode, String ptCode, String memo,
			Date effectiveDate, String fillBy, String useStatus, String saveBy,
			String saveMark, String enterpriseCode) {
		this.dztzdId = dztzdId;
		this.deviceId = deviceId;
		this.dzjssmId = dzjssmId;
		this.dztzdCode = dztzdCode;
		this.ctCode = ctCode;
		this.ptCode = ptCode;
		this.memo = memo;
		this.effectiveDate = effectiveDate;
		this.fillBy = fillBy;
		this.useStatus = useStatus;
		this.saveBy = saveBy;
		this.saveMark = saveMark;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DZTZD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDztzdId() {
		return this.dztzdId;
	}

	public void setDztzdId(Long dztzdId) {
		this.dztzdId = dztzdId;
	}

	@Column(name = "DEVICE_ID", precision = 10, scale = 0)
	public Long getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "DZJSSM_ID", precision = 10, scale = 0)
	public Long getDzjssmId() {
		return this.dzjssmId;
	}

	public void setDzjssmId(Long dzjssmId) {
		this.dzjssmId = dzjssmId;
	}

	@Column(name = "DZTZD_CODE", length = 50)
	public String getDztzdCode() {
		return this.dztzdCode;
	}

	public void setDztzdCode(String dztzdCode) {
		this.dztzdCode = dztzdCode;
	}

	@Column(name = "CT_CODE", length = 300)
	public String getCtCode() {
		return this.ctCode;
	}

	public void setCtCode(String ctCode) {
		this.ctCode = ctCode;
	}

	@Column(name = "PT_CODE", length = 300)
	public String getPtCode() {
		return this.ptCode;
	}

	public void setPtCode(String ptCode) {
		this.ptCode = ptCode;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_DATE", length = 7)
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Column(name = "FILL_BY", length = 16)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Column(name = "USE_STATUS", length = 1)
	public String getUseStatus() {
		return this.useStatus;
	}

	public void setUseStatus(String useStatus) {
		this.useStatus = useStatus;
	}

	@Column(name = "SAVE_BY", length = 16)
	public String getSaveBy() {
		return this.saveBy;
	}

	public void setSaveBy(String saveBy) {
		this.saveBy = saveBy;
	}

	@Column(name = "SAVE_MARK", length = 1)
	public String getSaveMark() {
		return this.saveMark;
	}

	public void setSaveMark(String saveMark) {
		this.saveMark = saveMark;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}