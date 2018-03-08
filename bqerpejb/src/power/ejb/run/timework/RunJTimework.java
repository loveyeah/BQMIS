package power.ejb.run.timework;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJTimework entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_J_TIMEWORK", schema = "POWER")
public class RunJTimework implements java.io.Serializable {

	// Fields

	private Long id;
	private String workItemCode;
	private String machprofCode;
	private String workType;
	private String workItemName;
	private Date workDate;
	private String cycle;
	private Long classSequence;
	private String classTeam;
	private Long dutytype;
	private String ifdelay;
	private String delayresult;
	private String delayman;
	private Date delaydate;
	private String delaytype;
	private String opTicket;
	private String workresult;
	private String workExplain;
	private String ifcheck;
	private Date checkdate;
	private String checkman;
	private String checkresult;
	private String ifimage;
	private String imagecode;
	private String ifExplain;
	private String memo;
	private String operator;
	private Date delingDate;
	private Date delayDate;
	private String protector;
	private String ifOpticket;
	private String conman;
	private Date condate;
	private String status;
	private String enterprisecode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunJTimework() {
	}

	/** minimal constructor */
	public RunJTimework(Long id, String workItemCode) {
		this.id = id;
		this.workItemCode = workItemCode;
	}

	/** full constructor */
	public RunJTimework(Long id, String workItemCode, String machprofCode,
			String workType, String workItemName, Date workDate, String cycle,
			Long classSequence, String classTeam, Long dutytype,
			String ifdelay, String delayresult, String delayman,
			Date delaydate, String delaytype, String opTicket,
			String workresult, String workExplain, String ifcheck,
			Date checkdate, String checkman, String checkresult,
			String ifimage, String imagecode, String ifExplain, String memo,
			String operator, Date delingDate, Date delayDate, String protector,
			String ifOpticket, String conman, Date condate, String status,
			String enterprisecode, String isUse) {
		this.id = id;
		this.workItemCode = workItemCode;
		this.machprofCode = machprofCode;
		this.workType = workType;
		this.workItemName = workItemName;
		this.workDate = workDate;
		this.cycle = cycle;
		this.classSequence = classSequence;
		this.classTeam = classTeam;
		this.dutytype = dutytype;
		this.ifdelay = ifdelay;
		this.delayresult = delayresult;
		this.delayman = delayman;
		this.delaydate = delaydate;
		this.delaytype = delaytype;
		this.opTicket = opTicket;
		this.workresult = workresult;
		this.workExplain = workExplain;
		this.ifcheck = ifcheck;
		this.checkdate = checkdate;
		this.checkman = checkman;
		this.checkresult = checkresult;
		this.ifimage = ifimage;
		this.imagecode = imagecode;
		this.ifExplain = ifExplain;
		this.memo = memo;
		this.operator = operator;
		this.delingDate = delingDate;
		this.delayDate = delayDate;
		this.protector = protector;
		this.ifOpticket = ifOpticket;
		this.conman = conman;
		this.condate = condate;
		this.status = status;
		this.enterprisecode = enterprisecode;
		this.isUse = isUse;
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

	@Column(name = "WORK_ITEM_CODE", nullable = false, length = 15)
	public String getWorkItemCode() {
		return this.workItemCode;
	}

	public void setWorkItemCode(String workItemCode) {
		this.workItemCode = workItemCode;
	}

	@Column(name = "MACHPROF_CODE", length = 15)
	public String getMachprofCode() {
		return this.machprofCode;
	}

	public void setMachprofCode(String machprofCode) {
		this.machprofCode = machprofCode;
	}

	@Column(name = "WORK_TYPE", length = 12)
	public String getWorkType() {
		return this.workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	@Column(name = "WORK_ITEM_NAME", length = 100)
	public String getWorkItemName() {
		return this.workItemName;
	}

	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "WORK_DATE", length = 7)
	public Date getWorkDate() {
		return this.workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	@Column(name = "CYCLE", length = 6)
	public String getCycle() {
		return this.cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	@Column(name = "CLASS_SEQUENCE", precision = 10, scale = 0)
	public Long getClassSequence() {
		return this.classSequence;
	}

	public void setClassSequence(Long classSequence) {
		this.classSequence = classSequence;
	}

	@Column(name = "CLASS_TEAM", length = 6)
	public String getClassTeam() {
		return this.classTeam;
	}

	public void setClassTeam(String classTeam) {
		this.classTeam = classTeam;
	}

	@Column(name = "DUTYTYPE", precision = 10, scale = 0)
	public Long getDutytype() {
		return this.dutytype;
	}

	public void setDutytype(Long dutytype) {
		this.dutytype = dutytype;
	}

	@Column(name = "IFDELAY", length = 1)
	public String getIfdelay() {
		return this.ifdelay;
	}

	public void setIfdelay(String ifdelay) {
		this.ifdelay = ifdelay;
	}

	@Column(name = "DELAYRESULT", length = 1)
	public String getDelayresult() {
		return this.delayresult;
	}

	public void setDelayresult(String delayresult) {
		this.delayresult = delayresult;
	}

	@Column(name = "DELAYMAN", length = 6)
	public String getDelayman() {
		return this.delayman;
	}

	public void setDelayman(String delayman) {
		this.delayman = delayman;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELAYDATE", length = 7)
	public Date getDelaydate() {
		return this.delaydate;
	}

	public void setDelaydate(Date delaydate) {
		this.delaydate = delaydate;
	}

	@Column(name = "DELAYTYPE", length = 6)
	public String getDelaytype() {
		return this.delaytype;
	}

	public void setDelaytype(String delaytype) {
		this.delaytype = delaytype;
	}

	@Column(name = "OP_TICKET", length = 15)
	public String getOpTicket() {
		return this.opTicket;
	}

	public void setOpTicket(String opTicket) {
		this.opTicket = opTicket;
	}

	@Column(name = "WORKRESULT", length = 6)
	public String getWorkresult() {
		return this.workresult;
	}

	public void setWorkresult(String workresult) {
		this.workresult = workresult;
	}

	@Column(name = "WORK_EXPLAIN", length = 200)
	public String getWorkExplain() {
		return this.workExplain;
	}

	public void setWorkExplain(String workExplain) {
		this.workExplain = workExplain;
	}

	@Column(name = "IFCHECK", length = 1)
	public String getIfcheck() {
		return this.ifcheck;
	}

	public void setIfcheck(String ifcheck) {
		this.ifcheck = ifcheck;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECKDATE", length = 7)
	public Date getCheckdate() {
		return this.checkdate;
	}

	public void setCheckdate(Date checkdate) {
		this.checkdate = checkdate;
	}

	@Column(name = "CHECKMAN", length = 6)
	public String getCheckman() {
		return this.checkman;
	}

	public void setCheckman(String checkman) {
		this.checkman = checkman;
	}

	@Column(name = "CHECKRESULT", length = 1)
	public String getCheckresult() {
		return this.checkresult;
	}

	public void setCheckresult(String checkresult) {
		this.checkresult = checkresult;
	}

	@Column(name = "IFIMAGE", length = 1)
	public String getIfimage() {
		return this.ifimage;
	}

	public void setIfimage(String ifimage) {
		this.ifimage = ifimage;
	}

	@Column(name = "IMAGECODE", length = 30)
	public String getImagecode() {
		return this.imagecode;
	}

	public void setImagecode(String imagecode) {
		this.imagecode = imagecode;
	}

	@Column(name = "IF_EXPLAIN", length = 1)
	public String getIfExplain() {
		return this.ifExplain;
	}

	public void setIfExplain(String ifExplain) {
		this.ifExplain = ifExplain;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "OPERATOR", length = 6)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELING_DATE", length = 7)
	public Date getDelingDate() {
		return this.delingDate;
	}

	public void setDelingDate(Date delingDate) {
		this.delingDate = delingDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELAY_DATE", length = 7)
	public Date getDelayDate() {
		return this.delayDate;
	}

	public void setDelayDate(Date delayDate) {
		this.delayDate = delayDate;
	}

	@Column(name = "PROTECTOR", length = 6)
	public String getProtector() {
		return this.protector;
	}

	public void setProtector(String protector) {
		this.protector = protector;
	}

	@Column(name = "IF_OPTICKET", length = 1)
	public String getIfOpticket() {
		return this.ifOpticket;
	}

	public void setIfOpticket(String ifOpticket) {
		this.ifOpticket = ifOpticket;
	}

	@Column(name = "CONMAN", length = 6)
	public String getConman() {
		return this.conman;
	}

	public void setConman(String conman) {
		this.conman = conman;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CONDATE", length = 7)
	public Date getCondate() {
		return this.condate;
	}

	public void setCondate(Date condate) {
		this.condate = condate;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ENTERPRISECODE", length = 15)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}