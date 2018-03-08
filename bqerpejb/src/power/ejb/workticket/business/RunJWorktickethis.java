package power.ejb.workticket.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJWorktickethis entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_J_WORKTICKETHIS")
public class RunJWorktickethis implements java.io.Serializable {

	// Fields

	private Long id;
	private String workticketNo;
	private String oldChargeBy;
	private String newChargeBy;
	private Date oldApprovedFinishDate;
	private Date newApprovedFinishDate;
	private String reason;
	private String approveBy;
	private Date approveDate;
	private String approveText;
	private String changeStatus;
	private String approveStatus;
	private String dutyChargeBy;//add
	private String fireBy;      //add
	private Long  totalLine;//add TOTAL_LINE
	private Long nobackoutLine;  //add  NOBACKOUT_LINE
    private String nobackoutNum;  // add  NOBACKOUT_NUM
	
	// Constructors

	/** default constructor */
	public RunJWorktickethis() {
	}

	/** minimal constructor */
	public RunJWorktickethis(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RunJWorktickethis(Long id, String workticketNo, String oldChargeBy,
			String newChargeBy, Date oldApprovedFinishDate,
			Date newApprovedFinishDate, String reason, String approveBy,
			Date approveDate, String approveText, String changeStatus,
			String approveStatus,String dutyChargeBy,String fireBy,Long totalLine,
			Long nobackoutLine,String nobackoutNum) {
		this.id = id;
		this.workticketNo = workticketNo;
		this.oldChargeBy = oldChargeBy;
		this.newChargeBy = newChargeBy;
		this.oldApprovedFinishDate = oldApprovedFinishDate;
		this.newApprovedFinishDate = newApprovedFinishDate;
		this.reason = reason;
		this.approveBy = approveBy;
		this.approveDate = approveDate;
		this.approveText = approveText;
		this.changeStatus = changeStatus;
		this.approveStatus = approveStatus;
		this.dutyChargeBy=dutyChargeBy;
		this.fireBy=fireBy;
		this.totalLine=totalLine;
		this.nobackoutLine=nobackoutLine;
		this.nobackoutNum=nobackoutNum;
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

	@Column(name = "WORKTICKET_NO", length = 22)
	public String getWorkticketNo() {
		return this.workticketNo;
	}

	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	@Column(name = "OLD_CHARGE_BY", length = 30)
	public String getOldChargeBy() {
		return this.oldChargeBy;
	}

	public void setOldChargeBy(String oldChargeBy) {
		this.oldChargeBy = oldChargeBy;
	}

	@Column(name = "NEW_CHARGE_BY", length = 30)
	public String getNewChargeBy() {
		return this.newChargeBy;
	}

	public void setNewChargeBy(String newChargeBy) {
		this.newChargeBy = newChargeBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OLD_APPROVED_FINISH_DATE", length = 7)
	public Date getOldApprovedFinishDate() {
		return this.oldApprovedFinishDate;
	}

	public void setOldApprovedFinishDate(Date oldApprovedFinishDate) {
		this.oldApprovedFinishDate = oldApprovedFinishDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NEW_APPROVED_FINISH_DATE", length = 7)
	public Date getNewApprovedFinishDate() {
		return this.newApprovedFinishDate;
	}

	public void setNewApprovedFinishDate(Date newApprovedFinishDate) {
		this.newApprovedFinishDate = newApprovedFinishDate;
	}

	@Column(name = "REASON")
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "APPROVE_BY", length = 30)
	public String getApproveBy() {
		return this.approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVE_DATE", length = 7)
	public Date getApproveDate() {
		return this.approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	@Column(name = "APPROVE_TEXT")
	public String getApproveText() {
		return this.approveText;
	}

	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}

	@Column(name = "CHANGE_STATUS", length = 1)
	public String getChangeStatus() {
		return this.changeStatus;
	}

	public void setChangeStatus(String changeStatus) {
		this.changeStatus = changeStatus;
	}

	@Column(name = "APPROVE_STATUS", length = 30)
	public String getApproveStatus() {
		return this.approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	@Column(name = "DUTY_CHARGE_BY", length = 30)
	public String getDutyChargeBy() {
		return dutyChargeBy;
	}

	public void setDutyChargeBy(String dutyChargeBy) {
		this.dutyChargeBy = dutyChargeBy;
	}
	@Column(name = "FIRE_BY", length = 30)
	public String getFireBy() {
		return fireBy;
	}

	public void setFireBy(String fireBy) {
		this.fireBy = fireBy;
	}
	@Column(name = "TOTAL_LINE", precision = 10, scale = 0)
	public Long getTotalLine() {
		return totalLine;
	}

	public void setTotalLine(Long totalLine) {
		this.totalLine = totalLine;
	}
	@Column(name = "NOBACKOUT_LINE", precision = 10, scale = 0)
	public Long getNobackoutLine() {
		return nobackoutLine;
	}

	public void setNobackoutLine(Long nobackoutLine) {
		this.nobackoutLine = nobackoutLine;
	}
	
	@Column(name = "NOBACKOUT_NUM", length = 30)
	public String getNobackoutNum() {
		return nobackoutNum;
	}

	public void setNobackoutNum(String nobackoutNum) {
		this.nobackoutNum = nobackoutNum;
	}


	

}