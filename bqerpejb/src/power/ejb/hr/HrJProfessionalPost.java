package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJProfessionalPost entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_PROFESSIONAL_POST", schema = "POWER")
public class HrJProfessionalPost implements java.io.Serializable {

	// Fields

	private Long id;
	private Long empId;
	private Date recruitmentDate;
	private Long professionalPost;
	private Long professionalLevel;
	private String isNow;
	private String judgeMode;
	private Date validStartDate;
	private Date validEndDate;
	private String certificateName;
	private String certificateCode;
	private String certificateDept;
	private String approveCode;
	private String judgeApproveDept;
	private String memo;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJProfessionalPost() {
	}

	/** minimal constructor */
	public HrJProfessionalPost(Long id) {
		this.id = id;
	}

	/** full constructor */
	public HrJProfessionalPost(Long id, Long empId, Date recruitmentDate,
			Long professionalPost, Long professionalLevel, String isNow,
			String judgeMode, Date validStartDate, Date validEndDate,
			String certificateName, String certificateCode,
			String certificateDept, String approveCode,
			String judgeApproveDept, String memo, String lastModifiedBy,
			Date lastModifiedDate, String isUse, String enterpriseCode) {
		this.id = id;
		this.empId = empId;
		this.recruitmentDate = recruitmentDate;
		this.professionalPost = professionalPost;
		this.professionalLevel = professionalLevel;
		this.isNow = isNow;
		this.judgeMode = judgeMode;
		this.validStartDate = validStartDate;
		this.validEndDate = validEndDate;
		this.certificateName = certificateName;
		this.certificateCode = certificateCode;
		this.certificateDept = certificateDept;
		this.approveCode = approveCode;
		this.judgeApproveDept = judgeApproveDept;
		this.memo = memo;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
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

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "RECRUITMENT_DATE", length = 7)
	public Date getRecruitmentDate() {
		return this.recruitmentDate;
	}

	public void setRecruitmentDate(Date recruitmentDate) {
		this.recruitmentDate = recruitmentDate;
	}

	@Column(name = "PROFESSIONAL_POST", precision = 10, scale = 0)
	public Long getProfessionalPost() {
		return this.professionalPost;
	}

	public void setProfessionalPost(Long professionalPost) {
		this.professionalPost = professionalPost;
	}

	@Column(name = "PROFESSIONAL_LEVEL", precision = 10, scale = 0)
	public Long getProfessionalLevel() {
		return this.professionalLevel;
	}

	public void setProfessionalLevel(Long professionalLevel) {
		this.professionalLevel = professionalLevel;
	}

	@Column(name = "IS_NOW", length = 1)
	public String getIsNow() {
		return this.isNow;
	}

	public void setIsNow(String isNow) {
		this.isNow = isNow;
	}

	@Column(name = "JUDGE_MODE", length = 1)
	public String getJudgeMode() {
		return this.judgeMode;
	}

	public void setJudgeMode(String judgeMode) {
		this.judgeMode = judgeMode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "VALID_START_DATE", length = 7)
	public Date getValidStartDate() {
		return this.validStartDate;
	}

	public void setValidStartDate(Date validStartDate) {
		this.validStartDate = validStartDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "VALID_END_DATE", length = 7)
	public Date getValidEndDate() {
		return this.validEndDate;
	}

	public void setValidEndDate(Date validEndDate) {
		this.validEndDate = validEndDate;
	}

	@Column(name = "CERTIFICATE_NAME", length = 100)
	public String getCertificateName() {
		return this.certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	@Column(name = "CERTIFICATE_CODE", length = 100)
	public String getCertificateCode() {
		return this.certificateCode;
	}

	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}

	@Column(name = "CERTIFICATE_DEPT", length = 100)
	public String getCertificateDept() {
		return this.certificateDept;
	}

	public void setCertificateDept(String certificateDept) {
		this.certificateDept = certificateDept;
	}

	@Column(name = "APPROVE_CODE", length = 100)
	public String getApproveCode() {
		return this.approveCode;
	}

	public void setApproveCode(String approveCode) {
		this.approveCode = approveCode;
	}

	@Column(name = "JUDGE_APPROVE_DEPT", length = 100)
	public String getJudgeApproveDept() {
		return this.judgeApproveDept;
	}

	public void setJudgeApproveDept(String judgeApproveDept) {
		this.judgeApproveDept = judgeApproveDept;
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