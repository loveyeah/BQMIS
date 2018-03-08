package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCStatItemRealtime entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_STAT_ITEM_REALTIME")
public class BpCStatItemRealtime implements java.io.Serializable {

	// Fields

	private String itemCode;
	private String nodeCode;
	private String apartCode;
	private String usedefault;
	private Long defaultValue;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCStatItemRealtime() {
	}

	/** minimal constructor */
	public BpCStatItemRealtime(String itemCode) {
		this.itemCode = itemCode;
	}

	/** full constructor */
	public BpCStatItemRealtime(String itemCode, String nodeCode,
			String apartCode, String usedefault, Long defaultValue,
			String enterpriseCode) {
		this.itemCode = itemCode;
		this.nodeCode = nodeCode;
		this.apartCode = apartCode;
		this.usedefault = usedefault;
		this.defaultValue = defaultValue;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ITEM_CODE", unique = true, nullable = false, length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "NODE_CODE", length = 50)
	public String getNodeCode() {
		return this.nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	@Column(name = "APART_CODE", length = 20)
	public String getApartCode() {
		return this.apartCode;
	}

	public void setApartCode(String apartCode) {
		this.apartCode = apartCode;
	}

	@Column(name = "USEDEFAULT", length = 1)
	public String getUsedefault() {
		return this.usedefault;
	}

	public void setUsedefault(String usedefault) {
		this.usedefault = usedefault;
	}

	@Column(name = "DEFAULT_VALUE", precision = 22, scale = 0)
	public Long getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(Long defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}