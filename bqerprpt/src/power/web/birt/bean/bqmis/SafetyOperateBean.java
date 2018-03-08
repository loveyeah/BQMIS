package power.web.birt.bean.bqmis;

import java.util.List;
/**
 * 安措执行（拆除)卡数据
 * @author wangjunjie 
 */
public class SafetyOperateBean {
	
	/* 工作票种类名称 */
    private String workticketName="";
    /* 检修班组 */
    private String chargeDept="";
    /* 机组 */
    private String equAttributeName="";
    /* 检修单位 */
    private String repairSpecail="";
    /* 工作内容和工作地点 */
    private String workticketContent;
    /* 工作负责人 */
    private String chargeBy;
    /* 工作票号 */
    private String workticketNo;
    /* 安措列表 */
    private List<SafetyContentBean> safetyContent;
	/**
	 * 工作票种类名称
	 * @return the workticketName
	 */
	public String getWorkticketName() {
		return workticketName;
	}
	/**
	 * 工作票种类名称
	 * @param workticketName the workticketName to set
	 */
	public void setWorkticketName(String workticketName) {
		this.workticketName = workticketName;
	}
	/**
	 * 检修班组
	 * @return the chargeDept
	 */
	public String getChargeDept() {
		return chargeDept;
	}
	/**
	 * 检修班组
	 * @param chargeDept the chargeDept to set
	 */
	public void setChargeDept(String chargeDept) {
		this.chargeDept = chargeDept;
	}
	/**
	 * 机组
	 * @return the equAttributeName
	 */
	public String getEquAttributeName() {
		return equAttributeName;
	}
	/**
	 * 机组
	 * @param equAttributeName the equAttributeName to set
	 */
	public void setEquAttributeName(String equAttributeName) {
		this.equAttributeName = equAttributeName;
	}
	/**
	 * 检修单位
	 * @return the repairSpecail
	 */
	public String getRepairSpecail() {
		return repairSpecail;
	}
	/**
	 * 检修单位
	 * @param repairSpecail the repairSpecail to set
	 */
	public void setRepairSpecail(String repairSpecail) {
		this.repairSpecail = repairSpecail;
	}
	/**
	 * 工作内容和工作地点
	 * @return the workticketContent
	 */
	public String getWorkticketContent() {
		return workticketContent;
	}
	/**
	 * 工作内容和工作地点
	 * @param workticketContent the workticketContent to set
	 */
	public void setWorkticketContent(String workticketContent) {
		this.workticketContent = workticketContent;
	}
	/**
	 * 工作负责人
	 * @return the chargeBy
	 */
	public String getChargeBy() {
		return chargeBy;
	}
	/**
	 * 工作负责人
	 * @param chargeBy the chargeBy to set
	 */
	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}
	/**
	 * 工作票号
	 * @return the workticketNo
	 */
	public String getWorkticketNo() {
		return workticketNo;
	}
	/**
	 * 工作票号
	 * @param workticketNo the workticketNo to set
	 */
	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}
	/**
	 * 安措列表
	 * @return the safetyContent
	 */
	public List<SafetyContentBean> getSafetyContent() {
		return safetyContent;
	}
	/**
	 * 安措列表
	 * @param safetyContent the safetyContent to set
	 */
	public void setSafetyContent(List<SafetyContentBean> safetyContent) {
		this.safetyContent = safetyContent;
	}
    
	
}