package power.ejb.opticket;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCOpticketTask entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_OPTICKET_TASK")
public class RunCOpticketTask implements java.io.Serializable {

	// Fields

	private Long operateTaskId;
	private String operateTaskCode;
	private String operateTaskName;
	private Long parentOperateTaskId;
	private String isTask;
	private String operateTaskExplain;
	private Long displayNo;
	private String isMain;
	private String enterpriseCode;
	private String isUse;
	private String modifyBy;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public RunCOpticketTask() {
	}

	/** minimal constructor */
	public RunCOpticketTask(Long operateTaskId) {
		this.operateTaskId = operateTaskId;
	}

	/** full constructor */
	public RunCOpticketTask(Long operateTaskId, String operateTaskCode,
			String operateTaskName, Long parentOperateTaskId, String isTask,
			String operateTaskExplain, Long displayNo, String isMain,
			String enterpriseCode, String isUse,String modifyBy,Date modifyDate) {
		this.operateTaskId = operateTaskId;
		this.operateTaskCode = operateTaskCode;
		this.operateTaskName = operateTaskName;
		this.parentOperateTaskId = parentOperateTaskId;
		this.isTask = isTask;
		this.operateTaskExplain = operateTaskExplain;
		this.displayNo = displayNo;
		this.isMain = isMain;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.modifyBy=modifyBy;
		this.modifyDate=modifyDate;
	}

	// Property accessors
	@Id
	@Column(name = "OPERATE_TASK_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getOperateTaskId() {
		return this.operateTaskId;
	}

	public void setOperateTaskId(Long operateTaskId) {
		this.operateTaskId = operateTaskId;
	}

	@Column(name = "OPERATE_TASK_CODE", length = 30)
	public String getOperateTaskCode() {
		return this.operateTaskCode;
	}

	public void setOperateTaskCode(String operateTaskCode) {
		this.operateTaskCode = operateTaskCode;
	}

	@Column(name = "OPERATE_TASK_NAME")
	public String getOperateTaskName() {
		return this.operateTaskName;
	}

	public void setOperateTaskName(String operateTaskName) {
		this.operateTaskName = operateTaskName;
	}

	@Column(name = "PARENT_OPERATE_TASK_ID", precision = 10, scale = 0)
	public Long getParentOperateTaskId() {
		return this.parentOperateTaskId;
	}

	public void setParentOperateTaskId(Long parentOperateTaskId) {
		this.parentOperateTaskId = parentOperateTaskId;
	}

	@Column(name = "IS_TASK", length = 1)
	public String getIsTask() {
		return this.isTask;
	}

	public void setIsTask(String isTask) {
		this.isTask = isTask;
	}

	@Column(name = "OPERATE_TASK_EXPLAIN")
	public String getOperateTaskExplain() {
		return this.operateTaskExplain;
	}

	public void setOperateTaskExplain(String operateTaskExplain) {
		this.operateTaskExplain = operateTaskExplain;
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
	@Column(name = "modify_by")
	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "modify_date")
	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}