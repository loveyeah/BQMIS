package power.ejb.manage.plan;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * BpCPlanTopicItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_PLAN_TOPIC_ITEM")
public class BpCPlanTopicItem implements java.io.Serializable {

	// Fields

	private BpCPlanTopicItemId id;
	private String itemName;
	private Long displayNo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCPlanTopicItem() {
	}

	/** minimal constructor */
	public BpCPlanTopicItem(BpCPlanTopicItemId id) {
		this.id = id;
	}

	/** full constructor */
	public BpCPlanTopicItem(BpCPlanTopicItemId id, String itemName,
			Long displayNo, String enterpriseCode) {
		this.id = id;
		this.itemName = itemName;
		this.displayNo = displayNo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "topicCode", column = @Column(name = "TOPIC_CODE", nullable = false, length = 40)),
			@AttributeOverride(name = "itemCode", column = @Column(name = "ITEM_CODE", nullable = false, length = 40)) })
	public BpCPlanTopicItemId getId() {
		return this.id;
	}

	public void setId(BpCPlanTopicItemId id) {
		this.id = id;
	}

	@Column(name = "ITEM_NAME", length = 50)
	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "DISPLAY_NO", precision = 4, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}