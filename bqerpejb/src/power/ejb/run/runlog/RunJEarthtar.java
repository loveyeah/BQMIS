package power.ejb.run.runlog;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJEarthtar entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_EARTHTAR", uniqueConstraints = {})
public class RunJEarthtar implements java.io.Serializable {

	// Fields

	private Long earthRecordId;
	private Long installLogid;
	private Long earthId;
	private String installMan;
	private Date installTime;
	private String installPlace;
	private String installCharger;
	private String backoutMan;
	private Date backoutTime;
	private Long backoutLogid;
	private String backoutCharger;
	private String isBack;
	private String isUse;
	private String enterpriseCode;
	private String specialityCode;

	// Constructors

	/** default constructor */
	public RunJEarthtar() {
	}

	/** minimal constructor */
	public RunJEarthtar(Long earthRecordId) {
		this.earthRecordId = earthRecordId;
	}

	/** full constructor */
	public RunJEarthtar(Long earthRecordId, Long installLogid, Long earthId,
			String installMan, Date installTime, String installPlace,
			String installCharger, String backoutMan, Date backoutTime,
			Long backoutLogid, String backoutCharger, String isBack,
			String isUse, String enterpriseCode, String specialityCode) {
		this.earthRecordId = earthRecordId;
		this.installLogid = installLogid;
		this.earthId = earthId;
		this.installMan = installMan;
		this.installTime = installTime;
		this.installPlace = installPlace;
		this.installCharger = installCharger;
		this.backoutMan = backoutMan;
		this.backoutTime = backoutTime;
		this.backoutLogid = backoutLogid;
		this.backoutCharger = backoutCharger;
		this.isBack = isBack;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.specialityCode = specialityCode;
	}

	// Property accessors
	@Id
	@Column(name = "EARTH_RECORD_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getEarthRecordId() {
		return this.earthRecordId;
	}

	public void setEarthRecordId(Long earthRecordId) {
		this.earthRecordId = earthRecordId;
	}

	@Column(name = "INSTALL_LOGID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getInstallLogid() {
		return this.installLogid;
	}

	public void setInstallLogid(Long installLogid) {
		this.installLogid = installLogid;
	}

	@Column(name = "EARTH_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getEarthId() {
		return this.earthId;
	}

	public void setEarthId(Long earthId) {
		this.earthId = earthId;
	}

	@Column(name = "INSTALL_MAN", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getInstallMan() {
		return this.installMan;
	}

	public void setInstallMan(String installMan) {
		this.installMan = installMan;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSTALL_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getInstallTime() {
		return this.installTime;
	}

	public void setInstallTime(Date installTime) {
		this.installTime = installTime;
	}

	@Column(name = "INSTALL_PLACE", unique = false, nullable = true, insertable = true, updatable = true, length = 30)
	public String getInstallPlace() {
		return this.installPlace;
	}

	public void setInstallPlace(String installPlace) {
		this.installPlace = installPlace;
	}

	@Column(name = "INSTALL_CHARGER", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getInstallCharger() {
		return this.installCharger;
	}

	public void setInstallCharger(String installCharger) {
		this.installCharger = installCharger;
	}

	@Column(name = "BACKOUT_MAN", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getBackoutMan() {
		return this.backoutMan;
	}

	public void setBackoutMan(String backoutMan) {
		this.backoutMan = backoutMan;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BACKOUT_TIME", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getBackoutTime() {
		return this.backoutTime;
	}

	public void setBackoutTime(Date backoutTime) {
		this.backoutTime = backoutTime;
	}

	@Column(name = "BACKOUT_LOGID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getBackoutLogid() {
		return this.backoutLogid;
	}

	public void setBackoutLogid(Long backoutLogid) {
		this.backoutLogid = backoutLogid;
	}

	@Column(name = "BACKOUT_CHARGER", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getBackoutCharger() {
		return this.backoutCharger;
	}

	public void setBackoutCharger(String backoutCharger) {
		this.backoutCharger = backoutCharger;
	}

	@Column(name = "IS_BACK", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsBack() {
		return this.isBack;
	}

	public void setIsBack(String isBack) {
		this.isBack = isBack;
	}

	@Column(name = "IS_USE", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "SPECIALITY_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 15)
	public String getSpecialityCode() {
		return this.specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

}