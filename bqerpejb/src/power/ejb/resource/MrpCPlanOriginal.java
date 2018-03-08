package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * MrpCPlanOriginal entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="MRP_C_PLAN_ORIGINAL")

public class MrpCPlanOriginal  implements java.io.Serializable {


    // Fields    

     private Long planOriginalId;
     private Long parentId;
     private String planOriginalDesc;
     private String originalType;
     private String isUse;
     private String enterpriseCode;
     private String lastModifiedBy;
     private Date lastModifiedDate;


    // Constructors

    /** default constructor */
    public MrpCPlanOriginal() {
    }

	/** minimal constructor */
    public MrpCPlanOriginal(Long planOriginalId, Long parentId, String lastModifiedBy, Date lastModifiedDate) {
        this.planOriginalId = planOriginalId;
        this.parentId = parentId;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }
    
    /** full constructor */
    public MrpCPlanOriginal(Long planOriginalId, Long parentId, String planOriginalDesc, String originalType, String isUse, String enterpriseCode, String lastModifiedBy, Date lastModifiedDate) {
        this.planOriginalId = planOriginalId;
        this.parentId = parentId;
        this.planOriginalDesc = planOriginalDesc;
        this.originalType = originalType;
        this.isUse = isUse;
        this.enterpriseCode = enterpriseCode;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="PLAN_ORIGINAL_ID", unique=true, nullable=false, precision=10, scale=0)

    public Long getPlanOriginalId() {
        return this.planOriginalId;
    }
    
    public void setPlanOriginalId(Long planOriginalId) {
        this.planOriginalId = planOriginalId;
    }
    
    @Column(name="PARENT_ID", nullable=false, precision=10, scale=0)

    public Long getParentId() {
        return this.parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    @Column(name="PLAN_ORIGINAL_DESC", length=100)

    public String getPlanOriginalDesc() {
        return this.planOriginalDesc;
    }
    
    public void setPlanOriginalDesc(String planOriginalDesc) {
        this.planOriginalDesc = planOriginalDesc;
    }
    
    @Column(name="ORIGINAL_TYPE", length=1)

    public String getOriginalType() {
        return this.originalType;
    }
    
    public void setOriginalType(String originalType) {
        this.originalType = originalType;
    }
    
    @Column(name="IS_USE", length=1)

    public String getIsUse() {
        return this.isUse;
    }
    
    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }
    
    @Column(name="ENTERPRISE_CODE", length=20)

    public String getEnterpriseCode() {
        return this.enterpriseCode;
    }
    
    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
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

}