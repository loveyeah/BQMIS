package power.ejb.manage.stat;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * BpCInputReportItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_INPUT_REPORT_ITEM")
public class BpCInputReportItem implements java.io.Serializable {

	// Fields

	private BpCInputReportItemId id;
	private String itemAlias;
	private String dataType;
	private Long displayNo;
	private String enterpriseCode;
	private String itemBaseName;

	// Constructors

	/** default constructor */
	public BpCInputReportItem() {
	}

	/** minimal constructor */
	public BpCInputReportItem(BpCInputReportItemId id) {
		this.id = id;
	}

	/** full constructor */
	public BpCInputReportItem(BpCInputReportItemId id, String itemAlias,
			String dataType, Long displayNo, String enterpriseCode) {
		this.id = id;
		this.itemAlias = itemAlias;
		this.dataType = dataType;
		this.displayNo = displayNo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "reportCode", column = @Column(name = "REPORT_CODE", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "itemCode", column = @Column(name = "ITEM_CODE", nullable = false, length = 40)) })
	public BpCInputReportItemId getId() {
		return this.id;
	}

	public void setId(BpCInputReportItemId id) {
		this.id = id;
	}

	@Column(name = "ITEM_ALIAS", length = 50)
	public String getItemAlias() {
		return this.itemAlias;
	}

	public void setItemAlias(String itemAlias) {
		this.itemAlias = itemAlias;
	}

	@Column(name = "DATA_TYPE", length = 1)
	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
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

	@Column(name = "ITEM_BASE_NAME", length = 50)
	public String getItemBaseName() {
		return itemBaseName;
	}

	public void setItemBaseName(String itemBaseName) {
		this.itemBaseName = itemBaseName;
	}

}