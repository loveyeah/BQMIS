package power.ejb.manage.budget;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CbmCBasis entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "CBM_C_BASIS")
public class CbmCBasis implements java.io.Serializable {

	// Fields

	private Long basisId;
	private Long budgetItemId;
	private String budgetBasis;
	private Double budgetAmount;
	private String lastModifyBy;
	private Date lastModifyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmCBasis() {
	}

	/** minimal constructor */
	public CbmCBasis(Long basisId) {
		this.basisId = basisId;
	}

	/** full constructor */
	public CbmCBasis(Long basisId, Long budgetItemId, String budgetBasis,
			Double budgetAmount, String lastModifyBy, Date lastModifyDate,
			String isUse, String enterpriseCode) {
		this.basisId = basisId;
		this.budgetItemId = budgetItemId;
		this.budgetBasis = budgetBasis;
		this.budgetAmount = budgetAmount;
		this.lastModifyBy = lastModifyBy;
		this.lastModifyDate = lastModifyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BASIS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBasisId() {
		return this.basisId;
	}

	public void setBasisId(Long basisId) {
		this.basisId = basisId;
	}

	@Column(name = "BUDGET_ITEM_ID", precision = 10, scale = 0)
	public Long getBudgetItemId() {
		return this.budgetItemId;
	}

	public void setBudgetItemId(Long budgetItemId) {
		this.budgetItemId = budgetItemId;
	}

	@Column(name = "BUDGET_BASIS", length = 200)
	public String getBudgetBasis() {
		return this.budgetBasis;
	}

	public void setBudgetBasis(String budgetBasis) {
		this.budgetBasis = budgetBasis;
	}

	@Column(name = "BUDGET_AMOUNT", precision = 18, scale = 6)
	public Double getBudgetAmount() {
		return this.budgetAmount;
	}

	public void setBudgetAmount(Double budgetAmount) {
		this.budgetAmount = budgetAmount;
	}

	@Column(name = "LAST_MODIFY_BY", length = 20)
	public String getLastModifyBy() {
		return this.lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFY_DATE", length = 7)
	public Date getLastModifyDate() {
		return this.lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
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