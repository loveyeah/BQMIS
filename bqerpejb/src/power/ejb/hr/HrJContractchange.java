package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJContractchange entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_CONTRACTCHANGE")
public class HrJContractchange implements java.io.Serializable {

	// Fields

	private Long contractchangeid;
	private Long workcontractid;
	private Date chargeDate;
	private Long oldContractCode;
	private Long newContractCode;
	private Date newStateTime;
	private Date oldStateTime;
	private Date oldEndTime;
	private Date newEndTime;
	private Long oldDepCode;
	private Long newDepCode;
	private Long oldStationCode;
	private Long newStationCode;
	private String changeReason;
	private String memo;
	private String insertby;
	private Date insertdate;
	private String enterpriseCode;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Long empId2;
	private String isUse;

	// Constructors

	/** default constructor */
	public HrJContractchange() {
	}

	/** minimal constructor */
	public HrJContractchange(Long contractchangeid, Long empId2) {
		this.contractchangeid = contractchangeid;
		this.empId2 = empId2;
	}

	/** full constructor */
	public HrJContractchange(Long contractchangeid, Long workcontractid,
			Date chargeDate, Long oldContractCode, Long newContractCode,
			Date newStateTime, Date oldStateTime, Date oldEndTime,
			Date newEndTime, Long oldDepCode, Long newDepCode,
			Long oldStationCode, Long newStationCode, String changeReason,
			String memo, String insertby, Date insertdate,
			String enterpriseCode, String lastModifiedBy,
			Date lastModifiedDate, Long empId2, String isUse) {
		this.contractchangeid = contractchangeid;
		this.workcontractid = workcontractid;
		this.chargeDate = chargeDate;
		this.oldContractCode = oldContractCode;
		this.newContractCode = newContractCode;
		this.newStateTime = newStateTime;
		this.oldStateTime = oldStateTime;
		this.oldEndTime = oldEndTime;
		this.newEndTime = newEndTime;
		this.oldDepCode = oldDepCode;
		this.newDepCode = newDepCode;
		this.oldStationCode = oldStationCode;
		this.newStationCode = newStationCode;
		this.changeReason = changeReason;
		this.memo = memo;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.enterpriseCode = enterpriseCode;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.empId2 = empId2;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "CONTRACTCHANGEID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getContractchangeid() {
		return this.contractchangeid;
	}

	public void setContractchangeid(Long contractchangeid) {
		this.contractchangeid = contractchangeid;
	}

	@Column(name = "WORKCONTRACTID", precision = 10, scale = 0)
	public Long getWorkcontractid() {
		return this.workcontractid;
	}

	public void setWorkcontractid(Long workcontractid) {
		this.workcontractid = workcontractid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHARGE_DATE", length = 7)
	public Date getChargeDate() {
		return this.chargeDate;
	}

	public void setChargeDate(Date chargeDate) {
		this.chargeDate = chargeDate;
	}

	@Column(name = "OLD_CONTRACT_CODE", precision = 10, scale = 0)
	public Long getOldContractCode() {
		return this.oldContractCode;
	}

	public void setOldContractCode(Long oldContractCode) {
		this.oldContractCode = oldContractCode;
	}

	@Column(name = "NEW_CONTRACT_CODE", precision = 10, scale = 0)
	public Long getNewContractCode() {
		return this.newContractCode;
	}

	public void setNewContractCode(Long newContractCode) {
		this.newContractCode = newContractCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NEW_STATE_TIME", length = 7)
	public Date getNewStateTime() {
		return this.newStateTime;
	}

	public void setNewStateTime(Date newStateTime) {
		this.newStateTime = newStateTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OLD_STATE_TIME", length = 7)
	public Date getOldStateTime() {
		return this.oldStateTime;
	}

	public void setOldStateTime(Date oldStateTime) {
		this.oldStateTime = oldStateTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OLD_END_TIME", length = 7)
	public Date getOldEndTime() {
		return this.oldEndTime;
	}

	public void setOldEndTime(Date oldEndTime) {
		this.oldEndTime = oldEndTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "NEW_END_TIME", length = 7)
	public Date getNewEndTime() {
		return this.newEndTime;
	}

	public void setNewEndTime(Date newEndTime) {
		this.newEndTime = newEndTime;
	}

	@Column(name = "OLD_DEP_CODE", precision = 10, scale = 0)
	public Long getOldDepCode() {
		return this.oldDepCode;
	}

	public void setOldDepCode(Long oldDepCode) {
		this.oldDepCode = oldDepCode;
	}

	@Column(name = "NEW_DEP_CODE", precision = 10, scale = 0)
	public Long getNewDepCode() {
		return this.newDepCode;
	}

	public void setNewDepCode(Long newDepCode) {
		this.newDepCode = newDepCode;
	}

	@Column(name = "OLD_STATION_CODE", precision = 10, scale = 0)
	public Long getOldStationCode() {
		return this.oldStationCode;
	}

	public void setOldStationCode(Long oldStationCode) {
		this.oldStationCode = oldStationCode;
	}

	@Column(name = "NEW_STATION_CODE", precision = 10, scale = 0)
	public Long getNewStationCode() {
		return this.newStationCode;
	}

	public void setNewStationCode(Long newStationCode) {
		this.newStationCode = newStationCode;
	}

	@Column(name = "CHANGE_REASON")
	public String getChangeReason() {
		return this.changeReason;
	}

	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
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

	@Column(name = "EMP_ID2", nullable = false, precision = 10, scale = 0)
	public Long getEmpId2() {
		return this.empId2;
	}

	public void setEmpId2(Long empId2) {
		this.empId2 = empId2;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}