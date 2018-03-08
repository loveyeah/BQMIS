package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCAttendancedep entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_ATTENDANCEDEP")
public class HrCAttendancedep implements java.io.Serializable {

	// Fields

	private Long id;
	private Long attendanceDeptId;
	private String attendanceDeptName;
	private String attendDepType;
	private Long topCheckDepId;
	private Long replaceDepId;
	private Long attendWriterId;
	private Long attendCheckerId;
	private String lastModifiyBy;
	private Date lastModifiyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCAttendancedep() {
	}

	/** minimal constructor */
	public HrCAttendancedep(Long id, Long attendanceDeptId,
			String attendanceDeptName) {
		this.id = id;
		this.attendanceDeptId = attendanceDeptId;
		this.attendanceDeptName = attendanceDeptName;
	}

	/** full constructor */
	public HrCAttendancedep(Long id, Long attendanceDeptId,
			String attendanceDeptName, String attendDepType,
			Long topCheckDepId, Long replaceDepId, Long attendWriterId,
			Long attendCheckerId, String lastModifiyBy, Date lastModifiyDate,
			String isUse, String enterpriseCode) {
		this.id = id;
		this.attendanceDeptId = attendanceDeptId;
		this.attendanceDeptName = attendanceDeptName;
		this.attendDepType = attendDepType;
		this.topCheckDepId = topCheckDepId;
		this.replaceDepId = replaceDepId;
		this.attendWriterId = attendWriterId;
		this.attendCheckerId = attendCheckerId;
		this.lastModifiyBy = lastModifiyBy;
		this.lastModifiyDate = lastModifiyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
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

	@Column(name = "ATTENDANCE_DEPT_ID", nullable = false, precision = 10, scale = 0)
	public Long getAttendanceDeptId() {
		return this.attendanceDeptId;
	}

	public void setAttendanceDeptId(Long attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}

	@Column(name = "ATTENDANCE_DEPT_NAME", nullable = false, length = 100)
	public String getAttendanceDeptName() {
		return this.attendanceDeptName;
	}

	public void setAttendanceDeptName(String attendanceDeptName) {
		this.attendanceDeptName = attendanceDeptName;
	}

	@Column(name = "ATTEND_DEP_TYPE", length = 1)
	public String getAttendDepType() {
		return this.attendDepType;
	}

	public void setAttendDepType(String attendDepType) {
		this.attendDepType = attendDepType;
	}

	@Column(name = "TOP_CHECK_DEP_ID", precision = 10, scale = 0)
	public Long getTopCheckDepId() {
		return this.topCheckDepId;
	}

	public void setTopCheckDepId(Long topCheckDepId) {
		this.topCheckDepId = topCheckDepId;
	}

	@Column(name = "REPLACE_DEP_ID", precision = 10, scale = 0)
	public Long getReplaceDepId() {
		return this.replaceDepId;
	}

	public void setReplaceDepId(Long replaceDepId) {
		this.replaceDepId = replaceDepId;
	}

	@Column(name = "ATTEND_WRITER_ID", precision = 10, scale = 0)
	public Long getAttendWriterId() {
		return this.attendWriterId;
	}

	public void setAttendWriterId(Long attendWriterId) {
		this.attendWriterId = attendWriterId;
	}

	@Column(name = "ATTEND_CHECKER_ID", precision = 10, scale = 0)
	public Long getAttendCheckerId() {
		return this.attendCheckerId;
	}

	public void setAttendCheckerId(Long attendCheckerId) {
		this.attendCheckerId = attendCheckerId;
	}

	@Column(name = "LAST_MODIFIY_BY", length = 16)
	public String getLastModifiyBy() {
		return this.lastModifiyBy;
	}

	public void setLastModifiyBy(String lastModifiyBy) {
		this.lastModifiyBy = lastModifiyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIY_DATE", length = 7)
	public Date getLastModifiyDate() {
		return this.lastModifiyDate;
	}

	public void setLastModifiyDate(Date lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}