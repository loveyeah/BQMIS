package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmJAnalysisYear entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_ANALYSIS_YEAR")
public class CbmJAnalysisYear implements java.io.Serializable {

	// Fields

	private Long analysisYearId;
	private Long itemId;
	private Long centerId;
	private String dataTime;
	private Double totalFact;
	private Double yearBudget;
	private Double percentValue;
	private String itemContent;
	private String itemExplain;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmJAnalysisYear() {
	}

	/** minimal constructor */
	public CbmJAnalysisYear(Long analysisYearId, String dataTime) {
		this.analysisYearId = analysisYearId;
		this.dataTime = dataTime;
	}

	/** full constructor */
	public CbmJAnalysisYear(Long analysisYearId, Long itemId, Long centerId,
			String dataTime, Double totalFact, Double yearBudget,
			Double percentValue, String itemContent, String itemExplain,
			String isUse, String enterpriseCode) {
		this.analysisYearId = analysisYearId;
		this.itemId = itemId;
		this.centerId = centerId;
		this.dataTime = dataTime;
		this.totalFact = totalFact;
		this.yearBudget = yearBudget;
		this.percentValue = percentValue;
		this.itemContent = itemContent;
		this.itemExplain = itemExplain;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ANALYSIS_YEAR_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAnalysisYearId() {
		return this.analysisYearId;
	}

	public void setAnalysisYearId(Long analysisYearId) {
		this.analysisYearId = analysisYearId;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "CENTER_ID", precision = 10, scale = 0)
	public Long getCenterId() {
		return this.centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}

	@Column(name = "DATA_TIME", nullable = false, length = 10)
	public String getDataTime() {
		return this.dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	@Column(name = "TOTAL_FACT", precision = 18, scale = 6)
	public Double getTotalFact() {
		return this.totalFact;
	}

	public void setTotalFact(Double totalFact) {
		this.totalFact = totalFact;
	}

	@Column(name = "YEAR_BUDGET", precision = 18, scale = 6)
	public Double getYearBudget() {
		return this.yearBudget;
	}

	public void setYearBudget(Double yearBudget) {
		this.yearBudget = yearBudget;
	}

	@Column(name = "PERCENT_VALUE", precision = 10, scale = 4)
	public Double getPercentValue() {
		return this.percentValue;
	}

	public void setPercentValue(Double percentValue) {
		this.percentValue = percentValue;
	}

	@Column(name = "ITEM_CONTENT", length = 100)
	public String getItemContent() {
		return this.itemContent;
	}

	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}

	@Column(name = "ITEM_EXPLAIN", length = 500)
	public String getItemExplain() {
		return this.itemExplain;
	}

	public void setItemExplain(String itemExplain) {
		this.itemExplain = itemExplain;
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