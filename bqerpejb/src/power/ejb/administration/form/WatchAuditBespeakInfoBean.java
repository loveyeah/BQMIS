/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.form;

import java.util.List;

/**
* 值长审核订餐信息表Bean
* @author zhaozhijie
*
*/
public class WatchAuditBespeakInfoBean {

	/** 订餐类别 */
	private String menuDisplayType;
	/** 订餐日期 */
	private String menuDisplayDate;
    /** 值长审核订餐信息表明细 */
    private List<LeaderAuditInfo> leaderAuditInfoList;

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
	public List<LeaderAuditInfo> getLeaderAuditInfoList() {
		return leaderAuditInfoList;
	}
	/**
	 * @param leaderAuditInfoList the leaderAuditInfoList to set
	 */
	public void setLeaderAuditInfoList(List<LeaderAuditInfo> leaderAuditInfoList) {
		this.leaderAuditInfoList = leaderAuditInfoList;
	}
}
