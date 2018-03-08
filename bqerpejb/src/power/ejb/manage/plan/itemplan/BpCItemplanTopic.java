package power.ejb.manage.plan.itemplan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCItemplanTopic entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_ITEMPLAN_TOPIC")
public class BpCItemplanTopic implements java.io.Serializable {

	// Fields

	private Long topicId;
	private String topicName;
	private String topicMemo;
	private Long displayNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCItemplanTopic() {
	}

	/** minimal constructor */
	public BpCItemplanTopic(Long topicId) {
		this.topicId = topicId;
	}

	/** full constructor */
	public BpCItemplanTopic(Long topicId, String topicName, String topicMemo,
			Long displayNo, String isUse, String enterpriseCode) {
		this.topicId = topicId;
		this.topicName = topicName;
		this.topicMemo = topicMemo;
		this.displayNo = displayNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TOPIC_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTopicId() {
		return this.topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	@Column(name = "TOPIC_NAME", length = 40)
	public String getTopicName() {
		return this.topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Column(name = "TOPIC_MEMO", length = 200)
	public String getTopicMemo() {
		return this.topicMemo;
	}

	public void setTopicMemo(String topicMemo) {
		this.topicMemo = topicMemo;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
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