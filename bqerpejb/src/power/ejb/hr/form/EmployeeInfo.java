package power.ejb.hr.form;

import power.ejb.hr.HrJEmpInfo;

@SuppressWarnings("serial")
public class EmployeeInfo implements java.io.Serializable{
	private HrJEmpInfo model; 
	private String nationName;//民族
	private String cityName; //城市
	private String politicsName ;//政治面貌
	private String belongDeptName;//所属部门
	private String workStationName;//工作岗位
	private String stationLevelName;//岗位级别
	private String workTypeName;//工种
	private String workerTypeName;//员工类别
	private String skillTypeName;//技能名称
	private String skillLevelName;//技术等级
	private String educationName;//学历
	private String degreeName;//学位
	private String managerUnit;//协理单位
	public HrJEmpInfo getModel() {
		return model;
	}
	public void setModel(HrJEmpInfo model) {
		this.model = model;
	}
	public String getNationName() {
		return nationName;
	}
	public void setNationName(String nationName) {
		this.nationName = nationName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getPoliticsName() {
		return politicsName;
	}
	public void setPoliticsName(String politicsName) {
		this.politicsName = politicsName;
	}
	public String getBelongDeptName() {
		return belongDeptName;
	}
	public void setBelongDeptName(String belongDeptName) {
		this.belongDeptName = belongDeptName;
	}
	public String getWorkStationName() {
		return workStationName;
	}
	public void setWorkStationName(String workStationName) {
		this.workStationName = workStationName;
	}
	public String getStationLevelName() {
		return stationLevelName;
	}
	public void setStationLevelName(String stationLevelName) {
		this.stationLevelName = stationLevelName;
	}
	public String getWorkTypeName() {
		return workTypeName;
	}
	public void setWorkTypeName(String workTypeName) {
		this.workTypeName = workTypeName;
	}
	public String getWorkerTypeName() {
		return workerTypeName;
	}
	public void setWorkerTypeName(String workerTypeName) {
		this.workerTypeName = workerTypeName;
	}
	public String getSkillTypeName() {
		return skillTypeName;
	}
	public void setSkillTypeName(String skillTypeName) {
		this.skillTypeName = skillTypeName;
	}
	public String getSkillLevelName() {
		return skillLevelName;
	}
	public void setSkillLevelName(String skillLevelName) {
		this.skillLevelName = skillLevelName;
	}
	public String getEducationName() {
		return educationName;
	}
	public void setEducationName(String educationName) {
		this.educationName = educationName;
	}
	public String getDegreeName() {
		return degreeName;
	}
	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}
	public String getManagerUnit() {
		return managerUnit;
	}
	public void setManagerUnit(String managerUnit) {
		this.managerUnit = managerUnit;
	}
	
}
