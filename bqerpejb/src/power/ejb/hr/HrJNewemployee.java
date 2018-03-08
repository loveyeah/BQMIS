package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJNewemployee entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_J_NEWEMPLOYEE")
public class HrJNewemployee implements java.io.Serializable {

	// Fields

	private Long newEmpid;
	private Long empId;
	private String advicenoteNo;
	private Long newDeptid;
	private Long checkStationGrade;
	private Long stationId;
	private Double salaryPoint;
	private Date missionDate;
	private Date startsalaryDate;
	private String memo;
	private String isUse;
	private String enterpriseCode;
	private Date registerDate;
	private String empType;
	private String empTime;
	private Date printDate;

	// Constructors

	/** default constructor */
	public HrJNewemployee() {
	}

	/** minimal constructor */
	public HrJNewemployee(Long newEmpid) {
		this.newEmpid = newEmpid;
	}

	/** full constructor */
	public HrJNewemployee(Long newEmpid, Long empId, String advicenoteNo,
			Long newDeptid, Long checkStationGrade, Long stationId,
			Double salaryPoint, Date missionDate, Date startsalaryDate,
			String memo, String isUse, String enterpriseCode) {
		this.newEmpid = newEmpid;
		this.empId = empId;
		this.advicenoteNo = advicenoteNo;
		this.newDeptid = newDeptid;
		this.checkStationGrade = checkStationGrade;
		this.stationId = stationId;
		this.salaryPoint = salaryPoint;
		this.missionDate = missionDate;
		this.startsalaryDate = startsalaryDate;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "NEW_EMPID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getNewEmpid() {
		return this.newEmpid;
	}

	public void setNewEmpid(Long newEmpid) {
		this.newEmpid = newEmpid;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "ADVICENOTE_NO", length = 20)
	public String getAdvicenoteNo() {
		return this.advicenoteNo;
	}

	public void setAdvicenoteNo(String advicenoteNo) {
		this.advicenoteNo = advicenoteNo;
	}

	@Column(name = "NEW_DEPTID", precision = 10, scale = 0)
	public Long getNewDeptid() {
		return this.newDeptid;
	}

	public void setNewDeptid(Long newDeptid) {
		this.newDeptid = newDeptid;
	}

	@Column(name = "CHECK_STATION_GRADE", precision = 10, scale = 0)
	public Long getCheckStationGrade() {
		return this.checkStationGrade;
	}

	public void setCheckStationGrade(Long checkStationGrade) {
		this.checkStationGrade = checkStationGrade;
	}

	@Column(name = "STATION_ID", precision = 10, scale = 0)
	public Long getStationId() {
		return this.stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	@Column(name = "SALARY_POINT", precision = 10)
	public Double getSalaryPoint() {
		return this.salaryPoint;
	}

	public void setSalaryPoint(Double salaryPoint) {
		this.salaryPoint = salaryPoint;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MISSION_DATE", length = 7)
	public Date getMissionDate() {
		return this.missionDate;
	}

	public void setMissionDate(Date missionDate) {
		this.missionDate = missionDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "STARTSALARY_DATE", length = 7)
	public Date getStartsalaryDate() {
		return this.startsalaryDate;
	}

	public void setStartsalaryDate(Date startsalaryDate) {
		this.startsalaryDate = startsalaryDate;
	}

	@Column(name = "MEMO", length = 300)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "REGISTER_DATE", length = 7)
	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}


	@Column(name = "EMP_TYPE", length = 20)
	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}


	@Column(name = "EMP_TIME", length = 20)
	public String getEmpTime() {
		return empTime;
	}

	public void setEmpTime(String empTime) {
		this.empTime = empTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PRINT_DATE", length = 7)
	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	
	

}