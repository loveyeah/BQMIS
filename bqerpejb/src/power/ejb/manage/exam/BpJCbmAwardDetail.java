package power.ejb.manage.exam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpJCbmAwardDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_CBM_AWARD_DETAIL")
public class BpJCbmAwardDetail implements java.io.Serializable {

	// Fields

	private Long declareDetailId;
	private String yearMonth;
	private Long affiliatedId;
	private Double cashBonus;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJCbmAwardDetail() {
	}

	/** minimal constructor */
	public BpJCbmAwardDetail(Long declareDetailId) {
		this.declareDetailId = declareDetailId;
	}

	/** full constructor */
	public BpJCbmAwardDetail(Long declareDetailId, String yearMonth,
			Long affiliatedId, Double cashBonus, String memo, String isUse,
			String enterpriseCode) {
		this.declareDetailId = declareDetailId;
		this.yearMonth = yearMonth;
		this.affiliatedId = affiliatedId;
		this.cashBonus = cashBonus;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DECLARE_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDeclareDetailId() {
		return this.declareDetailId;
	}

	public void setDeclareDetailId(Long declareDetailId) {
		this.declareDetailId = declareDetailId;
	}

	@Column(name = "YEAR_MONTH", length = 10)
	public String getYearMonth() {
		return this.yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Column(name = "AFFILIATED_ID", precision = 10, scale = 0)
	public Long getAffiliatedId() {
		return this.affiliatedId;
	}

	public void setAffiliatedId(Long affiliatedId) {
		this.affiliatedId = affiliatedId;
	}

	@Column(name = "CASH_BONUS", precision = 10)
	public Double getCashBonus() {
		return this.cashBonus;
	}

	public void setCashBonus(Double cashBonus) {
		this.cashBonus = cashBonus;
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