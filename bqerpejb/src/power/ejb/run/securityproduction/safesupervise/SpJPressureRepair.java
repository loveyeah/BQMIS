package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJPressureRepair entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_PRESSURE_REPAIR", schema = "POWER")
public class SpJPressureRepair implements java.io.Serializable {

	// Fields

	private Long boilerRepairId;
	private Long boilerId;
	private String type;
	private Date repairBegin;
	private Date repairEnd;
	private String reportNo;
	private String repairUnit;
	private String repairResult;
	private Date nextTime;
	private String isUse;
	private String enterpriseCode;
	private String fillBy;//add by ltong
	private Date fillTime;//add by ltong

	// Constructors

	@Column(name = "FILL_BY", length = 20)
	public String getFillBy() {
		return fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FILL_TIME", length = 7)
	public Date getFillTime() {
		return fillTime;
	}

	public void setFillTime(Date fillTime) {
		this.fillTime = fillTime;
	}

	/** default constructor */
	public SpJPressureRepair() {
	}

	/** minimal constructor */
	public SpJPressureRepair(Long boilerRepairId) {
		this.boilerRepairId = boilerRepairId;
	}

	/** full constructor */
	public SpJPressureRepair(Long boilerRepairId, Long boilerId, String type,
			Date repairBegin, Date repairEnd, String reportNo,
			String repairUnit, String repairResult, Date nextTime,
			String isUse, String enterpriseCode) {
		this.boilerRepairId = boilerRepairId;
		this.boilerId = boilerId;
		this.type = type;
		this.repairBegin = repairBegin;
		this.repairEnd = repairEnd;
		this.reportNo = reportNo;
		this.repairUnit = repairUnit;
		this.repairResult = repairResult;
		this.nextTime = nextTime;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BOILER_REPAIR_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBoilerRepairId() {
		return this.boilerRepairId;
	}

	public void setBoilerRepairId(Long boilerRepairId) {
		this.boilerRepairId = boilerRepairId;
	}

	@Column(name = "BOILER_ID", precision = 10, scale = 0)
	public Long getBoilerId() {
		return this.boilerId;
	}

	public void setBoilerId(Long boilerId) {
		this.boilerId = boilerId;
	}

	@Column(name = "TYPE", length = 1)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REPAIR_BEGIN", length = 7)
	public Date getRepairBegin() {
		return this.repairBegin;
	}

	public void setRepairBegin(Date repairBegin) {
		this.repairBegin = repairBegin;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REPAIR_END", length = 7)
	public Date getRepairEnd() {
		return this.repairEnd;
	}

	public void setRepairEnd(Date repairEnd) {
		this.repairEnd = repairEnd;
	}

	@Column(name = "REPORT_NO", length = 100)
	public String getReportNo() {
		return this.reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	@Column(name = "REPAIR_UNIT", length = 200)
	public String getRepairUnit() {
		return this.repairUnit;
	}

	public void setRepairUnit(String repairUnit) {
		this.repairUnit = repairUnit;
	}

	@Column(name = "REPAIR_RESULT", length = 1)
	public String getRepairResult() {
		return this.repairResult;
	}

	public void setRepairResult(String repairResult) {
		this.repairResult = repairResult;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "NEXT_TIME", length = 7)
	public Date getNextTime() {
		return this.nextTime;
	}

	public void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
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