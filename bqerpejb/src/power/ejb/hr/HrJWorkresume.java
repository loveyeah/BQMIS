package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJWorkresume entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_WORKRESUME")
public class HrJWorkresume implements java.io.Serializable {

    // Fields

    private Long workresumeid;
    private Date startDate;
    private Date endDate;
    private String unit;
    private String stationName;
    private String headshipName;
    private String witness;
    private String memo;
    private String insertby;
    private Date insertdate;
    private String enterpriseCode;
    private String isUse;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private Long empId;
    
    private String deptName;

    // Constructors

    
	/** default constructor */
    public HrJWorkresume() {
    }

    /** minimal constructor */
    public HrJWorkresume(Long workresumeid) {
        this.workresumeid = workresumeid;
    }

    /** full constructor */
    public HrJWorkresume(Long workresumeid, Date startDate, Date endDate, String unit, String stationName,
            String headshipName, String witness, String memo, String insertby, Date insertdate, String enterpriseCode,
            String isUse, String lastModifiedBy, Date lastModifiedDate, Long empId) {
        this.workresumeid = workresumeid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.unit = unit;
        this.stationName = stationName;
        this.headshipName = headshipName;
        this.witness = witness;
        this.memo = memo;
        this.insertby = insertby;
        this.insertdate = insertdate;
        this.enterpriseCode = enterpriseCode;
        this.isUse = isUse;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.empId = empId;
    }

    // Property accessors
    @Id
    @Column(name = "WORKRESUMEID", unique = true, nullable = false, precision = 10, scale = 0)
    public Long getWorkresumeid() {
        return this.workresumeid;
    }

    public void setWorkresumeid(Long workresumeid) {
        this.workresumeid = workresumeid;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE", length = 7)
    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE", length = 7)
    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column(name = "UNIT", length = 30)
    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Column(name = "STATION_NAME", length = 60)
    public String getStationName() {
        return this.stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Column(name = "HEADSHIP_NAME", length = 30)
    public String getHeadshipName() {
        return this.headshipName;
    }

    public void setHeadshipName(String headshipName) {
        this.headshipName = headshipName;
    }

    @Column(name = "WITNESS", length = 12)
    public String getWitness() {
        return this.witness;
    }

    public void setWitness(String witness) {
        this.witness = witness;
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

    @Column(name = "EMP_ID", precision = 10, scale = 0)
    public Long getEmpId() {
        return this.empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }
 
    @Column(name="DEPT_NAME", length = 50)
    public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}


}