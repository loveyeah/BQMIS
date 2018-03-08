/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * 餐厅计划维护
 * 
 * @author sufeiyu
*/
@SuppressWarnings("serial")
public class RestaurantPlanInfo implements Serializable{
	/**餐厅计划维护**/
	
	/**序号**/
	private Long id;
	/**菜谱编码**/
	private String menuCode;
	/**菜谱名称**/
	private String menuName;
	/**日期**/
	private String planDate;
	/**检索码**/
	private String retrieveCode;
	/**菜谱类别**/
	private String menuType;
	/**类别编码**/
	private String menuTypeCode;
	/**类别名称**/
	private String menuTypeName;
	/**单价**/
	private Double menuPrice;
	/**备注**/
	private String memo;
	/**修改时间**/
	private String updateTime;
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
	 * @return the menuCode
	 */
	public String getmenuCode() {
		return menuCode;
	}
	/**
	 * @param menuCode the menuCode to set
	 */
	public void setmenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	/**
	 * @return the menuName
	 */
	public String getmenuName() {
		return menuName;
	}
	/**
	 * @param menuName the menuName to set
	 */
	public void setmenuName(String menuName) {
		this.menuName = menuName;
	}
	/**
	 * @return the planDate
	 */
	public String getPlanDate() {
		return planDate;
	}
	/**
	 * @param planDate the planDate to set
	 */
	public void setPlanDate(String planDate) {
		this.planDate = planDate;
	}
	/**
	 * @return the retrieveCode
	 */
	public String getRetrieveCode() {
		return retrieveCode;
	}
	/**
	 * @param retrieveCode the retrieveCode to set
	 */
	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}
	/**
	 * @return the menuType
	 */
	public String getMenuType() {
		return menuType;
	}
	/**
	 * @param menuType the menuType to set
	 */
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	/**
	 * @return the menuTypeCode
	 */
	public String getMenuTypeCode() {
		return menuTypeCode;
	}
	/**
	 * @param menuTypeCode the menuTypeCode to set
	 */
	public void setMenuTypeCode(String menuTypeCode) {
		this.menuTypeCode = menuTypeCode;
	}
	/**
	 * @return the menuTypeName
	 */
	public String getMenuTypeName() {
		return menuTypeName;
	}
	/**
	 * @param menuTypeName the menuTypeName to set
	 */
	public void setMenuTypeName(String menuTypeName) {
		this.menuTypeName = menuTypeName;
	}
	/**
	 * @return the menuPrice
	 */
	public Double getMenuPrice() {
		return menuPrice;
	}
	/**
	 * @param menuPrice the menuPrice to set
	 */
	public void setMenuPrice(Double menuPrice) {
		this.menuPrice = menuPrice;
	}
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
