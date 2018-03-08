package power.ejb.hr.form;


public class EmpInfo {
	private String empId;
	private String chsName;
	private String enName;
	private String empCode;
	private String deptId;
	private String deptCode;
	private String deptName;
	private String retrieveCode;
	private String nationId;
	private String nativePlaceId;
	private String politicsId;
	private String brithday;
	private String sex;
	private String isWedded;
	private String archivesId;
	private String empStationId;
	private String stationId;
	private String stationName;
	private String stationLevel;
	private String gradation;
	private String empTypeId;
	private String workDate;
	private String missionDate;
	private String dimissionDate;
	private String technologyTitlesTypeId;
	private String technologyTitlesTypeName;
	private String technologyGradeId;
	private String typeOfWorkId;
	private String identityCard;
	private String timeCardId;
	private String payCardId;
	private String socialInsuranceId;
	private String socialInsuranceDate;
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
	private String educationId;
	private String degreeId;
	private String speciality;
	private String graduateDate;
	private String isVeteran;
	private String isBorrow;
	private String isRetired;
	private String empState;
	private String orderBy;
	private String recommendMan;
	private String assistantManagerUnitsId;
	private String oneStrongSuit;
	private String memo;
	private String createBy;
	private String createDate;
	private String lastModifiyBy;
	private String lastModifiyDate;
	private String photo;
	private String curriculumVitae;
	private String societyInfo;
	//add by wpzhu 
	private String isSpecailTrades;
	private String isCardes;
	private String politicsName;
//	------------------------------
	
	// 标准岗级 add by liuyi 090922
	private String stationGrade;
	//薪级 add by liuyi 090922
	private String salaryLevel;
	// 执行岗级 add by liuyi 090922
	private String checkStationGrade;
	//考勤部门id add by liuyi 090922
	private String  attendanceDeptId;
	// 考勤部门名 add by liuyi 090922
	private String attendanceDeptName;
	//班组 add by liuyi 090922
	private String banzuId;
	// 班组编码 add by liuyi 090922
	private String banzuCode;
	// 班组名 add by liuyi 090922
	private String banzuName;
	//退休日期  add by drdu 091223
	private String retirementDate;
	//年龄  add by drdu 091223
	private String age;
	//退休预警倒计时  add by drdu 091223
	private String countDown;

	public String getCurriculumVitae() {
		return curriculumVitae;
	}
	public void setCurriculumVitae(String curriculumVitae) {
		this.curriculumVitae = curriculumVitae;
	}
	
	public String getSocietyInfo() {
		return societyInfo;
	}
	public void setSocietyInfo(String societyInfo) {
		this.societyInfo = societyInfo;
	}
	
	public String getChsName() {
		return chsName;
	}
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getRetrieveCode() {
		return retrieveCode;
	}
	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

	

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIsWedded() {
		return isWedded;
	}
	public void setIsWedded(String isWedded) {
		this.isWedded = isWedded;
	}
	public String getArchivesId() {
		return archivesId;
	}
	public void setArchivesId(String archivesId) {
		this.archivesId = archivesId;
	}

	public String getIdentityCard() {
		return identityCard;
	}
	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}
	public String getTimeCardId() {
		return timeCardId;
	}
	public void setTimeCardId(String timeCardId) {
		this.timeCardId = timeCardId;
	}
	public String getPayCardId() {
		return payCardId;
	}
	public void setPayCardId(String payCardId) {
		this.payCardId = payCardId;
	}
	public String getSocialInsuranceId() {
		return socialInsuranceId;
	}
	public void setSocialInsuranceId(String socialInsuranceId) {
		this.socialInsuranceId = socialInsuranceId;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getFamilyTel() {
		return familyTel;
	}
	public void setFamilyTel(String familyTel) {
		this.familyTel = familyTel;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getMsn() {
		return msn;
	}
	public void setMsn(String msn) {
		this.msn = msn;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getFamilyAddress() {
		return familyAddress;
	}
	public void setFamilyAddress(String familyAddress) {
		this.familyAddress = familyAddress;
	}
	public String getOfficeTel1() {
		return officeTel1;
	}
	public void setOfficeTel1(String officeTel1) {
		this.officeTel1 = officeTel1;
	}
	public String getOfficeTel2() {
		return officeTel2;
	}
	public void setOfficeTel2(String officeTel2) {
		this.officeTel2 = officeTel2;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEMail() {
		return EMail;
	}
	public void setEMail(String mail) {
		EMail = mail;
	}
	public String getInstancyMan() {
		return instancyMan;
	}
	public void setInstancyMan(String instancyMan) {
		this.instancyMan = instancyMan;
	}
	public String getInstancyTel() {
		return instancyTel;
	}
	public void setInstancyTel(String instancyTel) {
		this.instancyTel = instancyTel;
	}
	public String getGraduateSchool() {
		return graduateSchool;
	}
	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getIsVeteran() {
		return isVeteran;
	}
	public void setIsVeteran(String isVeteran) {
		this.isVeteran = isVeteran;
	}
	public String getIsBorrow() {
		return isBorrow;
	}
	public void setIsBorrow(String isBorrow) {
		this.isBorrow = isBorrow;
	}
	public String getIsRetired() {
		return isRetired;
	}
	public void setIsRetired(String isRetired) {
		this.isRetired = isRetired;
	}
	public String getEmpState() {
		return empState;
	}
	public void setEmpState(String empState) {
		this.empState = empState;
	}

	public String getRecommendMan() {
		return recommendMan;
	}
	public void setRecommendMan(String recommendMan) {
		this.recommendMan = recommendMan;
	}

	public String getOneStrongSuit() {
		return oneStrongSuit;
	}
	public void setOneStrongSuit(String oneStrongSuit) {
		this.oneStrongSuit = oneStrongSuit;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}


	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getBrithday() {
		return brithday;
	}
	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getMissionDate() {
		return missionDate;
	}
	public void setMissionDate(String missionDate) {
		this.missionDate = missionDate;
	}
	public String getDimissionDate() {
		return dimissionDate;
	}
	public void setDimissionDate(String dimissionDate) {
		this.dimissionDate = dimissionDate;
	}
	public String getSocialInsuranceDate() {
		return socialInsuranceDate;
	}
	public void setSocialInsuranceDate(String socialInsuranceDate) {
		this.socialInsuranceDate = socialInsuranceDate;
	}
	public String getGraduateDate() {
		return graduateDate;
	}
	public void setGraduateDate(String graduateDate) {
		this.graduateDate = graduateDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getLastModifiyDate() {
		return lastModifiyDate;
	}
	public void setLastModifiyDate(String lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getNationId() {
		return nationId;
	}
	public void setNationId(String nationId) {
		this.nationId = nationId;
	}
	public String getNativePlaceId() {
		return nativePlaceId;
	}
	public void setNativePlaceId(String nativePlaceId) {
		this.nativePlaceId = nativePlaceId;
	}
	public String getPoliticsId() {
		return politicsId;
	}
	public void setPoliticsId(String politicsId) {
		this.politicsId = politicsId;
	}
	public String getEmpStationId() {
		return empStationId;
	}
	public void setEmpStationId(String empStationId) {
		this.empStationId = empStationId;
	}
	public String getStationLevel() {
		return stationLevel;
	}
	public void setStationLevel(String stationLevel) {
		this.stationLevel = stationLevel;
	}
	public String getGradation() {
		return gradation;
	}
	public void setGradation(String gradation) {
		this.gradation = gradation;
	}
	public String getEmpTypeId() {
		return empTypeId;
	}
	public void setEmpTypeId(String empTypeId) {
		this.empTypeId = empTypeId;
	}
	public String getTechnologyTitlesTypeId() {
		return technologyTitlesTypeId;
	}
	public void setTechnologyTitlesTypeId(String technologyTitlesTypeId) {
		this.technologyTitlesTypeId = technologyTitlesTypeId;
	}
	
	public String getTechnologyTitlesTypeName() {
		return technologyTitlesTypeName;
	}
	public void setTechnologyTitlesTypeName(String technologyTitlesTypeName) {
		this.technologyTitlesTypeName = technologyTitlesTypeName;
	}
	public String getTechnologyGradeId() {
		return technologyGradeId;
	}
	public void setTechnologyGradeId(String technologyGradeId) {
		this.technologyGradeId = technologyGradeId;
	}
	public String getTypeOfWorkId() {
		return typeOfWorkId;
	}
	public void setTypeOfWorkId(String typeOfWorkId) {
		this.typeOfWorkId = typeOfWorkId;
	}
	public String getEducationId() {
		return educationId;
	}
	public void setEducationId(String educationId) {
		this.educationId = educationId;
	}
	public String getDegreeId() {
		return degreeId;
	}
	public void setDegreeId(String degreeId) {
		this.degreeId = degreeId;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getAssistantManagerUnitsId() {
		return assistantManagerUnitsId;
	}
	public void setAssistantManagerUnitsId(String assistantManagerUnitsId) {
		this.assistantManagerUnitsId = assistantManagerUnitsId;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getLastModifiyBy() {
		return lastModifiyBy;
	}
	public void setLastModifiyBy(String lastModifiyBy) {
		this.lastModifiyBy = lastModifiyBy;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getStationGrade() {
		return stationGrade;
	}
	public void setStationGrade(String stationGrade) {
		this.stationGrade = stationGrade;
	}
	public String getSalaryLevel() {
		return salaryLevel;
	}
	public void setSalaryLevel(String salaryLevel) {
		this.salaryLevel = salaryLevel;
	}
	public String getCheckStationGrade() {
		return checkStationGrade;
	}
	public void setCheckStationGrade(String checkStationGrade) {
		this.checkStationGrade = checkStationGrade;
	}
	public String getAttendanceDeptId() {
		return attendanceDeptId;
	}
	public void setAttendanceDeptId(String attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}
	public String getAttendanceDeptName() {
		return attendanceDeptName;
	}
	public void setAttendanceDeptName(String attendanceDeptName) {
		this.attendanceDeptName = attendanceDeptName;
	}
	public String getBanzuId() {
		return banzuId;
	}
	public void setBanzuId(String banzuId) {
		this.banzuId = banzuId;
	}
	public String getBanzuCode() {
		return banzuCode;
	}
	public void setBanzuCode(String banzuCode) {
		this.banzuCode = banzuCode;
	}
	public String getBanzuName() {
		return banzuName;
	}
	public void setBanzuName(String banzuName) {
		this.banzuName = banzuName;
	}
	public String getRetirementDate() {
		return retirementDate;
	}
	public void setRetirementDate(String retirementDate) {
		this.retirementDate = retirementDate;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getCountDown() {
		return countDown;
	}
	public void setCountDown(String countDown) {
		this.countDown = countDown;
	}
	public String getIsSpecailTrades() {
		return isSpecailTrades;
	}
	public void setIsSpecailTrades(String isSpecailTrades) {
		this.isSpecailTrades = isSpecailTrades;
	}
	public String getIsCardes() {
		return isCardes;
	}
	public void setIsCardes(String isCardes) {
		this.isCardes = isCardes;
	}
	public String getPoliticsName() {
		return politicsName;
	}
	public void setPoliticsName(String politicsName) {
		this.politicsName = politicsName;
	}


}
