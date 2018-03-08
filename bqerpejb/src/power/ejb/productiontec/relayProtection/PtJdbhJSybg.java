package power.ejb.productiontec.relayProtection;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJdbhJSybg entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_JDBH_J_SYBG")
public class PtJdbhJSybg implements java.io.Serializable {

	// Fields

	private Long jdsybgId;
	private Long deviceId;
	private Long sylbId;
	private String jdsybgName;
	private String testPlace;
	private Date testDate;
	private Date lastTestDate;
	private Date planTestDate;
	private String testType;
	private String weather;
	private Double temperature;
	private Double humidity;
	private String testBy;
	private String chargeBy;
	private String testSituation;
	private String content;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJdbhJSybg() {
	}

	/** minimal constructor */
	public PtJdbhJSybg(Long jdsybgId) {
		this.jdsybgId = jdsybgId;
	}

	/** full constructor */
	public PtJdbhJSybg(Long jdsybgId, Long deviceId, Long sylbId,
			String jdsybgName, String testPlace, Date testDate,
			Date lastTestDate, Date planTestDate, String testType,
			String weather, Double temperature, Double humidity, String testBy,
			String chargeBy, String testSituation, String content, String memo,
			String enterpriseCode) {
		this.jdsybgId = jdsybgId;
		this.deviceId = deviceId;
		this.sylbId = sylbId;
		this.jdsybgName = jdsybgName;
		this.testPlace = testPlace;
		this.testDate = testDate;
		this.lastTestDate = lastTestDate;
		this.planTestDate = planTestDate;
		this.testType = testType;
		this.weather = weather;
		this.temperature = temperature;
		this.humidity = humidity;
		this.testBy = testBy;
		this.chargeBy = chargeBy;
		this.testSituation = testSituation;
		this.content = content;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JDSYBG_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJdsybgId() {
		return this.jdsybgId;
	}

	public void setJdsybgId(Long jdsybgId) {
		this.jdsybgId = jdsybgId;
	}

	@Column(name = "DEVICE_ID", precision = 10, scale = 0)
	public Long getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "SYLB_ID", precision = 10, scale = 0)
	public Long getSylbId() {
		return this.sylbId;
	}

	public void setSylbId(Long sylbId) {
		this.sylbId = sylbId;
	}

	@Column(name = "JDSYBG_NAME", length = 50)
	public String getJdsybgName() {
		return this.jdsybgName;
	}

	public void setJdsybgName(String jdsybgName) {
		this.jdsybgName = jdsybgName;
	}

	@Column(name = "TEST_PLACE", length = 50)
	public String getTestPlace() {
		return this.testPlace;
	}

	public void setTestPlace(String testPlace) {
		this.testPlace = testPlace;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "TEST_DATE", length = 7)
	public Date getTestDate() {
		return this.testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_TEST_DATE", length = 7)
	public Date getLastTestDate() {
		return this.lastTestDate;
	}

	public void setLastTestDate(Date lastTestDate) {
		this.lastTestDate = lastTestDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_TEST_DATE", length = 7)
	public Date getPlanTestDate() {
		return this.planTestDate;
	}

	public void setPlanTestDate(Date planTestDate) {
		this.planTestDate = planTestDate;
	}

	@Column(name = "TEST_TYPE", length = 20)
	public String getTestType() {
		return this.testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	@Column(name = "WEATHER", length = 10)
	public String getWeather() {
		return this.weather;
	}

	public void setWeather(String weather) {
		this.weather = weather;
	}

	@Column(name = "TEMPERATURE", precision = 10)
	public Double getTemperature() {
		return this.temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	@Column(name = "HUMIDITY", precision = 10)
	public Double getHumidity() {
		return this.humidity;
	}

	public void setHumidity(Double humidity) {
		this.humidity = humidity;
	}

	@Column(name = "TEST_BY", length = 100)
	public String getTestBy() {
		return this.testBy;
	}

	public void setTestBy(String testBy) {
		this.testBy = testBy;
	}

	@Column(name = "CHARGE_BY", length = 16)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Column(name = "TEST_SITUATION", length = 500)
	public String getTestSituation() {
		return this.testSituation;
	}

	public void setTestSituation(String testSituation) {
		this.testSituation = testSituation;
	}

	@Column(name = "CONTENT", length = 256)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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