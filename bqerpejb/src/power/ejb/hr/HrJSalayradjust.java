/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJSalayradjust entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_SALAYRADJUST")
public class HrJSalayradjust implements java.io.Serializable {

	// Fields

	private Long salayradjustid;
	private Date doDate;
	private Long oldStationGrade;
	private Long oldCheckStationGrade;
	private Long oldSalaryGrade;
	private Long newStationGrade;
	private Long newCheckStationGrade;
	private Long newSalaryGrade;
	private String adjustType;
	private String stationChangeType;
	private String reason;
	private String memo;
	private String insertby;
	private Date insertdate;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Long stationremoveid;
	private Long empId;
	private String dcmState;

	// Constructors

	/** default constructor */
	public HrJSalayradjust() {
	}

	/** minimal constructor */
	public HrJSalayradjust(Long salayradjustid, Long stationremoveid, Long empId) {
		this.salayradjustid = salayradjustid;
		this.stationremoveid = stationremoveid;
		this.empId = empId;
	}

	/** full constructor */
	public HrJSalayradjust(Long salayradjustid, Date doDate,
			Long oldStationGrade, Long oldCheckStationGrade,
			Long oldSalaryGrade, Long newStationGrade,
			Long newCheckStationGrade, Long newSalaryGrade, String adjustType,
			String stationChangeType, String reason, String memo,
			String insertby, Date insertdate, String enterpriseCode,
			String isUse, String lastModifiedBy, Date lastModifiedDate,
			Long stationremoveid, Long empId, String dcmState) {
		this.salayradjustid = salayradjustid;
		this.doDate = doDate;
		this.oldStationGrade = oldStationGrade;
		this.oldCheckStationGrade = oldCheckStationGrade;
		this.oldSalaryGrade = oldSalaryGrade;
		this.newStationGrade = newStationGrade;
		this.newCheckStationGrade = newCheckStationGrade;
		this.newSalaryGrade = newSalaryGrade;
		this.adjustType = adjustType;
		this.stationChangeType = stationChangeType;
		this.reason = reason;
		this.memo = memo;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.stationremoveid = stationremoveid;
		this.empId = empId;
		this.dcmState = dcmState;
	}

	// Property accessors
	@Id
	@Column(name = "SALAYRADJUSTID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSalayradjustid() {
		return this.salayradjustid;
	}

	public void setSalayradjustid(Long salayradjustid) {
		this.salayradjustid = salayradjustid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DO_DATE", length = 7)
	public Date getDoDate() {
		return this.doDate;
	}

	public void setDoDate(Date doDate) {
		this.doDate = doDate;
	}

	@Column(name = "OLD_STATION_GRADE", precision = 10, scale = 0)
	public Long getOldStationGrade() {
		return this.oldStationGrade;
	}

	public void setOldStationGrade(Long oldStationGrade) {
		this.oldStationGrade = oldStationGrade;
	}

	@Column(name = "OLD_CHECK_STATION_GRADE", precision = 10, scale = 0)
	public Long getOldCheckStationGrade() {
		return this.oldCheckStationGrade;
	}

	public void setOldCheckStationGrade(Long oldCheckStationGrade) {
		this.oldCheckStationGrade = oldCheckStationGrade;
	}

	@Column(name = "OLD_SALARY_GRADE", precision = 10, scale = 0)
	public Long getOldSalaryGrade() {
		return this.oldSalaryGrade;
	}

	public void setOldSalaryGrade(Long oldSalaryGrade) {
		this.oldSalaryGrade = oldSalaryGrade;
	}

	@Column(name = "NEW_STATION_GRADE", precision = 10, scale = 0)
	public Long getNewStationGrade() {
		return this.newStationGrade;
	}

	public void setNewStationGrade(Long newStationGrade) {
		this.newStationGrade = newStationGrade;
	}

	@Column(name = "NEW_CHECK_STATION_GRADE", precision = 10, scale = 0)
	public Long getNewCheckStationGrade() {
		return this.newCheckStationGrade;
	}

	public void setNewCheckStationGrade(Long newCheckStationGrade) {
		this.newCheckStationGrade = newCheckStationGrade;
	}

	@Column(name = "NEW_SALARY_GRADE", precision = 10, scale = 0)
	public Long getNewSalaryGrade() {
		return this.newSalaryGrade;
	}

	public void setNewSalaryGrade(Long newSalaryGrade) {
		this.newSalaryGrade = newSalaryGrade;
	}

	@Column(name = "ADJUST_TYPE", length = 1)
	public String getAdjustType() {
		return this.adjustType;
	}

	public void setAdjustType(String adjustType) {
		this.adjustType = adjustType;
	}

	@Column(name = "STATION_CHANGE_TYPE", length = 1)
	public String getStationChangeType() {
		return this.stationChangeType;
	}

	public void setStationChangeType(String stationChangeType) {
		this.stationChangeType = stationChangeType;
	}

	@Column(name = "REASON", length = 400)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "MEMO")
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

	@Column(name = "ENTERPRISE_CODE", length = 10)
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

	@Column(name = "LAST_MODIFIED_BY", length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "STATIONREMOVEID", nullable = false, precision = 10, scale = 0)
	public Long getStationremoveid() {
		return this.stationremoveid;
	}

	public void setStationremoveid(Long stationremoveid) {
		this.stationremoveid = stationremoveid;
	}

	@Column(name = "EMP_ID", nullable = false, precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "DCM_STATE", length = 1)
	public String getDcmState() {
		return this.dcmState;
	}

	public void setDcmState(String dcmState) {
		this.dcmState = dcmState;
	}

}