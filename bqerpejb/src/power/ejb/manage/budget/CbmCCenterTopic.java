package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCCenterTopic entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_CENTER_TOPIC")
public class CbmCCenterTopic implements java.io.Serializable {

	// Fields

	private Long centerTopicId;
	private Long centerId;
	private Long topicId;
	private String directManager;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmCCenterTopic() {
	}

	/** minimal constructor */
	public CbmCCenterTopic(Long centerTopicId) {
		this.centerTopicId = centerTopicId;
	}

	/** full constructor */
	public CbmCCenterTopic(Long centerTopicId, Long centerId, Long topicId,
			String directManager, String isUse, String enterpriseCode) {
		this.centerTopicId = centerTopicId;
		this.centerId = centerId;
		this.topicId = topicId;
		this.directManager = directManager;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "CENTER_TOPIC_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCenterTopicId() {
		return this.centerTopicId;
	}

	public void setCenterTopicId(Long centerTopicId) {
		this.centerTopicId = centerTopicId;
	}

	@Column(name = "CENTER_ID", precision = 10, scale = 0)
	public Long getCenterId() {
		return this.centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}

	@Column(name = "TOPIC_ID", precision = 10, scale = 0)
	public Long getTopicId() {
		return this.topicId;
	}

	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}

	@Column(name = "DIRECT_MANAGER", length = 16)
	public String getDirectManager() {
		return this.directManager;
	}

	public void setDirectManager(String directManager) {
		this.directManager = directManager;
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