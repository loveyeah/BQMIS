package power.ejb.opticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJOpticketstep entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_OPTICKETSTEP")
public class RunJOpticketstep implements java.io.Serializable {

	// Fields

	private Long operateStepId;
	private String opticketCode;
	private String operateStepName;
	private Date finishTime;
	private String execMan;
	private String execStatus;
	private Long displayNo;
	private String isMain;
	private String memo;
	private String runAddFlag;
	private String proMan;
	// Constructors

	
	/** default constructor */
	public RunJOpticketstep() {
	}

	/** minimal constructor */
	public RunJOpticketstep(Long operateStepId) {
		this.operateStepId = operateStepId;
	}

	/** full constructor */
	public RunJOpticketstep(Long operateStepId,
			String operateStepName, Date finishTime, String execMan,
			String execStatus, Long displayNo, String isMain, String memo,String runAddFlag) {
		this.operateStepId = operateStepId;
		this.operateStepName = operateStepName;
		this.finishTime = finishTime;
		this.execMan = execMan;
		this.execStatus = execStatus;
		this.displayNo = displayNo;
		this.isMain = isMain;
		this.memo = memo;
		this.runAddFlag=runAddFlag;
	}

	// Property accessors
	@Id
	@Column(name = "OPERATE_STEP_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getOperateStepId() {
		return this.operateStepId;
	}

	public void setOperateStepId(Long operateStepId) {
		this.operateStepId = operateStepId;
	}
	
	@Column(name = "OPTICKET_CODE", length = 19)
	public String getOpticketCode() {
		return this.opticketCode;
	}

	public void setOpticketCode(String opticketCode) {
		this.opticketCode = opticketCode;
	}

	@Column(name = "OPERATE_STEP_NAME", length = 500)
	public String getOperateStepName() {
		return this.operateStepName;
	}

	public void setOperateStepName(String operateStepName) {
		this.operateStepName = operateStepName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FINISH_TIME", length = 7)
	public Date getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Column(name = "EXEC_MAN", length = 6)
	public String getExecMan() {
		return this.execMan;
	}

	public void setExecMan(String execMan) {
		this.execMan = execMan;
	}

	@Column(name = "EXEC_STATUS", length = 1)
	public String getExecStatus() {
		return this.execStatus;
	}

	public void setExecStatus(String execStatus) {
		this.execStatus = execStatus;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "IS_MAIN", length = 1)
	public String getIsMain() {
		return this.isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

	@Column(name = "MEMO")
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "RUN_ADD_FLAG")
	public String getRunAddFlag() {
		return runAddFlag;
	}

	public void setRunAddFlag(String runAddFlag) {
		this.runAddFlag = runAddFlag;
	}
	@Column(name = "PRO_MAN")
	public String getProMan() {
		return proMan;
	}

	public void setProMan(String proMan) {
		this.proMan = proMan;
	}


}