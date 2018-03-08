package power.ejb.webservice.run.ticketmanage;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJTaskInformation entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_TASK_INFORMATION")
public class RunJTaskInformation implements java.io.Serializable {

	// Fields

	private Long id;
	private String taskNo;
	private String tickeNo;
	private String isStandardticket;
	private String isWorkticket;
	private String tickettypeId;
	private String tickettypeName;
	private String watchtypeId;
	private String watchtypeName;
	private String machinetypeId;
	private String machinetypeName;
	private Date disuseTime;
	private Date beginTime;
	private String stryear;
	private String strmonth;
	private Date finishEndTime;
	private String isFlag;
	private String causes;
	private String methodName;
	private String enterprisecode;
	private String operator;
	private Date createDate;
	private Long execteResult;

	// Constructors

	/** default constructor */
	public RunJTaskInformation() {
	}

	/** minimal constructor */
	public RunJTaskInformation(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RunJTaskInformation(Long id, String taskNo, String tickeNo,
			String isStandardticket, String isWorkticket, String tickettypeId,
			String tickettypeName, String watchtypeId, String watchtypeName,
			String machinetypeId, String machinetypeName, Date disuseTime,
			Date beginTime, String stryear, String strmonth,
			Date finishEndTime, String isFlag, String causes,
			String methodName, String enterprisecode, String operator,
			Date createDate, Long execteResult) {
		this.id = id;
		this.taskNo = taskNo;
		this.tickeNo = tickeNo;
		this.isStandardticket = isStandardticket;
		this.isWorkticket = isWorkticket;
		this.tickettypeId = tickettypeId;
		this.tickettypeName = tickettypeName;
		this.watchtypeId = watchtypeId;
		this.watchtypeName = watchtypeName;
		this.machinetypeId = machinetypeId;
		this.machinetypeName = machinetypeName;
		this.disuseTime = disuseTime;
		this.beginTime = beginTime;
		this.stryear = stryear;
		this.strmonth = strmonth;
		this.finishEndTime = finishEndTime;
		this.isFlag = isFlag;
		this.causes = causes;
		this.methodName = methodName;
		this.enterprisecode = enterprisecode;
		this.operator = operator;
		this.createDate = createDate;
		this.execteResult = execteResult;
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

	@Column(name = "TASK_NO", length = 30)
	public String getTaskNo() {
		return this.taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	@Column(name = "TICKE_NO", length = 30)
	public String getTickeNo() {
		return this.tickeNo;
	}

	public void setTickeNo(String tickeNo) {
		this.tickeNo = tickeNo;
	}

	@Column(name = "IS_STANDARDTICKET", length = 1)
	public String getIsStandardticket() {
		return this.isStandardticket;
	}

	public void setIsStandardticket(String isStandardticket) {
		this.isStandardticket = isStandardticket;
	}

	@Column(name = "IS_WORKTICKET", length = 1)
	public String getIsWorkticket() {
		return this.isWorkticket;
	}

	public void setIsWorkticket(String isWorkticket) {
		this.isWorkticket = isWorkticket;
	}

	@Column(name = "TICKETTYPE_ID", length = 20)
	public String getTickettypeId() {
		return this.tickettypeId;
	}

	public void setTickettypeId(String tickettypeId) {
		this.tickettypeId = tickettypeId;
	}

	@Column(name = "TICKETTYPE_NAME", length = 20)
	public String getTickettypeName() {
		return this.tickettypeName;
	}

	public void setTickettypeName(String tickettypeName) {
		this.tickettypeName = tickettypeName;
	}

	@Column(name = "WATCHTYPE_ID", length = 20)
	public String getWatchtypeId() {
		return this.watchtypeId;
	}

	public void setWatchtypeId(String watchtypeId) {
		this.watchtypeId = watchtypeId;
	}

	@Column(name = "WATCHTYPE_NAME", length = 20)
	public String getWatchtypeName() {
		return this.watchtypeName;
	}

	public void setWatchtypeName(String watchtypeName) {
		this.watchtypeName = watchtypeName;
	}

	@Column(name = "MACHINETYPE_ID", length = 20)
	public String getMachinetypeId() {
		return this.machinetypeId;
	}

	public void setMachinetypeId(String machinetypeId) {
		this.machinetypeId = machinetypeId;
	}

	@Column(name = "MACHINETYPE_NAME", length = 20)
	public String getMachinetypeName() {
		return this.machinetypeName;
	}

	public void setMachinetypeName(String machinetypeName) {
		this.machinetypeName = machinetypeName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DISUSE_TIME", length = 7)
	public Date getDisuseTime() {
		return this.disuseTime;
	}

	public void setDisuseTime(Date disuseTime) {
		this.disuseTime = disuseTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BEGIN_TIME", length = 7)
	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	@Column(name = "STRYEAR", length = 20)
	public String getStryear() {
		return this.stryear;
	}

	public void setStryear(String stryear) {
		this.stryear = stryear;
	}

	@Column(name = "STRMONTH", length = 20)
	public String getStrmonth() {
		return this.strmonth;
	}

	public void setStrmonth(String strmonth) {
		this.strmonth = strmonth;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FINISH_END_TIME", length = 7)
	public Date getFinishEndTime() {
		return this.finishEndTime;
	}

	public void setFinishEndTime(Date finishEndTime) {
		this.finishEndTime = finishEndTime;
	}

	@Column(name = "IS_FLAG", length = 1)
	public String getIsFlag() {
		return this.isFlag;
	}

	public void setIsFlag(String isFlag) {
		this.isFlag = isFlag;
	}

	@Column(name = "CAUSES", length = 100)
	public String getCauses() {
		return this.causes;
	}

	public void setCauses(String causes) {
		this.causes = causes;
	}

	@Column(name = "METHOD_NAME", length = 20)
	public String getMethodName() {
		return this.methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Column(name = "ENTERPRISECODE", length = 20)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

	@Column(name = "OPERATOR", length = 30)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "EXECTE_RESULT", precision = 10, scale = 0)
	public Long getExecteResult() {
		return this.execteResult;
	}

	public void setExecteResult(Long execteResult) {
		this.execteResult = execteResult;
	}

}