package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpCDriver entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_C_DRIVER", schema = "POWER")
public class SpCDriver implements java.io.Serializable {

	// Fields

	private Long driverId;
	private String driverName;
	private String sex;
	private Long nativePlaceId;
	private Date brithday;
	private String deptCode;
	private Date workTime;
	private String homePhone;
	private Long politicsId;
	private Date joinInTime;
	private String mobilePhone;
	private String driveCode;
	private String allowDriverType;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpCDriver() {
	}

	/** minimal constructor */
	public SpCDriver(Long driverId) {
		this.driverId = driverId;
	}

	/** full constructor */
	public SpCDriver(Long driverId, String driverName, String sex,
			Long nativePlaceId, Date brithday, String deptCode, Date workTime,
			String homePhone, Long politicsId, Date joinInTime,
			String mobilePhone, String driveCode, String allowDriverType,
			String memo, String isUse, String enterpriseCode) {
		this.driverId = driverId;
		this.driverName = driverName;
		this.sex = sex;
		this.nativePlaceId = nativePlaceId;
		this.brithday = brithday;
		this.deptCode = deptCode;
		this.workTime = workTime;
		this.homePhone = homePhone;
		this.politicsId = politicsId;
		this.joinInTime = joinInTime;
		this.mobilePhone = mobilePhone;
		this.driveCode = driveCode;
		this.allowDriverType = allowDriverType;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DRIVER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDriverId() {
		return this.driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	@Column(name = "DRIVER_NAME", length = 25)
	public String getDriverName() {
		return this.driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	@Column(name = "SEX", length = 1)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "NATIVE_PLACE_ID", precision = 10, scale = 0)
	public Long getNativePlaceId() {
		return this.nativePlaceId;
	}

	public void setNativePlaceId(Long nativePlaceId) {
		this.nativePlaceId = nativePlaceId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BRITHDAY", length = 7)
	public Date getBrithday() {
		return this.brithday;
	}

	public void setBrithday(Date brithday) {
		this.brithday = brithday;
	}

	@Column(name = "DEPT_CODE", length = 20)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "WORK_TIME", length = 7)
	public Date getWorkTime() {
		return this.workTime;
	}

	public void setWorkTime(Date workTime) {
		this.workTime = workTime;
	}

	@Column(name = "HOME_PHONE", length = 20)
	public String getHomePhone() {
		return this.homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	@Column(name = "POLITICS_ID", precision = 2, scale = 0)
	public Long getPoliticsId() {
		return this.politicsId;
	}

	public void setPoliticsId(Long politicsId) {
		this.politicsId = politicsId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "JOIN_IN_TIME", length = 7)
	public Date getJoinInTime() {
		return this.joinInTime;
	}

	public void setJoinInTime(Date joinInTime) {
		this.joinInTime = joinInTime;
	}

	@Column(name = "MOBILE_PHONE", length = 20)
	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Column(name = "DRIVE_CODE", length = 30)
	public String getDriveCode() {
		return this.driveCode;
	}

	public void setDriveCode(String driveCode) {
		this.driveCode = driveCode;
	}

	@Column(name = "ALLOW_DRIVER_TYPE", length = 30)
	public String getAllowDriverType() {
		return this.allowDriverType;
	}

	public void setAllowDriverType(String allowDriverType) {
		this.allowDriverType = allowDriverType;
	}

	@Column(name = "MEMO", length = 300)
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

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}