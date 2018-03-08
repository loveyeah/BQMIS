package power.ejb.run.runlog.shift;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCShiftWorker entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_SHIFT_WORKER", uniqueConstraints = {})
public class RunCShiftWorker implements java.io.Serializable {

	// Fields

	private Long shiftWorkerId;
	private Long shiftId;
	private String empCode;
	private Long deptId;
	private String isUse;
	private String enterpriseCode;
	private String isHand;

	// Constructors

	/** default constructor */
	public RunCShiftWorker() {
	}

	/** minimal constructor */
	public RunCShiftWorker(Long shiftWorkerId) {
		this.shiftWorkerId = shiftWorkerId;
	}

	/** full constructor */
	public RunCShiftWorker(Long shiftWorkerId, Long shiftId, String empCode,
			Long deptId, String isUse, String enterpriseCode, String isHand) {
		this.shiftWorkerId = shiftWorkerId;
		this.shiftId = shiftId;
		this.empCode = empCode;
		this.deptId = deptId;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.isHand = isHand;
	}

	// Property accessors
	@Id
	@Column(name = "SHIFT_WORKER_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getShiftWorkerId() {
		return this.shiftWorkerId;
	}

	public void setShiftWorkerId(Long shiftWorkerId) {
		this.shiftWorkerId = shiftWorkerId;
	}

	@Column(name = "SHIFT_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getShiftId() {
		return this.shiftId;
	}

	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
	}

	@Column(name = "EMP_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getEmpCode() {
		return this.empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	@Column(name = "DEPT_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
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
	@Column(name = "IS_HAND", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsHand() {
		return this.isHand;
	}

	public void setIsHand(String isHand) {
		this.isHand = isHand;
	}

}