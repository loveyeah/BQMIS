package power.ejb.manage.plan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCPlanTopic entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_PLAN_TOPIC")
public class BpCPlanTopic implements java.io.Serializable {

	// Fields

	private String topicCode;
	private String topicName;
	private String topicMemo;
	private Long displayNo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCPlanTopic() {
	}

	/** minimal constructor */
	public BpCPlanTopic(String topicCode) {
		this.topicCode = topicCode;
	}

	/** full constructor */
	public BpCPlanTopic(String topicCode, String topicName, String topicMemo,
			Long displayNo, String enterpriseCode) {
		this.topicCode = topicCode;
		this.topicName = topicName;
		this.topicMemo = topicMemo;
		this.displayNo = displayNo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TOPIC_CODE", unique = true, nullable = false, length = 40)
	public String getTopicCode() {
		return this.topicCode;
	}

	public void setTopicCode(String topicCode) {
		this.topicCode = topicCode;
	}

	@Column(name = "TOPIC_NAME", length = 80)
	public String getTopicName() {
		return this.topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Column(name = "TOPIC_MEMO")
	public String getTopicMemo() {
		return this.topicMemo;
	}

	public void setTopicMemo(String topicMemo) {
		this.topicMemo = topicMemo;
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