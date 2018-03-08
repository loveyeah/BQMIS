package power.ejb.manage.client;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ConCAppraiseItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_C_APPRAISE_ITEM")
public class ConCAppraiseItem implements java.io.Serializable {

	// Fields

	private Long eventId;
	private String appraiseItem;
	private Long appraiseMark;
	private String appraiseCriterion;
	private Long displayNo;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public ConCAppraiseItem() {
	}

	/** minimal constructor */
	public ConCAppraiseItem(Long eventId) {
		this.eventId = eventId;
	}

	/** full constructor */
	public ConCAppraiseItem(Long eventId, String appraiseItem,
			Long appraiseMark, String appraiseCriterion, Long displayNo,
			String enterpriseCode, String isUse) {
		this.eventId = eventId;
		this.appraiseItem = appraiseItem;
		this.appraiseMark = appraiseMark;
		this.appraiseCriterion = appraiseCriterion;
		this.displayNo = displayNo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "EVENT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEventId() {
		return this.eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	@Column(name = "APPRAISE_ITEM", length = 60)
	public String getAppraiseItem() {
		return this.appraiseItem;
	}

	public void setAppraiseItem(String appraiseItem) {
		this.appraiseItem = appraiseItem;
	}

	@Column(name = "APPRAISE_MARK", precision = 2, scale = 0)
	public Long getAppraiseMark() {
		return this.appraiseMark;
	}

	public void setAppraiseMark(Long appraiseMark) {
		this.appraiseMark = appraiseMark;
	}

	@Column(name = "APPRAISE_CRITERION", length = 600)
	public String getAppraiseCriterion() {
		return this.appraiseCriterion;
	}

	public void setAppraiseCriterion(String appraiseCriterion) {
		this.appraiseCriterion = appraiseCriterion;
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

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}