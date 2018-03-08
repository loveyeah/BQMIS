package power.ejb.hr.archives;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCPunish entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_PUNISH")
public class HrCPunish implements java.io.Serializable {

	// Fields

	private Long punishinfId;
	private Long empId;
	private String punishName;
	private Date punishTime;
	private String punishUnit;
	private Date punishExecuteTime;
	private Date punishEndTime;
	private String  punishDeadline;
	private String punishReason;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCPunish() {
	}

	/** minimal constructor */
	public HrCPunish(Long punishinfId) {
		this.punishinfId = punishinfId;
	}

	/** full constructor */
	public HrCPunish(Long punishinfId, Long empId, String punishName,
			Date punishTime, String punishUnit, Date punishExecuteTime,
			Date punishEndTime, String punishDeadline, String punishReason,
			String memo, String isUse, String enterpriseCode) {
		this.punishinfId = punishinfId;
		this.empId = empId;
		this.punishName = punishName;
		this.punishTime = punishTime;
		this.punishUnit = punishUnit;
		this.punishExecuteTime = punishExecuteTime;
		this.punishEndTime = punishEndTime;
		this.punishDeadline = punishDeadline;
		this.punishReason = punishReason;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "PUNISHINF_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPunishinfId() {
		return this.punishinfId;
	}

	public void setPunishinfId(Long punishinfId) {
		this.punishinfId = punishinfId;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "PUNISH_NAME", length = 50)
	public String getPunishName() {
		return this.punishName;
	}

	public void setPunishName(String punishName) {
		this.punishName = punishName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PUNISH_TIME", length = 7)
	public Date getPunishTime() {
		return this.punishTime;
	}

	public void setPunishTime(Date punishTime) {
		this.punishTime = punishTime;
	}

	@Column(name = "PUNISH_UNIT", length = 30)
	public String getPunishUnit() {
		return this.punishUnit;
	}

	public void setPunishUnit(String punishUnit) {
		this.punishUnit = punishUnit;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PUNISH_EXECUTE_TIME", length = 7)
	public Date getPunishExecuteTime() {
		return this.punishExecuteTime;
	}

	public void setPunishExecuteTime(Date punishExecuteTime) {
		this.punishExecuteTime = punishExecuteTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PUNISH_END_TIME", length = 7)
	public Date getPunishEndTime() {
		return this.punishEndTime;
	}

	public void setPunishEndTime(Date punishEndTime) {
		this.punishEndTime = punishEndTime;
	}

	@Column(name = "PUNISH_DEADLINE", length = 20)
	public String getPunishDeadline() {
		return this.punishDeadline;
	}

	public void setPunishDeadline(String punishDeadline) {
		this.punishDeadline = punishDeadline;
	}

	@Column(name = "PUNISH_REASON", length = 100)
	public String getPunishReason() {
		return this.punishReason;
	}

	public void setPunishReason(String punishReason) {
		this.punishReason = punishReason;
	}

	@Column(name = "MEMO", length = 200)
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