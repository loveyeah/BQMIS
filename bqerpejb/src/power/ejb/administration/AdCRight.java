/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdCRight entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_C_RIGHT")
public class AdCRight implements java.io.Serializable {

    // Fields

    private Long id;
    private String userCode;
    private String worktypeCode;
    private String isUse;
    private String updateUser;
    private Date updateTime;
    private String enterpriseCode;

    // Constructors

    /** default constructor */
    public AdCRight() {
    }

    /** minimal constructor */
    public AdCRight(Long id) {
        this.id = id;
    }

    /** full constructor */
    public AdCRight(Long id, String userCode, String worktypeCode,
            String isUse, String updateUser, Date updateTime, String enterpriseCode) {
        this.id = id;
        this.userCode = userCode;
        this.worktypeCode = worktypeCode;
        this.isUse = isUse;
        this.updateUser = updateUser;
        this.updateTime = updateTime;
        this.enterpriseCode = enterpriseCode;
    }

    // Property accessors
    @Id
    @Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "USER_CODE", length = 6)
    public String getUserCode() {
        return this.userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Column(name = "WORKTYPE_CODE", length = 2)
    public String getWorktypeCode() {
        return this.worktypeCode;
    }

    public void setWorktypeCode(String worktypeCode) {
        this.worktypeCode = worktypeCode;
    }

    @Column(name = "IS_USE", length = 1)
    public String getIsUse() {
        return this.isUse;
    }

    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }

    @Column(name = "UPDATE_USER", length = 10)
    public String getUpdateUser() {
        return this.updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_TIME", length = 7)
    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    @Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}