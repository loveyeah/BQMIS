/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.bean.hr;

import java.util.List;
import power.ejb.hr.*;

/**
 * 职工履历表Bean
 * @author wangjunjie
 *
 */
public class EmployeeRecordBean {

    // 姓名
    private String name;
    // 英文名
    private String nameEn;
    // 性别
    private String sex;
    // 出生年月
    private String birthday;
    // 民族
    private String nation;
    // 籍贯
    private String home;
    // 婚姻状况
    private String marryStatus;
    // 身份证号码
    private String idNum;
    // 参加工作时间
    private String firstJobTime;
    // 入本企业时间
    private String toHereTime;
    // 政治面貌
    private String politics;
    // 职称
    private String technologyPost;
    // 原始学历
    private String originalEdu;
    // 原始所学专业
    private String originalSpecial;
    // 原始毕业时间
    private String oriGraduateTime;
    // 当前学历
    private String nowEdu;
    // 所学专业
    private String nowSpecial;
    // 毕业时间
    private String nowGraduateTime;
    // 家庭住址
    private String homeAdds;
    // 员工ID
    private Long empId;   
    // 学历教育登记列表
    private List<HrJEducationBean> educationList;
    // 工作简历登记列表
    private List<HrJWorkresume> workresumeList;
    // 社会关系登记列表
    private List<HrJFamilymemberBean> familymemberList;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the nameEn
	 */
	public String getNameEn() {
		return nameEn;
	}
	/**
	 * @param nameEn the nameEn to set
	 */
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
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
	 * @return the nation
	 */
	public String getNation() {
		return nation;
	}
	/**
	 * @param nation the nation to set
	 */
	public void setNation(String nation) {
		this.nation = nation;
	}
	/**
	 * @return the home
	 */
	public String getHome() {
		return home;
	}
	/**
	 * @param home the home to set
	 */
	public void setHome(String home) {
		this.home = home;
	}
	/**
	 * @return the marryStatus
	 */
	public String getMarryStatus() {
		return marryStatus;
	}
	/**
	 * @param marryStatus the marryStatus to set
	 */
	public void setMarryStatus(String marryStatus) {
		this.marryStatus = marryStatus;
	}
	/**
	 * @return the idNum
	 */
	public String getIdNum() {
		return idNum;
	}
	/**
	 * @param idNum the idNum to set
	 */
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	/**
	 * @return the firstJobTime
	 */
	public String getFirstJobTime() {
		return firstJobTime;
	}
	/**
	 * @param firstJobTime the firstJobTime to set
	 */
	public void setFirstJobTime(String firstJobTime) {
		this.firstJobTime = firstJobTime;
	}
	/**
	 * @return the toHereTime
	 */
	public String getToHereTime() {
		return toHereTime;
	}
	/**
	 * @param toHereTime the toHereTime to set
	 */
	public void setToHereTime(String toHereTime) {
		this.toHereTime = toHereTime;
	}
	/**
	 * @return the politics
	 */
	public String getPolitics() {
		return politics;
	}
	/**
	 * @param politics the politics to set
	 */
	public void setPolitics(String politics) {
		this.politics = politics;
	}	
	/**
	 * @return the technologyPost
	 */
	public String getTechnologyPost() {
		return technologyPost;
	}
	/**
	 * @param technologyPost the technologyPost to set
	 */
	public void setTechnologyPost(String technologyPost) {
		this.technologyPost = technologyPost;
	}
	/**
	 * @return the originalEdu
	 */
	public String getOriginalEdu() {
		return originalEdu;
	}
	/**
	 * @param originalEdu the originalEdu to set
	 */
	public void setOriginalEdu(String originalEdu) {
		this.originalEdu = originalEdu;
	}
	/**
	 * @return the originalSpecial
	 */
	public String getOriginalSpecial() {
		return originalSpecial;
	}
	/**
	 * @param originalSpecial the originalSpecial to set
	 */
	public void setOriginalSpecial(String originalSpecial) {
		this.originalSpecial = originalSpecial;
	}
	/**
	 * @return the oriGraduateTime
	 */
	public String getOriGraduateTime() {
		return oriGraduateTime;
	}
	/**
	 * @param oriGraduateTime the oriGraduateTime to set
	 */
	public void setOriGraduateTime(String oriGraduateTime) {
		this.oriGraduateTime = oriGraduateTime;
	}
	/**
	 * @return the nowEdu
	 */
	public String getNowEdu() {
		return nowEdu;
	}
	/**
	 * @param nowEdu the nowEdu to set
	 */
	public void setNowEdu(String nowEdu) {
		this.nowEdu = nowEdu;
	}
	/**
	 * @return the nowSpecial
	 */
	public String getNowSpecial() {
		return nowSpecial;
	}
	/**
	 * @param nowSpecial the nowSpecial to set
	 */
	public void setNowSpecial(String nowSpecial) {
		this.nowSpecial = nowSpecial;
	}
	/**
	 * @return the nowGraduateTime
	 */
	public String getNowGraduateTime() {
		return nowGraduateTime;
	}
	/**
	 * @param nowGraduateTime the nowGraduateTime to set
	 */
	public void setNowGraduateTime(String nowGraduateTime) {
		this.nowGraduateTime = nowGraduateTime;
	}
	public String getHomeAdds() {
		return homeAdds;
	}
	public void setHomeAdds(String homeAdds) {
		this.homeAdds = homeAdds;
	}
	/**
	 * @return the empId
	 */
	public Long getEmpId() {
		return empId;
	}
	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	/**
	 * @return the educationList
	 */
	public List<HrJEducationBean> getEducationList() {
		return educationList;
	}
	/**
	 * @param educationList the educationList to set
	 */
	public void setEducationList(List<HrJEducationBean> educationList) {
		this.educationList = educationList;
	}
	/**
	 * @return the workresumeList
	 */
	public List<HrJWorkresume> getWorkresumeList() {
		return workresumeList;
	}
	/**
	 * @param workresumeList the workresumeList to set
	 */
	public void setWorkresumeList(List<HrJWorkresume> workresumeList) {
		this.workresumeList = workresumeList;
	}
	/**
	 * @return the familymemberList
	 */
	public List<HrJFamilymemberBean> getFamilymemberList() {
		return familymemberList;
	}
	/**
	 * @param familymemberList the familymemberList to set
	 */
	public void setFamilymemberList(List<HrJFamilymemberBean> familymemberList) {
		this.familymemberList = familymemberList;
	}

	
    
}