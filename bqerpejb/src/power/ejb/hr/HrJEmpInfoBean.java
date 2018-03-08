/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.ejb.hr;

import java.util.Date;

/**
 * 员工档案综合查询维护 人员基本信息自定义Bean
 * 
 * @author huyou
 * @version 1.3
 */
public class HrJEmpInfoBean implements java.io.Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 人员ID
	 */
	private Long empId = null;

	/**
	 * 上次修改时间
	 */
	private String lastModifiedDate = null;
	
	/**
	 * 照片上次修改时间
	 */
	private String photoLastModifiedDate = null;

	/**
	 * 部门设置表.部门名称
	 */
	private String deptName = null;

	/**
	 * 技术职称编码表.技术职称ID
	 */
	private Long technologyTitlesId = null;

	/**
	 * 办公电话1
	 */
	private String officeTel1 = null;

	/**
	 * 入职时间
	 */
	private String missionDate = null;

	/**
	 * 标准岗级
	 */
	private Long stationGrade = null;

	/**
	 * 显示顺序
	 */
	private Long orderBy = null;

	/**
	 * 薪级
	 */
	private Long salaryLevel = null;

	/**
	 * 参保时间
	 */
	private String socialInsuranceDate = null;

	/**
	 * 传真
	 */
	private String fax = null;

	/**
	 * 推荐人
	 */
	private String recommendMan = null;

	/**
	 * 人员姓名
	 */
	private String chsName = null;

	/**
	 * 性别
	 */
	private String sex = null;

	/**
	 * 手机
	 */
	private String mobilePhone = null;

	/**
	 * 员工类别设置.员工类别Id
	 */
	private Long empTypeId = null;

	/**
	 * 毕业时间
	 */
	private String graduateDate = null;

	/**
	 * 职工照片表.图片
	 */
	private byte[] photo = null;

	/**
	 * 政治面貌.政治面貌
	 */
	private Long politicsId = null;

	/**
	 * 家庭住址
	 */
	private String familyAddress = null;

	/**
	 * 岗位设置表.岗位Id
	 */
	private Long stationId = null;

	/**
	 * 考勤卡号
	 */
	private String timeCardId = null;

	/**
	 * 籍贯编码表.籍贯
	 */
	private Long nativePlaceId = null;

	/**
	 * 邮政编码
	 */
	private String postalcode = null;

	/**
	 * 技术等级编码表.技术等级Id
	 */
	private Long technologyGradeId = null;

	/**
	 * 档案编号
	 */
	private String archivesId = null;

	/**
	 * 紧急联系电话
	 */
	private String instancyTel = null;

	/**
	 * 劳保工种编码.劳保工种ID
	 */
	private Long lbWorkId = null;

	/**
	 * 工种编码表.工种ID
	 */
	private Long typeOfWorkId = null;

	/**
	 * 执行岗级
	 */
	private Long checkStationGrade = null;

	/**
	 * 出生日期
	 */
	private String brithday = null;

	/**
	 * 参加工作时间
	 */
	private String workDate = null;

	/**
	 * 员工编码
	 */
	private String empCode = null;

	/**
	 * 家庭电话
	 */
	private String familyTel = null;

	/**
	 * 民族编码表.民族
	 */
	private Long nationCodeId = null;

	/**
	 * 员工身份维护表.员工身份Id
	 */
	private Long workId = null;

	/**
	 * 备注
	 */
	private String memo = null;

	/**
	 * 紧急联系人
	 */
	private String instancyMan = null;

	/**
	 * 特长
	 */
	private String oneStrongSuit = null;

	/**
	 * 学位编码.学位Id
	 */
	private Long degreeId = null;

	/**
	 * 是否退转军人
	 */
	private String isVeteran = null;

	/**
	 * 身份证
	 */
	private String identityCard = null;

	/**
	 * 工资卡号
	 */
	private String payCardId = null;

	/**
	 * 学习专业编码.学习专业Id
	 */
	private Long specialtyCodeId = null;

	/**
	 * 社保卡号
	 */
	private String socialInsuranceId = null;

	/**
	 * 电子邮件
	 */
	private String eMail = null;

	/**
	 * 英文名
	 */
	private String enName = null;

	/**
	 * 办公电话2
	 */
	private String officeTel2 = null;

	/**
	 * 婚否
	 */
	private String isWedded = null;

	/**
	 * 学历编码.学历Id
	 */
	private Long educationId = null;

	/**
	 * 劳保工种编码.劳保工种名称
	 */
	private String lbWorkName = null;

	/**
	 * 学历编码.学历名称
	 */
	private String educationName = null;

	/**
	 * 技术职称编码表.技术职称
	 */
	private String technologyTitlesName = null;

	/**
	 * 学位编码.学位名称
	 */
	private String degreeName = null;

	/**
	 * 民族编码表.民族名称
	 */
	private String nationName = null;

	/**
	 * 学校编码.学校编码Id
	 */
	private Long schoolCodeId = null;

	/**
	 * 政治面貌.政治面貌名称
	 */
	private String politicsName = null;

	/**
	 * 员工身份维护表.员工身份
	 */
	private String workName = null;

	/**
	 * 员工类别设置.员工类别名称
	 */
	private String empTypeName = null;

	/**
	 * 籍贯编码表.籍贯名称
	 */
	private String nativePlaceName = null;

	/**
	 * 技术等级编码表.技术等级名称
	 */
	private String technologyGradeName = null;

	/**
	 * 工种编码表.工种名称
	 */
	private String typeOfWorkName = null;

	/**
	 * 学校编码.学校名称
	 */
	private String schoolName = null;

	/**
	 * 学习专业编码.学习专业名称
	 */
	private String specialtyName = null;

	/**
	 * 岗位设置表.岗位名称
	 */
	private String stationName = null;
	/**
	 * 员工状态
	 */
	private String empState = null;

	/**
	 * 部门Id
	 */
	private Long deptId = null;
	
	/**
	 * 新工号 add by liuyi 20100406 
	 */
	private String newEmpCode = null;
	
	// add by liuyi 20100603 
	private String jnGrade = null;//JN_GRADE 技能等级 
	private String isMainWork = null;//IS_MAIN_WORK	是否主业
	private String  intoPartDate = null;//INTO_PART_DATE	入党时间
	private String  partPositiveDate = null;//PART_POSITIVE_DATE	入党转正时间
	private String household = null;//HOUSEHOLD
	private String country = null;//COUNTRY
	private String bloodType= null;//BLOOD_TYPE
	private String component = null;//COMPONENT

	private String isSpecialTrades = null;//add by drdu 20100629
	private String isCadres = null;//add by drdu 20100629
	
	/**
	 * 取得部门Id
	 *
	 * @return 部门Id
	 */
	public Long getDeptId() {
		return deptId;
	}

	/**
	 * 设置部门Id
	 *
	 * @param argDeptId 部门Id
	 */
	public void setDeptId(Long argDeptId) {
		deptId = argDeptId;
	}

	/**
	 * 取得员工状态
	 *
	 * @return 员工状态
	 */
	public String getEmpState() {
		return empState;
	}

	/**
	 * 设置员工状态
	 *
	 * @param argEmpState 员工状态
	 */
	public void setEmpState(String argEmpState) {
		empState = argEmpState;
	}
	
	/**
	 * 取得劳保工种编码.劳保工种名称
	 *
	 * @return 劳保工种编码.劳保工种名称
	 */
	public String getLbWorkName() {
		return lbWorkName;
	}

	/**
	 * 设置劳保工种编码.劳保工种名称
	 *
	 * @param argLbWorkName 劳保工种编码.劳保工种名称
	 */
	public void setLbWorkName(String argLbWorkName) {
		lbWorkName = argLbWorkName;
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
	 * 取得技术职称编码表.技术职称
	 *
	 * @return 技术职称编码表.技术职称
	 */
	public String getTechnologyTitlesName() {
		return technologyTitlesName;
	}

	/**
	 * 设置技术职称编码表.技术职称
	 *
	 * @param argTechnologyTitlesName 技术职称编码表.技术职称
	 */
	public void setTechnologyTitlesName(String argTechnologyTitlesName) {
		technologyTitlesName = argTechnologyTitlesName;
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
	 * 取得民族编码表.民族名称
	 *
	 * @return 民族编码表.民族名称
	 */
	public String getNationName() {
		return nationName;
	}

	/**
	 * 设置民族编码表.民族名称
	 *
	 * @param argNationName 民族编码表.民族名称
	 */
	public void setNationName(String argNationName) {
		nationName = argNationName;
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
	 * 取得政治面貌.政治面貌名称
	 *
	 * @return 政治面貌.政治面貌名称
	 */
	public String getPoliticsName() {
		return politicsName;
	}

	/**
	 * 设置政治面貌.政治面貌名称
	 *
	 * @param argPoliticsName 政治面貌.政治面貌名称
	 */
	public void setPoliticsName(String argPoliticsName) {
		politicsName = argPoliticsName;
	}

	/**
	 * 取得员工身份维护表.员工身份
	 *
	 * @return 员工身份维护表.员工身份
	 */
	public String getWorkName() {
		return workName;
	}

	/**
	 * 设置员工身份维护表.员工身份
	 *
	 * @param argWorkName 员工身份维护表.员工身份
	 */
	public void setWorkName(String argWorkName) {
		workName = argWorkName;
	}

	/**
	 * 取得员工类别设置.员工类别名称
	 *
	 * @return 员工类别设置.员工类别名称
	 */
	public String getEmpTypeName() {
		return empTypeName;
	}

	/**
	 * 设置员工类别设置.员工类别名称
	 *
	 * @param argEmpTypeName 员工类别设置.员工类别名称
	 */
	public void setEmpTypeName(String argEmpTypeName) {
		empTypeName = argEmpTypeName;
	}

	/**
	 * 取得籍贯编码表.籍贯名称
	 *
	 * @return 籍贯编码表.籍贯名称
	 */
	public String getNativePlaceName() {
		return nativePlaceName;
	}

	/**
	 * 设置籍贯编码表.籍贯名称
	 *
	 * @param argNativePlaceName 籍贯编码表.籍贯名称
	 */
	public void setNativePlaceName(String argNativePlaceName) {
		nativePlaceName = argNativePlaceName;
	}

	/**
	 * 取得技术等级编码表.技术等级名称
	 *
	 * @return 技术等级编码表.技术等级名称
	 */
	public String getTechnologyGradeName() {
		return technologyGradeName;
	}

	/**
	 * 设置技术等级编码表.技术等级名称
	 *
	 * @param argTechnologyGradeName 技术等级编码表.技术等级名称
	 */
	public void setTechnologyGradeName(String argTechnologyGradeName) {
		technologyGradeName = argTechnologyGradeName;
	}

	/**
	 * 取得工种编码表.工种名称
	 *
	 * @return 工种编码表.工种名称
	 */
	public String getTypeOfWorkName() {
		return typeOfWorkName;
	}

	/**
	 * 设置工种编码表.工种名称
	 *
	 * @param argTypeOfWorkName 工种编码表.工种名称
	 */
	public void setTypeOfWorkName(String argTypeOfWorkName) {
		typeOfWorkName = argTypeOfWorkName;
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
	 * 取得岗位设置表.岗位名称
	 *
	 * @return 岗位设置表.岗位名称
	 */
	public String getStationName() {
		return stationName;
	}

	/**
	 * 设置岗位设置表.岗位名称
	 *
	 * @param argStationName 岗位设置表.岗位名称
	 */
	public void setStationName(String argStationName) {
		stationName = argStationName;
	}

	/**
	 * 取得部门设置表.部门名称
	 *
	 * @return 部门设置表.部门名称
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * 设置部门设置表.部门名称
	 *
	 * @param argTfDeptName 部门设置表.部门名称
	 */
	public void setDeptName(String argTfDeptName) {
		deptName = argTfDeptName;
	}

	/**
	 * 取得技术职称编码表.技术职称ID
	 *
	 * @return 技术职称编码表.技术职称ID
	 */
	public Long getTechnologyTitlesId() {
		return technologyTitlesId;
	}

	/**
	 * 设置技术职称编码表.技术职称ID
	 *
	 * @param argTechnologyTitlesId 技术职称编码表.技术职称ID
	 */
	public void setTechnologyTitlesId(Long argTechnologyTitlesId) {
		technologyTitlesId = argTechnologyTitlesId;
	}

	/**
	 * 取得办公电话1
	 *
	 * @return 办公电话1
	 */
	public String getOfficeTel1() {
		return officeTel1;
	}

	/**
	 * 设置办公电话1
	 *
	 * @param argOfficeTel1 办公电话1
	 */
	public void setOfficeTel1(String argOfficeTel1) {
		officeTel1 = argOfficeTel1;
	}

	/**
	 * 取得入职时间
	 *
	 * @return 入职时间
	 */
	public String getMissionDate() {
		return missionDate;
	}

	/**
	 * 设置入职时间
	 *
	 * @param argMissionDate 入职时间
	 */
	public void setMissionDate(String argMissionDate) {
		missionDate = argMissionDate;
	}

	/**
	 * 取得标准岗级
	 *
	 * @return 标准岗级
	 */
	public Long getStationGrade() {
		return stationGrade;
	}

	/**
	 * 设置标准岗级
	 *
	 * @param argStationGrade 标准岗级
	 */
	public void setStationGrade(Long argStationGrade) {
		stationGrade = argStationGrade;
	}

	/**
	 * 取得显示顺序
	 *
	 * @return 显示顺序
	 */
	public Long getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置显示顺序
	 *
	 * @param argOrderBy 显示顺序
	 */
	public void setOrderBy(Long argOrderBy) {
		orderBy = argOrderBy;
	}

	/**
	 * 取得薪级
	 *
	 * @return 薪级
	 */
	public Long getSalaryLevel() {
		return salaryLevel;
	}

	/**
	 * 设置薪级
	 *
	 * @param argSalaryLevel 薪级
	 */
	public void setSalaryLevel(Long argSalaryLevel) {
		salaryLevel = argSalaryLevel;
	}

	/**
	 * 取得参保时间
	 *
	 * @return 参保时间
	 */
	public String getSocialInsuranceDate() {
		return socialInsuranceDate;
	}

	/**
	 * 设置参保时间
	 *
	 * @param argSocialInsuranceDate 参保时间
	 */
	public void setSocialInsuranceDate(String argSocialInsuranceDate) {
		socialInsuranceDate = argSocialInsuranceDate;
	}

	/**
	 * 取得传真
	 *
	 * @return 传真
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * 设置传真
	 *
	 * @param argFax 传真
	 */
	public void setFax(String argFax) {
		fax = argFax;
	}

	/**
	 * 取得推荐人
	 *
	 * @return 推荐人
	 */
	public String getRecommendMan() {
		return recommendMan;
	}

	/**
	 * 设置推荐人
	 *
	 * @param argRecommendMan 推荐人
	 */
	public void setRecommendMan(String argRecommendMan) {
		recommendMan = argRecommendMan;
	}

	/**
	 * 取得人员姓名
	 *
	 * @return 人员姓名
	 */
	public String getChsName() {
		return chsName;
	}

	/**
	 * 设置人员姓名
	 *
	 * @param argChsName 人员姓名
	 */
	public void setChsName(String argChsName) {
		chsName = argChsName;
	}

	/**
	 * 取得性别
	 *
	 * @return 性别
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 设置性别
	 *
	 * @param argSex 性别
	 */
	public void setSex(String argSex) {
		sex = argSex;
	}

	/**
	 * 取得手机
	 *
	 * @return 手机
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * 设置手机
	 *
	 * @param argMobilePhone 手机
	 */
	public void setMobilePhone(String argMobilePhone) {
		mobilePhone = argMobilePhone;
	}

	/**
	 * 取得员工类别设置.员工类别Id
	 *
	 * @return 员工类别设置.员工类别Id
	 */
	public Long getEmpTypeId() {
		return empTypeId;
	}

	/**
	 * 设置员工类别设置.员工类别Id
	 *
	 * @param argEmpTypeId 员工类别设置.员工类别Id
	 */
	public void setEmpTypeId(Long argEmpTypeId) {
		empTypeId = argEmpTypeId;
	}

	/**
	 * 取得毕业时间
	 *
	 * @return 毕业时间
	 */
	public String getGraduateDate() {
		return graduateDate;
	}

	/**
	 * 设置毕业时间
	 *
	 * @param argGraduateDate 毕业时间
	 */
	public void setGraduateDate(String argGraduateDate) {
		graduateDate = argGraduateDate;
	}

	/**
	 * 取得职工照片表.图片
	 *
	 * @return 职工照片表.图片
	 */
	public byte[] getPhoto() {
		return photo;
	}

	/**
	 * 设置职工照片表.图片
	 *
	 * @param argPhoto 职工照片表.图片
	 */
	public void setPhoto(byte[] argPhoto) {
		photo = argPhoto;
	}

	/**
	 * 取得政治面貌.政治面貌
	 *
	 * @return 政治面貌.政治面貌
	 */
	public Long getPoliticsId() {
		return politicsId;
	}

	/**
	 * 设置政治面貌.政治面貌
	 *
	 * @param argPoliticsId 政治面貌.政治面貌
	 */
	public void setPoliticsId(Long argPoliticsId) {
		politicsId = argPoliticsId;
	}

	/**
	 * 取得家庭住址
	 *
	 * @return 家庭住址
	 */
	public String getFamilyAddress() {
		return familyAddress;
	}

	/**
	 * 设置家庭住址
	 *
	 * @param argFamilyAddress 家庭住址
	 */
	public void setFamilyAddress(String argFamilyAddress) {
		familyAddress = argFamilyAddress;
	}

	/**
	 * 取得岗位设置表.岗位Id
	 *
	 * @return 岗位设置表.岗位Id
	 */
	public Long getStationId() {
		return stationId;
	}

	/**
	 * 设置岗位设置表.岗位Id
	 *
	 * @param argStationId 岗位设置表.岗位Id
	 */
	public void setStationId(Long argStationId) {
		stationId = argStationId;
	}

	/**
	 * 取得考勤卡号
	 *
	 * @return 考勤卡号
	 */
	public String getTimeCardId() {
		return timeCardId;
	}

	/**
	 * 设置考勤卡号
	 *
	 * @param argTimeCardId 考勤卡号
	 */
	public void setTimeCardId(String argTimeCardId) {
		timeCardId = argTimeCardId;
	}

	/**
	 * 取得籍贯编码表.籍贯
	 *
	 * @return 籍贯编码表.籍贯
	 */
	public Long getNativePlaceId() {
		return nativePlaceId;
	}

	/**
	 * 设置籍贯编码表.籍贯
	 *
	 * @param argNativePlaceId 籍贯编码表.籍贯
	 */
	public void setNativePlaceId(Long argNativePlaceId) {
		nativePlaceId = argNativePlaceId;
	}

	/**
	 * 取得邮政编码
	 *
	 * @return 邮政编码
	 */
	public String getPostalcode() {
		return postalcode;
	}

	/**
	 * 设置邮政编码
	 *
	 * @param argPostalcode 邮政编码
	 */
	public void setPostalcode(String argPostalcode) {
		postalcode = argPostalcode;
	}

	/**
	 * 取得技术等级编码表.技术等级Id
	 *
	 * @return 技术等级编码表.技术等级Id
	 */
	public Long getTechnologyGradeId() {
		return technologyGradeId;
	}

	/**
	 * 设置技术等级编码表.技术等级Id
	 *
	 * @param argTechnologyGradeId 技术等级编码表.技术等级Id
	 */
	public void setTechnologyGradeId(Long argTechnologyGradeId) {
		technologyGradeId = argTechnologyGradeId;
	}

	/**
	 * 取得档案编号
	 *
	 * @return 档案编号
	 */
	public String getArchivesId() {
		return archivesId;
	}

	/**
	 * 设置档案编号
	 *
	 * @param argArchivesId 档案编号
	 */
	public void setArchivesId(String argArchivesId) {
		archivesId = argArchivesId;
	}

	/**
	 * 取得紧急联系电话
	 *
	 * @return 紧急联系电话
	 */
	public String getInstancyTel() {
		return instancyTel;
	}

	/**
	 * 设置紧急联系电话
	 *
	 * @param argInstancyTel 紧急联系电话
	 */
	public void setInstancyTel(String argInstancyTel) {
		instancyTel = argInstancyTel;
	}

	/**
	 * 取得劳保工种编码.劳保工种ID
	 *
	 * @return 劳保工种编码.劳保工种ID
	 */
	public Long getLbWorkId() {
		return lbWorkId;
	}

	/**
	 * 设置劳保工种编码.劳保工种ID
	 *
	 * @param argLbWorkId 劳保工种编码.劳保工种ID
	 */
	public void setLbWorkId(Long argLbWorkId) {
		lbWorkId = argLbWorkId;
	}

	/**
	 * 取得工种编码表.工种ID
	 *
	 * @return 工种编码表.工种ID
	 */
	public Long getTypeOfWorkId() {
		return typeOfWorkId;
	}

	/**
	 * 设置工种编码表.工种ID
	 *
	 * @param argTypeOfWorkId 工种编码表.工种ID
	 */
	public void setTypeOfWorkId(Long argTypeOfWorkId) {
		typeOfWorkId = argTypeOfWorkId;
	}

	/**
	 * 取得执行岗级
	 *
	 * @return 执行岗级
	 */
	public Long getCheckStationGrade() {
		return checkStationGrade;
	}

	/**
	 * 设置执行岗级
	 *
	 * @param argCheckStationGrade 执行岗级
	 */
	public void setCheckStationGrade(Long argCheckStationGrade) {
		checkStationGrade = argCheckStationGrade;
	}

	/**
	 * 取得出生日期
	 *
	 * @return 出生日期
	 */
	public String getBrithday() {
		return brithday;
	}

	/**
	 * 设置出生日期
	 *
	 * @param argBrithday 出生日期
	 */
	public void setBrithday(String argBrithday) {
		brithday = argBrithday;
	}

	/**
	 * 取得参加工作时间
	 *
	 * @return 参加工作时间
	 */
	public String getWorkDate() {
		return workDate;
	}

	/**
	 * 设置参加工作时间
	 *
	 * @param argWorkDate 参加工作时间
	 */
	public void setWorkDate(String argWorkDate) {
		workDate = argWorkDate;
	}

	/**
	 * 取得员工编码
	 *
	 * @return 员工编码
	 */
	public String getEmpCode() {
		return empCode;
	}

	/**
	 * 设置员工编码
	 *
	 * @param argEmpCode 员工编码
	 */
	public void setEmpCode(String argEmpCode) {
		empCode = argEmpCode;
	}

	/**
	 * 取得家庭电话
	 *
	 * @return 家庭电话
	 */
	public String getFamilyTel() {
		return familyTel;
	}

	/**
	 * 设置家庭电话
	 *
	 * @param argFamilyTel 家庭电话
	 */
	public void setFamilyTel(String argFamilyTel) {
		familyTel = argFamilyTel;
	}

	/**
	 * 取得民族编码表.民族
	 *
	 * @return 民族编码表.民族
	 */
	public Long getNationCodeId() {
		return nationCodeId;
	}

	/**
	 * 设置民族编码表.民族
	 *
	 * @param argNationCodeId 民族编码表.民族
	 */
	public void setNationCodeId(Long argNationCodeId) {
		nationCodeId = argNationCodeId;
	}

	/**
	 * 取得员工身份维护表.员工身份Id
	 *
	 * @return 员工身份维护表.员工身份Id
	 */
	public Long getWorkId() {
		return workId;
	}

	/**
	 * 设置员工身份维护表.员工身份Id
	 *
	 * @param argWorkId 员工身份维护表.员工身份Id
	 */
	public void setWorkId(Long argWorkId) {
		workId = argWorkId;
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
	 * 取得紧急联系人
	 *
	 * @return 紧急联系人
	 */
	public String getInstancyMan() {
		return instancyMan;
	}

	/**
	 * 设置紧急联系人
	 *
	 * @param argInstancyMan 紧急联系人
	 */
	public void setInstancyMan(String argInstancyMan) {
		instancyMan = argInstancyMan;
	}

	/**
	 * 取得特长
	 *
	 * @return 特长
	 */
	public String getOneStrongSuit() {
		return oneStrongSuit;
	}

	/**
	 * 设置特长
	 *
	 * @param argOneStrongSuit 特长
	 */
	public void setOneStrongSuit(String argOneStrongSuit) {
		oneStrongSuit = argOneStrongSuit;
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
	 * 取得是否退转军人
	 *
	 * @return 是否退转军人
	 */
	public String getIsVeteran() {
		return isVeteran;
	}

	/**
	 * 设置是否退转军人
	 *
	 * @param argIsVeteran 是否退转军人
	 */
	public void setIsVeteran(String argIsVeteran) {
		isVeteran = argIsVeteran;
	}

	/**
	 * 取得身份证
	 *
	 * @return 身份证
	 */
	public String getIdentityCard() {
		return identityCard;
	}

	/**
	 * 设置身份证
	 *
	 * @param argIdentityCard 身份证
	 */
	public void setIdentityCard(String argIdentityCard) {
		identityCard = argIdentityCard;
	}

	/**
	 * 取得工资卡号
	 *
	 * @return 工资卡号
	 */
	public String getPayCardId() {
		return payCardId;
	}

	/**
	 * 设置工资卡号
	 *
	 * @param argPayCardId 工资卡号
	 */
	public void setPayCardId(String argPayCardId) {
		payCardId = argPayCardId;
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
	 * 取得社保卡号
	 *
	 * @return 社保卡号
	 */
	public String getSocialInsuranceId() {
		return socialInsuranceId;
	}

	/**
	 * 设置社保卡号
	 *
	 * @param argSocialInsuranceId 社保卡号
	 */
	public void setSocialInsuranceId(String argSocialInsuranceId) {
		socialInsuranceId = argSocialInsuranceId;
	}

	/**
	 * 取得电子邮件
	 *
	 * @return 电子邮件
	 */
	public String getEMail() {
		return eMail;
	}

	/**
	 * 设置电子邮件
	 *
	 * @param argEMail 电子邮件
	 */
	public void setEMail(String argEMail) {
		eMail = argEMail;
	}

	/**
	 * 取得英文名
	 *
	 * @return 英文名
	 */
	public String getEnName() {
		return enName;
	}

	/**
	 * 设置英文名
	 *
	 * @param argEnName 英文名
	 */
	public void setEnName(String argEnName) {
		enName = argEnName;
	}

	/**
	 * 取得办公电话2
	 *
	 * @return 办公电话2
	 */
	public String getOfficeTel2() {
		return officeTel2;
	}

	/**
	 * 设置办公电话2
	 *
	 * @param argOfficeTel2 办公电话2
	 */
	public void setOfficeTel2(String argOfficeTel2) {
		officeTel2 = argOfficeTel2;
	}

	/**
	 * 取得婚否
	 *
	 * @return 婚否
	 */
	public String getIsWedded() {
		return isWedded;
	}

	/**
	 * 设置婚否
	 *
	 * @param argIsWedded 婚否
	 */
	public void setIsWedded(String argIsWedded) {
		isWedded = argIsWedded;
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

	/**
	 * 取得人员ID
	 *
	 * @return 人员ID
	 */
	public Long getEmpId() {
		return empId;
	}

	/**
	 * 设置人员ID
	 *
	 * @param argEmpId 人员ID
	 */
	public void setEmpId(Long argEmpId) {
		empId = argEmpId;
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
	 * 取得照片上次修改时间
	 *
	 * @return 照片上次修改时间
	 */
	public String getPhotoLastModifiedDate() {
		return photoLastModifiedDate;
	}
	
	/**
	 * 设置照片上次修改时间
	 *
	 * @param argLastModifiedDate 照片上次修改时间
	 */
	public void setPhotoLastModifiedDate(String argLastModifiedDate) {
		photoLastModifiedDate = argLastModifiedDate;
	}

	public String getNewEmpCode() {
		return newEmpCode;
	}

	public void setNewEmpCode(String newEmpCode) {
		this.newEmpCode = newEmpCode;
	}

	public String getJnGrade() {
		return jnGrade;
	}

	public void setJnGrade(String jnGrade) {
		this.jnGrade = jnGrade;
	}

	public String getIsMainWork() {
		return isMainWork;
	}

	public void setIsMainWork(String isMainWork) {
		this.isMainWork = isMainWork;
	}

	public String getIntoPartDate() {
		return intoPartDate;
	}

	public void setIntoPartDate(String intoPartDate) {
		this.intoPartDate = intoPartDate;
	}

	public String getPartPositiveDate() {
		return partPositiveDate;
	}

	public void setPartPositiveDate(String partPositiveDate) {
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

	public String getIsSpecialTrades() {
		return isSpecialTrades;
	}

	public void setIsSpecialTrades(String isSpecialTrades) {
		this.isSpecialTrades = isSpecialTrades;
	}

	public String getIsCadres() {
		return isCadres;
	}

	public void setIsCadres(String isCadres) {
		this.isCadres = isCadres;
	}
}
