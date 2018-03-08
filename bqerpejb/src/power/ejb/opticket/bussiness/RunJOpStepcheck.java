package power.ejb.opticket.bussiness;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunJOpStepcheck entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_OP_STEPCHECK")
public class RunJOpStepcheck implements java.io.Serializable {

	// Fields

	private Long stepCheckId;
	private String opticketCode;
	private String stepCheckName;
	private String runAddFlag;
	private String checkStatus;
	private String checkMan;
	private Long displayNo;
	private String memo;

	// Constructors

	/** default constructor */
	public RunJOpStepcheck() {
	}

	/** minimal constructor */
	public RunJOpStepcheck(Long stepCheckId) {
		this.stepCheckId = stepCheckId;
	}

	/** full constructor */
	public RunJOpStepcheck(Long stepCheckId, String opticketCode,
			String stepCheckName, String runAddFlag, String checkStatus,
			String checkMan, Long displayNo, String memo) {
		this.stepCheckId = stepCheckId;
		this.opticketCode = opticketCode;
		this.stepCheckName = stepCheckName;
		this.runAddFlag = runAddFlag;
		this.checkStatus = checkStatus;
		this.checkMan = checkMan;
		this.displayNo = displayNo;
		this.memo = memo;
	}

	// Property accessors
	@Id
	@Column(name = "STEP_CHECK_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getStepCheckId() {
		return this.stepCheckId;
	}

	public void setStepCheckId(Long stepCheckId) {
		this.stepCheckId = stepCheckId;
	}

	@Column(name = "OPTICKET_CODE", length = 19)
	public String getOpticketCode() {
		return this.opticketCode;
	}

	public void setOpticketCode(String opticketCode) {
		this.opticketCode = opticketCode;
	}

	@Column(name = "STEP_CHECK_NAME", length = 500)
	public String getStepCheckName() {
		return this.stepCheckName;
	}

	public void setStepCheckName(String stepCheckName) {
		this.stepCheckName = stepCheckName;
	}

	@Column(name = "RUN_ADD_FLAG", length = 1)
	public String getRunAddFlag() {
		return this.runAddFlag;
	}

	public void setRunAddFlag(String runAddFlag) {
		this.runAddFlag = runAddFlag;
	}

	@Column(name = "CHECK_STATUS", length = 500)
	public String getCheckStatus() {
		return this.checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
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