/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
    
/**
 * HrCVacationtype entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_VACATIONTYPE")
public class HrCVacationtype implements java.io.Serializable {

    // Fields

    private Long vacationTypeId;
    private String vacationType;
    private String ifCycle;
    private String ifWeekend;
    private String vacationMark;
    private String lastModifiyBy;
    private Date lastModifiyDate;
    private String isUse;
    private String enterpriseCode;

    // Constructors

    /** default constructor */
    public HrCVacationtype() {
    }

    /** minimal constructor */
    public HrCVacationtype(Long vacationTypeId) {
        this.vacationTypeId = vacationTypeId;
    }

    /** full constructor */
    public HrCVacationtype(Long vacationTypeId, String vacationType,
            String ifCycle, String ifWeekend, String vacationMark,
            String lastModifiyBy, Date lastModifiyDate, String isUse,
            String enterpriseCode) {
        this.vacationTypeId = vacationTypeId;
        this.vacationType = vacationType;
        this.ifCycle = ifCycle;
        this.ifWeekend = ifWeekend;
        this.vacationMark = vacationMark;
        this.lastModifiyBy = lastModifiyBy;
        this.lastModifiyDate = lastModifiyDate;
        this.isUse = isUse;
        this.enterpriseCode = enterpriseCode;
    }

    // Property accessors
    @Id
    @Column(name = "VACATION_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Long getVacationTypeId() {
        return this.vacationTypeId;
    }

    public void setVacationTypeId(Long vacationTypeId) {
        this.vacationTypeId = vacationTypeId;
    }

    @Column(name = "VACATION_TYPE", length = 20)
    public String getVacationType() {
        return this.vacationType;
    }

    public void setVacationType(String vacationType) {
        this.vacationType = vacationType;
    }

    @Column(name = "IF_CYCLE", length = 1)
    public String getIfCycle() {
        return this.ifCycle;
    }

    public void setIfCycle(String ifCycle) {
        this.ifCycle = ifCycle;
    }

    @Column(name = "IF_WEEKEND", length = 1)
    public String getIfWeekend() {
        return this.ifWeekend;
    }

    public void setIfWeekend(String ifWeekend) {
        this.ifWeekend = ifWeekend;
    }

    @Column(name = "VACATION_MARK", length = 10)
    public String getVacationMark() {
        return this.vacationMark;
    }

    public void setVacationMark(String vacationMark) {
        this.vacationMark = vacationMark;
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

}