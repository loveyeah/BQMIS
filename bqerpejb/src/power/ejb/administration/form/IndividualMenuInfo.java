/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * 个人订单信息
 * @author zhaomingjian
 *
 */
@SuppressWarnings("serial")
public class IndividualMenuInfo implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 订单号
	 */
	private String  mId;
	/**
	 * 订餐日期
	 */
	private String menuDate;
	/**
	 *  订餐类别
	 */
	private String menuType;
	/**
	 *  姓名
	 */
	private String userName;
	/**
	 * 部门名称
	 */
	private String depName;
    /**
     *  填单日期
     */
	private String insertDate;
	/**
	 *  订单状态
	 * 
	 */
	private String menuInfo;
	/**
	 * 工作类别
	 */
	private String stationName;
	
	// xsTan 追加 2009-1-28 追加更新时间
	/**
	 * 更新时间
	 */
	private String strUpdateTime;
	/**
	 * place
	 */
	private String place;
	/**
	/**
	 * 
	 * @return  订单号
	 */
	public String getMId() {
		return mId;
	}
	/**
	 * 
	 * @param id  订单号
	 */
	public void setMId(String id) {
		mId = id;
	}
	/**
	 * 
	 * @return  订餐日期
	 */
	public String getMenuDate() {
		return menuDate;
	}
	/**
	 * 
	 * @param menuDate 订餐日期
	 */
	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}
	/**
	 * 
	 * @return  订餐类别
	 */
	public String getMenuType() {
		return menuType;
	}
	/**
	 * 
	 * @param menuType 订餐类别
	 */
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	/**
	 * 
	 * @return 姓名
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 
	 * @param userName 姓名
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 
	 * @return 部门名称
	 */
	public String getDepName() {
		return depName;
	}
	/**
	 * 
	 * @param depName  部门名称
	 */
	public void setDepName(String depName) {
		this.depName = depName;
	}
	/**
	 * 
	 * @return 填单日期
	 */
	public String getInsertDate() {
		return insertDate;
	}
	/**
	 * 
	 * @param insertDate 填单日期
	 */
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
	/**
	 * 
	 * @return 订单状态
	 */
	public String getMenuInfo() {
		return menuInfo;
	}
	/**
	 * 
	 * @param menuInfo 订单状态
	 */
	public void setMenuInfo(String menuInfo) {
		this.menuInfo = menuInfo;
	}
	/**
	 * 
	 * @return 工作类别
	 */
	public String getstationName() {
		return stationName;
	}
	/**
	 * 
	 * @param workKind 工作类别
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	/**
	 * @return 更新时间
	 */
	public String getStrUpdateTime() {
		return strUpdateTime;
	}
	/**
	 * @param strUpdateTime 更新时间
	 */
	public void setStrUpdateTime(String strUpdateTime) {
		this.strUpdateTime = strUpdateTime;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	
}
