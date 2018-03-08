package power.ejb.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 用色与功能模块关联实体
 * @author sltang modify by wzhyan  
 */
@Entity
@Table(name = "SYS_J_RRS")
//@SequenceGenerator(name="SYS_J_RRS_SEQUENCE", sequenceName="SYS_J_RRS_SEQUENCE")
public class SysJRrs implements java.io.Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -6554837021556430278L;
	private Long id;
	private Long roleId;
	private Long fileId;
	private String isUse;
	private String enterpriseCode;
	private String modifyBy;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public SysJRrs() {
	}

	/** minimal constructor */
	public SysJRrs(Long id, String enterpriseCode) {
		this.id = id;
		this.enterpriseCode = enterpriseCode;
	}

	/** full constructor */
	public SysJRrs(Long id, Long roleId, Long fileId, String isUse,
			String enterpriseCode, String modifyBy, Date modifyDate) {
		this.id = id;
		this.roleId = roleId;
		this.fileId = fileId;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
	}

	// Property accessors
	@Id
	@Column(name = "ID",  nullable = false, precision = 10, scale = 0)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SYS_J_RRS_SEQUENCE")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ROLE_ID", precision = 10, scale = 0)
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "FILE_ID", precision = 10, scale = 0)
	public Long getFileId() {
		return this.fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 30)
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