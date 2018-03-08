package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJStationremove entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_STATIONREMOVE")
public class HrJStationremove implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 1L;
    private Long stationremoveid;
    private Long stationMoveTypeId;
    private String requisitionNo;
    private Date removeDate;
    private Long oldDepId;
    private Long oldStationId;
    private Long newDepId;
    private Long newStationId;
    private Date doDate;
    private String wirteType;
    private String reason;
    private String memo;
    private String insertby;
    private Date insertdate;
    private String dcmState;
    private Date doDate2;
    private String enterpriseCode;
    private String isUse;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private Long empId;
    
    // add by liuyi 20100617 
    private Long oldStationGrade;
    private Long newStationGrade;
    private Long oldSalaryGrade;
    private Long newSalaryGrade;
    private Double oldSalaryPoint;// OLD_SALARY_POINT
    private Double newSalaryPoint;//NEW_SALARY_POINT


    private Long oldBanzu;//add by sychen 20100721
    private Long newBanzu;//add by sychen 20100721
    private Date printDate;//add by sychen 20100723
    
    // Constructors

    /** default constructor */
    public HrJStationremove() {
    }

    /** minimal constructor */
    public HrJStationremove(Long stationremoveid) {
	this.stationremoveid = stationremoveid;
    }

    /** full constructor */
    public HrJStationremove(Long stationremoveid, Long stationMoveTypeId,
	    String requisitionNo, Date removeDate, Long oldDepId,
	    Long oldStationId, Long newDepId, Long newStationId, Date doDate,
	    String wirteType, String reason, String memo, String insertby,
	    Date insertdate, String dcmState, Date doDate2,
	    String enterpriseCode, String isUse, String lastModifiedBy,
	    Date lastModifiedDate, Long empId) {
	this.stationremoveid = stationremoveid;
	this.stationMoveTypeId = stationMoveTypeId;
	this.requisitionNo = requisitionNo;
	this.removeDate = removeDate;
	this.oldDepId = oldDepId;
	this.oldStationId = oldStationId;
	this.newDepId = newDepId;
	this.newStationId = newStationId;
	this.doDate = doDate;
	this.wirteType = wirteType;
	this.reason = reason;
	this.memo = memo;
	this.insertby = insertby;
	this.insertdate = insertdate;
	this.dcmState = dcmState;
	this.doDate2 = doDate2;
	this.enterpriseCode = enterpriseCode;
	this.isUse = isUse;
	this.lastModifiedBy = lastModifiedBy;
	this.lastModifiedDate = lastModifiedDate;
	this.empId = empId;
    }

    // Property accessors
    @Id
    @Column(name = "STATIONREMOVEID", unique = true, nullable = false, precision = 10, scale = 0)
    public Long getStationremoveid() {
	return this.stationremoveid;
    }

    public void setStationremoveid(Long stationremoveid) {
	this.stationremoveid = stationremoveid;
    }

    @Column(name = "STATION_MOVE_TYPE_ID", precision = 10, scale = 0)
    public Long getStationMoveTypeId() {
	return this.stationMoveTypeId;
    }

    public void setStationMoveTypeId(Long stationMoveTypeId) {
	this.stationMoveTypeId = stationMoveTypeId;
    }

    @Column(name = "REQUISITION_NO", length = 40)
    public String getRequisitionNo() {
	return this.requisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
	this.requisitionNo = requisitionNo;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "REMOVE_DATE", length = 7)
    public Date getRemoveDate() {
	return this.removeDate;
    }

    public void setRemoveDate(Date removeDate) {
	this.removeDate = removeDate;
    }

    @Column(name = "OLD_DEP_ID", precision = 10, scale = 0)
    public Long getOldDepId() {
	return this.oldDepId;
    }

    public void setOldDepId(Long oldDepId) {
	this.oldDepId = oldDepId;
    }

    @Column(name = "OLD_STATION_ID", precision = 10, scale = 0)
    public Long getOldStationId() {
	return this.oldStationId;
    }

    public void setOldStationId(Long oldStationId) {
	this.oldStationId = oldStationId;
    }

    @Column(name = "NEW_DEP_ID", precision = 10, scale = 0)
    public Long getNewDepId() {
	return this.newDepId;
    }

    public void setNewDepId(Long newDepId) {
	this.newDepId = newDepId;
    }

    @Column(name = "NEW_STATION_ID", precision = 10, scale = 0)
    public Long getNewStationId() {
	return this.newStationId;
    }

    public void setNewStationId(Long newStationId) {
	this.newStationId = newStationId;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "DO_DATE", length = 7)
    public Date getDoDate() {
	return this.doDate;
    }

    public void setDoDate(Date doDate) {
	this.doDate = doDate;
    }

    @Column(name = "WIRTE_TYPE", length = 1)
    public String getWirteType() {
	return this.wirteType;
    }

    public void setWirteType(String wirteType) {
	this.wirteType = wirteType;
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

    @Column(name = "DCM_STATE", length = 1)
    public String getDcmState() {
	return this.dcmState;
    }

    public void setDcmState(String dcmState) {
	this.dcmState = dcmState;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "DO_DATE2", length = 7)
    public Date getDoDate2() {
	return this.doDate2;
    }

    public void setDoDate2(Date doDate2) {
	this.doDate2 = doDate2;
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

    @Column(name = "EMP_ID", precision = 10, scale = 0)
    public Long getEmpId() {
	return this.empId;
    }

    public void setEmpId(Long empId) {
	this.empId = empId;
    }

    @Column(name = "OLD_STATION_GRADE", precision = 10, scale = 0)
	public Long getOldStationGrade() {
		return oldStationGrade;
	}

	public void setOldStationGrade(Long oldStationGrade) {
		this.oldStationGrade = oldStationGrade;
	}

	@Column(name = "NEW_STATION_GRADE", precision = 10, scale = 0)
	public Long getNewStationGrade() {
		return newStationGrade;
	}

	
	public void setNewStationGrade(Long newStationGrade) {
		this.newStationGrade = newStationGrade;
	}

	@Column(name = "OLD_SALARY_GRADE", precision = 10, scale = 0)
	public Long getOldSalaryGrade() {
		return oldSalaryGrade;
	}

	public void setOldSalaryGrade(Long oldSalaryGrade) {
		this.oldSalaryGrade = oldSalaryGrade;
	}

	@Column(name = "NEW_SALARY_GRADE", precision = 10, scale = 0)
	public Long getNewSalaryGrade() {
		return newSalaryGrade;
	}

	public void setNewSalaryGrade(Long newSalaryGrade) {
		this.newSalaryGrade = newSalaryGrade;
	}

	@Column(name = "OLD_SALARY_POINT", precision = 10, scale = 2)
	public Double getOldSalaryPoint() {
		return oldSalaryPoint;
	}

	public void setOldSalaryPoint(Double oldSalaryPoint) {
		this.oldSalaryPoint = oldSalaryPoint;
	}

	@Column(name = "NEW_SALARY_POINT", precision = 10, scale = 2)
	public Double getNewSalaryPoint() {
		return newSalaryPoint;
	}

	public void setNewSalaryPoint(Double newSalaryPoint) {
		this.newSalaryPoint = newSalaryPoint;
	}

    @Column(name = "OLD_BANZU", precision = 10, scale = 0)
	public Long getOldBanzu() {
		return oldBanzu;
	}

	public void setOldBanzu(Long oldBanzu) {
		this.oldBanzu = oldBanzu;
	}

    @Column(name = "NEW_BANZU", precision = 10, scale = 0)
	public Long getNewBanzu() {
		return newBanzu;
	}

	public void setNewBanzu(Long newBanzu) {
		this.newBanzu = newBanzu;
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