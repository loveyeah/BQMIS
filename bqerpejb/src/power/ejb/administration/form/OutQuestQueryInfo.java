/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;


/**
 * 签报申请查询Bean
 * 签报信息查阅Bean
 * 
 * @author jincong
 * @version 1.0
 */
public class OutQuestQueryInfo implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	// 签报申请
	/** 申请单号 */
	private String applyId;
	/** 申请人 */
	private String applyMan;
	/** 呈报日期 */
	private String applyDate;
	/** 呈报主题 */
	private String applyTopic;
	/** 呈报内容 */
	private String applyText;
	/** 核稿人 */
	private String checkedMan;
	/** 签字状态 */
	private String dcmStatus;
	/** 签报种类 */
	private String appType; 
	/** 编号 */
	private String reportId;
	/** 部门经理姓名 */
	private String depBossCode = "";
	/** 部门经理意见 */
	private String depIdea;
	/** 部门经理签字时间 */
	private String depSignDate;
	/** 行政部经理姓名 */
	private String xzBossCode = "";
	/** 行政部经理意见 */
	private String xzIdea;
	/** 行政部经理签字时间 */
	private String xzSignDate;
	/** 总经理姓名 */
	private String bigBossCode ="";
	/** 总经理意见 */
	private String bigIdea;
	/** 总经理签字时间 */
	private String bigSignDate;
	/** 修改时间 */
	private String updateTime;
	/** 部门经理姓名 */
	private String depBossName;
	/** 行政部经理姓名 */
	private String xzBossName;
	/** 总经理姓名 */
	private String bigBossName;
	/** 部门经理Flag */
	private boolean depBossFlag = true;
	/** 行政部经理Flag */
	private boolean xzBossFlag = true;
	/** 总经理Flag */
	private boolean bigBossFlag = true;
	
	// 部门编码表
	/** 部门名称 */
	private String depName;
	
	// 人员编码表
	/** 姓名 */
	private String name;
	
	/** 附件条数 */
	private Long fileCount;
	
	// 签报申请附件
	/** 附件ID */
	private Long id;
	/** 附件名称 */
	private String fileName;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}


	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileCount
	 */
	public Long getFileCount() {
		return fileCount;
	}

	/**
	 * @param fileCount the fileCount to set
	 */
	public void setFileCount(Long fileCount) {
		this.fileCount = fileCount;
	}

	/**
	 * @return the applyId
	 */
	public String getApplyId() {
		return applyId;
	}

	/**
	 * @param applyId the applyId to set
	 */
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	/**
	 * @return the applyMan
	 */
	public String getApplyMan() {
		return applyMan;
	}

	/**
	 * @param applyMan the applyMan to set
	 */
	public void setApplyMan(String applyMan) {
		this.applyMan = applyMan;
	}

	/**
	 * @return the applyDate
	 */
	public String getApplyDate() {
		return applyDate;
	}

	/**
	 * @param applyDate the applyDate to set
	 */
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	/**
	 * @return the applyTopic
	 */
	public String getApplyTopic() {
		return applyTopic;
	}

	/**
	 * @param applyTopic the applyTopic to set
	 */
	public void setApplyTopic(String applyTopic) {
		this.applyTopic = applyTopic;
	}

	/**
	 * @return the applyText
	 */
	public String getApplyText() {
		return applyText;
	}

	/**
	 * @param applyText the applyText to set
	 */
	public void setApplyText(String applyText) {
		this.applyText = applyText;
	}

	/**
	 * @return the checkedMan
	 */
	public String getCheckedMan() {
		return checkedMan;
	}

	/**
	 * @param checkedMan the checkedMan to set
	 */
	public void setCheckedMan(String checkedMan) {
		this.checkedMan = checkedMan;
	}

	/**
	 * @return the dcmStatus
	 */
	public String getDcmStatus() {
		return dcmStatus;
	}

	/**
	 * @param dcmStatus the dcmStatus to set
	 */
	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}

	/**
	 * @return the reportId
	 */
	public String getReportId() {
		return reportId;
	}

	/**
	 * @param reportId the reportId to set
	 */
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	/**
	 * @return the depBossCode
	 */
	public String getDepBossCode() {
		return depBossCode;
	}

	/**
	 * @param depBossCode the depBossCode to set
	 */
	public void setDepBossCode(String depBossCode) {
		this.depBossCode = depBossCode;
	}

	/**
	 * @return the depIdea
	 */
	public String getDepIdea() {
		return depIdea;
	}

	/**
	 * @param depIdea the depIdea to set
	 */
	public void setDepIdea(String depIdea) {
		this.depIdea = depIdea;
	}

	/**
	 * @return the depSignDate
	 */
	public String getDepSignDate() {
		return depSignDate;
	}

	/**
	 * @param depSignDate the depSignDate to set
	 */
	public void setDepSignDate(String depSignDate) {
		this.depSignDate = depSignDate;
	}

	/**
	 * @return the xzBossCode
	 */
	public String getXzBossCode() {
		return xzBossCode;
	}

	/**
	 * @param xzBossCode the xzBossCode to set
	 */
	public void setXzBossCode(String xzBossCode) {
		this.xzBossCode = xzBossCode;
	}

	/**
	 * @return the xzIdea
	 */
	public String getXzIdea() {
		return xzIdea;
	}

	/**
	 * @param xzIdea the xzIdea to set
	 */
	public void setXzIdea(String xzIdea) {
		this.xzIdea = xzIdea;
	}

	/**
	 * @return the xzSignDate
	 */
	public String getXzSignDate() {
		return xzSignDate;
	}

	/**
	 * @param xzSignDate the xzSignDate to set
	 */
	public void setXzSignDate(String xzSignDate) {
		this.xzSignDate = xzSignDate;
	}

	/**
	 * @return the bigBossCode
	 */
	public String getBigBossCode() {
		return bigBossCode;
	}

	/**
	 * @param bigBossCode the bigBossCode to set
	 */
	public void setBigBossCode(String bigBossCode) {
		this.bigBossCode = bigBossCode;
	}

	/**
	 * @return the bigIdea
	 */
	public String getBigIdea() {
		return bigIdea;
	}

	/**
	 * @param bigIdea the bigIdea to set
	 */
	public void setBigIdea(String bigIdea) {
		this.bigIdea = bigIdea;
	}

	/**
	 * @return the bigSignDate
	 */
	public String getBigSignDate() {
		return bigSignDate;
	}

	/**
	 * @param bigSignDate the bigSignDate to set
	 */
	public void setBigSignDate(String bigSignDate) {
		this.bigSignDate = bigSignDate;
	}

	/**
	 * @return the depName
	 */
	public String getDepName() {
		return depName;
	}

	/**
	 * @param depName the depName to set
	 */
	public void setDepName(String depName) {
		this.depName = depName;
	}

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
	 * @return the appType
	 */
	public String getAppType() {
		return appType;
	}

	/**
	 * @param appType the appType to set
	 */
	public void setAppType(String appType) {
		this.appType = appType;
	}

	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the depBossName
	 */
	public String getDepBossName() {
		return depBossName;
	}

	/**
	 * @param depBossName the depBossName to set
	 */
	public void setDepBossName(String depBossName) {
		this.depBossName = depBossName;
	}

	/**
	 * @return the xzBossName
	 */
	public String getXzBossName() {
		return xzBossName;
	}

	/**
	 * @param xzBossName the xzBossName to set
	 */
	public void setXzBossName(String xzBossName) {
		this.xzBossName = xzBossName;
	}

	/**
	 * @return the bigBossName
	 */
	public String getBigBossName() {
		return bigBossName;
	}

	/**
	 * @param bigBossName the bigBossName to set
	 */
	public void setBigBossName(String bigBossName) {
		this.bigBossName = bigBossName;
	}



	/**
	 * @return the depBossFlag
	 */
	public boolean isDepBossFlag() {
		return depBossFlag;
	}

	/**
	 * @param depBossFlag the depBossFlag to set
	 */
	public void setDepBossFlag(boolean depBossFlag) {
		this.depBossFlag = depBossFlag;
	}

	/**
	 * @return the xzBossFlag
	 */
	public boolean isXzBossFlag() {
		return xzBossFlag;
	}

	/**
	 * @param xzBossFlag the xzBossFlag to set
	 */
	public void setXzBossFlag(boolean xzBossFlag) {
		this.xzBossFlag = xzBossFlag;
	}

	/**
	 * @return the bigBossFlag
	 */
	public boolean isBigBossFlag() {
		return bigBossFlag;
	}

	/**
	 * @param bigBossFlag the bigBossFlag to set
	 */
	public void setBigBossFlag(boolean bigBossFlag) {
		this.bigBossFlag = bigBossFlag;
	}

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
