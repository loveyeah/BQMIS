package power.ejb.manage.plan.form;

import java.util.Date;


@SuppressWarnings("serial")
public class repairDetailForm implements java.io.Serializable {
	private Long planDetailId;
	private Long planDepId;
	private String content;
	private String chargeDep;
	private String chargeDepName;
	private String assortDep;
	private String assortDepName;
	private Date beginTime;
	private Date endTime;
	private Long days;
	private String memo;
	private String isUse;
	private String enterpriseCode;
    private String  weekStartTime;
    private String  weekEndTime;
	private Long DepId;
	private String  planTime;
	private String editDep;
	private String editDepName;
	private String editBy;
	private String editByName;
	private Date editDate;
	private Long workFlowNo;
	private Long status;
	public Long getPlanDetailId() {
		return planDetailId;
	}
	public void setPlanDetailId(Long planDetailId) {
		this.planDetailId = planDetailId;
	}
	public Long getPlanDepId() {
		return planDepId;
	}
	public void setPlanDepId(Long planDepId) {
		this.planDepId = planDepId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getChargeDep() {
		return chargeDep;
	}
	public void setChargeDep(String chargeDep) {
		this.chargeDep = chargeDep;
	}
	public String getAssortDep() {
		return assortDep;
	}
	public void setAssortDep(String assortDep) {
		this.assortDep = assortDep;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Long getDays() {
		return days;
	}
	public void setDays(Long days) {
		this.days = days;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public Long getDepId() {
		return DepId;
	}
	public void setDepId(Long depId) {
		DepId = depId;
	}
	
	public String getEditDep() {
		return editDep;
	}
	public void setEditDep(String editDep) {
		this.editDep = editDep;
	}
	public String getEditBy() {
		return editBy;
	}
	public void setEditBy(String editBy) {
		this.editBy = editBy;
	}
	public Date getEditDate() {
		return editDate;
	}
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}
	public Long getWorkFlowNo() {
		return workFlowNo;
	}
	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getChargeDepName() {
		return chargeDepName;
	}
	public void setChargeDepName(String chargeDepName) {
		this.chargeDepName = chargeDepName;
	}
	public String getAssortDepName() {
		return assortDepName;
	}
	public void setAssortDepName(String assortDepName) {
		this.assortDepName = assortDepName;
	}
	public String getEditDepName() {
		return editDepName;
	}
	public void setEditDepName(String editDepName) {
		this.editDepName = editDepName;
	}
	public String getEditByName() {
		return editByName;
	}
	public void setEditByName(String editByName) {
		this.editByName = editByName;
	}
	public String getWeekStartTime() {
		return weekStartTime;
	}
	public void setWeekStartTime(String weekStartTime) {
		this.weekStartTime = weekStartTime;
	}
	public String getWeekEndTime() {
		return weekEndTime;
	}
	public void setWeekEndTime(String weekEndTime) {
		this.weekEndTime = weekEndTime;
	}
	public String getPlanTime() {
		return planTime;
	}
	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}
	

	
}