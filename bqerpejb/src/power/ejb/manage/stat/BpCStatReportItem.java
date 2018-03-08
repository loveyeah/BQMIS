package power.ejb.manage.stat;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * BpCStatReportItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_STAT_REPORT_ITEM")
public class BpCStatReportItem implements java.io.Serializable {

	// Fields

	private BpCStatReportItemId id;
	private Long displayNo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCStatReportItem() {
	}

	/** minimal constructor */
	public BpCStatReportItem(BpCStatReportItemId id) {
		this.id = id;
	}

	/** full constructor */
	public BpCStatReportItem(BpCStatReportItemId id, Long displayNo,
			String enterpriseCode) {
		this.id = id;
		this.displayNo = displayNo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "reportCode", column = @Column(name = "REPORT_CODE", nullable = false, precision = 10, scale = 0)),
			@AttributeOverride(name = "itemCode", column = @Column(name = "ITEM_CODE", nullable = false, length = 40)) })
	public BpCStatReportItemId getId() {
		return this.id;
	}

	public void setId(BpCStatReportItemId id) {
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

}