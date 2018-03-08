package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJPolitics entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_POLITICS")
public class HrJPolitics implements java.io.Serializable {

	// Fields

	private Long politicsid;
	private Date joinDate;
	private String belongUnit;
	private Date exitDate;
	private String introducer;
	private String joinUnit;
	private String introducerUnit;
	private String joinPlace;
	private String ifNewMark;
	private String memo;
	private String insertby;
	private Date insertdate;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Long empId;
	private Long politicsId;

	// Constructors

	/** default constructor */
	public HrJPolitics() {
	}

	/** minimal constructor */
	public HrJPolitics(Long politicsid, Long politicsId) {
		this.politicsid = politicsid;
		this.politicsId = politicsId;
	}

	/** full constructor */
	public HrJPolitics(Long politicsid, Date joinDate, String belongUnit,
			Date exitDate, String introducer, String joinUnit,
			String introducerUnit, String joinPlace, String ifNewMark,
			String memo, String insertby, Date insertdate,
			String enterpriseCode, String isUse, String lastModifiedBy,
			Date lastModifiedDate, Long empId, Long politicsId) {
		this.politicsid = politicsid;
		this.joinDate = joinDate;
		this.belongUnit = belongUnit;
		this.exitDate = exitDate;
		this.introducer = introducer;
		this.joinUnit = joinUnit;
		this.introducerUnit = introducerUnit;
		this.joinPlace = joinPlace;
		this.ifNewMark = ifNewMark;
		this.memo = memo;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.empId = empId;
		this.politicsId = politicsId;
	}

	// Property accessors
	@Id
	@Column(name = "POLITICSID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPoliticsid() {
		return this.politicsid;
	}

	public void setPoliticsid(Long politicsid) {
		this.politicsid = politicsid;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "JOIN_DATE", length = 7)
	public Date getJoinDate() {
		return this.joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	@Column(name = "BELONG_UNIT", length = 30)
	public String getBelongUnit() {
		return this.belongUnit;
	}

	public void setBelongUnit(String belongUnit) {
		this.belongUnit = belongUnit;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EXIT_DATE", length = 7)
	public Date getExitDate() {
		return this.exitDate;
	}

	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}

	@Column(name = "INTRODUCER", length = 20)
	public String getIntroducer() {
		return this.introducer;
	}

	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}

	@Column(name = "JOIN_UNIT", length = 50)
	public String getJoinUnit() {
		return this.joinUnit;
	}

	public void setJoinUnit(String joinUnit) {
		this.joinUnit = joinUnit;
	}

	@Column(name = "INTRODUCER_UNIT", length = 30)
	public String getIntroducerUnit() {
		return this.introducerUnit;
	}

	public void setIntroducerUnit(String introducerUnit) {
		this.introducerUnit = introducerUnit;
	}

	@Column(name = "JOIN_PLACE", length = 30)
	public String getJoinPlace() {
		return this.joinPlace;
	}

	public void setJoinPlace(String joinPlace) {
		this.joinPlace = joinPlace;
	}

	@Column(name = "IF_NEW_MARK", length = 1)
	public String getIfNewMark() {
		return this.ifNewMark;
	}

	public void setIfNewMark(String ifNewMark) {
		this.ifNewMark = ifNewMark;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "INSERTBY", length = 16)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
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

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "POLITICS_ID", nullable = false, precision = 10, scale = 0)
	public Long getPoliticsId() {
		return this.politicsId;
	}

	public void setPoliticsId(Long politicsId) {
		this.politicsId = politicsId;
	}

}