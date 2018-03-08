package power.ejb.opticket;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

 
/**
 * 操作票主记录
 * @author wzhyan  
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_J_OPTICKET")
public class RunJOpticket implements java.io.Serializable {

	// Fields

	private String opticketCode;
	private String opticketName;
	private String isStandar;
	private String opticketType;
	private Long operateTaskId;
	private String appendixAddr;
	private String operateTaskName;
	private String opticketStatus;
	private Date planStartTime;
	private Date planEndTime;
	private Date startTime;
	private Date endTime;
	private String specialityCode;
	private String workTicketNo;
	private String createBy;
	private Date createDate;
	private String memo;
	private Long workFlowNo;
	private String enterpriseCode;
	private String isUse;
	private String operatorMan;
	private String protectorMan;
	private String classLeader;
	private String chargeBy;
	private String isSingle;
	private String applyNo;

	// Constructors

	

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	/** default constructor */
	public RunJOpticket() {
	}

	/** minimal constructor */
	public RunJOpticket(String opticketCode) {
		this.opticketCode = opticketCode;
	} 

	// Property accessors
	@Id
	@Column(name = "OPTICKET_CODE", unique = true, nullable = false, length = 19)
	public String getOpticketCode() {
		return this.opticketCode;
	}

	public void setOpticketCode(String opticketCode) {
		this.opticketCode = opticketCode;
	}

	@Column(name = "OPTICKET_NAME")
	public String getOpticketName() {
		return this.opticketName;
	}

	public void setOpticketName(String opticketName) {
		this.opticketName = opticketName;
	}

	@Column(name = "IS_STANDAR", length = 1)
	public String getIsStandar() {
		return this.isStandar;
	}

	public void setIsStandar(String isStandar) {
		this.isStandar = isStandar;
	}

	@Column(name = "OPTICKET_TYPE", length = 4)
	public String getOpticketType() {
		return this.opticketType;
	}

	public void setOpticketType(String opticketType) {
		this.opticketType = opticketType;
	}

	@Column(name = "OPERATE_TASK_ID", precision = 10, scale = 0)
	public Long getOperateTaskId() {
		return this.operateTaskId;
	}

	public void setOperateTaskId(Long operateTaskId) {
		this.operateTaskId = operateTaskId;
	} 
	@Column(name = "APPENDIX_ADDR" ,length = 100)
	public String getAppendixAddr() {
		return appendixAddr;
	}

	public void setAppendixAddr(String appendixAddr) {
		this.appendixAddr = appendixAddr;
	}

	@Column(name = "OPERATE_TASK_NAME")
	public String getOperateTaskName() {
		return this.operateTaskName;
	}

	public void setOperateTaskName(String operateTaskName) {
		this.operateTaskName = operateTaskName;
	}

	@Column(name = "OPTICKET_STATUS", length = 1)
	public String getOpticketStatus() {
		return this.opticketStatus;
	}

	public void setOpticketStatus(String opticketStatus) {
		this.opticketStatus = opticketStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PLAN_START_TIME", length = 7)
	public Date getPlanStartTime() {
		return this.planStartTime;
	}

	public void setPlanStartTime(Date planStartTime) {
		this.planStartTime = planStartTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PLAN_END_TIME", length = 7)
	public Date getPlanEndTime() {
		return this.planEndTime;
	}

	public void setPlanEndTime(Date planEndTime) {
		this.planEndTime = planEndTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", length = 7)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME", length = 7)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "SPECIALITY_CODE", length = 20)
	public String getSpecialityCode() {
		return this.specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	@Column(name = "WORK_TICKET_NO", length = 22)
	public String getWorkTicketNo() {
		return this.workTicketNo;
	}

	public void setWorkTicketNo(String workTicketNo) {
		this.workTicketNo = workTicketNo;
	}

	@Column(name = "CREATE_BY", length = 30)
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "MEMO")
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
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

	@Column(name = "OPERATORMANS")
	public String getOperatorMan() {
		return operatorMan;
	}

	public void setOperatorMan(String operatorMan) {
		this.operatorMan = operatorMan;
	}

	@Column(name = "PROTECTORMANS")
	public String getProtectorMan() {
		return protectorMan;
	} 
	public void setProtectorMan(String protectorMan) {
		this.protectorMan = protectorMan;
	}
	@Column(name = "CLASS_LEADER")
	public String getClassLeader() {
		return classLeader;
	}

	public void setClassLeader(String classLeader) {
		this.classLeader = classLeader;
	}

	@Column(name = "CHARGE_BY")
	public String getChargeBy() {
		return chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}
	
	@Column(name = "IS_SINGLE")
	public String getIsSingle() {
		return isSingle;
	}

	public void setIsSingle(String isSingle) {
		this.isSingle = isSingle;
	}

}