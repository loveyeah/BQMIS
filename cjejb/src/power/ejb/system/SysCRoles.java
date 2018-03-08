package power.ejb.system;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

 
/**
 * 角色实体
 * @author wzhyan  
 */
@Entity
@Table(name = "SYS_C_ROLES")
public class SysCRoles implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6443727119765979413L;
	private Long roleId;
	private String roleName;
	private String roleType;
	
	private String isUse;
	private String memo;
	private Long line;
	private String enterpriseCode;
	private String modifyBy;
	private Date modifyDate; 

	/** default constructor */
	public SysCRoles() {
	}

	/** minimal constructor */
	public SysCRoles(Long roleId) {
		this.roleId = roleId;
	}

	/** full constructor */
	public SysCRoles(Long roleId, String roleName, String roleType,
			String isUse, String memo, Long line, String enterpriseCode,
			String modifyBy, Date modifyDate) {
		this.roleId = roleId;
		this.roleName = roleName;
		this.roleType = roleType;
		this.isUse = isUse;
		this.memo = memo;
		this.line = line;
		this.enterpriseCode = enterpriseCode;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
	}

	// Property accessors
	@Id
	@Column(name = "ROLE_ID",  nullable = false, precision = 10, scale = 0)
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "ROLE_NAME", length = 30)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "role_type", length = 1)
	public String getRoleType() {
		return this.roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "MEMO", length = 100)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "LINE", precision = 10, scale = 0)
	public Long getLine() {
		return this.line;
	}

	public void setLine(Long line) {
		this.line = line;
	}

	@Column(name = "ENTERPRISE_CODE", length = 30)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
}