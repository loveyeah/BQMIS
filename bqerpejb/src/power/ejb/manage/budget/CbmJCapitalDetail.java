package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmJCapitalDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_CAPITAL_DETAIL")
public class CbmJCapitalDetail implements java.io.Serializable {

	// Fields

	private Long capitalDetailId;
	private Long capitalId;
	private String project;
	private Double materialCost;
	private Double workingCost;
	private Double otherCost;
	private Double deviceCost;
	private Double totalCost;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmJCapitalDetail() {
	}

	/** minimal constructor */
	public CbmJCapitalDetail(Long capitalDetailId, Long capitalId) {
		this.capitalDetailId = capitalDetailId;
		this.capitalId = capitalId;
	}

	/** full constructor */
	public CbmJCapitalDetail(Long capitalDetailId, Long capitalId,
			String project, Double materialCost, Double workingCost,
			Double otherCost, Double deviceCost, Double totalCost, String memo,
			String isUse, String enterpriseCode) {
		this.capitalDetailId = capitalDetailId;
		this.capitalId = capitalId;
		this.project = project;
		this.materialCost = materialCost;
		this.workingCost = workingCost;
		this.otherCost = otherCost;
		this.deviceCost = deviceCost;
		this.totalCost = totalCost;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "CAPITAL_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCapitalDetailId() {
		return this.capitalDetailId;
	}

	public void setCapitalDetailId(Long capitalDetailId) {
		this.capitalDetailId = capitalDetailId;
	}

	@Column(name = "CAPITAL_ID", nullable = false, precision = 10, scale = 0)
	public Long getCapitalId() {
		return this.capitalId;
	}

	public void setCapitalId(Long capitalId) {
		this.capitalId = capitalId;
	}

	@Column(name = "PROJECT", length = 80)
	public String getProject() {
		return this.project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	@Column(name = "MATERIAL_COST", precision = 15, scale = 4)
	public Double getMaterialCost() {
		return this.materialCost;
	}

	public void setMaterialCost(Double materialCost) {
		this.materialCost = materialCost;
	}

	@Column(name = "WORKING_COST", precision = 15, scale = 4)
	public Double getWorkingCost() {
		return this.workingCost;
	}

	public void setWorkingCost(Double workingCost) {
		this.workingCost = workingCost;
	}

	@Column(name = "OTHER_COST", precision = 15, scale = 4)
	public Double getOtherCost() {
		return this.otherCost;
	}

	public void setOtherCost(Double otherCost) {
		this.otherCost = otherCost;
	}

	@Column(name = "DEVICE_COST", precision = 15, scale = 4)
	public Double getDeviceCost() {
		return this.deviceCost;
	}

	public void setDeviceCost(Double deviceCost) {
		this.deviceCost = deviceCost;
	}

	@Column(name = "TOTAL_COST", precision = 15, scale = 4)
	public Double getTotalCost() {
		return this.totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
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