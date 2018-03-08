/**
　* Copyright ustcsoft.com
　* All right reserved.
*/
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * 订餐系统信息
 * 
 * @author zhao
 */
public class MenuSystemInfo implements Serializable {
	/** serial id */
	private static final long serialVersionUID = 1L;
	//fields
	/**
	 * 菜谱ID
	 */
	private String mId;
	/**
	 * 菜谱类型
	 */
	private String menuType;
	/**
	 * 用餐时间
	 */
	private String menuDate;
	/**
	 * 订餐人
	 */
	private String name;
	/**
	 * 部门名
	 */
	private String deptName;
	/**
	 * 填单时间
	 */
	private String insertDate;
	/**
	 * 地址
	 */
	private String place;
	/**
	 * 菜谱名
	 */
	private String menuName;
	/**
	 * 菜谱类型名
	 */
	private String menuTypeName;
	/**
	 * 数量
	 */
	private String menuAmount;
	/**
	 * 单价
	 */
	private String menuPrice;
	/**
	 * 合计
	 */
	private String menuTotal;
	/**
	 * 备注
	 */
	private String memo;
	/** 行数计数 */
	private Integer cntRow;
	
	
	/**
	 * @return the cntRow
	 */
	public Integer getCntRow() {
		return cntRow;
	}
	/**
	 * @param cntRow the cntRow to set
	 */
	public void setCntRow(Integer cntRow) {
		this.cntRow = cntRow;
	}
	/**
	 * 
	 * @return 菜谱ID
	 */
	public String getMId() {
		return mId;
	}
	/**
	 * 
	 * @param id 菜谱ID
	 */ 
	public void setMId(String id) {
		mId = id;
	}
	/**
	 * 
	 * @return 菜谱类型
	 */
	public String getMenuType() {
		return menuType;
	}
	/**
	 * 
	 * @param menuType 菜谱类型
	 */
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	/**
	 * 
	 * @return 用餐时间
	 */
	public String getMenuDate() {
		return menuDate;
	}
	/**
	 * 
	 * @param menuDate 用餐时间
	 */
	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}
	/**
	 * 
	 * @return 订餐人
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name 订餐人
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return 部门名
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * 
	 * @param deptName 部门名
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * 
	 * @return 填单时间
	 */
	public String getInsertDate() {
		return insertDate;
	}
	/**
	 * 
	 * @param insertDate 填单时间
	 */
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
	/**
	 * 
	 * @return 地址
	 */
	public String getPlace() {
		return place;
	}
	/**
	 * 
	 * @param place 地址
	 */
	public void setPlace(String place) {
		this.place = place;
	}
	/**
	 * 
	 * @return 菜谱名
	 */
	public String getMenuName() {
		return menuName;
	}
	/**
	 * 
	 * @param menuName 菜谱名
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	/**
	 * 
	 * @return 菜谱类型名
	 */
	public String getMenuTypeName() {
		return menuTypeName;
	}
	/**
	 * 
	 * @param menuTypeName 菜谱类型名
	 */
	public void setMenuTypeName(String menuTypeName) {
		this.menuTypeName = menuTypeName;
	}
	/**
	 * 
	 * @return  数量
	 */
	public String getMenuAmount() {
		return menuAmount;
	}
	/**
	 * 
	 * @param menuAmount  数量
	 */
	public void setMenuAmount(String menuAmount) {
		this.menuAmount = menuAmount;
	}
	/**
	 * 
	 * @return 单价
	 */
	public String getMenuPrice() {
		return menuPrice;
	}
	/**
	 * 
	 * @param menuPrice 单价
	 */
	public void setMenuPrice(String menuPrice) {
		this.menuPrice = menuPrice;
	}
	/**
	 * 
	 * @return 合计
	 */
	public String getMenuTotal() {
		return menuTotal;
	}
	/**
	 * 
	 * @param menuTotal 合计
	 */
	public void setMenuTotal(String menuTotal) {
		this.menuTotal = menuTotal;
	}
	/**
	 * 
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * 
	 * @param memo 备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
}
