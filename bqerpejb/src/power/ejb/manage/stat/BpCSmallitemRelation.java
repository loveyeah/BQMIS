package power.ejb.manage.stat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "BP_C_SMALLITEM_RELATION")
public class BpCSmallitemRelation implements java.io.Serializable {
	private Long id;
	private Long reportId;
	private String itemCode;
	private String itemAlias;
	private String dataType; 
	// private Long rowDatatypeId;
	private Long orderBy;
	private BpCSmallitemRowtype bpCSmallitemRowtype;
//	private String isItem;
	private String isIgnoreZero;
	private String compluteMethod;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "row_datatype_id")
	public BpCSmallitemRowtype getBpCSmallitemRowtype() {
		return bpCSmallitemRowtype;
	}

	public void setBpCSmallitemRowtype(BpCSmallitemRowtype bpCSmallitemRowtype) {
		this.bpCSmallitemRowtype = bpCSmallitemRowtype;
	}

	// Constructors

	/** default constructor */
	public BpCSmallitemRelation() {
	}

	/** minimal constructor */
	public BpCSmallitemRelation(Long id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "REPORT_ID", precision = 10, scale = 0)
	public Long getReportId() {
		return this.reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	@Column(name = "ITEM_CODE", length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "ITEM_ALIAS", length = 100)
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

	// @Column(name = "ROW_DATATYPE_ID", precision = 10, scale = 0)
	// public Long getRowDatatypeId() {
	// return this.rowDatatypeId;
	// }
	//
	// public void setRowDatatypeId(Long rowDatatypeId) {
	// this.rowDatatypeId = rowDatatypeId;
	// }

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}
//	@Column(name = "IS_ITEM", length = 1)
//	public String getIsItem() {
//		return isItem;
//	}
//
//	public void setIsItem(String isItem) {
//		this.isItem = isItem;
//	}
	@Column(name = "IS_IGNORE_ZERO", length = 1)
	public String getIsIgnoreZero() {
		return isIgnoreZero;
	}

	public void setIsIgnoreZero(String isIgnoreZero) {
		this.isIgnoreZero = isIgnoreZero;
	}
	@Column(name = "COMPLUTE_MEHTOD", length = 3)
	public String getCompluteMethod() {
		return compluteMethod;
	}

	public void setCompluteMethod(String compluteMethod) {
		this.compluteMethod = compluteMethod;
	}

}