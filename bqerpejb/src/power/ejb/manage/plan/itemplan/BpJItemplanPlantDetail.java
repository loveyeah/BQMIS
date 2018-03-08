package power.ejb.manage.plan.itemplan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpJItemplanPlantDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_ITEMPLAN_PLANT_DETAIL")
public class BpJItemplanPlantDetail implements java.io.Serializable {

	// Fields

	private Long plantDetailId;
	private Long plantMainId;
	private Long economicItemId;
	private String plantPlan1112;
	private String plantPlan12;
	private String plantFact1112;
	private String plantFact12;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJItemplanPlantDetail() {
	}

	/** minimal constructor */
	public BpJItemplanPlantDetail(Long plantDetailId, Long plantMainId,
			Long economicItemId) {
		this.plantDetailId = plantDetailId;
		this.plantMainId = plantMainId;
		this.economicItemId = economicItemId;
	}

	/** full constructor */
	public BpJItemplanPlantDetail(Long plantDetailId, Long plantMainId,
			Long economicItemId, String plantPlan1112, String plantPlan12,
			String plantFact1112, String plantFact12, String isUse,
			String enterpriseCode) {
		this.plantDetailId = plantDetailId;
		this.plantMainId = plantMainId;
		this.economicItemId = economicItemId;
		this.plantPlan1112 = plantPlan1112;
		this.plantPlan12 = plantPlan12;
		this.plantFact1112 = plantFact1112;
		this.plantFact12 = plantFact12;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "PLANT_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPlantDetailId() {
		return this.plantDetailId;
	}

	public void setPlantDetailId(Long plantDetailId) {
		this.plantDetailId = plantDetailId;
	}

	@Column(name = "PLANT_MAIN_ID", nullable = false, precision = 10, scale = 0)
	public Long getPlantMainId() {
		return this.plantMainId;
	}

	public void setPlantMainId(Long plantMainId) {
		this.plantMainId = plantMainId;
	}

	@Column(name = "ECONOMIC_ITEM_ID", nullable = false, precision = 10, scale = 0)
	public Long getEconomicItemId() {
		return this.economicItemId;
	}

	public void setEconomicItemId(Long economicItemId) {
		this.economicItemId = economicItemId;
	}

	@Column(name = "PLANT_PLAN_1112", length = 40)
	public String getPlantPlan1112() {
		return this.plantPlan1112;
	}

	public void setPlantPlan1112(String plantPlan1112) {
		this.plantPlan1112 = plantPlan1112;
	}

	@Column(name = "PLANT_PLAN_12", length = 40)
	public String getPlantPlan12() {
		return this.plantPlan12;
	}

	public void setPlantPlan12(String plantPlan12) {
		this.plantPlan12 = plantPlan12;
	}

	@Column(name = "PLANT_FACT_1112", length = 40)
	public String getPlantFact1112() {
		return this.plantFact1112;
	}

	public void setPlantFact1112(String plantFact1112) {
		this.plantFact1112 = plantFact1112;
	}

	@Column(name = "PLANT_FACT_12", length = 40)
	public String getPlantFact12() {
		return this.plantFact12;
	}

	public void setPlantFact12(String plantFact12) {
		this.plantFact12 = plantFact12;
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