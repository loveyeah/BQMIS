package power.ejb.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



/**
 * 角色对应用户关联实体
 * @author sltang modify by wzhyan  
 */
@Entity
@Table(name = "SYS_J_UR")
//@javax.persistence.SequenceGenerator(name="SYS_J_UR_SEQUENCE" ,sequenceName="SYS_J_UR_SEQUENCE")
public class SysJUr implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5809834655115105166L;
	private Long id;
	private Long workerId;
	private Long roleId;
	private String isUse;
	private String enterpriseCode;
	private String modifyBy;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public SysJUr() {
	}

	/** minimal constructor */
	public SysJUr(Long id) {
		this.id = id;
	}

	/** full constructor */
	public SysJUr(Long id, Long workerId, Long roleId, String isUse,
			String enterpriseCode, String modifyBy, Date modifyDate) {
		this.id = id;
		this.workerId = workerId;
		this.roleId = roleId;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
	}

	// Property accessors
	@Id
	@Column(name = "ID",  nullable = false, precision = 10, scale = 0)
	//@javax.persistence.GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SYS_J_UR_SEQUENCE")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "WORKER_ID", precision = 10, scale = 0)
	public Long getWorkerId() {
		return this.workerId;
	}

	public void setWorkerId(Long workerId) {
		this.workerId = workerId;
	}

	@Column(name = "ROLE_ID", precision = 10, scale = 0)
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
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