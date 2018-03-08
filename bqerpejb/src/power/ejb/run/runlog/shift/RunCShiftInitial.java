package power.ejb.run.runlog.shift;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCShiftInitial entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_SHIFT_INITIAL", uniqueConstraints = {})
public class RunCShiftInitial implements java.io.Serializable {

	// Fields

	private Long initialNo;
	private String specialityCode;
	private Long shiftAmount;
	private Long timeAmount;
	private Date activeDate;
	private Date disconnectDate;
	private String isUse;
	private String enterpriseCode;
	private String initialName;

	// Constructors

	/** default constructor */
	public RunCShiftInitial() {
	}

	/** minimal constructor */
	public RunCShiftInitial(Long initialNo) {
		this.initialNo = initialNo;
	}

	/** full constructor */
	public RunCShiftInitial(Long initialNo, String specialityCode,
			Long shiftAmount, Long timeAmount, Date activeDate,
			Date disconnectDate, String isUse, String enterpriseCode,
			String initialName) {
		this.initialNo = initialNo;
		this.specialityCode = specialityCode;
		this.shiftAmount = shiftAmount;
		this.timeAmount = timeAmount;
		this.activeDate = activeDate;
		this.disconnectDate = disconnectDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.initialName = initialName;
	}

	// Property accessors
	@Id
	@Column(name = "INITIAL_NO", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getInitialNo() {
		return this.initialNo;
	}

	public void setInitialNo(Long initialNo) {
		this.initialNo = initialNo;
	}

	@Column(name = "SPECIALITY_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getSpecialityCode() {
		return this.specialityCode;
	}

	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}

	@Column(name = "SHIFT_AMOUNT", unique = false, nullable = true, insertable = true, updatable = true, precision = 2, scale = 0)
	public Long getShiftAmount() {
		return this.shiftAmount;
	}

	public void setShiftAmount(Long shiftAmount) {
		this.shiftAmount = shiftAmount;
	}

	@Column(name = "TIME_AMOUNT", unique = false, nullable = true, insertable = true, updatable = true, precision = 2, scale = 0)
	public Long getTimeAmount() {
		return this.timeAmount;
	}

	public void setTimeAmount(Long timeAmount) {
		this.timeAmount = timeAmount;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ACTIVE_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getActiveDate() {
		return this.activeDate;
	}

	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DISCONNECT_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getDisconnectDate() {
		return this.disconnectDate;
	}

	public void setDisconnectDate(Date disconnectDate) {
		this.disconnectDate = disconnectDate;
	}

	@Column(name = "IS_USE", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "INITIAL_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getInitialName() {
		return this.initialName;
	}

	public void setInitialName(String initialName) {
		this.initialName = initialName;
	}

}