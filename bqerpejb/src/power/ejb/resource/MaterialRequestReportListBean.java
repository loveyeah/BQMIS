/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

/**
 * 物料需求计划登记明细List
 * @author zhujie 
 */
public class MaterialRequestReportListBean {

	/** ID */
    private String Id="";
    /** 状态 */
    private String status="";
    /** 申请编号 */
    private String applicationNo="";
    /** 申请单位 */
    private String applicationClass="";
    /** 申请人 */
    private String applicationPerson="";
    /** 申请原因 */
    private String applicationReason="";
    /** 申请日期 */
    private String applicationDate="";
    /** 物料类型 */
    private String materialType="";
    /** 费用来源 */
    private String moneyFrom="";
	/**
	 * @return the id
	 */
	public String getId() {
		return Id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		Id = id;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the applicationClass
	 */
	public String getApplicationClass() {
		return applicationClass;
	}
	/**
	 * @param applicationClass the applicationClass to set
	 */
	public void setApplicationClass(String applicationClass) {
		this.applicationClass = applicationClass;
	}
	/**
	 * @return the applicationPerson
	 */
	public String getApplicationPerson() {
		return applicationPerson;
	}
	/**
	 * @param applicationPerson the applicationPerson to set
	 */
	public void setApplicationPerson(String applicationPerson) {
		this.applicationPerson = applicationPerson;
	}
	/**
	 * @return the applicationReason
	 */
	public String getApplicationReason() {
		return applicationReason;
	}
	/**
	 * @param applicationReason the applicationReason to set
	 */
	public void setApplicationReason(String applicationReason) {
		this.applicationReason = applicationReason;
	}
	/**
	 * @return the applicationDate
	 */
	public String getApplicationDate() {
		return applicationDate;
	}
	/**
	 * @param applicationDate the applicationDate to set
	 */
	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}
	/**
	 * @return the materialType
	 */
	public String getMaterialType() {
		return materialType;
	}
	/**
	 * @param materialType the materialType to set
	 */
	public void setMaterialType(String materialType) {
		this.materialType = materialType;
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
}
