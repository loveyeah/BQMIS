package power.ejb.run.runlog.shift;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCShift entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_SHIFT",  uniqueConstraints = {})
public class RunCShift implements java.io.Serializable {

	// Fields

	private Long shiftId;
	private Long initialNo;
	private String shiftName;
	private Long shiftSequence;
	private String isShift;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunCShift() {
	}

	/** minimal constructor */
	public RunCShift(Long shiftId) {
		this.shiftId = shiftId;
	}

	/** full constructor */
	public RunCShift(Long shiftId, Long initialNo, String shiftName,
			Long shiftSequence, String isShift, String isUse,
			String enterpriseCode) {
		this.shiftId = shiftId;
		this.initialNo = initialNo;
		this.shiftName = shiftName;
		this.shiftSequence = shiftSequence;
		this.isShift = isShift;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SHIFT_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getShiftId() {
		return this.shiftId;
	}

	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
	}

	@Column(name = "INITIAL_NO", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getInitialNo() {
		return this.initialNo;
	}

	public void setInitialNo(Long initialNo) {
		this.initialNo = initialNo;
	}

	@Column(name = "SHIFT_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getShiftName() {
		return this.shiftName;
	}

	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}

	@Column(name = "SHIFT_SEQUENCE", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getShiftSequence() {
		return this.shiftSequence;
	}

	public void setShiftSequence(Long shiftSequence) {
		this.shiftSequence = shiftSequence;
	}

	@Column(name = "IS_SHIFT", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsShift() {
		return this.isShift;
	}

	public void setIsShift(String isShift) {
		this.isShift = isShift;
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

}