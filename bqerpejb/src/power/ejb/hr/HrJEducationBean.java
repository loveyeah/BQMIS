/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.ejb.hr;

/**
 * 员工档案综合查询维护 员工学历教育登记自定义Bean
 * 
 * @author huyou
 * @version 1.0
 */
public class HrJEducationBean implements java.io.Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 语种编码.语种名称
	 */
	private String languageName = null;

	/**
	 * 学历编码.学历名称
	 */
	private String educationName = null;

	/**
	 * 是否毕业
	 */
	private String ifGraduate = null;

	/**
	 * 学习类别维护.学习类别
	 */
	private String studyType = null;

	/**
	 * 入学日期
	 */
	private String enrollmentDate = null;

	/**
	 * 学习类别维护.学习类别编码Id
	 */
	private Long studyTypeCodeId = null;

	/**
	 * 学号
	 */
	private String studyCode = null;

	/**
	 * 毕业日期
	 */
	private String graduateDate = null;

	/**
	 * 培训费用
	 */
	private Double studyMoney = null;

	/**
	 * 备注
	 */
	private String memo = null;

	/**
	 * 学位编码.学位名称
	 */
	private String degreeName = null;

	/**
	 * 证书号码
	 */
	private String certificateCode = null;

	/**
	 * 学位编码.学位Id
	 */
	private Long degreeId = null;

	/**
	 * 学校编码.学校编码Id
	 */
	private Long schoolCodeId = null;

	/**
	 * 学历教育ID
	 */
	private Long educationid = null;

	/**
	 * 是否原始学历
	 */
	private String ifOriginality = null;

	/**
	 * 是否最高学历
	 */
	private String ifHightestXl = null;

	/**
	 * 学习专业编码.学习专业Id
	 */
	private Long specialtyCodeId = null;

	/**
	 * 学制
	 */
	private String studyLimit = null;

	/**
	 * 学校编码.学校名称
	 */
	private String schoolName = null;

	/**
	 * 语种编码.语种编码Id
	 */
	private Long languageCodeId = null;

	/**
	 * 教育结果
	 */
	private String educationResult = null;

	/**
	 * 上次修改时间
	 */
	private String lastModifiedDate = null;

	/**
	 * 学习专业编码.学习专业名称
	 */
	private String specialtyName = null;

	/**
	 * 学历编码.学历Id
	 */
	private Long educationId = null;
	
	private String category = null;//CATEGORY
	
	private String graduateSchool = null;//add by sychen 20100709
	
	private String specialty = null;//add by sychen 20100709
	
	private String newEmpCode = null;//add by sychen 20100713
	/**
	 * 取得语种编码.语种名称
	 *
	 * @return 语种编码.语种名称
	 */
	public String getLanguageName() {
		return languageName;
	}

	/**
	 * 设置语种编码.语种名称
	 *
	 * @param argLanguageName 语种编码.语种名称
	 */
	public void setLanguageName(String argLanguageName) {
		languageName = argLanguageName;
	}

	/**
	 * 取得学历编码.学历名称
	 *
	 * @return 学历编码.学历名称
	 */
	public String getEducationName() {
		return educationName;
	}

	/**
	 * 设置学历编码.学历名称
	 *
	 * @param argEducationName 学历编码.学历名称
	 */
	public void setEducationName(String argEducationName) {
		educationName = argEducationName;
	}

	/**
	 * 取得是否毕业
	 *
	 * @return 是否毕业
	 */
	public String getIfGraduate() {
		return ifGraduate;
	}

	/**
	 * 设置是否毕业
	 *
	 * @param argIfGraduate 是否毕业
	 */
	public void setIfGraduate(String argIfGraduate) {
		ifGraduate = argIfGraduate;
	}

	/**
	 * 取得学习类别维护.学习类别
	 *
	 * @return 学习类别维护.学习类别
	 */
	public String getStudyType() {
		return studyType;
	}

	/**
	 * 设置学习类别维护.学习类别
	 *
	 * @param argStudyType 学习类别维护.学习类别
	 */
	public void setStudyType(String argStudyType) {
		studyType = argStudyType;
	}

	/**
	 * 取得入学日期
	 *
	 * @return 入学日期
	 */
	public String getEnrollmentDate() {
		return enrollmentDate;
	}

	/**
	 * 设置入学日期
	 *
	 * @param argEnrollmentDate 入学日期
	 */
	public void setEnrollmentDate(String argEnrollmentDate) {
		enrollmentDate = argEnrollmentDate;
	}

	/**
	 * 取得学习类别维护.学习类别编码Id
	 *
	 * @return 学习类别维护.学习类别编码Id
	 */
	public Long getStudyTypeCodeId() {
		return studyTypeCodeId;
	}

	/**
	 * 设置学习类别维护.学习类别编码Id
	 *
	 * @param argStudyTypeCodeId 学习类别维护.学习类别编码Id
	 */
	public void setStudyTypeCodeId(Long argStudyTypeCodeId) {
		studyTypeCodeId = argStudyTypeCodeId;
	}

	/**
	 * 取得学号
	 *
	 * @return 学号
	 */
	public String getStudyCode() {
		return studyCode;
	}

	/**
	 * 设置学号
	 *
	 * @param argStudyCode 学号
	 */
	public void setStudyCode(String argStudyCode) {
		studyCode = argStudyCode;
	}

	/**
	 * 取得毕业日期
	 *
	 * @return 毕业日期
	 */
	public String getGraduateDate() {
		return graduateDate;
	}

	/**
	 * 设置毕业日期
	 *
	 * @param argGraduateDate 毕业日期
	 */
	public void setGraduateDate(String argGraduateDate) {
		graduateDate = argGraduateDate;
	}

	/**
	 * 取得培训费用
	 *
	 * @return 培训费用
	 */
	public Double getStudyMoney() {
		return studyMoney;
	}

	/**
	 * 设置培训费用
	 *
	 * @param argStudyMoney 培训费用
	 */
	public void setStudyMoney(Double argStudyMoney) {
		studyMoney = argStudyMoney;
	}

	/**
	 * 取得备注
	 *
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 *
	 * @param argMemo 备注
	 */
	public void setMemo(String argMemo) {
		memo = argMemo;
	}

	/**
	 * 取得学位编码.学位名称
	 *
	 * @return 学位编码.学位名称
	 */
	public String getDegreeName() {
		return degreeName;
	}

	/**
	 * 设置学位编码.学位名称
	 *
	 * @param argDegreeName 学位编码.学位名称
	 */
	public void setDegreeName(String argDegreeName) {
		degreeName = argDegreeName;
	}

	/**
	 * 取得证书号码
	 *
	 * @return 证书号码
	 */
	public String getCertificateCode() {
		return certificateCode;
	}

	/**
	 * 设置证书号码
	 *
	 * @param argCertificateCode 证书号码
	 */
	public void setCertificateCode(String argCertificateCode) {
		certificateCode = argCertificateCode;
	}

	/**
	 * 取得学位编码.学位Id
	 *
	 * @return 学位编码.学位Id
	 */
	public Long getDegreeId() {
		return degreeId;
	}

	/**
	 * 设置学位编码.学位Id
	 *
	 * @param argDegreeId 学位编码.学位Id
	 */
	public void setDegreeId(Long argDegreeId) {
		degreeId = argDegreeId;
	}

	/**
	 * 取得学校编码.学校编码Id
	 *
	 * @return 学校编码.学校编码Id
	 */
	public Long getSchoolCodeId() {
		return schoolCodeId;
	}

	/**
	 * 设置学校编码.学校编码Id
	 *
	 * @param argSchoolCodeId 学校编码.学校编码Id
	 */
	public void setSchoolCodeId(Long argSchoolCodeId) {
		schoolCodeId = argSchoolCodeId;
	}

	/**
	 * 取得学历教育ID
	 *
	 * @return 学历教育ID
	 */
	public Long getEducationid() {
		return educationid;
	}

	/**
	 * 设置学历教育ID
	 *
	 * @param argEducationid 学历教育ID
	 */
	public void setEducationid(Long argEducationid) {
		educationid = argEducationid;
	}

	/**
	 * 取得是否原始学历
	 *
	 * @return 是否原始学历
	 */
	public String getIfOriginality() {
		return ifOriginality;
	}

	/**
	 * 设置是否原始学历
	 *
	 * @param argIfOriginality 是否原始学历
	 */
	public void setIfOriginality(String argIfOriginality) {
		ifOriginality = argIfOriginality;
	}

	/**
	 * 取得是否最高学历
	 *
	 * @return 是否最高学历
	 */
	public String getIfHightestXl() {
		return ifHightestXl;
	}

	/**
	 * 设置是否最高学历
	 *
	 * @param argIfHightestXl 是否最高学历
	 */
	public void setIfHightestXl(String argIfHightestXl) {
		ifHightestXl = argIfHightestXl;
	}

	/**
	 * 取得学习专业编码.学习专业Id
	 *
	 * @return 学习专业编码.学习专业Id
	 */
	public Long getSpecialtyCodeId() {
		return specialtyCodeId;
	}

	/**
	 * 设置学习专业编码.学习专业Id
	 *
	 * @param argSpecialtyCodeId 学习专业编码.学习专业Id
	 */
	public void setSpecialtyCodeId(Long argSpecialtyCodeId) {
		specialtyCodeId = argSpecialtyCodeId;
	}

	/**
	 * 取得学制
	 *
	 * @return 学制
	 */
	public String getStudyLimit() {
		return studyLimit;
	}

	/**
	 * 设置学制
	 *
	 * @param argStudyLimit 学制
	 */
	public void setStudyLimit(String argStudyLimit) {
		studyLimit = argStudyLimit;
	}

	/**
	 * 取得学校编码.学校名称
	 *
	 * @return 学校编码.学校名称
	 */
	public String getSchoolName() {
		return schoolName;
	}

	/**
	 * 设置学校编码.学校名称
	 *
	 * @param argSchoolName 学校编码.学校名称
	 */
	public void setSchoolName(String argSchoolName) {
		schoolName = argSchoolName;
	}

	/**
	 * 取得语种编码.语种编码Id
	 *
	 * @return 语种编码.语种编码Id
	 */
	public Long getLanguageCodeId() {
		return languageCodeId;
	}

	/**
	 * 设置语种编码.语种编码Id
	 *
	 * @param argLanguageCodeId 语种编码.语种编码Id
	 */
	public void setLanguageCodeId(Long argLanguageCodeId) {
		languageCodeId = argLanguageCodeId;
	}

	/**
	 * 取得教育结果
	 *
	 * @return 教育结果
	 */
	public String getEducationResult() {
		return educationResult;
	}

	/**
	 * 设置教育结果
	 *
	 * @param argEducationResult 教育结果
	 */
	public void setEducationResult(String argEducationResult) {
		educationResult = argEducationResult;
	}

	/**
	 * 取得上次修改时间
	 *
	 * @return 上次修改时间
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * 设置上次修改时间
	 *
	 * @param argLastModifiedDate 上次修改时间
	 */
	public void setLastModifiedDate(String argLastModifiedDate) {
		lastModifiedDate = argLastModifiedDate;
	}

	/**
	 * 取得学习专业编码.学习专业名称
	 *
	 * @return 学习专业编码.学习专业名称
	 */
	public String getSpecialtyName() {
		return specialtyName;
	}

	/**
	 * 设置学习专业编码.学习专业名称
	 *
	 * @param argSpecialtyName 学习专业编码.学习专业名称
	 */
	public void setSpecialtyName(String argSpecialtyName) {
		specialtyName = argSpecialtyName;
	}

	/**
	 * 取得学历编码.学历Id
	 *
	 * @return 学历编码.学历Id
	 */
	public Long getEducationId() {
		return educationId;
	}

	/**
	 * 设置学历编码.学历Id
	 *
	 * @param argEducationId 学历编码.学历Id
	 */
	public void setEducationId(Long argEducationId) {
		educationId = argEducationId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getGraduateSchool() {
		return graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getNewEmpCode() {
		return newEmpCode;
	}

	public void setNewEmpCode(String newEmpCode) {
		this.newEmpCode = newEmpCode;
	}


}
