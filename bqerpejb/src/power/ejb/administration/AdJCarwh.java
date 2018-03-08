package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJCarwh entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_CARWH")
public class AdJCarwh implements java.io.Serializable {

	// Fields

	private Long id;
	private String whId;
	private String carNo;
	private Date repairDate;
	private String cpCode;
	private Double driveMile;
	private String manager;
	private String reason;
	private Double sum;
	private Double realSum;
	private String memo;
	private String dcmStatus;
	private String workFlowNo;
	private String isUse;
	private String updateUser;
	private Date updateTime;
	private String depBossCode;
	private String depIdea;
	private Date depSignDate;
	private String viceBossCode;
	private String viceBossIdea;
	private Date viceBossSignDate;
	private String bigBossCode;
	private String bigBossIdea;
	private Date bigBossSignDate;
	private String repairStatus;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdJCarwh() {
	}

	/** minimal constructor */
	public AdJCarwh(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJCarwh(Long id, String whId, String carNo, Date repairDate,
			String cpCode, Double driveMile, String manager, String driver,
			String reason, Double sum, Double realSum, String memo,
			String dcmStatus, String workFlowNo, String isUse,
			String updateUser, Date updateTime, String depBossCode,
			String depIdea, Date depSignDate, String viceBossCode,
			String viceBossIdea, Date viceBossSignDate, String bigBossCode,
			String bigBossIdea, Date bigBossSignDate, String repairStatus,
			String enterpriseCode) {
		this.id = id;
		this.whId = whId;
		this.carNo = carNo;
		this.repairDate = repairDate;
		this.cpCode = cpCode;
		this.driveMile = driveMile;
		this.manager = manager;
		this.reason = reason;
		this.sum = sum;
		this.realSum = realSum;
		this.memo = memo;
		this.dcmStatus = dcmStatus;
		this.workFlowNo = workFlowNo;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.depBossCode = depBossCode;
		this.depIdea = depIdea;
		this.depSignDate = depSignDate;
		this.viceBossCode = viceBossCode;
		this.viceBossIdea = viceBossIdea;
		this.viceBossSignDate = viceBossSignDate;
		this.bigBossCode = bigBossCode;
		this.bigBossIdea = bigBossIdea;
		this.bigBossSignDate = bigBossSignDate;
		this.repairStatus = repairStatus;
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

	@Column(name = "WH_ID", length = 12)
	public String getWhId() {
		return this.whId;
	}

	public void setWhId(String whId) {
		this.whId = whId;
	}

	@Column(name = "CAR_NO", length = 10)
	public String getCarNo() {
		return this.carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REPAIR_DATE", length = 7)
	public Date getRepairDate() {
		return this.repairDate;
	}

	public void setRepairDate(Date repairDate) {
		this.repairDate = repairDate;
	}

	@Column(name = "CP_CODE", length = 6)
	public String getCpCode() {
		return this.cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	@Column(name = "DRIVE_MILE", precision = 15)
	public Double getDriveMile() {
		return this.driveMile;
	}

	public void setDriveMile(Double driveMile) {
		this.driveMile = driveMile;
	}

	@Column(name = "MANAGER", length = 6)
	public String getManager() {
		return this.manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
	
	@Column(name = "REASON", length = 60)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "SUM", precision = 13)
	public Double getSum() {
		return this.sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	@Column(name = "REAL_SUM", precision = 13)
	public Double getRealSum() {
		return this.realSum;
	}

	public void setRealSum(Double realSum) {
		this.realSum = realSum;
	}

	@Column(name = "MEMO", length = 100)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "DCM_STATUS", length = 1)
	public String getDcmStatus() {
		return this.dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}

	@Column(name = "WORK_FLOW_NO", length = 26)
	public String getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(String workFlowNo) {
		this.workFlowNo = workFlowNo;
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

	@Column(name = "DEP_BOSS_CODE", length = 6)
	public String getDepBossCode() {
		return this.depBossCode;
	}

	public void setDepBossCode(String depBossCode) {
		this.depBossCode = depBossCode;
	}

	@Column(name = "DEP_IDEA", length = 100)
	public String getDepIdea() {
		return this.depIdea;
	}

	public void setDepIdea(String depIdea) {
		this.depIdea = depIdea;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DEP_SIGN_DATE", length = 7)
	public Date getDepSignDate() {
		return this.depSignDate;
	}

	public void setDepSignDate(Date depSignDate) {
		this.depSignDate = depSignDate;
	}

	@Column(name = "VICE_BOSS_CODE", length = 6)
	public String getViceBossCode() {
		return this.viceBossCode;
	}

	public void setViceBossCode(String viceBossCode) {
		this.viceBossCode = viceBossCode;
	}

	@Column(name = "VICE_BOSS_IDEA", length = 100)
	public String getViceBossIdea() {
		return this.viceBossIdea;
	}

	public void setViceBossIdea(String viceBossIdea) {
		this.viceBossIdea = viceBossIdea;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "VICE_BOSS_SIGN_DATE", length = 7)
	public Date getViceBossSignDate() {
		return this.viceBossSignDate;
	}

	public void setViceBossSignDate(Date viceBossSignDate) {
		this.viceBossSignDate = viceBossSignDate;
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

	@Column(name = "REPAIR_STATUS", length = 1)
	public String getRepairStatus() {
		return this.repairStatus;
	}

	public void setRepairStatus(String repairStatus) {
		this.repairStatus = repairStatus;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}