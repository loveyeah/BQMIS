package power.ejb.manage.plan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJPlanGuideNew entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_PLAN_GUIDE_NEW")
public class BpJPlanGuideNew implements java.io.Serializable {

	// Fields

	private String guideId;
	private Date planTime;
	private String guideContent;
	private String referDepcode;
	private String mainDepcode;
	private String otherDepcode;
	private Long ifComplete;
	private String completeDesc;
	private String editBy;
	private Date editDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJPlanGuideNew() {
	}

	/** minimal constructor */
	public BpJPlanGuideNew(String guideId) {
		this.guideId = guideId;
	}

	/** full constructor */
	public BpJPlanGuideNew(String guideId, Date planTime, String guideContent,
			String referDepcode, String mainDepcode, String otherDepcode,
			Long ifComplete, String completeDesc, String editBy, Date editDate,
			String enterpriseCode) {
		this.guideId = guideId;
		this.planTime = planTime;
		this.guideContent = guideContent;
		this.referDepcode = referDepcode;
		this.mainDepcode = mainDepcode;
		this.otherDepcode = otherDepcode;
		this.ifComplete = ifComplete;
		this.completeDesc = completeDesc;
		this.editBy = editBy;
		this.editDate = editDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "GUIDE_ID", unique = true, nullable = false, length = 20)
	public String getGuideId() {
		return this.guideId;
	}

	public void setGuideId(String guideId) {
		this.guideId = guideId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_TIME", length = 7)
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

	@Column(name = "OTHER_DEPCODE", length = 100)
	public String getOtherDepcode() {
		return this.otherDepcode;
	}

	public void setOtherDepcode(String otherDepcode) {
		this.otherDepcode = otherDepcode;
	}

	@Column(name = "IF_COMPLETE", precision = 1, scale = 0)
	public Long getIfComplete() {
		return this.ifComplete;
	}

	public void setIfComplete(Long ifComplete) {
		this.ifComplete = ifComplete;
	}

	@Column(name = "COMPLETE_DESC", length = 500)
	public String getCompleteDesc() {
		return this.completeDesc;
	}

	public void setCompleteDesc(String completeDesc) {
		this.completeDesc = completeDesc;
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