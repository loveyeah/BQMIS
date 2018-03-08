package power.ejb.manage.plan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpJPlanTopicItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_PLAN_TOPIC_ITEM")
public class BpJPlanTopicItem implements java.io.Serializable {

	// Fields

	private Long topicItemId;
	private Long reportId;
	private String itemCode;
	private Double planValue;
	private Double lastValue;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJPlanTopicItem() {
	}

	/** minimal constructor */
	public BpJPlanTopicItem(Long topicItemId) {
		this.topicItemId = topicItemId;
	}

	/** full constructor */
	public BpJPlanTopicItem(Long topicItemId, Long reportId, String itemCode,
			Double planValue, Double lastValue, String enterpriseCode) {
		this.topicItemId = topicItemId;
		this.reportId = reportId;
		this.itemCode = itemCode;
		this.planValue = planValue;
		this.lastValue = lastValue;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TOPIC_ITEM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTopicItemId() {
		return this.topicItemId;
	}

	public void setTopicItemId(Long topicItemId) {
		this.topicItemId = topicItemId;
	}

	@Column(name = "REPORT_ID", precision = 10, scale = 0)
	public Long getReportId() {
		return this.reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	@Column(name = "ITEM_CODE", length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "PLAN_VALUE", precision = 15, scale = 4)
	public Double getPlanValue() {
		return this.planValue;
	}

	public void setPlanValue(Double planValue) {
		this.planValue = planValue;
	}

	@Column(name = "LAST_VALUE", precision = 15, scale = 4)
	public Double getLastValue() {
		return this.lastValue;
	}

	public void setLastValue(Double lastValue) {
		this.lastValue = lastValue;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}