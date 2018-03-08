package power.ejb.hr.labor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrJLaborStandard entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_LABOR_STANDARD")
public class HrJLaborStandard implements java.io.Serializable {

	// Fields

	private Long laborStandardId;
	private Long lbWorkId;
	private Long laborMaterialId;
	private Long spacingMonth;
	private Long materialNum;
	private String sendKind;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public HrJLaborStandard() {
	}

	/** minimal constructor */
	public HrJLaborStandard(Long laborStandardId) {
		this.laborStandardId = laborStandardId;
	}

	/** full constructor */
	public HrJLaborStandard(Long laborStandardId, Long lbWorkId,
			Long laborMaterialId, Long spacingMonth, Long materialNum,
			String sendKind, String enterpriseCode, String isUse) {
		this.laborStandardId = laborStandardId;
		this.lbWorkId = lbWorkId;
		this.laborMaterialId = laborMaterialId;
		this.spacingMonth = spacingMonth;
		this.materialNum = materialNum;
		this.sendKind = sendKind;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "LABOR_STANDARD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getLaborStandardId() {
		return this.laborStandardId;
	}

	public void setLaborStandardId(Long laborStandardId) {
		this.laborStandardId = laborStandardId;
	}

	@Column(name = "LB_WORK_ID", precision = 10, scale = 0)
	public Long getLbWorkId() {
		return this.lbWorkId;
	}

	public void setLbWorkId(Long lbWorkId) {
		this.lbWorkId = lbWorkId;
	}

	@Column(name = "LABOR_MATERIAL_ID", precision = 10, scale = 0)
	public Long getLaborMaterialId() {
		return this.laborMaterialId;
	}

	public void setLaborMaterialId(Long laborMaterialId) {
		this.laborMaterialId = laborMaterialId;
	}

	@Column(name = "SPACING_MONTH", precision = 10, scale = 0)
	public Long getSpacingMonth() {
		return this.spacingMonth;
	}

	public void setSpacingMonth(Long spacingMonth) {
		this.spacingMonth = spacingMonth;
	}

	@Column(name = "MATERIAL_NUM", precision = 10, scale = 0)
	public Long getMaterialNum() {
		return this.materialNum;
	}

	public void setMaterialNum(Long materialNum) {
		this.materialNum = materialNum;
	}

	@Column(name = "SEND_KIND", length = 1)
	public String getSendKind() {
		return this.sendKind;
	}

	public void setSendKind(String sendKind) {
		this.sendKind = sendKind;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}