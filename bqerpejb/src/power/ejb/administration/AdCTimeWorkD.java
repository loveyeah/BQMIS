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
 * AdCTimeWorkD entity. 
 * @author liugonglei
 */
@Entity
@Table(name="AD_C_TIMEWORKD")
public class AdCTimeWorkD  implements java.io.Serializable {


    // Fields    
	 /**序号 */
     private Long id;
     /**工作项目编码 */
     private String workitemCode;
     /**周期号 */
     private Long rangeNumber;
     
     private String memo;
     /**备注 */
     private String isUse;
     /**是否使用 */
     /**修改人 */
     private String updateUser;
     /**修改时间 */
     private Date updateTime;


    // Constructors

    /** default constructor */
    public AdCTimeWorkD() {
    }

	/** minimal constructor */
    public AdCTimeWorkD(Long id) {
        this.id = id;
    }
    
    /** full constructor */
    public AdCTimeWorkD(Long id, String workitemCode, Long rangeNumber, String memo, String isUse, String updateUser, Date updateTime) {
        this.id = id;
        this.workitemCode = workitemCode;
        this.rangeNumber = rangeNumber;
        this.memo = memo;
        this.isUse = isUse;
        this.updateUser = updateUser;
        this.updateTime = updateTime;
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
    
    @Column(name="RANGE_NUMBER", precision=10, scale=0)
    // 取得周期号
    public Long getRangeNumber() {
        return this.rangeNumber;
    }
 // 设定周期号
    public void setRangeNumber(Long rangeNumber) {
        this.rangeNumber = rangeNumber;
    }
    
    @Column(name="MEMO", length=100)
    // 取得备注
    public String getMemo() {
        return this.memo;
    }
    // 设定备注
    public void setMemo(String memo) {
        this.memo = memo;
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
    // 取得修改人
    public String getUpdateUser() {
        return this.updateUser;
    }
    // 设定修改人
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UPDATE_TIME", length=7)
    // 取得修改时间
    public Date getUpdateTime() {
        return this.updateTime;
    }
    //设定修改时间
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}