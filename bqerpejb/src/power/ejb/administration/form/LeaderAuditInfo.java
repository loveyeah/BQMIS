/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.form;

/**
 * 值长审核bean
 * @author zhengzhipeng
 * 
 */
public class LeaderAuditInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	// Fields
	/** 行号 */
	private int index;
	/** 订单号 */
	private String menuId;
	/** 隐藏的订单号 */
	private String menuHidId;
	/** 订餐类别 */
	private String menuType;
	/** 订餐日期 */
	private String menuDate;
	/** 订餐人 */
	private String workerName;
	/** 所属部门 */
	private String depName;
	/** 填单日期 */
	private String insertDate;
	/** 就餐地点 */
	private String place;
	/** 菜谱名称 */
	private String menuName;
	/** 菜谱类别 */
	private String menuTypeName;
	/** 份数 */
	private String menuAmount;
	/** 单价 */
	private String menuPrice;
	/** 备注 */
	private String memo;
	/** 行数计数 */
	private Integer cntRow;
	/** 上次修改时间 */
	private String updateTime;
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return 订餐类别
	 */
	public String getMenuType() {
		return menuType;
	}
	/**
	 * @param 订餐类别
	 */
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	/**
	 * @return 订餐人
	 */
	public String getWorkerName() {
		return workerName;
	}
	/**
	 * @param 订餐人
	 */
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	/**
	 * @return 所属部门
	 */
	public String getDepName() {
		return depName;
	}
	/**
	 * @param 所属部门
	 */
	public void setDepName(String depName) {
		this.depName = depName;
	}
	/**
	 * @return 填单日期
	 */
	public String getInsertDate() {
		return insertDate;
	}
	/**
	 * @param 填单日期
	 */
	public void setInsertDate(String insertDate) {
		this.insertDate = insertDate;
	}
	/**
	 * @return 菜谱名称
	 */
	public String getMenuName() {
		return menuName;
	}
	/**
	 * @param 菜谱名称
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	/**
	 * @return 菜谱类别
	 */
	public String getMenuTypeName() {
		return menuTypeName;
	}
	/**
	 * @param 菜谱类别
	 */
	public void setMenuTypeName(String menuTypeName) {
		this.menuTypeName = menuTypeName;
	}
	/**
	 * @return 份数
	 */
	public String getMenuAmount() {
		return menuAmount;
	}
	/**
	 * @param 份数
	 */
	public void setMenuAmount(String menuAmount) {
		this.menuAmount = menuAmount;
	}
	/**
	 * @return 单价
	 */
	public String getMenuPrice() {
		return menuPrice;
	}
	/**
	 * @param 单价
	 */
	public void setMenuPrice(String menuPrice) {
		this.menuPrice = menuPrice;
	}
	/**
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param 备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * @return the menuDate
	 */
	public String getMenuDate() {
		return menuDate;
	}
	/**
	 * @param menuDate the menuDate to set
	 */
	public void setMenuDate(String menuDate) {
		this.menuDate = menuDate;
	}
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
	 * @return 就餐地点
	 */
	public String getPlace() {
		return place;
	}
	/**
	 * @param 就餐地点
	 */
	public void setPlace(String place) {
		this.place = place;
	}
	/**
	 * @return 订单号
	 */
	public String getMenuId() {
		return menuId;
	}
	/**
	 * @param 订单号
	 */
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	/**
	 * @return the menuHidId
	 */
	public String getMenuHidId() {
		return menuHidId;
	}
	/**
	 * @param menuHidId the menuHidId to set
	 */
	public void setMenuHidId(String menuHidId) {
		this.menuHidId = menuHidId;
	}
	/**
	 * @return 上次修改时间
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param 上次修改时间
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
