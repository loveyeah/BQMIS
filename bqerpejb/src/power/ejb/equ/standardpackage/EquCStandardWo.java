package power.ejb.equ.standardpackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCStandardWo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_STANDARD_WO")
public class EquCStandardWo implements java.io.Serializable {

	// Fields

	private Long woId;
	private String woCode;
	private String jobCode;
	private String workorderTitle;
	private String workorderMemo;
	private String professionCode;
	private String crewId;
	private String maintDep;
	private String repairModel;
	private String repairmethodCode;
	private String filepackageCode;
	private String relationFilepackageMemo;
	private String ifWorkticket;
	private String equCode;
	private String kksCode;
	private String equName;
	private String equPostionCode;
	private String remark;
	private String ifOutside;
	private String ifFireticket;
	private String ifMaterials;
	private String ifReport;
	private String ifConform;
	private String ifRemove;
	private String ifCrane;
	private String ifFalsework;
	private Double planWotime;
	private Double planWofee;
	private Long orderby;
	private String status;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquCStandardWo() {
	}

	/** minimal constructor */
	public EquCStandardWo(Long woId, String woCode) {
		this.woId = woId;
		this.woCode = woCode;
	}

	/** full constructor */
	public EquCStandardWo(Long woId, String woCode, String jobCode,
			String workorderTitle, String workorderMemo, String professionCode,
			String crewId, String maintDep, String repairModel,
			String repairmethodCode, String filepackageCode,
			String relationFilepackageMemo, String ifWorkticket,
			String equCode, String kksCode, String equName,
			String equPostionCode, String remark, String ifOutside,
			String ifFireticket, String ifMaterials, String ifReport,
			String ifConform, String ifRemove, String ifCrane,
			String ifFalsework, Double planWotime, Double planWofee,
			Long orderby, String status, String enterprisecode, String ifUse) {
		this.woId = woId;
		this.woCode = woCode;
		this.jobCode = jobCode;
		this.workorderTitle = workorderTitle;
		this.workorderMemo = workorderMemo;
		this.professionCode = professionCode;
		this.crewId = crewId;
		this.maintDep = maintDep;
		this.repairModel = repairModel;
		this.repairmethodCode = repairmethodCode;
		this.filepackageCode = filepackageCode;
		this.relationFilepackageMemo = relationFilepackageMemo;
		this.ifWorkticket = ifWorkticket;
		this.equCode = equCode;
		this.kksCode = kksCode;
		this.equName = equName;
		this.equPostionCode = equPostionCode;
		this.remark = remark;
		this.ifOutside = ifOutside;
		this.ifFireticket = ifFireticket;
		this.ifMaterials = ifMaterials;
		this.ifReport = ifReport;
		this.ifConform = ifConform;
		this.ifRemove = ifRemove;
		this.ifCrane = ifCrane;
		this.ifFalsework = ifFalsework;
		this.planWotime = planWotime;
		this.planWofee = planWofee;
		this.orderby = orderby;
		this.status = status;
		this.enterprisecode = enterprisecode;
		this.ifUse = ifUse;
	}

	// Property accessors
	@Id
	@Column(name = "WO_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getWoId() {
		return this.woId;
	}

	public void setWoId(Long woId) {
		this.woId = woId;
	}

	@Column(name = "WO_CODE", nullable = false, length = 20)
	public String getWoCode() {
		return this.woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	@Column(name = "JOB_CODE", length = 20)
	public String getJobCode() {
		return this.jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	@Column(name = "WORKORDER_TITLE", length = 100)
	public String getWorkorderTitle() {
		return this.workorderTitle;
	}

	public void setWorkorderTitle(String workorderTitle) {
		this.workorderTitle = workorderTitle;
	}

	@Column(name = "WORKORDER_MEMO", length = 4000)
	public String getWorkorderMemo() {
		return this.workorderMemo;
	}

	public void setWorkorderMemo(String workorderMemo) {
		this.workorderMemo = workorderMemo;
	}

	@Column(name = "PROFESSION_CODE", length = 20)
	public String getProfessionCode() {
		return this.professionCode;
	}

	public void setProfessionCode(String professionCode) {
		this.professionCode = professionCode;
	}

	@Column(name = "CREW_ID", length = 10)
	public String getCrewId() {
		return this.crewId;
	}

	public void setCrewId(String crewId) {
		this.crewId = crewId;
	}

	@Column(name = "MAINT_DEP", length = 20)
	public String getMaintDep() {
		return this.maintDep;
	}

	public void setMaintDep(String maintDep) {
		this.maintDep = maintDep;
	}

	@Column(name = "REPAIR_MODEL", length = 20)
	public String getRepairModel() {
		return this.repairModel;
	}

	public void setRepairModel(String repairModel) {
		this.repairModel = repairModel;
	}

	@Column(name = "REPAIRMETHOD_CODE", length = 20)
	public String getRepairmethodCode() {
		return this.repairmethodCode;
	}

	public void setRepairmethodCode(String repairmethodCode) {
		this.repairmethodCode = repairmethodCode;
	}

	@Column(name = "FILEPACKAGE_CODE", length = 20)
	public String getFilepackageCode() {
		return this.filepackageCode;
	}

	public void setFilepackageCode(String filepackageCode) {
		this.filepackageCode = filepackageCode;
	}

	@Column(name = "RELATION_FILEPACKAGE_MEMO", length = 100)
	public String getRelationFilepackageMemo() {
		return this.relationFilepackageMemo;
	}

	public void setRelationFilepackageMemo(String relationFilepackageMemo) {
		this.relationFilepackageMemo = relationFilepackageMemo;
	}

	@Column(name = "IF_WORKTICKET", length = 1)
	public String getIfWorkticket() {
		return this.ifWorkticket;
	}

	public void setIfWorkticket(String ifWorkticket) {
		this.ifWorkticket = ifWorkticket;
	}

	@Column(name = "EQU_CODE", length = 30)
	public String getEquCode() {
		return this.equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

	@Column(name = "KKS_CODE", length = 30)
	public String getKksCode() {
		return this.kksCode;
	}

	public void setKksCode(String kksCode) {
		this.kksCode = kksCode;
	}

	@Column(name = "EQU_NAME", length = 100)
	public String getEquName() {
		return this.equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	@Column(name = "EQU_POSTION_CODE", length = 30)
	public String getEquPostionCode() {
		return this.equPostionCode;
	}

	public void setEquPostionCode(String equPostionCode) {
		this.equPostionCode = equPostionCode;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "IF_OUTSIDE", length = 1)
	public String getIfOutside() {
		return this.ifOutside;
	}

	public void setIfOutside(String ifOutside) {
		this.ifOutside = ifOutside;
	}

	@Column(name = "IF_FIRETICKET", length = 1)
	public String getIfFireticket() {
		return this.ifFireticket;
	}

	public void setIfFireticket(String ifFireticket) {
		this.ifFireticket = ifFireticket;
	}

	@Column(name = "IF_MATERIALS", length = 1)
	public String getIfMaterials() {
		return this.ifMaterials;
	}

	public void setIfMaterials(String ifMaterials) {
		this.ifMaterials = ifMaterials;
	}

	@Column(name = "IF_REPORT", length = 1)
	public String getIfReport() {
		return this.ifReport;
	}

	public void setIfReport(String ifReport) {
		this.ifReport = ifReport;
	}

	@Column(name = "IF_CONFORM", length = 1)
	public String getIfConform() {
		return this.ifConform;
	}

	public void setIfConform(String ifConform) {
		this.ifConform = ifConform;
	}

	@Column(name = "IF_REMOVE", length = 1)
	public String getIfRemove() {
		return this.ifRemove;
	}

	public void setIfRemove(String ifRemove) {
		this.ifRemove = ifRemove;
	}

	@Column(name = "IF_CRANE", length = 1)
	public String getIfCrane() {
		return this.ifCrane;
	}

	public void setIfCrane(String ifCrane) {
		this.ifCrane = ifCrane;
	}

	@Column(name = "IF_FALSEWORK", length = 1)
	public String getIfFalsework() {
		return this.ifFalsework;
	}

	public void setIfFalsework(String ifFalsework) {
		this.ifFalsework = ifFalsework;
	}

	@Column(name = "PLAN_WOTIME", precision = 18, scale = 5)
	public Double getPlanWotime() {
		return this.planWotime;
	}

	public void setPlanWotime(Double planWotime) {
		this.planWotime = planWotime;
	}

	@Column(name = "PLAN_WOFEE", precision = 18, scale = 5)
	public Double getPlanWofee() {
		return this.planWofee;
	}

	public void setPlanWofee(Double planWofee) {
		this.planWofee = planWofee;
	}

	@Column(name = "ORDERBY", precision = 10, scale = 0)
	public Long getOrderby() {
		return this.orderby;
	}

	public void setOrderby(Long orderby) {
		this.orderby = orderby;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ENTERPRISECODE", length = 20)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

	@Column(name = "IF_USE", length = 1)
	public String getIfUse() {
		return this.ifUse;
	}

	public void setIfUse(String ifUse) {
		this.ifUse = ifUse;
	}

}