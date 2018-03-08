package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * InvJBalance entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="INV_J_BALANCE")

public class InvJBalance  implements java.io.Serializable {


    // Fields    

     private Long balanceId;
     private String balanceType;
     private String whsNo;
     private String locationNo;
     private Long balanceYearMonth;
     private Long balanceYear;
     private Date balanceDate;
     private Long transHisMinid;
     private Long transHisMaxid;
     private String balanceStatus;
     private String lastModifiedBy;
     private Date lastModifiedDate;
     private String enterpriseCode;
     private String isUse;


    // Constructors

    /** default constructor */
    public InvJBalance() {
    }

	/** minimal constructor */
    public InvJBalance(Long balanceId, String balanceType, String whsNo, String locationNo, Date balanceDate, Long transHisMinid, Long transHisMaxid, String balanceStatus, String lastModifiedBy, Date lastModifiedDate, String enterpriseCode, String isUse) {
        this.balanceId = balanceId;
        this.balanceType = balanceType;
        this.whsNo = whsNo;
        this.locationNo = locationNo;
        this.balanceDate = balanceDate;
        this.transHisMinid = transHisMinid;
        this.transHisMaxid = transHisMaxid;
        this.balanceStatus = balanceStatus;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.enterpriseCode = enterpriseCode;
        this.isUse = isUse;
    }
    
    /** full constructor */
    public InvJBalance(Long balanceId, String balanceType, String whsNo, String locationNo, Long balanceYearMonth, Long balanceYear, Date balanceDate, Long transHisMinid, Long transHisMaxid, String balanceStatus, String lastModifiedBy, Date lastModifiedDate, String enterpriseCode, String isUse) {
        this.balanceId = balanceId;
        this.balanceType = balanceType;
        this.whsNo = whsNo;
        this.locationNo = locationNo;
        this.balanceYearMonth = balanceYearMonth;
        this.balanceYear = balanceYear;
        this.balanceDate = balanceDate;
        this.transHisMinid = transHisMinid;
        this.transHisMaxid = transHisMaxid;
        this.balanceStatus = balanceStatus;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.enterpriseCode = enterpriseCode;
        this.isUse = isUse;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="BALANCE_ID", unique=true, nullable=false, precision=10, scale=0)

    public Long getBalanceId() {
        return this.balanceId;
    }
    
    public void setBalanceId(Long balanceId) {
        this.balanceId = balanceId;
    }
    
    @Column(name="BALANCE_TYPE", nullable=false, length=1)

    public String getBalanceType() {
        return this.balanceType;
    }
    
    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }
    
    @Column(name="WHS_NO", length=10)

    public String getWhsNo() {
        return this.whsNo;
    }
    
    public void setWhsNo(String whsNo) {
        this.whsNo = whsNo;
    }
    
    @Column(name="LOCATION_NO",  length=10)

    public String getLocationNo() {
        return this.locationNo;
    }
    
    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }
    
    @Column(name="BALANCE_YEAR_MONTH", precision=10, scale=0)

    public Long getBalanceYearMonth() {
        return this.balanceYearMonth;
    }
    
    public void setBalanceYearMonth(Long balanceYearMonth) {
        this.balanceYearMonth = balanceYearMonth;
    }
    
    @Column(name="BALANCE_YEAR", precision=10, scale=0)

    public Long getBalanceYear() {
        return this.balanceYear;
    }
    
    public void setBalanceYear(Long balanceYear) {
        this.balanceYear = balanceYear;
    }
@Temporal(TemporalType.TIMESTAMP)
    @Column(name="BALANCE_DATE", nullable=false, length=7)

    public Date getBalanceDate() {
        return this.balanceDate;
    }
    
    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }
    
    @Column(name="TRANS_HIS_MINID", nullable=false, precision=10, scale=0)

    public Long getTransHisMinid() {
        return this.transHisMinid;
    }
    
    public void setTransHisMinid(Long transHisMinid) {
        this.transHisMinid = transHisMinid;
    }
    
    @Column(name="TRANS_HIS_MAXID", nullable=false, precision=10, scale=0)

    public Long getTransHisMaxid() {
        return this.transHisMaxid;
    }
    
    public void setTransHisMaxid(Long transHisMaxid) {
        this.transHisMaxid = transHisMaxid;
    }
    
    @Column(name="BALANCE_STATUS", nullable=false, length=2)

    public String getBalanceStatus() {
        return this.balanceStatus;
    }
    
    public void setBalanceStatus(String balanceStatus) {
        this.balanceStatus = balanceStatus;
    }
    
    @Column(name="LAST_MODIFIED_BY", nullable=false, length=16)

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }
    
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
@Temporal(TemporalType.TIMESTAMP)
    @Column(name="LAST_MODIFIED_DATE", nullable=false, length=7)

    public Date getLastModifiedDate() {
        return this.lastModifiedDate;
    }
    
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    @Column(name="ENTERPRISE_CODE", nullable=false, length=10)

    public String getEnterpriseCode() {
        return this.enterpriseCode;
    }
    
    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }
    
    @Column(name="IS_USE", nullable=false, length=1)

    public String getIsUse() {
        return this.isUse;
    }
    
    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }
   








}