package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJApplycar entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_APPLYCAR")
public class AdJApplycar implements java.io.Serializable {

	// Fields

	private Long id;
	private String applyMan;
	private Date useDate;
	private Long useNum;
	private String ifOut;
	private String reason;
	private String depIdea;
	private String depBossCode;
	private Date depSignDate;
	private String xzBossCode;
	private String xzBossIdea;
	private Date xzSignDate;
	private String bigBossCode;
	private String bigBossIdea;
	private Date bigBossSignDate;
	private String adminIdea;
	private Date startTime;
	private Date endTime;
	private String aim;
	private Double lqPay;
	private Double useOil;
	private Double goMile;
	private Double comeMile;
	private Double distance;
	private String carNo;
	private String driver;
	private Double useDays;
	private String workFlowNo;
	private String bossIdea;
	private String isUse;
	private String updateUser;
	private String dcmStatus;
	private Date updateTime;
	private String carApplyId;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdJApplycar() {
	}

	/** minimal constructor */
	public AdJApplycar(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJApplycar(Long id, String applyMan, Date useDate, Long useNum,
			String ifOut, String reason, String depIdea, String depBossCode,
			Date depSignDate, String xzBossCode, String xzBossIdea,
			Date xzSignDate, String bigBossCode, String bigBossIdea,
			Date bigBossSignDate, String adminIdea, Date startTime,
			Date endTime, String aim, Double lqPay, Double useOil,
			Double goMile, Double comeMile, Double distance, String carNo,
			String driver, Double useDays, String workFlowNo, String bossIdea,
			String isUse, String updateUser, String dcmStatus, Date updateTime,
			String carApplyId, String enterpriseCode) {
		this.id = id;
		this.applyMan = applyMan;
		this.useDate = useDate;
		this.useNum = useNum;
		this.ifOut = ifOut;
		this.reason = reason;
		this.depIdea = depIdea;
		this.depBossCode = depBossCode;
		this.depSignDate = depSignDate;
		this.xzBossCode = xzBossCode;
		this.xzBossIdea = xzBossIdea;
		this.xzSignDate = xzSignDate;
		this.bigBossCode = bigBossCode;
		this.bigBossIdea = bigBossIdea;
		this.bigBossSignDate = bigBossSignDate;
		this.adminIdea = adminIdea;
		this.startTime = startTime;
		this.endTime = endTime;
		this.aim = aim;
		this.lqPay = lqPay;
		this.useOil = useOil;
		this.goMile = goMile;
		this.comeMile = comeMile;
		this.distance = distance;
		this.carNo = carNo;
		this.driver = driver;
		this.useDays = useDays;
		this.workFlowNo = workFlowNo;
		this.bossIdea = bossIdea;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.dcmStatus = dcmStatus;
		this.updateTime = updateTime;
		this.carApplyId = carApplyId;
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

	@Column(name = "APPLY_MAN", length = 6)
	public String getApplyMan() {
		return this.applyMan;
	}

	public void setApplyMan(String applyMan) {
		this.applyMan = applyMan;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "USE_DATE", length = 7)
	public Date getUseDate() {
		return this.useDate;
	}

	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}

	@Column(name = "USE_NUM", precision = 3, scale = 0)
	public Long getUseNum() {
		return this.useNum;
	}

	public void setUseNum(Long useNum) {
		this.useNum = useNum;
	}

	@Column(name = "IF_OUT", length = 1)
	public String getIfOut() {
		return this.ifOut;
	}

	public void setIfOut(String ifOut) {
		this.ifOut = ifOut;
	}

	@Column(name = "REASON", length = 100)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "DEP_IDEA", length = 100)
	public String getDepIdea() {
		return this.depIdea;
	}

	public void setDepIdea(String depIdea) {
		this.depIdea = depIdea;
	}

	@Column(name = "DEP_BOSS_CODE", length = 6)
	public String getDepBossCode() {
		return this.depBossCode;
	}

	public void setDepBossCode(String depBossCode) {
		this.depBossCode = depBossCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DEP_SIGN_DATE", length = 7)
	public Date getDepSignDate() {
		return this.depSignDate;
	}

	public void setDepSignDate(Date depSignDate) {
		this.depSignDate = depSignDate;
	}

	@Column(name = "XZ_BOSS_CODE", length = 6)
	public String getXzBossCode() {
		return this.xzBossCode;
	}

	public void setXzBossCode(String xzBossCode) {
		this.xzBossCode = xzBossCode;
	}

	@Column(name = "XZ_BOSS_IDEA", length = 100)
	public String getXzBossIdea() {
		return this.xzBossIdea;
	}

	public void setXzBossIdea(String xzBossIdea) {
		this.xzBossIdea = xzBossIdea;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "XZ_SIGN_DATE", length = 7)
	public Date getXzSignDate() {
		return this.xzSignDate;
	}

	public void setXzSignDate(Date xzSignDate) {
		this.xzSignDate = xzSignDate;
	}

	@Column(name = "BIG_BOSS_CODE", length = 6)
	public String getBigBossCode() {
		return this.bigBossCode;
	}

	public void setBigBossCode(String bigBossCode) {
		this.bigBossCode = bigBossCode;
	}

	@Column(name = "BIG_BOSS_IDEA", length = 100)
	public String getBigBossIdea() {
		return this.bigBossIdea;
	}

	public void setBigBossIdea(String bigBossIdea) {
		this.bigBossIdea = bigBossIdea;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BIG_BOSS_SIGN_DATE", length = 7)
	public Date getBigBossSignDate() {
		return this.bigBossSignDate;
	}

	public void setBigBossSignDate(Date bigBossSignDate) {
		this.bigBossSignDate = bigBossSignDate;
	}

	@Column(name = "ADMIN_IDEA", length = 100)
	public String getAdminIdea() {
		return this.adminIdea;
	}

	public void setAdminIdea(String adminIdea) {
		this.adminIdea = adminIdea;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", length = 7)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME", length = 7)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "AIM", length = 100)
	public String getAim() {
		return this.aim;
	}

	public void setAim(String aim) {
		this.aim = aim;
	}

	@Column(name = "LQ_PAY", precision = 14)
	public Double getLqPay() {
		return this.lqPay;
	}

	public void setLqPay(Double lqPay) {
		this.lqPay = lqPay;
	}

	@Column(name = "USE_OIL", precision = 14)
	public Double getUseOil() {
		return this.useOil;
	}

	public void setUseOil(Double useOil) {
		this.useOil = useOil;
	}

	@Column(name = "GO_MILE", precision = 15)
	public Double getGoMile() {
		return this.goMile;
	}

	public void setGoMile(Double goMile) {
		this.goMile = goMile;
	}

	@Column(name = "COME_MILE", precision = 15)
	public Double getComeMile() {
		return this.comeMile;
	}

	public void setComeMile(Double comeMile) {
		this.comeMile = comeMile;
	}

	@Column(name = "DISTANCE", precision = 15)
	public Double getDistance() {
		return this.distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	@Column(name = "CAR_NO", length = 10)
	public String getCarNo() {
		return this.carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	@Column(name = "DRIVER", length = 6)
	public String getDriver() {
		return this.driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	@Column(name = "USE_DAYS", precision = 10)
	public Double getUseDays() {
		return this.useDays;
	}

	public void setUseDays(Double useDays) {
		this.useDays = useDays;
	}

	@Column(name = "WORK_FLOW_NO", length = 26)
	public String getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(String workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "BOSS_IDEA", length = 100)
	public String getBossIdea() {
		return this.bossIdea;
	}

	public void setBossIdea(String bossIdea) {
		this.bossIdea = bossIdea;
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

	@Column(name = "DCM_STATUS", length = 1)
	public String getDcmStatus() {
		return this.dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "CAR_APPLY_ID", length = 12)
	public String getCarApplyId() {
		return this.carApplyId;
	}

	public void setCarApplyId(String carApplyId) {
		this.carApplyId = carApplyId;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}