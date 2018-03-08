package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJToolsRepair entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_TOOLS_REPAIR", schema = "POWER")
public class SpJToolsRepair implements java.io.Serializable {

	// Fields

	private Long repairId;
	private Long toolId;
	private String belongDep;
	private String repairResult;
	private Date repairBegin;
	private Date repairEnd;
	private String repairBy;
	private String repairDep;
	private Date nextTime;
	private String memo;
	private String isUse;
	private String enterpriseCode;
	
	private String fillBy;

	// Constructors


	/** default constructor */
	public SpJToolsRepair() {
	}

	/** minimal constructor */
	public SpJToolsRepair(Long repairId) {
		this.repairId = repairId;
	}

	/** full constructor */
	public SpJToolsRepair(Long repairId, Long toolId, String belongDep,
			String repairResult, Date repairBegin, Date repairEnd,
			String repairBy, String repairDep, Date nextTime, String memo,
			String isUse, String enterpriseCode) {
		this.repairId = repairId;
		this.toolId = toolId;
		this.belongDep = belongDep;
		this.repairResult = repairResult;
		this.repairBegin = repairBegin;
		this.repairEnd = repairEnd;
		this.repairBy = repairBy;
		this.repairDep = repairDep;
		this.nextTime = nextTime;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
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

	@Column(name = "BELONG_DEP", length = 20)
	public String getBelongDep() {
		return this.belongDep;
	}

	public void setBelongDep(String belongDep) {
		this.belongDep = belongDep;
	}

	@Column(name = "REPAIR_RESULT", length = 200)
	public String getRepairResult() {
		return this.repairResult;
	}

	public void setRepairResult(String repairResult) {
		this.repairResult = repairResult;
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
		return fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

}