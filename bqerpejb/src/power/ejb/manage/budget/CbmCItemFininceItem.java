package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCItemFininceItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_ITEM_FININCE_ITEM")
public class CbmCItemFininceItem implements java.io.Serializable {

	// Fields

	private Long relationId;
	private Long itemId;
	private Long financeId;
	private String debitCredit;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmCItemFininceItem() {
	}

	/** minimal constructor */
	public CbmCItemFininceItem(Long relationId) {
		this.relationId = relationId;
	}

	/** full constructor */
	public CbmCItemFininceItem(Long relationId, Long itemId, Long financeId,
			String debitCredit, String isUse, String enterpriseCode) {
		this.relationId = relationId;
		this.itemId = itemId;
		this.financeId = financeId;
		this.debitCredit = debitCredit;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RELATION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRelationId() {
		return this.relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "FINANCE_ID", precision = 10, scale = 0)
	public Long getFinanceId() {
		return this.financeId;
	}

	public void setFinanceId(Long financeId) {
		this.financeId = financeId;
	}

	@Column(name = "DEBIT_CREDIT", length = 1)
	public String getDebitCredit() {
		return this.debitCredit;
	}

	public void setDebitCredit(String debitCredit) {
		this.debitCredit = debitCredit;
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