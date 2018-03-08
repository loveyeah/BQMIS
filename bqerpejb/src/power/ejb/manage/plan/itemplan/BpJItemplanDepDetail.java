package power.ejb.manage.plan.itemplan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpJItemplanDepDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_ITEMPLAN_DEP_DETAIL")
public class BpJItemplanDepDetail implements java.io.Serializable {

	// Fields

	private Long depDetailId;
	private Long depMainId;
	private Long economicItemId;
	private String depPlan1112;
	private String depPlan12;
	private String depFact1112;
	private String depFact12;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJItemplanDepDetail() {
	}

	/** minimal constructor */
	public BpJItemplanDepDetail(Long depDetailId, Long depMainId,
			Long economicItemId) {
		this.depDetailId = depDetailId;
		this.depMainId = depMainId;
		this.economicItemId = economicItemId;
	}

	/** full constructor */
	public BpJItemplanDepDetail(Long depDetailId, Long depMainId,
			Long economicItemId, String depPlan1112, String depPlan12,
			String depFact1112, String depFact12, String isUse,
			String enterpriseCode) {
		this.depDetailId = depDetailId;
		this.depMainId = depMainId;
		this.economicItemId = economicItemId;
		this.depPlan1112 = depPlan1112;
		this.depPlan12 = depPlan12;
		this.depFact1112 = depFact1112;
		this.depFact12 = depFact12;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DEP_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDepDetailId() {
		return this.depDetailId;
	}

	public void setDepDetailId(Long depDetailId) {
		this.depDetailId = depDetailId;
	}

	@Column(name = "DEP_MAIN_ID", nullable = false, precision = 10, scale = 0)
	public Long getDepMainId() {
		return this.depMainId;
	}

	public void setDepMainId(Long depMainId) {
		this.depMainId = depMainId;
	}

	@Column(name = "ECONOMIC_ITEM_ID", nullable = false, precision = 10, scale = 0)
	public Long getEconomicItemId() {
		return this.economicItemId;
	}

	public void setEconomicItemId(Long economicItemId) {
		this.economicItemId = economicItemId;
	}

	@Column(name = "DEP_PLAN_1112", length = 40)
	public String getDepPlan1112() {
		return this.depPlan1112;
	}

	public void setDepPlan1112(String depPlan1112) {
		this.depPlan1112 = depPlan1112;
	}

	@Column(name = "DEP_PLAN_12", length = 40)
	public String getDepPlan12() {
		return this.depPlan12;
	}

	public void setDepPlan12(String depPlan12) {
		this.depPlan12 = depPlan12;
	}

	@Column(name = "DEP_FACT_1112", length = 40)
	public String getDepFact1112() {
		return this.depFact1112;
	}

	public void setDepFact1112(String depFact1112) {
		this.depFact1112 = depFact1112;
	}

	@Column(name = "DEP_FACT_12", length = 40)
	public String getDepFact12() {
		return this.depFact12;
	}

	public void setDepFact12(String depFact12) {
		this.depFact12 = depFact12;
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