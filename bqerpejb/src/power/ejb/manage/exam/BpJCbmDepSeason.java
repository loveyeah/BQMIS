package power.ejb.manage.exam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpJCbmDepSeason entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_J_CBM_DEP_SEASON")
public class BpJCbmDepSeason implements java.io.Serializable {

	// Fields

	private Long seasonId;
	private String yearSeason;
	private Long depId;
	private Long overheadItemId;
	private Double seasonValue;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJCbmDepSeason() {
	}

	/** minimal constructor */
	public BpJCbmDepSeason(Long seasonId) {
		this.seasonId = seasonId;
	}

	/** full constructor */
	public BpJCbmDepSeason(Long seasonId, String yearSeason, Long depId,
			Long overheadItemId, Double seasonValue, String enterpriseCode) {
		this.seasonId = seasonId;
		this.yearSeason = yearSeason;
		this.depId = depId;
		this.overheadItemId = overheadItemId;
		this.seasonValue = seasonValue;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SEASON_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSeasonId() {
		return this.seasonId;
	}

	public void setSeasonId(Long seasonId) {
		this.seasonId = seasonId;
	}

	@Column(name = "YEAR_SEASON", length = 10)
	public String getYearSeason() {
		return this.yearSeason;
	}

	public void setYearSeason(String yearSeason) {
		this.yearSeason = yearSeason;
	}

	@Column(name = "DEP_ID", precision = 10, scale = 0)
	public Long getDepId() {
		return this.depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	@Column(name = "OVERHEAD_ITEM_ID", precision = 10, scale = 0)
	public Long getOverheadItemId() {
		return this.overheadItemId;
	}

	public void setOverheadItemId(Long overheadItemId) {
		this.overheadItemId = overheadItemId;
	}

	@Column(name = "SEASON_VALUE", precision = 10)
	public Double getSeasonValue() {
		return this.seasonValue;
	}

	public void setSeasonValue(Double seasonValue) {
		this.seasonValue = seasonValue;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}