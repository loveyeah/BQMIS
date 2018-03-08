//package power.ejb.hr;
//
//import java.util.Date;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
///**
// * HrJEmpInfo entity.
// * 
// * @author MyEclipse Persistence Tools
// */
//@Entity
//@Table(name = "HR_J_EMP_INFO", schema = "power")
//public class HrJEmpInfo implements java.io.Serializable {
//
//	// Fields
//
//	private Long empId;
//	private String chsName;
//	private String enName;
//	private String empCode;
//	private Long deptId;
//	private String retrieveCode;
//	private Long nationId;
//	private Long nativePlaceId;
//	private Long politicsId;
//	private Date brithday;
//	private String sex;
//	private String isWedded;
//	private String archivesId;
//	private Long empStationId;
//	private Long stationId;
//	private Long stationLevel;
//	private Long gradation;
//	private Long empTypeId;
//	private Date workDate;
//	private Date missionDate;
//	private Date dimissionDate;
//	private Long technologyTitlesTypeId;
//	private Long technologyGradeId;
//	private Long typeOfWorkId;
//	private String identityCard;
//	private String timeCardId;
//	private String payCardId;
//	private String socialInsuranceId;
//	private Date socialInsuranceDate;
//	private String mobilePhone;
//	private String familyTel;
//	private String qq;
//	private String msn;
//	private String postalcode;
//	private String familyAddress;
//	private String officeTel1;
//	private String officeTel2;
//	private String fax;
//	private String EMail;
//	private String instancyMan;
//	private String instancyTel;
//	private String graduateSchool;
//	private Long educationId;
//	private Long degreeId;
//	private String speciality;
//	private Date graduateDate;
//	private String isVeteran;
//	private String isBorrow;
//	private String isRetired;
//	private String empState;
//	private Long orderBy;
//	private String recommendMan;
//	private Long assistantManagerUnitsId;
//	private String oneStrongSuit;
//	private String memo;
//	private Long createBy;
//	private Date createDate;
//	private Long lastModifiyBy;
//	private Date lastModifiyDate;
//	private String curriculumVitae;
//	private String societyInfo;
//	
//	// 标准岗级 add by liuyi 090922
//	private Long stationGrade;
//	//薪级 add by liuyi 090922
//	private Long salaryLevel;
//	// 执行岗级 add by liuyi 090922
//	private Long checkStationGrade;
//	//考勤部门id add by liuyi 090922
//	private Long attendanceDeptId;
//	//班组 add by liuyi 090922
//	private Long banzuId;
//	// 是否使用 add by liuyi 090922
//	private String isUse;
//	//企业编码 add by liuyi 090922
//	private String enterpriseCode;
//	
//	// 试用期开始时间 add by liuyi 091124
//	private Date tryoutStartDate;
//	// 试用期结束时间 add by liuyi 091124
//	private Date tryoutEndDate;
//	// 学习专业编码Id add by liuyi 091124
//	private Long specialtyCodeId;
//	// 毕业学校id
//	private Long graduateSchoolId;
//	// 民族id add by liuyi 091124
//	private Long nationCodeId;
//	// 进厂类别id add by liuyi 091124
//	private Long inTypeId;
//	// 员工身份Id add by liuyi 091125
//	private Long workId;
//	// 劳保工种Id add by liuyi 091125
//	private Long lbWorkId;
//	//退休日期  add by drdu 091223
//	private Date retirementDate;
//	// Constructors
//
//	/** default constructor */
//	public HrJEmpInfo() {
//	}
//
//	/** minimal constructor */
//	public HrJEmpInfo(Long empId) {
//		this.empId = empId;
//	}
//
//	/** full constructor */
//	public HrJEmpInfo(Long empId, String chsName, String enName,
//			String empCode, Long deptId, String retrieveCode, Long nationId,
//			Long nativePlaceId, Long politicsId, Date brithday, String sex,
//			String isWedded, String archivesId, Long empStationId,
//			Long stationId, Long stationLevel, Long gradation, Long empTypeId,
//			Date workDate, Date missionDate, Date dimissionDate,
//			Long technologyTitlesTypeId, Long technologyGradeId,
//			Long typeOfWorkId, String identityCard, String timeCardId,
//			String payCardId, String socialInsuranceId,
//			Date socialInsuranceDate, String mobilePhone, String familyTel,
//			String qq, String msn, String postalcode, String familyAddress,
//			String officeTel1, String officeTel2, String fax, String EMail,
//			String instancyMan, String instancyTel, String graduateSchool,
//			Long educationId, Long degreeId, String speciality,
//			Date graduateDate, String isVeteran, String isBorrow,
//			String isRetired, String empState, Long orderBy,
//			String recommendMan, Long assistantManagerUnitsId,
//			String oneStrongSuit, String memo, Long createBy, Date createDate,
//			Long lastModifiyBy, Date lastModifiyDate,String curriculumVitae,String societyInfo) {
//		this.empId = empId;
//		this.chsName = chsName;
//		this.enName = enName;
//		this.empCode = empCode;
//		this.deptId = deptId;
//		this.retrieveCode = retrieveCode;
//		this.nationId = nationId;
//		this.nativePlaceId = nativePlaceId;
//		this.politicsId = politicsId;
//		this.brithday = brithday;
//		this.sex = sex;
//		this.isWedded = isWedded;
//		this.archivesId = archivesId;
//		this.empStationId = empStationId;
//		this.stationId = stationId;
//		this.stationLevel = stationLevel;
//		this.gradation = gradation;
//		this.empTypeId = empTypeId;
//		this.workDate = workDate;
//		this.missionDate = missionDate;
//		this.dimissionDate = dimissionDate;
//		this.technologyTitlesTypeId = technologyTitlesTypeId;
//		this.technologyGradeId = technologyGradeId;
//		this.typeOfWorkId = typeOfWorkId;
//		this.identityCard = identityCard;
//		this.timeCardId = timeCardId;
//		this.payCardId = payCardId;
//		this.socialInsuranceId = socialInsuranceId;
//		this.socialInsuranceDate = socialInsuranceDate;
//		this.mobilePhone = mobilePhone;
//		this.familyTel = familyTel;
//		this.qq = qq;
//		this.msn = msn;
//		this.postalcode = postalcode;
//		this.familyAddress = familyAddress;
//		this.officeTel1 = officeTel1;
//		this.officeTel2 = officeTel2;
//		this.fax = fax;
//		this.EMail = EMail;
//		this.instancyMan = instancyMan;
//		this.instancyTel = instancyTel;
//		this.graduateSchool = graduateSchool;
//		this.educationId = educationId;
//		this.degreeId = degreeId;
//		this.speciality = speciality;
//		this.graduateDate = graduateDate;
//		this.isVeteran = isVeteran;
//		this.isBorrow = isBorrow;
//		this.isRetired = isRetired;
//		this.empState = empState;
//		this.orderBy = orderBy;
//		this.recommendMan = recommendMan;
//		this.assistantManagerUnitsId = assistantManagerUnitsId;
//		this.oneStrongSuit = oneStrongSuit;
//		this.memo = memo;
//		this.createBy = createBy;
//		this.createDate = createDate;
//		this.lastModifiyBy = lastModifiyBy;
//		this.lastModifiyDate = lastModifiyDate;
//		this.curriculumVitae = curriculumVitae;
//		this.societyInfo = societyInfo;
//	}
//
//	// Property accessors
//	@Id
//	@Column(name = "EMP_ID", unique = true, nullable = false, precision = 10, scale = 0)
//	public Long getEmpId() {
//		return this.empId;
//	}
//
//	public void setEmpId(Long empId) {
//		this.empId = empId;
//	}
//
//	@Column(name = "CHS_NAME", length = 25)
//	public String getChsName() {
//		return this.chsName;
//	}
//
//	public void setChsName(String chsName) {
//		this.chsName = chsName;
//	}
//	//新增履历列
//	@Column(name = "curriculum_Vitae",length=2000)
//	public String getCurriculumVitae()
//	{
//		return this.curriculumVitae;
//	}
//	public void setCurriculumVitae(String curriculumVitae)
//	{
//		this.curriculumVitae = curriculumVitae;
//	}
//	//新增社会关系列
//	@Column(name = "Society_Info",length=2000)
//	public String getSocietyInfo()
//	{
//		return this.societyInfo;
//	}
//	public void setSocietyInfo(String societyInfo)
//	{
//		this.societyInfo = societyInfo;
//	}
//	
//	@Column(name = "EN_NAME", length = 25)
//	public String getEnName() {
//		return this.enName;
//	}
//
//	public void setEnName(String enName) {
//		this.enName = enName;
//	}
//
//	@Column(name = "EMP_CODE", length = 20)
//	public String getEmpCode() {
//		return this.empCode;
//	}
//
//	public void setEmpCode(String empCode) {
//		this.empCode = empCode;
//	}
//
//	@Column(name = "DEPT_ID", precision = 10, scale = 0)
//	public Long getDeptId() {
//		return this.deptId;
//	}
//
//	public void setDeptId(Long deptId) {
//		this.deptId = deptId;
//	}
//
//	@Column(name = "RETRIEVE_CODE", length = 20)
//	public String getRetrieveCode() {
//		return this.retrieveCode;
//	}
//
//	public void setRetrieveCode(String retrieveCode) {
//		this.retrieveCode = retrieveCode;
//	}
//
//	@Column(name = "NATION_ID", precision = 2, scale = 0)
//	public Long getNationId() {
//		return this.nationId;
//	}
//
//	public void setNationId(Long nationId) {
//		this.nationId = nationId;
//	}
//
//	@Column(name = "NATIVE_PLACE_ID", precision = 10, scale = 0)
//	public Long getNativePlaceId() {
//		return this.nativePlaceId;
//	}
//
//	public void setNativePlaceId(Long nativePlaceId) {
//		this.nativePlaceId = nativePlaceId;
//	}
//
//	@Column(name = "POLITICS_ID", precision = 10, scale = 0)
//	public Long getPoliticsId() {
//		return this.politicsId;
//	}
//
//	public void setPoliticsId(Long politicsId) {
//		this.politicsId = politicsId;
//	}
//
//	@Temporal(TemporalType.DATE)
//	@Column(name = "BRITHDAY", length = 7)
//	public Date getBrithday() {
//		return this.brithday;
//	}
//
//	public void setBrithday(Date brithday) {
//		this.brithday = brithday;
//	}
//
//	@Column(name = "SEX", length = 1)
//	public String getSex() {
//		return this.sex;
//	}
//
//	public void setSex(String sex) {
//		this.sex = sex;
//	}
//
//	@Column(name = "IS_WEDDED", length = 1)
//	public String getIsWedded() {
//		return this.isWedded;
//	}
//
//	public void setIsWedded(String isWedded) {
//		this.isWedded = isWedded;
//	}
//
//	@Column(name = "ARCHIVES_ID", length = 50)
//	public String getArchivesId() {
//		return this.archivesId;
//	}
//
//	public void setArchivesId(String archivesId) {
//		this.archivesId = archivesId;
//	}
//
//	@Column(name = "EMP_STATION_ID", precision = 10, scale = 0)
//	public Long getEmpStationId() {
//		return this.empStationId;
//	}
//
//	public void setEmpStationId(Long empStationId) {
//		this.empStationId = empStationId;
//	}
//
//	@Column(name = "STATION_ID", precision = 10, scale = 0)
//	public Long getStationId() {
//		return this.stationId;
//	}
//
//	public void setStationId(Long stationId) {
//		this.stationId = stationId;
//	}
//
//	@Column(name = "STATION_LEVEL", precision = 10, scale = 0)
//	public Long getStationLevel() {
//		return this.stationLevel;
//	}
//
//	public void setStationLevel(Long stationLevel) {
//		this.stationLevel = stationLevel;
//	}
//
//	@Column(name = "GRADATION", precision = 10, scale = 0)
//	public Long getGradation() {
//		return this.gradation;
//	}
//
//	public void setGradation(Long gradation) {
//		this.gradation = gradation;
//	}
//
//	@Column(name = "EMP_TYPE_ID", precision = 10, scale = 0)
//	public Long getEmpTypeId() {
//		return this.empTypeId;
//	}
//
//	public void setEmpTypeId(Long empTypeId) {
//		this.empTypeId = empTypeId;
//	}
//
//	@Temporal(TemporalType.DATE)
//	@Column(name = "WORK_DATE", length = 7)
//	public Date getWorkDate() {
//		return this.workDate;
//	}
//
//	public void setWorkDate(Date workDate) {
//		this.workDate = workDate;
//	}
//
//	@Temporal(TemporalType.DATE)
//	@Column(name = "MISSION_DATE", length = 7)
//	public Date getMissionDate() {
//		return this.missionDate;
//	}
//
//	public void setMissionDate(Date missionDate) {
//		this.missionDate = missionDate;
//	}
//
//	@Temporal(TemporalType.DATE)
//	@Column(name = "DIMISSION_DATE", length = 7)
//	public Date getDimissionDate() {
//		return this.dimissionDate;
//	}
//
//	public void setDimissionDate(Date dimissionDate) {
//		this.dimissionDate = dimissionDate;
//	}
//
//	@Column(name = "TECHNOLOGY_TITLES_TYPE_ID", precision = 10, scale = 0)
//	public Long getTechnologyTitlesTypeId() {
//		return this.technologyTitlesTypeId;
//	}
//
//	public void setTechnologyTitlesTypeId(Long technologyTitlesTypeId) {
//		this.technologyTitlesTypeId = technologyTitlesTypeId;
//	}
//
//	@Column(name = "TECHNOLOGY_GRADE_ID", precision = 10, scale = 0)
//	public Long getTechnologyGradeId() {
//		return this.technologyGradeId;
//	}
//
//	public void setTechnologyGradeId(Long technologyGradeId) {
//		this.technologyGradeId = technologyGradeId;
//	}
//
//	@Column(name = "TYPE_OF_WORK_ID", precision = 10, scale = 0)
//	public Long getTypeOfWorkId() {
//		return this.typeOfWorkId;
//	}
//
//	public void setTypeOfWorkId(Long typeOfWorkId) {
//		this.typeOfWorkId = typeOfWorkId;
//	}
//
//	@Column(name = "IDENTITY_CARD", length = 50)
//	public String getIdentityCard() {
//		return this.identityCard;
//	}
//
//	public void setIdentityCard(String identityCard) {
//		this.identityCard = identityCard;
//	}
//
//	@Column(name = "TIME_CARD_ID", length = 50)
//	public String getTimeCardId() {
//		return this.timeCardId;
//	}
//
//	public void setTimeCardId(String timeCardId) {
//		this.timeCardId = timeCardId;
//	}
//
//	@Column(name = "PAY_CARD_ID", length = 50)
//	public String getPayCardId() {
//		return this.payCardId;
//	}
//
//	public void setPayCardId(String payCardId) {
//		this.payCardId = payCardId;
//	}
//
//	@Column(name = "SOCIAL_INSURANCE_ID", length = 50)
//	public String getSocialInsuranceId() {
//		return this.socialInsuranceId;
//	}
//
//	public void setSocialInsuranceId(String socialInsuranceId) {
//		this.socialInsuranceId = socialInsuranceId;
//	}
//
//	@Temporal(TemporalType.DATE)
//	@Column(name = "SOCIAL_INSURANCE_DATE", length = 7)
//	public Date getSocialInsuranceDate() {
//		return this.socialInsuranceDate;
//	}
//
//	public void setSocialInsuranceDate(Date socialInsuranceDate) {
//		this.socialInsuranceDate = socialInsuranceDate;
//	}
//
//	@Column(name = "MOBILE_PHONE", length = 20)
//	public String getMobilePhone() {
//		return this.mobilePhone;
//	}
//
//	public void setMobilePhone(String mobilePhone) {
//		this.mobilePhone = mobilePhone;
//	}
//
//	@Column(name = "FAMILY_TEL", length = 20)
//	public String getFamilyTel() {
//		return this.familyTel;
//	}
//
//	public void setFamilyTel(String familyTel) {
//		this.familyTel = familyTel;
//	}
//
//	@Column(name = "QQ", length = 20)
//	public String getQq() {
//		return this.qq;
//	}
//
//	public void setQq(String qq) {
//		this.qq = qq;
//	}
//
//	@Column(name = "MSN", length = 25)
//	public String getMsn() {
//		return this.msn;
//	}
//
//	public void setMsn(String msn) {
//		this.msn = msn;
//	}
//
//	@Column(name = "POSTALCODE", length = 10)
//	public String getPostalcode() {
//		return this.postalcode;
//	}
//
//	public void setPostalcode(String postalcode) {
//		this.postalcode = postalcode;
//	}
//
//	@Column(name = "FAMILY_ADDRESS", length = 100)
//	public String getFamilyAddress() {
//		return this.familyAddress;
//	}
//
//	public void setFamilyAddress(String familyAddress) {
//		this.familyAddress = familyAddress;
//	}
//
//	@Column(name = "OFFICE_TEL1", length = 20)
//	public String getOfficeTel1() {
//		return this.officeTel1;
//	}
//
//	public void setOfficeTel1(String officeTel1) {
//		this.officeTel1 = officeTel1;
//	}
//
//	@Column(name = "OFFICE_TEL2", length = 20)
//	public String getOfficeTel2() {
//		return this.officeTel2;
//	}
//
//	public void setOfficeTel2(String officeTel2) {
//		this.officeTel2 = officeTel2;
//	}
//
//	@Column(name = "FAX", length = 20)
//	public String getFax() {
//		return this.fax;
//	}
//
//	public void setFax(String fax) {
//		this.fax = fax;
//	}
//
//	@Column(name = "E_MAIL", length = 50)
//	public String getEMail() {
//		return this.EMail;
//	}
//
//	public void setEMail(String EMail) {
//		this.EMail = EMail;
//	}
//
//	@Column(name = "INSTANCY_MAN", length = 50)
//	public String getInstancyMan() {
//		return this.instancyMan;
//	}
//
//	public void setInstancyMan(String instancyMan) {
//		this.instancyMan = instancyMan;
//	}
//
//	@Column(name = "INSTANCY_TEL", length = 20)
//	public String getInstancyTel() {
//		return this.instancyTel;
//	}
//
//	public void setInstancyTel(String instancyTel) {
//		this.instancyTel = instancyTel;
//	}
//
//	@Column(name = "GRADUATE_SCHOOL", length = 100)
//	public String getGraduateSchool() {
//		return this.graduateSchool;
//	}
//
//	public void setGraduateSchool(String graduateSchool) {
//		this.graduateSchool = graduateSchool;
//	}
//
//	@Column(name = "EDUCATION_ID", precision = 10, scale = 0)
//	public Long getEducationId() {
//		return this.educationId;
//	}
//
//	public void setEducationId(Long educationId) {
//		this.educationId = educationId;
//	}
//
//	@Column(name = "DEGREE_ID", precision = 10, scale = 0)
//	public Long getDegreeId() {
//		return this.degreeId;
//	}
//
//	public void setDegreeId(Long degreeId) {
//		this.degreeId = degreeId;
//	}
//
//	@Column(name = "SPECIALITY", length = 100)
//	public String getSpeciality() {
//		return this.speciality;
//	}
//
//	public void setSpeciality(String speciality) {
//		this.speciality = speciality;
//	}
//
//	@Temporal(TemporalType.DATE)
//	@Column(name = "GRADUATE_DATE", length = 7)
//	public Date getGraduateDate() {
//		return this.graduateDate;
//	}
//
//	public void setGraduateDate(Date graduateDate) {
//		this.graduateDate = graduateDate;
//	}
//
//	@Column(name = "IS_VETERAN", length = 1)
//	public String getIsVeteran() {
//		return this.isVeteran;
//	}
//
//	public void setIsVeteran(String isVeteran) {
//		this.isVeteran = isVeteran;
//	}
//
//	@Column(name = "IS_BORROW", length = 1)
//	public String getIsBorrow() {
//		return this.isBorrow;
//	}
//
//	public void setIsBorrow(String isBorrow) {
//		this.isBorrow = isBorrow;
//	}
//
//	@Column(name = "IS_RETIRED", length = 1)
//	public String getIsRetired() {
//		return this.isRetired;
//	}
//
//	public void setIsRetired(String isRetired) {
//		this.isRetired = isRetired;
//	}
//
//	@Column(name = "EMP_STATE", length = 1)
//	public String getEmpState() {
//		return this.empState;
//	}
//
//	public void setEmpState(String empState) {
//		this.empState = empState;
//	}
//
//	@Column(name = "ORDER_BY", precision = 15, scale = 0)
//	public Long getOrderBy() {
//		return this.orderBy;
//	}
//
//	public void setOrderBy(Long orderBy) {
//		this.orderBy = orderBy;
//	}
//
//	@Column(name = "RECOMMEND_MAN", length = 50)
//	public String getRecommendMan() {
//		return this.recommendMan;
//	}
//
//	public void setRecommendMan(String recommendMan) {
//		this.recommendMan = recommendMan;
//	}
//
//	@Column(name = "ASSISTANT_MANAGER_UNITS_ID", precision = 10, scale = 0)
//	public Long getAssistantManagerUnitsId() {
//		return this.assistantManagerUnitsId;
//	}
//
//	public void setAssistantManagerUnitsId(Long assistantManagerUnitsId) {
//		this.assistantManagerUnitsId = assistantManagerUnitsId;
//	}
//
//	@Column(name = "ONE_STRONG_SUIT", length = 500)
//	public String getOneStrongSuit() {
//		return this.oneStrongSuit;
//	}
//
//	public void setOneStrongSuit(String oneStrongSuit) {
//		this.oneStrongSuit = oneStrongSuit;
//	}
//
//	@Column(name = "MEMO", length = 500)
//	public String getMemo() {
//		return this.memo;
//	}
//
//	public void setMemo(String memo) {
//		this.memo = memo;
//	}
//
//	@Column(name = "CREATE_BY", precision = 10, scale = 0)
//	public Long getCreateBy() {
//		return this.createBy;
//	}
//
//	public void setCreateBy(Long createBy) {
//		this.createBy = createBy;
//	}
//
//	@Temporal(TemporalType.DATE)
//	@Column(name = "CREATE_DATE", length = 7)
//	public Date getCreateDate() {
//		return this.createDate;
//	}
//
//	public void setCreateDate(Date createDate) {
//		this.createDate = createDate;
//	}
//
//	@Column(name = "LAST_MODIFIY_BY", precision = 10, scale = 0)
//	public Long getLastModifiyBy() {
//		return this.lastModifiyBy;
//	}
//
//	public void setLastModifiyBy(Long lastModifiyBy) {
//		this.lastModifiyBy = lastModifiyBy;
//	}
//
//	// modified by liuyi 091201 为员工离职登记中的通过时间做验证使用
////	@Temporal(TemporalType.DATE)
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name = "LAST_MODIFIY_DATE", length = 7)
//	public Date getLastModifiyDate() {
//		return this.lastModifiyDate;
//	}
//
//	public void setLastModifiyDate(Date lastModifiyDate) {
//		this.lastModifiyDate = lastModifiyDate;
//	}
//	
//	@Column(name = "STATION_GRADE", precision = 10, scale = 0)
//	public Long getStationGrade() {
//		return stationGrade;
//	}
//
//	public void setStationGrade(Long stationGrade) {
//		this.stationGrade = stationGrade;
//	}
//
//	@Column(name = "SALARY_LEVEL", precision = 10, scale = 0)
//	public Long getSalaryLevel() {
//		return salaryLevel;
//	}
//
//	public void setSalaryLevel(Long salaryLevel) {
//		this.salaryLevel = salaryLevel;
//	}
//
//	@Column(name = "CHECK_STATION_GRADE", precision = 10, scale = 0)
//	public Long getCheckStationGrade() {
//		return checkStationGrade;
//	}
//
//	public void setCheckStationGrade(Long checkStationGrade) {
//		this.checkStationGrade = checkStationGrade;
//	}
//
//	@Column(name = "ATTENDANCE_DEPT_ID", precision = 10, scale = 0)
//	public Long getAttendanceDeptId() {
//		return attendanceDeptId;
//	}
//
//	public void setAttendanceDeptId(Long attendanceDeptId) {
//		this.attendanceDeptId = attendanceDeptId;
//	}
//
//	@Column(name = "BANZU_ID", precision = 10, scale = 0)
//	public Long getBanzuId() {
//		return banzuId;
//	}
//
//	public void setBanzuId(Long banzuId) {
//		this.banzuId = banzuId;
//	}
//
//	
//	@Column(name = "IS_USE", length = 1)
//	public String getIsUse() {
//		return isUse;
//	}
//
//	public void setIsUse(String isUse) {
//		this.isUse = isUse;
//	}
//
//	@Column(name = "ENTERPRISE_CODE", length = 20)
//	public String getEnterpriseCode() {
//		return enterpriseCode;
//	}
//
//	public void setEnterpriseCode(String enterpriseCode) {
//		this.enterpriseCode = enterpriseCode;
//	}
//
//	@Temporal(TemporalType.DATE)
//	@Column(name = "TRYOUT_START_DATE", length = 7)
//	public Date getTryoutStartDate() {
//		return this.tryoutStartDate;
//	}
//
//	public void setTryoutStartDate(Date tryoutStartDate) {
//		this.tryoutStartDate = tryoutStartDate;
//	}
//
//	@Temporal(TemporalType.DATE)
//	@Column(name = "TRYOUT_END_DATE", length = 7)
//	public Date getTryoutEndDate() {
//		return this.tryoutEndDate;
//	}
//
//	public void setTryoutEndDate(Date tryoutEndDate) {
//		this.tryoutEndDate = tryoutEndDate;
//	}
//
//	@Column(name = "SPECIALTY_CODE_ID", precision = 10, scale = 0)
//	public Long getSpecialtyCodeId() {
//		return this.specialtyCodeId;
//	}
//
//	public void setSpecialtyCodeId(Long specialtyCodeId) {
//		this.specialtyCodeId = specialtyCodeId;
//	}
//	
//	@Column(name = "GRADUATE_SCHOOL_ID", precision = 10, scale = 0)
//	public Long getGraduateSchoolId() {
//		return this.graduateSchoolId;
//	}
//
//	public void setGraduateSchoolId(Long graduateSchoolId) {
//		this.graduateSchoolId = graduateSchoolId;
//	}
//
//	@Column(name = "NATION_CODE_ID", precision = 10, scale = 0)
//	public Long getNationCodeId() {
//		return this.nationCodeId;
//	}
//
//	public void setNationCodeId(Long nationCodeId) {
//		this.nationCodeId = nationCodeId;
//	}
//	@Column(name = "IN_TYPE_ID", precision = 10, scale = 0)
//	public Long getInTypeId() {
//		return this.inTypeId;
//	}
//
//	public void setInTypeId(Long inTypeId) {
//		this.inTypeId = inTypeId;
//	}
//
//	@Column(name = "WORK_ID", precision = 10, scale = 0)
//	public Long getWorkId() {
//		return this.workId;
//	}
//
//	public void setWorkId(Long workId) {
//		this.workId = workId;
//	}
//
//	@Column(name = "LB_WORK_ID", precision = 10, scale = 0)
//	public Long getLbWorkId() {
//		return this.lbWorkId;
//	}
//
//	public void setLbWorkId(Long lbWorkId) {
//		this.lbWorkId = lbWorkId;
//	}
//	
//	@Temporal(TemporalType.DATE)
//	@Column(name = "RETIREMENT_DATE", length = 7)
//	public Date getRetirementDate() {
//		return retirementDate;
//	}
//
//	public void setRetirementDate(Date retirementDate) {
//		this.retirementDate = retirementDate;
//	}
//}

package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * HrJEmpInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_EMP_INFO", schema = "POWER", uniqueConstraints = {
		@UniqueConstraint(columnNames = "EMP_CODE"),
		@UniqueConstraint(columnNames = "NEW_EMP_CODE") })
public class HrJEmpInfo implements java.io.Serializable {

	// Fields

	private Long empId;
	private String chsName;
	private String enName;
	private String empCode;
	private Long deptId;
	private String retrieveCode;
	private Long nationId;
	private Long nativePlaceId;
	private Long politicsId;
	private Date brithday;
	private String sex;
	private String isWedded;
	private String archivesId;
	private Long empStationId;
	private Long stationId;
	private Long stationLevel;
	private Long gradation;
	private Long empTypeId;
	private Date workDate;
	private Date missionDate;
	private Date dimissionDate;
	private Long technologyTitlesTypeId;
	private Long technologyGradeId;
	private Long typeOfWorkId;
	private String identityCard;
	private String timeCardId;
	private String payCardId;
	private String socialInsuranceId;
	private Date socialInsuranceDate;
	private String mobilePhone;
	private String familyTel;
	private String qq;
	private String msn;
	private String postalcode;
	private String familyAddress;
	private String officeTel1;
	private String officeTel2;
	private String fax;
	private String EMail;
	private String instancyMan;
	private String instancyTel;
	private String graduateSchool;
	private Long educationId;
	private Long degreeId;
	private String speciality;
	private Date graduateDate;
	private String isVeteran;
	private String isBorrow;
	private String isRetired;
	private String empState;
	private Long orderBy;
	private String recommendMan;
	private Long assistantManagerUnitsId;
	private String oneStrongSuit;
	private String memo;
	private Long createBy;
	private Date createDate;
	private Long lastModifiyBy;
	private Date lastModifiyDate;
	private String curriculumVitae;
	private String societyInfo;
	private String enterpriseCode;
	private Long stationGrade;
	private Long salaryLevel;
	private Long checkStationGrade;
	private Long attendanceDeptId;
	private String isUse;
	private Long banzuId;
	private Date tryoutStartDate;
	private Date tryoutEndDate;
	private Long specialtyCodeId;
	private Long graduateSchoolId;
	private Long nationCodeId;
	private Long inTypeId;
	private Long workId;
	private Long lbWorkId;
	private Date retirementDate;
	private String newEmpCode;
	
	// add by liuyi 20100603 
	private String jnGrade;//JN_GRADE
	private String isMainWork;//IS_MAIN_WORK
	private Date intoPartDate;//INTO_PART_DATE
	private Date partPositiveDate;//PART_POSITIVE_DATE
	private String household;//HOUSEHOLD
	private String country;//COUNTRY
	private String bloodType;//BLOOD_TYPE
	private String component;//COMPONENT
	private String  isChairman;//add by wpzhu 
	private String isSpecialTrades;//add by drdu 20100626
	private String isCadres;//add by drdu 20100626
	// Constructors
	private String nativePlaceName;//add by fyyang 20100714

	
	

	/** default constructor */
	public HrJEmpInfo() {
	}

	/** minimal constructor */
	public HrJEmpInfo(Long empId) {
		this.empId = empId;
	}

	/** full constructor */
	public HrJEmpInfo(Long empId, String chsName, String enName,
			String empCode, Long deptId, String retrieveCode, Long nationId,
			Long nativePlaceId, Long politicsId, Date brithday, String sex,
			String isWedded, String archivesId, Long empStationId,
			Long stationId, Long stationLevel, Long gradation, Long empTypeId,
			Date workDate, Date missionDate, Date dimissionDate,
			Long technologyTitlesTypeId, Long technologyGradeId,
			Long typeOfWorkId, String identityCard, String timeCardId,
			String payCardId, String socialInsuranceId,
			Date socialInsuranceDate, String mobilePhone, String familyTel,
			String qq, String msn, String postalcode, String familyAddress,
			String officeTel1, String officeTel2, String fax, String EMail,
			String instancyMan, String instancyTel, String graduateSchool,
			Long educationId, Long degreeId, String speciality,
			Date graduateDate, String isVeteran, String isBorrow,
			String isRetired, String empState, Long orderBy,
			String recommendMan, Long assistantManagerUnitsId,
			String oneStrongSuit, String memo, Long createBy, Date createDate,
			Long lastModifiyBy, Date lastModifiyDate, String curriculumVitae,
			String societyInfo, String enterpriseCode, Long stationGrade,
			Long salaryLevel, Long checkStationGrade, Long attendanceDeptId,
			String isUse, Long banzuId, Date tryoutStartDate,
			Date tryoutEndDate, Long specialtyCodeId, Long graduateSchoolId,
			Long nationCodeId, Long inTypeId, Long workId, Long lbWorkId,
			Date retirementDate, String newEmpCode,String isChairman) {
		this.empId = empId;
		this.chsName = chsName;
		this.enName = enName;
		this.empCode = empCode;
		this.deptId = deptId;
		this.retrieveCode = retrieveCode;
		this.nationId = nationId;
		this.nativePlaceId = nativePlaceId;
		this.politicsId = politicsId;
		this.brithday = brithday;
		this.sex = sex;
		this.isWedded = isWedded;
		this.archivesId = archivesId;
		this.empStationId = empStationId;
		this.stationId = stationId;
		this.stationLevel = stationLevel;
		this.gradation = gradation;
		this.empTypeId = empTypeId;
		this.workDate = workDate;
		this.missionDate = missionDate;
		this.dimissionDate = dimissionDate;
		this.technologyTitlesTypeId = technologyTitlesTypeId;
		this.technologyGradeId = technologyGradeId;
		this.typeOfWorkId = typeOfWorkId;
		this.identityCard = identityCard;
		this.timeCardId = timeCardId;
		this.payCardId = payCardId;
		this.socialInsuranceId = socialInsuranceId;
		this.socialInsuranceDate = socialInsuranceDate;
		this.mobilePhone = mobilePhone;
		this.familyTel = familyTel;
		this.qq = qq;
		this.msn = msn;
		this.postalcode = postalcode;
		this.familyAddress = familyAddress;
		this.officeTel1 = officeTel1;
		this.officeTel2 = officeTel2;
		this.fax = fax;
		this.EMail = EMail;
		this.instancyMan = instancyMan;
		this.instancyTel = instancyTel;
		this.graduateSchool = graduateSchool;
		this.educationId = educationId;
		this.degreeId = degreeId;
		this.speciality = speciality;
		this.graduateDate = graduateDate;
		this.isVeteran = isVeteran;
		this.isBorrow = isBorrow;
		this.isRetired = isRetired;
		this.empState = empState;
		this.orderBy = orderBy;
		this.recommendMan = recommendMan;
		this.assistantManagerUnitsId = assistantManagerUnitsId;
		this.oneStrongSuit = oneStrongSuit;
		this.memo = memo;
		this.createBy = createBy;
		this.createDate = createDate;
		this.lastModifiyBy = lastModifiyBy;
		this.lastModifiyDate = lastModifiyDate;
		this.curriculumVitae = curriculumVitae;
		this.societyInfo = societyInfo;
		this.enterpriseCode = enterpriseCode;
		this.stationGrade = stationGrade;
		this.salaryLevel = salaryLevel;
		this.checkStationGrade = checkStationGrade;
		this.attendanceDeptId = attendanceDeptId;
		this.isUse = isUse;
		this.banzuId = banzuId;
		this.tryoutStartDate = tryoutStartDate;
		this.tryoutEndDate = tryoutEndDate;
		this.specialtyCodeId = specialtyCodeId;
		this.graduateSchoolId = graduateSchoolId;
		this.nationCodeId = nationCodeId;
		this.inTypeId = inTypeId;
		this.workId = workId;
		this.lbWorkId = lbWorkId;
		this.retirementDate = retirementDate;
		this.newEmpCode = newEmpCode;
		this.isChairman=isChairman;
	}

	// Property accessors
	@Id
	@Column(name = "EMP_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "CHS_NAME", length = 25)
	public String getChsName() {
		return this.chsName;
	}

	public void setChsName(String chsName) {
		this.chsName = chsName;
	}

	@Column(name = "EN_NAME", length = 25)
	public String getEnName() {
		return this.enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	@Column(name = "EMP_CODE", unique = true, length = 20)
	public String getEmpCode() {
		return this.empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	@Column(name = "DEPT_ID", precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Column(name = "RETRIEVE_CODE", length = 20)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

	@Column(name = "NATION_ID", precision = 2, scale = 0)
	public Long getNationId() {
		return this.nationId;
	}

	public void setNationId(Long nationId) {
		this.nationId = nationId;
	}

	@Column(name = "NATIVE_PLACE_ID", precision = 10, scale = 0)
	public Long getNativePlaceId() {
		return this.nativePlaceId;
	}

	public void setNativePlaceId(Long nativePlaceId) {
		this.nativePlaceId = nativePlaceId;
	}

	@Column(name = "POLITICS_ID", precision = 10, scale = 0)
	public Long getPoliticsId() {
		return this.politicsId;
	}

	public void setPoliticsId(Long politicsId) {
		this.politicsId = politicsId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BRITHDAY", length = 7)
	public Date getBrithday() {
		return this.brithday;
	}

	public void setBrithday(Date brithday) {
		this.brithday = brithday;
	}

	@Column(name = "SEX", length = 1)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "IS_WEDDED", length = 1)
	public String getIsWedded() {
		return this.isWedded;
	}

	public void setIsWedded(String isWedded) {
		this.isWedded = isWedded;
	}

	@Column(name = "ARCHIVES_ID", length = 50)
	public String getArchivesId() {
		return this.archivesId;
	}

	public void setArchivesId(String archivesId) {
		this.archivesId = archivesId;
	}

	@Column(name = "EMP_STATION_ID", precision = 10, scale = 0)
	public Long getEmpStationId() {
		return this.empStationId;
	}

	public void setEmpStationId(Long empStationId) {
		this.empStationId = empStationId;
	}

	@Column(name = "STATION_ID", precision = 10, scale = 0)
	public Long getStationId() {
		return this.stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	@Column(name = "STATION_LEVEL", precision = 10, scale = 0)
	public Long getStationLevel() {
		return this.stationLevel;
	}

	public void setStationLevel(Long stationLevel) {
		this.stationLevel = stationLevel;
	}

	@Column(name = "GRADATION", precision = 10, scale = 0)
	public Long getGradation() {
		return this.gradation;
	}

	public void setGradation(Long gradation) {
		this.gradation = gradation;
	}

	@Column(name = "EMP_TYPE_ID", precision = 10, scale = 0)
	public Long getEmpTypeId() {
		return this.empTypeId;
	}

	public void setEmpTypeId(Long empTypeId) {
		this.empTypeId = empTypeId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "WORK_DATE", length = 7)
	public Date getWorkDate() {
		return this.workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MISSION_DATE", length = 7)
	public Date getMissionDate() {
		return this.missionDate;
	}

	public void setMissionDate(Date missionDate) {
		this.missionDate = missionDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DIMISSION_DATE", length = 7)
	public Date getDimissionDate() {
		return this.dimissionDate;
	}

	public void setDimissionDate(Date dimissionDate) {
		this.dimissionDate = dimissionDate;
	}

	@Column(name = "TECHNOLOGY_TITLES_TYPE_ID", precision = 10, scale = 0)
	public Long getTechnologyTitlesTypeId() {
		return this.technologyTitlesTypeId;
	}

	public void setTechnologyTitlesTypeId(Long technologyTitlesTypeId) {
		this.technologyTitlesTypeId = technologyTitlesTypeId;
	}

	@Column(name = "TECHNOLOGY_GRADE_ID", precision = 10, scale = 0)
	public Long getTechnologyGradeId() {
		return this.technologyGradeId;
	}

	public void setTechnologyGradeId(Long technologyGradeId) {
		this.technologyGradeId = technologyGradeId;
	}

	@Column(name = "TYPE_OF_WORK_ID", precision = 10, scale = 0)
	public Long getTypeOfWorkId() {
		return this.typeOfWorkId;
	}

	public void setTypeOfWorkId(Long typeOfWorkId) {
		this.typeOfWorkId = typeOfWorkId;
	}

	@Column(name = "IDENTITY_CARD", length = 50)
	public String getIdentityCard() {
		return this.identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	@Column(name = "TIME_CARD_ID", length = 50)
	public String getTimeCardId() {
		return this.timeCardId;
	}

	public void setTimeCardId(String timeCardId) {
		this.timeCardId = timeCardId;
	}

	@Column(name = "PAY_CARD_ID", length = 50)
	public String getPayCardId() {
		return this.payCardId;
	}

	public void setPayCardId(String payCardId) {
		this.payCardId = payCardId;
	}

	@Column(name = "SOCIAL_INSURANCE_ID", length = 50)
	public String getSocialInsuranceId() {
		return this.socialInsuranceId;
	}

	public void setSocialInsuranceId(String socialInsuranceId) {
		this.socialInsuranceId = socialInsuranceId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SOCIAL_INSURANCE_DATE", length = 7)
	public Date getSocialInsuranceDate() {
		return this.socialInsuranceDate;
	}

	public void setSocialInsuranceDate(Date socialInsuranceDate) {
		this.socialInsuranceDate = socialInsuranceDate;
	}

	@Column(name = "MOBILE_PHONE", length = 20)
	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@Column(name = "FAMILY_TEL", length = 20)
	public String getFamilyTel() {
		return this.familyTel;
	}

	public void setFamilyTel(String familyTel) {
		this.familyTel = familyTel;
	}

	@Column(name = "QQ", length = 20)
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "MSN", length = 25)
	public String getMsn() {
		return this.msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	@Column(name = "POSTALCODE", length = 10)
	public String getPostalcode() {
		return this.postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	@Column(name = "FAMILY_ADDRESS", length = 100)
	public String getFamilyAddress() {
		return this.familyAddress;
	}

	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
	}

	@Column(name = "OFFICE_TEL1", length = 20)
	public String getOfficeTel1() {
		return this.officeTel1;
	}

	public void setOfficeTel1(String officeTel1) {
		this.officeTel1 = officeTel1;
	}

	@Column(name = "OFFICE_TEL2", length = 20)
	public String getOfficeTel2() {
		return this.officeTel2;
	}

	public void setOfficeTel2(String officeTel2) {
		this.officeTel2 = officeTel2;
	}

	@Column(name = "FAX", length = 20)
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "E_MAIL", length = 50)
	public String getEMail() {
		return this.EMail;
	}

	public void setEMail(String EMail) {
		this.EMail = EMail;
	}

	@Column(name = "INSTANCY_MAN", length = 50)
	public String getInstancyMan() {
		return this.instancyMan;
	}

	public void setInstancyMan(String instancyMan) {
		this.instancyMan = instancyMan;
	}

	@Column(name = "INSTANCY_TEL", length = 20)
	public String getInstancyTel() {
		return this.instancyTel;
	}

	public void setInstancyTel(String instancyTel) {
		this.instancyTel = instancyTel;
	}

	@Column(name = "GRADUATE_SCHOOL", length = 100)
	public String getGraduateSchool() {
		return this.graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
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

	@Column(name = "SPECIALITY", length = 100)
	public String getSpeciality() {
		return this.speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GRADUATE_DATE", length = 7)
	public Date getGraduateDate() {
		return this.graduateDate;
	}

	public void setGraduateDate(Date graduateDate) {
		this.graduateDate = graduateDate;
	}

	@Column(name = "IS_VETERAN", length = 1)
	public String getIsVeteran() {
		return this.isVeteran;
	}

	public void setIsVeteran(String isVeteran) {
		this.isVeteran = isVeteran;
	}

	@Column(name = "IS_BORROW", length = 1)
	public String getIsBorrow() {
		return this.isBorrow;
	}

	public void setIsBorrow(String isBorrow) {
		this.isBorrow = isBorrow;
	}

	@Column(name = "IS_RETIRED", length = 1)
	public String getIsRetired() {
		return this.isRetired;
	}

	public void setIsRetired(String isRetired) {
		this.isRetired = isRetired;
	}

	@Column(name = "EMP_STATE", length = 1)
	public String getEmpState() {
		return this.empState;
	}

	public void setEmpState(String empState) {
		this.empState = empState;
	}

	@Column(name = "ORDER_BY", precision = 15, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "RECOMMEND_MAN", length = 50)
	public String getRecommendMan() {
		return this.recommendMan;
	}

	public void setRecommendMan(String recommendMan) {
		this.recommendMan = recommendMan;
	}

	@Column(name = "ASSISTANT_MANAGER_UNITS_ID", precision = 10, scale = 0)
	public Long getAssistantManagerUnitsId() {
		return this.assistantManagerUnitsId;
	}

	public void setAssistantManagerUnitsId(Long assistantManagerUnitsId) {
		this.assistantManagerUnitsId = assistantManagerUnitsId;
	}

	@Column(name = "ONE_STRONG_SUIT", length = 500)
	public String getOneStrongSuit() {
		return this.oneStrongSuit;
	}

	public void setOneStrongSuit(String oneStrongSuit) {
		this.oneStrongSuit = oneStrongSuit;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "CREATE_BY", precision = 10, scale = 0)
	public Long getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", length = 7)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "LAST_MODIFIY_BY", precision = 10, scale = 0)
	public Long getLastModifiyBy() {
		return this.lastModifiyBy;
	}

	public void setLastModifiyBy(Long lastModifiyBy) {
		this.lastModifiyBy = lastModifiyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIY_DATE", length = 7)
	public Date getLastModifiyDate() {
		return this.lastModifiyDate;
	}

	public void setLastModifiyDate(Date lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

	@Column(name = "CURRICULUM_VITAE", length = 2000)
	public String getCurriculumVitae() {
		return this.curriculumVitae;
	}

	public void setCurriculumVitae(String curriculumVitae) {
		this.curriculumVitae = curriculumVitae;
	}

	@Column(name = "SOCIETY_INFO", length = 2000)
	public String getSocietyInfo() {
		return this.societyInfo;
	}

	public void setSocietyInfo(String societyInfo) {
		this.societyInfo = societyInfo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "STATION_GRADE", precision = 10, scale = 0)
	public Long getStationGrade() {
		return this.stationGrade;
	}

	public void setStationGrade(Long stationGrade) {
		this.stationGrade = stationGrade;
	}

	@Column(name = "SALARY_LEVEL", precision = 10, scale = 0)
	public Long getSalaryLevel() {
		return this.salaryLevel;
	}

	public void setSalaryLevel(Long salaryLevel) {
		this.salaryLevel = salaryLevel;
	}

	@Column(name = "CHECK_STATION_GRADE", precision = 10, scale = 0)
	public Long getCheckStationGrade() {
		return this.checkStationGrade;
	}

	public void setCheckStationGrade(Long checkStationGrade) {
		this.checkStationGrade = checkStationGrade;
	}

	@Column(name = "ATTENDANCE_DEPT_ID", precision = 10, scale = 0)
	public Long getAttendanceDeptId() {
		return this.attendanceDeptId;
	}

	public void setAttendanceDeptId(Long attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "BANZU_ID", precision = 10, scale = 0)
	public Long getBanzuId() {
		return this.banzuId;
	}

	public void setBanzuId(Long banzuId) {
		this.banzuId = banzuId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TRYOUT_START_DATE", length = 7)
	public Date getTryoutStartDate() {
		return this.tryoutStartDate;
	}

	public void setTryoutStartDate(Date tryoutStartDate) {
		this.tryoutStartDate = tryoutStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TRYOUT_END_DATE", length = 7)
	public Date getTryoutEndDate() {
		return this.tryoutEndDate;
	}

	public void setTryoutEndDate(Date tryoutEndDate) {
		this.tryoutEndDate = tryoutEndDate;
	}

	@Column(name = "SPECIALTY_CODE_ID", precision = 10, scale = 0)
	public Long getSpecialtyCodeId() {
		return this.specialtyCodeId;
	}

	public void setSpecialtyCodeId(Long specialtyCodeId) {
		this.specialtyCodeId = specialtyCodeId;
	}

	@Column(name = "GRADUATE_SCHOOL_ID", precision = 10, scale = 0)
	public Long getGraduateSchoolId() {
		return this.graduateSchoolId;
	}

	public void setGraduateSchoolId(Long graduateSchoolId) {
		this.graduateSchoolId = graduateSchoolId;
	}

	@Column(name = "NATION_CODE_ID", precision = 10, scale = 0)
	public Long getNationCodeId() {
		return this.nationCodeId;
	}

	public void setNationCodeId(Long nationCodeId) {
		this.nationCodeId = nationCodeId;
	}

	@Column(name = "IN_TYPE_ID", precision = 10, scale = 0)
	public Long getInTypeId() {
		return this.inTypeId;
	}

	public void setInTypeId(Long inTypeId) {
		this.inTypeId = inTypeId;
	}

	@Column(name = "WORK_ID", precision = 10, scale = 0)
	public Long getWorkId() {
		return this.workId;
	}

	public void setWorkId(Long workId) {
		this.workId = workId;
	}

	@Column(name = "LB_WORK_ID", precision = 10, scale = 0)
	public Long getLbWorkId() {
		return this.lbWorkId;
	}

	public void setLbWorkId(Long lbWorkId) {
		this.lbWorkId = lbWorkId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RETIREMENT_DATE", length = 7)
	public Date getRetirementDate() {
		return this.retirementDate;
	}

	public void setRetirementDate(Date retirementDate) {
		this.retirementDate = retirementDate;
	}

	@Column(name = "NEW_EMP_CODE", unique = true, length = 20)
	public String getNewEmpCode() {
		return this.newEmpCode;
	}

	public void setNewEmpCode(String newEmpCode) {
		this.newEmpCode = newEmpCode;
	}

	@Column(name = "JN_GRADE",  length = 50)
	public String getJnGrade() {
		return jnGrade;
	}

	public void setJnGrade(String jnGrade) {
		this.jnGrade = jnGrade;
	}

	@Column(name = "IS_MAIN_WORK", length = 1)
	public String getIsMainWork() {
		return isMainWork;
	}

	public void setIsMainWork(String isMainWork) {
		this.isMainWork = isMainWork;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INTO_PART_DATE", length = 7)
	public Date getIntoPartDate() {
		return intoPartDate;
	}

	public void setIntoPartDate(Date intoPartDate) {
		this.intoPartDate = intoPartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PART_POSITIVE_DATE", length = 7)
	public Date getPartPositiveDate() {
		return partPositiveDate;
	}

	public void setPartPositiveDate(Date partPositiveDate) {
		this.partPositiveDate = partPositiveDate;
	}

	
	public String getHousehold() {
		return household;
	}

	public void setHousehold(String household) {
		this.household = household;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "BLOOD_TYPE", length = 2)
	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}
	@Column(name = "IS_CHAIRMAN", length = 1)
	public String getIsChairman() {
		return isChairman;
	}

	public void setIsChairman(String isChairman) {
		this.isChairman = isChairman;
	}
	
	@Column(name = "IS_SPECIAL_TRADES", length = 1)
	public String getIsSpecialTrades() {
		return isSpecialTrades;
	}

	public void setIsSpecialTrades(String isSpecialTrades) {
		this.isSpecialTrades = isSpecialTrades;
	}
	
	@Column(name = "IS_CADRES", length = 1)
	public String getIsCadres() {
		return isCadres;
	}

	public void setIsCadres(String isCadres) {
		this.isCadres = isCadres;
	}

	@Column(name = "NATIVE_PLACE_NAME", length = 50)
	public String getNativePlaceName() {
		return nativePlaceName;
	}

	public void setNativePlaceName(String nativePlaceName) {
		this.nativePlaceName = nativePlaceName;
	}

}