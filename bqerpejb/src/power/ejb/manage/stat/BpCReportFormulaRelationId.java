package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BpCReportFormulaRelationId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class BpCReportFormulaRelationId implements java.io.Serializable {

	// Fields

	private String relationCode;
	private Long formulaNo;
	private String formulaContent;
	private String fornulaType;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCReportFormulaRelationId() {
	}

	/** minimal constructor */
	public BpCReportFormulaRelationId(String relationCode, Long formulaNo) {
		this.relationCode = relationCode;
		this.formulaNo = formulaNo;
	}

	/** full constructor */
	public BpCReportFormulaRelationId(String relationCode, Long formulaNo,
			String formulaContent, String fornulaType, String enterpriseCode) {
		this.relationCode = relationCode;
		this.formulaNo = formulaNo;
		this.formulaContent = formulaContent;
		this.fornulaType = fornulaType;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors

	@Column(name = "RELATION_CODE", nullable = false)
	public String getRelationCode() {
		return this.relationCode;
	}

	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}

	@Column(name = "FORMULA_NO", nullable = false, precision = 10, scale = 0)
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

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BpCReportFormulaRelationId))
			return false;
		BpCReportFormulaRelationId castOther = (BpCReportFormulaRelationId) other;

		return ((this.getRelationCode() == castOther.getRelationCode()) || (this
				.getRelationCode() != null
				&& castOther.getRelationCode() != null && this
				.getRelationCode().equals(castOther.getRelationCode())))
				&& ((this.getFormulaNo() == castOther.getFormulaNo()) || (this
						.getFormulaNo() != null
						&& castOther.getFormulaNo() != null && this
						.getFormulaNo().equals(castOther.getFormulaNo())))
				&& ((this.getFormulaContent() == castOther.getFormulaContent()) || (this
						.getFormulaContent() != null
						&& castOther.getFormulaContent() != null && this
						.getFormulaContent().equals(
								castOther.getFormulaContent())))
				&& ((this.getFornulaType() == castOther.getFornulaType()) || (this
						.getFornulaType() != null
						&& castOther.getFornulaType() != null && this
						.getFornulaType().equals(castOther.getFornulaType())))
				&& ((this.getEnterpriseCode() == castOther.getEnterpriseCode()) || (this
						.getEnterpriseCode() != null
						&& castOther.getEnterpriseCode() != null && this
						.getEnterpriseCode().equals(
								castOther.getEnterpriseCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getRelationCode() == null ? 0 : this.getRelationCode()
						.hashCode());
		result = 37 * result
				+ (getFormulaNo() == null ? 0 : this.getFormulaNo().hashCode());
		result = 37
				* result
				+ (getFormulaContent() == null ? 0 : this.getFormulaContent()
						.hashCode());
		result = 37
				* result
				+ (getFornulaType() == null ? 0 : this.getFornulaType()
						.hashCode());
		result = 37
				* result
				+ (getEnterpriseCode() == null ? 0 : this.getEnterpriseCode()
						.hashCode());
		return result;
	}

}