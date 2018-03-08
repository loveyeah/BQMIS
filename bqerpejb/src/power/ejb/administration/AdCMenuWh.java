/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdCMenuWh entity.
 * 
 * @author　Li Chensheng
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AD_C_MENU_WH")
public class AdCMenuWh implements java.io.Serializable {

	// Fields
    
	// 流水号
	private Long id;
	// 菜谱编码
	private String menuCode;
	// 菜谱名称
	private String menuName;
	// 配料说明
	private String menuMemo;
	// 菜谱价格
	private Double menuPrice;
	// 检索码
	private String retrieveCode;
	private String isUse;
	// 菜谱类别编码
	private String menutypeCode;
	// 更新者
	private String updateUser;
	// 更新时间
	private Date updateTime;
	// 企业代码
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdCMenuWh() {
	}

	/** minimal constructor */
	public AdCMenuWh(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdCMenuWh(Long id, String menuCode, String menuName,
			String menuMemo, Double menuPrice,
			String retrieveCode, String isUse, String menutypeCode,
			String updateUser, Date updateTime,String enterpriseCode) {
		this.id = id;
		this.menuCode = menuCode;
		this.menuName = menuName;
		this.menuMemo = menuMemo;
		this.menuPrice = menuPrice;
		this.retrieveCode = retrieveCode;
		this.isUse = isUse;
		this.menutypeCode = menutypeCode;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	
	// 流水号取得
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}
	// 流水号设置
	public void setId(Long id) {
		this.id = id;
	}
	// 菜谱编码取得
	@Column(name = "MENU_CODE", length = 6)
	public String getMenuCode() {
		return this.menuCode;
	}
	// 菜谱编码设置
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	// 菜谱名称取得
	@Column(name = "MENU_NAME", length = 50)
	public String getMenuName() {
		return this.menuName;
	}
	// 菜谱名称设置
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	// 配料说明取得
	@Column(name = "MENU_MEMO")
	public String getMenuMemo() {
		return this.menuMemo;
	}
	// 配料说明设置
	public void setMenuMemo(String menuMemo) {
		this.menuMemo = menuMemo;
	}
	// 菜谱价格取得
	@Column(name = "MENU_PRICE", precision = 15, scale = 4)
	public Double getMenuPrice() {
		return this.menuPrice;
	}
	// 菜谱价格设置
	public void setMenuPrice(Double menuPrice) {
		this.menuPrice = menuPrice;
	}
	// 检索码取得
	@Column(name = "RETRIEVE_CODE", length = 6)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}
	// 检索码设置
	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}
	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	// 菜谱类别编码取得
	@Column(name = "MENUTYPE_CODE", length = 2)
	public String getMenutypeCode() {
		return this.menutypeCode;
	}
	// 菜谱类别编码设置
	public void setMenutypeCode(String menutypeCode) {
		this.menutypeCode = menutypeCode;
	}
	// 更新者取得
	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}
	// 更新者设置
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	// 更新时间取得
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}
	// 更新时间设置
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
    // 企业代码
	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}