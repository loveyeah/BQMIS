package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJRoomPrice entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_ROOM_PRICE")
public class AdJRoomPrice implements java.io.Serializable {

	// Fields

	private Long id;
	private String roomTypeCode;
	private String roomTypeName;
	private Double price;
	private String updateUser;
	private Date updateTime;
	private String isUse;

	// Constructors

	/** default constructor */
	public AdJRoomPrice() {
	}

	/** minimal constructor */
	public AdJRoomPrice(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJRoomPrice(Long id, String roomTypeCode, String roomTypeName,
			Double price, String updateUser, Date updateTime, String isUse) {
		this.id = id;
		this.roomTypeCode = roomTypeCode;
		this.roomTypeName = roomTypeName;
		this.price = price;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.isUse = isUse;
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

	@Column(name = "ROOM_TYPE_CODE", length = 2)
	public String getRoomTypeCode() {
		return this.roomTypeCode;
	}

	public void setRoomTypeCode(String roomTypeCode) {
		this.roomTypeCode = roomTypeCode;
	}

	@Column(name = "ROOM_TYPE_NAME", length = 20)
	public String getRoomTypeName() {
		return this.roomTypeName;
	}

	public void setRoomTypeName(String roomTypeName) {
		this.roomTypeName = roomTypeName;
	}

	@Column(name = "PRICE", precision = 15, scale = 4)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "UPDATE_USER", length = 6)
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

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}