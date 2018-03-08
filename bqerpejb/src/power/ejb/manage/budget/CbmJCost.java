package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmJCost entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_COST")
public class CbmJCost implements java.io.Serializable {

	// Fields

	private Long costId;
	private String analyseDate;
	private Long itemId;
	private Double factValue;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmJCost() {
	}

	/** minimal constructor */
	public CbmJCost(Long costId) {
		this.costId = costId;
	}

	/** full constructor */
	public CbmJCost(Long costId, String analyseDate, Long itemId,
			Double factValue, String memo, String isUse, String enterpriseCode) {
		this.costId = costId;
		this.analyseDate = analyseDate;
		this.itemId = itemId;
		this.factValue = factValue;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "COST_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCostId() {
		return this.costId;
	}

	public void setCostId(Long costId) {
		this.costId = costId;
	}

	@Column(name = "ANALYSE_DATE", length = 10)
	public String getAnalyseDate() {
		return this.analyseDate;
	}

	public void setAnalyseDate(String analyseDate) {
		this.analyseDate = analyseDate;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "FACT_VALUE", precision = 18, scale = 6)
	public Double getFactValue() {
		return this.factValue;
	}

	public void setFactValue(Double factValue) {
		this.factValue = factValue;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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