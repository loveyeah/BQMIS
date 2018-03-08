package power.ejb.run.runlog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCLogWeather entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_LOG_WEATHER", schema = "POWER")
public class RunCLogWeather implements java.io.Serializable {
	// Fields

	private Long weatherKeyId;
	private String weatherCode;
	private String weatherName;
	private Long diaplayNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunCLogWeather() {
	}

	/** minimal constructor */
	public RunCLogWeather(Long weatherKeyId) {
		this.weatherKeyId = weatherKeyId;
	}

	/** full constructor */
	public RunCLogWeather(Long weatherKeyId, String weatherCode,
			String weatherName, Long diaplayNo, String isUse,
			String enterpriseCode) {
		this.weatherKeyId = weatherKeyId;
		this.weatherCode = weatherCode;
		this.weatherName = weatherName;
		this.diaplayNo = diaplayNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "WEATHER_KEY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getWeatherKeyId() {
		return this.weatherKeyId;
	}

	public void setWeatherKeyId(Long weatherKeyId) {
		this.weatherKeyId = weatherKeyId;
	}

	@Column(name = "WEATHER_CODE", length = 10)
	public String getWeatherCode() {
		return this.weatherCode;
	}

	public void setWeatherCode(String weatherCode) {
		this.weatherCode = weatherCode;
	}

	@Column(name = "WEATHER_NAME", length = 20)
	public String getWeatherName() {
		return this.weatherName;
	}

	public void setWeatherName(String weatherName) {
		this.weatherName = weatherName;
	}

	@Column(name = "DIAPLAY_NO", precision = 10, scale = 0)
	public Long getDiaplayNo() {
		return this.diaplayNo;
	}

	public void setDiaplayNo(Long diaplayNo) {
		this.diaplayNo = diaplayNo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}