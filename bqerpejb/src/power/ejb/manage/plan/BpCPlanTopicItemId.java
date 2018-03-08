package power.ejb.manage.plan;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BpCPlanTopicItemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class BpCPlanTopicItemId implements java.io.Serializable {

	// Fields

	private String topicCode;
	private String itemCode;

	// Constructors

	/** default constructor */
	public BpCPlanTopicItemId() {
	}

	/** full constructor */
	public BpCPlanTopicItemId(String topicCode, String itemCode) {
		this.topicCode = topicCode;
		this.itemCode = itemCode;
	}

	// Property accessors

	@Column(name = "TOPIC_CODE", nullable = false, length = 40)
	public String getTopicCode() {
		return this.topicCode;
	}

	public void setTopicCode(String topicCode) {
		this.topicCode = topicCode;
	}

	@Column(name = "ITEM_CODE", nullable = false, length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BpCPlanTopicItemId))
			return false;
		BpCPlanTopicItemId castOther = (BpCPlanTopicItemId) other;

		return ((this.getTopicCode() == castOther.getTopicCode()) || (this
				.getTopicCode() != null
				&& castOther.getTopicCode() != null && this.getTopicCode()
				.equals(castOther.getTopicCode())))
				&& ((this.getItemCode() == castOther.getItemCode()) || (this
						.getItemCode() != null
						&& castOther.getItemCode() != null && this
						.getItemCode().equals(castOther.getItemCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getTopicCode() == null ? 0 : this.getTopicCode().hashCode());
		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		return result;
	}

}