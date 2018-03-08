package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJDriverfile entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_DRIVERFILE")
public class AdJDriverfile implements java.io.Serializable {

	// Fields

	private Long id;
	private String driverCode;
	private String licence;
	private String licenceNo;
	private Date licenceDate;
	private Date checkDate;
	private String mobileNo;
	private String telNo;
	private String homeAddr;
	private String comAddr;
	private byte[] photo;
	private String isUse;
	private String updateUser;
	private Date updateTime;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdJDriverfile() {
	}

	/** minimal constructor */
	public AdJDriverfile(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJDriverfile(Long id, String driverCode, String licence,
			String licenceNo, Date licenceDate, Date checkDate,
			String mobileNo, String telNo, String homeAddr, String comAddr,
			byte[] photo, String isUse, String updateUser, Date updateTime, String enterpriseCode) {
		this.id = id;
		this.driverCode = driverCode;
		this.licence = licence;
		this.licenceNo = licenceNo;
		this.licenceDate = licenceDate;
		this.checkDate = checkDate;
		this.mobileNo = mobileNo;
		this.telNo = telNo;
		this.homeAddr = homeAddr;
		this.comAddr = comAddr;
		this.photo = photo;
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

	@Column(name = "DRIVER_CODE", length = 6)
	public String getDriverCode() {
		return this.driverCode;
	}

	public void setDriverCode(String driverCode) {
		this.driverCode = driverCode;
	}

	@Column(name = "LICENCE", length = 1)
	public String getLicence() {
		return this.licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	@Column(name = "LICENCE_NO", length = 20)
	public String getLicenceNo() {
		return this.licenceNo;
	}

	public void setLicenceNo(String licenceNo) {
		this.licenceNo = licenceNo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LICENCE_DATE", length = 7)
	public Date getLicenceDate() {
		return this.licenceDate;
	}

	public void setLicenceDate(Date licenceDate) {
		this.licenceDate = licenceDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECK_DATE", length = 7)
	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Column(name = "MOBILE_NO", length = 15)
	public String getMobileNo() {
		return this.mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Column(name = "TEL_NO", length = 15)
	public String getTelNo() {
		return this.telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	@Column(name = "HOME_ADDR", length = 100)
	public String getHomeAddr() {
		return this.homeAddr;
	}

	public void setHomeAddr(String homeAddr) {
		this.homeAddr = homeAddr;
	}

	@Column(name = "COM_ADDR", length = 100)
	public String getComAddr() {
		return this.comAddr;
	}

	public void setComAddr(String comAddr) {
		this.comAddr = comAddr;
	}

	@Column(name = "PHOTO")
	public byte[] getPhoto() {
		return this.photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
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