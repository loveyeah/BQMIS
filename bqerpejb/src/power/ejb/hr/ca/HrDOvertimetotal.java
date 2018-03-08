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
 * 加班统计bean
 * 
 * @author zhouxu
 * @version 1.0
 */
@Entity
@Table(name = "HR_D_OVERTIMETOTAL")
public class HrDOvertimetotal implements java.io.Serializable {

    // Fields

    private static final long serialVersionUID = 1L;
    private HrDOvertimetotalId id;
    private Long deptId;
    private Long attendanceDeptId;
    private Double days;
    private String lastModifiyBy;
    private Date lastModifiyDate;
    private String isUse;
    private String enterpriseCode;

    // Constructors

    /** default constructor */
    public HrDOvertimetotal() {
    }

    /** minimal constructor */
    public HrDOvertimetotal(HrDOvertimetotalId id) {
        this.id = id;
    }

    /** full constructor */
    public HrDOvertimetotal(HrDOvertimetotalId id, Long deptId, Double days, String lastModifiyBy,
            Date lastModifiyDate, String isUse, String enterpriseCode) {
        this.id = id;
        this.deptId = deptId;
        this.days = days;
        this.lastModifiyBy = lastModifiyBy;
        this.lastModifiyDate = lastModifiyDate;
        this.isUse = isUse;
        this.enterpriseCode = enterpriseCode;
    }

    // Property accessors
    @EmbeddedId
    @AttributeOverrides( {
            @AttributeOverride(name = "attendanceYear", column = @Column(name = "ATTENDANCE_YEAR", nullable = false, length = 4)),
            @AttributeOverride(name = "attendanceMonth", column = @Column(name = "ATTENDANCE_MONTH", nullable = false, length = 2)),
            @AttributeOverride(name = "empId", column = @Column(name = "EMP_ID", nullable = false, precision = 10, scale = 0)),
            @AttributeOverride(name = "overtimeTypeId", column = @Column(name = "OVERTIME_TYPE_ID", nullable = false, precision = 10, scale = 0)) })
    public HrDOvertimetotalId getId() {
        return this.id;
    }

    public void setId(HrDOvertimetotalId id) {
        this.id = id;
    }

    @Column(name = "DEPT_ID", precision = 10, scale = 0)
    public Long getDeptId() {
        return this.deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    @Column(name = "DAYS", precision = 15, scale = 4)
    public Double getDays() {
        return this.days;
    }

    public void setDays(Double days) {
        this.days = days;
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

    @Column(name = "ATTENDANCE_DEPT_ID", length = 10)
	public Long getAttendanceDeptId() {
		return attendanceDeptId;
	}

	public void setAttendanceDeptId(Long attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}

}