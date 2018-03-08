package power.ejb.opticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCOpStepcheck entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_OP_STEPCHECK")
public class RunCOpStepcheck implements java.io.Serializable {

	// Fields

	private Long stepCheckId;
	private Long operateTaskId;
	private String stepCheckName;
	private Long displayNo;
	private String memo;
	private String enterpriseCode;
	private String isUse;
	private String modifyBy;
	private Date modifyDate;
	private String checkStatus;

	// Constructors

	/** default constructor */
	public RunCOpStepcheck() {
	}

	/** minimal constructor */
	public RunCOpStepcheck(Long stepCheckId) {
		this.stepCheckId = stepCheckId;
	}

	/** full constructor */
	public RunCOpStepcheck(Long stepCheckId, Long operateTaskId,
			String stepCheckName, Long displayNo, String memo,
			String enterpriseCode, String isUse, String modifyBy,
			Date modifyDate,String checkStatus) {
		this.stepCheckId = stepCheckId;
		this.operateTaskId = operateTaskId;
		this.stepCheckName = stepCheckName;
		this.displayNo = displayNo;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.checkStatus = checkStatus;
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

	@Column(name = "OPERATE_TASK_ID", precision = 10, scale = 0)
	public Long getOperateTaskId() {
		return this.operateTaskId;
	}

	public void setOperateTaskId(Long operateTaskId) {
		this.operateTaskId = operateTaskId;
	}

	@Column(name = "STEP_CHECK_NAME", length = 500)
	public String getStepCheckName() {
		return this.stepCheckName;
	}

	public void setStepCheckName(String stepCheckName) {
		this.stepCheckName = stepCheckName;
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

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	@Column(name = "CHECK_STATUS", length = 500)
	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

}