package power.ejb.administration;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJCarwhList entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "AD_J_CARWH_LIST")
public class AdJCarwhList implements java.io.Serializable {

	// Fields

	private Long id;
	private String whId;
	private String proCode;
	private String partName;
	private String unit;
	private Double num;
	private Double realNum;
	private Double unitPrice;
	private Double realUnitPrice;
	private String note;
	private String isUse;
	private String updateUser;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public AdJCarwhList() {
	}

	/** minimal constructor */
	public AdJCarwhList(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJCarwhList(Long id, String whId, String proCode, String partName,
			String unit, Double num, Double realNum, Double unitPrice,
			Double realUnitPrice, String note, String isUse, String updateUser,
			Date updateTime) {
		this.id = id;
		this.whId = whId;
		this.proCode = proCode;
		this.partName = partName;
		this.unit = unit;
		this.num = num;
		this.realNum = realNum;
		this.unitPrice = unitPrice;
		this.realUnitPrice = realUnitPrice;
		this.note = note;
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

	@Column(name = "PART_NAME", length = 50)
	public String getPartName() {
		return this.partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	@Column(name = "UNIT", length = 12)
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "NUM", precision = 12)
	public Double getNum() {
		return this.num;
	}

	public void setNum(Double num) {
		this.num = num;
	}

	@Column(name = "REAL_NUM", precision = 12)
	public Double getRealNum() {
		return this.realNum;
	}

	public void setRealNum(Double realNum) {
		this.realNum = realNum;
	}

	@Column(name = "UNIT_PRICE", precision = 15, scale = 4)
	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Column(name = "REAL_UNIT_PRICE", precision = 15, scale = 4)
	public Double getRealUnitPrice() {
		return this.realUnitPrice;
	}

	public void setRealUnitPrice(Double realUnitPrice) {
		this.realUnitPrice = realUnitPrice;
	}

	@Column(name = "NOTE", length = 200)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
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