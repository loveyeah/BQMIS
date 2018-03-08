package power.ejb.manage.plan;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * BpCPlanFormula entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_PLAN_FORMULA")
public class BpCPlanFormula implements java.io.Serializable {

	// Fields

	private BpCPlanFormulaId id;
	private String formulaContent;
	private String fornulaType;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCPlanFormula() {
	}

	/** minimal constructor */
	public BpCPlanFormula(BpCPlanFormulaId id) {
		this.id = id;
	}

	/** full constructor */
	public BpCPlanFormula(BpCPlanFormulaId id, String formulaContent,
			String fornulaType, String enterpriseCode) {
		this.id = id;
		this.formulaContent = formulaContent;
		this.fornulaType = fornulaType;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "itemCode", column = @Column(name = "ITEM_CODE", nullable = false, length = 40)),
			@AttributeOverride(name = "formulaNo", column = @Column(name = "FORMULA_NO", nullable = false, precision = 10, scale = 0)) })
	public BpCPlanFormulaId getId() {
		return this.id;
	}

	public void setId(BpCPlanFormulaId id) {
		this.id = id;
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

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}