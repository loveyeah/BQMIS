package power.ejb.hr.form;

import java.util.Date;

import power.ejb.hr.HrJProfessionalPost;

public class ProfessionalPostForm
{
//	private HrJProfessionalPost post;
	private Long id;
	private Long empId;
//	private Date recruitmentDate;
	private Long professionalPost;
	private Long professionalLevel;
	private String isNow;
	private String judgeMode;
//	private Date validStartDate;
//	private Date validEndDate;
	private String certificateName;
	private String certificateCode;
	private String certificateDept;
	private String approveCode;
	private String judgeApproveDept;
	private String memo;
	
	
	
	private String empName;
	private String technologyName;
	private String recruitmentDateString;
	private String validStartDateString;
	private String validEndDateString;
//	public HrJProfessionalPost getPost() {
//		return post;
//	}
//	public void setPost(HrJProfessionalPost post) {
//		this.post = post;
//	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getTechnologyName() {
		return technologyName;
	}
	public void setTechnologyName(String technologyName) {
		this.technologyName = technologyName;
	}
	public String getRecruitmentDateString() {
		return recruitmentDateString;
	}
	public void setRecruitmentDateString(String recruitmentDateString) {
		this.recruitmentDateString = recruitmentDateString;
	}
	public String getValidStartDateString() {
		return validStartDateString;
	}
	public void setValidStartDateString(String validStartDateString) {
		this.validStartDateString = validStartDateString;
	}
	public String getValidEndDateString() {
		return validEndDateString;
	}
	public void setValidEndDateString(String validEndDateString) {
		this.validEndDateString = validEndDateString;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
//	public Date getRecruitmentDate() {
//		return recruitmentDate;
//	}
//	public void setRecruitmentDate(Date recruitmentDate) {
//		this.recruitmentDate = recruitmentDate;
//	}
	public Long getProfessionalPost() {
		return professionalPost;
	}
	public void setProfessionalPost(Long professionalPost) {
		this.professionalPost = professionalPost;
	}
	public Long getProfessionalLevel() {
		return professionalLevel;
	}
	public void setProfessionalLevel(Long professionalLevel) {
		this.professionalLevel = professionalLevel;
	}
	public String getIsNow() {
		return isNow;
	}
	public void setIsNow(String isNow) {
		this.isNow = isNow;
	}
	public String getJudgeMode() {
		return judgeMode;
	}
	public void setJudgeMode(String judgeMode) {
		this.judgeMode = judgeMode;
	}
//	public Date getValidStartDate() {
//		return validStartDate;
//	}
//	public void setValidStartDate(Date validStartDate) {
//		this.validStartDate = validStartDate;
//	}
//	public Date getValidEndDate() {
//		return validEndDate;
//	}
//	public void setValidEndDate(Date validEndDate) {
//		this.validEndDate = validEndDate;
//	}
	public String getCertificateName() {
		return certificateName;
	}
	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}
	public String getCertificateCode() {
		return certificateCode;
	}
	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}
	public String getCertificateDept() {
		return certificateDept;
	}
	public void setCertificateDept(String certificateDept) {
		this.certificateDept = certificateDept;
	}
	public String getApproveCode() {
		return approveCode;
	}
	public void setApproveCode(String approveCode) {
		this.approveCode = approveCode;
	}
	public String getJudgeApproveDept() {
		return judgeApproveDept;
	}
	public void setJudgeApproveDept(String judgeApproveDept) {
		this.judgeApproveDept = judgeApproveDept;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}