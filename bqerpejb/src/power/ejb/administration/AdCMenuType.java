package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdCMenuType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_C_MENU_TYPE")
public class AdCMenuType implements java.io.Serializable {

	// Fields

	private Long id;
	private String menutypeCode;
	private String menutypeName;
	private String retrieveCode;
	private String isUse;
	private String updateUser;
	private Date updateTime;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdCMenuType() {
	}

	/** minimal constructor */
	public AdCMenuType(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdCMenuType(Long id, String menutypeCode, String menutypeName,
			String retrieveCode, String isUse, String updateUser,
			Date updateTime) {
		this.id = id;
		this.menutypeCode = menutypeCode;
		this.menutypeName = menutypeName;
		this.retrieveCode = retrieveCode;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
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

	@Column(name = "MENUTYPE_CODE", length = 2)
	public String getMenutypeCode() {
		return this.menutypeCode;
	}

	public void setMenutypeCode(String menutypeCode) {
		this.menutypeCode = menutypeCode;
	}

	@Column(name = "MENUTYPE_NAME", length = 50)
	public String getMenutypeName() {
		return this.menutypeName;
	}

	public void setMenutypeName(String menutypeName) {
		this.menutypeName = menutypeName;
	}

	@Column(name = "RETRIEVE_CODE", length = 6)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
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
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}