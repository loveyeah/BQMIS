package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJCarwhMx entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_CARWH_MX")
public class AdJCarwhMx implements java.io.Serializable {

	// Fields

	private Long id;
	private String whId;
	private String proCode;
	private Double price;
	private Double realPrice;
	private String isUse;
	private String updateUser;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public AdJCarwhMx() {
	}

	/** minimal constructor */
	public AdJCarwhMx(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJCarwhMx(Long id, String whId, String proCode, Double price,
			Double realPrice, String isUse, String updateUser, Date updateTime) {
		this.id = id;
		this.whId = whId;
		this.proCode = proCode;
		this.price = price;
		this.realPrice = realPrice;
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

	@Column(name = "WH_ID", length = 12)
	public String getWhId() {
		return this.whId;
	}

	public void setWhId(String whId) {
		this.whId = whId;
	}

	@Column(name = "PRO_CODE", length = 4)
	public String getProCode() {
		return this.proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	@Column(name = "PRICE", precision = 16, scale = 4)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "REAL_PRICE", precision = 16, scale = 4)
	public Double getRealPrice() {
		return this.realPrice;
	}

	public void setRealPrice(Double realPrice) {
		this.realPrice = realPrice;
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