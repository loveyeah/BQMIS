package power.ejb.manage.exam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCCbmAffiliated entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_CBM_AFFILIATED")
public class BpCCbmAffiliated implements java.io.Serializable {

	// Fields

	private Long affiliatedId;
	private Long itemId;
	private String depCode;
	private String affiliatedLevel;
	private Double affiliatedValue;
	private String isUse;
	private String enterpriseCode;
	private Long viewNo;

	// Constructors

	/** default constructor */
	public BpCCbmAffiliated() {
	}

	/** minimal constructor */
	public BpCCbmAffiliated(Long affiliatedId) {
		this.affiliatedId = affiliatedId;
	}

	/** full constructor */
	public BpCCbmAffiliated(Long affiliatedId, Long itemId, String depCode,
			String affiliatedLevel, Double affiliatedValue, String isUse,
			String enterpriseCode, Long viewNo) {
		this.affiliatedId = affiliatedId;
		this.itemId = itemId;
		this.depCode = depCode;
		this.affiliatedLevel = affiliatedLevel;
		this.affiliatedValue = affiliatedValue;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.viewNo = viewNo;
	}

	// Property accessors
	@Id
	@Column(name = "AFFILIATED_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAffiliatedId() {
		return this.affiliatedId;
	}

	public void setAffiliatedId(Long affiliatedId) {
		this.affiliatedId = affiliatedId;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "DEP_CODE", length = 400)
	public String getDepCode() {
		return this.depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	@Column(name = "AFFILIATED_LEVEL", length = 1)
	public String getAffiliatedLevel() {
		return this.affiliatedLevel;
	}

	public void setAffiliatedLevel(String affiliatedLevel) {
		this.affiliatedLevel = affiliatedLevel;
	}

	@Column(name = "AFFILIATED_VALUE", precision = 10)
	public Double getAffiliatedValue() {
		return this.affiliatedValue;
	}

	public void setAffiliatedValue(Double affiliatedValue) {
		this.affiliatedValue = affiliatedValue;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "VIEW_NO", precision = 10, scale = 0)
	public Long getViewNo() {
		return viewNo;
	}

	public void setViewNo(Long viewNo) {
		this.viewNo = viewNo;
	}

}