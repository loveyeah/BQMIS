package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJRestaurantPlan entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AD_J_RESTAURANT_PLAN")
public class AdJRestaurantPlan implements java.io.Serializable {

	/** 获得当前用户ID* */
	private Long id;
	/** 获得菜谱编码* */
	private String memuCode;
	/** 获得日期* */
	private Date planDate;
	/** 获得weekno* */
	private String weekNo;
	/** 获得菜谱类别* */
	private String menuType;
	/** 获得菜谱价格* */
	private Double menuPrice;
	/** 获得备注* */
	private String memo;
	/** 获得使用* */
	private String isUse;
	/** 获得更新人* */
	private String updateUser;
	/** 获得更新时间* */
	private Date updateTime;
	/** 企业代码* */
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public AdJRestaurantPlan() {
	}

	/** minimal constructor */
	public AdJRestaurantPlan(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJRestaurantPlan(Long id, String memuCode, Date planDate,
			String weekNo, String menuType, Double menuPrice, String memo,
			String isUse, String updateUser, Date updateTime,
			String enterpriseCode) {
		this.id = id;
		this.memuCode = memuCode;
		this.planDate = planDate;
		this.weekNo = weekNo;
		this.menuType = menuType;
		this.menuPrice = menuPrice;
		this.memo = memo;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "MEMU_CODE", length = 6)
	public String getMemuCode() {
		return this.memuCode;
	}

	public void setMemuCode(String memuCode) {
		this.memuCode = memuCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_DATE", length = 7)
	public Date getPlanDate() {
		return this.planDate;
	}

	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}

	@Column(name = "WEEK_NO", length = 1)
	public String getWeekNo() {
		return this.weekNo;
	}

	public void setWeekNo(String weekNo) {
		this.weekNo = weekNo;
	}

	@Column(name = "MENU_TYPE", length = 1)
	public String getMenuType() {
		return this.menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	@Column(name = "MENU_PRICE", precision = 13)
	public Double getMenuPrice() {
		return this.menuPrice;
	}

	public void setMenuPrice(Double menuPrice) {
		this.menuPrice = menuPrice;
	}

	@Column(name = "MEMO")
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}