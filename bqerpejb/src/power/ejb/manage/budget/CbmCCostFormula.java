package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCCostFormula entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "CBM_C_COST_FORMULA")
public class CbmCCostFormula implements java.io.Serializable {

	// Fields

	private Long formulaId;
	private Long itemId;
	private Long formulaNo;
	private String formulaContent;
	private String fornulaType;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmCCostFormula() {
	}

	/** minimal constructor */
	public CbmCCostFormula(Long formulaId) {
		this.formulaId = formulaId;
	}

	/** full constructor */
	public CbmCCostFormula(Long formulaId, Long itemId, Long formulaNo,
			String formulaContent, String fornulaType, String isUse,
			String enterpriseCode) {
		this.formulaId = formulaId;
		this.itemId = itemId;
		this.formulaNo = formulaNo;
		this.formulaContent = formulaContent;
		this.fornulaType = fornulaType;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "FORMULA_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFormulaId() {
		return this.formulaId;
	}

	public void setFormulaId(Long formulaId) {
		this.formulaId = formulaId;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "FORMULA_NO", precision = 10, scale = 0)
	public Long getFormulaNo() {
		return this.formulaNo;
	}

	public void setFormulaNo(Long formulaNo) {
		this.formulaNo = formulaNo;
	}

	@Column(name = "FORMULA_CONTENT", length = 40)
	public String getFormulaContent() {
		return this.formulaContent;
	}

	public void setFormulaContent(String formulaContent) {
		this.formulaContent = formulaContent;
	}

	@Column(name = "FORNULA_TYPE", length = 1)
	public String getFornulaType() {
		return this.fornulaType;
	}

	public void setFornulaType(String fornulaType) {
		this.fornulaType = fornulaType;
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