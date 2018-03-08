/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 考勤登记bean
 *
 * @author zhouxu
 */
@Entity
@Table(name = "HR_J_WORKATTENDANCE")
public class HrJWorkattendance implements java.io.Serializable {

	// Fields

	private HrJWorkattendanceId id;
	private Long deptId;
	private Long attendanceDeptId;
	private String amBegingTime;
	private String amEndTime;
	private String pmBegingTime;
	private String pmEndTime;
	private Long workShiftId;
	private Long vacationTypeId;
	private Long overtimeTypeId;
	private String work;
	private String restType;
	private String absentWork;
	private String lateWork;
	private String leaveEarly;
	private String outWork;
	private String evectionType;
	private String memo;
	private String insertby;
	private Date insertdate;
	private String lastModifiyBy;
	private Date lastModifiyDate;
	private String isUse;
	private String enterpriseCode;
	
	// add by liuyi 20100202
	// 加班时间id overtime_time_id
	private Long overtimeTimeId;
	//病假时间Id sick_leave_time_id
	private Long sickLeaveTimeId;
	//事假时间Id event_time_id
	private Long eventTimeId;
	// 旷工时间Id absent_time_id
	private Long absentTimeId;
	// 其他假时间Id other_time_id
	private Long otherTimeId;

	// Constructors
	

	

	/** default constructor */
	public HrJWorkattendance() {
	}

	/** minimal constructor */
	public HrJWorkattendance(HrJWorkattendanceId id) {
		this.id = id;
	}

	/** full constructor */
	public HrJWorkattendance(HrJWorkattendanceId id, Long deptId,
			Long attendanceDeptId, String amBegingTime, String amEndTime,
			String pmBegingTime, String pmEndTime, Long workShiftId,
			Long vacationTypeId, Long overtimeTypeId, String work,
			String restType, String absentWork, String lateWork,
			String leaveEarly, String outWork, String evectionType,
			String memo, String insertby, Date insertdate,
			String lastModifiyBy, Date lastModifiyDate, String isUse,
			String enterpriseCode) {
		this.id = id;
		this.deptId = deptId;
		this.attendanceDeptId = attendanceDeptId;
		this.amBegingTime = amBegingTime;
		this.amEndTime = amEndTime;
		this.pmBegingTime = pmBegingTime;
		this.pmEndTime = pmEndTime;
		this.workShiftId = workShiftId;
		this.vacationTypeId = vacationTypeId;
		this.overtimeTypeId = overtimeTypeId;
		this.work = work;
		this.restType = restType;
		this.absentWork = absentWork;
		this.lateWork = lateWork;
		this.leaveEarly = leaveEarly;
		this.outWork = outWork;
		this.evectionType = evectionType;
		this.memo = memo;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.lastModifiyBy = lastModifiyBy;
		this.lastModifiyDate = lastModifiyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "empId", column = @Column(name = "EMP_ID", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "attendanceDate", column = @Column(name = "ATTENDANCE_DATE", nullable = false, length = 7)) })
	public HrJWorkattendanceId getId() {
		return this.id;
	}

	public void setId(HrJWorkattendanceId id) {
		this.id = id;
	}

	@Column(name = "DEPT_ID", precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Column(name = "ATTENDANCE_DEPT_ID", precision = 10, scale = 0)
	public Long getAttendanceDeptId() {
		return this.attendanceDeptId;
	}

	public void setAttendanceDeptId(Long attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}

	@Column(name = "AM_BEGING_TIME", length = 10)
	public String getAmBegingTime() {
		return this.amBegingTime;
	}

	public void setAmBegingTime(String amBegingTime) {
		this.amBegingTime = amBegingTime;
	}

	@Column(name = "AM_END_TIME", length = 10)
	public String getAmEndTime() {
		return this.amEndTime;
	}

	public void setAmEndTime(String amEndTime) {
		this.amEndTime = amEndTime;
	}

	@Column(name = "PM_BEGING_TIME", length = 10)
	public String getPmBegingTime() {
		return this.pmBegingTime;
	}

	public void setPmBegingTime(String pmBegingTime) {
		this.pmBegingTime = pmBegingTime;
	}

	@Column(name = "PM_END_TIME", length = 10)
	public String getPmEndTime() {
		return this.pmEndTime;
	}

	public void setPmEndTime(String pmEndTime) {
		this.pmEndTime = pmEndTime;
	}

	@Column(name = "WORK_SHIFT_ID", precision = 10, scale = 0)
	public Long getWorkShiftId() {
		return this.workShiftId;
	}

	public void setWorkShiftId(Long workShiftId) {
		this.workShiftId = workShiftId;
	}

	@Column(name = "VACATION_TYPE_ID", precision = 10, scale = 0)
	public Long getVacationTypeId() {
		return this.vacationTypeId;
	}

	public void setVacationTypeId(Long vacationTypeId) {
		this.vacationTypeId = vacationTypeId;
	}

	@Column(name = "OVERTIME_TYPE_ID", precision = 10, scale = 0)
	public Long getOvertimeTypeId() {
		return this.overtimeTypeId;
	}

	public void setOvertimeTypeId(Long overtimeTypeId) {
		this.overtimeTypeId = overtimeTypeId;
	}

	@Column(name = "WORK", length = 1)
	public String getWork() {
		return this.work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	@Column(name = "REST_TYPE", length = 1)
	public String getRestType() {
		return this.restType;
	}

	public void setRestType(String restType) {
		this.restType = restType;
	}

	@Column(name = "ABSENT_WORK", length = 1)
	public String getAbsentWork() {
		return this.absentWork;
	}

	public void setAbsentWork(String absentWork) {
		this.absentWork = absentWork;
	}

	@Column(name = "LATE_WORK", length = 1)
	public String getLateWork() {
		return this.lateWork;
	}

	public void setLateWork(String lateWork) {
		this.lateWork = lateWork;
	}

	@Column(name = "LEAVE_EARLY", length = 1)
	public String getLeaveEarly() {
		return this.leaveEarly;
	}

	public void setLeaveEarly(String leaveEarly) {
		this.leaveEarly = leaveEarly;
	}

	@Column(name = "OUT_WORK", length = 1)
	public String getOutWork() {
		return this.outWork;
	}

	public void setOutWork(String outWork) {
		this.outWork = outWork;
	}

	@Column(name = "EVECTION_TYPE", length = 1)
	public String getEvectionType() {
		return this.evectionType;
	}

	public void setEvectionType(String evectionType) {
		this.evectionType = evectionType;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "INSERTBY", length = 16)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

	@Column(name = "LAST_MODIFIY_BY", length = 16)
	public String getLastModifiyBy() {
		return this.lastModifiyBy;
	}

	public void setLastModifiyBy(String lastModifiyBy) {
		this.lastModifiyBy = lastModifiyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIY_DATE", length = 7)
	public Date getLastModifiyDate() {
		return this.lastModifiyDate;
	}

	public void setLastModifiyDate(Date lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	
	
	@Column(name = "overtime_time_id", precision = 10, scale = 0)
	public Long getOvertimeTimeId() {
		return overtimeTimeId;
	}

	public void setOvertimeTimeId(Long overtimeTimeId) {
		this.overtimeTimeId = overtimeTimeId;
	}

	@Column(name = "sick_leave_time_id", precision = 10, scale = 0)
	public Long getSickLeaveTimeId() {
		return sickLeaveTimeId;
	}

	public void setSickLeaveTimeId(Long sickLeaveTimeId) {
		this.sickLeaveTimeId = sickLeaveTimeId;
	}

	@Column(name = "event_time_id", precision = 10, scale = 0)
	public Long getEventTimeId() {
		return eventTimeId;
	}

	public void setEventTimeId(Long eventTimeId) {
		this.eventTimeId = eventTimeId;
	}

	@Column(name = "absent_time_id", precision = 10, scale = 0)
	public Long getAbsentTimeId() {
		return absentTimeId;
	}

	public void setAbsentTimeId(Long absentTimeId) {
		this.absentTimeId = absentTimeId;
	}
	
	@Column(name = "OTHER_TIME_ID", precision = 10, scale = 0)
	public Long getOtherTimeId() {
		return otherTimeId;
	}

	public void setOtherTimeId(Long otherTimeId) {
		this.otherTimeId = otherTimeId;
	}

}