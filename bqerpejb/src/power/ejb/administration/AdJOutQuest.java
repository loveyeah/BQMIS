package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJOutQuest entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AD_J_OUT_QUEST")
public class AdJOutQuest implements java.io.Serializable {

	// Fields

	private Long id;
	private String applyId;
	private String applyMan;
	private String applyReason;
	private Date outDate;
	private Double outDays;
	private Date inDate;
	private String aim;
	private String depBossCode;
	private String depIdea;
	private Date depSignDate;
	private String xzBossCode;
	private String xzBossIdea;
	private Date xzSignDate;
	private String bigBossCode;
	private String bigBossIdea;
	private Date bigBossSignDate;
	private String dcmStatus;
	private String workFlowNo;
	private String isUse;
	private String updateUser;
	private String applyTopic;
	private String applyText;
	private Date applyDate;
	private String checkedMan;
	private Date updateTime;
	private String appType;
	private String reportId;
	private String ifRead;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdJOutQuest() {
	}

	/** minimal constructor */
	public AdJOutQuest(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJOutQuest(Long id, String applyId, String applyMan,
			String applyReason, Date outDate, Double outDays, Date inDate,
			String aim, String depBossCode, String depIdea, Date depSignDate,
			String xzBossCode, String xzBossIdea, Date xzSignDate,
			String bigBossCode, String bigBossIdea, Date bigBossSignDate,
			String dcmStatus, String workFlowNo, String isUse,
			String updateUser, String applyTopic, String applyText,
			Date applyDate, String checkedMan, Date updateTime, String appType,
			String reportId, String ifRead, String enterpriseCode) {
		this.id = id;
		this.applyId = applyId;
		this.applyMan = applyMan;
		this.applyReason = applyReason;
		this.outDate = outDate;
		this.outDays = outDays;
		this.inDate = inDate;
		this.aim = aim;
		this.depBossCode = depBossCode;
		this.depIdea = depIdea;
		this.depSignDate = depSignDate;
		this.xzBossCode = xzBossCode;
		this.xzBossIdea = xzBossIdea;
		this.xzSignDate = xzSignDate;
		this.bigBossCode = bigBossCode;
		this.bigBossIdea = bigBossIdea;
		this.bigBossSignDate = bigBossSignDate;
		this.dcmStatus = dcmStatus;
		this.workFlowNo = workFlowNo;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.applyTopic = applyTopic;
		this.applyText = applyText;
		this.applyDate = applyDate;
		this.checkedMan = checkedMan;
		this.updateTime = updateTime;
		this.appType = appType;
		this.reportId = reportId;
		this.ifRead = ifRead;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "APPLY_ID", length = 12)
	public String getApplyId() {
		return this.applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	@Column(name = "APPLY_MAN", length = 6)
	public String getApplyMan() {
		return this.applyMan;
	}

	public void setApplyMan(String applyMan) {
		this.applyMan = applyMan;
	}

	@Column(name = "APPLY_REASON", length = 200)
	public String getApplyReason() {
		return this.applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OUT_DATE", length = 7)
	public Date getOutDate() {
		return this.outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}

	@Column(name = "OUT_DAYS", precision = 15, scale = 1)
	public Double getOutDays() {
		return this.outDays;
	}

	public void setOutDays(Double outDays) {
		this.outDays = outDays;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "IN_DATE", length = 7)
	public Date getInDate() {
		return this.inDate;
	}

	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}

	@Column(name = "AIM", length = 100)
	public String getAim() {
		return this.aim;
	}

	public void setAim(String aim) {
		this.aim = aim;
	}

	@Column(name = "DEP_BOSS_CODE", length = 6)
	public String getDepBossCode() {
		return this.depBossCode;
	}

	public void setDepBossCode(String depBossCode) {
		this.depBossCode = depBossCode;
	}

	@Column(name = "DEP_IDEA", length = 100)
	public String getDepIdea() {
		return this.depIdea;
	}

	public void setDepIdea(String depIdea) {
		this.depIdea = depIdea;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DEP_SIGN_DATE", length = 7)
	public Date getDepSignDate() {
		return this.depSignDate;
	}

	public void setDepSignDate(Date depSignDate) {
		this.depSignDate = depSignDate;
	}

	@Column(name = "XZ_BOSS_CODE", length = 6)
	public String getXzBossCode() {
		return this.xzBossCode;
	}

	public void setXzBossCode(String xzBossCode) {
		this.xzBossCode = xzBossCode;
	}

	@Column(name = "XZ_BOSS_IDEA", length = 100)
	public String getXzBossIdea() {
		return this.xzBossIdea;
	}

	public void setXzBossIdea(String xzBossIdea) {
		this.xzBossIdea = xzBossIdea;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "XZ_SIGN_DATE", length = 7)
	public Date getXzSignDate() {
		return this.xzSignDate;
	}

	public void setXzSignDate(Date xzSignDate) {
		this.xzSignDate = xzSignDate;
	}

	@Column(name = "BIG_BOSS_CODE", length = 6)
	public String getBigBossCode() {
		return this.bigBossCode;
	}

	public void setBigBossCode(String bigBossCode) {
		this.bigBossCode = bigBossCode;
	}

	@Column(name = "BIG_BOSS_IDEA", length = 100)
	public String getBigBossIdea() {
		return this.bigBossIdea;
	}

	public void setBigBossIdea(String bigBossIdea) {
		this.bigBossIdea = bigBossIdea;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BIG_BOSS_SIGN_DATE", length = 7)
	public Date getBigBossSignDate() {
		return this.bigBossSignDate;
	}

	public void setBigBossSignDate(Date bigBossSignDate) {
		this.bigBossSignDate = bigBossSignDate;
	}

	@Column(name = "DCM_STATUS", length = 1)
	public String getDcmStatus() {
		return this.dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}

	@Column(name = "WORK_FLOW_NO", length = 26)
	public String getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(String workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Column(name = "APPLY_TOPIC", length = 200)
	public String getApplyTopic() {
		return this.applyTopic;
	}

	public void setApplyTopic(String applyTopic) {
		this.applyTopic = applyTopic;
	}

	@Column(name = "APPLY_TEXT", length = 4000)
	public String getApplyText() {
		return this.applyText;
	}

	public void setApplyText(String applyText) {
		this.applyText = applyText;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "APPLY_DATE", length = 7)
	public Date getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	@Column(name = "CHECKED_MAN", length = 20)
	public String getCheckedMan() {
		return this.checkedMan;
	}

	public void setCheckedMan(String checkedMan) {
		this.checkedMan = checkedMan;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "APP_TYPE", length = 1)
	public String getAppType() {
		return this.appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	@Column(name = "REPORT_ID", length = 40)
	public String getReportId() {
		return this.reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	@Column(name = "IF_READ", length = 1)
	public String getIfRead() {
		return this.ifRead;
	}

	public void setIfRead(String ifRead) {
		this.ifRead = ifRead;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}