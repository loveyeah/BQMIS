package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCTopic entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_TOPIC")
public class CbmCTopic implements java.io.Serializable {

	// Fields

	private Long topicId;
	private String topicCode;
	private String topicName;
	private String budgetType;
	private String dataType;
	private String timeType;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmCTopic() {
	}

	/** minimal constructor */
	public CbmCTopic(Long topicId) {
		this.topicId = topicId;
	}

	/** full constructor */
	public CbmCTopic(Long topicId, String topicCode, String topicName,
			String budgetType, String dataType, String timeType, String isUse,
			String enterpriseCode) {
		this.topicId = topicId;
		this.topicCode = topicCode;
		this.topicName = topicName;
		this.budgetType = budgetType;
		this.dataType = dataType;
		this.timeType = timeType;
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

	@Column(name = "TOPIC_CODE", length = 3)
	public String getTopicCode() {
		return this.topicCode;
	}

	public void setTopicCode(String topicCode) {
		this.topicCode = topicCode;
	}

	@Column(name = "TOPIC_NAME", length = 60)
	public String getTopicName() {
		return this.topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Column(name = "BUDGET_TYPE", length = 1)
	public String getBudgetType() {
		return this.budgetType;
	}

	public void setBudgetType(String budgetType) {
		this.budgetType = budgetType;
	}

	@Column(name = "DATA_TYPE", length = 1)
	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "TIME_TYPE", length = 1)
	public String getTimeType() {
		return this.timeType;
	}

	public void setTimeType(String timeType) {
		this.timeType = timeType;
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