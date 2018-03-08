/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

import java.util.List;

/**
 * 物料需求计划登记表
 * @author zhujie 
 */
public class MaterialRequestReportBean {

	/** 制单人 */
    private String createMan="";
    /** 当前日期 */
    private String nowDate="";
    /** 申请单编号 */
    private String applicationNo="";
    /** 申请部门 */
    private String applicationDept="";
    /** 需求日期 */
    private String dueDate="";
    /** 计划来源 */
    private String planFrom="";
    /** 费用来源 */
    private String moneyFrom="";
    /** 归口部门 */
    private String belongDept="";
    /** 归口专业 */
    private String belongClass="";
    /** 采购员 */
    private List<MaterialRequestReportListBean> materialRequestReportList;
    /** 采购员 */
    private List<MaterialRequestReportDetailListBean> materialRequestReportDetailList;
    /** 合计金额 */
    private String totalMoney ="";
    /** 是否隐藏合计金额 */
    private Boolean flag =true;
	/**
	 * @return the flag
	 */
	public Boolean getFlag() {
		return flag;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	/**
	 * @return the createMan
	 */
	public String getCreateMan() {
		return createMan;
	}
	/**
	 * @param createMan the createMan to set
	 */
	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}
	/**
	 * @return the nowDate
	 */
	public String getNowDate() {
		return nowDate;
	}
	/**
	 * @param nowDate the nowDate to set
	 */
	public void setNowDate(String nowDate) {
		this.nowDate = nowDate;
	}
	/**
	 * @return the materialRequestReportList
	 */
	public List<MaterialRequestReportListBean> getMaterialRequestReportList() {
		return materialRequestReportList;
	}
	/**
	 * @param materialRequestReportList the materialRequestReportList to set
	 */
	public void setMaterialRequestReportList(
			List<MaterialRequestReportListBean> materialRequestReportList) {
		this.materialRequestReportList = materialRequestReportList;
	}
	/**
	 * @return the materialRequestReportDetailList
	 */
	public List<MaterialRequestReportDetailListBean> getMaterialRequestReportDetailList() {
		return materialRequestReportDetailList;
	}
	/**
	 * @param materialRequestReportDetailList the materialRequestReportDetailList to set
	 */
	public void setMaterialRequestReportDetailList(
			List<MaterialRequestReportDetailListBean> materialRequestReportDetailList) {
		this.materialRequestReportDetailList = materialRequestReportDetailList;
	}
	/**
	 * @return the applicationNo
	 */
	public String getApplicationNo() {
		return applicationNo;
	}
	/**
	 * @param applicationNo the applicationNo to set
	 */
	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}
	/**
	 * @return the applicationDept
	 */
	public String getApplicationDept() {
		return applicationDept;
	}
	/**
	 * @param applicationDept the applicationDept to set
	 */
	public void setApplicationDept(String applicationDept) {
		this.applicationDept = applicationDept;
	}
	/**
	 * @return the dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}
	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	/**
	 * @return the planFrom
	 */
	public String getPlanFrom() {
		return planFrom;
	}
	/**
	 * @param planFrom the planFrom to set
	 */
	public void setPlanFrom(String planFrom) {
		this.planFrom = planFrom;
	}
	/**
	 * @return the moneyFrom
	 */
	public String getMoneyFrom() {
		return moneyFrom;
	}
	/**
	 * @param moneyFrom the moneyFrom to set
	 */
	public void setMoneyFrom(String moneyFrom) {
		this.moneyFrom = moneyFrom;
	}
	/**
	 * @return the belongDept
	 */
	public String getBelongDept() {
		return belongDept;
	}
	/**
	 * @param belongDept the belongDept to set
	 */
	public void setBelongDept(String belongDept) {
		this.belongDept = belongDept;
	}
	/**
	 * @return the belongClass
	 */
	public String getBelongClass() {
		return belongClass;
	}
	/**
	 * @param belongClass the belongClass to set
	 */
	public void setBelongClass(String belongClass) {
		this.belongClass = belongClass;
	}
	/**
	 * @return the totalMoney
	 */
	public String getTotalMoney() {
		return totalMoney;
	}
	/**
	 * @param totalMoney the totalMoney to set
	 */
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

}
