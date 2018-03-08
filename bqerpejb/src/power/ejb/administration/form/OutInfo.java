/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * 物资出门管理
 * 
 * @author sufeiyu
*/
@SuppressWarnings("serial")
public class OutInfo implements Serializable{
	 /** 物资出门管理**/
	
	private Long id;
	private String agent;
	private String firm;
	private String outDate;
	private String wpName;
	private String standard;
	private String unit;
	private Long num;
	private String note;
	private String reason;
	private String onduty;
	private String dcmStatus;
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
	 * @return the agent
	 */
	public String getAgent() {
		return agent;
	}
	/**
	 * @param agent the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}
	/**
	 * @return the firm
	 */
	public String getFirm() {
		return firm;
	}
	/**
	 * @param firm the firm to set
	 */
	public void setFirm(String firm) {
		this.firm = firm;
	}
	/**
	 * @return the outDate
	 */
	public String getOutDate() {
		return outDate;
	}
	/**
	 * @param outDate the outDate to set
	 */
	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}
	/**
	 * @return the wpName
	 */
	public String getWpName() {
		return wpName;
	}
	/**
	 * @param wpName the wpName to set
	 */
	public void setWpName(String wpName) {
		this.wpName = wpName;
	}
	/**
	 * @return the standard
	 */
	public String getStandard() {
		return standard;
	}
	/**
	 * @param standard the standard to set
	 */
	public void setStandard(String standard) {
		this.standard = standard;
	}
	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * @return the num
	 */
	public Long getNum() {
		return num;
	}
	/**
	 * @param num the num to set
	 */
	public void setNum(Long num) {
		this.num = num;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the onduty
	 */
	public String getOnduty() {
		return onduty;
	}
	/**
	 * @param onduty the onduty to set
	 */
	public void setOnduty(String onduty) {
		this.onduty = onduty;
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
}
