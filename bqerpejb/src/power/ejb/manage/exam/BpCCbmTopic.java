package power.ejb.manage.exam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCCbmTopic entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_CBM_TOPIC", schema = "POWER")
public class BpCCbmTopic implements java.io.Serializable {

	// Fields

	private Long topicId;
	private String topicName;
	private Double coefficient;
	private Long displayNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCCbmTopic() {
	}

	/** minimal constructor */
	public BpCCbmTopic(Long topicId) {
		this.topicId = topicId;
	}

	/** full constructor */
	public BpCCbmTopic(Long topicId, String topicName, Double coefficient,
			Long displayNo, String isUse, String enterpriseCode) {
		this.topicId = topicId;
		this.topicName = topicName;
		this.coefficient = coefficient;
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

	@Column(name = "TOPIC_NAME", length = 30)
	public String getTopicName() {
		return this.topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Column(name = "COEFFICIENT", precision = 5)
	public Double getCoefficient() {
		return this.coefficient;
	}

	public void setCoefficient(Double coefficient) {
		this.coefficient = coefficient;
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