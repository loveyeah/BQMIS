package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJAttendanceApprove entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_ATTENDANCE_APPROVE", schema = "POWER")
public class HrJAttendanceApprove implements java.io.Serializable {

	// Fields

	private Long approveId;
	private String approveMonth;
	private Long attendanceDeptId;
	private String sendState;
	private Long workFlowNo;
	private String reportBy;
	private Date reportDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public HrJAttendanceApprove() {
	}

	/** minimal constructor */
	public HrJAttendanceApprove(Long approveId) {
		this.approveId = approveId;
	}

	/** full constructor */
	public HrJAttendanceApprove(Long approveId, String approveMonth,
			Long attendanceDeptId, String sendState, Long workFlowNo,
			String reportBy, Date reportDate, String enterpriseCode,
			String isUse) {
		this.approveId = approveId;
		this.approveMonth = approveMonth;
		this.attendanceDeptId = attendanceDeptId;
		this.sendState = sendState;
		this.workFlowNo = workFlowNo;
		this.reportBy = reportBy;
		this.reportDate = reportDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "APPROVE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getApproveId() {
		return this.approveId;
	}

	public void setApproveId(Long approveId) {
		this.approveId = approveId;
	}

	@Column(name = "APPROVE_MONTH", length = 10)
	public String getApproveMonth() {
		return this.approveMonth;
	}

	public void setApproveMonth(String approveMonth) {
		this.approveMonth = approveMonth;
	}

	@Column(name = "ATTENDANCE_DEPT_ID", precision = 10, scale = 0)
	public Long getAttendanceDeptId() {
		return this.attendanceDeptId;
	}

	public void setAttendanceDeptId(Long attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}

	@Column(name = "SEND_STATE", length = 1)
	public String getSendState() {
		return this.sendState;
	}

	public void setSendState(String sendState) {
		this.sendState = sendState;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "REPORT_BY", length = 30)
	public String getReportBy() {
		return this.reportBy;
	}

	public void setReportBy(String reportBy) {
		this.reportBy = reportBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE", length = 7)
	public Date getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
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

}