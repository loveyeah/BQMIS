package power.ejb.productiontec.relayProtection;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJdbhJBhzzdzqk entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_JDBH_J_BHZZDZQK")
public class PtJdbhJBhzzdzqk implements java.io.Serializable {

	// Fields

	private Long bhzzdzId;
	private Long deviceId;
	private Date actDate;
	private String chargeDep;
	private String actAppaise;
	private Long actNum;
	private Long waveNumber;
	private Long waveGoodNumber;
	private String protectAct;
	private String errorAnalyze;
	private String fillBy;
	private Date fillDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhJBhzzdzqk() {
	}

	/** minimal constructor */
	public PtJdbhJBhzzdzqk(Long bhzzdzId, Long deviceId) {
		this.bhzzdzId = bhzzdzId;
		this.deviceId = deviceId;
	}

	/** full constructor */
	public PtJdbhJBhzzdzqk(Long bhzzdzId, Long deviceId, Date actDate,
			String chargeDep, String actAppaise, Long actNum, Long waveNumber,
			Long waveGoodNumber, String protectAct, String errorAnalyze,
			String fillBy, Date fillDate, String enterpriseCode) {
		this.bhzzdzId = bhzzdzId;
		this.deviceId = deviceId;
		this.actDate = actDate;
		this.chargeDep = chargeDep;
		this.actAppaise = actAppaise;
		this.actNum = actNum;
		this.waveNumber = waveNumber;
		this.waveGoodNumber = waveGoodNumber;
		this.protectAct = protectAct;
		this.errorAnalyze = errorAnalyze;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BHZZDZ_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBhzzdzId() {
		return this.bhzzdzId;
	}

	public void setBhzzdzId(Long bhzzdzId) {
		this.bhzzdzId = bhzzdzId;
	}

	@Column(name = "DEVICE_ID", nullable = false, precision = 10, scale = 0)
	public Long getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ACT_DATE", length = 7)
	public Date getActDate() {
		return this.actDate;
	}

	public void setActDate(Date actDate) {
		this.actDate = actDate;
	}

	@Column(name = "CHARGE_DEP", length = 20)
	public String getChargeDep() {
		return this.chargeDep;
	}

	public void setChargeDep(String chargeDep) {
		this.chargeDep = chargeDep;
	}

	@Column(name = "ACT_APPAISE", length = 10)
	public String getActAppaise() {
		return this.actAppaise;
	}

	public void setActAppaise(String actAppaise) {
		this.actAppaise = actAppaise;
	}

	@Column(name = "ACT_NUM", precision = 4, scale = 0)
	public Long getActNum() {
		return this.actNum;
	}

	public void setActNum(Long actNum) {
		this.actNum = actNum;
	}

	@Column(name = "WAVE_NUMBER", precision = 10, scale = 0)
	public Long getWaveNumber() {
		return this.waveNumber;
	}

	public void setWaveNumber(Long waveNumber) {
		this.waveNumber = waveNumber;
	}

	@Column(name = "WAVE_GOOD_NUMBER", precision = 10, scale = 0)
	public Long getWaveGoodNumber() {
		return this.waveGoodNumber;
	}

	public void setWaveGoodNumber(Long waveGoodNumber) {
		this.waveGoodNumber = waveGoodNumber;
	}

	@Column(name = "PROTECT_ACT", length = 500)
	public String getProtectAct() {
		return this.protectAct;
	}

	public void setProtectAct(String protectAct) {
		this.protectAct = protectAct;
	}

	@Column(name = "ERROR_ANALYZE", length = 500)
	public String getErrorAnalyze() {
		return this.errorAnalyze;
	}

	public void setErrorAnalyze(String errorAnalyze) {
		this.errorAnalyze = errorAnalyze;
	}

	@Column(name = "FILL_BY", length = 16)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FILL_DATE", length = 7)
	public Date getFillDate() {
		return this.fillDate;
	}

	public void setFillDate(Date fillDate) {
		this.fillDate = fillDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}