package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJReception entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_RECEPTION")
public class AdJReception implements java.io.Serializable {

	// Fields

	private Long id;
	private String applyId;
	private String applyMan;
	private Date logDate;
	private Date meetDate;
	private String meetNote;
	private Long repastNum;
	private Double repastBz;
	private String repastPlan;
	private Long roomNum;
	private Double roomBz;
	private String roomPlan;
	private Double payoutBz;
	private Double payout;
	private Double balance;
	private String other;
	private String workFlowNo;
	private String isUse;
	private String dcmStatus;
	private String updateUser;
	private Date updateTime;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdJReception() {
	}

	/** minimal constructor */
	public AdJReception(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJReception(Long id, String applyId, String applyMan, Date logDate,
			Date meetDate, String meetNote, Long repastNum, Double repastBz,
			String repastPlan, Long roomNum, Double roomBz, String roomPlan,
			Double payoutBz, Double payout, Double balance, String other,
			String workFlowNo, String isUse, String dcmStatus,
			String updateUser, Date updateTime, String enterpriseCode) {
		this.id = id;
		this.applyId = applyId;
		this.applyMan = applyMan;
		this.logDate = logDate;
		this.meetDate = meetDate;
		this.meetNote = meetNote;
		this.repastNum = repastNum;
		this.repastBz = repastBz;
		this.repastPlan = repastPlan;
		this.roomNum = roomNum;
		this.roomBz = roomBz;
		this.roomPlan = roomPlan;
		this.payoutBz = payoutBz;
		this.payout = payout;
		this.balance = balance;
		this.other = other;
		this.workFlowNo = workFlowNo;
		this.isUse = isUse;
		this.dcmStatus = dcmStatus;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "LOG_DATE", length = 7)
	public Date getLogDate() {
		return this.logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MEET_DATE", length = 7)
	public Date getMeetDate() {
		return this.meetDate;
	}

	public void setMeetDate(Date meetDate) {
		this.meetDate = meetDate;
	}

	@Column(name = "MEET_NOTE", length = 60)
	public String getMeetNote() {
		return this.meetNote;
	}

	public void setMeetNote(String meetNote) {
		this.meetNote = meetNote;
	}

	@Column(name = "REPAST_NUM", precision = 4, scale = 0)
	public Long getRepastNum() {
		return this.repastNum;
	}

	public void setRepastNum(Long repastNum) {
		this.repastNum = repastNum;
	}

	@Column(name = "REPAST_BZ", precision = 8)
	public Double getRepastBz() {
		return this.repastBz;
	}

	public void setRepastBz(Double repastBz) {
		this.repastBz = repastBz;
	}

	@Column(name = "REPAST_PLAN", length = 40)
	public String getRepastPlan() {
		return this.repastPlan;
	}

	public void setRepastPlan(String repastPlan) {
		this.repastPlan = repastPlan;
	}

	@Column(name = "ROOM_NUM", precision = 4, scale = 0)
	public Long getRoomNum() {
		return this.roomNum;
	}

	public void setRoomNum(Long roomNum) {
		this.roomNum = roomNum;
	}

	@Column(name = "ROOM_BZ", precision = 8)
	public Double getRoomBz() {
		return this.roomBz;
	}

	public void setRoomBz(Double roomBz) {
		this.roomBz = roomBz;
	}

	@Column(name = "ROOM_PLAN", length = 40)
	public String getRoomPlan() {
		return this.roomPlan;
	}

	public void setRoomPlan(String roomPlan) {
		this.roomPlan = roomPlan;
	}

	@Column(name = "PAYOUT_BZ", precision = 13)
	public Double getPayoutBz() {
		return this.payoutBz;
	}

	public void setPayoutBz(Double payoutBz) {
		this.payoutBz = payoutBz;
	}

	@Column(name = "PAYOUT", precision = 13)
	public Double getPayout() {
		return this.payout;
	}

	public void setPayout(Double payout) {
		this.payout = payout;
	}

	@Column(name = "BALANCE", precision = 13)
	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Column(name = "OTHER", length = 200)
	public String getOther() {
		return this.other;
	}

	public void setOther(String other) {
		this.other = other;
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

	@Column(name = "DCM_STATUS", length = 1)
	public String getDcmStatus() {
		return this.dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}

	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}