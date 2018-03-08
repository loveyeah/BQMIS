package power.ejb.hr.reward;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCStationQuantify entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_STATION_QUANTIFY")
public class HrCStationQuantify implements java.io.Serializable {

	// Fields

	private Long quantifyId;
	private String stationName;
	private Double quantifyProportion;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCStationQuantify() {
	}

	/** minimal constructor */
	public HrCStationQuantify(Long quantifyId) {
		this.quantifyId = quantifyId;
	}

	/** full constructor */
	public HrCStationQuantify(Long quantifyId, String stationName,
			Double quantifyProportion, String isUse, String enterpriseCode) {
		this.quantifyId = quantifyId;
		this.stationName = stationName;
		this.quantifyProportion = quantifyProportion;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "QUANTIFY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getQuantifyId() {
		return this.quantifyId;
	}

	public void setQuantifyId(Long quantifyId) {
		this.quantifyId = quantifyId;
	}

	@Column(name = "STATION_NAME", length = 100)
	public String getStationName() {
		return this.stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Column(name = "QUANTIFY_PROPORTION", precision = 15, scale = 4)
	public Double getQuantifyProportion() {
		return this.quantifyProportion;
	}

	public void setQuantifyProportion(Double quantifyProportion) {
		this.quantifyProportion = quantifyProportion;
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