/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

import java.util.List;

/**
 * 盘点损益表
 * @author zhujie 
 */
public class CheckBalanceBean {

	/** 制单人 */
    private String createMan="";
    /** 当前日期 */
    private String nowDate="";
    /** 盘点损益表数据 */
    private List<CheckBalanceListBean> checkBalanceList;
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
	 * @return the checkBalanceList
	 */
	public List<CheckBalanceListBean> getCheckBalanceList() {
		return checkBalanceList;
	}
	/**
	 * @param checkBalanceList the checkBalanceList to set
	 */
	public void setCheckBalanceList(List<CheckBalanceListBean> checkBalanceList) {
		this.checkBalanceList = checkBalanceList;
	}

}
