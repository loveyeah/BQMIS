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
 * HrJWorkattendancenew entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_WORKATTENDANCENEW")
public class HrJWorkattendancenew implements java.io.Serializable {

	// Fields

	private HrJWorkattendancenewId id;
	private Long deptId;
	private Long attendanceDeptId;
	private String work;
	private Long workShiftId;
	private Long overtimeTypeId;
	private Long overtimeTimeId;
	private Long absentTimeId;
	private Long vacationTypeId;
	private Long otherTimeId;
	private String changeRest;
	private String yearRest;
	private String evectionType;
	private String outWork;
	private String memo;
	private String insertby;
	private Date insertdate;
	private String lastModifiyBy;
	private Date lastModifiyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJWorkattendancenew() {
	}

	/** minimal constructor */
	public HrJWorkattendancenew(HrJWorkattendancenewId id) {
		this.id = id;
	}

	/** full constructor */
	public HrJWorkattendancenew(HrJWorkattendancenewId id, Long deptId,
			Long attendanceDeptId, String work, Long workShiftId,
			Long overtimeTypeId, Long overtimeTimeId, Long absentTimeId,
			Long vacationTypeId, Long otherTimeId, String changeRest,
			String yearRest, String evectionType, String outWork, String memo,
			String insertby, Date insertdate, String lastModifiyBy,
			Date lastModifiyDate, String isUse, String enterpriseCode) {
		this.id = id;
		this.deptId = deptId;
		this.attendanceDeptId = attendanceDeptId;
		this.work = work;
		this.workShiftId = workShiftId;
		this.overtimeTypeId = overtimeTypeId;
		this.overtimeTimeId = overtimeTimeId;
		this.absentTimeId = absentTimeId;
		this.vacationTypeId = vacationTypeId;
		this.otherTimeId = otherTimeId;
		this.changeRest = changeRest;
		this.yearRest = yearRest;
		this.evectionType = evectionType;
		this.outWork = outWork;
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
	public HrJWorkattendancenewId getId() {
		return this.id;
	}

	public void setId(HrJWorkattendancenewId id) {
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

	@Column(name = "WORK", length = 1)
	public String getWork() {
		return this.work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	@Column(name = "WORK_SHIFT_ID", precision = 10, scale = 0)
	public Long getWorkShiftId() {
		return this.workShiftId;
	}

	public void setWorkShiftId(Long workShiftId) {
		this.workShiftId = workShiftId;
	}

	@Column(name = "OVERTIME_TYPE_ID", precision = 10, scale = 0)
	public Long getOvertimeTypeId() {
		return this.overtimeTypeId;
	}

	public void setOvertimeTypeId(Long overtimeTypeId) {
		this.overtimeTypeId = overtimeTypeId;
	}

	@Column(name = "OVERTIME_TIME_ID", precision = 10, scale = 0)
	public Long getOvertimeTimeId() {
		return this.overtimeTimeId;
	}

	public void setOvertimeTimeId(Long overtimeTimeId) {
		this.overtimeTimeId = overtimeTimeId;
	}

	@Column(name = "ABSENT_TIME_ID", precision = 10, scale = 0)
	public Long getAbsentTimeId() {
		return this.absentTimeId;
	}

	public void setAbsentTimeId(Long absentTimeId) {
		this.absentTimeId = absentTimeId;
	}

	@Column(name = "VACATION_TYPE_ID", precision = 10, scale = 0)
	public Long getVacationTypeId() {
		return this.vacationTypeId;
	}

	public void setVacationTypeId(Long vacationTypeId) {
		this.vacationTypeId = vacationTypeId;
	}

	@Column(name = "OTHER_TIME_ID", precision = 10, scale = 0)
	public Long getOtherTimeId() {
		return this.otherTimeId;
	}

	public void setOtherTimeId(Long otherTimeId) {
		this.otherTimeId = otherTimeId;
	}

	@Column(name = "CHANGE_REST", length = 1)
	public String getChangeRest() {
		return this.changeRest;
	}

	public void setChangeRest(String changeRest) {
		this.changeRest = changeRest;
	}

	@Column(name = "YEAR_REST", length = 1)
	public String getYearRest() {
		return this.yearRest;
	}

	public void setYearRest(String yearRest) {
		this.yearRest = yearRest;
	}

	@Column(name = "EVECTION_TYPE", length = 1)
	public String getEvectionType() {
		return this.evectionType;
	}

	public void setEvectionType(String evectionType) {
		this.evectionType = evectionType;
	}

	@Column(name = "OUT_WORK", length = 1)
	public String getOutWork() {
		return this.outWork;
	}

	public void setOutWork(String outWork) {
		this.outWork = outWork;
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

	@Temporal(TemporalType.DATE)
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

	@Temporal(TemporalType.DATE)
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

}