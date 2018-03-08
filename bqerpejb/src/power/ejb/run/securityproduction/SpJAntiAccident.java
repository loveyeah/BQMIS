package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJAntiAccident entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_ANTI_ACCIDENT", schema = "POWER")
public class SpJAntiAccident implements java.io.Serializable {

	// Fields

	private String measureCode;
	private String measureName;
	private String specialCode;
	private String fdDutyLeader;
	private String fdManager;
	private String fdTechnologyBy;
	private String fdSuperviseBy;
	private String dtDutyLeader;
	private String dtManager;
	private String dtTechnologyBy;
	private String dtSuperviseBy;
	private String entryBy;
	private String entryDept;
	private Date entryDate;
	private String memo;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public SpJAntiAccident() {
	}

	/** minimal constructor */
	public SpJAntiAccident(String measureCode) {
		this.measureCode = measureCode;
	}

	/** full constructor */
	public SpJAntiAccident(String measureCode, String measureName,
			String specialCode, String fdDutyLeader, String fdManager,
			String fdTechnologyBy, String fdSuperviseBy, String dtDutyLeader,
			String dtManager, String dtTechnologyBy, String dtSuperviseBy,
			String entryBy, String entryDept, Date entryDate, String memo,
			String enterpriseCode, String isUse) {
		this.measureCode = measureCode;
		this.measureName = measureName;
		this.specialCode = specialCode;
		this.fdDutyLeader = fdDutyLeader;
		this.fdManager = fdManager;
		this.fdTechnologyBy = fdTechnologyBy;
		this.fdSuperviseBy = fdSuperviseBy;
		this.dtDutyLeader = dtDutyLeader;
		this.dtManager = dtManager;
		this.dtTechnologyBy = dtTechnologyBy;
		this.dtSuperviseBy = dtSuperviseBy;
		this.entryBy = entryBy;
		this.entryDept = entryDept;
		this.entryDate = entryDate;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "MEASURE_CODE", unique = true, nullable = false, length = 6)
	public String getMeasureCode() {
		return this.measureCode;
	}

	public void setMeasureCode(String measureCode) {
		this.measureCode = measureCode;
	}

	@Column(name = "MEASURE_NAME", length = 500)
	public String getMeasureName() {
		return this.measureName;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	@Column(name = "SPECIAL_CODE", length = 10)
	public String getSpecialCode() {
		return this.specialCode;
	}

	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}

	@Column(name = "FD_DUTY_LEADER", length = 300)
	public String getFdDutyLeader() {
		return this.fdDutyLeader;
	}

	public void setFdDutyLeader(String fdDutyLeader) {
		this.fdDutyLeader = fdDutyLeader;
	}

	@Column(name = "FD_MANAGER", length = 30)
	public String getFdManager() {
		return this.fdManager;
	}

	public void setFdManager(String fdManager) {
		this.fdManager = fdManager;
	}

	@Column(name = "FD_TECHNOLOGY_BY", length = 30)
	public String getFdTechnologyBy() {
		return this.fdTechnologyBy;
	}

	public void setFdTechnologyBy(String fdTechnologyBy) {
		this.fdTechnologyBy = fdTechnologyBy;
	}

	@Column(name = "FD_SUPERVISE_BY", length = 30)
	public String getFdSuperviseBy() {
		return this.fdSuperviseBy;
	}

	public void setFdSuperviseBy(String fdSuperviseBy) {
		this.fdSuperviseBy = fdSuperviseBy;
	}

	@Column(name = "DT_DUTY_LEADER", length = 30)
	public String getDtDutyLeader() {
		return this.dtDutyLeader;
	}

	public void setDtDutyLeader(String dtDutyLeader) {
		this.dtDutyLeader = dtDutyLeader;
	}

	@Column(name = "DT_MANAGER", length = 30)
	public String getDtManager() {
		return this.dtManager;
	}

	public void setDtManager(String dtManager) {
		this.dtManager = dtManager;
	}

	@Column(name = "DT_TECHNOLOGY_BY", length = 30)
	public String getDtTechnologyBy() {
		return this.dtTechnologyBy;
	}

	public void setDtTechnologyBy(String dtTechnologyBy) {
		this.dtTechnologyBy = dtTechnologyBy;
	}

	@Column(name = "DT_SUPERVISE_BY", length = 30)
	public String getDtSuperviseBy() {
		return this.dtSuperviseBy;
	}

	public void setDtSuperviseBy(String dtSuperviseBy) {
		this.dtSuperviseBy = dtSuperviseBy;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Column(name = "ENTRY_DEPT", length = 20)
	public String getEntryDept() {
		return this.entryDept;
	}

	public void setEntryDept(String entryDept) {
		this.entryDept = entryDept;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}