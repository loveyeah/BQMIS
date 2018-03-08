package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCSmallitemRowtype entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_SMALLITEM_ROWTYPE")
public class BpCSmallitemRowtype implements java.io.Serializable {

	// Fields

	private Long rowDatatypeId;
	private Long reportId;
	private String rowDatatypeName;
	private Long orderBy;
	private String isItem;

	// Constructors

	/** default constructor */
	public BpCSmallitemRowtype() {
	}

	/** minimal constructor */
	public BpCSmallitemRowtype(Long rowDatatypeId) {
		this.rowDatatypeId = rowDatatypeId;
	}

	/** full constructor */
	public BpCSmallitemRowtype(Long rowDatatypeId, Long reportId,
			String rowDatatypeName, Long orderBy) {
		this.rowDatatypeId = rowDatatypeId;
		this.reportId = reportId;
		this.rowDatatypeName = rowDatatypeName;
		this.orderBy = orderBy;
	}

	// Property accessors
	@Id
	@Column(name = "ROW_DATATYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRowDatatypeId() {
		return this.rowDatatypeId;
	}

	public void setRowDatatypeId(Long rowDatatypeId) {
		this.rowDatatypeId = rowDatatypeId;
	}

	@Column(name = "REPORT_ID", precision = 10, scale = 0)
	public Long getReportId() {
		return this.reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	@Column(name = "ROW_DATATYPE_NAME", length = 20)
	public String getRowDatatypeName() {
		return this.rowDatatypeName;
	}

	public void setRowDatatypeName(String rowDatatypeName) {
		this.rowDatatypeName = rowDatatypeName;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}
	@Column(name = "IS_ITEM", length = 1)
	public String getIsItem() {
		return isItem;
	}

	public void setIsItem(String isItem) {
		this.isItem = isItem;
	}

}