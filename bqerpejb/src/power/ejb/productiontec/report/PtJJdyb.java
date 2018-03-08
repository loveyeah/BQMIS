package power.ejb.productiontec.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJJdyb entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_J_JDYB", schema = "POWER")
public class PtJJdyb implements java.io.Serializable {

	// Fields

	private Long jdybId;
	private String month;
	private Long tfMotionSum;
	private Long tfMotionRight;
	private Long ooMotionSum;
	private Long ooMotionRight;
	private Long ttMotionSum;
	private Long ttMotionRight;
	private Long oochzMotionSum;
	private Long oochzMotionRight;
	private Long ttchzMotionSum;
	private Long ttchzMotionRight;
	private Long errorMotionSum;
	private Long errorMotionRight;
	private String managerBy;
	private String protectedBy;
	private String approveBy;
	private String entryBy;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJJdyb() {
	}

	/** minimal constructor */
	public PtJJdyb(Long jdybId) {
		this.jdybId = jdybId;
	}

	/** full constructor */
	public PtJJdyb(Long jdybId, String month, Long tfMotionSum,
			Long tfMotionRight, Long ooMotionSum, Long ooMotionRight,
			Long ttMotionSum, Long ttMotionRight, Long oochzMotionSum,
			Long oochzMotionRight, Long ttchzMotionSum, Long ttchzMotionRight,
			Long errorMotionSum, Long errorMotionRight, String managerBy,
			String protectedBy, String approveBy, String entryBy,
			String enterpriseCode) {
		this.jdybId = jdybId;
		this.month = month;
		this.tfMotionSum = tfMotionSum;
		this.tfMotionRight = tfMotionRight;
		this.ooMotionSum = ooMotionSum;
		this.ooMotionRight = ooMotionRight;
		this.ttMotionSum = ttMotionSum;
		this.ttMotionRight = ttMotionRight;
		this.oochzMotionSum = oochzMotionSum;
		this.oochzMotionRight = oochzMotionRight;
		this.ttchzMotionSum = ttchzMotionSum;
		this.ttchzMotionRight = ttchzMotionRight;
		this.errorMotionSum = errorMotionSum;
		this.errorMotionRight = errorMotionRight;
		this.managerBy = managerBy;
		this.protectedBy = protectedBy;
		this.approveBy = approveBy;
		this.entryBy = entryBy;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "JDYB_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJdybId() {
		return this.jdybId;
	}

	public void setJdybId(Long jdybId) {
		this.jdybId = jdybId;
	}

	@Column(name = "MONTH", length = 10)
	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Column(name = "TF_MOTION_SUM", precision = 10, scale = 0)
	public Long getTfMotionSum() {
		return this.tfMotionSum;
	}

	public void setTfMotionSum(Long tfMotionSum) {
		this.tfMotionSum = tfMotionSum;
	}

	@Column(name = "TF_MOTION_RIGHT", precision = 10, scale = 0)
	public Long getTfMotionRight() {
		return this.tfMotionRight;
	}

	public void setTfMotionRight(Long tfMotionRight) {
		this.tfMotionRight = tfMotionRight;
	}

	@Column(name = "OO_MOTION_SUM", precision = 10, scale = 0)
	public Long getOoMotionSum() {
		return this.ooMotionSum;
	}

	public void setOoMotionSum(Long ooMotionSum) {
		this.ooMotionSum = ooMotionSum;
	}

	@Column(name = "OO_MOTION_RIGHT", precision = 10, scale = 0)
	public Long getOoMotionRight() {
		return this.ooMotionRight;
	}

	public void setOoMotionRight(Long ooMotionRight) {
		this.ooMotionRight = ooMotionRight;
	}

	@Column(name = "TT_MOTION_SUM", precision = 10, scale = 0)
	public Long getTtMotionSum() {
		return this.ttMotionSum;
	}

	public void setTtMotionSum(Long ttMotionSum) {
		this.ttMotionSum = ttMotionSum;
	}

	@Column(name = "TT_MOTION_RIGHT", precision = 10, scale = 0)
	public Long getTtMotionRight() {
		return this.ttMotionRight;
	}

	public void setTtMotionRight(Long ttMotionRight) {
		this.ttMotionRight = ttMotionRight;
	}

	@Column(name = "OOCHZ_MOTION_SUM", precision = 10, scale = 0)
	public Long getOochzMotionSum() {
		return this.oochzMotionSum;
	}

	public void setOochzMotionSum(Long oochzMotionSum) {
		this.oochzMotionSum = oochzMotionSum;
	}

	@Column(name = "OOCHZ_MOTION_RIGHT", precision = 10, scale = 0)
	public Long getOochzMotionRight() {
		return this.oochzMotionRight;
	}

	public void setOochzMotionRight(Long oochzMotionRight) {
		this.oochzMotionRight = oochzMotionRight;
	}

	@Column(name = "TTCHZ_MOTION_SUM", precision = 10, scale = 0)
	public Long getTtchzMotionSum() {
		return this.ttchzMotionSum;
	}

	public void setTtchzMotionSum(Long ttchzMotionSum) {
		this.ttchzMotionSum = ttchzMotionSum;
	}

	@Column(name = "TTCHZ_MOTION_RIGHT", precision = 10, scale = 0)
	public Long getTtchzMotionRight() {
		return this.ttchzMotionRight;
	}

	public void setTtchzMotionRight(Long ttchzMotionRight) {
		this.ttchzMotionRight = ttchzMotionRight;
	}

	@Column(name = "ERROR_MOTION_SUM", precision = 10, scale = 0)
	public Long getErrorMotionSum() {
		return this.errorMotionSum;
	}

	public void setErrorMotionSum(Long errorMotionSum) {
		this.errorMotionSum = errorMotionSum;
	}

	@Column(name = "ERROR_MOTION_RIGHT", precision = 10, scale = 0)
	public Long getErrorMotionRight() {
		return this.errorMotionRight;
	}

	public void setErrorMotionRight(Long errorMotionRight) {
		this.errorMotionRight = errorMotionRight;
	}

	@Column(name = "MANAGER_BY", length = 30)
	public String getManagerBy() {
		return this.managerBy;
	}

	public void setManagerBy(String managerBy) {
		this.managerBy = managerBy;
	}

	@Column(name = "PROTECTED_BY", length = 30)
	public String getProtectedBy() {
		return this.protectedBy;
	}

	public void setProtectedBy(String protectedBy) {
		this.protectedBy = protectedBy;
	}

	@Column(name = "APPROVE_BY", length = 30)
	public String getApproveBy() {
		return this.approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}