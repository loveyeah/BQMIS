package power.ejb.run.runlog;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJRunlogWorker entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_RUNLOG_WORKER", uniqueConstraints = {})
public class RunJRunlogWorker implements java.io.Serializable {

	// Fields

	private Long runlogWorkerId;
	private Long runLogid;
	private String woWorktype;
	private String bookedEmployee;
	private String operateBy;
	private Date operateTime;
	private String operateMemo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunJRunlogWorker() {
	}

	/** minimal constructor */
	public RunJRunlogWorker(Long runlogWorkerId) {
		this.runlogWorkerId = runlogWorkerId;
	}

	/** full constructor */
	public RunJRunlogWorker(Long runlogWorkerId, Long runLogid,
			String woWorktype, String bookedEmployee, String operateBy,
			Date operateTime, String operateMemo, String isUse,
			String enterpriseCode) {
		this.runlogWorkerId = runlogWorkerId;
		this.runLogid = runLogid;
		this.woWorktype = woWorktype;
		this.bookedEmployee = bookedEmployee;
		this.operateBy = operateBy;
		this.operateTime = operateTime;
		this.operateMemo = operateMemo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RUNLOG_WORKER_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getRunlogWorkerId() {
		return this.runlogWorkerId;
	}

	public void setRunlogWorkerId(Long runlogWorkerId) {
		this.runlogWorkerId = runlogWorkerId;
	}

	@Column(name = "RUN_LOGID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getRunLogid() {
		return this.runLogid;
	}

	public void setRunLogid(Long runLogid) {
		this.runLogid = runLogid;
	}

	@Column(name = "WO_WORKTYPE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getWoWorktype() {
		return this.woWorktype;
	}

	public void setWoWorktype(String woWorktype) {
		this.woWorktype = woWorktype;
	}

	@Column(name = "BOOKED_EMPLOYEE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getBookedEmployee() {
		return this.bookedEmployee;
	}

	public void setBookedEmployee(String bookedEmployee) {
		this.bookedEmployee = bookedEmployee;
	}

	@Column(name = "OPERATE_BY", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getOperateBy() {
		return this.operateBy;
	}

	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPERATE_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getOperateTime() {
		return this.operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	@Column(name = "OPERATE_MEMO", unique = false, nullable = true, insertable = true, updatable = true, length = 256)
	public String getOperateMemo() {
		return this.operateMemo;
	}

	public void setOperateMemo(String operateMemo) {
		this.operateMemo = operateMemo;
	}

	@Column(name = "IS_USE", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}