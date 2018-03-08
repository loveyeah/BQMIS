package power.ejb.manage.plan.itemplan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpJItemplanTecDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_ITEMPLAN_TEC_DETAIL")
public class BpJItemplanTecDetail implements java.io.Serializable {

	// Fields

	private Long tecDetailId;
	private Long tecMainId;
	private Long technologyItemId;
	private Long depId;
	private String tecPlan;
	private String tecFact;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJItemplanTecDetail() {
	}

	/** minimal constructor */
	public BpJItemplanTecDetail(Long tecDetailId, Long tecMainId,
			Long technologyItemId, Long depId) {
		this.tecDetailId = tecDetailId;
		this.tecMainId = tecMainId;
		this.technologyItemId = technologyItemId;
		this.depId = depId;
	}

	/** full constructor */
	public BpJItemplanTecDetail(Long tecDetailId, Long tecMainId,
			Long technologyItemId, Long depId, String tecPlan, String tecFact,
			String isUse, String enterpriseCode) {
		this.tecDetailId = tecDetailId;
		this.tecMainId = tecMainId;
		this.technologyItemId = technologyItemId;
		this.depId = depId;
		this.tecPlan = tecPlan;
		this.tecFact = tecFact;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TEC_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTecDetailId() {
		return this.tecDetailId;
	}

	public void setTecDetailId(Long tecDetailId) {
		this.tecDetailId = tecDetailId;
	}

	@Column(name = "TEC_MAIN_ID", nullable = false, precision = 10, scale = 0)
	public Long getTecMainId() {
		return this.tecMainId;
	}

	public void setTecMainId(Long tecMainId) {
		this.tecMainId = tecMainId;
	}

	@Column(name = "TECHNOLOGY_ITEM_ID", nullable = false, precision = 10, scale = 0)
	public Long getTechnologyItemId() {
		return this.technologyItemId;
	}

	public void setTechnologyItemId(Long technologyItemId) {
		this.technologyItemId = technologyItemId;
	}

	@Column(name = "DEP_ID", nullable = false, precision = 10, scale = 0)
	public Long getDepId() {
		return this.depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	@Column(name = "TEC_PLAN", length = 40)
	public String getTecPlan() {
		return this.tecPlan;
	}

	public void setTecPlan(String tecPlan) {
		this.tecPlan = tecPlan;
	}

	@Column(name = "TEC_FACT", length = 40)
	public String getTecFact() {
		return this.tecFact;
	}

	public void setTecFact(String tecFact) {
		this.tecFact = tecFact;
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