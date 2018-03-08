package power.ejb.run.runlog.shift;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunCShiftTime entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_SHIFT_TIME",  uniqueConstraints = {})
public class RunCShiftTime implements java.io.Serializable {

	// Fields

	private Long shiftTimeId;
	private Long initialNo;
	private String shiftTimeName;
	private Date onDutyTime;
	private Date offDutyTime;
	private Long shiftSerial;
	private String shiftTimeDesc;
	private String isUse;
	private String isRest;
	private String enterpriseCode;
	
	

	// Constructors

	/** default constructor */
	public RunCShiftTime() {
	}

	/** minimal constructor */
	public RunCShiftTime(Long shiftTimeId) {
		this.shiftTimeId = shiftTimeId;
	}

	/** full constructor */
	public RunCShiftTime(Long shiftTimeId, Long initialNo,
			String shiftTimeName, Date onDutyTime, Date offDutyTime,
			Long shiftSerial, String shiftTimeDesc, String isUse,
			String isRest, String enterpriseCode) {
		this.shiftTimeId = shiftTimeId;
		this.initialNo = initialNo;
		this.shiftTimeName = shiftTimeName;
		this.onDutyTime = onDutyTime;
		this.offDutyTime = offDutyTime;
		this.shiftSerial = shiftSerial;
		this.shiftTimeDesc = shiftTimeDesc;
		this.isUse = isUse;
		this.isRest = isRest;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SHIFT_TIME_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getShiftTimeId() {
		return this.shiftTimeId;
	}

	public void setShiftTimeId(Long shiftTimeId) {
		this.shiftTimeId = shiftTimeId;
	}

	@Column(name = "INITIAL_NO", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getInitialNo() {
		return this.initialNo;
	}

	public void setInitialNo(Long initialNo) {
		this.initialNo = initialNo;
	}

	@Column(name = "SHIFT_TIME_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getShiftTimeName() {
		return this.shiftTimeName;
	}

	public void setShiftTimeName(String shiftTimeName) {
		this.shiftTimeName = shiftTimeName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ON_DUTY_TIME")
	public Date getOnDutyTime() {
		return this.onDutyTime;
	}

	public void setOnDutyTime(Date onDutyTime) {
		this.onDutyTime = onDutyTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OFF_DUTY_TIME")
	public Date getOffDutyTime() {
		return this.offDutyTime;
	}

	public void setOffDutyTime(Date offDutyTime) {
		this.offDutyTime = offDutyTime;
	}

	@Column(name = "SHIFT_SERIAL", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getShiftSerial() {
		return this.shiftSerial;
	}

	public void setShiftSerial(Long shiftSerial) {
		this.shiftSerial = shiftSerial;
	}

	@Column(name = "SHIFT_TIME_DESC", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getShiftTimeDesc() {
		return this.shiftTimeDesc;
	}

	public void setShiftTimeDesc(String shiftTimeDesc) {
		this.shiftTimeDesc = shiftTimeDesc;
	}

	@Column(name = "IS_USE", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "IS_REST", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsRest() {
		return this.isRest;
	}

	public void setIsRest(String isRest) {
		this.isRest = isRest;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}