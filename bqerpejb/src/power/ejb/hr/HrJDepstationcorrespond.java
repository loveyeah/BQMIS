package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJDepstationcorrespond entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_DEPSTATIONCORRESPOND")
public class HrJDepstationcorrespond implements java.io.Serializable {

	// Fields

	private Long depstationcorrespondid;
	private Long deptId;
	private Long standardPersonNum;
	private String memo;
	private Date insertdate;
	private String insertby;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Long stationId;
	private String isLead;

	// Constructors

	/** default constructor */
	public HrJDepstationcorrespond() {
	}

	/** minimal constructor */
	public HrJDepstationcorrespond(Long depstationcorrespondid, Long stationId) {
		this.depstationcorrespondid = depstationcorrespondid;
		this.stationId = stationId;
	}

	/** full constructor */
	public HrJDepstationcorrespond(Long depstationcorrespondid, Long deptId,
			Long standardPersonNum, String memo, Date insertdate,
			String insertby, String enterpriseCode, String isUse,
			String lastModifiedBy, Date lastModifiedDate, Long stationId,
			String isLead) {
		this.depstationcorrespondid = depstationcorrespondid;
		this.deptId = deptId;
		this.standardPersonNum = standardPersonNum;
		this.memo = memo;
		this.insertdate = insertdate;
		this.insertby = insertby;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.stationId = stationId;
		this.isLead = isLead;
	}

	// Property accessors
	@Id
	@Column(name = "DEPSTATIONCORRESPONDID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDepstationcorrespondid() {
		return this.depstationcorrespondid;
	}

	public void setDepstationcorrespondid(Long depstationcorrespondid) {
		this.depstationcorrespondid = depstationcorrespondid;
	}

	@Column(name = "DEPT_ID", precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Column(name = "STANDARD_PERSON_NUM", precision = 10, scale = 0)
	public Long getStandardPersonNum() {
		return this.standardPersonNum;
	}

	public void setStandardPersonNum(Long standardPersonNum) {
		this.standardPersonNum = standardPersonNum;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

	@Column(name = "INSERTBY", length = 16)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "STATION_ID", nullable = false, precision = 10, scale = 0)
	public Long getStationId() {
		return this.stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	@Column(name = "IS_LEAD", length = 1)
	public String getIsLead() {
		return this.isLead;
	}

	public void setIsLead(String isLead) {
		this.isLead = isLead;
	}

}