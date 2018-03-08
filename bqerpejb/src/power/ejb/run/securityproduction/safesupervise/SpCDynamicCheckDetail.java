package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * SpCDynamicCheckDetail entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="SP_C_DYNAMIC_CHECK_DETAIL")

public class SpCDynamicCheckDetail  implements java.io.Serializable {


    // Fields    

     private Long detailId;
     private Long mainId;
     private String existQuestion;
     private String wholeStep;
     private String avoidStep;
     private Date planDate;
     private Date actualDate;
     private String dutyDeptCode;
     private String dutyBy;
     private String superDeptCode;
     private String superBy;
     private String noReason;
     private String issueProerty;
     private String isUse;
     private String enterpriseCode;
     private String entryBy;
     private Date entryDate;


    // Constructors

    /** default constructor */
    public SpCDynamicCheckDetail() {
    }

	/** minimal constructor */
    public SpCDynamicCheckDetail(Long detailId) {
        this.detailId = detailId;
    }
    
    /** full constructor */
    public SpCDynamicCheckDetail(Long detailId, Long mainId, String existQuestion, String wholeStep, String avoidStep, Date planDate, Date actualDate, String dutyDeptCode, String dutyBy, String superDeptCode, String superBy, String noReason, String issueProerty, String isUse, String enterpriseCode, String entryBy, Date entryDate) {
        this.detailId = detailId;
        this.mainId = mainId;
        this.existQuestion = existQuestion;
        this.wholeStep = wholeStep;
        this.avoidStep = avoidStep;
        this.planDate = planDate;
        this.actualDate = actualDate;
        this.dutyDeptCode = dutyDeptCode;
        this.dutyBy = dutyBy;
        this.superDeptCode = superDeptCode;
        this.superBy = superBy;
        this.noReason = noReason;
        this.issueProerty = issueProerty;
        this.isUse = isUse;
        this.enterpriseCode = enterpriseCode;
        this.entryBy = entryBy;
        this.entryDate = entryDate;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="DETAIL_ID", unique=true, nullable=false, precision=10, scale=0)

    public Long getDetailId() {
        return this.detailId;
    }
    
    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }
    
    @Column(name="MAIN_ID", precision=10, scale=0)

    public Long getMainId() {
        return this.mainId;
    }
    
    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }
    
    @Column(name="EXIST_QUESTION", length=100)

    public String getExistQuestion() {
        return this.existQuestion;
    }
    
    public void setExistQuestion(String existQuestion) {
        this.existQuestion = existQuestion;
    }
    
    @Column(name="WHOLE_STEP", length=100)

    public String getWholeStep() {
        return this.wholeStep;
    }
    
    public void setWholeStep(String wholeStep) {
        this.wholeStep = wholeStep;
    }
    
    @Column(name="AVOID_STEP", length=100)

    public String getAvoidStep() {
        return this.avoidStep;
    }
    
    public void setAvoidStep(String avoidStep) {
        this.avoidStep = avoidStep;
    }
@Temporal(TemporalType.DATE)
    @Column(name="PLAN_DATE", length=7)

    public Date getPlanDate() {
        return this.planDate;
    }
    
    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }
@Temporal(TemporalType.DATE)
    @Column(name="ACTUAL_DATE", length=7)

    public Date getActualDate() {
        return this.actualDate;
    }
    
    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }
    
    @Column(name="DUTY_DEPT_CODE", length=20)

    public String getDutyDeptCode() {
        return this.dutyDeptCode;
    }
    
    public void setDutyDeptCode(String dutyDeptCode) {
        this.dutyDeptCode = dutyDeptCode;
    }
    
    @Column(name="DUTY_BY", length=20)

    public String getDutyBy() {
        return this.dutyBy;
    }
    
    public void setDutyBy(String dutyBy) {
        this.dutyBy = dutyBy;
    }
    
    @Column(name="SUPER_DEPT_CODE", length=20)

    public String getSuperDeptCode() {
        return this.superDeptCode;
    }
    
    public void setSuperDeptCode(String superDeptCode) {
        this.superDeptCode = superDeptCode;
    }
    
    @Column(name="SUPER_BY", length=20)

    public String getSuperBy() {
        return this.superBy;
    }
    
    public void setSuperBy(String superBy) {
        this.superBy = superBy;
    }
    
    @Column(name="NO_REASON", length=100)

    public String getNoReason() {
        return this.noReason;
    }
    
    public void setNoReason(String noReason) {
        this.noReason = noReason;
    }
    
    @Column(name="ISSUE_PROERTY", length=20)

    public String getIssueProerty() {
        return this.issueProerty;
    }
    
    public void setIssueProerty(String issueProerty) {
        this.issueProerty = issueProerty;
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
    
    @Column(name="ENTRY_BY", length=20)

    public String getEntryBy() {
        return this.entryBy;
    }
    
    public void setEntryBy(String entryBy) {
        this.entryBy = entryBy;
    }
@Temporal(TemporalType.DATE)
    @Column(name="ENTRY_DATE", length=7)

    public Date getEntryDate() {
        return this.entryDate;
    }
    
    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }
   








}