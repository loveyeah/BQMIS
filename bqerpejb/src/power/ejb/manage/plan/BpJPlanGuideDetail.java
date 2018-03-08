package power.ejb.manage.plan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJPlanGuideDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_PLAN_GUIDE_DETAIL")
public class BpJPlanGuideDetail implements java.io.Serializable {

	// Fields

	private Long guideId;
	private Date planTime;
	private String guideContent;
	private String referDepcode;
	private String mainDepcode;
	private String taskDepcode;
	private String ifComplete;
	private String completeDesc;
	private String ifCheck;
	private String checkStatus;
	private String targetDepcode;
	private String checkDesc;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJPlanGuideDetail() {
	}

	/** minimal constructor */
	public BpJPlanGuideDetail(Long guideId, Date planTime) {
		this.guideId = guideId;
		this.planTime = planTime;
	}

	/** full constructor */
	public BpJPlanGuideDetail(Long guideId, Date planTime, String guideContent,
			String referDepcode, String mainDepcode, String taskDepcode,
			String ifComplete, String completeDesc, String ifCheck,
			String checkStatus, String targetDepcode, String checkDesc,
			String enterpriseCode) {
		this.guideId = guideId;
		this.planTime = planTime;
		this.guideContent = guideContent;
		this.referDepcode = referDepcode;
		this.mainDepcode = mainDepcode;
		this.taskDepcode = taskDepcode;
		this.ifComplete = ifComplete;
		this.completeDesc = completeDesc;
		this.ifCheck = ifCheck;
		this.checkStatus = checkStatus;
		this.targetDepcode = targetDepcode;
		this.checkDesc = checkDesc;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "GUIDE_ID", unique = true, nullable = false, length = 20)
	public Long getGuideId() {
		return this.guideId;
	}

	public void setGuideId(Long guideId) {
		this.guideId = guideId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_TIME", nullable = false, length = 7)
	public Date getPlanTime() {
		return this.planTime;
	}

	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}

	@Column(name = "GUIDE_CONTENT", length = 1000)
	public String getGuideContent() {
		return this.guideContent;
	}

	public void setGuideContent(String guideContent) {
		this.guideContent = guideContent;
	}

	@Column(name = "REFER_DEPCODE", length = 20)
	public String getReferDepcode() {
		return this.referDepcode;
	}

	public void setReferDepcode(String referDepcode) {
		this.referDepcode = referDepcode;
	}

	@Column(name = "MAIN_DEPCODE", length = 20)
	public String getMainDepcode() {
		return this.mainDepcode;
	}

	public void setMainDepcode(String mainDepcode) {
		this.mainDepcode = mainDepcode;
	}

	@Column(name = "TASK_DEPCODE", length = 100)
	public String getTaskDepcode() {
		return this.taskDepcode;
	}

	public void setTaskDepcode(String taskDepcode) {
		this.taskDepcode = taskDepcode;
	}

	@Column(name = "IF_COMPLETE", length = 1)
	public String getIfComplete() {
		return this.ifComplete;
	}

	public void setIfComplete(String ifComplete) {
		this.ifComplete = ifComplete;
	}

	@Column(name = "COMPLETE_DESC", length = 200)
	public String getCompleteDesc() {
		return this.completeDesc;
	}

	public void setCompleteDesc(String completeDesc) {
		this.completeDesc = completeDesc;
	}

	@Column(name = "IF_CHECK", length = 1)
	public String getIfCheck() {
		return this.ifCheck;
	}

	public void setIfCheck(String ifCheck) {
		this.ifCheck = ifCheck;
	}

	@Column(name = "CHECK_STATUS", length = 1)
	public String getCheckStatus() {
		return this.checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	@Column(name = "TARGET_DEPCODE", length = 20)
	public String getTargetDepcode() {
		return this.targetDepcode;
	}

	public void setTargetDepcode(String targetDepcode) {
		this.targetDepcode = targetDepcode;
	}

	@Column(name = "CHECK_DESC", length = 200)
	public String getCheckDesc() {
		return this.checkDesc;
	}

	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}