package power.ejb.equ.workbill;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquJWo entity.
 * 
 * @author slTang
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_J_WO")
public class EquJWo implements java.io.Serializable {

	// Fields

	private Long woId;
	private String woCodeShow;
	private String woCode;
	private String faWoCode;
	private String workorderContent;
	private String workorderStatus;
	private String workorderType;
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
	private String ifContact;
	private String ifConform;
	private String ifRemove;
	private String ifCrane;
	private String ifFalsework;
	private String ifSafety;
	private String projectNum;
	private Date requireStarttime;
	private Date requireEndtime;
	private Date planStarttime;
	private Date planEndtime;
	private Date factStarttime;
	private Date factEndtime;
	private String repairDepartment;
	private String workChargeCode;
	private String professionHeader;
	private String requireManCode;
	private Date requireTime;
	private String checkManCode;
	private Date checkTime;
	private String checkReportid;
	private String checkResultid;
	private String checkReasonid;
	private Double requireWotime;
	private Double requireWofee;
	private Long workFlowNo;
	private Long wfState;
	private String assembly;
	private Double planWotime;
	private Double planWofee;
	private Double factWotime;
	private Double factWofee;
	private String enterprisecode;
	private String ifUse;

	// Constructors

	/** default constructor */
	public EquJWo() {
	}

	/** minimal constructor */
	public EquJWo(Long woId, String woCode, String faWoCode) {
		this.woId = woId;
		this.woCode = woCode;
//		this.woCodeShow = woCodeShow;
		this.faWoCode = faWoCode;
	}

	/** full constructor */
	public EquJWo(Long woId,String woCodeShow, String woCode,String faWoCode,
			String workorderContent, String workorderStatus,
			String workorderType, String professionCode, String crewId,
			String maintDep, String repairModel, String repairmethodCode,
			String filepackageCode, String relationFilepackageMemo,
			String ifWorkticket, String equCode, String kksCode,
			String equName, String equPostionCode, String remark,
			String ifOutside, String ifFireticket, String ifMaterials,
			String ifReport, String ifContact, String ifConform,
			String ifRemove, String ifCrane, String ifFalsework,
			String ifSafety, String projectNum, Date requireStarttime,
			Date requireEndtime, Date planStarttime, Date planEndtime,
			Date factStarttime, Date factEndtime, String repairDepartment,
			String workChargeCode, String professionHeader,
			String requireManCode, Date requireTime, String checkManCode,
			Date checkTime, String checkReportid, String checkResultid,
			String checkReasonid, Double requireWotime, Double requireWofee,
			Long workFlowNo, Long wfState, String assembly, Double planWotime,
			Double planWofee, Double factWotime, Double factWofee,
			String enterprisecode, String ifUse) {
		this.woId = woId;
		this.woCodeShow = woCodeShow;
		this.woCode = woCode;
		this.faWoCode = faWoCode;
		this.workorderContent = workorderContent;
		this.workorderStatus = workorderStatus;
		this.workorderType = workorderType;
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
		this.ifContact = ifContact;
		this.ifConform = ifConform;
		this.ifRemove = ifRemove;
		this.ifCrane = ifCrane;
		this.ifFalsework = ifFalsework;
		this.ifSafety = ifSafety;
		this.projectNum = projectNum;
		this.requireStarttime = requireStarttime;
		this.requireEndtime = requireEndtime;
		this.planStarttime = planStarttime;
		this.planEndtime = planEndtime;
		this.factStarttime = factStarttime;
		this.factEndtime = factEndtime;
		this.repairDepartment = repairDepartment;
		this.workChargeCode = workChargeCode;
		this.professionHeader = professionHeader;
		this.requireManCode = requireManCode;
		this.requireTime = requireTime;
		this.checkManCode = checkManCode;
		this.checkTime = checkTime;
		this.checkReportid = checkReportid;
		this.checkResultid = checkResultid;
		this.checkReasonid = checkReasonid;
		this.requireWotime = requireWotime;
		this.requireWofee = requireWofee;
		this.workFlowNo = workFlowNo;
		this.wfState = wfState;
		this.assembly = assembly;
		this.planWotime = planWotime;
		this.planWofee = planWofee;
		this.factWotime = factWotime;
		this.factWofee = factWofee;
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

	@Column(name = "FA_WO_CODE", length = 20)
	public String getFaWoCode() {
		return this.faWoCode;
	}

	public void setFaWoCode(String faWoCode) {
		this.faWoCode = faWoCode;
	}
	
	@Column(name = "WO_CODE_SHOW", nullable = false, length = 20)
	public String getWoCodeShow() {
		return woCodeShow;
	}

	public void setWoCodeShow(String woCodeShow) {
		this.woCodeShow = woCodeShow;
	}
	
	@Column(name = "WORKORDER_CONTENT", length = 4000)
	public String getWorkorderContent() {
		return this.workorderContent;
	}

	public void setWorkorderContent(String workorderContent) {
		this.workorderContent = workorderContent;
	}

	@Column(name = "WORKORDER_STATUS", length = 20)
	public String getWorkorderStatus() {
		return this.workorderStatus;
	}

	public void setWorkorderStatus(String workorderStatus) {
		this.workorderStatus = workorderStatus;
	}

	@Column(name = "WORKORDER_TYPE", length = 1)
	public String getWorkorderType() {
		return this.workorderType;
	}

	public void setWorkorderType(String workorderType) {
		this.workorderType = workorderType;
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

	@Column(name = "IF_CONTACT", length = 1)
	public String getIfContact() {
		return this.ifContact;
	}

	public void setIfContact(String ifContact) {
		this.ifContact = ifContact;
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

	@Column(name = "IF_SAFETY", length = 1)
	public String getIfSafety() {
		return this.ifSafety;
	}

	public void setIfSafety(String ifSafety) {
		this.ifSafety = ifSafety;
	}

	@Column(name = "PROJECT_NUM", length = 50)
	public String getProjectNum() {
		return this.projectNum;
	}

	public void setProjectNum(String projectNum) {
		this.projectNum = projectNum;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUIRE_STARTTIME", length = 7)
	public Date getRequireStarttime() {
		return this.requireStarttime;
	}

	public void setRequireStarttime(Date requireStarttime) {
		this.requireStarttime = requireStarttime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUIRE_ENDTIME", length = 7)
	public Date getRequireEndtime() {
		return this.requireEndtime;
	}

	public void setRequireEndtime(Date requireEndtime) {
		this.requireEndtime = requireEndtime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PLAN_STARTTIME", length = 7)
	public Date getPlanStarttime() {
		return this.planStarttime;
	}

	public void setPlanStarttime(Date planStarttime) {
		this.planStarttime = planStarttime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PLAN_ENDTIME", length = 7)
	public Date getPlanEndtime() {
		return this.planEndtime;
	}

	public void setPlanEndtime(Date planEndtime) {
		this.planEndtime = planEndtime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FACT_STARTTIME", length = 7)
	public Date getFactStarttime() {
		return this.factStarttime;
	}

	public void setFactStarttime(Date factStarttime) {
		this.factStarttime = factStarttime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FACT_ENDTIME", length = 7)
	public Date getFactEndtime() {
		return this.factEndtime;
	}

	public void setFactEndtime(Date factEndtime) {
		this.factEndtime = factEndtime;
	}

	@Column(name = "REPAIR_DEPARTMENT", length = 30)
	public String getRepairDepartment() {
		return this.repairDepartment;
	}

	public void setRepairDepartment(String repairDepartment) {
		this.repairDepartment = repairDepartment;
	}

	@Column(name = "WORK_CHARGE_CODE", length = 30)
	public String getWorkChargeCode() {
		return this.workChargeCode;
	}

	public void setWorkChargeCode(String workChargeCode) {
		this.workChargeCode = workChargeCode;
	}

	@Column(name = "PROFESSION_HEADER", length = 30)
	public String getProfessionHeader() {
		return this.professionHeader;
	}

	public void setProfessionHeader(String professionHeader) {
		this.professionHeader = professionHeader;
	}

	@Column(name = "REQUIRE_MAN_CODE", length = 30)
	public String getRequireManCode() {
		return this.requireManCode;
	}

	public void setRequireManCode(String requireManCode) {
		this.requireManCode = requireManCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUIRE_TIME", length = 7)
	public Date getRequireTime() {
		return this.requireTime;
	}

	public void setRequireTime(Date requireTime) {
		this.requireTime = requireTime;
	}

	@Column(name = "CHECK_MAN_CODE", length = 30)
	public String getCheckManCode() {
		return this.checkManCode;
	}

	public void setCheckManCode(String checkManCode) {
		this.checkManCode = checkManCode;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECK_TIME", length = 7)
	public Date getCheckTime() {
		return this.checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	@Column(name = "CHECK_REPORTID", length = 1000)
	public String getCheckReportid() {
		return this.checkReportid;
	}

	public void setCheckReportid(String checkReportid) {
		this.checkReportid = checkReportid;
	}

	@Column(name = "CHECK_RESULTID", length = 1000)
	public String getCheckResultid() {
		return this.checkResultid;
	}

	public void setCheckResultid(String checkResultid) {
		this.checkResultid = checkResultid;
	}

	@Column(name = "CHECK_REASONID", length = 1000)
	public String getCheckReasonid() {
		return this.checkReasonid;
	}

	public void setCheckReasonid(String checkReasonid) {
		this.checkReasonid = checkReasonid;
	}

	@Column(name = "REQUIRE_WOTIME", precision = 18, scale = 5)
	public Double getRequireWotime() {
		return this.requireWotime;
	}

	public void setRequireWotime(Double requireWotime) {
		this.requireWotime = requireWotime;
	}

	@Column(name = "REQUIRE_WOFEE", precision = 18, scale = 5)
	public Double getRequireWofee() {
		return this.requireWofee;
	}

	public void setRequireWofee(Double requireWofee) {
		this.requireWofee = requireWofee;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "WF_STATE", precision = 11, scale = 0)
	public Long getWfState() {
		return this.wfState;
	}

	public void setWfState(Long wfState) {
		this.wfState = wfState;
	}

	@Column(name = "ASSEMBLY", length = 50)
	public String getAssembly() {
		return this.assembly;
	}

	public void setAssembly(String assembly) {
		this.assembly = assembly;
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

	@Column(name = "FACT_WOTIME", precision = 18, scale = 5)
	public Double getFactWotime() {
		return this.factWotime;
	}

	public void setFactWotime(Double factWotime) {
		this.factWotime = factWotime;
	}

	@Column(name = "FACT_WOFEE", precision = 18, scale = 5)
	public Double getFactWofee() {
		return this.factWofee;
	}

	public void setFactWofee(Double factWofee) {
		this.factWofee = factWofee;
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