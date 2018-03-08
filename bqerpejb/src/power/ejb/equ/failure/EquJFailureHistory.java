package power.ejb.equ.failure;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquJFailureHistory entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_J_FAILURE_HISTORY")
public class EquJFailureHistory implements java.io.Serializable {

	// Fields

	private Long id;
	private String failureCode;
	private String approveType;
	private Date approveTime;
	private String approveOpinion;
	private String approvePeople;
	private String arbitrateType;
	private String arbitrateDept;
	private String arbitrateProfession;
	private String checkArbitrateType;
	private String arbitrateKind;
	private String awaitType;
	private Date delayDate;
	private Date awaitappoDate;
	private String eliminateType;
	private String eliminateClass;
	private String tackleResult;
	private String chargeMan;
	private String approveRemark;
	private String chargerLeader;
	private String checkMan;
	private String checkQuality;
	private String enterpriseCode;
	private String isuse;

	// Constructors

	/** default constructor */
	public EquJFailureHistory() {
	}

	/** minimal constructor */
	public EquJFailureHistory(Long id) {
		this.id = id;
	}

	/** full constructor */
	public EquJFailureHistory(Long id, String failureCode, String approveType,
			Date approveTime, String approveOpinion, String approvePeople,
			String arbitrateType, String arbitrateDept,
			String arbitrateProfession, String checkArbitrateType,
			String arbitrateKind, String awaitType, Date delayDate,
			Date awaitappoDate, String eliminateType, String eliminateClass,
			String tackleResult, String chargeMan, String approveRemark,
			String chargerLeader, String checkMan, String checkQuality,
			String enterpriseCode, String isuse) {
		this.id = id;
		this.failureCode = failureCode;
		this.approveType = approveType;
		this.approveTime = approveTime;
		this.approveOpinion = approveOpinion;
		this.approvePeople = approvePeople;
		this.arbitrateType = arbitrateType;
		this.arbitrateDept = arbitrateDept;
		this.arbitrateProfession = arbitrateProfession;
		this.checkArbitrateType = checkArbitrateType;
		this.arbitrateKind = arbitrateKind;
		this.awaitType = awaitType;
		this.delayDate = delayDate;
		this.awaitappoDate = awaitappoDate;
		this.eliminateType = eliminateType;
		this.eliminateClass = eliminateClass;
		this.tackleResult = tackleResult;
		this.chargeMan = chargeMan;
		this.approveRemark = approveRemark;
		this.chargerLeader = chargerLeader;
		this.checkMan = checkMan;
		this.checkQuality = checkQuality;
		this.enterpriseCode = enterpriseCode;
		this.isuse = isuse;
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

	@Column(name = "FAILURE_CODE", length = 15)
	public String getFailureCode() {
		return this.failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	@Column(name = "APPROVE_TYPE", length = 2)
	public String getApproveType() {
		return this.approveType;
	}

	public void setApproveType(String approveType) {
		this.approveType = approveType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVE_TIME", length = 7)
	public Date getApproveTime() {
		return this.approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	@Column(name = "APPROVE_OPINION", length = 2000)
	public String getApproveOpinion() {
		return this.approveOpinion;
	}

	public void setApproveOpinion(String approveOpinion) {
		this.approveOpinion = approveOpinion;
	}

	@Column(name = "APPROVE_PEOPLE", length = 16)
	public String getApprovePeople() {
		return this.approvePeople;
	}

	public void setApprovePeople(String approvePeople) {
		this.approvePeople = approvePeople;
	}

	@Column(name = "ARBITRATE_TYPE", length = 15)
	public String getArbitrateType() {
		return this.arbitrateType;
	}

	public void setArbitrateType(String arbitrateType) {
		this.arbitrateType = arbitrateType;
	}

	@Column(name = "ARBITRATE_DEPT", length = 20)
	public String getArbitrateDept() {
		return this.arbitrateDept;
	}

	public void setArbitrateDept(String arbitrateDept) {
		this.arbitrateDept = arbitrateDept;
	}

	@Column(name = "ARBITRATE_PROFESSION", length = 20)
	public String getArbitrateProfession() {
		return this.arbitrateProfession;
	}

	public void setArbitrateProfession(String arbitrateProfession) {
		this.arbitrateProfession = arbitrateProfession;
	}

	@Column(name = "CHECK_ARBITRATE_TYPE", length = 10)
	public String getCheckArbitrateType() {
		return this.checkArbitrateType;
	}

	public void setCheckArbitrateType(String checkArbitrateType) {
		this.checkArbitrateType = checkArbitrateType;
	}

	@Column(name = "ARBITRATE_KIND", length = 10)
	public String getArbitrateKind() {
		return this.arbitrateKind;
	}

	public void setArbitrateKind(String arbitrateKind) {
		this.arbitrateKind = arbitrateKind;
	}

	@Column(name = "AWAIT_TYPE", length = 10)
	public String getAwaitType() {
		return this.awaitType;
	}

	public void setAwaitType(String awaitType) {
		this.awaitType = awaitType;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELAY_DATE", length = 7)
	public Date getDelayDate() {
		return this.delayDate;
	}

	public void setDelayDate(Date delayDate) {
		this.delayDate = delayDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AWAITAPPO_DATE", length = 7)
	public Date getAwaitappoDate() {
		return this.awaitappoDate;
	}

	public void setAwaitappoDate(Date awaitappoDate) {
		this.awaitappoDate = awaitappoDate;
	}

	@Column(name = "ELIMINATE_TYPE", length = 4)
	public String getEliminateType() {
		return this.eliminateType;
	}

	public void setEliminateType(String eliminateType) {
		this.eliminateType = eliminateType;
	}

	@Column(name = "ELIMINATE_CLASS", length = 8)
	public String getEliminateClass() {
		return this.eliminateClass;
	}

	public void setEliminateClass(String eliminateClass) {
		this.eliminateClass = eliminateClass;
	}

	@Column(name = "TACKLE_RESULT", length = 50)
	public String getTackleResult() {
		return this.tackleResult;
	}

	public void setTackleResult(String tackleResult) {
		this.tackleResult = tackleResult;
	}

	@Column(name = "CHARGE_MAN", length = 16)
	public String getChargeMan() {
		return this.chargeMan;
	}

	public void setChargeMan(String chargeMan) {
		this.chargeMan = chargeMan;
	}

	@Column(name = "APPROVE_REMARK", length = 2000)
	public String getApproveRemark() {
		return this.approveRemark;
	}

	public void setApproveRemark(String approveRemark) {
		this.approveRemark = approveRemark;
	}

	@Column(name = "CHARGER_LEADER", length = 16)
	public String getChargerLeader() {
		return this.chargerLeader;
	}

	public void setChargerLeader(String chargerLeader) {
		this.chargerLeader = chargerLeader;
	}

	@Column(name = "CHECK_MAN", length = 16)
	public String getCheckMan() {
		return this.checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	@Column(name = "CHECK_QUALITY", length = 20)
	public String getCheckQuality() {
		return this.checkQuality;
	}

	public void setCheckQuality(String checkQuality) {
		this.checkQuality = checkQuality;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "ISUSE", length = 1)
	public String getIsuse() {
		return this.isuse;
	}

	public void setIsuse(String isuse) {
		this.isuse = isuse;
	}

}