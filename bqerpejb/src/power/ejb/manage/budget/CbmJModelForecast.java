package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmJModelForecast entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_MODEL_FORECAST")
public class CbmJModelForecast implements java.io.Serializable {

	// Fields

	private Long forecastId;
	private Long modelItemId;
	private String forecastTime;
	private Double forecastValue;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmJModelForecast() {
	}

	/** minimal constructor */
	public CbmJModelForecast(Long forecastId) {
		this.forecastId = forecastId;
	}

	/** full constructor */
	public CbmJModelForecast(Long forecastId, Long modelItemId,
			String forecastTime, Double forecastValue, String enterpriseCode) {
		this.forecastId = forecastId;
		this.modelItemId = modelItemId;
		this.forecastTime = forecastTime;
		this.forecastValue = forecastValue;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "FORECAST_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getForecastId() {
		return this.forecastId;
	}

	public void setForecastId(Long forecastId) {
		this.forecastId = forecastId;
	}

	@Column(name = "MODEL_ITEM_ID", precision = 10, scale = 0)
	public Long getModelItemId() {
		return this.modelItemId;
	}

	public void setModelItemId(Long modelItemId) {
		this.modelItemId = modelItemId;
	}

	@Column(name = "FORECAST_TIME", length = 10)
	public String getForecastTime() {
		return this.forecastTime;
	}

	public void setForecastTime(String forecastTime) {
		this.forecastTime = forecastTime;
	}

	@Column(name = "FORECAST_VALUE", precision = 15, scale = 4)
	public Double getForecastValue() {
		return this.forecastValue;
	}

	public void setForecastValue(Double forecastValue) {
		this.forecastValue = forecastValue;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}