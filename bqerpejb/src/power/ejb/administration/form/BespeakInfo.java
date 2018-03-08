/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.form;

import java.util.List;

/**
* 订餐信息表Bean
* @author daichunlin
*/
public class BespeakInfo implements java.io.Serializable{

	/** 订餐类别 */
	private String menuDisplayType;
	/** 人员类别 */
	private String manDisplayType;
	/** 订餐日期 */
	private String menuDisplayDate;
	/** 合计 */
	private String countAll;
    /** 订餐信息表明细 */
    private List<MenuSystemInfo> leaderAuditInfoList;

	/**
	 * @return the menuDisplayType
	 */
	public String getMenuDisplayType() {
		return menuDisplayType;
	}
	/**
	 * @param menuDisplayType the menuDisplayType to set
	 */
	public void setMenuDisplayType(String menuDisplayType) {
		this.menuDisplayType = menuDisplayType;
	}
	/**
	 * @return the menuDisplayDate
	 */
	public String getMenuDisplayDate() {
		return menuDisplayDate;
	}
	/**
	 * @param menuDisplayDate the menuDisplayDate to set
	 */
	public void setMenuDisplayDate(String menuDisplayDate) {
		this.menuDisplayDate = menuDisplayDate;
	}
	/**
	 * @return the leaderAuditInfoList
	 */
	public List<MenuSystemInfo> getLeaderAuditInfoList() {
		return leaderAuditInfoList;
	}
	/**
	 * @param leaderAuditInfoList the leaderAuditInfoList to set
	 */
	public void setLeaderAuditInfoList(List<MenuSystemInfo> leaderAuditInfoList) {
		this.leaderAuditInfoList = leaderAuditInfoList;
	}
	/**
	 * @return the manDisplayType
	 */
	public String getManDisplayType() {
		return manDisplayType;
	}
	/**
	 * @param manDisplayType the manDisplayType to set
	 */
	public void setManDisplayType(String manDisplayType) {
		this.manDisplayType = manDisplayType;
	}
	/**
	 * @return the countAll
	 */
	public String getCountAll() {
		return countAll;
	}
	/**
	 * @param countAll the countAll to set
	 */
	public void setCountAll(String countAll) {
		this.countAll = countAll;
	}
}
