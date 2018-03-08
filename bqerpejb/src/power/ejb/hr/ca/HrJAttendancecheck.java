package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJAttendancecheck entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_ATTENDANCECHECK")
public class HrJAttendancecheck implements java.io.Serializable {

	// Fields

	private HrJAttendancecheckId id;
	private Long depCharge1;
	private Date checkedDate1;
	private Long depCharge2;
	private Date checkedDate2;
	private Long depCharge3;
	private Date checkedDate3;
	private Long depCharge4;
	private Date checkedDate4;
	private String lastModifiyBy;
	private Date lastModifiyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJAttendancecheck() {
	}

	/** minimal constructor */
	public HrJAttendancecheck(HrJAttendancecheckId id) {
		this.id = id;
	}

	/** full constructor */
	public HrJAttendancecheck(HrJAttendancecheckId id, Long depCharge1,
			Date checkedDate1, Long depCharge2, Date checkedDate2,
			Long depCharge3, Date checkedDate3, Long depCharge4,
			Date checkedDate4, String lastModifiyBy, Date lastModifiyDate,
			String isUse, String enterpriseCode) {
		this.id = id;
		this.depCharge1 = depCharge1;
		this.checkedDate1 = checkedDate1;
		this.depCharge2 = depCharge2;
		this.checkedDate2 = checkedDate2;
		this.depCharge3 = depCharge3;
		this.checkedDate3 = checkedDate3;
		this.depCharge4 = depCharge4;
		this.checkedDate4 = checkedDate4;
		this.lastModifiyBy = lastModifiyBy;
		this.lastModifiyDate = lastModifiyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "attendanceYear", column = @Column(name = "ATTENDANCE_YEAR", nullable = false, length = 4)),
			@AttributeOverride(name = "attendanceMonth", column = @Column(name = "ATTENDANCE_MONTH", nullable = false, length = 2)),
			@AttributeOverride(name = "attendanceDep", column = @Column(name = "ATTENDANCE_DEP", nullable = false, precision = 10, scale = 0)) })
	public HrJAttendancecheckId getId() {
		return this.id;
	}

	public void setId(HrJAttendancecheckId id) {
		this.id = id;
	}

	@Column(name = "DEP_CHARGE1", precision = 10, scale = 0)
	public Long getDepCharge1() {
		return this.depCharge1;
	}

	public void setDepCharge1(Long depCharge1) {
		this.depCharge1 = depCharge1;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CHECKED_DATE1", length = 7)
	public Date getCheckedDate1() {
		return this.checkedDate1;
	}

	public void setCheckedDate1(Date checkedDate1) {
		this.checkedDate1 = checkedDate1;
	}

	@Column(name = "DEP_CHARGE2", precision = 10, scale = 0)
	public Long getDepCharge2() {
		return this.depCharge2;
	}

	public void setDepCharge2(Long depCharge2) {
		this.depCharge2 = depCharge2;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CHECKED_DATE2", length = 7)
	public Date getCheckedDate2() {
		return this.checkedDate2;
	}

	public void setCheckedDate2(Date checkedDate2) {
		this.checkedDate2 = checkedDate2;
	}

	@Column(name = "DEP_CHARGE3", precision = 10, scale = 0)
	public Long getDepCharge3() {
		return this.depCharge3;
	}

	public void setDepCharge3(Long depCharge3) {
		this.depCharge3 = depCharge3;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CHECKED_DATE3", length = 7)
	public Date getCheckedDate3() {
		return this.checkedDate3;
	}

	public void setCheckedDate3(Date checkedDate3) {
		this.checkedDate3 = checkedDate3;
	}

	@Column(name = "DEP_CHARGE4", precision = 10, scale = 0)
	public Long getDepCharge4() {
		return this.depCharge4;
	}

	public void setDepCharge4(Long depCharge4) {
		this.depCharge4 = depCharge4;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CHECKED_DATE4", length = 7)
	public Date getCheckedDate4() {
		return this.checkedDate4;
	}

	public void setCheckedDate4(Date checkedDate4) {
		this.checkedDate4 = checkedDate4;
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