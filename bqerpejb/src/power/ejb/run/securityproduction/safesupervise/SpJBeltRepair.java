package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJBeltRepair entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_BELT_REPAIR", schema = "POWER")
public class SpJBeltRepair implements java.io.Serializable {

	// Fields

	private Long repairId;
	private Long toolId;
	private String useTime;
	private String beltNumber;
	private String repairResult;
	private String belongDep;
	private String repairBy;
	private String repairDep;
	private Date repairBegin;
	private Date repairEnd;
	private Date nextTime;
	private String memo;
	private String isUse;
	private String enterpriseCode;
	private String fillBy;
	private Date fillTime;

	// Constructors

	/** default constructor */
	public SpJBeltRepair() {
	}

	/** minimal constructor */
	public SpJBeltRepair(Long repairId) {
		this.repairId = repairId;
	}

	/** full constructor */
	public SpJBeltRepair(Long repairId, Long toolId, String useTime,
			String beltNumber, String repairResult, String belongDep,
			String repairBy, String repairDep, Date repairBegin,
			Date repairEnd, Date nextTime, String memo, String isUse,
			String enterpriseCode, String fillBy, Date fillTime) {
		this.repairId = repairId;
		this.toolId = toolId;
		this.useTime = useTime;
		this.beltNumber = beltNumber;
		this.repairResult = repairResult;
		this.belongDep = belongDep;
		this.repairBy = repairBy;
		this.repairDep = repairDep;
		this.repairBegin = repairBegin;
		this.repairEnd = repairEnd;
		this.nextTime = nextTime;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.fillBy = fillBy;
		this.fillTime = fillTime;
	}

	// Property accessors
	@Id
	@Column(name = "REPAIR_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRepairId() {
		return this.repairId;
	}

	public void setRepairId(Long repairId) {
		this.repairId = repairId;
	}

	@Column(name = "TOOL_ID", precision = 10, scale = 0)
	public Long getToolId() {
		return this.toolId;
	}

	public void setToolId(Long toolId) {
		this.toolId = toolId;
	}

	@Column(name = "USE_TIME", length = 20)
	public String getUseTime() {
		return this.useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}

	@Column(name = "BELT_NUMBER", length = 20)
	public String getBeltNumber() {
		return this.beltNumber;
	}

	public void setBeltNumber(String beltNumber) {
		this.beltNumber = beltNumber;
	}

	@Column(name = "REPAIR_RESULT", length = 200)
	public String getRepairResult() {
		return this.repairResult;
	}

	public void setRepairResult(String repairResult) {
		this.repairResult = repairResult;
	}

	@Column(name = "BELONG_DEP", length = 20)
	public String getBelongDep() {
		return this.belongDep;
	}

	public void setBelongDep(String belongDep) {
		this.belongDep = belongDep;
	}

	@Column(name = "REPAIR_BY", length = 20)
	public String getRepairBy() {
		return this.repairBy;
	}

	public void setRepairBy(String repairBy) {
		this.repairBy = repairBy;
	}

	@Column(name = "REPAIR_DEP", length = 20)
	public String getRepairDep() {
		return this.repairDep;
	}

	public void setRepairDep(String repairDep) {
		this.repairDep = repairDep;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "NEXT_TIME", length = 7)
	public Date getNextTime() {
		return this.nextTime;
	}

	public void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
	}

	@Column(name = "MEMO", length = 500)
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

	@Column(name = "FILL_BY", length = 20)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FILL_TIME", length = 7)
	public Date getFillTime() {
		return this.fillTime;
	}

	public void setFillTime(Date fillTime) {
		this.fillTime = fillTime;
	}

}