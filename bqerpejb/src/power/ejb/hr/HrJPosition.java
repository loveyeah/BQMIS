package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJPosition entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_POSITION", schema = "POWER")
public class HrJPosition implements java.io.Serializable {

	// Fields

	private Long positionId;
	private Long empId;
	private Date rmDate;
	private String positionName;
	private String isPosition;
	private String positionCode;
	private String isNow;
	private Long positionLevel;
	private String approveDept;
	private Long rmMode;
	private String rmReason;
	private String rmView;
	private String memo;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJPosition() {
	}

	/** minimal constructor */
	public HrJPosition(Long positionId) {
		this.positionId = positionId;
	}

	/** full constructor */
	public HrJPosition(Long positionId, Long empId, Date rmDate,
			String positionName, String isPosition, String positionCode,
			String isNow, Long positionLevel, String approveDept, Long rmMode,
			String rmReason, String rmView, String memo, String lastModifiedBy,
			Date lastModifiedDate, String isUse, String enterpriseCode) {
		this.positionId = positionId;
		this.empId = empId;
		this.rmDate = rmDate;
		this.positionName = positionName;
		this.isPosition = isPosition;
		this.positionCode = positionCode;
		this.isNow = isNow;
		this.positionLevel = positionLevel;
		this.approveDept = approveDept;
		this.rmMode = rmMode;
		this.rmReason = rmReason;
		this.rmView = rmView;
		this.memo = memo;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "POSITION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPositionId() {
		return this.positionId;
	}

	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "RM_DATE", length = 7)
	public Date getRmDate() {
		return this.rmDate;
	}

	public void setRmDate(Date rmDate) {
		this.rmDate = rmDate;
	}

	@Column(name = "POSITION_NAME", length = 50)
	public String getPositionName() {
		return this.positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	@Column(name = "IS_POSITION", length = 1)
	public String getIsPosition() {
		return this.isPosition;
	}

	public void setIsPosition(String isPosition) {
		this.isPosition = isPosition;
	}

	@Column(name = "POSITION_CODE", length = 100)
	public String getPositionCode() {
		return this.positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	@Column(name = "IS_NOW", length = 1)
	public String getIsNow() {
		return this.isNow;
	}

	public void setIsNow(String isNow) {
		this.isNow = isNow;
	}

	@Column(name = "POSITION_LEVEL", precision = 10, scale = 0)
	public Long getPositionLevel() {
		return this.positionLevel;
	}

	public void setPositionLevel(Long positionLevel) {
		this.positionLevel = positionLevel;
	}

	@Column(name = "APPROVE_DEPT", length = 20)
	public String getApproveDept() {
		return this.approveDept;
	}

	public void setApproveDept(String approveDept) {
		this.approveDept = approveDept;
	}

	@Column(name = "RM_MODE", precision = 10, scale = 0)
	public Long getRmMode() {
		return this.rmMode;
	}

	public void setRmMode(Long rmMode) {
		this.rmMode = rmMode;
	}

	@Column(name = "RM_REASON", length = 100)
	public String getRmReason() {
		return this.rmReason;
	}

	public void setRmReason(String rmReason) {
		this.rmReason = rmReason;
	}

	@Column(name = "RM_VIEW", length = 100)
	public String getRmView() {
		return this.rmView;
	}

	public void setRmView(String rmView) {
		this.rmView = rmView;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 16)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}