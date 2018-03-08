package power.ejb.workticket.form;

@SuppressWarnings("serial")
public class WorkticketBaseForPrint implements java.io.Serializable{
	
	//----基本信息-------
	/**
	 * 工作票号
	 */
	 private String workticketNo;
	 /**
      * 工作负责人
      */
     private String chargeBy;
     
     /**
      * 负责人所在部门
      */
     private String chargeDept;
     /**
      * 工作班成员数
      */
     private String memberCount;
     /**
      * 工作班成员
      */
     private String members;
     
     /**
      * 工作票来源
      */
     private String sourceName;
     
     /**
      * for elec2
      * 工作条件
      */
     private String workCondition;
     
     /**
      * 动火票级别
      */
     private String fireLevel;
     
     /**
      * 工作地点
      */
     private String locationName;
     
     /**
      * 工作内容
      * 
      */
     private String workticketContent;
     
     /**
      * 检修专业
      */
     private String repairSpecail;
     
     /**
      * 所属机组
      */
     private String equAttributeName;
     
     /**
      * 监工
      */
     private String watcher;
     
	 /**
	  * 是否紧急票
	  */
     private String isEmergency;
   
     /**
      * 计划开始时间
      */
     private String planStartDate;
     /**
      * 计划结束时间
      */
     private String planEndDate;
 
     /**
      * 填写人
      */
     private String entryBy;
   
     /**
      * 填写时间：
      */
     private String entryDate;
  
     /**
      * 实际开始时间
      */
     private String actualStartDate;
     /**
      * 实际结束时间
      */
     private String actualEndDate;
     
     /**
      * 批准工作结束时间：
      */
     private String approvedFinishDate;
    
     /**
      * 对应主票号
      */
     private String refMainticketNo;
     
     /**
      * 动火执行人
      */
      private String fireExeMan;
      
      /**
       * 动火票消防现场监护人
       */
      private String fireWatcherMan;
      
      
      /**
       * 备注：
       */
      private String workticketMemo;
      
      /**
       * 危险点类别
       */
      private String dangerType;
      
      /**
       * 危险点内容
       */
      private String dangerContent;
      
      /**
       * 工作票类型
       */
      private String workticketType;
      
      /**
       * 电一票附图
       */
      private byte[] workticketMap;
      
      /**
       * 挂排总数
       */
      private String markCardNum;
      
      /**
       * 需要退出热工保护或自动装置名称  add 090217
       */
      private String autoDeviceName;
      
      private String workticketTypeCode; //add by fyyang 090312

	public String getWorkticketNo() {
		return workticketNo;
	}

	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	public String getChargeBy() {
		return chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	public String getChargeDept() {
		return chargeDept;
	}

	public void setChargeDept(String chargeDept) {
		this.chargeDept = chargeDept;
	}

	public String getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(String memberCount) {
		this.memberCount = memberCount;
	}

	public String getMembers() {
		return members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getWorkCondition() {
		return workCondition;
	}

	public void setWorkCondition(String workCondition) {
		this.workCondition = workCondition;
	}

	public String getFireLevel() {
		return fireLevel;
	}

	public void setFireLevel(String fireLevel) {
		this.fireLevel = fireLevel;
	}

	public String getWorkticketContent() {
		return workticketContent;
	}

	public void setWorkticketContent(String workticketContent) {
		this.workticketContent = workticketContent;
	}

	public String getRepairSpecail() {
		return repairSpecail;
	}

	public void setRepairSpecail(String repairSpecail) {
		this.repairSpecail = repairSpecail;
	}

	public String getEquAttributeName() {
		return equAttributeName;
	}

	public void setEquAttributeName(String equAttributeName) {
		this.equAttributeName = equAttributeName;
	}

	public String getWatcher() {
		return watcher;
	}

	public void setWatcher(String watcher) {
		this.watcher = watcher;
	}

	public String getIsEmergency() {
		return isEmergency;
	}

	public void setIsEmergency(String isEmergency) {
		this.isEmergency = isEmergency;
	}

	public String getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}

	public String getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}

	public String getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public String getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public String getApprovedFinishDate() {
		return approvedFinishDate;
	}

	public void setApprovedFinishDate(String approvedFinishDate) {
		this.approvedFinishDate = approvedFinishDate;
	}

	public String getRefMainticketNo() {
		return refMainticketNo;
	}

	public void setRefMainticketNo(String refMainticketNo) {
		this.refMainticketNo = refMainticketNo;
	}

	public String getFireExeMan() {
		return fireExeMan;
	}

	public void setFireExeMan(String fireExeMan) {
		this.fireExeMan = fireExeMan;
	}

	public String getFireWatcherMan() {
		return fireWatcherMan;
	}

	public void setFireWatcherMan(String fireWatcherMan) {
		this.fireWatcherMan = fireWatcherMan;
	}

	public String getWorkticketMemo() {
		return workticketMemo;
	}

	public void setWorkticketMemo(String workticketMemo) {
		this.workticketMemo = workticketMemo;
	}

	public String getDangerType() {
		return dangerType;
	}

	public void setDangerType(String dangerType) {
		this.dangerType = dangerType;
	}

	public String getDangerContent() {
		return dangerContent;
	}

	public void setDangerContent(String dangerContent) {
		this.dangerContent = dangerContent;
	}

	public byte[] getWorkticketMap() {
		return workticketMap;
	}

	public void setWorkticketMap(byte[] workticketMap) {
		this.workticketMap = workticketMap;
	}

	public String getWorkticketType() {
		return workticketType;
	}

	public void setWorkticketType(String workticketType) {
		this.workticketType = workticketType;
	}

	public String getMarkCardNum() {
		return markCardNum;
	}

	public void setMarkCardNum(String markCardNum) {
		this.markCardNum = markCardNum;
	}

	/**
	 * @return the locationName
	 */
	public String getLocationName() {
		return locationName;
	}

	/**
	 * @param locationName the locationName to set
	 */
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getAutoDeviceName() {
		return autoDeviceName;
	}

	public void setAutoDeviceName(String autoDeviceName) {
		this.autoDeviceName = autoDeviceName;
	}

	public String getWorkticketTypeCode() {
		return workticketTypeCode;
	}

	public void setWorkticketTypeCode(String workticketTypeCode) {
		this.workticketTypeCode = workticketTypeCode;
	}

     
 
   

}
