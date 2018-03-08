package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmCCenterUse entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_C_CENTER_USE")
public class CbmCCenterUse implements java.io.Serializable {

	// Fields

	private Long useId;
	private Long itemId;
	private Long centerId;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmCCenterUse() {
	}

	/** minimal constructor */
	public CbmCCenterUse(Long useId) {
		this.useId = useId;
	}

	/** full constructor */
	public CbmCCenterUse(Long useId, Long itemId, Long centerId, String isUse,
			String enterpriseCode) {
		this.useId = useId;
		this.itemId = itemId;
		this.centerId = centerId;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "USE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getUseId() {
		return this.useId;
	}

	public void setUseId(Long useId) {
		this.useId = useId;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "CENTER_ID", precision = 10, scale = 0)
	public Long getCenterId() {
		return this.centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
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

}