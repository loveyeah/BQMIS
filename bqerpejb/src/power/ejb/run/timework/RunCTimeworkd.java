package power.ejb.run.timework;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCTimeworkd entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_C_TIMEWORKD", schema = "POWER")
public class RunCTimeworkd implements java.io.Serializable {

	// Fields

	private Long id;
	private String workItemCode;
	private Long month;
	private Long weekNo;
	private Long testDay;
	private Long classSequence;
	private String cycleSequence;
	private String status;
	private String enterprisecode;
	private String isUse;

	// Constructors

	/** default constructor */
	public RunCTimeworkd() {
	}

	/** minimal constructor */
	public RunCTimeworkd(Long id) {
		this.id = id;
	}

	/** full constructor */
	public RunCTimeworkd(Long id, String workItemCode, Long month, Long weekNo,
			Long testDay, Long classSequence, String cycleSequence,
			String status, String enterprisecode, String isUse) {
		this.id = id;
		this.workItemCode = workItemCode;
		this.month = month;
		this.weekNo = weekNo;
		this.testDay = testDay;
		this.classSequence = classSequence;
		this.cycleSequence = cycleSequence;
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

	@Column(name = "WORK_ITEM_CODE", length = 15)
	public String getWorkItemCode() {
		return this.workItemCode;
	}

	public void setWorkItemCode(String workItemCode) {
		this.workItemCode = workItemCode;
	}

	@Column(name = "MONTH", precision = 10, scale = 0)
	public Long getMonth() {
		return this.month;
	}

	public void setMonth(Long month) {
		this.month = month;
	}

	@Column(name = "WEEK_NO", precision = 10, scale = 0)
	public Long getWeekNo() {
		return this.weekNo;
	}

	public void setWeekNo(Long weekNo) {
		this.weekNo = weekNo;
	}

	@Column(name = "TEST_DAY", precision = 10, scale = 0)
	public Long getTestDay() {
		return this.testDay;
	}

	public void setTestDay(Long testDay) {
		this.testDay = testDay;
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