package power.ejb.opticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCOpticketstep entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_OPTICKETSTEP")
public class RunCOpticketstep implements java.io.Serializable {

	// Fields

	private Long operateStepId;
	private Long operateTaskId;
	private String operateStepName;
	private Long displayNo;
	private String isMain;
	private String memo;
	private String enterpriseCode;
	private String isUse;
	private String modifyBy;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public RunCOpticketstep() {
	}

	/** minimal constructor */
	public RunCOpticketstep(Long operateStepId) {
		this.operateStepId = operateStepId;
	}

	/** full constructor */
	public RunCOpticketstep(Long operateStepId, Long operateTaskId,
			String operateStepName, Long displayNo, String isMain, String memo,
			String enterpriseCode, String isUse, String modifyBy,
			Date modifyDate) {
		this.operateStepId = operateStepId;
		this.operateTaskId = operateTaskId;
		this.operateStepName = operateStepName;
		this.displayNo = displayNo;
		this.isMain = isMain;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
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

	@Column(name = "OPERATE_TASK_ID", precision = 10, scale = 0)
	public Long getOperateTaskId() {
		return this.operateTaskId;
	}

	public void setOperateTaskId(Long operateTaskId) {
		this.operateTaskId = operateTaskId;
	}

	@Column(name = "OPERATE_STEP_NAME", length = 500)
	public String getOperateStepName() {
		return this.operateStepName;
	}

	public void setOperateStepName(String operateStepName) {
		this.operateStepName = operateStepName;
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

}