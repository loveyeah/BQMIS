package power.ejb.run.powernotice;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCPowerNotice entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_POWER_NOTICE")
public class RunCPowerNotice implements java.io.Serializable {

	// Fields

	private Long noticeId;
	private String noticeNo;
	private String contactDept;
	private String contactMonitor;
	private Date contactDate;
	private String contactContent;
	private String memo;
	private Date modifyDate;
	private String modifyBy;
	private String enterpriseCode;
	private String isUse;
	private Long workFlowNo;
	private Long wfState;
	private Long busiState;
	private String noticeSort; //NOTICE_SORT

	// Constructors

	/** default constructor */
	public RunCPowerNotice() {
	}

	/** minimal constructor */
	public RunCPowerNotice(Long noticeId) {
		this.noticeId = noticeId;
	}

	/** full constructor */
	public RunCPowerNotice(Long noticeId, String noticeNo, String contactDept,
			String contactMonitor, Date contactDate, String contactContent,
			String memo, Date modifyDate, String modifyBy,
			String enterpriseCode, String isUse, Long workFlowNo, Long wfState,Long busiState,String noticeSort) {
		this.noticeId = noticeId;
		this.noticeNo = noticeNo;
		this.contactDept = contactDept;
		this.contactMonitor = contactMonitor;
		this.contactDate = contactDate;
		this.contactContent = contactContent;
		this.memo = memo;
		this.modifyDate = modifyDate;
		this.modifyBy = modifyBy;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.workFlowNo = workFlowNo;
		this.wfState = wfState;
		this.busiState=busiState;
	}

	// Property accessors
	@Id
	@Column(name = "NOTICE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getNoticeId() {
		return this.noticeId;
	}

	public void setNoticeId(Long noticeId) {
		this.noticeId = noticeId;
	}

	@Column(name = "NOTICE_NO", length = 15)
	public String getNoticeNo() {
		return this.noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	@Column(name = "CONTACT_DEPT", length = 20)
	public String getContactDept() {
		return this.contactDept;
	}

	public void setContactDept(String contactDept) {
		this.contactDept = contactDept;
	}

	@Column(name = "CONTACT_MONITOR", length = 30)
	public String getContactMonitor() {
		return this.contactMonitor;
	}

	public void setContactMonitor(String contactMonitor) {
		this.contactMonitor = contactMonitor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONTACT_DATE", length = 7)
	public Date getContactDate() {
		return this.contactDate;
	}

	public void setContactDate(Date contactDate) {
		this.contactDate = contactDate;
	}

	@Column(name = "CONTACT_CONTENT", length = 2000)
	public String getContactContent() {
		return this.contactContent;
	}

	public void setContactContent(String contactContent) {
		this.contactContent = contactContent;
	}

	@Column(name = "MEMO", length = 300)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
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

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "WF_STATE", precision = 11, scale = 0)
	public Long getWfState() {
		return this.wfState;
	}

	public void setWfState(Long wfState) {
		this.wfState = wfState;
	}
	
	@Column(name = "BUSI_STATE", precision = 10, scale = 0)
	public Long getBusiState() {
		return busiState;
	}

	public void setBusiState(Long busiState) {
		this.busiState = busiState;
	}

	@Column(name = "NOTICE_SORT", length = 1)
	public String getNoticeSort() {
		return noticeSort;
	}

	public void setNoticeSort(String noticeSort) {
		this.noticeSort = noticeSort;
	}
	

	

}