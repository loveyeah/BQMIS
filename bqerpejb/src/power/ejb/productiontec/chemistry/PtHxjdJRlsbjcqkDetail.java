package power.ejb.productiontec.chemistry;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtHxjdJRlsbjcqkDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_HXJD_J_RLSBJCQK_DETAIL")
public class PtHxjdJRlsbjcqkDetail implements java.io.Serializable {

	// Fields

	private Long rlsbjcDetailId;
	private Long rlsbjcId;
	private String equCode;
	private Date repairDate;
	private Double courseNumber;
	private String repairType;
	private Long repairNumber;
	private String checkHigh;
	private String checkName;
	private String checkPart;
	private String dirtyCapacity;
	private String sedimentQuantity;
	private String memo;
	private String enterpriseCode;
	
	// Constructors

	/** default constructor */
	public PtHxjdJRlsbjcqkDetail() {
	}

	/** minimal constructor */
	public PtHxjdJRlsbjcqkDetail(Long rlsbjcDetailId) {
		this.rlsbjcDetailId = rlsbjcDetailId;
	}

	/** full constructor */
	public PtHxjdJRlsbjcqkDetail(Long rlsbjcDetailId, Long rlsbjcId,
			String equCode, Date repairDate, Double courseNumber,
			String repairType, Long repairNumber, String checkHigh,
			String checkName, String checkPart, String dirtyCapacity,
			String sedimentQuantity, String memo,String enterpriseCode) {
		this.rlsbjcDetailId = rlsbjcDetailId;
		this.rlsbjcId = rlsbjcId;
		this.equCode = equCode;
		this.repairDate = repairDate;
		this.courseNumber = courseNumber;
		this.repairType = repairType;
		this.repairNumber = repairNumber;
		this.checkHigh = checkHigh;
		this.checkName = checkName;
		this.checkPart = checkPart;
		this.dirtyCapacity = dirtyCapacity;
		this.sedimentQuantity = sedimentQuantity;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RLSBJC_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRlsbjcDetailId() {
		return this.rlsbjcDetailId;
	}

	public void setRlsbjcDetailId(Long rlsbjcDetailId) {
		this.rlsbjcDetailId = rlsbjcDetailId;
	}

	@Column(name = "RLSBJC_ID", precision = 10, scale = 0)
	public Long getRlsbjcId() {
		return this.rlsbjcId;
	}

	public void setRlsbjcId(Long rlsbjcId) {
		this.rlsbjcId = rlsbjcId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REPAIR_DATE", length = 7)
	public Date getRepairDate() {
		return this.repairDate;
	}

	public void setRepairDate(Date repairDate) {
		this.repairDate = repairDate;
	}

	@Column(name = "COURSE_NUMBER", precision = 10)
	public Double getCourseNumber() {
		return this.courseNumber;
	}

	public void setCourseNumber(Double courseNumber) {
		this.courseNumber = courseNumber;
	}

	@Column(name = "REPAIR_TYPE", length = 10)
	public String getRepairType() {
		return this.repairType;
	}

	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}

	@Column(name = "REPAIR_NUMBER", precision = 4, scale = 0)
	public Long getRepairNumber() {
		return this.repairNumber;
	}

	public void setRepairNumber(Long repairNumber) {
		this.repairNumber = repairNumber;
	}

	@Column(name = "CHECK_HIGH", length = 100)
	public String getCheckHigh() {
		return this.checkHigh;
	}

	public void setCheckHigh(String checkHigh) {
		this.checkHigh = checkHigh;
	}

	@Column(name = "CHECK_NAME", length = 100)
	public String getCheckName() {
		return this.checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	@Column(name = "CHECK_PART", length = 100)
	public String getCheckPart() {
		return this.checkPart;
	}

	public void setCheckPart(String checkPart) {
		this.checkPart = checkPart;
	}

	@Column(name = "DIRTY_CAPACITY", length = 100)
	public String getDirtyCapacity() {
		return this.dirtyCapacity;
	}

	public void setDirtyCapacity(String dirtyCapacity) {
		this.dirtyCapacity = dirtyCapacity;
	}

	@Column(name = "SEDIMENT_QUANTITY", length = 100)
	public String getSedimentQuantity() {
		return this.sedimentQuantity;
	}

	public void setSedimentQuantity(String sedimentQuantity) {
		this.sedimentQuantity = sedimentQuantity;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	
	@Column(name = "EQU_CODE", length = 30)
	public String getEquCode() {
		return equCode;
	}

	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}

}