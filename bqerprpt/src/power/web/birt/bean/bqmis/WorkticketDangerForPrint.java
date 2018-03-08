/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.bean.bqmis;

import java.util.List;

/**
 * 危险点控制措施票数据
 * 
 * @author LiuYingwen
 * 
 */
public class WorkticketDangerForPrint implements java.io.Serializable {

	private List<WorkticketDanger> danger;

	/** 后增加危险点 */
	private List<WorkticketDanger> AddDangerList;

	/**
	 * 工作票号
	 */
	private String workticketNo;
	/**
	 * 工作负责人
	 */
	private String chargeBy;
	/**
	 * 危险点内容
	 */
	private String dangerContent;

	/**
	 * 签发人
	 */
	private String signBy;
	/**
	 * 签发人审批意见
	 */
	private String signText;
	/**
	 * 工作票当前状态
	 */
	private String workticketStausId;

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

	public String getDangerContent() {
		return dangerContent;
	}

	public void setDangerContent(String dangerContent) {
		this.dangerContent = dangerContent;
	}

	public String getSignBy() {
		return signBy;
	}

	public void setSignBy(String signBy) {
		this.signBy = signBy;
	}

	public String getSignText() {
		return signText;
	}

	public void setSignText(String signText) {
		this.signText = signText;
	}

	public List<WorkticketDanger> getDanger() {
		return danger;
	}

	public void setDanger(List<WorkticketDanger> danger) {
		this.danger = danger;
	}

	/**
	 * @return the addDangerList
	 */
	public List<WorkticketDanger> getAddDangerList() {
		return AddDangerList;
	}

	/**
	 * @param addDangerList
	 *            the addDangerList to set
	 */
	public void setAddDangerList(List<WorkticketDanger> addDangerList) {
		AddDangerList = addDangerList;
	}

	/**
	 * @return the workticketStausId
	 */
	public String getWorkticketStausId() {
		return workticketStausId;
	}

	/**
	 * @param workticketStausId the workticketStausId to set
	 */
	public void setWorkticketStausId(String workticketStausId) {
		this.workticketStausId = workticketStausId;
	}

}
