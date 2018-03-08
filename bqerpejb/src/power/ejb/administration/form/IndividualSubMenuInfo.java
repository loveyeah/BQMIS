/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * 个人订餐子画面信息
 * @author zhaomingjian
 *
 */
@SuppressWarnings("serial")
public class IndividualSubMenuInfo implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户ID
	 */
	private  String id;
	/**
	 * 菜谱编码
	 */
	private String menuCode;
	/**
	 * 菜谱名称
	 */
	private String menuName;
	/*
	 * 菜谱类别名称
	 */
	private String menuTypeName;
	/**
	 * 份数
	 */
	private String menuAmount;
	/**
	 * 单价
	 */
	private  String menuPrice;
	/**
	 * 合计
	 */
	private String menuTotal;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 标志
	 */
	private String flag;
	
	// xsTan 追加 2009-1-28 追加更新时间
	/**
	 * 更新时间
	 */
	private String strUpdateTime;
	/**
	 * 
	 */
	private String mId;
	/**
	 * 
	 */
	/**
	 * 
	 * @return 用户ID
	 */
	public String getId() {
		return id;
	}
	/**
	 * 
	 * @param id用户ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 
	 * @return 菜谱编码
	 */
	public String getMenuCode() {
		return menuCode;
	}
	/**
	 * 
	 * @param menuCode 菜谱编码
	 */
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	/**
	 * 
	 * @return  菜谱名称
	 */
	public String getMenuName() {
		return menuName;
	}
	/**
	 * 
	 * @param menuName  菜谱名称
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	/**
	 * 
	 * @return 菜谱类别名称
	 */
	public String getMenuTypeName() {
		return menuTypeName;
	}
	/**
	 * 
	 * @param menuTypeName 菜谱类别名称
	 */
	public void setMenuTypeName(String menuTypeName) {
		this.menuTypeName = menuTypeName;
	}
	/**
	 * 
	 * @return 份数
	 */
	public String getMenuAmount() {
		return menuAmount;
	}
	/**
	 * 
	 * @param menuAmount 份数
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
	 * @param menuTotal  合计
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
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
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
	public String getMId() {
		return mId;
	}
	public void setMId(String id) {
		mId = id;
	}
	
}
