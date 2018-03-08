package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJUserMenu entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_USER_MENU")
public class AdJUserMenu implements java.io.Serializable {

	// Fields

	private static final long serialVersionUID = 1L;
	private Long MId;
	private Date menuDate;
	private String menuType;
	private String menuInfo;
	private String insertby;
	private Date insertdate;
	private String memo;
	private String isUse;
	private String updateUser;
	private Date updateTime;
	private String place;
	private String enterpriseCode;

	// Constructors



	/** default constructor */
	public AdJUserMenu() {
	}

	/** minimal constructor */
	public AdJUserMenu(Long MId) {
		this.MId = MId;
	}

	/** full constructor */
	public AdJUserMenu(Long MId, Date menuDate, String menuType,
			String menuInfo, String insertby, Date insertdate, String memo,
			String isUse, String updateUser, Date updateTime, String place) {
		this.MId = MId;
		this.menuDate = menuDate;
		this.menuType = menuType;
		this.menuInfo = menuInfo;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.memo = memo;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.place = place;
	}

	// Property accessors
	@Id
	@Column(name = "M_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMId() {
		return this.MId;
	}

	public void setMId(Long MId) {
		this.MId = MId;
	}
	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MENU_DATE", length = 7)
	public Date getMenuDate() {
		return this.menuDate;
	}

	public void setMenuDate(Date menuDate) {
		this.menuDate = menuDate;
	}

	@Column(name = "MENU_TYPE", length = 1)
	public String getMenuType() {
		return this.menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	@Column(name = "MENU_INFO", length = 1)
	public String getMenuInfo() {
		return this.menuInfo;
	}

	public void setMenuInfo(String menuInfo) {
		this.menuInfo = menuInfo;
	}

	@Column(name = "INSERTBY", length = 6)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

	@Column(name = "MEMO")
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	@Column(name = "PLACE", length = 1)
	public String getPlace() {
		return this.place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

}