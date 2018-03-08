package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJEducation entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_EDUCATION")
public class HrJEducation implements java.io.Serializable {

	// Fields

	private Long educationid;
	private Long languageCodeId;
	private Long empId;
	private Long schoolCodeId;
	private Long specialtyCodeId;
	private Long studyTypeCodeId;
	private Date enrollmentDate;
	private String studyLimit;
	private String studyCode;
	private String ifGraduate;
	private Date graduateDate;
	private Double studyMoney;
	private String educationResult;
	private String certificateCode;
	private String ifOriginality;
	private String ifHightestXl;
	private String memo;
	private String insertby;
	private Date insertdate;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Long educationId;
	private Long degreeId;
	private String graduateSchool; // add by sychen 20100709
	private String specialty;// add by sychen 20100709
	
	private String category;//CATEGORY

	// Constructors

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	/** default constructor */
	public HrJEducation() {
	}

	/** minimal constructor */
	public HrJEducation(Long educationid, Long educationId, Long degreeId) {
		this.educationid = educationid;
		this.educationId = educationId;
		this.degreeId = degreeId;
	}

	/** full constructor */
	public HrJEducation(Long educationid, Long languageCodeId, Long empId,
			Long schoolCodeId, Long specialtyCodeId, Long studyTypeCodeId,
			Date enrollmentDate, String studyLimit, String studyCode,
			String ifGraduate, Date graduateDate, Double studyMoney,
			String educationResult, String certificateCode,
			String ifOriginality, String ifHightestXl, String memo,
			String insertby, Date insertdate, String enterpriseCode,
			String isUse, String lastModifiedBy, Date lastModifiedDate,
			Long educationId, Long degreeId) {
		this.educationid = educationid;
		this.languageCodeId = languageCodeId;
		this.empId = empId;
		this.schoolCodeId = schoolCodeId;
		this.specialtyCodeId = specialtyCodeId;
		this.studyTypeCodeId = studyTypeCodeId;
		this.enrollmentDate = enrollmentDate;
		this.studyLimit = studyLimit;
		this.studyCode = studyCode;
		this.ifGraduate = ifGraduate;
		this.graduateDate = graduateDate;
		this.studyMoney = studyMoney;
		this.educationResult = educationResult;
		this.certificateCode = certificateCode;
		this.ifOriginality = ifOriginality;
		this.ifHightestXl = ifHightestXl;
		this.memo = memo;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.educationId = educationId;
		this.degreeId = degreeId;
	}

	// Property accessors
	@Id
	@Column(name = "EDUCATIONID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEducationid() {
		return this.educationid;
	}

	public void setEducationid(Long educationid) {
		this.educationid = educationid;
	}

	@Column(name = "LANGUAGE_CODE_ID", precision = 10, scale = 0)
	public Long getLanguageCodeId() {
		return this.languageCodeId;
	}

	public void setLanguageCodeId(Long languageCodeId) {
		this.languageCodeId = languageCodeId;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "SCHOOL_CODE_ID", precision = 10, scale = 0)
	public Long getSchoolCodeId() {
		return this.schoolCodeId;
	}

	public void setSchoolCodeId(Long schoolCodeId) {
		this.schoolCodeId = schoolCodeId;
	}

	@Column(name = "SPECIALTY_CODE_ID", precision = 10, scale = 0)
	public Long getSpecialtyCodeId() {
		return this.specialtyCodeId;
	}

	public void setSpecialtyCodeId(Long specialtyCodeId) {
		this.specialtyCodeId = specialtyCodeId;
	}

	@Column(name = "STUDY_TYPE_CODE_ID", precision = 10, scale = 0)
	public Long getStudyTypeCodeId() {
		return this.studyTypeCodeId;
	}

	public void setStudyTypeCodeId(Long studyTypeCodeId) {
		this.studyTypeCodeId = studyTypeCodeId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENROLLMENT_DATE", length = 7)
	public Date getEnrollmentDate() {
		return this.enrollmentDate;
	}

	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	@Column(name = "STUDY_LIMIT", length = 8)
	public String getStudyLimit() {
		return this.studyLimit;
	}

	public void setStudyLimit(String studyLimit) {
		this.studyLimit = studyLimit;
	}

	@Column(name = "STUDY_CODE", length = 10)
	public String getStudyCode() {
		return this.studyCode;
	}

	public void setStudyCode(String studyCode) {
		this.studyCode = studyCode;
	}

	@Column(name = "IF_GRADUATE", length = 1)
	public String getIfGraduate() {
		return this.ifGraduate;
	}

	public void setIfGraduate(String ifGraduate) {
		this.ifGraduate = ifGraduate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "GRADUATE_DATE", length = 7)
	public Date getGraduateDate() {
		return this.graduateDate;
	}

	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}

	@Column(name = "STUDY_MONEY", precision = 15, scale = 4)
	public Double getStudyMoney() {
		return this.studyMoney;
	}

	public void setStudyMoney(Double studyMoney) {
		this.studyMoney = studyMoney;
	}

	@Column(name = "EDUCATION_RESULT", length = 1)
	public String getEducationResult() {
		return this.educationResult;
	}

	public void setEducationResult(String educationResult) {
		this.educationResult = educationResult;
	}

	@Column(name = "CERTIFICATE_CODE", length = 20)
	public String getCertificateCode() {
		return this.certificateCode;
	}

	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}

	@Column(name = "IF_ORIGINALITY", length = 1)
	public String getIfOriginality() {
		return this.ifOriginality;
	}

	public void setIfOriginality(String ifOriginality) {
		this.ifOriginality = ifOriginality;
	}

	@Column(name = "IF_HIGHTEST_XL", length = 1)
	public String getIfHightestXl() {
		return this.ifHightestXl;
	}

	public void setIfHightestXl(String ifHightestXl) {
		this.ifHightestXl = ifHightestXl;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "INSERTBY", length = 16)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
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

	@Column(name = "EDUCATION_ID", precision = 10, scale = 0)
	public Long getEducationId() {
		return this.educationId;
	}

	public void setEducationId(Long educationId) {
		this.educationId = educationId;
	}

	@Column(name = "DEGREE_ID", precision = 10, scale = 0)
	public Long getDegreeId() {
		return this.degreeId;
	}

	public void setDegreeId(Long degreeId) {
		this.degreeId = degreeId;
	}

	@Column(name = "GRADUATE_SCHOOL", length = 20)
	public String getGraduateSchool() {
		return graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	@Column(name = "SPECIALTY", length = 20)
	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

}