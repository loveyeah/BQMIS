package power.ejb.manage.plan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJPlanTopic entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_PLAN_TOPIC")
public class BpJPlanTopic implements java.io.Serializable {

	// Fields

	private Long reportId;
	private String topicCode;
	private Date planTime;
	private String editBy;
	private Date editDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJPlanTopic() {
	}

	/** minimal constructor */
	public BpJPlanTopic(Long reportId, String topicCode, Date planTime) {
		this.reportId = reportId;
		this.topicCode = topicCode;
		this.planTime = planTime;
	}

	/** full constructor */
	public BpJPlanTopic(Long reportId, String topicCode, Date planTime,
			String editBy, Date editDate, String enterpriseCode) {
		this.reportId = reportId;
		this.topicCode = topicCode;
		this.planTime = planTime;
		this.editBy = editBy;
		this.editDate = editDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "REPORT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getReportId() {
		return this.reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	@Column(name = "TOPIC_CODE", nullable = false, length = 40)
	public String getTopicCode() {
		return this.topicCode;
	}

	public void setTopicCode(String topicCode) {
		this.topicCode = topicCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_TIME", nullable = false, length = 7)
	public Date getPlanTime() {
		return this.planTime;
	}

	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}

	@Column(name = "EDIT_BY", length = 16)
	public String getEditBy() {
		return this.editBy;
	}

	public void setEditBy(String editBy) {
		this.editBy = editBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EDIT_DATE", length = 7)
	public Date getEditDate() {
		return this.editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}