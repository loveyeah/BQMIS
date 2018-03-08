package power.ejb.opticket.bussiness;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunJOpFinwork entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_OP_FINWORK")
public class RunJOpFinwork implements java.io.Serializable {

	// Fields

	private Long finishWorkId;
	private String opticketCode;
	private String finishWorkName;
	private String checkStatus;
	private String runAddFlag;
	private String checkMan;
	private Long displayNo;
	private String memo;

	// Constructors

	/** default constructor */
	public RunJOpFinwork() {
	}

	/** minimal constructor */
	public RunJOpFinwork(Long finishWorkId) {
		this.finishWorkId = finishWorkId;
	}

	/** full constructor */
	public RunJOpFinwork(Long finishWorkId, String opticketCode,
			String finishWorkName, String checkStatus, String runAddFlag,
			String checkMan, Long displayNo, String memo) {
		this.finishWorkId = finishWorkId;
		this.opticketCode = opticketCode;
		this.finishWorkName = finishWorkName;
		this.checkStatus = checkStatus;
		this.runAddFlag = runAddFlag;
		this.checkMan = checkMan;
		this.displayNo = displayNo;
		this.memo = memo;
	}

	// Property accessors
	@Id
	@Column(name = "FINISH_WORK_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFinishWorkId() {
		return this.finishWorkId;
	}

	public void setFinishWorkId(Long finishWorkId) {
		this.finishWorkId = finishWorkId;
	}

	@Column(name = "OPTICKET_CODE", length = 19)
	public String getOpticketCode() {
		return this.opticketCode;
	}

	public void setOpticketCode(String opticketCode) {
		this.opticketCode = opticketCode;
	}

	@Column(name = "FINISH_WORK_NAME", length = 500)
	public String getFinishWorkName() {
		return this.finishWorkName;
	}

	public void setFinishWorkName(String finishWorkName) {
		this.finishWorkName = finishWorkName;
	}

	@Column(name = "CHECK_STATUS", length = 500)
	public String getCheckStatus() {
		return this.checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	@Column(name = "RUN_ADD_FLAG", length = 1)
	public String getRunAddFlag() {
		return this.runAddFlag;
	}

	public void setRunAddFlag(String runAddFlag) {
		this.runAddFlag = runAddFlag;
	}

	@Column(name = "CHECK_MAN", length = 30)
	public String getCheckMan() {
		return this.checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "MEMO")
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}