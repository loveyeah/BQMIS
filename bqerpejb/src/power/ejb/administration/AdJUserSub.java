package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJUserSub entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_USER_SUB")
public class AdJUserSub implements java.io.Serializable {

	// Fields

	private Long id;
	private String menuCode;
	private Long MId;
	private Long menuAmount;
	private Double menuPrice;
	private Double menuTotal;
	private String memo;
	private String isUse;
	private String updateUser;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public AdJUserSub() {
	}

	/** minimal constructor */
	public AdJUserSub(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJUserSub(Long id, String menuCode, Long MId, Long menuAmount,
			Double menuPrice, Double menuTotal, String memo, String isUse,
			String updateUser, Date updateTime) {
		this.id = id;
		this.menuCode = menuCode;
		this.MId = MId;
		this.menuAmount = menuAmount;
		this.menuPrice = menuPrice;
		this.menuTotal = menuTotal;
		this.memo = memo;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
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

	@Column(name = "MENU_CODE", length = 6)
	public String getMenuCode() {
		return this.menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	@Column(name = "M_ID", precision = 10, scale = 0)
	public Long getMId() {
		return this.MId;
	}

	public void setMId(Long MId) {
		this.MId = MId;
	}

	@Column(name = "MENU_AMOUNT", precision = 10, scale = 0)
	public Long getMenuAmount() {
		return this.menuAmount;
	}

	public void setMenuAmount(Long menuAmount) {
		this.menuAmount = menuAmount;
	}

	@Column(name = "MENU_PRICE", precision = 13)
	public Double getMenuPrice() {
		return this.menuPrice;
	}

	public void setMenuPrice(Double menuPrice) {
		this.menuPrice = menuPrice;
	}

	@Column(name = "MENU_TOTAL", precision = 13)
	public Double getMenuTotal() {
		return this.menuTotal;
	}

	public void setMenuTotal(Double menuTotal) {
		this.menuTotal = menuTotal;
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

}