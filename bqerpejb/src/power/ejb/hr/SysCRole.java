package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SysCRole entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_C_ROLE", schema = "power")
public class SysCRole implements java.io.Serializable {

	// Fields

	private Long roleId;
	private Long roleTypeId;
	private String roleName;
	private String roleStatus;
	private String memo;
	private Long orderBy;
	private Long createBy;
	private Date createDate;
	private Long lastModifiyBy;
	private Date lastModifiyDate;

	// Constructors

	/** default constructor */
	public SysCRole() {
	}

	/** minimal constructor */
	public SysCRole(Long roleId) {
		this.roleId = roleId;
	}

	/** full constructor */
	public SysCRole(Long roleId, Long roleTypeId, String roleName,
			String roleStatus, String memo, Long orderBy, Long createBy,
			Date createDate, Long lastModifiyBy, Date lastModifiyDate) {
		this.roleId = roleId;
		this.roleTypeId = roleTypeId;
		this.roleName = roleName;
		this.roleStatus = roleStatus;
		this.memo = memo;
		this.orderBy = orderBy;
		this.createBy = createBy;
		this.createDate = createDate;
		this.lastModifiyBy = lastModifiyBy;
		this.lastModifiyDate = lastModifiyDate;
	}

	// Property accessors
	@Id
	@Column(name = "ROLE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "ROLE_TYPE_ID", precision = 10, scale = 0)
	public Long getRoleTypeId() {
		return this.roleTypeId;
	}

	public void setRoleTypeId(Long roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	@Column(name = "ROLE_NAME", length = 100)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "ROLE_STATUS", length = 1)
	public String getRoleStatus() {
		return this.roleStatus;
	}

	public void setRoleStatus(String roleStatus) {
		this.roleStatus = roleStatus;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "CREATE_BY", precision = 10, scale = 0)
	public Long getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "LAST_MODIFIY_BY", precision = 10, scale = 0)
	public Long getLastModifiyBy() {
		return this.lastModifiyBy;
	}

	public void setLastModifiyBy(Long lastModifiyBy) {
		this.lastModifiyBy = lastModifiyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFIY_DATE", length = 7)
	public Date getLastModifiyDate() {
		return this.lastModifiyDate;
	}

	public void setLastModifiyDate(Date lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

}