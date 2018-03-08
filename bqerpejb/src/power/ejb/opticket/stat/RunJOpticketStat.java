package power.ejb.opticket.stat;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJOpticketStat entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_OPTICKET_STAT")
public class RunJOpticketStat implements java.io.Serializable {

	// Fields

	private Long id;
	private String reportName;
	private Long reportDate;
	private String profession;
	private String memo;
	private String deptCharge;
	private String statBy;
	private Date statDatea;
	private Long allCount;
	private Long totalCount;
	private Long qualifiedCount;
	private Long notQualifiedCount;
	private Double qualifiedRate;
	private String enterprisecode;

	// Constructors

	/** default constructor */
	public RunJOpticketStat() {
	}

	/** minimal constructor */
	public RunJOpticketStat(Long id) {
		this.id = id;
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

	@Column(name = "REPORT_NAME", length = 50)
	public String getReportName() {
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Column(name = "REPORT_DATE", precision = 10, scale = 0)
	public Long getReportDate() {
		return this.reportDate;
	}

	public void setReportDate(Long reportDate) {
		this.reportDate = reportDate;
	}

	@Column(name = "PROFESSION", length = 20)
	public String getProfession() {
		return this.profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "DEPT_CHARGE", length = 16)
	public String getDeptCharge() {
		return this.deptCharge;
	}

	public void setDeptCharge(String deptCharge) {
		this.deptCharge = deptCharge;
	}

	@Column(name = "STAT_BY", length = 16)
	public String getStatBy() {
		return this.statBy;
	}

	public void setStatBy(String statBy) {
		this.statBy = statBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "STAT_DATEA", length = 7)
	public Date getStatDatea() {
		return this.statDatea;
	}

	public void setStatDatea(Date statDatea) {
		this.statDatea = statDatea;
	}

	@Column(name = "ALL_COUNT", precision = 10, scale = 0)
	public Long getAllCount() {
		return this.allCount;
	}

	public void setAllCount(Long allCount) {
		this.allCount = allCount;
	}

	@Column(name = "TOTAL_COUNT", precision = 10, scale = 0)
	public Long getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	@Column(name = "QUALIFIED_COUNT", precision = 10, scale = 0)
	public Long getQualifiedCount() {
		return this.qualifiedCount;
	}

	public void setQualifiedCount(Long qualifiedCount) {
		this.qualifiedCount = qualifiedCount;
	}

	@Column(name = "NOT_QUALIFIED_COUNT", precision = 10, scale = 0)
	public Long getNotQualifiedCount() {
		return this.notQualifiedCount;
	}

	public void setNotQualifiedCount(Long notQualifiedCount) {
		this.notQualifiedCount = notQualifiedCount;
	}

	@Column(name = "QUALIFIED_RATE", precision = 15, scale = 4)
	public Double getQualifiedRate() {
		return this.qualifiedRate;
	}

	public void setQualifiedRate(Double qualifiedRate) {
		this.qualifiedRate = qualifiedRate;
	}

	@Column(name = "ENTERPRISECODE", length = 20)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

}