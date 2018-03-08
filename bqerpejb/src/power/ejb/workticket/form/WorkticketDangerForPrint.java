package power.ejb.workticket.form;

import java.util.List;

public class WorkticketDangerForPrint implements java.io.Serializable {

	private List<WorkticketDanger> danger;

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
	private String workticketStausId;// modify BY ywliu 09/04/30
										// workticketStausId

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
	 * @return the workticketStausId
	 */
	public String getWorkticketStausId() {
		return workticketStausId;
	}

	/**
	 * @param workticketStausId
	 *            the workticketStausId to set
	 */
	public void setWorkticketStausId(String workticketStausId) {
		this.workticketStausId = workticketStausId;
	}

}
