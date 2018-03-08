package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCFinanceItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_FINANCE_ITEM")
public class CbmCFinanceItem implements java.io.Serializable {

	// Fields

	private Long financeId;
	private String financeItem;
	private String financeName;
	private String upperItem;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmCFinanceItem() {
	}

	/** minimal constructor */
	public CbmCFinanceItem(Long financeId) {
		this.financeId = financeId;
	}

	/** full constructor */
	public CbmCFinanceItem(Long financeId, String financeItem,
			String financeName, String upperItem, String isUse,
			String enterpriseCode) {
		this.financeId = financeId;
		this.financeItem = financeItem;
		this.financeName = financeName;
		this.upperItem = upperItem;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "FINANCE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFinanceId() {
		return this.financeId;
	}

	public void setFinanceId(Long financeId) {
		this.financeId = financeId;
	}

	@Column(name = "FINANCE_ITEM", length = 40)
	public String getFinanceItem() {
		return this.financeItem;
	}

	public void setFinanceItem(String financeItem) {
		this.financeItem = financeItem;
	}

	@Column(name = "FINANCE_NAME", length = 50)
	public String getFinanceName() {
		return this.financeName;
	}

	public void setFinanceName(String financeName) {
		this.financeName = financeName;
	}

	@Column(name = "UPPER_ITEM", length = 40)
	public String getUpperItem() {
		return this.upperItem;
	}

	public void setUpperItem(String upperItem) {
		this.upperItem = upperItem;
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