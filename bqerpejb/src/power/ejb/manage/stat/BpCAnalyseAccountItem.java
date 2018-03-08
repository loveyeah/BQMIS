package power.ejb.manage.stat;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * BpCAnalyseAccountItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_ANALYSE_ACCOUNT_ITEM")
public class BpCAnalyseAccountItem implements java.io.Serializable {
	private BpCAnalyseAccountItemId id;
	private Long displayNo;
	private String enterpriseCode;
	private String dataType;
	private String itemAlias;
	private String itemBaseName;

	// Constructors

	/** default constructor */
	public BpCAnalyseAccountItem() {
	}

	/** minimal constructor */
	public BpCAnalyseAccountItem(BpCAnalyseAccountItemId id) {
		this.id = id;
	}

	/** full constructor */
	public BpCAnalyseAccountItem(BpCAnalyseAccountItemId id, Long displayNo,
			String enterpriseCode, String dataType, String itemAlias,
			String itemBaseName) {
		this.id = id;
		this.displayNo = displayNo;
		this.enterpriseCode = enterpriseCode;
		this.dataType = dataType;
		this.itemAlias = itemAlias;
		this.itemBaseName = itemBaseName;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "accountCode", column = @Column(name = "ACCOUNT_CODE", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "itemCode", column = @Column(name = "ITEM_CODE", nullable = false, length = 40)) })
	public BpCAnalyseAccountItemId getId() {
		return this.id;
	}

	public void setId(BpCAnalyseAccountItemId id) {
		this.id = id;
	}

	@Column(name = "DISPLAY_NO", precision = 10, scale = 0)
	public Long getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "DATA_TYPE", length = 1)
	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "ITEM_ALIAS", length = 50)
	public String getItemAlias() {
		return this.itemAlias;
	}

	public void setItemAlias(String itemAlias) {
		this.itemAlias = itemAlias;
	}

	@Column(name = "ITEM_BASE_NAME", length = 50)
	public String getItemBaseName() {
		return this.itemBaseName;
	}

	public void setItemBaseName(String itemBaseName) {
		this.itemBaseName = itemBaseName;
	}

}