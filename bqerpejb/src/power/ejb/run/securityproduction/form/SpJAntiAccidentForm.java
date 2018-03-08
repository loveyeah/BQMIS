package power.ejb.run.securityproduction.form;

import java.util.Date;

@SuppressWarnings("serial")
public class SpJAntiAccidentForm implements java.io.Serializable{
	
	private String pMeasureName;
	//措施编号
	private String measureCode;
	// 措施名称
	private String measureName;
	//专业编码
	private String specialCode;
	// 责任领导（发电企业）
	private String fdDutyLeader;
	// 管理责任人（发电企业）
	private String fdManager;
	// 技术责任人（发电企业）
	private String fdTechnologyBy;
	// 监督责任人（发电企业）
	private String fdSuperviseBy;
	// 责任领导（大唐陕西）
	private String dtDutyLeader;
	// 管理责任人（大唐陕西）
	private String dtManager;
	// 技术责任人（大唐陕西）
	private String dtTechnologyBy;
	// 监督责任人（大唐陕西）
	private String dtSuperviseBy;
	// 备注
	private String memo;
	private String entryBy;
	private String entryDept;
	private Date entryDate;
	
	
	//专业编码 名
	private String specialName;
	// 责任领导（发电企业） 名
	private String fdDutyLeaderName;
	// 管理责任人（发电企业） 名
	private String fdManagerName;
	// 技术责任人（发电企业） 名
	private String fdTechnologyName;
	// 监督责任人（发电企业） 名
	private String fdSuperviseName;
	// 责任领导（大唐陕西） 名
	private String dtDutyLeaderName;
	// 管理责任人（大唐陕西） 名
	private String dtManagerName;
	// 技术责任人（大唐陕西） 名
	private String dtTechnologyName;
	// 监督责任人（大唐陕西） 名
	private String dtSuperviseName;
	// 键入人名
	private String entryName;
	// 键入部门名
	private String entryDeptName;
	// 时间字符串
	private String entryDateString;
	
	public SpJAntiAccidentForm()
	{
		this.specialName = "";
		this.fdDutyLeaderName = "";
		this.fdManagerName = "";
		this.fdTechnologyName = "";
		this.fdSuperviseName = "";
		this.dtDutyLeaderName = "";
		this.dtManagerName = "";
		this.dtTechnologyName = "";
		this.dtSuperviseName = "";
	}
	public String getMeasureCode() {
		return measureCode;
	}
	public void setMeasureCode(String measureCode) {
		this.measureCode = measureCode;
	}
	public String getMeasureName() {
		return measureName;
	}
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}
	public String getSpecialCode() {
		return specialCode;
	}
	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}
	public String getFdDutyLeader() {
		return fdDutyLeader;
	}
	public void setFdDutyLeader(String fdDutyLeader) {
		this.fdDutyLeader = fdDutyLeader;
	}
	public String getFdManager() {
		return fdManager;
	}
	public void setFdManager(String fdManager) {
		this.fdManager = fdManager;
	}
	public String getFdTechnologyBy() {
		return fdTechnologyBy;
	}
	public void setFdTechnologyBy(String fdTechnologyBy) {
		this.fdTechnologyBy = fdTechnologyBy;
	}
	public String getFdSuperviseBy() {
		return fdSuperviseBy;
	}
	public void setFdSuperviseBy(String fdSuperviseBy) {
		this.fdSuperviseBy = fdSuperviseBy;
	}
	public String getDtDutyLeader() {
		return dtDutyLeader;
	}
	public void setDtDutyLeader(String dtDutyLeader) {
		this.dtDutyLeader = dtDutyLeader;
	}
	public String getDtManager() {
		return dtManager;
	}
	public void setDtManager(String dtManager) {
		this.dtManager = dtManager;
	}
	public String getDtTechnologyBy() {
		return dtTechnologyBy;
	}
	public void setDtTechnologyBy(String dtTechnologyBy) {
		this.dtTechnologyBy = dtTechnologyBy;
	}
	public String getDtSuperviseBy() {
		return dtSuperviseBy;
	}
	public void setDtSuperviseBy(String dtSuperviseBy) {
		this.dtSuperviseBy = dtSuperviseBy;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getSpecialName() {
		return specialName;
	}
	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}
	public String getFdDutyLeaderName() {
		return fdDutyLeaderName;
	}
	public void setFdDutyLeaderName(String fdDutyLeaderName) {
		this.fdDutyLeaderName = fdDutyLeaderName;
	}
	public String getFdManagerName() {
		return fdManagerName;
	}
	public void setFdManagerName(String fdManagerName) {
		this.fdManagerName = fdManagerName;
	}
	public String getFdTechnologyName() {
		return fdTechnologyName;
	}
	public void setFdTechnologyName(String fdTechnologyName) {
		this.fdTechnologyName = fdTechnologyName;
	}
	public String getFdSuperviseName() {
		return fdSuperviseName;
	}
	public void setFdSuperviseName(String fdSuperviseName) {
		this.fdSuperviseName = fdSuperviseName;
	}
	public String getDtDutyLeaderName() {
		return dtDutyLeaderName;
	}
	public void setDtDutyLeaderName(String dtDutyLeaderName) {
		this.dtDutyLeaderName = dtDutyLeaderName;
	}
	public String getDtManagerName() {
		return dtManagerName;
	}
	public void setDtManagerName(String dtManagerName) {
		this.dtManagerName = dtManagerName;
	}
	public String getDtTechnologyName() {
		return dtTechnologyName;
	}
	public void setDtTechnologyName(String dtTechnologyName) {
		this.dtTechnologyName = dtTechnologyName;
	}
	public String getDtSuperviseName() {
		return dtSuperviseName;
	}
	public void setDtSuperviseName(String dtSuperviseName) {
		this.dtSuperviseName = dtSuperviseName;
	}
	public String getEntryBy() {
		return entryBy;
	}
	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}
	public String getEntryDept() {
		return entryDept;
	}
	public void setEntryDept(String entryDept) {
		this.entryDept = entryDept;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public String getEntryName() {
		return entryName;
	}
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}
	public String getEntryDeptName() {
		return entryDeptName;
	}
	public void setEntryDeptName(String entryDeptName) {
		this.entryDeptName = entryDeptName;
	}
	public String getEntryDateString() {
		return entryDateString;
	}
	public void setEntryDateString(String entryDateString) {
		this.entryDateString = entryDateString;
	}
	public String getPMeasureName() {
		return pMeasureName;
	}
	public void setPMeasureName(String measureName) {
		pMeasureName = measureName;
	}
	
}
