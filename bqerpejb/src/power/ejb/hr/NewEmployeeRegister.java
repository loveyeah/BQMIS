/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

/**
 * 新进员工登记Bean
 * 
 * @author jincong
 * @version 1.0
 */
public class NewEmployeeRegister implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	// 人员基本信息表
	/** 员工ID */
	private String empId;
	/** 员工编码 */
	private String empCode;
	/** 出生日期 */
	private String birthday;
	/** 性别 */
	private String sex;
	/** 中文名 */
	private String chsName;
	/** 婚否状况 */
	private String isWedded;
	/** 英文名 */
	private String enName;
	/** 身份证 */
	private String identityCard;
	/** 入职时间 */
	private String missionDate;
	/** 试用期开始日期 */
	private String tryoutStartDate;
	/** 试用期终止日期 */
	private String tryoutEndDate;
	/** 员工状态 */
	private String empState;
	/** 毕业时间 */
	private String graduateDate;
	/** 是否退转军人 */
	private String isVeteran;
	/** 员工描述 */
	private String memo;
	/** 上次修改时间 */
	private String lastModifiyDate;
	
	// 籍贯编码表
	/** 籍贯编码ID */
	private String nativePlaceId;
	
	// 民族编码表
	/** 民族ID */
	private String nationCodeId;
	
	// 政治面貌表
	/** 政治面貌ID */
	private String politicsId;
	
	// 部门设置表
	/** 部门名称 */
	private String deptName;
	/** 部门ID */
	private String deptId;
	
	// 岗位设置表
	/** 岗位名称 */
	private String stationName;
	/** 岗位ID */
	private String stationId;
	
	// 员工类别设置
	/** 员工类别ID */
	private String empTypeId;
	/** 员工类别名称 */
	private String empTypeName;
	
	// 进厂类别维护
	/** 进厂类别ID */
	private String inTypeId;
	/** 进厂类别 */
	private String inType;
	
	// 学历编码
	/** 学历ID */
	private String educationId;
	
	// 学习专业编码
	/** 学习专业编码ID */
	private String specialtyCodeId;
	
	// 学位编码
	/** 学位ID */
	private String degreeId;
	
	// 学校编码
	/** 学校编码ID */
	private String schoolCodeId;

	/** 新工号*/
	private String newEmpCode;
	/**
	 * @return the empCode
	 */
	public String getEmpCode() {
		return empCode;
	}

	/**
	 * @param empCode the empCode to set
	 */
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	/**
	 * @return the birthday
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the chsName
	 */
	public String getChsName() {
		return chsName;
	}

	/**
	 * @param chsName the chsName to set
	 */
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}

	/**
	 * @return the isWedded
	 */
	public String getIsWedded() {
		return isWedded;
	}

	/**
	 * @param isWedded the isWedded to set
	 */
	public void setIsWedded(String isWedded) {
		this.isWedded = isWedded;
	}

	/**
	 * @return the enName
	 */
	public String getEnName() {
		return enName;
	}

	/**
	 * @param enName the enName to set
	 */
	public void setEnName(String enName) {
		this.enName = enName;
	}

	/**
	 * @return the identityCard
	 */
	public String getIdentityCard() {
		return identityCard;
	}

	/**
	 * @param identityCard the identityCard to set
	 */
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	/**
	 * @return the missionDate
	 */
	public String getMissionDate() {
		return missionDate;
	}

	/**
	 * @param missionDate the missionDate to set
	 */
	public void setMissionDate(String missionDate) {
		this.missionDate = missionDate;
	}

	/**
	 * @return the tryoutStartDate
	 */
	public String getTryoutStartDate() {
		return tryoutStartDate;
	}

	/**
	 * @param tryoutStartDate the tryoutStartDate to set
	 */
	public void setTryoutStartDate(String tryoutStartDate) {
		this.tryoutStartDate = tryoutStartDate;
	}

	/**
	 * @return the tryoutEndDate
	 */
	public String getTryoutEndDate() {
		return tryoutEndDate;
	}

	/**
	 * @param tryoutEndDate the tryoutEndDate to set
	 */
	public void setTryoutEndDate(String tryoutEndDate) {
		this.tryoutEndDate = tryoutEndDate;
	}

	/**
	 * @return the empState
	 */
	public String getEmpState() {
		return empState;
	}

	/**
	 * @param empState the empState to set
	 */
	public void setEmpState(String empState) {
		this.empState = empState;
	}

	/**
	 * @return the graduateDate
	 */
	public String getGraduateDate() {
		return graduateDate;
	}

	/**
	 * @param graduateDate the graduateDate to set
	 */
	public void setGraduateDate(String graduateDate) {
		this.graduateDate = graduateDate;
	}

	/**
	 * @return the isVeteran
	 */
	public String getIsVeteran() {
		return isVeteran;
	}

	/**
	 * @param isVeteran the isVeteran to set
	 */
	public void setIsVeteran(String isVeteran) {
		this.isVeteran = isVeteran;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the nativePlaceId
	 */
	public String getNativePlaceId() {
		return nativePlaceId;
	}

	/**
	 * @param nativePlaceId the nativePlaceId to set
	 */
	public void setNativePlaceId(String nativePlaceId) {
		this.nativePlaceId = nativePlaceId;
	}

	/**
	 * @return the nationCodeId
	 */
	public String getNationCodeId() {
		return nationCodeId;
	}

	/**
	 * @param nationCodeId the nationCodeId to set
	 */
	public void setNationCodeId(String nationCodeId) {
		this.nationCodeId = nationCodeId;
	}

	/**
	 * @return the politicsId
	 */
	public String getPoliticsId() {
		return politicsId;
	}

	/**
	 * @param politicsId the politicsId to set
	 */
	public void setPoliticsId(String politicsId) {
		this.politicsId = politicsId;
	}

	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * @return the deptId
	 */
	public String getDeptId() {
		return deptId;
	}

	/**
	 * @param deptId the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/**
	 * @return the stationName
	 */
	public String getStationName() {
		return stationName;
	}

	/**
	 * @param stationName the stationName to set
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	/**
	 * @return the stationId
	 */
	public String getStationId() {
		return stationId;
	}

	/**
	 * @param stationId the stationId to set
	 */
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	/**
	 * @return the empTypeId
	 */
	public String getEmpTypeId() {
		return empTypeId;
	}

	/**
	 * @param empTypeId the empTypeId to set
	 */
	public void setEmpTypeId(String empTypeId) {
		this.empTypeId = empTypeId;
	}

	/**
	 * @return the inTypeId
	 */
	public String getInTypeId() {
		return inTypeId;
	}

	/**
	 * @param inTypeId the inTypeId to set
	 */
	public void setInTypeId(String inTypeId) {
		this.inTypeId = inTypeId;
	}

	/**
	 * @return the educationId
	 */
	public String getEducationId() {
		return educationId;
	}

	/**
	 * @param educationId the educationId to set
	 */
	public void setEducationId(String educationId) {
		this.educationId = educationId;
	}

	/**
	 * @return the specialtyCodeId
	 */
	public String getSpecialtyCodeId() {
		return specialtyCodeId;
	}

	/**
	 * @param specialtyCodeId the specialtyCodeId to set
	 */
	public void setSpecialtyCodeId(String specialtyCodeId) {
		this.specialtyCodeId = specialtyCodeId;
	}

	/**
	 * @return the degreeId
	 */
	public String getDegreeId() {
		return degreeId;
	}

	/**
	 * @param degreeId the degreeId to set
	 */
	public void setDegreeId(String degreeId) {
		this.degreeId = degreeId;
	}

	/**
	 * @return the schoolCodeId
	 */
	public String getSchoolCodeId() {
		return schoolCodeId;
	}

	/**
	 * @param schoolCodeId the schoolCodeId to set
	 */
	public void setSchoolCodeId(String schoolCodeId) {
		this.schoolCodeId = schoolCodeId;
	}

	/**
	 * @return the empTypeName
	 */
	public String getEmpTypeName() {
		return empTypeName;
	}

	/**
	 * @param empTypeName the empTypeName to set
	 */
	public void setEmpTypeName(String empTypeName) {
		this.empTypeName = empTypeName;
	}

	/**
	 * @return the inType
	 */
	public String getInType() {
		return inType;
	}

	/**
	 * @param inType the inType to set
	 */
	public void setInType(String inType) {
		this.inType = inType;
	}

	/**
	 * @return the lastModifiyDate
	 */
	public String getLastModifiyDate() {
		return lastModifiyDate;
	}

	/**
	 * @param lastModifiyDate the lastModifiyDate to set
	 */
	public void setLastModifiyDate(String lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}

	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getNewEmpCode() {
		return newEmpCode;
	}

	public void setNewEmpCode(String newEmpCode) {
		this.newEmpCode = newEmpCode;
	}
}
