/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;
// default package

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * AdcTimeWork entity. 
 * @author liugonglei
 */
@SuppressWarnings("serial")
@Entity
@Table(name="AD_C_TIMEWORK")
public class AdCTimeWork  implements java.io.Serializable {


    // Fields    
     /**序号 */
     private Long id;
     /**工作项目编码 */
     private String workitemCode;
     /**工作项目名称 */
     private String workitemName;
     /**类别编码 */
     private String worktypeCode;
     /**子类别编码 */
     private String subWorktypeCode;
     /**周期类别 */
     private String workrangeType;
     /**开始时间 */
     private Date startTime;
     /**班次 */
     private Long classSequence;
     /**工作说明 */
     private String workExplain;
     /**备注 */
     private String memo;
     /**节假日是否工作 */
     private String ifWeekend;
     /**检索码 */
     private String retrieveCode;
     /**是否使用 */
     private String isUse;
     /**修改人 */
     private String updateUser;
     /**修改时间 */
     private Date updateTime;
     /**企业代码 */
     private String enterPriseCode;

    // Constructors

    /** default constructor */
    public AdCTimeWork() {
    }

	/** minimal constructor */
    public AdCTimeWork(Long id) {
        this.id = id;
    }
    
    /** full constructor */
    public AdCTimeWork(Long id, String workitemCode, String workitemName, String worktypeCode, String subWorktypeCode, String workrangeType, Date startTime, Long classSequence, String workExplain, String memo, String ifWeekend, String ifFlag, String retrieveCode, String isUse, String updateUser, Date updateTime,String enterPriseCode) {
        this.id = id;
        this.workitemCode = workitemCode;
        this.workitemName = workitemName;
        this.worktypeCode = worktypeCode;
        this.subWorktypeCode = subWorktypeCode;
        this.workrangeType = workrangeType;
        this.startTime = startTime;
        this.classSequence = classSequence;
        this.workExplain = workExplain;
        this.memo = memo;
        this.ifWeekend = ifWeekend;
        this.retrieveCode = retrieveCode;
        this.isUse = isUse;
        this.updateUser = updateUser;
        this.updateTime = updateTime;
        this.enterPriseCode = enterPriseCode;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="ID", unique=true, nullable=false, precision=10, scale=0)
    // 取得id
    public Long getId() {
        return this.id;
    }
    
    // 设定id
    public void setId(Long id) {
        this.id = id;
    }
    
    @Column(name="WORKITEM_CODE", length=6)

   // 取得工作项目编码
    public String getWorkitemCode() {
        return this.workitemCode;
    }
    
    // 设定工作项目编码
    public void setWorkitemCode(String workitemCode) {
        this.workitemCode = workitemCode;
    }
    
    @Column(name="WORKITEM_NAME", length=50)

   // 取得工作项目名称
    public String getWorkitemName() {
        return this.workitemName;
    }
    
    // 设定工作项目名称
    public void setWorkitemName(String workitemName) {
        this.workitemName = workitemName;
    }
    
    @Column(name="WORKTYPE_CODE", length=2)

   // 取得类别编码
    public String getWorktypeCode() {
        return this.worktypeCode;
    }
    
    // 设定类别编码
    public void setWorktypeCode(String worktypeCode) {
        this.worktypeCode = worktypeCode;
    }
    
    @Column(name="SUB_WORKTYPE_CODE", length=4)

   // 取得子类别编码
    public String getSubWorktypeCode() {
        return this.subWorktypeCode;
    }
    
    // 设定子类别编码
    public void setSubWorktypeCode(String subWorktypeCode) {
        this.subWorktypeCode = subWorktypeCode;
    }
    
    @Column(name="WORKRANGE_TYPE", length=1)

   // 取得周期类别
    public String getWorkrangeType() {
        return this.workrangeType;
    }
    
    // 设定周期类别
    public void setWorkrangeType(String workrangeType) {
        this.workrangeType = workrangeType;
    }
@Temporal(TemporalType.TIMESTAMP)
    @Column(name="START_TIME", length=7)
    // 取得开始时间
    public Date getStartTime() {
        return this.startTime;
    }
    
    // 设定开始时间
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    @Column(name="CLASS_SEQUENCE", precision=1, scale=0)
    // 取得班次
    public Long getClassSequence() {
        return this.classSequence;
    }
    
    // 设定班次
    public void setClassSequence(Long classSequence) {
        this.classSequence = classSequence;
    }
    
    @Column(name="WORK_EXPLAIN", length=200)

   // 取得工作说明
    public String getWorkExplain() {
        return this.workExplain;
    }
    
    // 设定工作说明
    public void setWorkExplain(String workExplain) {
        this.workExplain = workExplain;
    }
    
    @Column(name="MEMO", length=200)

   // 取得备注
    public String getMemo() {
        return this.memo;
    }
    
    // 设定备注
    public void setMemo(String memo) {
        this.memo = memo;
    }
    
    @Column(name="IF_WEEKEND", length=1)

   // 取得节假日是否工作
    public String getIfWeekend() {
        return this.ifWeekend;
    }
    
 // 设定节假日是否工作
    public void setIfWeekend(String ifWeekend) {
        this.ifWeekend = ifWeekend;
    }
    
    @Column(name="RETRIEVE_CODE", length=8)

    // 取得检索码
    public String getRetrieveCode() {
        return this.retrieveCode;
    }
    
    // 设定检索码
    public void setRetrieveCode(String retrieveCode) {
        this.retrieveCode = retrieveCode;
    }
    
    @Column(name="IS_USE", length=1)

    // 取得是否使用
    public String getIsUse() {
        return this.isUse;
    }
    
    // 设定是否使用
    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }
    
    @Column(name="UPDATE_USER", length=10)

   // 取得更新者
    public String getUpdateUser() {
        return this.updateUser;
    }
    
    // 设定更新者
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
   @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UPDATE_TIME", length=7)

    public Date getUpdateTime() {
        return this.updateTime;
    }
    
    // 设定修改时间
	public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
         }

	/**
	 * @return the enterPriseCode
	 */
	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterPriseCode() {
		return enterPriseCode;
	}

	/**
	 * @param enterPriseCode the enterPriseCode to set
	 */
	public void setEnterPriseCode(String enterPriseCode) {
		this.enterPriseCode = enterPriseCode;
	}
    }