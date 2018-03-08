/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

import java.util.List;

/**
 * 库存再订货报表
 * @author zhujie 
 */
public class WareHouseBean {

	/** 制单人 */
    private String createMan="";
    /** 当前日期 */
    private String nowDate="";
    /** 库存再订货报表数据 */
    private List<WareHouseListBean> wareHouseList;
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
	 * @return the wareHouseList
	 */
	public List<WareHouseListBean> getWareHouseList() {
		return wareHouseList;
	}
	/**
	 * @param wareHouseList the wareHouseList to set
	 */
	public void setWareHouseList(List<WareHouseListBean> wareHouseList) {
		this.wareHouseList = wareHouseList;
	}
}
