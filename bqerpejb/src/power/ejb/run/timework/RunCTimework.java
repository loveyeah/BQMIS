package power.ejb.run.timework;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCTimework entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_C_TIMEWORK", schema = "POWER")
public class RunCTimework implements java.io.Serializable {

	// Fields

	private Long id;
	private String workType;
	private String workItemCode;
	private String workItemName;
	private String machprofCode;
	private String operator;
	private String protector;
	private String cycle;
	private String workRangeType;
	private Long cycleNumber;
	private Long weekNo;
	private Long weekDay;
	private Long classSequence;
	private String cycleSequence;
	private Date startTime;
	private String delaytype;
	private String memo;
	private String importantlvl;
	private String workExplain;
	private String ifexplain;
	private String ifimage;
	private String ifcheck;
	private String iftest;
	private String ifopticket;
	private String opticketCode;
	private String status;
	private String enterprisecode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunCTimework() {
	}

	/** minimal constructor */
	public RunCTimework(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RunCTimework(Long id, String workType, String workItemCode,
			String workItemName, String machprofCode, String operator,
			String protector, String cycle, String workRangeType,
			Long cycleNumber, Long weekNo, Long weekDay, Long classSequence,
			String cycleSequence, Date startTime, String delaytype,
			String memo, String importantlvl, String workExplain,
			String ifexplain, String ifimage, String ifcheck, String iftest,
			String ifopticket, String opticketCode, String status,
			String enterprisecode, String isUse) {
		this.id = id;
		this.workType = workType;
		this.workItemCode = workItemCode;
		this.workItemName = workItemName;
		this.machprofCode = machprofCode;
		this.operator = operator;
		this.protector = protector;
		this.cycle = cycle;
		this.workRangeType = workRangeType;
		this.cycleNumber = cycleNumber;
		this.weekNo = weekNo;
		this.weekDay = weekDay;
		this.classSequence = classSequence;
		this.cycleSequence = cycleSequence;
		this.startTime = startTime;
		this.delaytype = delaytype;
		this.memo = memo;
		this.importantlvl = importantlvl;
		this.workExplain = workExplain;
		this.ifexplain = ifexplain;
		this.ifimage = ifimage;
		this.ifcheck = ifcheck;
		this.iftest = iftest;
		this.ifopticket = ifopticket;
		this.opticketCode = opticketCode;
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

	@Column(name = "WORK_TYPE", length = 6)
	public String getWorkType() {
		return this.workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	@Column(name = "WORK_ITEM_CODE", length = 15)
	public String getWorkItemCode() {
		return this.workItemCode;
	}

	public void setWorkItemCode(String workItemCode) {
		this.workItemCode = workItemCode;
	}

	@Column(name = "WORK_ITEM_NAME", length = 100)
	public String getWorkItemName() {
		return this.workItemName;
	}

	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}

	@Column(name = "MACHPROF_CODE", length = 15)
	public String getMachprofCode() {
		return this.machprofCode;
	}

	public void setMachprofCode(String machprofCode) {
		this.machprofCode = machprofCode;
	}

	@Column(name = "OPERATOR", length = 8)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "PROTECTOR", length = 8)
	public String getProtector() {
		return this.protector;
	}

	public void setProtector(String protector) {
		this.protector = protector;
	}

	@Column(name = "CYCLE", length = 6)
	public String getCycle() {
		return this.cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	@Column(name = "WORK_RANGE_TYPE", length = 1)
	public String getWorkRangeType() {
		return this.workRangeType;
	}

	public void setWorkRangeType(String workRangeType) {
		this.workRangeType = workRangeType;
	}

	@Column(name = "CYCLE_NUMBER", precision = 10, scale = 0)
	public Long getCycleNumber() {
		return this.cycleNumber;
	}

	public void setCycleNumber(Long cycleNumber) {
		this.cycleNumber = cycleNumber;
	}

	@Column(name = "WEEK_NO", precision = 10, scale = 0)
	public Long getWeekNo() {
		return this.weekNo;
	}

	public void setWeekNo(Long weekNo) {
		this.weekNo = weekNo;
	}

	@Column(name = "WEEK_DAY", precision = 10, scale = 0)
	public Long getWeekDay() {
		return this.weekDay;
	}

	public void setWeekDay(Long weekDay) {
		this.weekDay = weekDay;
	}

	@Column(name = "CLASS_SEQUENCE", precision = 6, scale = 0)
	public Long getClassSequence() {
		return this.classSequence;
	}

	public void setClassSequence(Long classSequence) {
		this.classSequence = classSequence;
	}

	@Column(name = "CYCLE_SEQUENCE", length = 100)
	public String getCycleSequence() {
		return this.cycleSequence;
	}

	public void setCycleSequence(String cycleSequence) {
		this.cycleSequence = cycleSequence;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_TIME", length = 7)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "DELAYTYPE", length = 6)
	public String getDelaytype() {
		return this.delaytype;
	}

	public void setDelaytype(String delaytype) {
		this.delaytype = delaytype;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "IMPORTANTLVL", length = 1)
	public String getImportantlvl() {
		return this.importantlvl;
	}

	public void setImportantlvl(String importantlvl) {
		this.importantlvl = importantlvl;
	}

	@Column(name = "WORK_EXPLAIN", length = 200)
	public String getWorkExplain() {
		return this.workExplain;
	}

	public void setWorkExplain(String workExplain) {
		this.workExplain = workExplain;
	}

	@Column(name = "IFEXPLAIN", length = 1)
	public String getIfexplain() {
		return this.ifexplain;
	}

	public void setIfexplain(String ifexplain) {
		this.ifexplain = ifexplain;
	}

	@Column(name = "IFIMAGE", length = 1)
	public String getIfimage() {
		return this.ifimage;
	}

	public void setIfimage(String ifimage) {
		this.ifimage = ifimage;
	}

	@Column(name = "IFCHECK", length = 1)
	public String getIfcheck() {
		return this.ifcheck;
	}

	public void setIfcheck(String ifcheck) {
		this.ifcheck = ifcheck;
	}

	@Column(name = "IFTEST", length = 1)
	public String getIftest() {
		return this.iftest;
	}

	public void setIftest(String iftest) {
		this.iftest = iftest;
	}

	@Column(name = "IFOPTICKET", length = 1)
	public String getIfopticket() {
		return this.ifopticket;
	}

	public void setIfopticket(String ifopticket) {
		this.ifopticket = ifopticket;
	}

	@Column(name = "OPTICKET_CODE", length = 10)
	public String getOpticketCode() {
		return this.opticketCode;
	}

	public void setOpticketCode(String opticketCode) {
		this.opticketCode = opticketCode;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ENTERPRISECODE", length = 30)
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