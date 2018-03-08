package power.ejb.run.powernotice;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJPowerNoticeApprove entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_POWER_NOTICE_APPROVE")
public class RunJPowerNoticeApprove implements java.io.Serializable {

	// Fields

	private Long approveId;
	private String noticeNo;
//	private Date finishDate;
//	private String electricMonitor;
//	private String validateMonitor;
	private String approveBy;
	private Date approveDate;
	private String approveText;
	private String approveStatus;
	private String enterpriseCode;
	private String  receiveTeam;//add by fyyang 20100805 接受班组

	// Constructors

	/** default constructor */
	public RunJPowerNoticeApprove() {
	}

	/** minimal constructor */
	public RunJPowerNoticeApprove(Long approveId) {
		this.approveId = approveId;
	}

	/** full constructor */
	public RunJPowerNoticeApprove(Long approveId, String noticeNo,
			String approveBy, Date approveDate, String approveText,
			String approveStatus, String enterpriseCode,String  receiveTeam) {
		this.approveId = approveId;
		this.noticeNo = noticeNo;
		this.approveBy = approveBy;
		this.approveDate = approveDate;
		this.approveText = approveText;
		this.approveStatus = approveStatus;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "APPROVE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getApproveId() {
		return this.approveId;
	}

	public void setApproveId(Long approveId) {
		this.approveId = approveId;
	}

	@Column(name = "NOTICE_NO", length = 15)
	public String getNoticeNo() {
		return this.noticeNo;
	}

	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}

	

	@Column(name = "APPROVE_BY", length = 30)
	public String getApproveBy() {
		return this.approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "APPROVE_DATE", length = 7)
	public Date getApproveDate() {
		return this.approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	@Column(name = "APPROVE_TEXT", length = 300)
	public String getApproveText() {
		return this.approveText;
	}

	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}

	@Column(name = "APPROVE_STATUS", length = 30)
	public String getApproveStatus() {
		return this.approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "RECEIVE_TEAM", length = 10)
	public String getReceiveTeam() {
		return receiveTeam;
	}

	public void setReceiveTeam(String receiveTeam) {
		this.receiveTeam = receiveTeam;
	}

}